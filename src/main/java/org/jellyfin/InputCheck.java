package org.jellyfin;

//TODO add proper way to format .srt files

public class InputCheck {
  /**
  *Run all parsers for the input String
   * Used in TerminalCommands.changeName()
  * @param input = file/directory local name
  * @param isFile = True if input is the name of a file
  * @return the file name which has been stripped of all unnecessary junk, ie
   * Violet Evergarden[BD][1080p][HEVC 10bit x265][Dual Audio][Tenrai-Sensei] -> Violet Evergarden
  */
  protected String runAllParsers(String input, boolean isFile, boolean isEpisode) {
    String fileExtension = "";
    //Remove file extension only if the input is a file name. For directories skip
    if (isFile) {
      // delete file extension from input and save it as its own variable so it can be added in the end
      fileExtension = getFileExtension(input);
      //if the file is a subtitle file do not make changes because jellyfin uses '.' in file extensions
      if (fileExtension.equals(".srt")) {
        return input;
      }
      input = removeFileExtension(input);
    }
    String seasonEpisode = "";

    // Run the parsers
    input = changeDotsToSpaces(input);
    if (isEpisode)  {
      seasonEpisode = getSeasonEpisode(input);
      input = deleteYear(input);
    } else {
      input = parenthesisTheYear(input);
    }
    input = removeTags(input);
    //input = removeNonimdbTags(input);
    //input = deleteRestOfTags(input);
    input = deleteLastSpace(input);
    if (input.contains(seasonEpisode) == false) {
      input = input + ' ' + seasonEpisode;
    }
    return input + fileExtension;
  }

  /**
   * Get Season and Episode from show episode file
   * @param path local name of file
   * @return Return season and episode tag -> SxxExx
   */
  private String getSeasonEpisode(String path) {
    String pathLowerCase = path.toLowerCase();
    String season = null;
    String episode = null;
    String output = "";

    int count = 0;
    while (count < pathLowerCase.length())  {
      if (pathLowerCase.charAt(count) == 's') {
        if (Character.isDigit(path.charAt(count + 1)) && Character.isDigit(path.charAt(count + 2))) {
          season = path.substring(count, count + 3);
        }
      }
      if (pathLowerCase.charAt(count) == 'e') {
        if (Character.isDigit(path.charAt(count + 1)) && Character.isDigit(path.charAt(count + 2))) {
          episode = path.substring(count, count + 3);
          break;
        }
      }
      count++;
    }

    if (season != null && episode != null)  {
      output = season + episode;
    }
    return output;
  }
  /**
  * This code should only be run for files. It will throw an error for directories always
  * Used in:
  * InputCheck.runAllParsers()
  * @param input = file local name
  * @return the file extension, ie .mp4
  * */
  protected String getFileExtension(String input) {
    return input.substring(input.lastIndexOf('.'));
  }
  /**
  * This code should only be run for files
  * used in:
  * InputCheck.runAllParsers()
  * @param input = file local name
  * @return the file name without the file extension, ie movie.mp4 -> movie
  * */
  private String removeFileExtension(String input) {
    if (input.lastIndexOf('.') == -1)  {
      throw new RuntimeException("File " + input + " is missing file extension or java thinks it's a directory");
    }
    return input.substring(0,input.lastIndexOf('.'));
  }
  /**
  * Used to check if the year in input has one of the specified chars enclosing the year. ie {2024} or [2024]
  * Used in:
  * InputCheck.parenthesisTheYear()
  * @param input = file/dir local name
  * @param startIndex = index of the millenium number of the year
  * @return true if input has both specified chars at the start and end of the year
  * */
  private boolean hasSpecifiedChar(String input, int startIndex) {
    boolean hasFirstSpecifiedChar =
            switch (input.charAt(startIndex - 1)) {
              case ' ' -> true;
              case '[' -> true;
              case '{' -> true;
              default -> false;
            };
    boolean hasSecondSpecifiedChar;
    if (input.length()-1 <= startIndex+4)  {
      hasSecondSpecifiedChar = true;
    } else {
      hasSecondSpecifiedChar =
            switch (input.charAt(startIndex + 4)) {
              case ' ' -> true;
              case ']' -> true;
              case '}' -> true;
              default -> false;
      };
    }
    return hasFirstSpecifiedChar && hasSecondSpecifiedChar;
  }
  /**
   * Used in:
   * InputCheck.runAllParsers()
   * @param input local file/dir name
   * @return file/dir name where the name dots have been changed to spaces, ie movie.name -> movie name
   */
  private String changeDotsToSpaces(String input) {
    String output = input;
    output = output.replace('.', ' ');
    return output;
  }
  /**
   * Because some parsers sometimes leaves a space as a last character this method must be used.
   * Used in:
   * InputCheck.runAllParsers
   * @param input = local file/dir name
   * @return file/dir name with the last character not being space, ie "movie " -> "movie"
   */
  private String deleteLastSpace(String input) {
    String output = input;
    if (input == "")  {
      System.out.println();
    }
    if (output.charAt(output.length() - 1) == ' ') {
      output = output.substring(0, output.length() - 1);
    }
    return output;
  }
  private String removeTags(String path)  {
    String year = null;
    String idTag = null;
    StringBuilder output = new StringBuilder(path);
    int endpoint = 0;
    int startIndex = output.indexOf("[");
    int endIndex = output.indexOf("]");
    while (startIndex != -1)  {
      String possibleTag = output.substring(startIndex, endIndex + 1);
      //Try
      try {
        if (possibleTag.substring(1, 3).equals("tt")) {
          idTag = possibleTag;
        }
        else if (possibleTag.substring(1,7).equals("tmdbid") || possibleTag.substring(1,7).equals("tvdbid")) {
          idTag = possibleTag;
        }
      } catch (IndexOutOfBoundsException e) {
        //should correct itself when the possible tag  is deleted
      }
      output.delete(startIndex, endIndex + 1);
      startIndex = output.indexOf("[");
      endIndex = output.indexOf("]");
    }
    startIndex = output.indexOf("(");
    endIndex = output.indexOf(")");
    while (startIndex != -1)  {
      String possibleYear = output.substring(startIndex, endIndex + 1);
      try {
        if (isYear(possibleYear.substring(1, 5))) {
          year = possibleYear;
          output.delete(endIndex + 1, output.length());
          break;
        }
      } catch (IndexOutOfBoundsException e) {
        //should correct itself when the possible tag  is deleted
      }
      output.delete(startIndex, endIndex + 1);
      startIndex = output.indexOf("(");
      endIndex = output.indexOf(")");
    }
    if (idTag != null)  {
      output.append(" ");
      output.append(idTag);

    }
    return output.toString();
  }
  /**
   * InputCheck.runAllParsers()
   * @param input = file/dir local name
   * @return file/dir name which has had all the tags removed i.e. movie [zmovies] [tt100123] -> movie [tt100123]
   */
  private String removeNonimdbTags(String input) {
    StringBuilder output = new StringBuilder(input);
    int startIndex = output.indexOf("[");
    int endIndex = output.indexOf("]");

    String idTag = null;
    while(startIndex != -1)  {
      //checks if tag is imdb id tag. If so skips it
      String possibleTag = output.substring(startIndex, startIndex + 3);
      if (possibleTag.equals("[tt") || possibleTag.equals("tmdbid-") || possibleTag.equals("tvdbid-"))  {
        idTag = output.substring(startIndex, endIndex + 1);
      }
      output.delete(startIndex, endIndex + 1);
      startIndex = output.indexOf("[");
      endIndex = output.indexOf("]");
    }
    if (idTag != null)  {
      output.append(" ");
      output.append(idTag);
    }
    return output.toString();
  }
  /**
   * Used in:
   * InputCheck.runAllParcers
   * @param input = local file/dir name
   * @return file/dir name with the year parenthesised, ie movie 2024 -> movie (2024)
   */
  private String parenthesisTheYear(String input) {
    String originalInput = input;
    StringBuilder output = new StringBuilder(input);
    char millenium = '1';
    int startIndex = 1;
    //checks that it's possible for input to still have a 4 digit year
    while (startIndex < input.length() - 3) {
      if (isYear(input.substring(startIndex, startIndex + 4)))  {
        if (hasSpecifiedChar(input, startIndex))  {
          output.insert(startIndex, '(');
          output.insert(startIndex + 5, ')');

          return output.toString();
        }
      }
      if (input.charAt(startIndex) == millenium) {
        startIndex++;
      }
      startIndex = input.indexOf(millenium, startIndex);

      if (millenium == '1') {
        if (startIndex == -1 || startIndex > input.length() - 3)  {
          input = originalInput;
          millenium = '2';
          startIndex = 1;
          startIndex = input.indexOf(millenium, startIndex);
          if (startIndex == -1) {
            return output.toString();
          }
        }
      } else if (startIndex == -1)  {
        return  output.toString();
      }
    }
    return  output.toString();
  }
  /**
   * Used in:
   * InputCheck.parenthesisTheYear
   * @param input = four character long input
   * @return true if input is a year in the format yyyy
   */
  private boolean isYear(String input)  {
    //these specific checks for index 0 and 1 are used because they reduce the amount of false positives with movie titles
    if (input.charAt(0) == '2' && input.charAt(1) == '0') {
      return Character.isDigit(input.charAt(2)) && Character.isDigit(input.charAt(3));
    } else if (input.charAt(0) == '1' && input.charAt(1) == '9')  {
      return Character.isDigit(input.charAt(2)) && Character.isDigit(input.charAt(3));
    } else {
      return false;
    }
  }
  /**
   * This method removes all the tags
   * @param input local file/dir name
   * @return the input with all the tags removed
   */
  private String deleteRestOfTags(String input) {
    StringBuilder output = new StringBuilder(input);
    int startIndex = input.indexOf('(');
    int endIndex = input.indexOf(')');
    String year = null;
    //This code could be better and clearer
    while (startIndex != -1) {
      if (isYear(input.substring(startIndex + 1)))  {
          year = input.substring(startIndex, startIndex + 5);
          output.delete(startIndex, endIndex + 1);
      }

      startIndex = output.indexOf("(");
      endIndex = output.indexOf(")");
    }
    if (year != null) {
      output.append(year);
    }
    return output.toString();
  }


  private String deleteYear(String path)  {
    StringBuilder output = new StringBuilder(path);
    int count = 1;
    while (count < path.length() - 3) {
      if (isYear(path.substring(count, count + 4))) {
        if (hasSpecifiedChar(path, count - 1))  {output.delete(count - 1, count + 5);
        } else if (path.charAt(count - 1) == '(' && path.charAt(count + 4) == ')') {output.delete(count - 2, count + 5);
        } else {output.delete(count, count+3);}
        break;
      }
      count++;
    }
    return output.toString();
  }

}

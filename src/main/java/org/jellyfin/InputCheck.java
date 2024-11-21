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
  protected String runAllParsers(String input, boolean isFile) {
    String fileExtension = "";
    //Remove file extension only if the input is a file name. For directories skip
    if (isFile == true) {
      // delete file extension from input and save it as its own variable so it can be added in the end
      fileExtension = getFileExtension(input);
      //if the file is a subtitle file do not make changes because jellyfin uses '.' in file extensions
      if (fileExtension.equals(".srt")) {
        return input;
      }

      input = removeFileExtension(input);
    }
    // Run the rest of the parsers
    input = changeDotsToSpaces(input);
    input = parenthesisTheYear(input);
    input = removeNonimdbTags(input);
    input = deleteRestOfTags(input);
    input = deleteLastSpace(input);
    return input + fileExtension;
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
  /**
   * InputCheck.runAllParsers()
   * @param input = file/dir local name
   * @return file/dir name which has had all the tags removed i.e. movie [zmovies] [tt100123] -> movie [tt100123]
   */
  private String removeNonimdbTags(String input) {
    int startIndex = 0;
    //redundant
    StringBuilder output = new StringBuilder(input);
    while(output.substring(startIndex).contains("["))  {
      startIndex = output.indexOf("[");
      //checks if tag is imdb id tag. If so skips it
      String possibleTag = output.substring(startIndex, startIndex + 3);
      if (possibleTag.equals("[tt") || possibleTag.equals("tmdbid-") || possibleTag.equals("tvdbid-"))  {
        startIndex = output.indexOf("]") + 1;
      }
      int endIndex = output.substring(startIndex).indexOf("]") + 1 + startIndex;
      //delete tag
      output.delete(startIndex, endIndex);
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
    if (input.charAt(0) == '2' || input.charAt(0) == '1') {
      if (input.charAt(1) == '9' || input.charAt(1) == '0') {
          return Character.isDigit(input.charAt(2)) && Character.isDigit(input.charAt(3));
      }
    }
    return false;
  }
  /**
   * This method removes all the tags
   * @param input local file/dir name
   * @return the input with all the tags removed
   */
  private String deleteRestOfTags(String input) {
    int startIndex = 1;
    int endIndex = 2;
    int year = 0;
    //This code could be better and clearer
    while (endIndex < input.length()) {
      if (input.charAt(startIndex) == '(') {
        try {
          year = Integer.parseInt(input.substring(startIndex + 1, endIndex + 4));
        } catch (NumberFormatException e) {
          //TODO
        }
      }
      startIndex++;
      endIndex = startIndex + 1;
      if (year != 0) {
        break;
      }
    }

    endIndex = input.indexOf(')');
    //This will check if the imbd tag is before or after the year
    if (endIndex < input.indexOf(']'))  {
      endIndex = input.indexOf(']');
    }
      if (endIndex != -1) {
          input = input.substring(0, endIndex + 1);
      }
      return input;
  }

}

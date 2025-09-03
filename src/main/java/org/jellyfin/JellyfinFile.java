package org.jellyfin;

import java.io.File;

public class JellyfinFile extends File {
  private String title = "";
  private String fileExtension = "";
  private String releaseYear = "";
  private String seasonEpisode = "";
  private String DBTag = "";

    public JellyfinFile(String pathToFile) {
    super(pathToFile);
    if (isFile()) {
      setFileExtension();
    }
  }

    /**
     * Rename the file to comply with jellyfin naming scheme
     *
     * @return the absolutpath of the new renamed file or the old name if the file
     *         could not be renamed
     */
    public String renameToJellyfinFormat() {
        setReleaseYear();
        setTitle();
        setDBTag();

        StringBuilder newName = new StringBuilder();
        newName.append(getParent());
        newName.append("/");
        newName.append(title);
        if (isEpisode()) {
            setSeasonEpisode();
            newName.append(seasonEpisode);
        } else  {
            newName.append(releaseYear);
            newName.append(DBTag);
        }
        newName.append(fileExtension);
        String oldName = getName();

        if (getAbsolutePath().equals(newName.toString())) {
            return getAbsolutePath();
        }
        File renamedFile = new File(newName.toString());
        if (renameTo(renamedFile)) {
            System.out.println(oldName + " renamed to " + newName);
            return newName.toString();
        } else {
            System.out.println("Failed to rename " + oldName);
            return getAbsolutePath();
        }
    }

    /**
   * @return true if file is of type .srt
   */
  public boolean isSRTFile() {
    if (isFile()) {
        return fileExtension.equals(".srt");
    }
    return false;
  }

  /**
   * @return true if parent folder is a season folder i.e.
   *         /path/to/media/season/thisFile.txt
   */
  public boolean isEpisode() {
    if (getParentFile() == null) {
      return false;
    }
    return getParentFile().getName().toLowerCase().contains("season");
  }

  /**
   * @return true if file is of "useless" type i.e. .ext, .txt, .jpg, .png
   */
  public boolean isUselessFile() {
    if (isFile()) {
      String[] formats = { ".exe", ".txt", ".jpg", ".png" };
      for (String format : formats) {
        if (fileExtension.equals(format)) {
          return true;
        }
      }
    }
    return false;
  }


  /**
   * Set te title variable
   * if the file is an episode the title would be named after the name of the
   * parent files parent ie the name of the show
   * /path/to/jellyffin/showName/season 01/Showname s01E01 episode name
   * Here the title would be named showName
   */
  private void setTitle() {
    if (isEpisode()) {
      title = getParentFile().getParentFile().getName();
    }  else if (isFile()){
        title = getName();
        title = title.substring(0,title.lastIndexOf('.'));

        }
    else {
        title = getName();
    }
      removeUselessTagsFromTitle();
      title = title.replace('.', ' ');
    while (title.charAt(title.length() -1) == ' ')  {
        title = title.substring(0, title.length() - 1);
    }

  }

  /**
   * remove useless tags from a file name i.e.
   * A movie title (torrent site) [hdtv] [720p] {genre} --> A movie title
   */
  private void removeUselessTagsFromTitle() {
      int count = 0;
      boolean hasForbiddenChar = false;
      while (count < title.length())    {
          int cutIndex = count;
          int initialSize = title.length();
          switch (title.charAt(count))  {
              case '(':
                  hasForbiddenChar = true;
                  cutIndex = title.indexOf(')');
                  break;
              case'[':
                  hasForbiddenChar = true;
                  cutIndex = title.indexOf(']');
                  break;
              case'{':
                  hasForbiddenChar = true;
                  cutIndex = title.indexOf('}');
                  break;
                  //This is used to check for non-matching forbidden characters
              case ')',']','}':
                  hasForbiddenChar = true;
                  cutIndex = count;
                  break;
              default:
                  hasForbiddenChar = false;

          }
          if (hasForbiddenChar)    {
              if (cutIndex != -1)   {
                  String beforeCut = title.substring(0, count);
                  String afterCut = title.substring(cutIndex + 1);
                  title = beforeCut + afterCut;
                  if (title.length() > initialSize)  {
                      throw new RuntimeException("Please manually rename: " + title + " and rerun the program");
                  }
              } else {
                  String beforeCut = title.substring(0, count);
                  String afterCut = title.substring(count + 1);
                  title = beforeCut + afterCut;
                  if (title.length() > initialSize)  {
                      throw new RuntimeException("Please manually rename: " + title + " and rerun the program");
                  }
              }

          } else {
              count++;
          }
      }

  }



  /**
   * set database tag to variable ie get imdb, tmdbid or tvdbid tag from name if
   * one exists.
   */
  private void setDBTag() {
    String localName = getName();
    int index = 1;
    StringBuilder tag = new StringBuilder();
    while (index < localName.length() - 8) {
      char currentChar = localName.charAt(index);
      if (currentChar == '[') {
        if (localName.substring(index, index + 3).equals("[tt")) {
          tag.append(' ');
          while (currentChar != ']') {
            currentChar = localName.charAt(index);
            tag.append(currentChar);
            index++;
          }
          break;
        } else if (localName.substring(index, index + 7).equals("[tmdbid")) {
          tag.append(' ');
          while (currentChar != ']') {
            currentChar = localName.charAt(index);
            tag.append(currentChar);
            index++;
          }
          break;
        } else if (localName.substring(index, index + 7).equals("[tvdbid")) {
          tag.append(' ');
          while (currentChar != ']') {
            currentChar = localName.charAt(index);
            tag.append(currentChar);
            index++;
          }
          break;
        }
      }
      index++;
    }
    DBTag = tag.toString();
  }

  /**
   * Set releaseYear variable if there is a year in the name of the file
   */
  private void setReleaseYear() {
    String localName = getName();
    int index = 1;
    while (index < localName.length() - 3) {
      if (isYear(localName.substring(index, index + 4))) {
        releaseYear = " (" + localName.substring(index, index + 4) + ")";
          return;
      } else {
        index++;
      }
    }
    //Checks if the folder of the movie has release year
      if (isFile()) {
          localName = getParent();
          index = 1;
          while (index < localName.length() - 3) {
              if (isYear(localName.substring(index, index + 4))) {
                  releaseYear = " (" + localName.substring(index, index + 4) + ")";
                  title = title + releaseYear;
                  return;
              } else {
                  index++;
              }
          }
      }


  }

  /**
   * Checks for years between 1800-2099
   * 
   * @param input = four character long input
   * @return true if input is a year in the format yyyy
   */
  private boolean isYear(String input) {
    String prefix = input.substring(0, 2);
    switch (prefix) {
      case "20", "19", "18":
        return Character.isDigit(input.charAt(2)) && Character.isDigit(input.charAt(3));
      default:
        return false;
    }
  }

  /**
   * get file extension and set it to a variable i.e. .mp4, .mkv etc
   */
  private void setFileExtension() {
    String localName = getName();
    int index = localName.lastIndexOf('.');
    if (index != -1) {
      fileExtension = localName.substring(localName.lastIndexOf('.'));
    }
  }

  /**
   * Set Season and Episode tag from episode file name
   * Set season and episode tag -> SxxExx with x being 0-9 -> S01E23
   */
  private void setSeasonEpisode() {
    String localName = getName();
    String pathLowerCase = localName.toLowerCase();
    String season = null;
    String episode = null;
    int count = 0;
    while (count + 2 < pathLowerCase.length()) {
        boolean hasCorrectDigits = Character.isDigit(localName.charAt(count + 1)) && Character.isDigit(localName.charAt(count + 2));
        if (pathLowerCase.charAt(count) == 's') {
        if (hasCorrectDigits) {
          season = localName.substring(count, count + 3);
        }
      }
      if (pathLowerCase.charAt(count) == 'e') {
        if (hasCorrectDigits) {
          episode = localName.substring(count, count + 3);
          break;
        }
      }
      count++;
    }
    if (season != null && episode != null) {
      seasonEpisode = " " + season.toUpperCase() + episode.toUpperCase();
    }
  }
}

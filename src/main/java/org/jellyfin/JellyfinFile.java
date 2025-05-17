package org.jellyfin;

import java.io.File;
import java.util.LinkedList;

public class JellyfinFile extends File {
  private String title = "";
  private String fileExtension = "";
  private String releaseYear = "";
  private String seasonEpisode = "";
  private String DBTag = "";
  private int indexOfYear;

  public JellyfinFile(String pathToFile) {
    super(pathToFile);
    if (isFile()) {
      setFileExtension();
    }
  }

  /**
   * @return true if file is of type .srt
   */
  public boolean isSRTFile() {
    if (isFile()) {
      if (fileExtension.equals(".srt")) {
        return true;
      }
    }
    return false;
  }

  /**
   * @return true if parent folder is a season folder ie.
   *         /path/to/media/season/thisFile.txt
   */
  public boolean isEpisode() {
    if (getParentFile() == null) {
      return false;
    }
    return getParentFile().getName().toLowerCase().contains("season");
  }

  /**
   * @return true if file is of "useless" type ie. .ext, .txt, .jpg, .png
   */
  public boolean isUselessFile(LinkedList<String> uselessFileFormats) {
    if (isFile()) {
      for (String format : uselessFileFormats) {
        if (fileExtension.equals(format)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Rename the file to comply with jellyfin naming scheme
   *
   * @return the absolutpath of the new renamed file or the old name if the file
   *         could not be renamed
   */
  public String getJellyfinFormatName() {
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
    } else {
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
   * Set te title variable
   * if the file is an episode the title would be named after the name of the
   * parent files parent ie the name of the show
   * /path/to/jellyffin/showName/season 01/Showname s01E01 episode name
   * Here the title would be named showName
   */
  private void setTitle() {
    if (isEpisode()) {
      title = getParentFile().getParentFile().getName();
      removeUselessTagsFromTitle();
      return;
    }
    if (isDirectory())  {
      if (formatSeasonTagOfDir() != true) {
        title = getName();
      }
    } else {
      title = getName().substring(0, getName().lastIndexOf('.'));
    }
    removeUselessTagsFromTitle();
    title = title.replace('.', ' ');
    if (title.charAt(title.length() - 1) == ' ') {
      title = title.substring(0, title.length() - 1);
    }

  }

  private boolean formatSeasonTagOfDir()  {
    String localName = getName();
    String pathLowerCase = localName.toLowerCase();
    String season = null;
    int count = 0;
    while (count + 2 < pathLowerCase.length()) {
      if (pathLowerCase.charAt(count) == 's') {
        if (Character.isDigit(localName.charAt(count + 1)) && Character.isDigit(localName.charAt(count + 2))) {
          season = localName.substring(count + 1, count + 3);
          break;
        }
      }
      count++;
    }
    if (season != null) {
      title = getName().substring(0,count) + "Season " + season;
      return true;
    } else {
      return false;
    }
  }

  /**
   * remove useless tags from a file name ie.
   * A movie title (torrent site) [hdtv] [720p] {genre} --> A movie title
   */
  private void removeUselessTagsFromTitle() {
    if (indexOfYear != 0 && indexOfYear < title.length()) {
      title = title.substring(0, indexOfYear);
    }
    int indexOfChar = title.indexOf('(');
    if (indexOfChar != -1) {
      title = title.substring(0, indexOfChar);
    }
    indexOfChar = title.indexOf('[');
    if (indexOfChar != -1) {
      title = title.substring(0, indexOfChar);
    }
    indexOfChar = title.indexOf('{');
    if (indexOfChar != -1) {
      title = title.substring(0, indexOfChar);
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
        if (localName.substring(index, index + 4) == "[tt") {
          tag.append(' ');
          while (currentChar != ']') {
            currentChar = localName.charAt(index);
            tag.append(currentChar);
            index++;
          }
          break;
        } else if (localName.substring(index, index + 8) == "[tmdbid") {
          tag.append(' ');
          while (currentChar != ']') {
            currentChar = localName.charAt(index);
            tag.append(currentChar);
            index++;
          }
          break;
        } else if (localName.substring(index, index + 8) == "[tvdbid") {
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
        indexOfYear = index;
        break;
      } else {
        index++;
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
   * get file extension and set it to a variable ie. .mp4, .mkv etc
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
      if (pathLowerCase.charAt(count) == 's') {
        if (Character.isDigit(localName.charAt(count + 1)) && Character.isDigit(localName.charAt(count + 2))) {
          season = localName.substring(count, count + 3);
        }
      }
      if (pathLowerCase.charAt(count) == 'e') {
        if (Character.isDigit(localName.charAt(count + 1)) && Character.isDigit(localName.charAt(count + 2))) {
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
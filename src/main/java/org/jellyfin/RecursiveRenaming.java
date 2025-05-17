package org.jellyfin;

import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;

public class RecursiveRenaming {


  /**
   * starts the CLI program and start to requirsivly rename files
   *
   * @param pathToJellyfinDir      absolute path to the directory where to start
   *                               renaming

   * @return returns true if pathToJellyfinDir is valide. Else retuns false
   */
  public static boolean runProgram(String pathToJellyfinDir) {
    System.out.println('\n' +
            "       _  __  __  _____     _                                    _               " + '\n' +
            "      | ||  \\/  ||  __ \\   (_)                                  (_)              " + '\n' +
            "      | || \\  / || |__) |   _  ___    _ __  _   _  _ __   _ __   _  _ __    __ _ " + '\n' +
            "  _   | || |\\/| ||  _  /   | |/ __|  | '__|| | | || '_ \\ | '_ \\ | || '_ \\  / _` |" + '\n' +
            " | |__| || |  | || | \\ \\   | |\\__ \\  | |   | |_| || | | || | | || || | | || (_| |" + '\n' +
            "  \\____/ |_|  |_||_|  \\_\\  |_||___/  |_|    \\__,_||_| |_||_| |_||_||_| |_| \\__, |" + '\n' +
            "                                                                            __/ |" + '\n' +
            "                                                                           |___/ " + '\n');

    JellyfinFile mediaRootDir = new JellyfinFile(pathToJellyfinDir);

    if (mediaRootDir.isDirectory()) {
      recursiveRename(mediaRootDir);
      return true;
    } else {
      System.out.println(mediaRootDir + " is not a directory");
      return false;
    }
  }

  /**
   * gets previosly renamed files from a log.txt file by reading it and placing
   * the names into a hashSet for quick lookup
   *
   * @param file log.txt file
   * @return HashSet of all of the names in the log.txt file if there is an
   *         exception catched the returned HashSet may be empty or missing items.
   */
  private static HashSet<String> getPrevioslyRenamedFiles(File file) {
    HashSet<String> namedFiles = new HashSet<>();
    if (file != null) {
      try {
        Scanner reader = new Scanner(file);
        while (reader.hasNextLine()) {
          namedFiles.add(reader.nextLine());
        }
        reader.close();
      } catch (Exception e) {
        System.out.println("A error occured while trying to get previoslyRenamedFiles from " + file.getAbsolutePath());
      }
    }
    return namedFiles;
  }

  /*
   * step1 get current path
   * step2 get subdirectories and files from root path
   * step3 delete useless files
   * step4 rename file/dir
   * step5 if the currentFile is a directory add it to the subDirectories stack
   * step6 iterate recursivly throught the subDirectories stack and return to
   */

  /**
   * Recursivly renames all of the files and subdirectories that are inside root.
   *
   * @param root               root directory of the Jellyfin library

   */
  private static void recursiveRename(JellyfinFile root) {
    // Step1
    String currentPath = root.getAbsolutePath() + "/";
    String[] subDirectoriePaths = root.list();
    Stack<JellyfinFile> subDirectories = new Stack<>();
    Stack<JellyfinFile> subtitleFiles = new Stack<>();

    // Step 2
    for (String dirPath : subDirectoriePaths) {
      JellyfinFile currentFile = new JellyfinFile(currentPath + dirPath);
      // Step3


      // TODO add support for .srt files
      if (currentFile.isSRTFile()) {
        subtitleFiles.push(currentFile);
        continue;
      }
      // Step4
        JellyfinFile renamedFile = new JellyfinFile(currentFile.getJellyfinFormatName());
        if (renamedFile.isDirectory()) {
          subDirectories.push(renamedFile);
        }

      // step5
      if (currentFile.isDirectory()) {
        subDirectories.push(currentFile);
      }
    }
    // Step6
    for (JellyfinFile subDirectory : subDirectories) {
      recursiveRename(subDirectory);
    }
  }
}
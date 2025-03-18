package org.jellyfin;

import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class RecursiveRenaming {

  /**
   * runs the CLI version of the program
   */
  public void runProgram() throws IOException {
    System.out.println('\n' +
        "       _  __  __  _____     _                                    _               " + '\n' +
        "      | ||  \\/  ||  __ \\   (_)                                  (_)              " + '\n' +
        "      | || \\  / || |__) |   _  ___    _ __  _   _  _ __   _ __   _  _ __    __ _ " + '\n' +
        "  _   | || |\\/| ||  _  /   | |/ __|  | '__|| | | || '_ \\ | '_ \\ | || '_ \\  / _` |" + '\n' +
        " | |__| || |  | || | \\ \\   | |\\__ \\  | |   | |_| || | | || | | || || | | || (_| |" + '\n' +
        "  \\____/ |_|  |_||_|  \\_\\  |_||___/  |_|    \\__,_||_| |_||_| |_||_||_| |_| \\__, |" + '\n' +
        "                                                                            __/ |" + '\n' +
        "                                                                           |___/ " + '\n');
    Scanner input = new Scanner(System.in);
    final boolean consent = getConsent(input);
    System.out.println("Give the path to the jellyfin library folder you would want to format");
    String pathToMediaRootDir = input.nextLine();
    input.close();

    JellyfinFile mediaRootDir = new JellyfinFile(pathToMediaRootDir);

    if (mediaRootDir.isDirectory()) {
      recursiveRename(mediaRootDir, consent);
    } else {
      System.out.println(mediaRootDir + " is not a directory");
    }
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
   * Recursivly rename all of the files and directories that are inside root and
   * it's subdirectories
   * 
   * @param root
   * @param consent true == delete useless files, false == don't delete useless
   *                files
   */
  private void recursiveRename(JellyfinFile root, boolean consent) {
    // Step1
    String currentPath = root.getAbsolutePath() + "/";
    String[] subDirectoriePaths = root.list();
    Stack<JellyfinFile> subDirectories = new Stack<>();
    Stack<JellyfinFile> subtitleFiles = new Stack<>();

    // Step 2
    for (String dirPath : subDirectoriePaths) {
      JellyfinFile currentFile = new JellyfinFile(currentPath + dirPath);
      // Step3
      if (consent == true) {
        if (currentFile.isUselessFile()) {
          if (currentFile.delete()) {
            System.out.println("Deleted file: " + currentFile.getAbsolutePath());
          } else {
            System.out.println("Failed to delete file: " + currentFile.getAbsolutePath());
          }
          continue;
        }
      }
      // TODO add support for .srt files
      if (currentFile.isSRTFile()) {
        subtitleFiles.push(currentFile);
        continue;
      }
      // Step4
      JellyfinFile renamedFile = new JellyfinFile(currentFile.renameToJellyfinFormat());
      // step5
      if (renamedFile.isDirectory()) {
        subDirectories.push(renamedFile);
      }
    }
    // Step6
    for (JellyfinFile subDirectory : subDirectories) {
      recursiveRename(subDirectory, consent);
    }
  }

  /**
   * @param input System.in Scanner
   * @return if 'Y' return true, else return false
   */
  private boolean getConsent(Scanner input) {
    System.out.println("Allow JMR to delete useless files such as .exe, .txt, .jpg and .png files (y/n)");
    return input.nextLine().equalsIgnoreCase("y");
  }
}

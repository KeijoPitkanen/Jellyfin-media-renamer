package org.jellyfin;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class CLI extends FileOperations {
  /**
   * How the algorithm works
   * 0. get homepath of jellyfin library from user
   * 1. get stack of all the subdirectories in current directory
   * 2. Change current dir to first subdirectory -> return to step 1
   * 3 get stack of all the files in current directory
   * 4. rename all the files
   * 5. get stack of all the subdirectories
   * 6. rename all the subdirectories -> return to step 2
   */
  public void runProgram() throws IOException {
    Scanner input = new Scanner(System.in);
    final boolean consent = getConsent(input);
    System.out.println("Give the path to the jellyfin library folder you would want to format");
    // step 0
    String pathToJellyfin = input.nextLine();
    input.close();
    File jellyfinDir = new File(pathToJellyfin);
    if (jellyfinDir.isDirectory() == false) {
      System.out.println(jellyfinDir + " is not a directory");
    } else {
      helperRunProgram(pathToJellyfin, consent);
    }
  }

  /**
   * Used in runProgram()
   * 
   * @param path absolute path to file/dir
   * @throws IOException from FileOperations methods
   */
  private void helperRunProgram(String path, boolean consent) throws IOException {
    // step 1
    Stack<String> subDirectories = getDirs(path);
    while (subDirectories.isEmpty() == false) {
      // step 2
      helperRunProgram(subDirectories.pop(), consent);
    }
    // step 3
    Stack<String> files = getFiles(path);
    // step 4
    while (files.isEmpty() == false) {
      rename(files.pop(), consent);
    }
    // step 5
    subDirectories = getDirs(path);
    // step 6
    for (String directory : subDirectories) {
      rename(directory, consent);
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

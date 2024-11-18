package org.jellyfin;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

//TODO add confirmation if the user wants files to be deleted

public class CLI extends TerminalCommands {
  /**
   * How the algorithm works
   * 0. get homepath of jellyfin library from user
   * 1. get stack of all the subdirectories in current directory
   * 2. Change current dir to first subdirectory -> return to step 1
   * 3  get stack of all the files in current directory
   * 4. rename all the files
   * 5. get stack of all the subdirectories
   * 6. rename all the subdirectories -> return to step 2
   */
  public void runProgram() throws IOException {
    System.out.println("Give the path to the jellyfin library folder you would want to format");
    //step 0
    String pathToJellyfin = getUserInput();
    File jellyfinDir = new File(pathToJellyfin);
    if (jellyfinDir.isDirectory() == false) {
      System.out.println(jellyfinDir + " is not a directory");
      return;
    }

    helperRunProgram(pathToJellyfin);
  }
  /**
   * Used in runProgram()
   * @param path absolute path to file/dir
   * @throws IOException from TerminalCommands methods
   */
  public void helperRunProgram(String path) throws IOException  {
    //step 1
    Stack<String> subDirectories = getDirs(path);
    while (subDirectories.isEmpty() == false) {
      //step 2
      helperRunProgram(subDirectories.pop());
    }
    //step 3
    Stack<String> files = getFiles(path);
    //step 4
    while (files.isEmpty() == false)  {rename(files.pop());}
    //step 5
    subDirectories = getDirs(path);
    //step 6
    for (String directory : subDirectories) {rename(directory);}
  }
}

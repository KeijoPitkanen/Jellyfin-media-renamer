package org.jellyfin;

import java.io.IOException;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) throws IOException {
    Scanner input = new Scanner(System.in);
    System.out.println("Give the path to the jellyfin library folder you would want to format");
    String pathToMediaRootDir = input.nextLine();
    input.close();
    RecursiveRenaming.runProgram(pathToMediaRootDir);
  }
}

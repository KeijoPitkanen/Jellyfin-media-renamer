package org.jellyfin;

import java.io.IOException;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) throws IOException {
    System.out.println("Program has started");
    CLI cli = new CLI();
    cli.runProgram();
  }


}
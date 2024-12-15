package org.jellyfin;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    System.out.println("Program has started");
    CLI cli = new CLI();
    cli.runProgram();
  }
}

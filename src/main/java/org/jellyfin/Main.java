package org.jellyfin;

public class Main {
  public static void main(String[] args) {
    System.out.println("Program has started");
    CLI cli = new CLI();
    cli.runProgram();
  }
}

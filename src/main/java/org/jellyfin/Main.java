package org.jellyfin;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    System.out.println( '\n' +
            "       _  __  __  _____     _                                    _               " + '\n' +
            "      | ||  \\/  ||  __ \\   (_)                                  (_)              " + '\n' +
            "      | || \\  / || |__) |   _  ___    _ __  _   _  _ __   _ __   _  _ __    __ _ " + '\n' +
            "  _   | || |\\/| ||  _  /   | |/ __|  | '__|| | | || '_ \\ | '_ \\ | || '_ \\  / _` |" + '\n' +
            " | |__| || |  | || | \\ \\   | |\\__ \\  | |   | |_| || | | || | | || || | | || (_| |" + '\n' +
            "  \\____/ |_|  |_||_|  \\_\\  |_||___/  |_|    \\__,_||_| |_||_| |_||_||_| |_| \\__, |" + '\n' +
            "                                                                            __/ |" + '\n' +
            "                                                                           |___/ " + '\n');
    CLI cli = new CLI();
    cli.runProgram();
  }
}

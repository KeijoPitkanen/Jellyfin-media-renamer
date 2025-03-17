package org.jellyfin;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    RecursiveRenaming client = new RecursiveRenaming();
    client.runProgram();
  }
}

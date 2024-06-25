package com.calc;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LinuxTerminal extends InputCheck{

    
    public static void runCommand() {
        Process command;
        try {
            command = Runtime.getRuntime().exec("mkdir /home/skula/test");
            System.out.println("exit: " + command.exitValue());
        }   catch (Exception e) {
            System.out.println("Something went wrong");
        }
    }
    
}

package com.calc;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LinuxTerminal extends InputCheck{

    protected static void changeDirectory(String path)    {
        Process command;
        try {
            //Test
            command = Runtime.getRuntime().exec("cd " + path );
            System.out.println("exit: " + command.exitValue());
            command.destroy();
        }   catch (Exception e) {
            System.out.println("Something went wrong");
        }
    }

    protected static void rename(String oldName, String newName)  {
        Process command;
        try {
            command = Runtime.getRuntime().exec("mv " + oldName + " " + newName);
            System.out.println("exit: " + command.exitValue());
            command.destroy();
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }
    }
    
    protected static void runCommand() {
        Process command;
        try {
            //Test
            command = Runtime.getRuntime().exec("mkdir /home/skula/test");
            System.out.println("exit: " + command.exitValue());
            command.destroy();
        }   catch (Exception e) {
            System.out.println("Something went wrong");
        }
    }
    
}

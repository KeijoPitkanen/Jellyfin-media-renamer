package com.calc;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LinuxTerminal extends InputCheck{

    

    protected void changeDirectory(String path)    {
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

    protected void rename(String oldName, String newName)  {
        Process command;
        try {
            command = Runtime.getRuntime().exec("mv " + oldName + " " + newName);
            System.out.println("exit: " + command.exitValue());
            command.destroy();
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }
    }

    
    //TODO: command.waitfor
    protected void runCommand() {
        Process command;
        try {
            //Test
            command = Runtime.getRuntime().exec(LinuxCommands.ls + "/home/skula/test/activeTesting");
            String temp;

            BufferedReader br = new BufferedReader(new InputStreamReader(command.getInputStream()));
            while ((temp = br.readLine()) != null)    {
                System.out.println(temp);
            }

            System.out.println();
            System.out.println("exit: " + command.exitValue());
            command.waitFor();
            command.destroy();
        }   catch (Exception e) {
            System.out.println("Something went wrong");
        }
    }

    protected void test()   {
        Process command;
        int numberOfItems = 0;
        String temp = "";

        try {
            command = Runtime.getRuntime().exec("ls /home/skula | wc -l");

            BufferedReader br = new BufferedReader(new InputStreamReader(command.getInputStream()));
            br.readLine();
            command.waitFor();
            temp = br.readLine();
            //This has to be done because wc -l command gives the output of "total {number}"
            temp = temp.substring(7);
            numberOfItems = Integer.parseInt(temp);

            System.out.println("Number of items " + numberOfItems);
            
            String pathOfFiles[] = new String[numberOfItems];
            

            command = Runtime.getRuntime().exec(LinuxCommands.ls + "/home/skula/test/activeTesting");
            br = new BufferedReader(new InputStreamReader(command.getInputStream()));
/* 
            while ((temp = br.readLine()) != null)    {
                System.out.println(temp);
            }
*/
            for(int count = 0; count < numberOfItems; count++)   {
                pathOfFiles[count] = br.readLine();
            }
            
            
        }catch (Exception e) {
            System.out.println("Something went wrong");
        }

    }
    
}

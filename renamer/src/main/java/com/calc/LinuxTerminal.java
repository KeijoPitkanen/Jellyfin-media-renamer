package com.calc;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.Arrays;

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

            BufferedReader br = new BufferedReader(new InputStreamReader(command.getInputStream()));
            String temp;

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

    //TODO: use find /home/skula/test/activeTesting -type d | wc -l
    public int getNumberOfItemsInDir(String dir, char type)   {
        //Type is d for directory and f for file
        Process command;
        String output = "";
        int numberOfItems = 0;
        try {
            command = Runtime.getRuntime().exec("find " + dir + " -type " + type + " | wc -l");

            BufferedReader br = new BufferedReader(new InputStreamReader(command.getInputStream()));

            output = br.readLine();

            numberOfItems = Integer.parseInt(output);
        } catch (Exception e) {
            System.out.println("Something went wrong with counting the files/fodlers");
        }

        if (type == 'd') {
            return numberOfItems - 1;
        }   else    {
            return numberOfItems;
        }
    }

    protected void test()   {
        Process command;
        int numberOfItems = 0;
        String temp = "";
        //Default value here
        String currentPath = "/home/skula/test/activeTesting";
        try {
            //wc not working. 
            command = Runtime.getRuntime().exec("ls " + currentPath + " | wc");
            BufferedReader br = new BufferedReader(new InputStreamReader(command.getInputStream()));
            command.waitFor();

            br.readLine();
            temp = br.readLine();
            //This has to be done because wc -l command gives the output of "total {number}"
            temp = temp.substring(6);
            numberOfItems = Integer.parseInt(temp);

            System.out.println("Number of items " + numberOfItems);
            
            String pathOfFiles[] = new String[numberOfItems];

            command = Runtime.getRuntime().exec(LinuxCommands.ls + currentPath);
            br = new BufferedReader(new InputStreamReader(command.getInputStream()));
            for(int count = 0; count < numberOfItems; count++)   {
                pathOfFiles[count] = br.readLine();
            }
            for(int count = 0; count < numberOfItems; count++)   {
                pathOfFiles[count] = currentPath + pathOfFiles[count];
            }


            System.out.println(Arrays.toString(pathOfFiles));

            
            
        }catch (Exception e) {
            System.out.println("Something went wrong");
        }
    }
    
}

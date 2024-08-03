package com.calc;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class LinuxTerminal extends InputCheck{

    private int getDirCount(String path) throws IOException {
        String line = "";

        ProcessBuilder[] builders = {
            new ProcessBuilder("ls -d */", path),
            new ProcessBuilder("wc", "-l")};
        List<Process> processes = ProcessBuilder.startPipeline(
            Arrays.asList(builders));
        Process last = processes.get(processes.size()-1);

        BufferedReader reader = new BufferedReader(new InputStreamReader(last.getInputStream()));
        line = reader.readLine();

        return Integer.valueOf(line);
    }

    private String[] getDirs(String path) throws IOException    {
        String line = "";
        String[] dirs = new String[getDirCount(path)];

        //processbuilder is overkill for this
        ProcessBuilder[] builders = {
            new ProcessBuilder("ls -dm */", path)};
        List<Process> processes = ProcessBuilder.startPipeline(
            Arrays.asList(builders));
        Process last = processes.get(processes.size()-1);
        BufferedReader reader = new BufferedReader(new InputStreamReader(last.getInputStream()));

        while (reader.readLine() != null)   {
            line = line + reader.readLine();
        }
        int temp = 0;
        int count = 0;
        //This might be better as a for statement
        while (count < dirs.length) {
            dirs[count] = line.substring(temp, line.indexOf(','));
    
            temp = line.indexOf(',') + 1;
            count ++;
            //Check that the last dir gets also added


        }

    }




    protected void test2() throws IOException{
        String homePath = "/home/skula/test/activeTesting";
        String activePath = homePath;

        int dirCount = getDirCount(activePath);

        while (dirCount != 0) {


        }



        


       

    }



    protected void test() throws IOException{
        System.out.println();;
        String line = "";
        
        ProcessBuilder[] builders = {
            new ProcessBuilder("ls", "/home/skula/test/activeTesting"),
            new ProcessBuilder("wc", "-l")};
            //ls -d */ lists only directories

        List<Process> processes = ProcessBuilder.startPipeline(
            Arrays.asList(builders));
        Process last = processes.get(processes.size()-1);
        BufferedReader reader = new BufferedReader(new InputStreamReader(last.getInputStream()));
        while ((line = reader.readLine()) != null)   {
        System.out.println(line);
        } 
    }
}

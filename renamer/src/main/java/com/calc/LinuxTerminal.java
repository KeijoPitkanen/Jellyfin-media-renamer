package com.calc;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class LinuxTerminal extends InputCheck{

    protected void test() throws IOException{
        System.out.println();;
        String line = "";
        
        ProcessBuilder[] builders = {
            new ProcessBuilder("ls", "/home/skula/test/activeTesting"),
            new ProcessBuilder("wc", "-l")};

        List<Process> processes = ProcessBuilder.startPipeline(
            Arrays.asList(builders));
        Process last = processes.get(processes.size()-1);
        BufferedReader reader = new BufferedReader(new InputStreamReader(last.getInputStream()));
        while ((line = reader.readLine()) != null)   {
        System.out.println(line);
        } 
    }
}

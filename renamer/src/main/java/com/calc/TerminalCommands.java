package com.calc;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class TerminalCommands extends InputCheck{

    protected void changeName(String path) throws IOException  {
        //files[] also includes dirs
        String files[] = new String[getDirAndFileCount(path)];

        if (files.length == 0) {
            return;
        }
        files = getDirsAndFiles(path);
        int count = 0;
        //This is dumb but maybe this is just the best way to do this
        while (count < files.length)    {
            ProcessBuilder[] builders = {
                new ProcessBuilder("cd", path),
                new ProcessBuilder("mv", files[0], runAllParsers(files[0]))};
            List<Process> processes = ProcessBuilder.startPipeline(
                Arrays.asList(builders));
            count++;
        }
    }

    protected int getDirCount(String path) throws IOException {
        String line = "";

        ProcessBuilder[] builders = {
            new ProcessBuilder("cd", path),
            new ProcessBuilder("ls -d */"),
            new ProcessBuilder("wc", "-l")};
        List<Process> processes = ProcessBuilder.startPipeline(
            Arrays.asList(builders));
        Process last = processes.get(processes.size()-1);

        BufferedReader reader = new BufferedReader(new InputStreamReader(last.getInputStream()));
        line = reader.readLine();

        return Integer.valueOf(line);
    }

    protected int getDirAndFileCount(String path) throws IOException  {
        String line = "";

        ProcessBuilder[] builders = {
            new ProcessBuilder("cd", path),
            new ProcessBuilder("ls"),
            new ProcessBuilder("wc", "-l")};
        List<Process> processes = ProcessBuilder.startPipeline(
            Arrays.asList(builders));
        Process last = processes.get(processes.size()-1);

        BufferedReader reader = new BufferedReader(new InputStreamReader(last.getInputStream()));
        line = reader.readLine();

        return Integer.valueOf(line);
    }

    protected String[] getDirsAndFiles(String path) throws IOException    {
        String line = "";
        String[] dirs = new String[getDirCount(path)];

        ProcessBuilder[] builders = {
            new ProcessBuilder("cd", path),
            new ProcessBuilder(("ls -m"))};
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
            //This is messy
            if (line.indexOf(',') == -1) {
                dirs[count] = line.substring(temp);
                break;
            }
        }
        return dirs;
    }

    protected String[] getDirs(String path) throws IOException    {
        String line = "";
        String[] dirs = new String[getDirCount(path)];

        //processbuilder is overkill for this
        ProcessBuilder[] builders = {
            new ProcessBuilder("cd ", path),
            new ProcessBuilder("ls -dm */")};
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
            //This is messy
            if (line.indexOf(',') == -1) {
                dirs[count] = line.substring(temp);
                break;
            }
        }
        return dirs;
    }
}

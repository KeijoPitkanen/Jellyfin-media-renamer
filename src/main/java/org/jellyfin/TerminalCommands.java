package org.jellyfin;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

public class TerminalCommands extends InputCheck{
    /**
     *Changes the name of a single file/dir to comply with Jellyfin formatting
     * @param path = absolute file path
     * @throws IOException from ProcessBuilder.startPipeline
     */
    protected void rename(String path) throws IOException  {
        //Path = /home/user/code.txt -> localName = code.txt
        String localName = path.substring(path.lastIndexOf('/') + 1);
        //This is only used to check if the file is a file or directory
        File currentFile = new File(path);
        if (currentFile.isFile())   {
            if (isUselessFile(path))    {
                if (deleteUselessFile(path) == false)    {
                    throw new RuntimeException("Jellyfin-media-renamer does not have permissions to delete " + path);
                } else {
                    System.out.println(path + " DELETED");
                    return;
                }
            }
        }
        String newLocalName = runAllParsers(localName, currentFile.isFile());
        String newPath = path.substring(0, path.lastIndexOf('/') + 1) + newLocalName;
        //check if file exists
        File newFile = new File(newPath);
        if (newFile.exists())   {
            System.out.println(localName + " WAS NOT RENAMED ");
            return;
        }
        ProcessBuilder[] builders = {new ProcessBuilder("mv", path, newPath)};
        ProcessBuilder.startPipeline(Arrays.asList(builders));
        System.out.println(localName + " RENAMED TO " + newLocalName);
    }

    /**
     * Get absolute paths of all the directories that are inside the current directory
     * @param path absolute path to directory
     * @return stack of absolute directory paths
     */
    protected Stack<String> getDirs(String path)    {
        Stack<String> subDirectories = new Stack<>();
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null)  {
            for (File file : files) {
                if (file.isDirectory()) {
                    subDirectories.push(file.getAbsolutePath());
                }
            }
        }
        return subDirectories;
    }
    /**
     * get absolute paths of all the files that are inside the current directory
     * @param path absolute path to directory
     * @return stack of files in current directory
     */
    protected Stack<String> getFiles(String path)   {
        Stack<String> files = new Stack<>();
        File directory = new File(path);
        File[] arrayOfFiles = directory.listFiles();
        if (arrayOfFiles != null)  {
            for (File file : arrayOfFiles)  {
                if (file.isFile())  {
                    files.push(file.getAbsolutePath());
                }
            }
        }
        return files;
    }
    /**
     * @return user text input from terminal
     */
    protected String getUserInput() {
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }
    /**
     * Delete the file if they are not video or subtitle file i.e. .exe, .txt, .jpg, .png etc
     * @param path global dir path
     */
    private boolean deleteUselessFile(String path){
        File file = new File(path);
        if (file.isFile())  {
            return file.delete();
        }   else {
            return false;
        }
    }

    /**
     * Check if file is useless and can be deleted
     * @param path Absolute path to file
     * @return true if file is of type formats[]
     */
    private boolean isUselessFile(String path) {
        String[] formats = {".exe", ".txt", ".jpg", ".png"};
        String fileExtension = getFileExtension(path);
        for (String format : formats)   {
            if (fileExtension.equals(format))   {
                return true;
            }
        }
        return false;
    }

    /**
     * @param oldName absolute path of file/dir
     * @param newName new formatted absolute path of file/dir
     * @param operation d -> delete r -> rename
     * @return if 'Y' return true, else return false
     */
    private boolean getConsent(String oldName, String newName, char operation)  {
        oldName = oldName.substring(oldName.lastIndexOf('/') + 1);
        newName = newName.substring(newName.lastIndexOf('/') +1 );
        String output = switch (operation) {
            case 'r' -> "Rename '" + oldName + "' to '" + newName + "' (y/n)";
            case 'd' -> "Delete file '" + oldName + "' (y/n";
            default -> "Unknown operation for file/dir " + oldName;
        };
        System.out.println(output);
        return getUserInput().equalsIgnoreCase("y") || getUserInput().isEmpty();
    }
}
package com.calc;
import java.util.HashMap;
import java.util.Scanner;

public class CLI extends LinuxTerminal{
    /*
     * How the algorithm works
     * 0. Homepath is set by user
     * 1. path = homepath
     * 2. if dircount(path) != 0
     *  2.1. Save hashmap(path, 0)
     *  2.2. Currentpath = getDirs(path)[0]
     *  2.3. path = path + Currentpath
     *  2.4. Go to step 2
     * 3. if dircount(path) = 0
     *  3.1. changeName(path)
     *  3.2. path = path - currentpath
     *  3.3. if hashmap(path, i), i < dirCount(path)
     *      3.3.1. i++
     *      3.3.2. currentpath = getDirs(path)[i]
     *      3.3.3. path = path + currentpath
     *      3.3.3. go to step 2
     *  3.4. if hashmap(path, i), i == dirCount(path)
     *      3.4.1. delete hashmap(path)
     *      3.4.2. changeName(path)
     *      3.4.3. if path == homepath end program
     *      3.4.4 go to step 3.2
     */     

    public void runProgram() {
        String homePath = "";
        String currentDir = "";
        HashMap<String, Integer> dirStructure = new HashMap<String, Integer>();
        System.out.println("The program has started");
    
        System.out.println("Filepath to media folder: ");
        Scanner input = new Scanner(System.in);
    
        homePath = input.nextLine();

        String path = homePath;
   
        
            
        



    
    }



    
}

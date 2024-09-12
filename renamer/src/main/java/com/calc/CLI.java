package com.calc;
import java.util.HashMap;
import java.util.Scanner;

public class CLI extends LinuxTerminal{
    /*
     * How the algorithm works
     * 0. Homepath is set by user
     * 1. path = homepath
     * 2. if getDirCount(path) != 0
     *  2.1. Save hashmap(path, 0)
     *  2.2. currentDir = getDirs(path)[0]
     *  2.3. path = path + currentDir
     *  2.4. Go to step 2
     * 3. if getDirCount(path) = 0
     *  3.1. changeName(path)
     *  3.2. path = path - currentDir
     *  3.3. if hashmap(path, i), i < getDirCount(path)
     *      3.3.1. i++
     *      3.3.2. currentDir = getDirs(path)[i]
     *      3.3.3. path = path + currentDir
     *      3.3.3. go to step 2
     *  3.4. if hashmap(path, i), i == getDirCount(path)
     *      3.4.1. delete hashmap(path)
     *      3.4.2. changeName(path)
     *      3.4.3. path = path - currentDir
     *      3.4.4. if path == homepath end program
     *      3.4.5. go to step 3.3
     */     

    public void runProgram() {
        String homePath = "";
        String currentDir = "";
        HashMap<String, Integer> dirStructure = new HashMap<String, Integer>();
        System.out.println("The program has started");
    
        System.out.println("Filepath to media folder: ");
        Scanner input = new Scanner(System.in);
        //step 0
        homePath = input.nextLine();

        //check if homePath actually has folders. otherwise causes problems at step 3
        try {
            if (getDirCount(homePath) == 0) {
                return;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        //step 1
        String path = homePath;

        //TODO: Refactor this so that the goto statements actually work
        //TODO: Add confirmation to this
   
        try {
            //step 2
            //It might be better to store the dirCount in the hashmap value
            if (getDirCount(path) != 0) {
                dirStructure.put(path, 0);
                currentDir = getDirs(path)[0];
                path = path + currentDir;
            }
            //step 3
            else {
                changeName(path);
                //step 3.2
                //the -1 is the make sure the indexing is correct
                path = path.substring(0, path.length() - currentDir.length() - 1);
                //step 3.3
                if (dirStructure.get(path) < getDirCount(path)) {
                    dirStructure.put(path, dirStructure.get(path) + 1);
                    currentDir = getDirs(path)[dirStructure.get(path)];
                    path = path + currentDir;
                    //GOTO STEP 2
                }
                //step 3.4
                else    {
                    dirStructure.remove(path);
                    changeName(path);
                    path = path.substring(0, path.length() - currentDir.length() - 1);
                    if (path.equals(homePath)) {
                        return;
                    }
                    //GOTO STEOP 3.3
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        
            
        



    
    }



    
}

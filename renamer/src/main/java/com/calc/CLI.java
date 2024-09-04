package com.calc;
import java.util.Scanner;

public class CLI {
    public static void runProgram() {
        String path = "";
        System.out.println("The program has started");
    
        System.out.println("Filepath to media folder: ");
        Scanner input = new Scanner(System.in);
    
        path = input.nextLine();
        System.out.println(path);
    
    }


    
}

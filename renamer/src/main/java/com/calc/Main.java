package com.calc;

public class Main {
    public static void main(String[] args) {
        System.out.println("The program has started");


        //Tests start
        LinuxTerminal terminal = new LinuxTerminal();
        System.out.println(terminal.getNumberOfItemsInDir("/home/skula/test/activeTesting", 'd'));

        //Tests end
    }
}
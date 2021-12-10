package com.xfoss.learningJava;

class Duck {
    int size;

    public Duck (int s = 32) {
        size = s;
        System.out.format("Quack... My size is %s\n", size);
    }
}

public class UseADuck {
    public static void main (String [] args) {
        Duck d = new Duck (32);
    }
}

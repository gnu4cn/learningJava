package com.xfoss.learningJava;

class Duck {
    int size = 32;

    Duck () {
        System.out.format("Quack... My size is %s\n", size);
    }

    Duck (int s) {
        size = s;
        System.out.format("Quack... My size is %s\n", size);
    }
}

public class UseADuck {
    public static void main (String [] args) {
        Duck d = new Duck (28);
        Duck d2 = new Duck ();
    }
}

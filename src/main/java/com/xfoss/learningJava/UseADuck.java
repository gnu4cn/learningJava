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
    private int size;

    public static void main (String [] args) {
        System.out.format("Size of duck is %s\n", getSize());
        Duck d = new Duck (28);
        Duck d2 = new Duck ();
    }

    public int getSize () {
        return size;
    }
}

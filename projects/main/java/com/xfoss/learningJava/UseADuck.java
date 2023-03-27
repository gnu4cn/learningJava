package com.xfoss.learningJava;

class Duck {
    private int size;
    private static int duckCount = 0;

    Duck () {
        this(32);
    }

    Duck (int s) {
        size = s;
        duckCount++;
        System.out.format("Quack... My size is %s, my number is %s\n", size, duckCount);
    }
}

public class UseADuck {
    private int size;

    public static void main (String [] args) {
        Duck d = new Duck (28);
        Duck d2 = new Duck ();
    }

    public int getSize () {
        return size;
    }
}

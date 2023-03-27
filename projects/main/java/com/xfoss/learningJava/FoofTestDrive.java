package com.xfoss.learningJava;

class Foof {
    final int size = 3;
    final int whuffie;

    Foof () {
        whuffie = 42;
    }
}

public class FoofTestDrive {
    public static void main (String [] args) {
        Foof f = new Foof ();

        System.out.format ("The size is %s, the whuffie is %s\n", f.size, f.whuffie);
    }
}

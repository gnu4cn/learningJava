package com.xfoss.learningJava;

public class TestBox {
    Integer i = new Integer(25);
    int j;

    public static void main (String [] args) {
        TestBox t = new TestBox ();
        t.go();
    }

    public void go () {
        i = j;
        System.out.format("j is %s\n", j);
        System.out.format("i is %s\n", i);

        double d = 42.5;
        System.out.format("`\"\" + d` is %s, Double.toString(d) is %s\n", (""+d), Double.toString(d));
    }
}

package com.xfoss.learningJava;

import java.io.*;

abstract class Animal {
    private String name;

    public String getName () {
        return name;
    }

    Animal () {}

    Animal (String theName) {
        name = theName;
    }
}

abstract class Canine extends Animal {}

abstract class Feline extends Animal {}

class Dog extends Canine implements Serializable {
    private static final long serialVersionUID = 1720600418317157466L;
}

class Cat extends Feline {}
class Hippo extends Animal {
    Hippo (String name) {
        super(name);
    }
}

public class AnimalTestDrive {
    public static void main (String [] args) {
        Hippo h = new Hippo("Buffy");
        System.out.println(h.getName());

    }
}

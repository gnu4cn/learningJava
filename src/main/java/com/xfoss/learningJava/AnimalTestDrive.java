package com.xfoss.learningJava;

abstract class Animal {
    private String name;

    public String getName () {
        return name;
    }

    Animal (String theName) {
        name = theName;
    }
}

// abstract class Canine extends Animal {}
// abstract class Feline extends Animal {}

// class Dog extends Canine {}
// class Cat extends Feline {}
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

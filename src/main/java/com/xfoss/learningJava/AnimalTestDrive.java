package com.xfoss.learningJava;

abstract class Animal {
    Animal () {
        System.out.println ("Making an Animal");
    }
}

abstract class Canine extends Animal {}
abstract class Feline extends Animal {}

class Dog extends Canine {}
class Cat extends Feline {}
class Hippo extends Animal {
    Hippo () {
        System.out.println("Making a Hippo");
    }
}

public class AnimalTestDrive {
    public static void main (String [] args) {
        System.out.println("Starting...");
        Hippo h = new Hippo();
    }
}

package com.xfoss.learningJava;

abstract class Animal {}

abstract class Canine extends Animal {}
abstract class Feline extends Animal {}

class Dog extends Canine {}
class Cat extends Feline {}
class Hippo extends Animal {}

class AnimalList {
    private Animal [] animals = new Animal[5];
    private int nextIndex = 0;

    public void add (Animal a) {
        if (nextIndex < animals.length) {
            animals[nextIndex] = a;
            System.out.format("Animal added at %s.\n", nextIndex);
            nextIndex++;
        }
    }
}

public class AnimalTestDrive {
    public static void main (String [] args) {
        AnimalList list = new AnimalList ();
        Dog d = new Dog ();
        Cat c = new Cat ();
        list.add(d);
        list.add(c);

        System.out.println(d.equals(c) ? "true" : "false");

        System.out.format("The type of d is: %s\n", d.getClass());
        System.out.format("The hashcode of c is: %s\n", c.hashCode());
        System.out.format("d.toString(): %s\n", d.toString());
    }
}

package com.xfoss.CollectionAndGenerics;

import java.util.*;

public class TestGenerics2 {
    public TestGenerics2 () {
        ArrayList<Animal> animals = new ArrayList<Animal> ();
        animals.add(new Dog());
        animals.add(new Cat());
        animals.add(new Dog());
        takeAnimals(animals);

        ArrayList<Dog> dogs = new ArrayList<Dog> ();
        dogs.add(new Dog());
        dogs.add(new Dog());
        takeAnimals(dogs);
    }

    public void takeAnimals(ArrayList<? extends Animal> animals) {
        for (Animal a: animals) {
            a.eat();
        }
    }

    public static void main (String[] args){
        new TestGenerics2();
    }
}

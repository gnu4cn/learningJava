package com.xfoss.CollectionAndGenerics;

import java.util.*;

public class TestGenerics2 {
    public TestGenerics2 () {
        ArrayList<Animal> animals = new ArrayList<Animal> ();
        animals.add(new Dog("丁丁"));
        animals.add(new Cat());
        animals.add(new Dog("阿飞"));
        takeAnimals(animals);

        ArrayList<Dog> dogs = new ArrayList<Dog> ();
        dogs.add(new Dog("Echo"));
        dogs.add(new Dog("阿黄"));

        List<Dog> puppies = dogs;
        System.out.println(puppies);
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

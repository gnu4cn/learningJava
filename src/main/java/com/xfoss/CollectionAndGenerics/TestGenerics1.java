package com.xfoss.CollectionAndGenerics;

import java.util.*;

public class TestGenerics1 {
    public TestGenerics1 () {
        Animal[] animals = {
            new Dog(),
            new Cat(),
            new Dog()
        };

        Dog[] dogs = {
            new Dog(),
            new Dog(),
            new Dog()
        };

        takeAnimals(animals);
        takeAnimals(dogs);
    }

    public void takeAnimals(Animal[] animals) {
        for (Animal a: animals) {
            a.eat();
        }
    }

    public static void main (String[] args){
        new TestGenerics1();
    }
}

abstract class Animal {
    void eat() {System.out.println("动物进食");}
}

class Dog extends Animal {
    void bark (){ System.out.println("汪汪......");}
}

class Cat extends Animal {
    void meow () {System.out.println("喵喵......");}
}

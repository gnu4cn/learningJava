package com.xfoss.CollectionAndGenerics;

import java.util.*;

public class TestGenerics1 {
    public TestGenerics1 () {
        Animal[] animals = {
            new Dog("丁丁"),
            new Cat(),
            new Dog("阿飞")
        };

        Dog[] dogs = {
            new Dog("老虎"),
            new Dog("阿黄"),
            new Dog("Echo")
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
    private String name;

    public Dog (String n) {
        name = n;
    }

    public String getName() {
        return name;
    }

    void bark (){ System.out.println("汪汪......");}

    public String toString() {
        return name;
    }
}

class Cat extends Animal {
    void meow () {System.out.println("喵喵......");}
}

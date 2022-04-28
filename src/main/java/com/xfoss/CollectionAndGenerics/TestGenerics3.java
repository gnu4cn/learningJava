package com.xfoss.CollectionAndGenerics;

import java.util.*;

public class TestGenerics3 {
    public TestGenerics3 () {
        Dog[] dogs = {
            new Dog(),
            new Dog(),
            new Dog()
        };

        takeAnimals(dogs);
    }

    public void takeAnimals(Animal[] animals) {
        animals[2] = new Cat();
    }

    public static void main (String[] args){
        new TestGenerics3();
    }
}

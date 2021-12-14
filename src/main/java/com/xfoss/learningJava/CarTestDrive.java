package com.xfoss.learningJava;

import java.awt.Color;

abstract class Car {
    private String brand;
    private int size;

    Car (String b) {
        brand = b;
    }

    Car (int s) {
        size = s;
    }

    Car (int s, String b) {
        size = s;
        brand = b;
    }
}

class Mini extends Car {
    Color color;

    Mini () {
        this(Color.RED);
    }

    Mini(Color c) {
        super("Mini");
        color = c;
    }

    Mini (int size) {
        super(size);
    }
}

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
        this(Color.RED);
        super(size);
    }
}

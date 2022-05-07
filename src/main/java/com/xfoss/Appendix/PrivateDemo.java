package com.xfoss.Appendix;

class Dog {
    private String name;
    private String type = "犬类";

    Dog (String n) {
        name = n;
    }

    public void printType () {
        System.out.println(type);
    }
}

public class PrivateDemo {
    public static void main (String[] args) {
        Dog d1 = new Dog ("阿黄");
        Dog d2 = new Dog ("馒头");

        d1.printType();
    }
}

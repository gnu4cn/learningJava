package com.xfoss.learningJava;

public class StringSplitDemo {
    public static void main(String[] args) {
        String toTest = "What i blue + yellow?/green";

        String[] result = toTest.split("/");

        for (String token:result) {
            System.out.println(token);
        }
    }
}

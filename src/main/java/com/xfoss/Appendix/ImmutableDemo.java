package com.xfoss.Appendix;

public class ImmutableDemo {
    public static void main (String[] args) {
        String s = "0";

        for (int x = 1; x < 10; x++) {
            s = s + x;
        }

        System.out.println(s);
    }
}

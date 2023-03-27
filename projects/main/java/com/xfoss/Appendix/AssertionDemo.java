package com.xfoss.Appendix;

public class AssertionDemo {
    public static void main (String[] args) {
        int size = 10;

        int sqrt = size * size;

        assert(sqrt < 100) : String.format("Sqare = %d", sqrt);
    }
}

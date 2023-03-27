package com.xfoss.Appendix;

public class MultiDArrayDemo {
    public static void main (String[] args) {
        int[][] a2d = new int[4][2];

        System.out.println(a2d[2][1]);

        int[] copy = a2d[0];

        for(int i : copy) System.out.println(i);
    }
}

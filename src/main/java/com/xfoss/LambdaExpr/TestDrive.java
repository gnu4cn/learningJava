package com.xfoss.LambdaExpr;

import java.util.ArrayList;

class TestDrive
{
    public static void main(String args[]){

        ArrayList<Integer> arrL = new ArrayList<Integer>();
        for(int i = 1; i < 5; i++) arrL.add(i);

        arrL.forEach(n -> System.out.println(n));

        arrL.forEach(n -> {if(n%2 == 0) System.out.println(n);});
    }
}

package com.xfoss.FunctionalInterface;

import java.util.*;
import java.util.function.Predicate;

class PredicateDemo {
    public static void main(String args[])
    {

        List<String> names = Arrays.asList(
                "G极客", "G极客测试", "g1", "Q问答", "G极客2");

        Predicate<String> p = (s) -> s.startsWith("G");

        for (String st : names){
            if (p.test(st)) System.out.println(st);
        }
    }
}

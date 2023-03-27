package com.xfoss.CollectionAndGenerics;

import java.util.*;

public class TestMap {
    public TestMap () {
        HashMap<String, Integer> scores = new HashMap<String, Integer> ();

        scores.put("Lenny", 42);
        scores.put("Echo", 343);
        scores.put("Rose", 420);

        System.out.println(scores);
        System.out.println(scores.get("Echo"));
    }


    public static void main (String[] args) {
        new TestMap();
    }
}

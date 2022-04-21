package com.xfoss.CollectionAndGenerics;

import java.io.*;
import java.util.*;

public class SortMountains {
    LinkedList<Mountain> mtn = new LinkedList<Mountain> ();

    class NameCompare implements Comparator<Mountain> {
        public int compare (Mountain one, Mountain two) {
            return one.getName().compareTo(two.getName());
        }
    }

    class HeightCompare implements Comparator<Mountain> {
        public int compare (Mountain one, Mountain two) {
            return one.getHeight() - two.getHeight();
        }
    }

    public SortMountains () {
        mtn.add(new Mountain("Longs", 14255));
        mtn.add(new Mountain("艾伯特", 14433));
        mtn.add(new Mountain("玛努恩", 14156));
        mtn.add(new Mountain("Castle", 14265));

        System.out.format("输入的是：\n%s\n", mtn);

        NameCompare nc = new NameCompare();
        Collections.sort(mtn, nc);
        System.out.format("依名称排序：\n%s\n", mtn);

        HeightCompare hc = new HeightCompare();
        Collections.sort(mtn, hc);
        System.out.format("依高度排序：\n%s\n", mtn);
    }

    public static void main (String[] args) {
        new SortMountains();
    }
}

class Mountain {
    private String name;
    private int height;

    public String getName () {
        return name;
    }

    public int getHeight () {
        return height;
    }

    public Mountain (String n, int h) {
        name = n;
        height = h;
    }

    public String toString () {
        return String.format("%s: %d", name, height);
    }
}

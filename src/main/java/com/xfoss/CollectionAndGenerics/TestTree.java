package com.xfoss.CollectionAndGenerics;

import java.util.*;
import java.io.*;

public class TestTree {

    public TestTree () {
        Book b1 = new Book ("How Cats Work", "Lenny Peng");
        Book b2 = new Book ("Remix your Body", "Echo Feng");
        Book b3 = new Book ("Finding Emo", "Rose Peng");

        TreeSet<Book> treeComparable = new TreeSet<Book> ();
        treeComparable.add(b1);
        treeComparable.add(b2);
        treeComparable.add(b3);

        System.out.format("使用 Comparable 元素：\n%s\n\n", treeComparable);


        TreeSet<Book> treeComparator = new TreeSet<Book> (new CompareWriter());
        treeComparator.add(b1);
        treeComparator.add(b2);
        treeComparator.add(b3);

        System.out.format("使用带 Comparator 参数的 TreeSet 的过载构造器：\n%s\n", treeComparator);
    }

    class CompareWriter implements Comparator<Book> {
        public int compare(Book b1, Book b2) {
            return b1.getWriter().compareTo(b2.getWriter());
        }
    }

    public static void main (String[] args) {
        new TestTree();
    }
    
}

class Book implements Comparable<Book> {
    private String title;
    private String writer;

    public Book (String t, String w) {
        title = t;
        writer = w;
    }

    public String getTitle () {
        return title;
    }

    public String getWriter () {
        return writer;
    }

    public int compareTo(Book b) {
        return title.compareTo(b.getTitle());
    }

    public String toString() {
        return String.format("%s: %s", title, writer);
    }
}

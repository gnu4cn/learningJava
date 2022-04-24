package com.xfoss.CollectionAndGenerics;

import java.util.*;
import java.io.*;

public class TestTree {

    public TestTree () {
        Book b1 = new Book ("How Cats Work");
        Book b2 = new Book ("Remix your Body");
        Book b3 = new Book ("Finding Emo");

        TreeSet<Book> tree = new TreeSet<Book> ();
        tree.add(b1);
        tree.add(b2);
        tree.add(b3);

        System.out.println(tree);
    }

    public static void main (String[] args) {
        new TestTree();
    }
    
}

class Book implements Comparable<Book> {
    private String title;

    public Book (String t) {
        title = t;
    }

    public String getTitle () {
        return title;
    }

    public int compareTo(Book b) {
        return title.compareTo(b.getTitle());
    }

    public String toString() {
        return title;
    }
}

package com.xfoss.learningJava;

public class TestSyncTest {
    public static void main (String[] args) {
        TestSync job = new TestSync();

        Thread a = new Thread(job);
        Thread b = new Thread(job);

        a.setName("Ryan");
        b.setName("Monica");

        a.start();
        b.start();
    }
}

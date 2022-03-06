package com.xfoss.learningJava;

class TestSync implements Runnable {
    private int balance;

    public void run () {
        String threadName = Thread.currentThread().getName();

        for (int i = 0; i < 10; i++){
            increment();
            System.out.format("%s 存了一块钱，现在余额为 %d\n", threadName, balance);
        }
    }

    private void increment () {
        int i = balance;
        balance = i + 1;
    }
}

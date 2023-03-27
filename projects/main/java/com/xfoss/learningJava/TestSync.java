package com.xfoss.learningJava;

class TestSync implements Runnable {
    private int balance;

    public void run () {
        String threadName = Thread.currentThread().getName();

        for (int i = 0; i < 10; i++){
            increment();
            System.out.format("%s 存了一块钱，现在余额为 %d\n", threadName, balance);
        }

        System.out.format("线程：%s 执行完毕, 现在余额为：%d\n-------------\n", threadName, balance);
    }

    public int getBalance () {
        return balance;
    }

    private synchronized void increment () {
        balance++;
    }
}

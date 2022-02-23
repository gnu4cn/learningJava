package com.xfoss.learningJava;

public class MyRunnable implements Runnable {
    private String incomingMessage;

    public MyRunnable (String message) {
        incomingMessage = message;
    }

    public void run () {
        go();
    }

    public void go () {
        doMore();
    }

    public void doMore () {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {ex.printStackTrace();}

        System.out.format("线程栈的顶部，收到主线程的消息：%s\n", incomingMessage);
    }
}

class ThreadTester {
    public static void main (String[] args) {
        Runnable threadJob = new MyRunnable("你好，用户线程！");
        Thread myThread = new Thread(threadJob);

        myThread.start();

        System.out.println("回到主线程");
    }
}

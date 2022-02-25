package com.xfoss.learningJava;

public class RunThreads implements Runnable {
    private Thread alpha;
    private Thread beta;

    public RunThreads () {
        alpha = new Thread(RunThreads.this);
        beta = new Thread(RunThreads.this);

        alpha.setName("线程 Alpha");
        beta.setName("线程 Beta");
        
        alpha.start();
        beta.start();
    }

    public static void main (String[] args) {
        new RunThreads();
    }

    public void run () {
        for (int i = 0; i < 5; i++) {

            // try {
            //    Thread.sleep(500);
            //} catch (InterruptedException ex) {ex.printStackTrace();}

            String threadName = Thread.currentThread().getName();
            System.out.format("%s 此时正在运行。\n", threadName);
        }
    }
}

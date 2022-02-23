package com.xfoss.learningJava;

public class RunThreads implements Runnable {
    private Thread alpha;
    private Thread beta;

    public RunThreads () {
        alpha = new Thread(RunThreads.this);
        beta = new Thread(RunThreads.this);

        alpha.setName("Alpha thread");
        beta.setName("Beta thread");
        
        alpha.start();
        beta.start();
    }

    public static void main (String[] args) {
        new RunThreads();
    }

    public void run () {
        for (int i = 0; i < 25; i++) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {ex.printStackTrace();}
            String threadName = Thread.currentThread().getName();
            System.out.format("%s is running.\n", threadName);
        }
    }
}

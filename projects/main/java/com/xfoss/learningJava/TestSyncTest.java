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

        try {
            Thread.sleep(500);
        } catch(InterruptedException ex){ex.printStackTrace();}

        System.out.format("程序运行完毕，现在余额为：%d\n", job.getBalance());
        System.exit(0);
    }
}

package com.xfoss.learningJava;

class BankAccount {
    private int balance = 100;

    public int getBalance () {
        return balance;
    }

    public void withdraw (int amount) {
        balance = balance - amount;
    }
}

public class RyanAndMonicaJob implements Runnable {

    private BankAccount account = new BankAccount ();

    public RyanAndMonicaJob () {
        Thread one = new Thread(RyanAndMonicaJob.this);
        Thread two = new Thread(RyanAndMonicaJob.this);

        one.setName("Ryan");
        two.setName("Monica");

        one.start();
        two.start();
    }

    public static void main (String[] args) {
        new RyanAndMonicaJob();
    }

    public void run () {
        for (int x = 0; x < 5; x++) {
            int random;

            while (true) {
                double mathRandom = Math.random();

                if ( mathRandom < 0.1) continue;
                else {
                    random = (int) (mathRandom * 10);
                    break;
                }
            }

            makeWithdrawal(random*10);
            if (account.getBalance() < 0) {
                System.out.format("账户已透支！余额为 %d\n", account.getBalance());
                break;
            }
        }
    }

    private void makeWithdrawal (int amount) {

        String currentThread = Thread.currentThread().getName();

        System.out.format("%s 即将进行支取，数额为 %d, 此时余额为 %d\n", 
                currentThread, amount, account.getBalance());

        if (account.getBalance() >= amount){
            try {
                System.out.format("%s 即将睡过去\n", currentThread);

                Thread.sleep(500);
            } catch (InterruptedException ex) {ex.printStackTrace();}

            System.out.format("%s 醒过来了\n", currentThread);

            account.withdraw(amount);
            System.out.format("%s 完成了支取，支出数额 %d, 此时账户余额为 %d\n", 
                    currentThread, amount, account.getBalance());
        }
        else {
            System.out.format("抱歉，%s, 已经余额不足\n", currentThread);
        }
    }
}

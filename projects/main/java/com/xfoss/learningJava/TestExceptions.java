package com.xfoss.learningJava;

class ScaryException extends Exception {};

public class TestExceptions {
    public static void main (String [] args) {
        String test = "no";

        try {
            System.out.println("开始尝试");
            doRisky(test);
            System.out.println("尝试结束");
        } catch (ScaryException ex) {
            System.out.println("可怕的异常");
            ex.printStackTrace();
        } finally {
            System.out.println("`finally` 代码块");
        }

        System.out.println("main方法的结束");
    }

    static void doRisky (String test) throws ScaryException {
        System.out.println("开始冒险");

        if ("yes".equals(test)) {
            throw new ScaryException ();
        }

        System.out.println("冒险结束");
        return;
    }
}

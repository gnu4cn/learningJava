package com.xfoss.FunctionalInterface;

class Test {
    public static void main(String args[]) {
        new Thread( () -> {
                System.out.println("新的线程已创建");
        }).start();
    }
}

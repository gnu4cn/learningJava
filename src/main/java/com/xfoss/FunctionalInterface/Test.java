package com.xfoss.FunctionalInterface;

class Test {
    public static void main(String args[]) {
        new Thread(new Runnable() {
            @Override public void run()
            {
                System.out.println("新的线程已创建");
            }
        }).start();
    }
}

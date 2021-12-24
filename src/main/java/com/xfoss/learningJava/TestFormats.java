package com.xfoss.learningJava;

public class TestFormats {
    public static void main (String [] args) {
        System.out.format("The number is: %,d\n", 1234567890);
                                        // 注意：这里的 "%, d" 和 "%,d" 有区别
                                        // "%," 和 "d" 之间，只能有两种情况：
                                        // 1. 什么也没有
                                        // 2. 一个空格
                                        // 不能有其他任何字符，包括转义字符
                                        // 两个空格都不可以 

        System.out.format("I have %,.2f bugs to fix.\n", 476578.09876);

        System.out.println(String.format("I have %.2f, bugs to fix.", 476578.09876));
    }
}

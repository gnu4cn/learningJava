package com.xfoss.learningJava;

import java.util.*;

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

        int one = 20456654;
        double two = 100567890.248907;
        System.out.format("排名为 %,.2f 中的第 %,d 位。\n", two, one);

        Date now = new Date();
        System.out.format("现在完整时间：%tc\n", now);
        System.out.format("现在时间：%tr\n", now);
        System.out.format("%tA, %tB %td\n", now, now, now);
    }
}

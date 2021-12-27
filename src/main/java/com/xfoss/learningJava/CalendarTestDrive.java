package com.xfoss.learningJava;

import java.util.*;

public class CalendarTestDrive {
    public static void main (String[] args) {
        Calendar c = Calendar.getInstance();

        c.set(2022, 0, 7, 15, 40);
        long day1 = c.getTimeInMillis();
        System.out.format("以毫秒表示: %,d，正常表示：%s\n", day1, c.getTime());

        day1 += 1000*60*60;
        c.setTimeInMillis(day1);
        System.out.format("新的几点钟： %d\n", c.get(c.HOUR_OF_DAY));

        c.add(c.DATE, 35);
        System.out.format("加上了35天后： %s\n", c.getTime());

        c.roll(c.DATE, 35);
        System.out.format("往前滚动 35 天： %s\n", c.getTime());

        c.set(c.DATE, 1);
        System.out.format("日期设置到 1 号： %s\n", c.getTime());
    }
}

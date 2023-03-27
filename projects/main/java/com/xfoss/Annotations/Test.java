package com.xfoss.Annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@interface TestAnnotation
{
    String Developer() default "Lenny";
    String Expirydate();
}

public class Test
{
    @TestAnnotation(Developer="Echo", Expirydate="01-10-2020")
    void fun1()
    {
        System.out.println("测试方法 1");
    }

    @TestAnnotation(Developer="Anil", Expirydate="01-10-2020")
    void fun2()
    {
        System.out.println("测试方法 2");
    }

    public static void main(String args[])
    {
        System.out.println("你好");
    }
}

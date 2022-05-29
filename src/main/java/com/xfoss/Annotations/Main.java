package com.xfoss.Annotations;

import java.lang.annotation.*;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MyRepeatedAnnos.class)
@interface Word
{
    String word() default "Hello";
    int value() default 0;
}

@Retention(RetentionPolicy.RUNTIME)
@interface MyRepeatedAnnos
{
    Word[] value();
}

public class Main {
    private static String first = "First";

    @Word(word = first, value = 1)
    @Word(word = "Second", value = 2)
    public static void newMethod()
    {
        Main obj = new Main();

        try {
            Class<?> c = obj.getClass();

            Method m = c.getMethod("newMethod");

            Annotation anno = m.getAnnotation(MyRepeatedAnnos.class);
            System.out.println(anno);
        }
        catch (NoSuchMethodException e) 
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) { newMethod(); }
}

package com.xfoss.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE_USE)
@interface TypeAnnoDemo {}

public class GFG {
    public static void main (String args[])
    {
        @TypeAnnoDemo String string = "我已被某个类型注解给注解过了";
        System.out.println(string);
        abc();
    }

    static @TypeAnnoDemo int abc ()
    {
        System.out.println("此函数的返回值类型已被注解过了");
        return 0;
    }
}

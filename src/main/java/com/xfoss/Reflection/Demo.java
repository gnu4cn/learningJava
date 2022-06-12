package com.xfoss.Reflection;

import java.lang.reflect.*;

class Test {
    
    private String s;

    public Test() { s = "极客空间"; }

    public void method1()
    {
        System.out.format("------------------\n该字符串为 %s\n", s);
    }

    public void method2(int n)
    {
        System.out.format("------------------\n该数字为 %d\n", n);
    }

    private void method3()
    {
        System.out.println("------------------\n私有方法被调用了");
    }
}

class Demo {
    
    public static void main(String args[]) throws Exception
    {

        Test obj = new Test();

        Class cls = obj.getClass();
        System.out.format("类的名字为 %s\n", cls.getName());

        Constructor constructor = cls.getConstructor();
        System.out.format("构造器的名字为 %s\n", constructor.getName());

        System.out.println("类的公共方法分别为：");
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) System.out.println(method.getName());

        Method methodCall1 = cls.getDeclaredMethod("method2", int.class);
        methodCall1.invoke(obj, 19);

        Field field = cls.getDeclaredField("s");
        field.set(obj, "Java");

        Method methodCall2 = cls.getDeclaredMethod("method1");
        methodCall2.invoke(obj);

        Method methodCall3 = cls.getDeclaredMethod("method3");
        methodCall3.invoke(obj);
    }
}

package com.xfoss.LambdaExpr;

interface FuncInterface
{
    void abstractFun(int x);

    default void normalFun()
    {
        System.out.println("你好");
    }
}

class Test
{
    public static void main(String args[])
    {
        FuncInterface fobj = (int x) -> System.out.println(2*x);

        fobj.abstractFun(5);
    }
}

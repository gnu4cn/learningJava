package com.xfoss.LambdaExpr;

public class LambdaExprDemo
{
    interface FuncInter1
    {
        int operation(int a, int b);
    }

    interface FuncInter2
    {
        void sayMessage(String message);
    }

    private int operate(int a, int b, FuncInter1 fobj)
    {
        return fobj.operation(a, b);
    }

    public static void main(String args[])
    {
        FuncInter1 add = (int x, int y) -> x + y;

        FuncInter1 multiply = (int x, int y) -> x * y;

        LambdaExprDemo dobj = new LambdaExprDemo();

        System.out.format("加法为 %d\n", dobj.operate(6, 3, add));

        System.out.format("乘法为 %d\n", dobj.operate(6, 3, multiply));

        FuncInter2 fobj = message -> System.out.format("你好 %s\n", message);

        fobj.sayMessage("极客");
    }
}

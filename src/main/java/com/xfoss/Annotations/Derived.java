package com.xfoss.Annotations;

class Base {

    public void display ()
    {
        System.out.println("类 Base 的 display()");
    }
}

public class Derived extends Base {

    @Override public void display(int x)
    {
        System.out.format("重写的 display(int), %d", x);
    }

    public static void main (String args[]) 
    {
        Derived obj = new Derived();
        obj.display();
    }
}

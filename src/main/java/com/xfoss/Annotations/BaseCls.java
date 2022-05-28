package com.xfoss.Annotations;

class BaseCls
{
    public void Display()
    {
        System.out.println("BaseCls diplay()");
    }

    public static void main(String args[])
    {
        BaseCls t1 = new DerivedCls();
        t1.Display();
    }
}

class DerivedCls extends BaseCls 
{
    @Override
    public void Display()
    {
        System.out.println("DerivedCls display()");
    }
}

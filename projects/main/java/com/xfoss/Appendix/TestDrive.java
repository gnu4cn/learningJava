package com.xfoss.Appendix;

class FooOuter {
    static class BarInner {
        void sayIt () {
            System.out.println("一个静态内部类的方法");
        }
    }
}

public class TestDrive {
    public static void main (String[] args) {
        FooOuter.BarInner foo = new FooOuter.BarInner();
        foo.sayIt();
    }
}

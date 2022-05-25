package com.xfoss.Annotations;

class Apartment extends Building {
    public Apartment (String n) {
        super(n);
    }

    public Apartment (String n, String a) {
        super(n, a);
    }

    @Override
    public void open() {
        System.out.println("公寓已打开");
    }

    @Override
    public void openFrontDoor(){
        System.out.println("公寓前门已打开");
    }

    @Override
    public void openBackDoor(){
        System.out.println("公寓后门已打开\n");
    }
}

public class AnnotationTestDrive {
    public static void main (String[] args) {
        new AnnotationTestDrive();
    }

    public AnnotationTestDrive () {
        Apartment a = new Apartment("测试楼栋a", "测试地址");
        System.out.println(a);
        a.open();
        a.openFrontDoor();
        a.openBackDoor();

        Apartment b = new Apartment("测试楼栋b");
        System.out.println(b);
        b.open();
        b.openFrontDoor();
        b.openBackDoor();
    }
}

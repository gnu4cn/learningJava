package com.xfoss.Annotations;

interface House {
    /**
     * @deprecated open 方法的使用是不鼓励的，请使用 openFrontDoor 或 openBackDoor 取而代之。
     */
    @Deprecated
    void open();
    void openFrontDoor();
    void openBackDoor();
}

@SuppressWarnings("deprecation")
class Apartment extends Building implements House {
    public Apartment (String n) {
        super(n);
    }

    public Apartment (String n, String a) {
        super(n, a);
    }

    public void open () {
        System.out.println("房子打开了");
    }

    public void openFrontDoor () {
        System.out.println("房子前门打开了");
    }

    public void openBackDoor () {
        System.out.println("房子后门打开了\n");
    }
}

class Building {
    private String name;
    private String address;

    public String getName () {
        return name;
    }

    public String getAddress () {
        return address;
    }

    public void setName (String n) {
        name = n;
    }

    public void setAddress (String a) {
        address = a;
    }

    public Building (String n) {
        name = n;
        address = "地址缺失";
    }

    public Building (String n, String a) {
        name = n;
        address = a;
    }

    @Override
    public String toString () {
        return String.format("名称：%s, 地址：%s", name, address);
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

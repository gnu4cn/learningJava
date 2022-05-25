package com.xfoss.Annotations;

public class Building implements House {
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

    @Deprecated
    public void open () {}
    public void openFrontDoor () {}
    public void openBackDoor () {}


    @Override
    public String toString () {
        return String.format("名称：%s, 地址：%s", name, address);
    }
}

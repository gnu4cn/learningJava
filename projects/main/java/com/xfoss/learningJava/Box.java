package com.xfoss.learningJava;

import java.io.*;

public class Box implements Serializable {

    private int width;
    private int height;

    public void setWidth (int w) {
        width = w;
    }

    public void setHeight (int h) {
        height = h;
    }

    public static void main (String[] args) {
        Box box = new Box();
        box.setWidth(50);
        box.setHeight(20);

        try {
            FileOutputStream fStream = new FileOutputStream("Box.ser");
            ObjectOutputStream oStream = new ObjectOutputStream(fStream);
            oStream.writeObject(box);
            oStream.close();
        } catch (Exception ex) {ex.printStackTrace();}
    }
}

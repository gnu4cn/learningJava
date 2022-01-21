package com.xfoss.learningJava;

import java.io.*;

public class Pond implements Serializable {
    private Duck duck = new Duck();
    
    public static void main (String[] args) {
        Pond pond = new Pond();

        try {
            FileOutputStream fStream = new FileOutputStream("Pond.ser");
            ObjectOutputStream oStream = new ObjectOutputStream(fStream);

            oStream.writeObject(pond);
            oStream.close();
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}

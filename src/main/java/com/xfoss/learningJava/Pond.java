package com.xfoss.learningJava;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import com.xfoss.Utils.XPlatformThings;

public class Pond implements Serializable {
    private transient Duck duck = new Duck();
    
    public static void main (String[] args) {
        Pond pond = new Pond();

        try {
            FileOutputStream fStream = new FileOutputStream(String.format("%s/Pond.ser", 
                        XPlatformThings.getWorkingDir("learningJava")));
            ObjectOutputStream oStream = new ObjectOutputStream(fStream);

            oStream.writeObject(pond);
            oStream.close();
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}

package com.xfoss.learningJava;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class Pond implements Serializable {
    private transient Duck duck = new Duck();
    
    public static void main (String[] args) {
        Pond pond = new Pond();

        String dataDir = String.format("%s/learningJava/data", System.getenv("LOCALAPPDATA"));

        File dir = new File(dataDir);
        if(!dir.exists()) dir.mkdirs();

        try {
            FileOutputStream fStream = new FileOutputStream(String.format("%s/Pond.ser", dataDir));
            ObjectOutputStream oStream = new ObjectOutputStream(fStream);

            oStream.writeObject(pond);
            oStream.close();
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}

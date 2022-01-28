package com.xfoss.learningJava;

import java.io.*;
import java.util.Objects;
import com.xfoss.Utils.*;

public class ReadFile {
    public static void main (String[] args) {
        XPlatformThings xpt = new XPlatformThings();
        String wd = xpt.getWorkingDir("learningJava");

        try {
            File f = new File(String.format("%s/data/MyText.txt", wd));
            FileReader fReader = new FileReader(f);

            BufferedReader reader = new BufferedReader(fReader);

            String line = null;
            while(!Objects.isNull(line = reader.readLine())) {
                System.out.println(line);
            }
            reader.close();
        } catch (Exception ex) {}
    }
}

package com.xfoss.Utils;

import java.io.File;

public class XPlatformThings {
    public static String getWorkingDir (String appName) {
        String OS = (System.getProperty("os.name")).toUpperCase();
        String workingDir;

        if (OS.contains("WIN")) {
            workingDir = String.format("%s\\%s", System.getenv("AppData"), appName);
        }
        else {
            workingDir = String.format("%s%s", 
                System.getProperty("user.home"), 
                OS.contains("LINUX") 
                ? String.format("/.%s", appName)
                : String.format("/Library/Application Support/%s", appName));
        }

        File d = new File(workingDir);

        if(!d.exists()) d.mkdirs();

        return workingDir;
    }
}

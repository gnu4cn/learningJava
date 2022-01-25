package com.xfoss.Utils;

public class XPlatformThings {
    private String OS = (System.getProperty("os.name")).toUpperCase();

    public String getWorkingDir (String appName) {
        if (OS.contains("WIN")) return String.format("%s\\%s", System.getenv("AppData"), appName);
        else return String.format("%s%s", 
                System.getProperty("user.home"), 
                OS.contains("LINUX") 
                ? String.format("/.%s", appName)
                : String.format("/Library/Application Support/%s", appName));
    }
}

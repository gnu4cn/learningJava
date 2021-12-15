package com.xfoss.learningJava;

class StarPlayer {
    static int playerCount = 0;
    private String name;

    StarPlayer (String n) {
        name = n;
        playerCount++;
    }
}

public class StarPlayerTestDrive {
    public static void main (String[] args) {
        System.out.println(StarPlayer.playerCount);
        StarPlayer one = new StarPlayer ("Tiger Woods");
        System.out.println(StarPlayer.playerCount);
    }
}

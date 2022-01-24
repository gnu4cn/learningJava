package com.xfoss.learningJava;

import java.io.*;

public class GameSaverTest {
    public static void main (String[] args) {
        GameCharacter one = new GameCharacter(50, "Elf", new String[] {"bow", "sword", "dust"});
        GameCharacter two = new GameCharacter(200, "Troll", new String[] {"bare hands", "big ax"});
        GameCharacter three = new GameCharacter(120, "Magician", new String[] {"spells", "invisibility"});

        String dataDir = String.format("%s/learningJava/data", System.getenv("LOCALAPPDATA"));

        File dir = new File(dataDir);
        if(!dir.exists()) dir.mkdirs();

        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(String.format("%s/GameCharacter.ser", dataDir)));
            os.writeObject(one);
            os.writeObject(two);
            os.writeObject(three);
            os.close();
        } catch (IOException ex) {ex.printStackTrace();}

        one = null;
        two = null;
        three = null;

        try {
        } catch (Exception ex) {ex.printStackTrace();}
    }
}

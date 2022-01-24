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
        
        String serFile = String.format("%s/GameCharacter.ser", dataDir);

        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(serFile));
            os.writeObject(one);
            os.writeObject(two);
            os.writeObject(three);
            os.close();
        } catch (IOException ex) {ex.printStackTrace();}

        one = null;
        two = null;
        three = null;

        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(serFile));
            GameCharacter oneRestore = (GameCharacter) is.readObject();
            GameCharacter twoRestore = (GameCharacter) is.readObject();
            GameCharacter threeRestore = (GameCharacter) is.readObject();
            is.close();

            System.out.format("One's type: %s\n", oneRestore.getType());
            System.out.format("Two's type: %s\n", twoRestore.getType());
            System.out.format("Three's type: %s\n", threeRestore.getType());
        } catch (Exception ex) {ex.printStackTrace();}
    }
}

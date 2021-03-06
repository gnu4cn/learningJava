package com.xfoss.learningJava;

import java.io.*;
import com.xfoss.Utils.XPlatformHelper;
import java.util.*;

public class GameSaverTest {
    public static void main (String[] args) {
        GameCharacter one = new GameCharacter(50, "Elf", new String[] {"bow", "sword", "dust"});
        GameCharacter two = new GameCharacter(200, "Troll", new String[] {"bare hands", "big ax"});
        GameCharacter three = new GameCharacter(120, "Magician", new String[] {"spells", "invisibility"});

        String serFile = String.format("%s/GameCharacter.ser", XPlatformHelper.getWorkingDir("learningJava"));

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
            while (true) {
                Object o = is.readObject();
                if(Objects.isNull(o)) break;
                System.out.format("One's type: %s\n", ((GameCharacter) o).getType());
            }
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

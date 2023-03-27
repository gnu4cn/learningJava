package com.xfoss.learningJava;

import com.xfoss.Utils.*;

public class GameLauncher {
	
    
    public static void main (String[] args) {
    	String osName = System.getProperty("os.name");

        if (osName.startsWith("Windows")) {
        	RegEditor ed = new RegEditor();
        	ed.enableWindows10AnsiSupport();
        }

        GuessGame game = new GuessGame();
        game.startGame();
    }
}

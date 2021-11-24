package com.xfoss.learningJava;

import com.xfoss.winUtils.*;

public class GameLauncher {
	
    
    public static void main (String[] args) {

        if (System.getProperty("os.name").startsWith("Windows")) {
        	RegEditor ed = new RegEditor();
        	ed.enableWindows10AnsiSupport();
        }

        GuessGame game = new GuessGame();
        game.startGame();
    }
}

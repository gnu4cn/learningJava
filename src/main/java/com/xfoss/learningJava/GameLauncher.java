package com.xfoss.learningJava;

import com.xfoss.winUtils.*;

public class GameLauncher {
	
    
    public static void main (String[] args) {
    	String osName = System.getProperty("os.name");
    	
    	System.out.format("The OS is %s"
    			+ "\n--------------------------\n", osName);

        if (System.getProperty("os.name").startsWith("Windows")) {
        	RegEditor ed = new RegEditor();
        	ed.enableWindows10AnsiSupport();
        }

        GuessGame game = new GuessGame();
        game.startGame();
    }
}

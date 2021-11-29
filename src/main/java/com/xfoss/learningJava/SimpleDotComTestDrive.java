package com.xfoss.learningJava;

import java.util.ArrayList;

public class SimpleDotComTestDrive {
	public static void main (String [] args) {
        int numOfGuesses = 0;

        GameHelper helper = new GameHelper ();
        SimpleDotCom dot = new SimpleDotCom ();

        boolean isAlive = true;
        while (isAlive) {
           String guess = helper.getUserInput("请输入一个数字");
           String result = dot.checkYourself(guess);
           ++numOfGuesses;

           if (result.equals("击沉")) {
               isAlive = false;
               System.out.format("你击沉 DotCom 用了 %s 次。\n", numOfGuesses);
           }
        }
	}
}

class SimpleDotCom {
	private ArrayList<String> locationCells = new ArrayList<String> ();
	
    // SimpleDotCom constructor
    public SimpleDotCom () {
        int randomNum = (int) (Math.random() * 5);
        for (int t = 0; t < 3; t++) {
            String tmp = Integer.toString(randomNum + t);
            locationCells.add(tmp);
        }
    }

    public String checkYourself (String stringGuess) {
        String result = "脱靶";

        int index = locationCells.indexOf(stringGuess);

        if (index >= 0) {
            locationCells.remove(index);

            if (locationCells.isEmpty()) {
                result = "击沉";
            } else {
                result = "命中";
            }
        }

        System.out.println(result);
        return result;
    }
}

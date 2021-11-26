package com.xfoss.learningJava;

public class SimpleDotComTestDrive {
	public static void main (String [] args) {
        int numOfGuesses = 0;
        GameHelper helper = new GameHelper ();

		SimpleDotCom dot = new SimpleDotCom ();

        int randomNum = (int) (Math.random() * 5);
        int[] locations = {randomNum, randomNum+1, randomNum+2};
        dot.setLocationCells(locations);

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
	private int [] locationCells;
	private int numOfHits;
	
	public void setLocationCells (int[] locs) {
		locationCells = locs;
	}

    public String checkYourself (String stringGuess) {
        int guess = Integer.parseInt(stringGuess);
        String result = "脱靶";

        for (int cell: locationCells) {
            if (guess == cell) {
                result = "命中";
                ++numOfHits;
                break;
            }
        }

        if (numOfHits == locationCells.length) {
            result = "击沉";
        }

        System.out.println(result);
        return result;
    }
}

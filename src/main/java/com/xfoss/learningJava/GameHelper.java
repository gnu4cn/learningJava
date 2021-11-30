package com.xfoss.learningJava;

import java.io.*;
import java.util.ArrayList;

public class GameHelper {
    private static final String alphabet = "abcdefg";
    private static final int gridLength = 7;
    private ArrayList<ArrayList<String>> alphaCellsList = new ArrayList<ArrayList<String>> ();

    public String getUserInput (String tips) {
        String inputLine = null;
        System.out.println(tips + "   ");

        try {
            BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
            inputLine = is.readLine();
            if(inputLine.length() == 0) return null;
        } catch (IOException e) {
            System.out.format("IOException: %s", e);
        }

        return inputLine.toLowerCase();
    }

    public ArrayList<String> placeDotCom (int comSize) {
        ArrayList<String> alphaCells = new ArrayList<String> ();

        // 0 - Horizontal, 1 - Vertical
        int direction = (int) (Math.round(Math.random()));

        while (true) {
            // When placed in horizontal, the alphabet could be "a-g", but number could only be "0-5"
            // When placed in vertical, the alphabe could only be "a-e", but numbe could be "0-6"
            int rangeX = (direction == 0)
                ? (gridLength - comSize + 1)
                : gridLength;
            int rangeY = (direction == 0)
                ? alphabet.length()
                : (alphabet.length() - comSize + 1);

            int randX = (int) (Math.random() * rangeX);
            int randY = (int) (Math.random() * rangeY);

            // Here we get the first cell
            String initialCell = String.valueOf(alphabet.charAt(randY))
                .concat(Integer.toString(randX));

            // First contruct the alphaCells...
            alphaCells.add(initialCell);
            for (int n = 1; n < comSize; n++) {
                String cell = (direction == 0)
                    ? String.valueOf(alphabet.charAt(randY))
                        .concat(Integer.toString(randX + n))
                    : String.valueOf(alphabet.charAt(randY + n))
                        .concat(Integer.toString(randX));

                alphaCells.add(cell);
            }
            
            // Then check whether the alphaCells available, need to be fixed.
            if (alphaCellsList.size() == 0) break;
            if (alphaCellsList.contains(alphaCells)) {
                alphaCells = new ArrayList<String> ();
                continue;
            }

            outerLoop:{
                for (ArrayList<String> alphaCellsToCheck : alphaCellsList) {
                    for (String cellToCheck : alphaCells) {
                        if (alphaCellsToCheck.contains(cellToCheck)) {
                            alphaCells = new ArrayList<String> ();
                            break outerLoop;
                        }
                    }
                }
            }

            if (alphaCells.size() == 0) continue;
        }

        return alphaCells;
    }
}

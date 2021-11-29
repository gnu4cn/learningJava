package com.xfoss.learningJava;

import java.io.*;
import java.util.ArrayList;

public class GameHelper {
    private static final String alphabet = "abcdefg";
    private static final int gridLength = 7;
    private static final int gridSize = 49;
    private int [] grid = new int [gridSize];
    private int comCount = 0;

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

        String tmp = null;
        int [] coords = new int [comSize];
        int attempts = 0;
        boolean success = false;
        int location = 0;

        comCount++;
        int incr = 1;
        if ((comCount % 2) == 1) {
            incr = gridLength;
        }

        while ( !success & attempts++ < 200 ) {
            location  = (int) (Math.random() * gridSize);

            int x = 0;
            success = true;
            while (success && x < comSize) {
                if (grid[location] == 0) {
                    coords[x+1] = location;
                    location += incr;
                    if (location >= gridSize) {
                        success = false;
                    }
                    if (x>0 && (location % gridLength == 0)) {
                        success = false;
                    }
                } else {
                    success = false;
                }
            }
        }

        int x = 0;
        int row = 0;
        int column = 0;

        while (x < comSize) {
            grid[coords[x]] = 1;
            row = (int) (coords[x] / gridLength);
            column = coords[x] % gridLength;
            tmp = String.valueOf(alphabet.charAt(column));

            alphaCells.add(tmp.concat(Integer.toString(row)));
        }

        return alphaCells;
    }
}

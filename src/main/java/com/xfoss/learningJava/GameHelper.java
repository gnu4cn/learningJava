package com.xfoss.learningJava;

import java.io.*;
import java.util.ArrayList;

public class GameHelper {
    private static final String alphabet = "abcdefg";
    private static final int gridLength = 7;
    private ArrayList<ArrayList<String>> alphaCellsList;

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

        boolean success = false;

        outerLoop: {
            while (!success) {
                // Horizontal, 这个时候“abcdefg”都可以选择，0-6只能选择0-4
                // Vertical，这个时候0-6都能选择，而“abcdefg”中只能选择“a-e”
                int randY = (int) (
                        direction == 0 ? 
                        Math.random() * alphabet.length()
                        : Math.random() * (alphabet.length() - comSize));
                int randX = (int) (
                        direction == 0 ?
                        Math.random() * (gridLength - comSize)
                        : Math.random() * gridLength);

                // 得到一个随机的第一格
                String initialCell = String.valueOf(alphabet.charAt(randY))
                    .concat(Integer.toString(randX));

                // 对这个随机的第一格进行检查
                if (alphaCellsList.size() == 0 ){
                    // alphaCellsList 为空的情况 -- 直接添加
                    alphaCells.add(initialCell);
                    // 后续两个格子，直接添加
                    for (int n = 1; n < comSize; n++) {
                        String cell = null;
                        if (direction == 0) {
                            cell = String.valueOf(alphabet.charAt(randY + n))
                                .concat(Integer.toString(randX));
                        } else {
                            cell = String.valueOf(alphabet.charAt(randY))
                                .concat(Integer.toString(randX + n));
                        }

                        alphaCells.add(cell);
                    }

                    success = true;
                    return alphaCells;
                }
                
                // alphaCellsList 不为空时，就要进行检查了
                for (ArrayList<String> alphaCellsToCheck : alphaCellsList) {
                    System.out.format("The initial cell is %s\n", initialCell);

                    if ( alphaCellsToCheck.contains(initialCell) ) {
                        break outerLoop;
                    } else {
                        alphaCells.add(initialCell);
                    }

                    for (int n = 1; n < comSize; n++) {
                        String cell = null;
                        if (direction == 0) {
                            cell = String.valueOf(alphabet.charAt(randY + n))
                                .concat(Integer.toString(randX));
                        } else {
                            cell = String.valueOf(alphabet.charAt(randY))
                                .concat(Integer.toString(randX + n));
                        }

                        System.out.format("The followed cell is %s\n", cell);

                        if (alphaCellsToCheck.contains(cell)) {
                            break outerLoop;
                        } else {
                            alphaCells.add(cell);
                        }
                    }
                }

                if (alphaCells.size() == 3) {
                    alphaCellsList.add(alphaCells);
                    success = true;
                } else {
                    alphaCells = null;
                    break outerLoop;
                }
            }
        }

        return alphaCells;
    }
}

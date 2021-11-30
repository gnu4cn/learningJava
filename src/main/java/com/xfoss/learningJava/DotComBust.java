package com.xfoss.learningJava;

import java.util.*;

public class DotComBust {
    private GameHelper helper = new GameHelper ();
    private ArrayList<DotCom> dotComList = new ArrayList<DotCom> ();
    private int numOfGuesses = 0;

    private void setUpGame () {
        DotCom one = new DotCom ("Pets.com");
        DotCom two = new DotCom ("eToys.com");
        DotCom three = new DotCom ("Go2.com");

        dotComList.add(one);
        dotComList.add(two);
        dotComList.add(three);

        System.out.format("你的目标是击沉三个点COM域名。\n"
                +"%s, %s, %s\n"
                +"你要用尽可能少的次数，来击沉他们\n", 
                one.getName(), two.getName(), three.getName());

        for (DotCom dC : dotComList) {
            ArrayList<String> newLocation = helper.placeDotCom(3);
            dC.setLocationCells(newLocation);
            //System.out.format("Website: %s\t--\talphaCells: %s\n", dC.getName(), newLocation);
        }
    }

    private void startPlaying () {
        while (!dotComList.isEmpty()) {
            String userGuess = helper.getUserInput("请输入一个格子地址：");
            checkUserGuess (userGuess);
        }
        finishGame();
    }

    private void checkUserGuess (String userInput) {
        numOfGuesses++;

        String result = "脱靶";

        for (int x = 0; x < dotComList.size(); x++) {
            result = dotComList.get(x).checkYourself(userInput);
            if (result.equals("命中")) {
                break;
            }
            if (result.equals("击沉")) {
                dotComList.remove(x);
                break;
            }
        }

        System.out.println(result);
    }

    private void finishGame () {
        System.out.println("所有点COM都被击沉！你的股票现在成了白纸。");

        if (numOfGuesses <= 18) 
            System.out.format("你只用了 %s 次。\n"
                    +"在选定的次数之前，就完成了游戏", numOfGuesses);
        else 
            System.out.format("你只用了 %s 次。\n"
                    +"差点就失败了", numOfGuesses);
    }

    public static void main (String[] args) {
        DotComBust game = new DotComBust ();
        game.setUpGame();
        game.startPlaying();
    }
}

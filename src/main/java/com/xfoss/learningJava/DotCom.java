package com.xfoss.learningJava;

import java.util.ArrayList;

public class DotCom {
    private ArrayList<String> locationCells;
    private String name;

    public DotCom (String n) {
        name = n;
    }

    public String getName () {
        return name;
    }

    public void setLocationCells (ArrayList<String> loc) {
        locationCells = loc;
    }

    public String checkYourself(String userInput) {
        String result = "脱靶";
        int index = locationCells.indexOf(userInput);

        if (index >= 0) {
            locationCells.remove(index);

            if (locationCells.isEmpty()) {
                result = "击沉";
                System.out.format("噢，你已经击沉 %s ：（", name);
            } else {
                result = "命中";
            }
        }

        return result;
    }
}

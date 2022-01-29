package com.xfoss.Utils;

import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class XMenuBar extends JMenuBar {
    public XMenuBar (ArrayList<MenuData> menuList) {
        super();

        menuList.forEach((menuData) -> {
            JMenu menu = new JMenu(menuData.menuText);
            menu.setMnemonic(menuData.vk);
            
            menuData.menuItemList.forEach((menuItemData) -> {
                JMenuItem menuItem = new JMenuItem(menuItemData.menuItemText);
                menuItem.setMnemonic(menuItemData.vk);
                menuItem.addActionListener(menuItemData.handler);

                menu.add(menuItem);
            });

            add(menu);
        });
    }
}

class MenuData {
    String menuText;
    ArrayList<MenuItemData> menuItemList;
    int vk;

    public MenuData(String text, int k, ArrayList<MenuItemData> list) {
        menuText = text;
        vk = k;
        menuItemList = list;
    }
}

class MenuItemData {
    String menuItemText;
    ActionListener handler;
    int vk;

    public MenuItemData (String text, int k, ActionListener h) {
        menuItemText = text;
        vk = k;
        handler = h;
    }
}

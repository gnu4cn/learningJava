package com.xfoss.Utils;

import java.awt.event.*;

public class XMenuBarTestDrive {
    public static void main (String[] args) {
        ArrayList<MenuItemData> menuItemList = new ArrayList<MenuItemData> ();
        menuItemList.add(new MenuItemData("新建（N）", KeyEvent.VK_N, new NewMenuItemListener()));
        menuItemList.add(new MenuItemData("保存（S）", KeyEvent.VK_S, new SaveMenuItemListener()));
        menuItemList.add(new MenuItemData("退出（Q）", KeyEvent.VK_Q, new QuitMenuItemListener()));

        MenuData menu = new MenuData("文件(F)", KeyEvent.VK_F, menuItemList);
        ArrayList<MenuData> menuList = new ArrayList<MenuData> ();
        menuList.add(menu);

        XMenuBar menuBar = new XMenuBar(menuList);

        JFrame frame = new JFrame("com.xfoss.Utils.XMenuBar 测试用例");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setJMenuBar(menuBar);

        frame.setSize(640, 480);
        frame.setVisible(true);
    }

    private class NewMenuItemListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {}
    }

    private class SaveMenuItemListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {}
    }

    private class QuitMenuItemListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {
            frame.dispose();
            System.exit(0);
        }
    }
}

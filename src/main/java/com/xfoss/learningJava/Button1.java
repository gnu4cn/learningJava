package com.xfoss.learningJava;

import javax.swing.*;
import java.awt.*;

public class Button1 {
    public static void main (String[] args) {
        Button1 gui = new Button1 ();
        gui.go();
    }

    public void go () {
        JFrame f = new JFrame ("BorderLayout 示例：一个放在东部区域的按钮");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton btn = new JButton ("因为你愿意所以点我，表示确定你真的要点我");

        f.getContentPane().add(BorderLayout.EAST, btn);
        f.setSize(640, 480);
        f.setVisible(true);
    }
}

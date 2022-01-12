package com.xfoss.learningJava;

import javax.swing.*;
import java.awt.*;

public class Button1 {
    public static void main (String[] args) {
        Button1 gui = new Button1 ();
        gui.go();
    }

    public void go () {
        JFrame f = new JFrame ("BorderLayout 示例：通过Button类的setFont()，让按钮变得更高");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton btn = new JButton ("因为你愿意所以点我，表示确定你真的要点我");
        Font bigFont = new Font("STXingkai", Font.BOLD, 32);
        btn.setFont(bigFont);

        f.getContentPane().add(BorderLayout.NORTH, btn);
        f.setSize(640, 480);
        f.setVisible(true);
    }
}

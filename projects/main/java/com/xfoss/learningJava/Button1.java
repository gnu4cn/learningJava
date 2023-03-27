package com.xfoss.learningJava;

import javax.swing.*;
import java.awt.*;

public class Button1 {
    public static void main (String[] args) {
        Button1 gui = new Button1 ();
        gui.go();
    }

    public void go () {
        JFrame f = new JFrame ("FlowLayout 自动换行的验证");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel();
        p.setBackground(Color.darkGray);

        for (int i = 0; i < 50; i++) {
            JButton btn = new JButton (String.format("按钮 - %d", i));
            p.add(btn);
        }

        f.getContentPane().add(BorderLayout.CENTER, p);
        f.setSize(640, 480);
        f.setVisible(true);
    }
}

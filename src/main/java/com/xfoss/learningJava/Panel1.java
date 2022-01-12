package com.xfoss.learningJava;

import javax.swing.*;
import java.awt.*;

public class Panel1 {
    public static void main (String[] args) {
        Panel1 gui = new Panel1();
        gui.go();
    }

    public void go () {
        JFrame f = new JFrame("FlowLayout 管理器：添加 50 个按钮到面板");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel();
        p.setBackground(Color.darkGray);

        for (int t = 0; t < 50; t++) {
            JButton btn = new JButton(String.format("按钮 - %s", t));
            p.add(btn);
        }
        
        f.getContentPane().add(BorderLayout.EAST, p);
        f.setSize(640, 480);
        f.setVisible(true);
    }
}

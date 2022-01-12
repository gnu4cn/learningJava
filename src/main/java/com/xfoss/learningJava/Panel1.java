package com.xfoss.learningJava;

import javax.swing.*;
import java.awt.*;

public class Panel1 {
    public static void main (String[] args) {
        Panel1 gui = new Panel1();
        gui.go();
    }

    public void go () {
        JFrame f = new JFrame("FlowLayout 管理器：添加两个按钮到面板");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel();
        p.setBackground(Color.darkGray);

        JButton btn = new JButton("吓我一跳！");
        JButton btnTwo = new JButton("祈福");
        p.add(btn);
        p.add(btnTwo);
        
        f.getContentPane().add(BorderLayout.EAST, p);
        f.setSize(640, 480);
        f.setVisible(true);
    }
}

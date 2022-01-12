package com.xfoss.learningJava;

import javax.swing.*;
import java.awt.*;

public class Panel1 {
    public static void main (String[] args) {
        Panel1 gui = new Panel1();
        gui.go();
    }

    public void go () {
        JFrame f = new JFrame("BoxLayout 管理器：把面板的布局管理器从默认的 FlowLayout 修改为 BoxLayout");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel();
        p.setBackground(Color.darkGray);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JButton btn = new JButton ("吓我一跳！");
        JButton btnTwo = new JButton ("祈福");
        p.add(btn);
        p.add(btnTwo);
        
        f.getContentPane().add(BorderLayout.EAST, p);
        f.setSize(640, 480);
        f.setVisible(true);
    }
}

package com.xfoss.learningJava;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SwingComponentsDemo {
    JTextField txtField;
    boolean txtFieldOriginal = true;

    public static void main (String[] args) {
        SwingComponentsDemo gui = new SwingComponentsDemo ();
        gui.go();
    }

    public void go () {
        JFrame f = new JFrame ("Swing 常用组件演示：JLabel 与 JTextField");
        f.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel ();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        f.setContentPane(p);

        JLabel lName = new JLabel ("姓名");
        p.add(lName);

        txtField = new JTextField("你的姓名", 20);
        p.add(txtField);

        JLabel lDesc = new JLabel("简介");
        p.add(lDesc);

        JTextArea text = new JTextArea("关于你......", 10, 20);
        text.setLineWrap(true);

        JScrollPane scroller = new JScrollPane(text);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        p.add(scroller);

        f.setSize(640, 480);
        f.setVisible(true);
    }
}

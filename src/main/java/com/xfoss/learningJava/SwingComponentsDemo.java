package com.xfoss.learningJava;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SwingComponentsDemo {
    JTextField txtField;

    public static void main (String[] args) {
        SwingComponentsDemo gui = new SwingComponentsDemo ();
        gui.go();
    }

    public void go () {
        JFrame f = new JFrame ("Swing 常用组件演示：JLabel 与 JTextField");
        f.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel ();
        f.setContentPane(p);

        JLabel l = new JLabel ("姓名");
        p.add(l);

        txtField = new JTextField("你的姓名", 20);
        txtField.addFocusListener(new txtFieldFocusListener());

        p.add(txtField);

        f.setSize(640, 480);
        f.setVisible(true);
    }

    class txtFieldFocusListener implements FocusListener {
        public void focusGained(FocusEvent e) {
            txtField.setText("");
        }

        public void focusLost(FocusEvent e){}
    }
}

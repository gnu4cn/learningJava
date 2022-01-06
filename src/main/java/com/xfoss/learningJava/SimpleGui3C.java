package com.xfoss.learningJava;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleGui3C implements ActionListener {
    JFrame frame;
    public static void main (String[] args) {
        SimpleGui3C gui = new SimpleGui3C();
        gui.go();
    }

    public void go () {
        frame = new JFrame("事件与绘制图形联动：点击按钮改变圆圈填充颜色实例");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton button = new JButton("改变颜色");
        button.addActionListener(this);

        CustomDrawPanel drawPanel = new CustomDrawPanel();
        
        frame.getContentPane().add(BorderLayout.SOUTH, button);
        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.setSize(640, 480);
        frame.setVisible(true);
    }
    public void actionPerformed (ActionEvent ev) {
        frame.repaint();
    }
}

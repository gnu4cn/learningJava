package com.xfoss.learningJava;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MultiWidgetsAndEvents {
    JFrame frame;
    JLabel label;

    public static void main (String[] args) {
        MultiWidgetsAndEvents gui = new MultiWidgetsAndEvents();
        gui.go();
    }

    public void go () {
        frame = new JFrame("事件与绘制图形联动：多个事件源（小部件）与处理器");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton btnChangeCircle = new JButton("改变圆圈");
        btnChangeCircle.addActionListener(new CircleBtnListener());

        JButton btnChangeLabel = new JButton("改变标签");
        btnChangeLabel.addActionListener(new LabelBtnListener());

        CustomDrawPanel drawPanel = new CustomDrawPanel();
        label = new JLabel("这是一个标签");

        frame.getContentPane().add(BorderLayout.SOUTH, btnChangeCircle);
        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.getContentPane().add(BorderLayout.EAST, btnChangeLabel);
        frame.getContentPane().add(BorderLayout.WEST, label);

        frame.setSize(640, 480);
        frame.setVisible(true);
    }

    class CircleBtnListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {
            frame.repaint();
        }
    }

    class LabelBtnListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {
            label.setText("那真痛！");
        }
    }
}

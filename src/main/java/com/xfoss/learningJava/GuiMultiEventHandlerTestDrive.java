package com.xfoss.learningJava;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GuiMultiEventHandlerTestDrive {
    JFrame frame;
    JLabel label;
    public static void main (String[] args) {
        GuiMultiEventHandlerTestDrive gui = new GuiMultiEventHandlerTestDrive();
        gui.go();
    }

    public void go () {
        frame = new JFrame("事件与绘制图形联动：多个事件源（小部件）与处理器");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton btnChangeCircle = new JButton("改变圆圈");
        btnChangeCircle.addActionListener(this);
        JButton btnChangeLabel = new JButton("改变标签");

        CustomDrawPanel drawPanel = new CustomDrawPanel();
        
        frame.getContentPane().add(BorderLayout.SOUTH, button);
        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.setSize(640, 480);
        frame.setVisible(true);
    }
}

class CircleButtonListener implements ActionListener {
    public void actionPerformed (ActionEvent ev) {
        frame.repaint();
    }
}

class LabelButtonListener implements ActionListener {
    public void actionPerformed (ActionEvent ev) {

    }
}

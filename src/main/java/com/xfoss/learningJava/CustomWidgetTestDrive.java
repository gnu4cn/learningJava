package com.xfoss.learningJava;

import java.awt.*;
import javax.swing.*;

public class CustomWidgetTestDrive {
    public static void main (String[] args) {
        try {
            CustomDrawPanel p = new CustomDrawPanel();
            JFrame f = new JFrame ();
            f.add(p);

            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(640, 480);
            f.setVisible(true);
        } catch (HeadlessException e) {
            System.out.format("没有显示器，无法运行本程序。\n"
                    + "错误代码\n"
                    + "------------------------------\n%s\n", e);			
        }
    }
}

class CustomDrawPanel extends JPanel {
    public void paintComponent (Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        GradientPaint gradient = new GradientPaint(70, 70, Color.blue, 150, 150, Color.orange);

        g2d.setPaint(gradient);
        g2d.fillOval(70, 70, 100, 100);
    }
}

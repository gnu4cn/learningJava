package com.xfoss.learningJava;

import java.awt.*;
import javax.swing.*;

public class CustomWidgetTestDrive {
    public static void main (String[] args) {
        try {
            CustomDrawPanel p = new CustomDrawPanel();
            JFrame f = new JFrame ("使用 Graphics2D 绘制渐变色填充的圆形");
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

        int red = (int) (Math.random() * 256);
        int green = (int) (Math.random() * 256);
        int blue = (int) (Math.random() * 256);
        Color startColor = new Color(red, green, blue);

        red = (int) (Math.random() * 256);
        green = (int) (Math.random() * 256);
        blue = (int) (Math.random() * 256);
        Color endColor = new Color(red, green, blue);

        GradientPaint gradient = new GradientPaint(70, 70, startColor, 150, 150, endColor);
        g2d.setPaint(gradient);
        g2d.fillOval(70, 70, 100, 70);
    }
}

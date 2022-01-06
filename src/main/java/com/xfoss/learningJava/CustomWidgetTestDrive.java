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
        // Image image = new ImageIcon("Default.jpeg").getImage();
        // g.drawImage(image, 3, 4, this);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        int red = (int) (Math.random() * 256);
        int green = (int) (Math.random() * 256);
        int blue = (int) (Math.random() * 256);

        Color randomColor = new Color(red, green, blue);
        g.setColor(randomColor);
        g.fillOval(70, 70, 100, 80);
    }
}

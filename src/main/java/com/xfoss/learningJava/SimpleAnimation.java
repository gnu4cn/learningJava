package com.xfoss.learningJava;

import javax.swing.*;
import java.awt.*;

public class SimpleAnimation {
    // 在GUI主类中构造两个实例变量，表示圆圈
    // 的 x 与 y 坐标
    int x = 20;
    int y = 20;

    public static void main (String[] args) {
        SimpleAnimation gui = new SimpleAnimation ();
        gui.go();
    }

    public void go () {
        JFrame f = new JFrame("Java GUI简单动画示例：内部类的用法之二，内部类与外层类不在同一继承树");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DrawingPanel p = new DrawingPanel();
        f.getContentPane().add(BorderLayout.CENTER, p);

        f.setSize(640, 480);
        f.setVisible(true);

        for (int i = 0; i < 260; i++) {
            x += 2;
            y++;

            p.repaint();
            try {
                Thread.sleep(50);
            } catch (Exception ex){}
        }
    }

    class DrawingPanel extends JPanel {
        public void paintComponent(Graphics g) {
            g.setColor(Color.green);
            g.fillOval(x, y, 40, 40);
        }
    }
}

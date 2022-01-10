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
        // 这些都沿用以前的代码。构造小部件，然后
        // 把这些小部件放到视窗框中
        JFrame f = new JFrame("Java GUI简单动画示例：内部类的用法" +
                "之二，内部类与外层类不在同一继承树");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DrawingPanel p = new DrawingPanel();
        f.getContentPane().add(BorderLayout.CENTER, p);

        f.setSize(640, 480);
        f.setVisible(true);

        // 这里就是运动发生的地方了！
        //
        // 重复了 260 次
        for (int i = 0; i < 260; i++) {
            // 让 x 与 y 坐标逐次递增
            x += 2;
            y++;

            // 告诉绘制面板对自己进行重绘（因此就可以在
            // 新的位置看到那个圆圈）
            p.repaint();

            // 这里的代码让运动慢下来（否则圆圈就会移动过快，甚至看不出
            // 他是在移动了）。关于这里的 Thread 对象，并没有要求对其有
            // 了解，所以不必焦虑。在后面的第 15 章会学到有关线程的内容。
            try {
                Thread.sleep(50);
            } catch (Exception ex){}
        }
    }

    // 现在这就是个内部类了。
    class DrawingPanel extends JPanel {
        public void paintComponent(Graphics g) {
            g.setColor(Color.white);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());

            g.setColor(Color.green);
            // 这里使用了外层类中持续更新的 x 与 y 坐标。
            g.fillOval(x, y, 40, 40);
        }
    }
}

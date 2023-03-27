package com.xfoss.learningJava;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TextAreaDemo {
    JTextArea text;

    public static void main(String[] args) {
        TextAreaDemo gui = new TextAreaDemo();
        gui.go();
    }

    public void go () {
        JFrame f = new JFrame("JTextArea 演示");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel();
        JButton btn = new JButton("点一下就好");
        btn.addActionListener(new btnActionListener());

        text = new JTextArea("示例内容\n", 10, 20);
        text.setLineWrap(true);

        JScrollPane scroller = new JScrollPane(text);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        p.add(scroller);

        f.getContentPane().add(BorderLayout.CENTER, p);
        f.getContentPane().add(BorderLayout.SOUTH, btn);
        f.setSize(640, 480);
        f.setVisible(true);
    }

    class btnActionListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {
            text.append("按钮已被点击\n");
            System.out.format("文本区内容为：\n%s", text.getText());
        }
    }
}

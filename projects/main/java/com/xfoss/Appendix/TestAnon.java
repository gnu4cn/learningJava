package com.xfoss.Appendix;

import java.awt.event.*;
import javax.swing.*;

public class TestAnon extends JFrame {

    public static void main (String[] args) {
        new TestAnon();
    }

    public TestAnon () {

        super("TestAnon");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton btn = new JButton ("点击");
        btn.addActionListener(new ActionListener () {
            public void actionPerformed (ActionEvent ev) {
                System.exit(0);
            }
        });

        getContentPane().add(btn);

        setBounds(50, 50, 640, 480);
        pack();
        setVisible(true);
    }
}

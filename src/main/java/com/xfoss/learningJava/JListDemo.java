package com.xfoss.learningJava;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class JListDemo {
    JList l;
    public static void main (String[] args) {
        String [] listEntries = {"apple", "banana", "carriot", "donut",
            "fish", "egg"};

        JListDemo gui = new JListDemo();
        gui.go(listEntries);
    }

    public void go (String[] lEntries) {
        JFrame f = new JFrame("JList Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel();
        p.setBackground(Color.darkGray);

        l = new JList(lEntries);
        l.setVisibleRowCount(4);
        l.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        l.addListSelectionListener(new ListSelectionHandler());

        JScrollPane s = new JScrollPane(l);
        s.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        s.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        p.add(s);

        f.getContentPane().add(BorderLayout.CENTER, p);
        f.setSize(640, 480);
        f.setVisible(true);

    }

    class ListSelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent ev) {
            if(!ev.getValueIsAdjusting()) {
                String selection = (String) l.getSelectedValue();
                System.out.format("所选的是：%s\n", selection);
            }
        }
    }
}

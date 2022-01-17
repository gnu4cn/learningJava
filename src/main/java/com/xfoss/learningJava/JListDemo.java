package com.xfoss.learningJava;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

public class JListDemo {
    JList<String> l;

    public static void main (String[] args) {
        JListDemo gui = new JListDemo();
        gui.go();
    }

    public void go () {
        JFrame f = new JFrame("JList Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel();
        p.setBackground(Color.darkGray);

        String [] listEntries = {"apple", "banana", "carriot", "donut", "fish", "egg", "grapes"};
        l = new JList<String>(listEntries);
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
                System.out.format("所选的是：%s\n", l.getSelectedValue());
            }
        }
    }
}

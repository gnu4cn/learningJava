package com.xfoss.learningJava;

import javax.swing.*;
import java.awt.*;

public class Button1 {
    public static void main (String[] args) {
        Button1 gui = new Button1 ();
        gui.go();
    }

    public void go () {
        JFrame f = new JFrame ("BorderLayout 示例：关于中部区域");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton btnEast = new JButton ("东部");
        JButton btnWest = new JButton ("西部");
        JButton btnNorth = new JButton ("北部");
        JButton btnSouth = new JButton ("南部");
        JButton btnCenter = new JButton ("中央");

        f.getContentPane().add(BorderLayout.EAST, btnEast);
        f.getContentPane().add(BorderLayout.WEST, btnWest);
        f.getContentPane().add(BorderLayout.NORTH, btnNorth);
        f.getContentPane().add(BorderLayout.SOUTH, btnSouth);
        f.getContentPane().add(BorderLayout.CENTER, btnCenter);

        f.setSize(640, 480);
        f.setVisible(true);
    }
}

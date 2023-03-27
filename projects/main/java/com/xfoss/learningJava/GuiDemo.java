package com.xfoss.learningJava;

import javax.swing.*;
import java.awt.*;

public class GuiDemo {
	public static void main (String[] args) {
		try {
			JFrame f = new JFrame ();
			JButton b = new JButton ("点我");
			
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.getContentPane().add(b);
			f.setSize(300, 300);
			f.setVisible(true);
		} catch (HeadlessException e) {
			System.out.format("没有显示器，无法运行本程序。\n"
					+ "错误代码\n"
					+ "------------------------------\n%s\n", e);
		}
	}
}

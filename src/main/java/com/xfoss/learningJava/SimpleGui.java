package com.xfoss.learningJava;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class SimpleGui implements ActionListener {
	JButton b;
	
	public static void main (String[] args) {
		try {
		SimpleGui gui = new SimpleGui();
		gui.go();
		} catch (HeadlessException e) {
			System.out.format("没有显示器，无法运行本程序。\n"
					+ "错误代码\n"
					+ "------------------------------\n%s\n", e);			
		}
	}
	
	public void go () {
		JFrame f = new JFrame ();
		b = new JButton ("点我");
		
		b.addActionListener(this);
		
		f.getContentPane().add(b);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(300, 300);
		f.setVisible(true);
	}
	
	public void actionPerformed (ActionEvent e) {
		b.setText("我已经被点了！");
	}
}

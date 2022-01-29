package com.xfoss.QuizCard;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class QuizCardPlayer {

    private JTextArea display;
    private JTextArea answer;
    private ArrayList<QuizCard> cardList;
    private QuizCard currentCard;
    private int currentCardIndex;
    private JFrame frame;
    private JButton nextBtn;
    private boolean isShowAnswer;

    public static void main (String[] args) {
        QuizCardPlayer player = new QuizCardPlayer();
        player.go();
    }

    public void go () {
        // 构建 GUI
        JPanel mainPanel = new JPanel();
        Font bigFont = new Font("sanserif", Font.BOLD, 24);

        display = new JTextArea(10, 20);
        display.setFont(bigFont);
        display.setLineWrap(true);
        display.setEditable(false);

        JScrollPane qScroller = new JScrollPane(display);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        nextBtn = new JButton("揭开答案");
        nextBtn.addActionListener(new NextCardListener());

        mainPanel.add(qScroller);
        mainPanel.add(nextBtn);

        JMenuBar menuBar = new JMenuBar ();
        JMenu fileMenu = new JMenu("文件（F）");
        JMenuItem loadMenuItem = new JMenuItem("加载卡片集（L）");
        loadMenuItem.addActionListener(new OpenMenuListener());
        fileMenu.add(loadMenuItem);
        menuBar.add(fileMenu);

        frame = new JFrame("测试卡播放器");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(640, 500);
        frame.setVisible(true);
    }

    private class NextCardListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {}
    }

    private class OpenMenuListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {}
    }

}

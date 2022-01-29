package com.xfoss.QuizCard;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class QuizCardPlayer extends {

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
    }

    public QuizCardPlayer () {
        // 构建 GUI
        super("测试卡播放器");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem loadMenuItem = new JMenuItem("加载卡片集（L）");
        loadMenuItem.setMnemonic(KeyEvent.VK_L);
        loadMenuItem.addActionListener(new OpenMenuListener());

        JMenuItem quitMenuItem = new JMenuItem("退出（Q）");
        quitMenuItem.addActionListener(new QuitMenuItemListener());
        quitMenuItem.setMnemonic(KeyEvent.VK_Q);

        fileMenu.add(loadMenuItem);
        fileMenu.add(quitMenuItem);
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
        getContentPane().add(BorderLayout.CENTER, mainPanel);
        setSize(640, 500);
        setVisible(true);
    }

    private class QuitMenuItemListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {
            frame.dispose();
            System.exit(0);
        }
    }

    private class NextCardListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {}
    }

    private class OpenMenuListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {}
    }

}

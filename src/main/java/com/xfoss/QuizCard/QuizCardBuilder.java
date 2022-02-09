package com.xfoss.QuizCard;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import com.xfoss.Utils.*;

public class QuizCardBuilder extends JFrame{
    private JTextArea question;
    private JTextArea answer;
    private ArrayList<QuizCard> cardList;

    public static void main (String[] args) {
        new QuizCardBuilder("测试卡构建器");
    }

    public QuizCardBuilder (String windowTitle) {
        // 构建出 GUI
        super(windowTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        URL icoURI = getClass().getResource("/images/flashcards.png");
        ImageIcon ico = new ImageIcon(icoURI);
        setIconImage(ico.getImage());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        Font bigFont = new Font("sanserif", Font.BOLD, 24);

        question = new JTextArea(6, 20);
        question.setLineWrap(true);
        question.setWrapStyleWord(true);
        question.setFont(bigFont);

        JScrollPane qScroller = new JScrollPane(question);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        answer = new JTextArea(6, 20);
        answer.setLineWrap(true);
        answer.setWrapStyleWord(true);
        answer.setFont(bigFont);

        JScrollPane aScroller = new JScrollPane(answer);
        aScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        aScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JButton nextBtn = new JButton("下一卡片");
        nextBtn.addActionListener(new NextCardListener());

        cardList = new ArrayList<QuizCard> ();

        JLabel qLabel = new JLabel("问题：");
        JLabel aLabel = new JLabel("答案：");

        mainPanel.add(qLabel);
        mainPanel.add(qScroller);
        mainPanel.add(aLabel);
        mainPanel.add(aScroller);
        mainPanel.add(nextBtn);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("文件（F）");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem newMenuItem = new JMenuItem("新建（N）");
        newMenuItem.addActionListener(new NewMenuItemListener());
        newMenuItem.setMnemonic(KeyEvent.VK_N);

        JMenuItem saveMenuItem = new JMenuItem("保存（S）");
        saveMenuItem.addActionListener(new SaveMenuItemListener());
        saveMenuItem.setMnemonic(KeyEvent.VK_S);

        JMenuItem quitMenuItem = new JMenuItem("退出（Q）");
        quitMenuItem.addActionListener(new QuitMenuItemListener());
        quitMenuItem.setMnemonic(KeyEvent.VK_Q);

        fileMenu.add(newMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(quitMenuItem);

        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
        getContentPane().add(BorderLayout.CENTER, mainPanel);
        setSize(576, 720);
        setVisible(true);
    }

    private class QuitMenuItemListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {
            dispose();
            System.exit(0);
        }
    }

    private class NextCardListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {
            if(question.getText().length() > 0 && answer.getText().length() > 0) {
                QuizCard card = new QuizCard(question.getText(), answer.getText());
                cardList.add(card);
                clearCard();
            }

            question.requestFocus();
        }
    }

    private class NewMenuItemListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {
            cardList.clear();
            clearCard();
        }
    }

    private class SaveMenuItemListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {
            QuizCard card = new QuizCard(question.getText(), answer.getText());
            cardList.add(card);

            JFileChooser fileSave = new JFileChooser();
            fileSave.showSaveDialog(QuizCardBuilder.this);
            File fileChoice = fileSave.getSelectedFile();
            if( fileChoice != null)saveFile(fileChoice);
        }
    }

    private void clearCard () {
        question.setText("");
        answer.setText("");
        question.requestFocus();
    }

    private void saveFile (File f) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));

            for (QuizCard c:cardList) {
                if(c.getQuestion().length() > 0 && c.getAnswer().length() > 0)
                    writer.write(String.format("%s/%s\n", c.getQuestion(), c.getAnswer()));
            }

            writer.close();
        } catch (IOException ex) {
            System.out.println("无法将卡片清单 cardList 写出");
            ex.printStackTrace();
        }
    }
}

class QuizCard {
    private String question;
    private String answer;

    public QuizCard(String q, String a) {
        question = q;
        answer = a;
    }

    public String getQuestion () {return question;}
    public String getAnswer() {return answer;}
}

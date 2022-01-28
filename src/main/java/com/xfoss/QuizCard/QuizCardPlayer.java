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
    }
}

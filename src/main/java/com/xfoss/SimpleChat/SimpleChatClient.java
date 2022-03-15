package com.xfoss.SimpleChat;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleChatClient extends JFrame {
    JTextArea incoming;
    JTextField outgoing;
    BufferedReader reader;
    PrintWriter writer;
    Socket sock;

    public static void main (String[] args) {
        new SimpleChatClient("简单的聊天客户端");
    }

    public SimpleChatClient (String winTitle) {
        super(winTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();

        incoming = new JTextArea(15, 50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);

        JScrollPane qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        outgoing = new JTextField(20);

        JButton sendBtn = new JButton("发送");
        sendBtn.addActionListener(new SendBtnListener());

        mainPanel.add(qScroller);
        mainPanel.add(outgoing);
        mainPanel.add(sendBtn);

        setUpNetworking();

        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        getContentPane().add(BorderLayout.CENTER, mainPanel);
        setSize(800, 600);
        setVisible(true);
    }

    class SendBtnListener implements ActionListener {
        public void actionPerformed(ActionEvent ev){
            try {
                writer.println(outgoing.getText());
                writer.flush();
            } catch (Exception ex){ ex.printStackTrace(); }

            outgoing.setText("");
            outgoing.requestFocus();
        }
    }

    class IncomingReader implements Runnable {
        public void run () {
            String msg;

            try{
                while ((msg = reader.readLine()) != null) {
                    System.out.format("读取到消息 %s", msg);
                    incoming.append(String.format("%s \n", msg));
                }
            } catch (Exception ex){ex.printStackTrace();}
        }
    }

    public void setUpNetworking () {
        try {
            sock = new Socket("192.168.153.134", 15000);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream(), "UTF-8");
            reader = new BufferedReader (streamReader);
            writer = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-8"));
            System.out.println("网络通信已建立");
        } catch (IOException ex){ex.printStackTrace();}
    }
}

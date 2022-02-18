package com.xfoss.SimpleChat;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleChatClientA extends JFrame {

    JTextField outgoing;
    PrintWriter writer;
    Socket sock;

    public SimpleChatClientA () {
        super("搞笑的简单聊天客户端");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();

        outgoing = new JTextField(20);

        JButton sendButton = new JButton("发送");
        sendButton.addActionListener(new SendButtonListener());

        mainPanel.add(outgoing);
        mainPanel.add(sendButton);

        getContentPane().add(BorderLayout.CENTER, mainPanel);

        setUpNetworking();

        setSize(640, 480);
        setVisible(true);

        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent winEvt) {
                if(writer != null) writer.close();
                System.exit(0);
            }
        });
    }

    public class SendButtonListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {
            try {
                writer.println(outgoing.getText());
                writer.flush();
            } catch (Exception ex) {ex.printStackTrace();}

            outgoing.setText("");
            outgoing.requestFocus();
        }
    }

    private void setUpNetworking () {
        try {
            sock = new Socket("127.0.0.1", 5000);
            writer = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-8"));
            System.out.println("网络通信已建立");
        } catch (IOException ex) {ex.printStackTrace();}
    }

    public static void main (String[] args) {
        new SimpleChatClientA();
    }
}

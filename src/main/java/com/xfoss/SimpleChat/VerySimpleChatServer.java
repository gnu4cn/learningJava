package com.xfoss.SimpleChat;

import java.io.*;
import java.net.*;
import java.util.*;

public class VerySimpleChatServer {
    ArrayList clientOutputStreams;

    public VerySimpleChatServer () {
        clientOutputStreams = new ArrayList();

        try {
            ServerSocket serverSock = new ServerSocket(5000);

            while(true) {
                Socket clientSocket = serverSock.accept();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
                clientOutputStreams.add(writer);

                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                System.out.println("已获取到一个连接");
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public class ClientHandler implements Runnable {
        BufferedReader reader;
        Socket sock;

        public ClientHandler (Socket clientSocket) {
            try {
                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream(), "UTF-8");
                reader = new BufferedReader(isReader);
            } catch (Exception ex) {ex.printStackTrace();}
        }

        public void run () {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.format("读取到：%s\n", message);
                    tellEveryOne(message);
                }
            } catch (Exception ex) {ex.printStackTrace();}
        }
    }
    
    public void tellEveryOne(String message) {
        Iterator it = clientOutputStreams.iterator();

        while(it.hasNext()) {
            try {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
            } catch (Exception ex) {ex.printStackTrace();}
        }
    }

    public static void main(String[] args) {
        new VerySimpleChatServer();
    }
}

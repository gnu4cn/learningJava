package com.xfoss.BeatBox;

import java.io.*;
import java.net.*;
import java.util.*;

public class MusicServer {

    ArrayList<ObjectOutputStream> clientOutputStreams;

    public MusicServer () {
        clientOutputStreams = new ArrayList<ObjectOutputStream> ();

        try {
            ServerSocket serverSock = new ServerSocket(14242);

            while(true) {
                Socket clientSock = serverSock.accept();
                ObjectOutputStream out = new ObjectOutputStream(clientSock.getOutputStream());
                clientOutputStreams.add(out);

                Thread t = new Thread(new ClientHandler(clientSock));
                t.start();

                System.out.println("已获取到一个连接");
            }
        } catch(Exception ex) {ex.printStackTrace();}
    }

    public class ClientHandler implements Runnable {

        ObjectInputStream in;
        Socket clientSock;

        public ClientHandler (Socket s) {
            try {
                clientSock = s;
                in = new ObjectInputStream(clientSock.getInputStream());
            } catch (Exception ex) {ex.printStackTrace();}
        }

        public void run () {
            Object o2 = null;
            Object o1 = null;

            try {
                while ((o1 = in.readObject()) != null) {
                    o2 = in.readObject();
                    System.out.println("已读取到两个对象");
                    tellEveryone(o1, o2);
                }
            } catch (Exception ex) {ex.printStackTrace();}
        }
    }

    public void tellEveryone(Object o1, Object o2) {
        Iterator it = clientOutputStreams.iterator();
        while(it.hasNext()) {
            try {
                ObjectOutputStream out = (ObjectOutputStream) it.next();
                out.writeObject(o1);
                out.writeObject(o2);
            } catch (Exception ex) {ex.printStackTrace();}
        }
    }

    public static void main (String[] args) {
        new MusicServer();
    }
}

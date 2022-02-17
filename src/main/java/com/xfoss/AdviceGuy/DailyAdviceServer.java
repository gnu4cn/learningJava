package com.xfoss.AdviceGuy;

import java.io.*;
import java.net.*;

public class DailyAdviceServer {
    String[] adviceList = {
        "少食多餐", 
        "买些紧身牛仔裤。他们不会让你看起来显胖。", 
        "一个字：不合适",
        "就今天而言，要诚实，告诉你的老板你的真实想法。",
        "对于这个发型，你应该三思而后行"
        };

    public DailyAdviceServer () {
        try {
        ServerSocket serverSock = new ServerSocket(4242);

        while (true) {
            Socket sock = serverSock.accept();

            PrintWriter writer = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-8"));
            String advice = getAdvice();
            writer.println(advice);
            writer.close();

            System.out.format("RemoteSocketAdress: %s, LocalSocketAddress: %s, 发送的劝解为：%s\n", 
                    sock.getRemoteSocketAddress(),
                    sock.getLocalSocketAddress(),
                    advice);
        }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String getAdvice() {
        int random = (int) (Math.random() * adviceList.length);
        return adviceList[random];
    }

    public static void main (String[] args) {new DailyAdviceServer();}
}

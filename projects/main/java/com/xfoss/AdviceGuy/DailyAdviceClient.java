package com.xfoss.AdviceGuy;

import java.io.*;
import java.net.*;

public class DailyAdviceClient {
    public DailyAdviceClient () {
        try {
            Socket s = new Socket("192.168.153.134", 4242);

            InputStreamReader streamReader = new InputStreamReader(s.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(streamReader);

            String advice = reader.readLine();
            System.out.format("本地Socket地址: %s, 远端Socket地址：%s, 今日宜：%s\n", 
                    s.getLocalSocketAddress(), 
                    s.getRemoteSocketAddress(),
                    advice);

            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new DailyAdviceClient();
    }
}

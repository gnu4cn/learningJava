package com.xfoss.NFCUtils;

import com.xfoss.learningJava.*;

public class CodeTranslator {
    public static void main (String [] args){
        GameHelper helper = new GameHelper();

        if (args.length == 0) {
            System.out.format("        -------------------------                       \n\n" +
                    "本程序将读卡器读到的手机NFC卡号，转换成公司门禁系统可以识别的形式\n\n" + 
                    "转换规则：\n" +
                    "\t\"aabbcc\" -> \"ccbbaa\" -> Integer.parseInt(\"ccbbaa\", 16)\n\n" +
                    "        -------------------------                       \n" +
                    "");

            while(true){
                String tmpHex = helper.getUserInput("请输入手机NFC卡号（'q' 退出程序）：");

                if(tmpHex.equals("q")) break;

                try {
                    int tmpInt = Integer.parseInt(tmpHex, 16);

                    String [] subStrings = tmpHex.split("(?<=\\G.{2})");
                    String targetHex = "";
                    for (int n = subStrings.length - 1; n >=0; n--){
                        targetHex = targetHex + subStrings[n];
                    }

                    System.out.format("该NFC卡号 %s 对应的门禁机编号为：%s\n", tmpHex, Integer.parseInt(targetHex, 16));
                } catch (NumberFormatException e) {
                    System.out.format("%s -- 不是一个16进制数，需要重新输入。\n", tmpHex);
                    continue;
                }
            }
        }
    }
}

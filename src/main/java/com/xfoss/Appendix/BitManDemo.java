package com.xfoss.Appendix;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BitManDemo {
    public static void main (String[] args) {
        System.out.format("10 的二进制形式：\t\t\t\t%s\n非运算（二进制位翻转）后的 ~10 二进制形式：\t%s\n\n",
                String.format("%8s", Integer.toBinaryString(10)).replaceAll(" ", "0"), 
                Integer.toBinaryString(~10).length() > 8 ? 
                Integer.toBinaryString(~10).substring(Integer.toBinaryString(~10).length()-8, Integer.toBinaryString(~10).length())
                : String.format("%8s", Integer.toBinaryString(~10)).replaceAll(" ", "0")
                );

        System.out.format("字符 a 的二进制形式：\t\t\t\t%s\n非运算（二进制位翻转）后的 ~a 二进制形式：\t%s\n\n",
                String.format("%8s", Integer.toBinaryString('a')).replaceAll(" ", "0"), 
                Integer.toBinaryString(~'a').length() > 8
                ? Integer.toBinaryString(~'a').substring(Integer.toBinaryString(~'a').length()-8, Integer.toBinaryString(~'a').length())
                : String.format("%8s", Integer.toBinaryString(~'a')).replaceAll(" ", "0"));

        System.out.format("字符串 \"This is a test.\" 的二进制形式：\n%s\n\n",
                prettyBin(
                    convertByteArraysToBin("This is a test.".getBytes(StandardCharsets.UTF_8)),
                    8, " ")); 

        System.out.format("整数 10 和 6 的按位与运算 10 & 6：\n10\t->\t%s\n6\t->\t%s\n结果：\t\t%s -> %d\n\n",
                String.format("%8s", Integer.toBinaryString(10)).replaceAll(" ", "0"),
                String.format("%8s", Integer.toBinaryString(6)).replaceAll(" ", "0"),
                String.format("%8s", Integer.toBinaryString(10 & 6)).replaceAll(" ", "0"),
                10 & 6);

        System.out.format("整数 10 和 6 的按位或运算 10 | 6：\n10\t->\t%s\n6\t->\t%s\n结果：\t\t%s -> %d\n\n",
                String.format("%8s", Integer.toBinaryString(10)).replaceAll(" ", "0"),
                String.format("%8s", Integer.toBinaryString(6)).replaceAll(" ", "0"),
                String.format("%8s", Integer.toBinaryString(10 | 6)).replaceAll(" ", "0"),
                10 | 6);

        System.out.format("整数 10 和 6 的按位异或运算 10 ^ 6：\n10\t->\t%s\n6\t->\t%s\n结果：\t\t%s -> %d\n\n",
                String.format("%8s", Integer.toBinaryString(10)).replaceAll(" ", "0"),
                String.format("%8s", Integer.toBinaryString(6)).replaceAll(" ", "0"),
                String.format("%8s", Integer.toBinaryString(10 ^ 6)).replaceAll(" ", "0"),
                10 ^ 6);

        System.out.format("负整型数 -11 的向右移位运算 -11 >> 2:\n-11\t->\t%s\n结果：\t\t%s -> %d\n\n",
                Integer.toBinaryString(-11).length() < 32
                ? prettyBin(String.format("%32s", Integer.toBinaryString(-11)).replaceAll(" ", "0"), 8, " ")
                : prettyBin(Integer.toBinaryString(-11), 8, " "),
                Integer.toBinaryString(-11 >> 2).length() < 32
                ? prettyBin(String.format("%32s", Integer.toBinaryString(-11 >> 2)).replaceAll(" ", "0"), 8, " ")
                : prettyBin(Integer.toBinaryString(-11 >> 2), 8, " "),
                -11 >> 2);

        System.out.format("正整型数 11 的向右移位运算 11 >> 2:\n11\t->\t%s\n结果：\t\t%s -> %d\n\n",
                Integer.toBinaryString(11).length() < 32
                ? prettyBin(String.format("%32s", Integer.toBinaryString(11)).replaceAll(" ", "0"), 8, " ")
                : prettyBin(Integer.toBinaryString(11), 8, " "),
                Integer.toBinaryString(11 >> 2).length() < 32
                ? prettyBin(String.format("%32s", Integer.toBinaryString(11 >> 2)).replaceAll(" ", "0"), 8, " ")
                : prettyBin(Integer.toBinaryString(11 >> 2), 8, " "),
                11 >> 2);

        System.out.format("负整型数 -11 的向右做无符号移位运算 -11 >>> 2:\n-11\t->\t%s\n结果：\t\t%s -> %,d\n\n",
                Integer.toBinaryString(-11).length() < 32
                ? prettyBin(String.format("%32s", Integer.toBinaryString(-11)).replaceAll(" ", "0"), 8, " ")
                : prettyBin(Integer.toBinaryString(-11), 8, " "),
                Integer.toBinaryString(-11 >>> 2).length() < 32
                ? prettyBin(String.format("%32s", Integer.toBinaryString(-11 >>> 2)).replaceAll(" ", "0"), 8, " ")
                : prettyBin(Integer.toBinaryString(-11 >>> 2), 8, " "),
                -11 >>> 2);

        System.out.format("负整型数 -11 的向左移位运算 -11 << 2:\n-11\t->\t%s\n结果：\t\t%s -> %,d\n\n",
                Integer.toBinaryString(-11).length() < 32
                ? prettyBin(String.format("%32s", Integer.toBinaryString(-11)).replaceAll(" ", "0"), 8, " ")
                : prettyBin(Integer.toBinaryString(-11), 8, " "),
                Integer.toBinaryString(-11 << 2).length() < 32
                ? prettyBin(String.format("%32s", Integer.toBinaryString(-11 << 2)).replaceAll(" ", "0"), 8, " ")
                : prettyBin(Integer.toBinaryString(-11 << 2), 8, " "),
                -11 << 2);

        byte[] bytes = "你".getBytes(StandardCharsets.UTF_8);
        String bin = convertByteArraysToBin(bytes);
        System.out.format("Unicode 字符 \"你\" 占用字节数：%d，二进制形式：\n%s\n",
                bytes.length,
                prettyBin(bin, 8, " ")); 
    }

    public static String convertByteArraysToBin(byte[] input) {

        StringBuilder result = new StringBuilder();
        for (byte b: input) {
            int val = b;

            for (int i = 0; i < 8; i++) {
                result.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return result.toString();
    }

    public static String prettyBin (String b, int blockSize, String separator) {
        
        List<String> result = new ArrayList<> ();
        int index = 0;
        while (index < b.length()) {
            result.add(b.substring(index, Math.min(index + blockSize, b.length())));
            index += blockSize;
        }

        return result.stream().collect(Collectors.joining(separator));
    }
}

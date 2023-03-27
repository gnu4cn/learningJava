package com.xfoss.learningJava;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressDemo {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress ip = InetAddress.getByName("10.12.10.175");

        System.out.format("%s - %s", ip.getHostAddress(), ip.getHostName());
    }
}

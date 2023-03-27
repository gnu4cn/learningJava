package com.xfoss.learningJava;

public class GetEnv {
    public static void main(String[] args) {
        String SNMP_ADMIN = System.getenv("SNMP_ADMIN");
        String AUTH_KEY = System.getenv("SNMP_AUTH_KEY");
        String PRIV_KEY = System.getenv("SNMP_PRIV_KEY");

        System.out.format("ADMIN: %s, AUTH_KEY: %s, PRIV_KEY: %s", SNMP_ADMIN, AUTH_KEY, PRIV_KEY);
    }
}

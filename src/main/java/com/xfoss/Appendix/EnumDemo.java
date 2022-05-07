package com.xfoss.Appendix;

public class EnumDemo {
    public static final int JERRY = 1;
    public static final int BOBBY = 2;
    public static final int PHIL = 3;

    public enum Members { JERRY, BOBBY, PHIL };

    public Members selectedBandMember;

    public static void main (String[] args) {
        new EnumDemo(1);
        System.out.println(Members.JERRY);
    }

    EnumDemo (int n) {
        oldWay(n);
    }

    public void oldWay (int selectedBandMember) {
        if(selectedBandMember == JERRY) System.out.println("JERRY");
    }
}

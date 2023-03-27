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

        // 将枚举值赋值给枚举类型的变量。
        Members n = Members.BOBBY;
        // 这两个语句工作正常！会打印出 “Rat Dog"。
        if (n.equals(Members.JERRY)) System.out.println("Jerrrrry!");
        if (n == Members.BOBBY) System.out.println("Rat Dog");

        Members ifName = Members.PHIL;
        // 突击测验！输出会是什么呢？
        // 答案：¡ʎpᴉssɐƆ dǝǝp oƃ
        switch (ifName) {
            case JERRY: System.out.print("make it sing ");
            case PHIL: System.out.print("go deep ");
            case BOBBY: System.out.print("Cassidy! ");
        }

    }

    EnumDemo (int n) {
        oldWay(n);
    }

    public void oldWay (int selectedBandMember) {
        if(selectedBandMember == JERRY) System.out.println("JERRY");
    }

}

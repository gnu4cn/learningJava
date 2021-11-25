package com.xfoss.learningJava;

public class EchoTestDrive {
    public static void main (String[] args) {
        Echo e1 = new Echo ();
        Echo e2 = new Echo ();
        
        int y;
        
        int x = 0;
        System.out.println(x+y);
        while ( x<4 ) {
        	e1.hello();
        	++e1.count;
        	if ( x==3 ) {
        		++e2.count;
        	}
        	if ( x>0 ) {
        		e2.count += e1.count;
        	}
        	System.out.format("x = %s\te1.count = %s\te2.count = %s\n", x, e1.count, e2.count);
        	++x;
        }
        
        System.out.println(e2.count);
    }
}

class Echo {
    int count = 0;
    void hello () {
        System.out.println ("helloooo....");
    }
}

package com.xfoss.learningJava;

public class MyFirstApp {
	public static void main (String[] args) {
		int x = 3;
		String name = "Dirk";
		x = x * 17;
		String output = String.format("x is %s.\t%s\n", x, name);
		System.out.println(output);
		double d = Math.random();
		System.out.format("d is %s.\n", d);
		
		while (x > -10) {
			System.out.println(x);
			--x;
		}
		
		for (; x < 100; ++x) {
			System.out.format("x is now %s.\n", x);
		}
		
		if(x > 100) {
			System.out.println("x is bigger than 100");
		}
		else {
			System.out.format("x is lower than %s.\n", 100);
		}
		
		
		
	}
}

package com.xfoss.learningJava;

public class Player {
	int num = 0;
	boolean guessed = false;
	
	public void guess () {
		num = (int) (Math.random() * 10);
		System.out.format("I'm guessing %s\n", num);
	}
}

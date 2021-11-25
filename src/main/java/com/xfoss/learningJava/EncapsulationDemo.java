package com.xfoss.learningJava;

public class EncapsulationDemo {
	public static void main (String[] args) {
		Dog one = new Dog ();
		one.setSize(75);
		Dog two = new Dog ();
		two.setSize(8);
		
		System.out.format ("Dog one: %s\n", one.getSize());
		System.out.format ("Dog two: %s\n", two.getSize());
		
		one.bark();
		two.bark();
	}
}

class Dog {
	private int size;
	
	public int getSize () {
		return size;
	}
	
	public void setSize (int s) {
		size = s;
	}
	
	void bark () {
		if (size > 60) {
			System.out.println("Woof! Woof!");
		} else if (size > 14) {
			System.out.println("Ruff! Ruff!");
		} else {
			System.out.println("Yip! Yip!");
		}
	}
}

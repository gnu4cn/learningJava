package com.xfoss.learningJava;

public class BeerSong {
	public static void main(String[] args) {
		int beerNum = 39;
		String word = "bottles";
		
		while (beerNum > 0) {
			
			if (beerNum == 1) {
				word = "bottle";
			}
			
			System.out.format("%s %s of beer on the wall.\n", beerNum, word);
			System.out.format("%s %s of beer.\n", beerNum, word);
			System.out.println("Take one down.\nPass it around.\n");
			--beerNum;
			
			if (beerNum == 0) {
				System.out.format("No more bottles of beer on the wall.\n");
			}
			
		}
	}
}

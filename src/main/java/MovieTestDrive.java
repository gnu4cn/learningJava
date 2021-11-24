package com.xfoss.learningJava;

class Movie {
	String title;
	String genre;
	int rating;
	
	void playIt () {
		System.out.format("Playing the \"%s\"\n\tGenre: %s\n\tRating: %s\n\n", 
				this.title, 
				this.genre, 
				this.rating
				);
	}
}

public class MovieTestDrive {
	public static void main(String[] args) {
		Movie one = new Movie();
		one.title = "Gone with the Stock";
		one.genre = "Tragic";
		one.rating = -2;
		
		Movie two = new Movie();
		two.title = "Lost in Cubicle Space";
		two.genre = "喜剧";
		two.rating = 5;
		
		Movie three = new Movie();
		three.title = "字节公会";
		three.genre = "Tragic but ultimately uplifting";
		three.rating = 127;
		
		one.playIt();
		two.playIt();
		three.playIt();
	}
}

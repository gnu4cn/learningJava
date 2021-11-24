package com.xfoss.learningJava;

public class GuessGame {
	Player p1;
	Player p2;
	Player p3;
	
	public void startGame () {
		p1 = new Player ();
		p2 = new Player ();
		p3 = new Player ();
		
		int targetNum = (int) (Math.random() * 10);
		System.out.println("I'm thinking of a number between 0 and 9...");
		
		while(true) {
			System.out.format("------------------------\n"
					+ "Number to guess is %s\n"
					+ "-----------------------------\n"
					+ "Game started.\n"
					+ "-----------------------------\n", targetNum);
			
			p1.guess();
			p2.guess();
			p3.guess();
			
			System.out.format("Player one guessed %s\n", p1.num);
			System.out.format("Player two guessed %s\n", p2.num);
			System.out.format("Player three guessed %s\n", p3.num);
			
			p1.guessed = (p1.num == targetNum);
			p2.guessed = (p2.num == targetNum);
			p3.guessed = (p3.num == targetNum);
			
			if (p1.guessed || p2.guessed || p3.guessed) {
				System.out.println("We've got a winner!");
				
				System.out.println("Player one got it right? " 
						+ (p1.guessed ? Constants.ANSI_GREEN : Constants.ANSI_RED) 
						+ Boolean.toString(p1.guessed) + Constants.ANSI_RESET);
				System.out.println("Player two got it right? " 
						+ (p2.guessed ? Constants.ANSI_GREEN : Constants.ANSI_RED) 
						+ Boolean.toString(p2.guessed) + Constants.ANSI_RESET);
				System.out.println("Player three got it right? " 
						+ (p3.guessed ? Constants.ANSI_GREEN : Constants.ANSI_RED) 
						+ Boolean.toString(p3.guessed) + Constants.ANSI_RESET);
				
				System.out.println("Game is over.\n");
				break;
			} else {
				System.out.println("---------------------\n"
						+ "Players will have to try again.\n");
			}
		}
		
		
		
	}
}

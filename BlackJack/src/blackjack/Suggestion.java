package blackjack;

import java.util.ArrayList;

public class Suggestion {
	ArrayList <Card> deck = new ArrayList<Card>();
	ArrayList <Card> player = new ArrayList<Card>();
	//suggestion utility
	
	public Suggestion(ArrayList <Card> deck, ArrayList <Card> player) {
		this.deck = deck;
		this.player = player;
		
	}
	
	//calculate suggestion
	//then print
	
	
	 @Override
	 public String toString() {
		 //String info = getSuit();
		 return String.format(getRank() + info.substring(0,1)); 
	 }
}

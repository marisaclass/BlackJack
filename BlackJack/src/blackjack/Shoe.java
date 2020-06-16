package blackjack;

import java.util.ArrayList;

public class Shoe {
	private int shoe = 0;
	private int playable = 0;
	private int[] unit_cards = null;
	
	private ArrayList <Card> deck = new ArrayList<Card>();
	private Card cards = null;
	String[] suit = new String[] {"Spades", "Hearts", "Diamonds", "Clubs"};
	String[] rank = new String[]{"A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K"};
	
	public Shoe(int shoe, int playable) {
		this.shoe = shoe;
		this.playable = playable;
	}
	
	public Shoe(int shoe, int playable, int... cards) {
		this.shoe = shoe;
		this.playable = playable;
		this.unit_cards = cards;
	}
	
	public ArrayList<Card> createDeck() {
		if(unit_cards != null) { 
			for(int i = 0; i < unit_cards.length; i++) {
				if(String.valueOf(unit_cards[i]).equals("J") || String.valueOf(unit_cards[i]).equals("Q") || 
						String.valueOf(unit_cards[i]).equals("K")) {
					cards = new Card(String.valueOf(unit_cards[i]), suit[1], 10);
				}
				else if(String.valueOf(unit_cards[i]).equals("A")) {
					cards = new Card("A", suit[1], 11); //setting Ace value to 11
				}
				
				else if(String.valueOf(unit_cards[i]).equals("10")) {
					cards = new Card("T", suit[1], 11); 
				}
				
				else {
					cards = new Card(String.valueOf(unit_cards[i]), suit[1], unit_cards[i]); //face value, face rank
				}
				deck.add(cards);
			}
		}
		
		else {
			for(int i = 0; i < shoe; i++){
				for(int n = 0; n < suit.length; n++) { //for loop to set suits
					for(int j = 0; j < rank.length; j++) { //for loop to set ranks
						if(j >= 9) { 
							cards = new Card(rank[j], suit[n], 10); //value for 10/J/Q/K is 10
						}
						else if (j == 0) {
							cards = new Card(rank[j], suit[n], 11); //(ace default value=11)
						}
						else{
							cards = new Card(rank[j], suit[n], j+1); //value is the face card value 
						}
						deck.add(cards);
					}
				}
			}
		}
		
		return deck;
	}
	
	public int removeFromDeck(int currcard) {
		deck.remove(currcard);
		currcard++;
		
		return currcard;
	}
	
	public ArrayList<Card> getCurrDeck() {
		return deck;
	}
	
	public int getPlayable() {
		return playable;
	}
	
}

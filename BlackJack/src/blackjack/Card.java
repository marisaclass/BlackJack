package blackjack;

public class Card {
	private int rank;
	private String suit;
	private int value;
	
	
	//get rank
		//get suit
		//get value
		//print string description of card 
	public Card(int rank, String suit, int value) {
		this.rank = rank;
		this.suit = suit;
		this.value = value;
	}
	
	public int getRank() {
		return rank;
	}
	
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public String getSuit() {
		return suit;
	}
	
	public void setSuit(String suit) {
		this.suit = suit;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public String showCard() {
		String info = null;
		
		if(rank == 11) {
			info = "Jack of " + suit;
		}
		else if(rank == 12) {
			info = "Queen of " + suit;
		}
		else if(rank == 13) {
			info = "King of " + suit;
		}
		else if (rank == 1) {
			info = "Ace of " + suit;
		}
		else {
			info = rank + " of " + suit;
		}
		
		return info;
	}

}

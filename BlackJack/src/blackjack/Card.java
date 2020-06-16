package blackjack;

public class Card {
	private String rank;
	private String suit;
	private int value;
	
	public Card(String rank, String suit, int value) {
		this.rank = rank;
		this.suit = suit;
		this.value = value;
	}
	
	public String getRank() {
		return rank;
	}
	
	public String getSuit() {
		return suit;
	}

	public int getValue() {
		return value;
	}
	
	 @Override
	public String toString() {
		 String info = getSuit();
		 return String.format(getRank() + info.substring(0,1)); 
	 }

}

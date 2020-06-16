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
//	public String showCard() {
//		
//		
//		String info = null;
//		
//		if(rank == 11) {
//			info = "Jack of " + suit;
//		}
//		else if(rank == 12) {
//			info = "Queen of " + suit;
//		}
//		else if(rank == 13) {
//			info = "King of " + suit;
//		}
//		else if (rank == 1) {
//			info = "Ace of " + suit;
//		}
//		else {
//			info = rank + " of " + suit;
//		}
//		
//		return info;
//	}

}

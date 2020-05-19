package blackjack;

import java.util.ArrayList;

public class Deck {
	ArrayList<Card> cardDeck = new ArrayList<Card>(); //arraylist of current deck
	
	public void setData(ArrayList<Card> cardDeck) {
		this.cardDeck = cardDeck;
	}
	public ArrayList<Card> getData(){
		return cardDeck;
	}

}

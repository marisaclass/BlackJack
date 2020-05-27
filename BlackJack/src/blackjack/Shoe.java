package blackjack;

public class Shoe {
	private int shoe;
	private int playable;
	private int[] cards;
	
	public Shoe(int shoe, int playable) {
		this.shoe = shoe;
		this.playable = playable;
	}
	
	public Shoe(int shoe, int playable, int... cards) {
		this.shoe = shoe;
		this.playable = playable;
		this.cards = cards;
	}
	
	public int getPlay() {
		return playable;
	}
	
	public void setCards() {
		for(int i = 0; i < shoe*52; i++) {
			for(int j = 0; j < cards.length; j++) {
				
			}
		}
	}
	
}

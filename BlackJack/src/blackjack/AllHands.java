package blackjack;

import java.util.ArrayList;

public class AllHands {
	
	ArrayList<Hand> playerHands = new ArrayList<Hand>(); //array of Json objects
	int split = 0;
	
	public void addData(Hand data) {
		playerHands.add(data);
	}
	
	public void removeData(Hand data) {
		playerHands.remove(data);
	}
	
	public void clearData() {
		playerHands.clear();
	}
	
	public ArrayList<Hand> getAllHands(){
		return playerHands;
	}
	
	public int maxSplit() {
		return split;
	}
	
	public void addSplit() {
		split++;
	}
}

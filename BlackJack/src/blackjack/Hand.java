package blackjack;

import java.util.ArrayList;

public class Hand {
	private ArrayList <Card> player = new ArrayList<Card>();
	//private int value = 0;
	
	public Hand(ArrayList<Card> player) {
		this.player = player;
		//this.value = value;
	}
	
	public ArrayList<Card> getHand() {
		return player;
	}
	
	public void clearData() {
		player.clear();
	}
	
	public void printCurrentHand(ArrayList <Card> player) {
		System.out.print("You're hand: ");
		for(int i = 0; i < player.size(); i++) {
			System.out.print(player.get(i).toString() + ", ");
		}
	}
	
	public int getSum() {
		int count = 0;
		int ace = 0;
		int high = 0; //keeping track of values of 11 being used
		boolean isSoft = false;
		
		for(int i = 0; i < player.size(); i++) {
			if(player.get(i).getRank().equals("A")) {
				ace++;
			}
			else {
				count += player.get(i).getValue(); 
			}
			//need to do Ace (1 or 11) edge case -> by default, ace is 11
		}
		
		for(int i = 0; i < ace; i++) { 
			if(count >= 21) {
				count++;
			}
			
			else {
				count += 11;
				high++;
			
				if(count > 21) {
					count -= 11; //dont want to add 11 if originally count is already at 21 or above so just add 1
					count++;
					high--;
				}
			}
			
			isSoft = true; //only enters for loop when ace > 0 
		}
		
		while(count > 21 && high > 0) { //checking at end 	
			count -= 11;
			count++; 
			high--;
		}
		
		if(isSoft && count <= 10) {
			count = getSoft();
		}

		return count;
	}
	
	public int getSoft() {
		int sum = 0;
		for(int i = 0; i < player.size(); i++) {
			if(!player.get(i).getRank().equals("A")) {
					sum += player.get(i).getValue();
			}
		}
		return sum;
	}
	
	public int hasAce() {
		int ace = 0;
		for(int i = 0; i < player.size(); i++) {
			if(player.get(i).getRank().equals("A")) {
					ace++;
			}
		}
		return ace;
	}
	
}
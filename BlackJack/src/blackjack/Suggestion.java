package blackjack;

import java.util.ArrayList;

import blackjack.Action;

public class Suggestion {
	private static ArrayList <Card> dealer = new ArrayList<Card>();
	private static ArrayList <Card> player = new ArrayList<Card>();
	private boolean isSoft = false;
	private static int playerSum = 0;
	
	//For the suggestion Util, I'd recommend using Enum for your actions, like Action.HIT, Action.STAND, etc. 
	//It should return an action based on the hand and the card the dealer is showing. 
	//It'll help you get some experience with Enum's even though they aren't used often.
	
	public Suggestion(ArrayList <Card> dealer, ArrayList<Card> player, int playerSum, boolean isSoft){
		this.dealer = dealer;
		this.player = player;
		this.playerSum = playerSum;
		this.isSoft = isSoft;
	}

	public String ToDo(){
		Action TODO = null;
		if(player.get(0).getRank() == player.get(1).getRank()) {
			TODO = Pair(); //
		}
		
		else if(isSoft == false) {
			TODO = hardHand();
		}
		
		else if(isSoft == true) {
			TODO = softHand();
		}
		
		return TODO.toString();
	}

	public static Action hardHand() {
		int card = dealer.get(0).getValue();
		Action sugg = null;
		if(card == 2 || card == 3) {
			if(playerSum >= 5 && playerSum < 9) {
				sugg = Action.HIT;
			}
			
			else if(playerSum >= 9 && playerSum < 12) {
				if(playerSum == 9 || playerSum == 10 || playerSum == 11 && player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}
			}
			
			else if(playerSum == 12) {
				sugg = Action.HIT;
			}
			else if(playerSum > 12 && playerSum < 22) {
				sugg = Action.STAND;
			}
		}
		
		else if(card == 4 || card == 5 || card == 6) {
			if(playerSum >= 5 && playerSum < 9) {
				sugg = Action.HIT;
			}
			else if(playerSum >= 9 && playerSum < 12) {
				if(playerSum == 9 || playerSum == 10 || playerSum == 11 && player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}
			}
			else if(playerSum >= 12 && playerSum < 22) {
				sugg = Action.STAND;
			}
		}
		
		else if(card == 7 || card == 8 || card == 9) {
			if(playerSum >= 5 && playerSum < 10) {
				sugg = Action.HIT;
			}
			
			else if(playerSum == 10 || playerSum == 11) {
				if(playerSum == 9 || playerSum == 10 || playerSum == 11 && player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}
			}
			
			else if(playerSum >= 12 && playerSum < 17) {
				sugg = Action.HIT;
			}
			else if(playerSum >= 17 && playerSum < 22) {
				sugg = Action.STAND;
			}
		}
		
		else if(card == 10) {
			if(playerSum >= 5 && playerSum < 11) {
				sugg = Action.HIT;
			}
			
			else if(playerSum == 11) {
				if(playerSum == 9 || playerSum == 10 || playerSum == 11 && player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}
			}
			
			else if(playerSum >= 12 && playerSum < 15) {
				sugg = Action.HIT;
			}
			else if(playerSum == 15 || playerSum == 16) {
				sugg = Action.SURRENDER;
			}
			
			else if(playerSum >= 17 && playerSum < 22) {
				sugg = Action.STAND;
			}
		}
		
		else if(card == 11) {
			if(playerSum >= 5 && playerSum < 11) {
				sugg = Action.HIT;
			}
			
			else if(playerSum == 11) {
				if(playerSum == 9 || playerSum == 10 || playerSum == 11 && player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}
			}
			
			else if(playerSum >= 12 && playerSum < 17) {
				sugg = Action.HIT;
			}
			
			else if(playerSum >= 17 && playerSum < 22) {
				sugg = Action.STAND;
			}
		}
		return sugg;
	}
	
	public static Action softHand() {
		int card = dealer.get(0).getValue();
		Action sugg = null;
		if(card == 9 || card == 10 || card == 11){
			if(playerSum >= 13 && playerSum < 19 ) {
				sugg = Action.HIT;
			}else if(playerSum >= 19 && playerSum < 22) {
				sugg = Action.STAND;
			}
		}
		
		if(card == 7 || card == 8){
			if(playerSum >= 13 && playerSum < 18 ) {
				sugg = Action.HIT;
			}else if(playerSum >= 18 && playerSum < 22) {
				sugg = Action.STAND;
			}
		}
		
		if(card == 5 || card == 6){
			if(playerSum >= 13 && playerSum < 18 ) {
				if(playerSum == 9 || playerSum == 10 || playerSum == 11 && player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}
			}else if(playerSum == 18) {
				if(playerSum == 9 || playerSum == 10 || playerSum == 11 && player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.STAND;
				}
			}else if(playerSum >= 19 && playerSum < 22) {
				sugg = Action.STAND;
			}
		}
		
		if(card == 4){
			if(playerSum >= 13 && playerSum < 15 ) {
				sugg = Action.HIT;
			}else if(playerSum >= 15 && playerSum < 18) {
				if(playerSum == 9 || playerSum == 10 || playerSum == 11 && player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}
			}
			else if(playerSum == 18) {
				if(playerSum == 9 || playerSum == 10 || playerSum == 11 && player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.STAND;
				}
			}else if(playerSum >= 19 && playerSum < 22) {
				sugg = Action.STAND;
			}
		}
		
		if(card == 3){
			if(playerSum >= 13 && playerSum < 17 ) {
				sugg = Action.HIT;
			}else if(playerSum == 17) {
				if(playerSum == 9 || playerSum == 10 || playerSum == 11 && player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}
			}
			else if(playerSum == 18) {
				if(playerSum == 9 || playerSum == 10 || playerSum == 11 && player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.STAND;
				}
			}else if(playerSum >= 19 && playerSum < 22) {
				sugg = Action.STAND;
			}
		}
		
		if(card == 2){
			if(playerSum >= 13 && playerSum < 18 ) {
				sugg = Action.HIT;
			}else if(playerSum >= 18) {
				sugg = Action.STAND;
			}
		}
		return sugg;
	}
	
	public static Action Pair() {
		int card = dealer.get(0).getValue();
		int play = player.get(0).getValue();
		Action sugg = null;
	
		if(card == 2 || card == 3 || card == 4){
			if(play == 2) {
				sugg = Action.SPLIT;
			}else if(play == 3) {
				sugg = Action.SPLIT;		
			}else if(play == 4) {
				sugg = Action.HIT;		
			}else if(play == 5) {
				if(playerSum == 9 || playerSum == 10 || playerSum == 11 && player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}		
			}else if(play == 6) {
				sugg = Action.SPLIT;		
			}else if(play == 7) {
				sugg = Action.SPLIT;		
			}else if(play == 8) {
				sugg = Action.SPLIT;		
			}else if(play == 9) {
				sugg = Action.SPLIT;		
			}else if(play == 10) {
				sugg = Action.STAND;			
			}else if(play == 11) {
				sugg = Action.SPLIT;		
			}		
		}
				
		if(card == 5 || card == 6){
			if(play == 2 ) {
				sugg = Action.SPLIT;
			}else if(play == 3) {
				sugg = Action.SPLIT;		
			}else if(play == 4) {
				sugg = Action.SPLIT;		
			}else if(play == 5) {
				if(playerSum == 9 || playerSum == 10 || playerSum == 11 && player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}		
			}else if(play == 6) {
				sugg = Action.SPLIT;		
			}else if(play == 7) {
				sugg = Action.SPLIT;		
			}else if(play == 8) {
				sugg = Action.SPLIT;		
			}else if(play == 9) {
				sugg = Action.SPLIT;		
			}else if(play == 10) {
				sugg = Action.STAND;		
			}else if(play == 11) {
				sugg = Action.SPLIT;		
			}
		}
		if(card == 7){
			if(play == 2) {
				sugg = Action.SPLIT;
			}else if(play == 3) {
				sugg = Action.SPLIT;		
			}else if(play == 4) {
				sugg = Action.HIT;		
			}else if(play == 5) {
				if(playerSum == 9 || playerSum == 10 || playerSum == 11 && player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}		
			}else if(play == 6) {
				sugg = Action.SPLIT;		
			}else if(play == 7) {
				sugg = Action.SPLIT;		
			}else if(play == 8) {
				sugg = Action.SPLIT;		
			}else if(play == 9) {
				sugg = Action.STAND;		
			}else if(play == 10) {
				sugg = Action.STAND;		
			}else if(play == 11) {
				sugg = Action.SPLIT;		
			}		
		}
		if(card == 8){
			if(play == 2) {
				sugg = Action.HIT;
			}else if(play == 3) {
				sugg = Action.HIT;		
			}else if(play == 4) {
				sugg = Action.HIT;		
			}else if(play == 5) {
				if(playerSum == 9 || playerSum == 10 || playerSum == 11 && player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}		
			}else if(play == 6) {
				sugg = Action.HIT;		
			}else if(play == 7) {
				sugg = Action.SPLIT;		
			}else if(play == 8) {
				sugg = Action.SPLIT;		
			}else if(play == 9) {
				sugg = Action.SPLIT;		
			}else if(play == 10) {
				sugg = Action.STAND;		
			}else if(play == 11) {
				sugg = Action.SPLIT;		
			}		
		}
				
		if(card == 9){
			if(play == 2) {
				sugg = Action.HIT;
			}else if(play == 3) {
				sugg = Action.HIT;
			}else if(play == 4) {
				sugg = Action.HIT;		
			}else if(play == 5) {
				if(playerSum == 9 || playerSum == 10 || playerSum == 11 && player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}		
			}else if(play == 6) {
				sugg = Action.HIT;		
			}else if(play == 7) {
				sugg = Action.HIT;	
			}else if(play == 8) {
				sugg = Action.SPLIT;		
			}else if(play == 9) {
				sugg = Action.SPLIT;		
			}else if(play == 10) {
				sugg = Action.STAND;		
			}else if(play == 11) {
				sugg = Action.SPLIT;		
			}
		}
		if(card == 10 || card == 11){
			if(play == 2) {
				sugg = Action.HIT;	
			}else if(play == 3) {
				sugg = Action.HIT;		
			}else if(play == 4) {
				sugg = Action.HIT;			
			}else if(play == 5) {
				sugg = Action.HIT;		
			}else if(play == 6) {
				sugg = Action.HIT;			
			}else if(play == 7) {
				sugg = Action.HIT;		
			}else if(play == 8) {
				sugg = Action.SPLIT;		
			}else if(play == 9) {
				sugg = Action.STAND;		
			}else if(play == 10) {
				sugg = Action.STAND;		
			}else if(play == 11) {
				sugg = Action.SPLIT;		
			}
		}
		return sugg;		
	}
}

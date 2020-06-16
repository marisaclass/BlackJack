package blackjack;

import java.util.ArrayList;

import javax.swing.Action;

public class Suggestion {
	private static ArrayList <Card> dealer = new ArrayList<Card>();
	private static ArrayList <Card> player = new ArrayList<Card>();
	private boolean isSoft = false;
	private static int playerSum = 0;
//	private static Action SPLIT = null;
//	private static Action HIT = null;
//	private static Action Dh = null;
//	private static Action Ds = null;
//	private static Action Rh = null;
//	private static Action STAND = null;
	
	//For the suggestion Util, I'd recommend using Enum for your actions, like Action.HIT, Action.STAND, etc. 
	//It should return an action based on the hand and the card the dealer is showing. 
	//It'll help you get some experience with Enum's even though they aren't used often.
	
	public Suggestion(ArrayList <Card> dealer, ArrayList<Card> player, int playerSum, boolean isSoft) {
		this.dealer = dealer;
		this.player = player;
		this.playerSum = playerSum;
		this.isSoft = isSoft;
	}
	
	private enum action { 
		HIT {
			@Override public String toString(){ 
				return "Hit"; 
			} 
		}, 
		STAND { 
			@Override public String toString(){ 
				return "Stand"; 
			} 
		}, 
		
		SPLIT { 
			@Override public String toString(){ 
				return "Split"; 
			} 
		},
		
		Dh {
			@Override public String toString(){ 
				return "Double. If not, then hit."; 
			} 
		}, 
		Ds { 
			@Override public String toString(){ 
				return "Double. If not, then stand."; 
			} 
		}, 
		
		Rh { 
			@Override public String toString(){ 
				return "Surrender. If not, then hit."; 
			} 
		},

	}

	public String ToDo(){
		action TODO = null;
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

	public static action hardHand() {
		int card = dealer.get(0).getValue();
		action sugg = null;
		if(card == 2 || card == 3) {
			if(playerSum >= 5 && playerSum < 9) {
				sugg = action.HIT;
			}
			
			else if(playerSum >= 9 && playerSum < 12) {
				sugg = action.Dh;
			}
			
			else if(playerSum == 12) {
				sugg = action.HIT;
			}
			else if(playerSum > 12 && playerSum < 22) {
				sugg = action.STAND;
			}
		}
		
		else if(card == 4 || card == 5 || card == 6) {
			if(playerSum >= 5 && playerSum < 9) {
				sugg = action.HIT;
			}
			else if(playerSum >= 9 && playerSum < 12) {
				sugg = action.Dh;
			}
			else if(playerSum >= 12 && playerSum < 22) {
				sugg = action.STAND;
			}
		}
		
		else if(card == 7 || card == 8 || card == 9) {
			if(playerSum >= 5 && playerSum < 10) {
				sugg = action.HIT;
			}
			
			else if(playerSum == 10 || playerSum == 11) {
				sugg = action.Dh;
			}
			
			else if(playerSum >= 12 && playerSum < 17) {
				sugg = action.HIT;
			}
			else if(playerSum >= 17 && playerSum < 22) {
				sugg = action.STAND;
			}
		}
		
		else if(card == 10) {
			if(playerSum >= 5 && playerSum < 11) {
				sugg = action.HIT;
			}
			
			else if(playerSum == 11) {
				sugg = action.Dh;
			}
			
			else if(playerSum >= 12 && playerSum < 15) {
				sugg = action.HIT;
			}
			else if(playerSum == 15 || playerSum == 16) {
				sugg = action.Rh;
			}
			
			else if(playerSum >= 17 && playerSum < 22) {
				sugg = action.STAND;
			}
		}
		
		else if(card == 11) {
			if(playerSum >= 5 && playerSum < 11) {
				sugg = action.HIT;
			}
			
			else if(playerSum == 11) {
				sugg = action.Dh;
			}
			
			else if(playerSum >= 12 && playerSum < 17) {
				sugg = action.HIT;
			}
			
			else if(playerSum >= 17 && playerSum < 22) {
				sugg = action.STAND;
			}
		}
		return sugg;
	}
	
	public static action softHand() {
		int card = dealer.get(0).getValue();
		action sugg = null;
		if(card == 9 || card == 10 || card == 11){
			if(playerSum >= 13 && playerSum < 19 ) {
				sugg = action.HIT;
			}else if(playerSum >= 19 && playerSum < 22) {
				sugg = action.STAND;
			}
		}
		
		if(card == 7 || card == 8){
			if(playerSum >= 13 && playerSum < 18 ) {
				sugg = action.HIT;
			}else if(playerSum >= 18 && playerSum < 22) {
				sugg = action.STAND;
			}
		}
		
		if(card == 5 || card == 6){
			if(playerSum >= 13 && playerSum < 18 ) {
				sugg = action.Dh;
			}else if(playerSum == 18) {
				sugg = action.Ds;
			}else if(playerSum >= 19 && playerSum < 22) {
				sugg = action.STAND;
			}
		}
		
		if(card == 4){
			if(playerSum >= 13 && playerSum < 15 ) {
				sugg = action.HIT;
			}else if(playerSum >= 15 && playerSum < 18) {
				sugg = action.Dh;
			}
			else if(playerSum == 18) {
				sugg = action.Ds;
			}else if(playerSum >= 19 && playerSum < 22) {
				sugg = action.STAND;
			}
		}
		
		if(card == 3){
			if(playerSum >= 13 && playerSum < 17 ) {
				sugg = action.HIT;
			}else if(playerSum == 17) {
				sugg = action.Dh;
			}
			else if(playerSum == 18) {
				sugg = action.Ds;
			}else if(playerSum >= 19 && playerSum < 22) {
				sugg = action.STAND;
			}
		}
		
		if(card == 2){
			if(playerSum >= 13 && playerSum < 18 ) {
				sugg = action.HIT;
			}else if(playerSum >= 18) {
				sugg = action.STAND;
			}
		}
		return sugg;
	}
	
	public static action Pair() {
		int card = dealer.get(0).getValue();
		int play = player.get(0).getValue();
		action sugg = null;
	
		if(card == 2 || card == 3 || card == 4){
			if(play == 2) {
				sugg = action.SPLIT;
			}else if(play == 3) {
				sugg = action.SPLIT;		
			}else if(play == 4) {
				sugg = action.HIT;		
			}else if(play == 5) {
				sugg = action.Dh;		
			}else if(play == 6) {
				sugg = action.SPLIT;		
			}else if(play == 7) {
				sugg = action.SPLIT;		
			}else if(play == 8) {
				sugg = action.SPLIT;		
			}else if(play == 9) {
				sugg = action.SPLIT;		
			}else if(play == 10) {
				sugg = action.STAND;		
			}else if(play == 11) {
				sugg = action.SPLIT;		
			}		
		}
				
		if(card == 5 || card == 6){
			if(play == 2 ) {
				sugg = action.SPLIT;
			}else if(play == 3) {
				sugg = action.SPLIT;		
			}else if(play == 4) {
				sugg = action.SPLIT;		
			}else if(play == 5) {
				sugg = action.Dh;		
			}else if(play == 6) {
				sugg = action.SPLIT;		
			}else if(play == 7) {
				sugg = action.SPLIT;		
			}else if(play == 8) {
				sugg = action.SPLIT;		
			}else if(play == 9) {
				sugg = action.SPLIT;		
			}else if(play == 10) {
				sugg = action.STAND;		
			}else if(play == 11) {
				sugg = action.SPLIT;		
			}
		}
		if(card == 7){
			if(play == 2) {
				sugg = action.SPLIT;
			}else if(play == 3) {
				sugg = action.SPLIT;		
			}else if(play == 4) {
				sugg = action.HIT;		
			}else if(play == 5) {
				sugg = action.Dh;		
			}else if(play == 6) {
				sugg = action.SPLIT;		
			}else if(play == 7) {
				sugg = action.SPLIT;		
			}else if(play == 8) {
				sugg = action.SPLIT;		
			}else if(play == 9) {
				sugg = action.STAND;		
			}else if(play == 10) {
				sugg = action.STAND;		
			}else if(play == 11) {
				sugg = action.SPLIT;		
			}		
		}
		if(card == 8){
			if(play == 2) {
				sugg = action.HIT;
			}else if(play == 3) {
				sugg = action.HIT;		
			}else if(play == 4) {
				sugg = action.HIT;		
			}else if(play == 5) {
				sugg = action.Dh;		
			}else if(play == 6) {
				sugg = action.HIT;		
			}else if(play == 7) {
				sugg = action.SPLIT;		
			}else if(play == 8) {
				sugg = action.SPLIT;		
			}else if(play == 9) {
				sugg = action.SPLIT;		
			}else if(play == 10) {
				sugg = action.STAND;		
			}else if(play == 11) {
				sugg = action.SPLIT;		
			}		
		}
				
		if(card == 9){
			if(play == 2) {
				sugg = action.HIT;
			}else if(play == 3) {
				sugg = action.HIT;
			}else if(play == 4) {
				sugg = action.HIT;		
			}else if(play == 5) {
				sugg = action.Dh;		
			}else if(play == 6) {
				sugg = action.HIT;		
			}else if(play == 7) {
				sugg = action.HIT;	
			}else if(play == 8) {
				sugg = action.SPLIT;		
			}else if(play == 9) {
				sugg = action.SPLIT;		
			}else if(play == 10) {
				sugg = action.STAND;		
			}else if(play == 11) {
				sugg = action.SPLIT;		
			}
		}
		if(card == 10 || card == 11){
			if(play == 2) {
				sugg = action.HIT;
			}else if(play == 3) {
				sugg = action.HIT;	
			}else if(play == 4) {
				sugg = action.HIT;		
			}else if(play == 5) {
				sugg = action.HIT;	
			}else if(play == 6) {
				sugg = action.HIT;		
			}else if(play == 7) {
				sugg = action.HIT;		
			}else if(play == 8) {
				sugg = action.SPLIT;		
			}else if(play == 9) {
				sugg = action.STAND;		
			}else if(play == 10) {
				sugg = action.STAND;		
			}else if(play == 11) {
				sugg = action.SPLIT;		
			}
		}
		return sugg;		
	}
}

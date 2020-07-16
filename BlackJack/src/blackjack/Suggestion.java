package blackjack;

import java.util.ArrayList;

import blackjack.Action;

public class Suggestion {
	public static Action getAdvice(int dealer, Hand hand, AllHands all){
		Action advice = null;
		ArrayList <Card> player = hand.getHand();
		int playerSum = hand.getSum();
		int soft = hand.getSoft();
		int max = all.maxSplit();
		
		if((player.get(0).getValue() == player.get(1).getValue()) && player.size() == 2) {
			advice = Pair(dealer, player, max); //
		}
		else {
			if(soft == 0) {
				advice = hardHand(dealer, playerSum, player);
			}
			else if(soft > 0) {
				advice = softHand(dealer, playerSum, player);
			}
		}
		return advice;
	}

	public static Action hardHand(int card, int playerSum, ArrayList<Card> player) {
		Action sugg = null;
		if(card == 2 || card == 3) {
			if(playerSum >= 5 && playerSum < 9) {
				sugg = Action.HIT;
			}
			else if(playerSum >= 9 && playerSum < 12) {
				if(player.size() == 2) {
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
				if(player.size() == 2) {
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
				if(player.size() == 2) {
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
				if(player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}
			}
			else if(playerSum >= 12 && playerSum <= 16) {
				sugg = Action.HIT;
			}
			/*else if(playerSum == 15 || playerSum == 16) {
				sugg = Action.SURRENDER;
			}*/
			
			else if(playerSum >= 17 && playerSum < 22) {
				sugg = Action.STAND;
			}
		}
		
		else if(card == 11) {
			if(playerSum >= 5 && playerSum < 11) {
				sugg = Action.HIT;
			}
			else if(playerSum == 11) {
				if(player.size() == 2) {
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
	
	public static Action softHand(int card, int playerSum, ArrayList<Card> player) {
		Action sugg = null;
		if(card == 11) {
			if(playerSum == 18 && player.size() > 2) {
				sugg = Action.STAND;
			}else if(playerSum >= 13 && playerSum < 19) {
				sugg = Action.HIT;
			}else if(playerSum >= 19 && playerSum < 22) {
				sugg = Action.STAND;
			}
		}
		else if(card == 9 || card == 10){
			if(playerSum >= 13 && playerSum < 19) {
				sugg = Action.HIT;
			}else if(playerSum >= 19 && playerSum < 22) {
				sugg = Action.STAND;
			}
		}
		else if(card == 7 || card == 8){
			if(playerSum >= 13 && playerSum < 18) {
				sugg = Action.HIT;
			}else if(playerSum >= 18 && playerSum < 22) {
				sugg = Action.STAND;
			}
		}
		else if(card == 5 || card == 6){
			if(playerSum >= 13 && playerSum < 18 ) {
				if(player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}
			}else if(playerSum == 18) {
				if(player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.STAND;
				}
			}else if(playerSum >= 19 && playerSum < 22) {
				sugg = Action.STAND;
			}
		}
		else if(card == 4){
			if(playerSum >= 13 && playerSum < 15 ) {
				sugg = Action.HIT;
			}else if(playerSum >= 15 && playerSum < 18) {
				if(player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}
			}
			else if(playerSum == 18) {
				if(player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.STAND;
				}
			}else if(playerSum >= 19 && playerSum < 22) {
				sugg = Action.STAND;
			}
		}
		else if(card == 3){
			if(playerSum >= 13 && playerSum < 17 ) {
				sugg = Action.HIT;
			}else if(playerSum == 17) {
				if(player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}
			}
			else if(playerSum == 18) {
				if(player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.STAND;
				}
			}else if(playerSum >= 19 && playerSum < 22) {
				sugg = Action.STAND;
			}
		}
		else if(card == 2){
			if(playerSum >= 13 && playerSum < 18 ) {
				sugg = Action.HIT;
			}else if(playerSum >= 18) {
				sugg = Action.STAND;
			}
		}
		return sugg;
	}
	
	public static Action Pair(int card, ArrayList<Card> player, int max) {
		int play = player.get(0).getValue();
		String rank = player.get(0).getRank();
		Action sugg = null;
	
		if(card == 2 || card == 3 || card == 4){
			if(play == 2 || play == 3){
				if(max < 5) {
					sugg = Action.SPLIT;		
				}
				else {
					sugg = Action.HIT;
				}
			}else if(play == 4) {
				sugg = Action.HIT;		
			}else if(play == 5) {
				if(player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}		
			}else if(play == 6){
				if(max < 5) {
					sugg = Action.SPLIT;		
				}
				
				else if(card == 4) {
					sugg = Action.STAND;
				}
				else {
					sugg = Action.HIT;
				}
			}else if(play == 8 || play == 9 || play == 7) {
				if(max < 5) {
					sugg = Action.SPLIT;		
				}
				else {
					sugg = Action.STAND;
				}
			}else if(play == 10) {
				sugg = Action.STAND;			
			}else if(rank.equalsIgnoreCase("A") && max < 5) {
				sugg = Action.SPLIT;		
			}		
		}	
		else if(card == 5 || card == 6){
			if(play == 2 || play == 3 || play == 4) {
				if(max < 5) {
					sugg = Action.SPLIT;		
				}
				else {
					sugg = Action.HIT;
				}
			}else if(play == 5) {
				if(player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}		
			}else if(play == 8 || play == 9 || play == 7 || play == 6) {
				if(max < 5) {
					sugg = Action.SPLIT;		
				}
				else {
					sugg = Action.STAND;
				}
			}else if(play == 10) {
				sugg = Action.STAND;		
			}else if(rank.equalsIgnoreCase("A") && max < 5) {
				sugg = Action.SPLIT;		
			}
		}
		else if(card == 7){
			if(play == 2 || play == 3){
				if(max < 5) {
					sugg = Action.SPLIT;		
				}
				else {
					sugg = Action.HIT;
				}
			}else if(play == 4) {
				sugg = Action.HIT;		
			}else if(play == 5) {
				if(player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}		
			}else if(play == 8 || play == 6 || play == 7) {
				if(max < 5) {
					sugg = Action.SPLIT;		
				}
				else {
					sugg = Action.HIT;
				}
			}else if(play == 9 || play == 10) {
				sugg = Action.STAND;		
			}else if(rank.equalsIgnoreCase("A") && max < 5) {
				sugg = Action.SPLIT;		
			}		
		}
		else if(card == 8){
			if(play == 2 || play == 3 || play == 4) {
				sugg = Action.HIT;
			}else if(play == 5) {
				if(player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}		
			}else if(play == 6) {
				sugg = Action.HIT;
			}else if(play == 8 || play == 7) {
				if(max < 5) {
					sugg = Action.SPLIT;		
				}
				else {
					sugg = Action.HIT;
				}
			}else if(play == 9) {
				if(max < 5) {
					sugg = Action.SPLIT;		
				}
				else {
					sugg = Action.STAND;
				}	
			}else if(play == 10) {
				sugg = Action.STAND;		
			}else if(rank.equalsIgnoreCase("A") && max < 5) {
				sugg = Action.SPLIT;		
			}		
		}	
		else if(card == 9){
			if(play == 2 || play == 3 || play == 4) {
				sugg = Action.HIT;
			}else if(play == 5) {
				if(player.size() == 2) {
					sugg = Action.DOUBLE;
				}
				else {
					sugg = Action.HIT;
				}		
			}else if(play == 6 || play == 7) {
				sugg = Action.HIT;
			}else if(play == 8){
				if(max < 5) {
					sugg = Action.SPLIT;		
				}
				else {
					sugg = Action.HIT;
				}
			}else if(play == 9) {
				if(max < 5) {
					sugg = Action.SPLIT;		
				}
				else {
					sugg = Action.STAND;
				}	
			}else if(play == 10) {
				sugg = Action.STAND;		
			}else if(rank.equalsIgnoreCase("A") && max < 5) {
				sugg = Action.SPLIT;		
			}
		}
		else if(card == 10 || card == 11){
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
			}else if(play == 8){
				if(max < 5) {
					sugg = Action.SPLIT;		
				}
				else {
					sugg = Action.HIT;
				}		
			}else if(play == 9 || play == 10) {
				sugg = Action.STAND;		
			}else if(rank.equalsIgnoreCase("A") && max < 5) {
				sugg = Action.SPLIT;		
			}
		}
		return sugg;		
	}
}

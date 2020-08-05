package blackjack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

import blackjack.Action;

public class StartGame {
	private static BigDecimal bankroll = BigDecimal.ZERO;
	private BigDecimal remaining = BigDecimal.ZERO;;
	private BigDecimal bet = BigDecimal.ZERO;;
	private static BigDecimal original = BigDecimal.ZERO;;
	private static int currcard = 0; //top most card in deck
	private static int actionp = 0;
	private static boolean playing = false;
	private Shoe shoe = null;
	ArrayList <Card> dealer = new ArrayList<Card>();
	ArrayList<Card> player = new ArrayList<Card>();
	Hand phand = null;
	Hand dhand = null;
	AllHands all = null;

	public StartGame(){
		
	}
	
	public static void main(String[] args) {
		StartGame game = new StartGame();
		game.run();
	}
	
	public void setShoe(int deck, int playable, int... cards) {
		shoe = new Shoe(deck, playable, cards);
		shoe.createDeck();
	}
	
	public void run() {		
		if(bet.compareTo(BigDecimal.valueOf(-1.0)) == 0) {
			//terminate
			playing = false;
		}
		else {
			playing = true;
			remaining = bankroll;
			original = bet;
			//remaining = remaining.subtract(bet);
			currcard = 0;
			actionp = 0;
		}
	}
	
	public void startGame() {
		Scanner scan = new Scanner(System.in);
		int statusP = 0;
		int statusD = 0;
		boolean asked = false;
		boolean shuffled = false;
		BigDecimal insure_bet = BigDecimal.ZERO;;
		
		phand = new Hand(player);
		dhand = new Hand(dealer);
		all = new AllHands();
		all.addData(phand);
		deal(); //dealing first 2 cards to dealing & 2 to player from shuffled deck
		while(playing) {
			if(asked == false) {
				try {
					if(dealer.get(1).getRank().equalsIgnoreCase("A")){ //dealer is currently showing an ace
						System.out.println("Would you like Insurance? If so, how much? Type '0' for None.");
						insure_bet = scan.nextBigDecimal(); 
						BigDecimal two = bet.divide(BigDecimal.valueOf(2.0));
						
						while(insure_bet.compareTo(two) == 1) {
							System.out.println("Please enter insurance that is atmost half of your current bet which is $" + bet);
							insure_bet = scan.nextBigDecimal(); //assuming at most half of current bet (no check here)
						}
						if(insure_bet.compareTo(BigDecimal.ZERO) == 1) {
							phand.setInsurance();
						}
						asked = true;
					}
				}catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
			for(int i = 0; i < all.getPlayerHands().size(); i++) {
				statusP = playerTurn(all.getPlayerHands().get(i));
			}
			if(statusP == 2 || statusP == 0) { //only call dealer if a split hand surrendered but not if single hand surrendered
				statusD = dealerTurn(insure_bet);
			}
			if((statusP == 1 || statusD == 1 || statusP == 2) && currcard <= shoe.getCurrDeck().size() - 4) { //still has 4+ cards to continue playing game
				//starting another game
				int winP = phand.getSum();
				int winD = dhand.getSum();
				
				if(winP == winD){ //"push" no one wins but player loses if both bust
					if(winP <= 21) {
						remaining = remaining.add(bet);
					}
				}
				if(currcard >= shoe.getPlayable() && shuffled == false) {
					shuffle();
					currcard = 0;
					shuffled = true; 
				}
				redeal();
				if (playing == false) {
					 break;
				}
			}	
			else if(currcard > shoe.getCurrDeck().size() - 1) { //end of deck so tally and terminate game
				int winP = 0;
				int winD = dhand.getSum();
				boolean accounted = false;
				boolean count = false;
				
				for(int i = 0; i < all.getPlayerHands().size(); i++) {
					winP = all.getPlayerHands().get(i).getSum();
					Hand current = all.getPlayerHands().get(i);
					ArrayList<Card> p = current.getHand();

					if(winP == 21 && winD != 21 && count == false) {
						if(p.size() == 2) {
							current.setBlackjack();
							BigDecimal back = bet.multiply(BigDecimal.valueOf(1.5));
							remaining = remaining.add(back); //payout for blackjack is 1.5*bet
						}else {
							remaining = remaining.add(bet);
						}
						count = true;
					}else if(winP > 21) {
						current.setBust();
						if(accounted == false) {
							if(all.getPlayerHands().size() > 1) {
								remaining = remaining.subtract(original);
							}
							else {
								remaining = remaining.subtract(bet);
							}
							accounted = true;
						}
					}else if(winD > winP && winD <= 21){
						if(all.getPlayerHands().size() > 1) {
							remaining = remaining.subtract(original);
						}
						else {
							if(accounted == false){
								remaining = remaining.subtract(bet);
							}
							accounted = true;
						}
					}else if(winP > winD && winP <= 21 && count == false) { //no one busted but player gets back original bet
						remaining = remaining.add(bet); //player gets back original betx2 (wins)
						count = true;
					}else if(current.isSurrender()) {
						remaining = remaining.subtract(bet);
					}
				}
				if(winD > 21) {
					dhand.setBust();
				}else if (winD == 21) {
					if(dealer.size() == 2) {
						dhand.setBlackjack();
					}
				}
				playing = false;
			}
		}
	}
	
	public int playerTurn(Hand phand) {
		int status = 0;
		Action force = null;
		if(phand.getHand().size() == 1) { //the next splitted hand
			phand.setSplit();
			phand.getHand().add(shoe.getCurrDeck().get(currcard));
			currcard++;
		}
		
		if(actionp != 0) {
			if(actionp == 1) { //forcing first action
				force = Action.STAND;
			}else if(actionp == 2) {
				force = Action.HIT;
			}else if(actionp == 3) {
				force = Action.DOUBLE;
			}else if(actionp == 4) {
				force = Action.SURRENDER;
			}else if(actionp == 5) {
				force = Action.SPLIT;
			}
			actionp = 0;
		}else {
			force = Suggestion.getAdvice(dealer.get(1).getValue(), phand, all);
		}
		if(force == Action.SPLIT) { 
			if(all.maxSplit() < 5 && phand.hasAce() != 1) {	
				split(phand.getHand());
				phand.setSplit();
				all.addSplit();
					/*if(phand.hasAce() > 0) { //cant resplit A,A
						int up = playerTally(phand);
						if(up == 1) {
							status = 1;
						}else if(up == 2 || up == 4) { //dealer must have turn
							status = 2;
						}else {
							status = 0;
						}			
						//return status;
					}
					
					while(all.maxSplit() < 5 && Suggestion.getAdvice(dealer.get(1).getValue(), phand, all) == Action.SPLIT) {
						split(phand.getHand());
						phand.setSplit();
						all.addSplit();
					}
					int down = playerTally(phand);
					if(down == 1) {
						status = 1;
					}else if(down == 2 || down == 4) { //dealer must have turn
						status = 2;
					}else {
						status = 0;
					}
					return status;*/

				if(all.maxSplit() == 1 && phand.getHand().get(0).getValue() == phand.getHand().get(1).getValue()) {
					actionp = 5;
				}
				return playerTurn(phand);
			}
		}
		else if(force == Action.DOUBLE) {
			if(phand.getHand().size() == 2) {
				doubleDown();
				phand.getHand().add(shoe.getCurrDeck().get(currcard));
				currcard++;
				phand.setDoubleDown();
					
				int up2 = playerTally(phand);
				if(up2 == 1) { //busted
					status = 1;
				}else if(up2 == 2 || up2 == 4) { //dealer must have turn
					status = 2;
				}
				return status;
			}
		}	
		else if(force == Action.SURRENDER) {
			if(dhand.getSum() != 21) {
				surrender();
				phand.setSurrender();
			}
			status = 0;
		}		
		else if(force == Action.HIT) {
			phand.getHand().add(shoe.getCurrDeck().get(currcard));
			currcard++;
			boolean hitP = true;
				
			while(hitP && (currcard < shoe.getCurrDeck().size() - 1)) {	
				force = Suggestion.getAdvice(dealer.get(1).getValue(), phand, all); //next action
				
				int up3 = playerTally(phand);	
				if(up3 == 1) { //busted
					hitP = false;
					status = 1;				
				}else if(up3 == 2 || up3 == 4) { //dealer must have turn
					status = 2;
					hitP = false;
				}else if(force == Action.HIT) {
					phand.getHand().add(shoe.getCurrDeck().get(currcard));
					currcard++;
					hitP = true;
				}else {
					if(force == Action.STAND || force != Action.HIT) {
						if(up3 == 1) { //busted
							status = 1;
						}else if(up3 == 2 || up3 == 4) { //dealer must have turn
							status = 2;
						}
						hitP = false;
					}			
				}
			} 
		}		
		else if(force == Action.STAND) {
			status = 2;
		}
		return status;
	}
	
	public int dealerTurn(BigDecimal insure_bet) {
		int status = 0;
		int tally = playerTally(phand);
		
		if(dealer.get(1).getRank().equalsIgnoreCase("A")){ //dealer is currently showing an ace
			if(dealer.get(0).getValue() == 10) {
				if (tally != 4) {
					dhand.setBlackjack();
				}
				if(phand.isInsurance()) {
					BigDecimal won = insure_bet.multiply(BigDecimal.valueOf(2.0));
					remaining = remaining.add(won).add(insure_bet);
					//remaining += (insure_bet*2 + insure_bet); //add to remaining -> won money back but original bet is still taken away (as already done)
				}
				status = 1;
			}else {
				//current hand continues as normal for p w/o insurance
				//p w/ insurance loses insurance bet & current hand continues
				if(phand.isInsurance()) {
					remaining = remaining.subtract(insure_bet);
				}
				if(tally != 0) {
					status = 1;
				}
			}
		}
		else if(dealer.get(1).getValue() == 10) {  //no insurance asked for here
			if(dealer.get(0).getRank().equalsIgnoreCase("A")) {
				if (tally != 4) {
					dhand.setBlackjack();
				}
				status = 1;
				return status;
			}
		}
		else {
			while(dhand.getSum() < 17 && (currcard < shoe.getCurrDeck().size())) {
				dealer.add(shoe.getCurrDeck().get(currcard));
				currcard++;
		
				int sum = dhand.getSum();
				if(sum > 21) {
					dhand.setBust();
					if(tally == 0 || tally == 2) {
						//didnt bust or get blackjack
						//BigDecimal back = bet.multiply(BigDecimal.valueOf(2.0));
						remaining = remaining.add(bet); //player gets back betx2 (wins)
					}
					status = 1;
					return status;
				}else if(sum == 21) {
					if(tally != 4) {
						dhand.setBlackjack();
					}	
					status = 1;
					return status;
				}else if(sum >= 17 && sum < 21){
					status = 0; //has soft 17 hand
					return status;
				}
			}
		}
		return status;
	}
	
	public void playerAction(Action first) { //forcing 1st action
		if(first == Action.STAND) {
			actionp = 1;
		}else if(first == Action.HIT) {
			actionp = 2;
		}else if(first == Action.DOUBLE) {
			actionp = 3;
		}else if(first == Action.SURRENDER) {
			actionp = 4;
		}else if(first == Action.SPLIT) {
			actionp = 5;
		}
	}
	
	public void shuffle(){
		Random rand = new Random(); 
	    for (int i = shoe.getCurrDeck().size() - 1; i > 0; i--) { 
	        // Random for remaining positions. 
	        int r = rand.nextInt(shoe.getCurrDeck().size()-i); 
	          
	         //swapping the elements 
	         Card temp = shoe.getCurrDeck().get(r); 
	         shoe.getCurrDeck().set(r, shoe.getCurrDeck().get(i)); 
	         shoe.getCurrDeck().set(i, temp);
	    } 
	}
	
	public void deal() {
		player.add(shoe.getCurrDeck().get(currcard));
		currcard++;
		
		dealer.add(shoe.getCurrDeck().get(currcard));
		currcard++;
		
		player.add(shoe.getCurrDeck().get(currcard));
		currcard++;
		
		dealer.add(shoe.getCurrDeck().get(currcard));
		currcard++;
	}
	
	public void redeal() {
		//clear
		clearHands();
		if(bet.compareTo(BigDecimal.valueOf(-1.0)) == 0) { 
			//terminate
			playing = false;
			return;
		}
		original = bet;
		//remaining = remaining.subtract(bet);
		all.addData(phand);
		deal(); //dealing first 2 cards to dealing & 2 to player from shuffled deck
		playing = true;
	}
	
	public void clearHands() {
		actionp = 0;
		dhand.clearData();
		dealer.clear();
		phand.clearData();
		all.clearData();
	}
	
	public Hand getDealerHand(){  //pass in dealer hand
		return dhand;
	}
	
	public ArrayList<Hand> getPlayerHands(){  //pass in player's hand(s)
		return all.getPlayerHands();
	}
	
	public void makeBet(BigDecimal newBet) {
		bet = newBet;
	}
	
	public void doubleDown() {
		if(dhand.getSum() != 21) {
			//remaining = remaining.subtract(original); //taking away bet*2 but original bet is already accounted for
			bet = bet.add(original);
		}
	}
	
	public void surrender() {
		BigDecimal half = bet.divide(BigDecimal.valueOf(2.0));
		bet = bet.subtract(half); //loses half of split bet & doesnt get it back in remaining
	}
	
	public void split(ArrayList<Card> player) {
		ArrayList <Card> split = new ArrayList<Card>();
		Hand new_hand = new Hand(split);

		split.add(player.get(1));
		player.remove(1);
		player.add(shoe.getCurrDeck().get(currcard));
		currcard++;
		
		if(player.get(0).getRank().equals("A")) {
			split.add(shoe.getCurrDeck().get(currcard));
			currcard++;
		}
		bet = bet.add(original); //doubling original bet -> represents bet for new split hand
		//remaining = remaining.subtract(original);
		all.addData(new_hand);
	}
	
	public void setBankroll(BigDecimal bank) {
		bankroll = bank;
	}

	public BigDecimal getBankroll() {
		return remaining.setScale(0,BigDecimal.ROUND_HALF_UP);
	}
	
	public int playerTally(Hand phand) {
		int status = 0;
		int sum = phand.getSum();
		int other = dhand.getSum();

		if(sum > 21) {
			if(all.getPlayerHands().size() == 1) {
				phand.setBust();
			}
			status = 1;
		}else if(sum == 21) {
			if(phand.getHand().size() > 2) {	
				status = 2; //dealer must go
				
			}else if(all.getPlayerHands().size() == 1 && phand.getHand().size() == 2) {
				/*if(other != 21) {
					phand.setBlackjack();
					BigDecimal back = original.multiply(BigDecimal.valueOf(1.5));
	
					remaining = remaining.add(back).add(bet); //payout for blackjack is 1.5*bet
				}else if(other == 21 && dhand.getHand().size() == 2) {
					remaining = remaining.add(bet); //tied game -> no blackjack
				}*/
				status = 4;
			}
		}else if(sum < 21) {
			status = 2;
		}
		return status;
	}
}

package blackjack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

import blackjack.Action;

public class StartGame {
	private static BigDecimal bankroll = BigDecimal.valueOf(0);
	private static BigDecimal remaining = BigDecimal.valueOf(0);
	private static BigDecimal bet = BigDecimal.valueOf(0);
	private static int currcard = 0; //top most card in deck
	private static int actionp = 0;
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
		//boolean start = false;
		Scanner scan = new Scanner(System.in);
		
		/*while(start == false) {
			try {
				int shoe_deck = 1;
				System.out.println("How many decks would you like to play with?");
				shoe_deck = scan.nextInt();
				
				if(shoe_deck > 0) {
					int playable = (shoe_deck*52) / 2;
					shoe = new Shoe(shoe_deck, playable);
					start = true;
				}
				else {
					//handle exception
					throw new NumberFormatException();
				}
				
			}catch(NumberFormatException io) {
				start = false;
			}
			
		}*/

		boolean dealing_game = true;
		int statusP = 0;
		int statusD = 0;
		boolean asked = false;
		BigDecimal original = BigDecimal.valueOf(0);
		BigDecimal insure_bet = BigDecimal.valueOf(0);;
		
		/*System.out.println("How much money do you want to put into your bankroll?");
		try {
			bankroll = scan.nextBigDecimal();
		}catch(NumberFormatException io) {
			io.printStackTrace();
		}*/
		
		//bet = getBet();
		//bet = makeBet();
		
		if(bet.compareTo(BigDecimal.valueOf(-1.0)) == 0) {
			//terminate
			dealing_game = false;
		}
		else {
			remaining = bankroll;
			original = bet;
			remaining = remaining.subtract(bet);
			 //must create new shoe object before line 90
			phand = new Hand(player);
			dhand = new Hand(dealer);
			all = new AllHands();
			shuffle(); //shuffling remaining cards in deck
			deal(); //dealing first 2 cards to dealing & 2 to player from shuffled deck
			all.addData(phand);
		}
		
		while(dealing_game) {
			if(asked == false) {
				try {
					if(dhand.hasAce() > 0){ //dealer is currently showing an ace
						//phand.printCurrentHand(player);
						System.out.println("Would you like Insurance? If so, how much? Type '0' for None.");
						insure_bet = scan.nextBigDecimal(); 
						
						BigDecimal two = bet.divide(BigDecimal.valueOf(2.0));
						
						while(insure_bet.compareTo(two) == 1) {
							System.out.println("Please enter insurance that is atmost half of your current bet which is $" + bet);
							insure_bet = scan.nextBigDecimal(); //assuming at most half of current bet (no check here)
						}
						
						if(insure_bet.compareTo(BigDecimal.valueOf(0)) == 1) {
							phand.setInsurance();
						}
						asked = true;
					}
				}catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
			
			for(int i = 0; i < all.getPlayerHands().size(); i++) {
				statusP = playerTurn(all.getPlayerHands().get(i), original);
			}
			if(statusP != 1) { //only call dealer if a split hand surrendered but not if single hand surrendered
				statusD = dealerTurn(insure_bet, original);
			}
			if((statusP == 1 &&  all.getPlayerHands().size() == 1) || (statusD == 1 && shoe.getCurrDeck().size() >= 4)) { //still cards to continue playing game w same deck(s) 
				//and dealer or player busted or won 
				//starting another game
				if(currcard >= shoe.getPlayable()) {
					shuffle();
					currcard = 0;
					//dealing_game = redeal(shoe, phand, dhand, all, original);
				}
				dealing_game = redeal(original);
			}
			
			if(shoe.getCurrDeck().size() < 4) {
				//end of game/deck -> count cards
				int winP = phand.getSum();
				int winD = dhand.getSum();
				
				if(winP > winD) {
					System.out.println("BlackJack! for Player");
					remaining = remaining.add(bet); //gets back current bet
				}
				else if (winD > winP) {
					System.out.println("BlackJack! for Dealer");
				}
				
				else {
					//tie game
					System.out.println("Tie!");
				}
				
				dealing_game = false;
			}
		}
		
		getBankroll(); //printing out money remaining in bankroll after deck officially ends
		//clearHands(phand, dhand, all);
	}
	
	public int playerTurn(Hand phand, BigDecimal original) {
		//Scanner scan = new Scanner(System.in);
		int status = 0;
		//String turn = null;
		ArrayList<Card> current = phand.getHand();
		ArrayList<Card> dealer = dhand.getHand();
		Action force = null;
		
		//while(hitP) {
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
			}	
			else {
				force = Suggestion.getAdvice(dealer.get(0).getValue(), phand, all);
			}
				
				
			if(current.size() == 1) { //the next splitted hand
				current.add(shoe.getCurrDeck().get(currcard));
				currcard++;
			}	
				
			//hand.printCurrentHand(current);
			//int sum = phand.getSum();
				//System.out.println("Type 'U' for Suggestion Utility.");
				//turn = scan.nextLine();
					
				//if(turn.equalsIgnoreCase("U")) {
					//System.out.println(Suggestion.getAdvice(dealer.get(0).getValue(), hand, all));
				//}
			try {
					//while(invalid) {
					//System.out.println("Type 'P' to split. Type 'D' to double down. Type 'R' to surrender. Type 'S' to stand or 'H' to hit. ");
					//turn = scan.nextLine();
					if(force == Action.SPLIT) { 
						//if(all.maxSplit() < 5 && current.size() == 2 && current.get(0).getValue() == current.get(1).getValue()) { //cant split when dealer is showing an Ace
						if(all.maxSplit() < 5 && remaining.compareTo(original) >= 0) {	
								split(current, original);
								phand.setSplit();
								all.addSplit();
								if(phand.hasAce() > 0) { //cant resplit once split an A,A
									if(playerTally(phand, original) == 1) {
										status = 1;
									}
									
									else {
										status = 0;
									}
									//invalid = false;
									//hitP = false;	
									return status;
								}
								//hand.printCurrentHand(current);
								while(all.maxSplit() < 5 && (Suggestion.getAdvice(dealer.get(0).getValue(), phand, all) == Action.SPLIT) && (remaining.compareTo(original) >= 0)) {
									//show resplit hand hands
									//System.out.println("Type 'PS' to resplit.");
									//turn = scan.nextLine();
									//if(turn.equalsIgnoreCase("PS")) {
										split(current, original);
										phand.setSplit();
										all.addSplit();
										//hand.printCurrentHand(current);
									//}
								}
								if(playerTally(phand, original) == 1) {
									status = 1;
								}
								
								else {
									status = 0;
								}
								return status;
						}
					
						/*else {
							System.out.println("You cannot split.");
							invalid = true;
						}*/
					}
					
					//else if(turn.equalsIgnoreCase("D")) {
					else if(force == Action.DOUBLE) {
						if(current.size() == 2) {
							doubleDown();
							current.add(shoe.getCurrDeck().get(currcard));
							currcard++;
							
							phand.setDoubleDown();
							//phand.printCurrentHand(current);
							if(playerTally(phand, original) == 1) {
								status = 1;
							}
							return status;
						}
						
						/*else {
							System.out.println("You cannot double down right now.");
						}*/
					}
					
					//else if(turn.equalsIgnoreCase("R")) {
					else if(force == Action.SURRENDER) {
						surrender(original);
						all.getPlayerHands().remove(phand); 
						//System.out.println("You Surrendered.");
						phand.setSurrender();
						
						if(all.getPlayerHands().size() <= 1) {
							status = 1; //so dont call dealer's hand -> round officially over
						}
						else {
							status = 0; 
						}
						return status;
					}
					
					//else if(turn.equalsIgnoreCase("H")){
					else if(force == Action.HIT) {
						boolean hitP = true;
						while(hitP) {
							current.add(shoe.getCurrDeck().get(currcard));
							currcard++;
							
							//phand.printCurrentHand(current);
							int what = playerTally(phand, original);
							if(what == 1) { //blackjack
								hitP = false;
								status = 1;
								return status;
							}
							
								//ask to hit or stay again if didnt get blackjack
							else if(what == 3) { //lost a split hand so done that round
								hitP = false;
								return status;
							}
							
							else {
								if(force == Action.STAND || force != Action.HIT) {
									if(playerTally(phand, original) == 1) {
										status = 1;
									}
									
									else {
										status = 0;
									}
									hitP = false;
									return status;
								}
								else if(force == Action.HIT) {
									hitP = true;
								}
							}
						}
					}
				
					//else if(turn.equalsIgnoreCase("S")) {
					else if(Suggestion.getAdvice(dealer.get(0).getValue(), phand, all) == Action.STAND) {
						//if stayed - now ask dealer to hit or stay
						if(playerTally(phand, original) == 1) {
							status = 1;
						}
						
						else {
							status = 0;
						}
					}
					else {
						throw new NoSuchElementException();
					}
			}catch(NoSuchElementException io) {
					System.out.println("You must make a valid move.");
			}
		
		return status;
	}
	
	public int dealerTurn(BigDecimal insure_bet, BigDecimal original) {
		int status = 0;
		ArrayList<Card> dealer = dhand.getHand();
		//cant split with insurance
		if(dhand.hasAce() == 1){ //dealer is currently showing an ace
			//dealer.get(1).toString(); //showing hole card now
			if(dealer.get(1).getValue() == 10) {
				//System.out.println("BlackJack! for Dealer");
				dhand.setBlackjack();
				if(phand.isInsurance()) {
					BigDecimal won = insure_bet.multiply(BigDecimal.valueOf(2.0));
					remaining = remaining.add(won).add(insure_bet);
					//remaining += (insure_bet*2 + insure_bet); //add to remaining -> won money back but original bet is still taken away (as already done)
				}
				if(phand.getSum() == 21) {
					phand.setBlackjack();
					remaining = remaining.add(bet); //gets paid original bet back -> even money
					//remaining -= insure_bet;
				}

				else if(phand.getSum() > 21) {
					//System.out.println("And Player busts.");
					phand.setBust();
						//doesnt get original bet back
						//if doesnt bust - still keeps insurance bet and loses original bet because lost either way
				}
				
				status = 1;
				return status;
			}
			else {
				//current hand continues as normal for p w/o insurance
				//p w/ insurance loses insurance bet & current hand continues
				if(insure_bet.compareTo(BigDecimal.valueOf(0)) == 1) {
					remaining = remaining.subtract(insure_bet);
				}
				if(phand.getSum() == 21) {
					//System.out.println("BlackJack! for Player.");
					phand.setBlackjack();
					BigDecimal paid = bet.multiply(BigDecimal.valueOf(1.5));
					remaining = remaining.add(bet).add(paid); //gets paid original bet + 3/2*bet (or 1.5x) but still loses insurance bet
					status = 1;
				}
				return status;
			}
		}
		
		else if(dealer.get(0).getValue() == 10) {  //no insurance asked for here
			if(dhand.hasAce() > 0) {
				dealer.get(1).toString(); //showing hole card now
				//System.out.println("BlackJack! for Dealer");
				dhand.setBlackjack();
				
				//check if player has blackjack also
				if(phand.getSum() == 21) {
					//System.out.println("BlackJack! for Player also... It's a tie!");
					phand.setBlackjack();
					remaining = remaining.add(bet); //just gets original bet back
				}
				
				status = 1;
				return status;	
			}
		}
		
		//int j = 0;
		//dhand.printCurrentHand(dealer);
		status = 1;
		//j++; 
		
		while(dhand.getSum() < 17 && (currcard < shoe.getCurrDeck().size() - 1)) {
			dealer.add(shoe.getCurrDeck().get(currcard));
			currcard++;
			
			//System.out.println(dealer.get(j).toString());
			//j++;
			
			int sum = dhand.getSum();
			if(sum > 21) {
				//System.out.println("Dealer Busted... You won!");
				dhand.setBust();
				BigDecimal back = bet.multiply(BigDecimal.valueOf(2.0));
				remaining = remaining.add(back); //gets back its current bet
				status = 1;
			}
			else if(sum == 21) {
				//System.out.println("BlackJack! for Dealer"); //already took out bet from remaining
				dhand.setBlackjack();
				if(phand.getSum() == 21) {
					//System.out.println("BlackJack! for Player also... It's a tie!");
					phand.setBlackjack();
					remaining = remaining.add(bet);
				}	
				status = 1;
			}
			else if(sum >= 17 && sum < 21){
				status = 0; //still in game
			}
		}
				
		return status;
	}
	
	public void playerAction(Action first) { //force first action to STAND
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
	//shuffling deck of cards for each new round
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
		//deals player first, then dealer, then player, then dealer
		//int count = 0;
		/*while((currcard < shoe.getCurrDeck().size() - 1) && count < 3) { //only want to loop through 2x
			player.add(shoe.getCurrDeck().get(currcard));
			//currcard = shoe.removeFromDeck(currcard);
			currcard++;
			
			dealer.add(shoe.getCurrDeck().get(currcard));
			//currcard = shoe.removeFromDeck(currcard);
			currcard++;
		}*/
		
		player.add(shoe.getCurrDeck().get(currcard));
		currcard++;
		
		dealer.add(shoe.getCurrDeck().get(currcard));
		currcard++;
		
		player.add(shoe.getCurrDeck().get(currcard));
		currcard++;
		
		dealer.add(shoe.getCurrDeck().get(currcard));
		currcard++;
		
		//System.out.println("D's card: " + dealer.get(0).toString());
	}
	
	public boolean redeal(BigDecimal original) {
		//clear
		clearHands();
		//System.out.println(getBankroll()); 
		
		//bet = getBet();
		//bet = makeBet();
		if(bet.compareTo(BigDecimal.valueOf(-1.0)) == 0) { 
			//terminate
			return false;
		}
		original = bet;
		remaining = remaining.subtract(bet);
		
		/*ArrayList<Card> player = new ArrayList<Card>();
		ArrayList<Card> dealer = new ArrayList<Card>(); */
		phand = new Hand(player);
		dhand = new Hand(dealer);
		
		deal(); //dealing first 2 cards to dealing & 2 to player from shuffled deck
		all.addData(phand);
		
		return true;
	}
	
	public void clearHands() {
		actionp = 0;
		dhand.clearData();
		phand.clearData();
		all.clearData();
	}
	
	public Hand getDealerHand(){  //pass in dealer hand
		return dhand;
	}
	
	public ArrayList<Hand> getPlayerHands(){  //pass in dealer hand
		return all.getPlayerHands();
	}
	
	public void makeBet(BigDecimal newBet) {
		bet = newBet;
	}
	
	/*public BigDecimal getBet() {
		Scanner scan = new Scanner(System.in);
		BigDecimal bet;
		
		if(remaining.compareTo(BigDecimal.valueOf(10)) == -1) {
			System.out.println("You have " + remaining + "... and cannot make a full bet (bet > 10).");
			bet = BigDecimal.valueOf(-1.0);
			return bet; 
		}
		
		System.out.println("How much money do you want to bet this game? (Bet must be atleast 10)" + "\n" + "Remaining: " + remaining);
		bet = scan.nextBigDecimal();
		
		while(bet.compareTo(remaining) == 1 && remaining.compareTo(BigDecimal.valueOf(10)) == 1) {
			System.out.println("You do not have enough money to bet that amount.");
			System.out.println("How much money do you want to bet this game?" + "\n" + "Remaining: " + remaining);
			bet = scan.nextBigDecimal();
		}
		
		while(bet.compareTo(BigDecimal.valueOf(10)) == -1) {
			System.out.println("Your bet must be atleast 10");
			System.out.println("How much money do you want to bet this game?" + "\n" + "Remaining: " + remaining);
			bet = scan.nextBigDecimal();
		}
		
		return bet;
	}*/
	
	public void doubleDown() {
		remaining = remaining.subtract(bet); //taking away bet*2 but original bet is already accounted for
		bet = bet.multiply(BigDecimal.valueOf(2.0));
	}
	
	public void surrender(BigDecimal original) {
		if(all.getPlayerHands().size() > 1) {
			BigDecimal half = original.divide(BigDecimal.valueOf(2.0));
			bet = bet.subtract(half); //loses half of split bet & doesnt get it back in remaining
		}
		else {
			bet = bet.divide(BigDecimal.valueOf(2.0));
			remaining = remaining.add(bet); //loses half its bet for that round
		}
		
	}
	
	public void split(ArrayList<Card> player, BigDecimal original) {
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
		
		remaining = remaining.subtract(original); //take out from remaining
		bet = bet.add(original); //doubling original bet -> represents bet for new split hand
		all.addData(new_hand);

	}
	
	public void setBankroll(BigDecimal bank) {
		bankroll = bank;
	}

	public String getBankroll() {
		String results = "total bankroll of $" + remaining;
		/*if(remaining.compareTo(BigDecimal.valueOf(0)) == -1) {
			results = "You owe " + remaining + " dollars.";
		}
		
		else{
			results = "Your payout is " + remaining + " dollars!";
		} */
		return results;
	}
	
	public int playerTally(Hand phand, BigDecimal original) {
		int status = 0;
		if(phand.getSum() > 21) {
			if(all.getPlayerHands().size() > 1) {
				//System.out.println("Busted... You lost this current hand.");
				//phand.isBust();
				bet = bet.subtract(original);
				//doesnt get original back in remaining
				status = 3;
			}
			
			else if(all.getPlayerHands().size() == 1) {
				//System.out.println("Busted... You lost.");
				phand.setBust();
				status = 1;
			}
			all.getPlayerHands().remove(phand);
			
		}
		else if(phand.getSum() == 21) {
			if(all.getPlayerHands().size() > 1) {
				//System.out.println("BlackJack! for this hand");
				//phand.isBlackjack();
				bet = bet.subtract(original);
				remaining = remaining.add(original);
				status = 3;
			}
			
			else if(all.getPlayerHands().size() == 1) {
				//System.out.println("BlackJack! for you.");
				phand.setBlackjack();
				BigDecimal back = bet.multiply(BigDecimal.valueOf(1.5));
				
				remaining = remaining.add(bet).add(back); //payout for blackjack
				status = 1;
			}
			
			all.getPlayerHands().remove(phand);
		}
		
		return status;
	}
}

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
	private static BigDecimal original = BigDecimal.valueOf(0);
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
			 //must create new shoe object before line 90
		}
	}
	
	public void startGame() {
		Scanner scan = new Scanner(System.in);
		int statusP = 0;
		int statusD = 0;
		boolean asked = false;
		boolean shuffled = false;
		BigDecimal insure_bet = BigDecimal.valueOf(0);
		
		phand = new Hand(player);
		dhand = new Hand(dealer);
		all = new AllHands();
		all.addData(phand);
		//shuffle(); //shuffling remaining cards in deck
		deal(); //dealing first 2 cards to dealing & 2 to player from shuffled deck
		
		while(playing) {
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
				statusP = playerTurn(all.getPlayerHands().get(i));
				//System.out.println(getBankroll());
			}
			
			if(statusP == 2 || statusP == 0) { //only call dealer if a split hand surrendered but not if single hand surrendered
				statusD = dealerTurn(insure_bet);
				//System.out.println("D:"+ getBankroll());
			}
			//infinite loop for player/dealer (statusD/statusP is never 1)
			
			if((statusP == 1 || statusD == 1 || statusP == 2) && currcard <= shoe.getCurrDeck().size() - 4) { //still has 4+ cards to continue playing game
				//and dealer or player busted or won 
				//only redeal 
				//starting another game
				
				//System.out.println("hi");
				if(currcard >= shoe.getPlayable() && shuffled == false) {
					shuffle();
					currcard = 0;
					shuffled = true; 
				}
				redeal();
				if (playing == false) {
					//infinite loop is here -> never reaches playing = false;
					 break;
				}
			}
			
			else if(currcard > shoe.getCurrDeck().size() - 4) { //end of deck so tally and terminate game
				int winP = phand.getSum();
				int winD = dhand.getSum();
				
				//System.out.println("hi");
				if(winP != winD) {
					if(winP == 21 && winD != 21) {
						if(winD > 21) {
							dhand.setBust();
						}
						phand.setBlackjack();
						BigDecimal back = bet.multiply(BigDecimal.valueOf(1.5));
						
						//remaining = remaining.add(bet).add(back); //payout for blackjack
						remaining = remaining.add(back); //payout for blackjack is 1.5*bet
					}
					else if (winP != 21 && winD == 21) {
						if(winP > 21) {
							phand.setBust();
						}
						dhand.setBlackjack();
					}
					
					else {
						if(winP > winD && winP < 21) { //no one busted but player gets back original bet
							remaining = remaining.add(bet);
						}
						
						else if(winP > winD && winP > 21) {
							phand.setBust();
						}
						else if(winP < winD && winD > 21) {
							//dealer busts -> you didnt bust
							dhand.setBust();
							//remaining = remaining.add(bet); 
							BigDecimal back = bet.multiply(BigDecimal.valueOf(2.0));
							remaining = remaining.add(back); //player gets back betx2 (wins)
						}
						else if(winP > 21 && winD > 21) {
							dhand.setBust();
							phand.setBust();
						}
					}
				}
				else if(winP == winD){ //"push" no one wins but player loses if both bust
					if(winP <= 21) {
						//gets back original bet
						remaining = remaining.add(bet);
					}
				}
				//System.out.println("hi");
				playing = false;
				//break;
			}
			
		}
		//System.out.println("hi");
		//getBankroll(); //printing out money remaining in bankroll after deck officially ends
		//clearHands(phand, dhand, all);
	}
	
	public int playerTurn(Hand phand) {
		int status = 0;
		//String turn = null;
		ArrayList<Card> current = phand.getHand();
		ArrayList<Card> dealer = dhand.getHand();
		Action force = null;
		int tally = playerTally(phand);
		
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
			phand.printCurrentHand(player);
			System.out.println(force + "\n");
			if(current.size() == 1) { //the next splitted hand
				current.add(shoe.getCurrDeck().get(currcard));
				currcard++;
			}
			
			if(force == Action.SPLIT) { 
				if(all.maxSplit() < 5) {	
								split(current);
								phand.setSplit();
								all.addSplit();
								if(phand.hasAce() > 0) { //cant resplit A,A
									int up = playerTally(phand);
									if(up == 1) {
										status = 1;
									}
									else if(up == 2 || up == 4) { //dealer must have turn
										status = 2;
									}
									
									else {
										status = 0;
									}
									//invalid = false;
									//hitP = false;	
									return status;
								}
								//hand.printCurrentHand(current);
								while(all.maxSplit() < 5 && (Suggestion.getAdvice(dealer.get(0).getValue(), phand, all) == Action.SPLIT)) {
									//show resplit hand hands
									//System.out.println("Type 'PS' to resplit.");
									//turn = scan.nextLine();
									//if(turn.equalsIgnoreCase("PS")) {
										split(current);
										phand.setSplit();
										all.addSplit();
										//hand.printCurrentHand(current);
									//}
								}
								int down = playerTally(phand);
								if(down == 1) {
									status = 1;
								}
								else if(down == 2 || down == 4) { //dealer must have turn
									status = 2;
								}
								
								else {
									status = 0;
								}
								return status;
						}
			}
		
			else if(force == Action.DOUBLE) {
				if(current.size() == 2) {
							doubleDown();
							current.add(shoe.getCurrDeck().get(currcard));
							currcard++;
							
							phand.setDoubleDown();
							//phand.printCurrentHand(current);
							int up2 = playerTally(phand);
							if(up2 == 1) { //busted
								status = 1;
							}
							else if(up2 == 2 || up2 == 4) { //dealer must have turn
								status = 2;
							}
							return status;
				}
						
						/*else {
							System.out.println("You cannot double down right now.");
						}*/
			}
					
			else if(force == Action.SURRENDER) {
						surrender(); 
						if(all.getPlayerHands().size() == 1) {
							phand.setSurrender();
							status = 1; //so dont call dealer's hand -> round officially over
						}
						else if(all.getPlayerHands().size() > 1) {
							status = 0;
						}
						all.getPlayerHands().remove(phand); 
						//System.out.println("You Surrendered.");
						return status;
			}
				
			else if(force == Action.HIT) {
				current.add(shoe.getCurrDeck().get(currcard));
				currcard++;
				
				boolean hitP = true;
				while(hitP) {
							
							
							force = Suggestion.getAdvice(dealer.get(0).getValue(), phand, all); //next action
							int up3 = playerTally(phand);
							if(up3 == 1) { //busted
								hitP = false;
								status = 1;
								return status;
								
							}
							else if(up3 == 2 || up3 == 4) { //dealer must have turn
								status = 2;
								hitP = false;
								return status;
							}
							
								//ask to hit or stay again if didnt get blackjack
							else if(up3 == 3) { //lost a split hand so done that round
								hitP = false;
								return status;
							}
							
							else if(force == Action.HIT) {
								current.add(shoe.getCurrDeck().get(currcard));
								currcard++;
								hitP = true;
							}
							else {
								if(force == Action.STAND || force != Action.HIT) {
									//int tally = playerTally(phand, original);
									if(tally == 1) { //busted
										status = 1;
									}
									else if(tally == 2 || tally == 4) { //dealer must have turn
										status = 2;
									}
									hitP = false;
									return status;
								}
								
							}
				}
			}
				
			else if(force == Action.STAND) {
				if(tally == 1) { //busted
					status = 1;
					
				}
				else if(tally == 2 || tally == 4) { //dealer must have turn
					status = 2;
				}
			}
		return status;
	}
	
	public int dealerTurn(BigDecimal insure_bet) {
		int status = 0;
		int tally = playerTally(phand);
		
		dhand.printCurrentHand(dealer);
		if(dealer.get(0).getRank().equalsIgnoreCase("A")){ //dealer is currently showing an ace
			//dealer.get(1).toString(); //showing hole card now
			if(dealer.get(1).getValue() == 10) {
				//System.out.println("BlackJack! for Dealer");
				if (tally != 4) {
					dhand.setBlackjack();
				}
				if(phand.isInsurance()) {
					BigDecimal won = insure_bet.multiply(BigDecimal.valueOf(2.0));
					remaining = remaining.add(won).add(insure_bet);
					//remaining += (insure_bet*2 + insure_bet); //add to remaining -> won money back but original bet is still taken away (as already done)
				}
				status = 1;
			}
			else {
				//current hand continues as normal for p w/o insurance
				//p w/ insurance loses insurance bet & current hand continues
				if(insure_bet.compareTo(BigDecimal.valueOf(0)) == 1) {
					remaining = remaining.subtract(insure_bet);
				}
				if(tally != 0 || tally != 3) {
					status = 1;
				}
			}
			return status;
		}
		
		else if(dealer.get(0).getValue() == 10) {  //no insurance asked for here
			if(dealer.get(1).getRank().equalsIgnoreCase("A")) {
				//check if player has blackjack also
				if (tally != 4) {
					dhand.setBlackjack();
				}
				status = 1;
				return status;
			}
		}
		
		while(dhand.getSum() < 17 && (currcard < shoe.getCurrDeck().size() - 1)) {
				dealer.add(shoe.getCurrDeck().get(currcard));
				currcard++;
				int sum = dhand.getSum();
				if(sum > 21) {
					//System.out.println("Dealer Busted... You won!");
					dhand.setBust();
					if(tally == 0 || tally == 2) {
						//didnt bust or get blackjack
						BigDecimal back = bet.multiply(BigDecimal.valueOf(2.0));
						remaining = remaining.add(back); //player gets back betx2 (wins)
					}
					status = 1;
				}
				else if(sum == 21) {
					//System.out.println("BlackJack! for Dealer"); //already took out bet from remaining
					if(tally != 4) {
						//System.out.println("BlackJack! for Player also... It's a tie!");
						dhand.setBlackjack();
						//remaining = remaining.add(bet); //just gets original bet back (no one wins blackjack)
					}	
					status = 1;
				}
				else if(sum >= 17 && sum < 21){
					status = 0; //still in game
					return status;
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
	
	public void surrender() {
		if(all.getPlayerHands().size() > 1) {
			BigDecimal half = original.divide(BigDecimal.valueOf(2.0));
			bet = bet.subtract(half); //loses half of split bet & doesnt get it back in remaining
		}
		else {
			bet = bet.divide(BigDecimal.valueOf(2.0));
			remaining = remaining.add(bet); //loses half its bet for that round
		}
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
	
	public int playerTally(Hand phand) {
		int status = 0;
		if(phand.getSum() > 21) {
			if(all.getPlayerHands().size() > 1) {
				//System.out.println("Busted... You lost this current hand.");
				//phand.isBust();
				remaining = remaining.subtract(original);
				bet = bet.subtract(original);
				phand.clearData();
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
				
				phand.clearData();
				status = 3;
			}
			
			else if(all.getPlayerHands().size() == 1) {
				//System.out.println("BlackJack! for you.");
				if(dhand.getSum() != 21) {
					phand.setBlackjack();
					BigDecimal back = bet.multiply(BigDecimal.valueOf(1.5));
					
					//remaining = remaining.add(bet).add(back); //payout for blackjack
					remaining = remaining.add(back); //payout for blackjack is 1.5*bet
				}
				else if(dhand.getSum() == 21) {
					remaining = remaining.add(bet); //tied game -> no blackjack
				}
				status = 4;
			}
			all.getPlayerHands().remove(phand);
			//phand.clearData();
		}
		else if(phand.getSum() < 21 && all.getPlayerHands().size() == 1) {
			status = 2;
		}
		
		return status;
	}
}

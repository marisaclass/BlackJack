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
	private static int didSplit = 0;
	private static boolean actionp = false;
	private Shoe shoe = null;

	public StartGame(){
		
	}
	
	public static void main(String[] args) {
		StartGame game = new StartGame();
		//Modify start game to import j units for test
		game.run();
		
	}
	
	public void setShoe(int deck, int playable, int... cards) {
		shoe = new Shoe(deck, playable, cards);
	}
	
	public void run() {
		//boolean start = false;
		Hand hand = new Hand();
		Scanner scan = new Scanner(System.in);
		ArrayList <Card> dealer = new ArrayList<Card>();
		ArrayList <Card> player = new ArrayList<Card>();
		
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
		boolean hasAce = false;
		boolean asked = false;
		BigDecimal original = BigDecimal.valueOf(0);
		BigDecimal insure_bet = BigDecimal.valueOf(0);;
		
		System.out.println("How much money do you want to put into your bankroll?");
		try {
			bankroll = scan.nextBigDecimal();
		}catch(NumberFormatException io) {
			io.printStackTrace();
		}
		remaining = bankroll;
		//bet = getBet();
		//bet = makeBet();
		
		if(bet.compareTo(BigDecimal.valueOf(-1.0)) == 0) {
			//end of game 
			//terminate
			dealing_game = false;
		}
		else {
			original = bet;
			remaining = remaining.subtract(bet);
			ArrayList<Card> deck = shoe.createDeck(); //must create new shoe object before line 90
			shuffle(shoe); //shuffle deck
			deal(shoe, dealer, player); //deal
			hand.getPlayerHands().add(player);
		}
		
		while(dealing_game) {
			if(currcard == shoe.getPlayable()) {
				shuffle(shoe);
			}
			if(asked == false) {
				try {
					if(dealer.get(0).getRank().equals("A")){ //dealer is currently showing an ace
						hasAce = true;
						System.out.println("Your hand: ");
						for(int i = 0; i < player.size(); i++) {
							System.out.println(player.get(i).toString());
						}
						System.out.println("Would you like Insurance? If so, how much? Type '0' for None.");
						insure_bet = scan.nextBigDecimal(); 
						
						BigDecimal two = bet.divide(BigDecimal.valueOf(2.0));
						
						while(insure_bet.compareTo(two) == 1) {
							System.out.println("Please enter insurance that is atmost half of your current bet which is $" + bet);
							insure_bet = scan.nextBigDecimal(); //assuming at most half of current bet (no check here)
						}
						asked = true;
					}
				}catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
			for(int i = 0; i < hand.getPlayerHands().size(); i++) {
				statusP = playerTurn(hand.getPlayerHands().get(i), dealer, shoe, hand, hasAce, original);
			}
			if(statusP != 1) { //only call dealer if a split hand surrendered but not if single hand surrendered
				statusD = dealerTurn(dealer, player, shoe, insure_bet, original);
			}
			if((statusP == 1 && hand.getPlayerHands().size() == 1) || (statusD == 1 && shoe.getCurrDeck().size() > 0)) { //still cards to continue playing game w same deck(s) 
				//and dealer or player busted or won 
				//starting another game
				dealing_game = redeal(shoe, hand, dealer, player, original);
				hasAce = false;
				//return;
			}
			
			if(shoe.getCurrDeck().size() == 0) {
				//end of game/deck -> count cards
				int winP = sum(player);
				int winD = sum(dealer);
				
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
		clearHands(hand, dealer, player);
		hasAce = false;
	}
	
	public int playerTurn(ArrayList<Card> player, ArrayList<Card> dealer, Shoe shoe, Hand hand, boolean hasAce, BigDecimal original) {
		boolean hitP = true;
		boolean invalid = true;
		Scanner scan = new Scanner(System.in);
		int status = 0;
		String turn = null;
		int ace = 0;
		
		while(hitP) {
				invalid = true;
				if(actionp == true) { //forcing first action to STAND
					if(playerTally(hand, player, original) == 1) {
						status = 1;
					}
					
					else {
						status = 0;
					}
					actionp = false;
					invalid = false;
					hitP = false;
					return status;
				}
				
				
				if(player.size() == 1) { //the next splitted hand
					player.add(shoe.getCurrDeck().get(currcard));
					currcard = shoe.removeFromDeck(currcard);
				}	
				
				System.out.println("Your currrent hand: ");
				for(int i = 0; i < player.size(); i++) {
					if(player.get(i).getRank().equals("A")) {
						ace++;
					}
					System.out.println(player.get(i).toString());
				}
				
				int sum = sum(player);
				System.out.println("Type 'U' for Suggestion Utility.");
				turn = scan.nextLine();
					
				if(turn.equalsIgnoreCase("U")) {
					Suggestion sugg = null;
					if(ace > 0) { //dealer has 17 soft hand
							sugg = new Suggestion(dealer, player, sum, true); //compare both cards
							
					}else { //hard hand
							sugg = new Suggestion(dealer, player, sum, false);
					}
					System.out.println(sugg.ToDo());
				}
				
				while(invalid) {
					try {
					System.out.println("Type 'P' to split. Type 'D' to double down. Type 'R' to surrender. Type 'S' to stand or 'H' to hit. ");
					turn = scan.nextLine();
					if(turn.equalsIgnoreCase("P")) { 
						if(didSplit < 4 && hasAce == false && player.size() == 2 && player.get(0).getValue() == player.get(1).getValue()) { //cant split when dealer is showing an Ace
							if(remaining.compareTo(original) >= 0) {
								split(player, shoe, hand, original);
								didSplit++;
								if(player.get(0).getRank().equals("A")) {
									if(playerTally(hand, player, original) == 1) {
										status = 1;
									}
									
									else {
										status = 0;
									}
									invalid = false;
									hitP = false;	
									return status;
								}
								System.out.println("Your new current hand: ");
								for(int i = 0; i < player.size(); i++) {
									System.out.println(player.get(i).toString());
								}
								while(didSplit < 4 && player.get(0).getValue() == player.get(1).getValue() && (remaining.compareTo(original) >= 0)) {
									//show resplit hand hands
									System.out.println("Type 'PS' to resplit.");
									turn = scan.nextLine();
									if(turn.equalsIgnoreCase("PS")) {
										split(player, shoe, hand, original);
										didSplit++;
										
										System.out.println("Your new current hand: ");
										for(int i = 0; i < player.size(); i++) {
											System.out.println(player.get(i).toString());
										}
									}
									
									else {
										break;
									}
								}
							}
						}
						else {
							System.out.println("You cannot split right now.");
							invalid = true;
						}
					}
					
					else if(turn.equalsIgnoreCase("D")) {
						if(sum == 9 || sum == 10 || sum == 11 && player.size() == 2) {
							doubleDown();
							player.add(shoe.getCurrDeck().get(currcard));
							currcard = shoe.removeFromDeck(currcard);
							
							System.out.println("Your hand: ");
							for(int i = 0; i < player.size(); i++) {
								System.out.println(player.get(i).toString());
							}
							if(playerTally(hand, player, original) == 1) {
								status = 1;
							}
							invalid = false;
							hitP = false;
							return status;
						}
						
						else {
							System.out.println("You cannot double down right now.");
							invalid = true;
						}
					}
					
					else if(turn.equalsIgnoreCase("R")) {
						surrender(hand, original);
						hand.getPlayerHands().remove(player); 
						System.out.println("You Surrendered.");
						
						if(hand.getPlayerHands().size() <= 1) {
							status = 1; //so dont call dealer's hand -> round officially over
						}
						else {
							status = 0; 
						}
						invalid = false; //exit turn
						hitP = false;
						return status;
					}
					
					else if(turn.equalsIgnoreCase("H")){
						player.add(shoe.getCurrDeck().get(currcard));
						currcard = shoe.removeFromDeck(currcard);
						
						System.out.println("Your currrent hand: ");
						for(int i = 0; i < player.size(); i++) {
							System.out.println(player.get(i).toString());
						}
						int what = playerTally(hand, player, original);
						if(what == 1) { //blackjack
							invalid = false;
							hitP = false;
							status = 1;
						}
						else {
							//ask to hit or stay again if didnt get blackjack
							if(what == 3) { //lost a split hand
								invalid = false;
								hitP = false;
								return status;
							}
							invalid = false;
							hitP = true;
						}
					}
				
					else if(turn.equalsIgnoreCase("S")) {
						//if stayed - now ask dealer to hit or stay
						if(playerTally(hand, player, original) == 1) {
							status = 1;
						}
						
						else {
							status = 0;
						}
						invalid = false;
						hitP = false;
					}
					else {
						throw new NoSuchElementException();
					}
				}catch(NoSuchElementException io) {
					System.out.println("You must make a valid move.");
					invalid = true;
				}
			}
		}
		
		return status;
	}
	
	public int dealerTurn(ArrayList<Card> dealer, ArrayList<Card> player, Shoe shoe, BigDecimal insure_bet, BigDecimal original) {
		int status = 0;
		//cant split with insurance
		if(dealer.get(0).getRank().equals("A")){ //dealer is currently showing an ace
			dealer.get(1).toString(); //showing hole card now
			if(dealer.get(1).getValue() == 10) {
				System.out.println("BlackJack! for Dealer");
				if(insure_bet.compareTo(BigDecimal.valueOf(0)) == 1) {
					BigDecimal won = insure_bet.multiply(BigDecimal.valueOf(2.0));
					remaining = remaining.add(won).add(insure_bet);
					//remaining += (insure_bet*2 + insure_bet); //add to remaining -> won money back but original bet is still taken away (as already done)
				}
				if(sum(player) == 21) {
					System.out.println("BlackJack! for Player also... It's a tie!");
					remaining = remaining.add(bet); //gets paid original bet back -> even money
					//remaining -= insure_bet;
				}

				else if(sum(player) > 21) {
						System.out.println("And Player busts.");
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
				if(sum(player) == 21) {
					System.out.println("BlackJack! for Player.");
					BigDecimal paid = bet.multiply(BigDecimal.valueOf(1.5));
					remaining = remaining.add(bet).add(paid); //gets paid original bet + 3/2*bet (or 1.5x) but still loses insurance bet
					status = 1;
				}
				return status;
			}
		}
		
		else if(dealer.get(0).getValue() == 10) {  //no insurance asked for here
			if(dealer.get(1).getRank().equals("A")) {
				dealer.get(1).toString(); //showing hole card now
				System.out.println("BlackJack! for Dealer");
				
				//check if player has blackjack also
				if(sum(player) == 21) {
					System.out.println("BlackJack! for Player also... It's a tie!");
					remaining = remaining.add(bet); //just gets original bet back
				}
				
				status = 1;
				return status;	
			}
		}
		
		int j = 0;
		System.out.println("D's hand:");
		for(int i = 0; i < dealer.size(); i++) {
			System.out.println(dealer.get(i).toString());
			j = i;
		}
		status = 1;
		j++; 
		while(sum(dealer) < 17) {
			dealer.add(shoe.getCurrDeck().get(currcard));
			currcard = shoe.removeFromDeck(currcard);
			
			System.out.println(dealer.get(j).toString());
			j++;
			
			int sum = sum(dealer);
			if(sum > 21) {
				System.out.println("Dealer Busted... You won!");
				BigDecimal back = bet.multiply(BigDecimal.valueOf(2.0));
				remaining = remaining.add(back); //gets back its current bet
				status = 1;
			}
			else if(sum == 21) {
				System.out.println("BlackJack! for Dealer"); //already took out bet from remaining
				if(sum(player) == 21) {
					System.out.println("BlackJack! for Player also... It's a tie!");
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
			actionp = true;
		}
		else {
			actionp = false;
		}
	}
	
	public static void shuffle(Shoe shoe){
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
	
	public static void deal(Shoe shoe, ArrayList<Card> dealer, ArrayList<Card> player) {
		//deals player first, then dealer, then player, then dealer
		player.add(shoe.getCurrDeck().get(currcard));
		currcard = shoe.removeFromDeck(currcard);
		
		dealer.add(shoe.getCurrDeck().get(currcard));
		currcard = shoe.removeFromDeck(currcard);
		
		player.add(shoe.getCurrDeck().get(currcard));
		currcard = shoe.removeFromDeck(currcard);
			
		dealer.add(shoe.getCurrDeck().get(currcard));
		currcard = shoe.removeFromDeck(currcard);
		
		System.out.println("D's card: " + dealer.get(0).toString());
	}
	
	public boolean redeal(Shoe shoe, Hand hand, ArrayList<Card> dealer, ArrayList<Card> player, BigDecimal original) {
		//clear
		clearHands(hand, dealer, player);
		System.out.println(getBankroll()); 
		
		//bet = getBet();
		//bet = makeBet();
		if(bet.compareTo(BigDecimal.valueOf(-1.0)) == 0) {
			//end of game 
			//terminate
			return false;
		}
		original = bet;
		remaining = remaining.subtract(bet);
		shuffle(shoe); //shuffling remaining cards in deck
		deal(shoe, dealer, player); //dealing first 2 cards to dealing & 2 to player from shuffled deck
		hand.getPlayerHands().add(player);
		return true;
	}
	
	public void clearHands(Hand hand, ArrayList<Card> dealer, ArrayList<Card> player) {
		didSplit = 0; //reset
		actionp = false;
		dealer.clear();
		player.clear();
		hand.getPlayerHands().clear();
	}
	
	public int sum(ArrayList <Card> hand) {
		int count = 0;
		int ace = 0;
		int high = 0; //keeping track of values of 11 being used
		
		for(int i = 0; i < hand.size(); i++) {
			if(hand.get(i).getRank().equals("A")) {
				ace++;
			}
			else {
				count += hand.get(i).getValue(); 
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
		}
		
		while(count > 21 && high > 0) { //checking at end 	
			count -= 11;
			count++; 
			high--;
		}

		return count;
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
	
	public void surrender(Hand hand, BigDecimal original) {
		if(hand.getPlayerHands().size() > 1) {
			BigDecimal half = original.divide(BigDecimal.valueOf(2.0));
			bet = bet.subtract(half); //loses half of split bet & doesnt get it back in remaining
		}
		else {
			bet = bet.divide(BigDecimal.valueOf(2.0));
			remaining = remaining.add(bet); //loses half its bet for that round
		}
		
	}
	
	public void split(ArrayList<Card> player, Shoe shoe, Hand hand, BigDecimal original) {
		ArrayList <Card> split = new ArrayList<Card>();
		
		split.add(player.get(1));
		player.remove(1);
		
		player.add(shoe.getCurrDeck().get(currcard));
		currcard = shoe.removeFromDeck(currcard);
		
		if(player.get(0).getRank().equals("A")) {
			split.add(shoe.getCurrDeck().get(currcard));
			currcard = shoe.removeFromDeck(currcard);
		}
		
		remaining = remaining.subtract(original); //take out from remaining
		bet = bet.add(original); //doubling original bet -> represents bet for new split hand
		hand.getPlayerHands().add(split);
	}

	public static String getBankroll() {
		String results = "total bankroll of $" + remaining;
		/*if(remaining.compareTo(BigDecimal.valueOf(0)) == -1) {
			results = "You owe " + remaining + " dollars.";
		}
		
		else{
			results = "Your payout is " + remaining + " dollars!";
		} */
		return results;
	}
	
	public int playerTally(Hand hand, ArrayList<Card> player, BigDecimal original) {
		int status = 0;
		if(sum(player) > 21) {
			if(hand.getPlayerHands().size() > 1) {
				System.out.println("Busted... You lost this current hand.");
				bet = bet.subtract(original);
				//doesnt get original back in remaining
				status = 3;
			}
			
			else if(hand.getPlayerHands().size() == 1) {
				System.out.println("Busted... You lost this round.");
				status = 1;
			}
			hand.getPlayerHands().remove(player);
			
		}
		else if(sum(player) == 21) {
			if(hand.getPlayerHands().size() > 1) {
				System.out.println("BlackJack! for this hand");
				bet = bet.subtract(original);
				remaining = remaining.add(original);
				status = 3;
			}
			
			else if(hand.getPlayerHands().size() == 1) {
				System.out.println("BlackJack! for you.");
				BigDecimal back = bet.multiply(BigDecimal.valueOf(1.5));
				
				remaining = remaining.add(bet).add(back); //payout for blackjack
				status = 1;
			}
			
			hand.getPlayerHands().remove(player);
		}
		
		return status;
	}
}

package blackjack;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class StartGame {
	private static int currcard = 0;
	private static int playable;
	private static double bankroll = 0;
	private static double remaining = 0;
	private static double bet = 0;
	private static int didSplit = 0;
	private static boolean dealer_showing = false;
	static ArrayList <ArrayList<Card> > playerHands = new ArrayList<ArrayList<Card> >();

	public StartGame(){
		
	}
	
	public static void main(String[] args) {
		boolean start = false;
		ArrayList <Card> dealer = new ArrayList<Card>();
		ArrayList <Card> player = new ArrayList<Card>();
		
		ArrayList <Card> deck = new ArrayList<Card>();
		String[] suit = new String[] {"Spades", "Hearts", "Diamonds", "Clubs"};
		String[] rank = new String[]{"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
		
		Card cards = null;
		Shoe new_shoe = null;
		Scanner scan = new Scanner(System.in);
		
		while(start == false) {
			try {
				int shoe = 1;
				
				System.out.println("How many decks would you like to play with?");
				shoe = scan.nextInt();
				
				//create new shoe object (instance of class)
				if(shoe > 0) {
					playable = (shoe*52) / 2;
					new_shoe = new Shoe(shoe, playable);
					
					for(int i = 0; i < shoe; i++){
						for(int n = 0; n < suit.length; n++) { //for loop to set suits
							for(int j = 0; j < rank.length; j++) { //for loop to set ranks
								if(j >= 9) { 
									cards = new Card(rank[j], suit[n], 10); //value for 10/J/Q/K is 10
								}
								else if (j == 0) {
									cards = new Card(rank[j], suit[n], 11); //(ace default value=11)
								}
								else{
									cards = new Card(rank[j], suit[n], j+1); //value is the face card value 
								}
								deck.add(cards);
							}
						}
					}
					start = true;
				}
				else {
					//handle exception
					throw new NumberFormatException();
				}
				
			}catch(NumberFormatException io) {
				start = false;
			}
			
		}
		
		//shuffle deck
		//deal first 2 cards to player and dealer
		shuffle(deck);
		deal(deck, dealer, player); //dealing first 2 cards to dealing & 2 to player from shuffled deck

		boolean dealing_game = true;
		int statusP = 0;
		int statusD = 0;
		double insure_bet = 0;
		boolean hasAce = false;
		boolean asked = false;
		
		System.out.println("How much money do you want to put into your bankroll?");
		try {
			bankroll = scan.nextDouble();
		}catch(NumberFormatException io) {
			io.printStackTrace();
		}
		remaining = bankroll;
		bet = getBet();
		
		if(bet == -100) {
			//end of game 
			//terminate
			dealing_game = false;
		}
		else {
			remaining -= bet;
			System.out.println("Dealer's hand: " + dealer.get(0).toString());
			playerHands.add(player);
		}
		
		while(dealing_game) {
			if(currcard == new_shoe.getPlay()) {
				shuffle(deck);
			}
			
			if(asked == false) {
				try {
					if(dealer.get(0).getRank().equals("A")){ //dealer is currently showing an ace
						hasAce = true;
						System.out.println("Your hand: ");
						for(int i = 0; i < player.size(); i++) {
							System.out.println(player.get(i).toString());
						}
						System.out.println("Would you like Insurance? If so, how much? Type '0' for no insurance.");
						insure_bet = scan.nextDouble(); 
								
						while(insure_bet > bet/2) {
							System.out.println("Please enter insurance that is atmost half of your current bet which is $" + bet);
							insure_bet = scan.nextDouble(); //assuming at most half of current bet (no check here)
						}
						asked = true;
					}
				}catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
			for(int i = 0; i < playerHands.size(); i++) {
				statusP = hit_player(playerHands.get(i), deck, hasAce);
			}
			statusD = hit_dealer(dealer, player, deck, insure_bet);
			
			if((statusP == 1 && playerHands.size() == 1) || statusD == 1 && deck.size() > 0) { //still cards to continue playing game w same deck(s) 
				//and dealer or player busted or won 
				//starting another game
				dealing_game = redeal(deck, dealer, player);
				hasAce = false;
				//return;
			}
			
			if(deck.size() == 0) {
				//end of game/deck -> count cards
				int winP = sum(player);
				int winD = sum(dealer);
				
				if(winP > winD) {
					System.out.println("BlackJack! for Player");
					remaining += bet; //gets back current bet
				}
				else if (winD > winP) {
					System.out.println("BlackJack! for Dealer");
				}
				
				else {
					//tie game
					System.out.println("It's a tie!");
				}
				
				dealing_game = false;
			}
		}
		
		results(); //printing out money remaining in bankroll after deck officially ends
		didSplit = 0; //reset
		hasAce = false;
		playerHands.clear();
	}
	
	public static int hit_player(ArrayList<Card> player, ArrayList <Card> deck, boolean hasAce) {
		boolean hitP = true;
		Scanner scan = new Scanner(System.in);
		int status = 0;
		String response = null;
		String turn = null;
		
		while(hitP) {
			try {
				if(player.size() > 1) {
					System.out.println("Your current hand: ");
					for(int i = 0; i < player.size(); i++) {
						System.out.println(player.get(i).toString());
					}
				}
				
				if(player.size() == 1) { //the next splitted hand
					player.add(deck.get(currcard));
					deck.remove(currcard);
					currcard++;
					
					System.out.println("Your currrent hand: ");
					for(int i = 0; i < player.size(); i++) {
						System.out.println(player.get(i).toString());
					}
					
				}
				
				if(didSplit < 4 && hasAce == false && player.size() == 2 && player.get(0).getValue() == player.get(1).getValue()) { //cant split when dealer is showing an Ace
					System.out.println("Would you like to split?");
					turn = scan.nextLine();
					if(turn.equalsIgnoreCase("Yes")) { 
						
						split(player, deck);
						didSplit++;
						if(player.get(0).getRank().equals("A")) {
							if(playerTally(player) == 1) {
								status = 1;
							}
							
							else {
								status = 0;
							}
							hitP = false;	
							return status;
						}
						System.out.println("Your new current hand: ");
						for(int i = 0; i < player.size(); i++) {
							System.out.println(player.get(i).toString());
						}
						while(didSplit < 4 && player.get(0).getValue() == player.get(1).getValue()) {
							//show resplit hand hands
							System.out.println("Your hand: ");
							System.out.println(player.get(0).toString() + ", " + player.get(1).toString() );
							
							System.out.println("Would you like to Resplit?");
							turn = scan.nextLine();
							if(turn.equalsIgnoreCase("yes")) {
								split(player, deck);
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
				
				if(dealer_showing) { //only ask for suggestion when dealer has his first turn and gets his 17 soft hand
					System.out.println("Type 'S' for suggestion utility");
					turn = scan.nextLine();
					
					/*if(turn.equalsIgnoreCase("S")) {
						Suggestion sugg = new Suggestion(deck, player);
						System.out.println(sugg.toString());
					}*/
				}
				
				System.out.println("Would you like to Double Down?");
				turn = scan.nextLine();
				if(turn.equalsIgnoreCase("Yes")) {
					doubleDown();
					player.add(deck.get(currcard));
					deck.remove(currcard);
					currcard++;
					
					System.out.println("Your hand: ");
					for(int i = 0; i < player.size(); i++) {
						System.out.println(player.get(i).toString());
					}
					if(playerTally(player) == 1) {
						//hitP = false;
						status = 1;
					}
					hitP = false;
					return status;
				}
				
				System.out.println("Would you like to Surrender?");
				turn = scan.nextLine();
				if(turn.equalsIgnoreCase("Yes")) {
					surrender();
					System.out.println("You Surrendered.");
					hitP = false;
					status = 1;
					return status;
				}
				
				System.out.print("Hit or Stay?");
				response = scan.nextLine();
				if(response.equalsIgnoreCase("Hit")){
					player.add(deck.get(currcard));
					deck.remove(currcard);
					currcard++;
					
					System.out.println("Your hand: ");
					for(int i = 0; i < player.size(); i++) {
						System.out.println(player.get(i).toString());
					}
					
					if(playerTally(player) == 1) {
						hitP = false;
						status = 1;
					}
					else{
						//ask to hit or stay again
						hitP = true;
					}
				}
			
				else if(response.equalsIgnoreCase("Stay")) {
					//if stayed - now ask dealer to hit or stay
					if(playerTally(player) == 1) {
						status = 1;
					}
					
					else {
						status = 0;
					}
					hitP = false;		
				}
				else {
					throw new NoSuchElementException();
				}
			
			}catch(NoSuchElementException io) {
				hitP = true;
			}
		}
		
		return status;
	}
	
	public static int hit_dealer(ArrayList<Card> dealer, ArrayList<Card> player, ArrayList <Card> deck, double insure_bet) {
		int status = 0;
		
		if(dealer.get(0).getRank().equals("A")){ //dealer is currently showing an ace
			dealer.get(1).toString(); //showing hole card now
			if(dealer.get(1).getValue() == 10) {
				System.out.println("BlackJack! for Dealer");
				
				if(sum(player) == 21) {
					System.out.println("BlackJack! for Player also... It's a tie!");
					remaining += bet; //gets paid original bet back -> even money
				}
				//dealer collects bets and pays insurance wagers 2 to 1
				else {
					insurance(insure_bet); 
				}
				status = 1;
				return status;
			}
			else {
				//current hand continues as normal for p w/o insurance
				//p w/ insurance loses insurance bet & current hand continues
				if(insure_bet > 0) {
					remaining -= insure_bet;
				}
				if(sum(player) == 21) {
					System.out.println("BlackJack! for Player.");
					remaining += bet; //gets paid original bet back
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
					remaining += bet;
				}
				status = 1;
				return status;	
			}
		}
		int i;
		System.out.println("Dealer's hand:");
		for(i = 0; i < dealer.size(); i++) {
			System.out.println(dealer.get(i).toString());
		}

		dealer_showing = true;
		while(sum(dealer) < 17) {
			dealer.add(deck.get(currcard));
			deck.remove(currcard);
			currcard++;
			
			System.out.println(dealer.get(i).toString());
			i++;
			
			int sum = sum(dealer);
			if(sum > 21) {
				System.out.println("Dealer Busted... You won!");
				remaining += bet; //gets back its current bet
				status = 1;
			}
			else if(sum == 21) {
				System.out.println("BlackJack! for Dealer"); //already took out bet from remaining
				if(sum(player) == 21) {
					System.out.println("BlackJack! for Player also... It's a tie!");
					remaining += bet;
				}
				status = 1;
			}
			else if(sum >= 17 && sum < 21){
				status = 0; //still in game
			}
			
			
		}
				
		return status;
	}
	
	public static void shuffle(ArrayList<Card> deck){
	//shuffling deck of cards for each new round
		Random rand = new Random(); 
    
	    for (int i = deck.size() - 1; i > 0; i--) { 
	        // Random for remaining positions. 
	        int r = rand.nextInt(deck.size()-i); 
	          
	         //swapping the elements 
	         Card temp = deck.get(r); 
	         deck.set(r, deck.get(i)); 
	         deck.set(i, temp);
	    } 
	          
	}
	
	public static void deal(ArrayList<Card> deck, ArrayList<Card> dealer, ArrayList<Card> player) {
		//deals player first, then dealer, then player, then dealer
		player.add(deck.get(currcard));
		deck.remove(currcard);
		currcard++;
		
		dealer.add(deck.get(currcard));
		deck.remove(currcard);
		currcard++;
		
		player.add(deck.get(currcard));
		deck.remove(currcard);
		currcard++;
			
		dealer.add(deck.get(currcard));
		deck.remove(currcard);
		currcard++;
	}
	
	public static boolean redeal(ArrayList <Card> deck, ArrayList<Card> dealer, ArrayList<Card> player) {
		didSplit = 0; //resetting
		shuffle(deck); //shuffling remaining cards in deck
		
		//clear dealer and players hands
		didSplit = 0; //reset
		dealer.clear();
		player.clear();
		playerHands.clear();
		
		bet = getBet();
		if(bet == -100) {
			//end of game 
			//terminate
			return false;
		}
		remaining -= bet;
		deal(deck, dealer, player); //dealing first 2 cards to dealing & 2 to player from shuffled deck
		playerHands.add(player);
		
		System.out.println("Dealer's hand: " + dealer.get(0).toString());
		return true;
	}
	
	public static int sum(ArrayList <Card> hand) {
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
	
	public static double getBet() {
		Scanner scan = new Scanner(System.in);
		double bet = 0;
		
		if(remaining < 10) {
			System.out.println("You have " + remaining + "... and cannot make a full bet (bet > 10).");
			bet = -100;
			return bet; 
		}
		
		System.out.println("How much money do you want to bet this game? (Bet must be atleast 10)" + "\n" + "Remaining: " + remaining);
		bet = scan.nextDouble();
		
		while(bet > remaining && remaining > 10) {
			System.out.println("You do not have enough money to bet that amount.");
			System.out.println("How much money do you want to bet this game?" + "\n" + "Remaining: " + remaining);
			bet = scan.nextDouble();
		}
		
		while(bet < 10) {
			System.out.println("Your bet must be atleast 10");
			System.out.println("How much money do you want to bet this game?" + "\n" + "Remaining: " + remaining);
			bet = scan.nextDouble();
		}
		
		return bet;
	}
	
	public static void doubleDown() {
		remaining -= bet; //taking away bet*2 but original bet is already accounted for
		bet = bet*2;
	}
	
	public static void surrender() {
		bet = bet/2;
		remaining += bet; //loses half its bet for that round
	}
	
	public static void insurance(double insure_bet) {
		if(insure_bet > 0) {
			remaining += (2*insure_bet); //add to remaining -> won money back but original bet is still taken away (as already done)
		}
		
		//else: loses original bet also -> but nothing won back so remaining stays the same then
	}
	
	public static void split(ArrayList<Card> player, ArrayList<Card> deck) {
		ArrayList <Card> split = new ArrayList<Card>();
		
		split.add(player.get(1));
		player.remove(1);
		
		player.add(deck.get(currcard));
		deck.remove(currcard);
		currcard++;
		
		if(player.get(0).getRank().equals("A")) {
			split.add(deck.get(currcard));
			deck.remove(currcard);
			currcard++;
		}
		remaining -= bet; //take out from remaining
		bet += bet; //doubling original bet -> represents bet for new split hand
		playerHands.add(split);
	}

	public static String results() {
		String results = null;
		if(remaining < 0) {
			results = "You owe " + remaining + " dollars.";
		}
		
		else if (remaining > 0) {
			results = "You've have " + remaining + " dollars!";
		}
		
		else {
			results = "You have 0 dollars left.";
		}
		
		return results;
	}
	
	public static int playerTally(ArrayList<Card> player) {
		int status = 0;
		if(sum(player) > 21) {
			if(playerHands.size() > 1) {
				System.out.println("Busted... You lost this hand.");
				bet = (bet/2);
			}
			else{
				System.out.println("Busted... You lost!");
			}
			playerHands.remove(player);
			
			if(playerHands.size() < 1) {
				status = 1;
			}
		}
		else if(sum(player) == 21) {
			System.out.println("BlackJack! for You");
			playerHands.remove(player);
			
			if(playerHands.size() < 1) {
				status = 1;
			}
			
			if(playerHands.size() > 1) {
				bet -= (bet/2);
				//remaining += 
			}
		}
		
		return status;
	}
}

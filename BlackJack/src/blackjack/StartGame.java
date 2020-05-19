package blackjack;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class StartGame {
	static int currcard = 0;
	
	public static void main(String[] args) {
		//once there is user input
		boolean start = false;
		 //creating deck of cards
		ArrayList <Card> dealer = new ArrayList<Card>();
		ArrayList <Card> player = new ArrayList<Card>();
		
		ArrayList <Card> deck = new ArrayList<Card>();
		String[] suit = new String[] {"Spades", "Hearts", "Diamonds", "Clubs"};
		Card cards;
		
		while(start == false) {
			try {
				int shoe = 1;
				Scanner scan = new Scanner(System.in);
				
				System.out.println("How many decks would you like to play with?");
				shoe = scan.nextInt();
				
				if(shoe > 0) {
					for(int i = 0; i < shoe; i++){
						for(int n = 0; n < suit.length; n++) { //for loop to set suits
							for(int j = 1; j <= 13; j++) { //for loop to set ranks
								if(j >= 10) { //value for J/Q/K is 10
									cards = new Card(j, suit[n], 10);
								}
								else{
									cards = new Card(j, suit[n], j); //value is the face card value (ace default value=1)
								}
								deck.add(cards);
							}
						}
					}
					start = true;
					//scan.close();
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
		int status = 0;
		
		while(dealing_game) {
			if(hit_player(player, deck) == 1) {
				dealing_game = false;
				return;
				//end game
			}
			
			if(hit_dealer(dealer, deck) == 1) {
				dealing_game = false;
				return;
				//end game
			}
			
			if(deck.size() == 0) {
				//end of deck -> count cards
				int winP = sum(player);
				int winD = sum(dealer);
				
				if(winP > winD) {
					System.out.println("BlackJack! for Player");
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
		
	}
	
	public static int hit_player(ArrayList<Card> player, ArrayList <Card> deck) {
		boolean hitP = true;
		Scanner scan = new Scanner(System.in);
		int status = 0;
		String response = null;
		
		while(hitP) {
			try {
				System.out.println("Your hand: ");
				for(int i = 0; i < player.size(); i++) {
					System.out.println(player.get(i).showCard());
				}
				System.out.print("Hit or Stay? ");
				response = scan.nextLine();
				
				if(response.equalsIgnoreCase("Hit")){
					player.add(deck.get(currcard));
					deck.remove(currcard);
					currcard++;
					
					int sum = sum(player);
					if(sum > 21) {
						//busted
						hitP = false;
						System.out.println("Busted... You Lost!");
						status = 1; 
					}
					else if(sum == 21) {
						hitP = false;
						System.out.println("BlackJack! for You");
						status = 1;
					}
					else{
						//ask to hit or stay again
						hitP = true;
					}
				}
			
				else if(response.equalsIgnoreCase("Stay")) {
					//if stayed - now ask dealer to hit or stay
					hitP = false;	
					status = 0;
				}
				else {
					throw new NoSuchElementException();
				}
			
			}catch(NoSuchElementException io) {
				hitP = true;
			}
		}
		
		return status; //return back to main()
	}
	
	public static int hit_dealer(ArrayList<Card> dealer, ArrayList <Card> deck) {
		boolean hitD = true;
		Scanner scan = new Scanner(System.in);
		int status = 0;
		String response = null;
		
		while(hitD) {
			try {
				System.out.println("Dealer's hand: ");
				for(int i = 0; i < dealer.size(); i++) {
					System.out.println(dealer.get(i).showCard());
				}
				System.out.println("Hit or Stay? ");
				response = scan.nextLine();
				
				if(response.equalsIgnoreCase("Hit")){
					dealer.add(deck.get(currcard));
					deck.remove(currcard);
					currcard++;
					
					int sum = sum(dealer);
					if(sum > 21) {
						//busted
						hitD = false;
						System.out.println("Dealer Busted... You won!");
						status = 1;
					}
					else if(sum == 21) {
						hitD = false;
						System.out.println("BlackJack! for Dealer");
						status = 1;
					}
					else{
						//ask to hit or stay again
						hitD = true;
					}
				}
			
				else if(response.equalsIgnoreCase("Stay")) {
					hitD = false;	
					status = 0; //return back to main()
				}
				else {
					throw new NoSuchElementException();
				}
			
			}catch(NoSuchElementException io) {
				hitD = true;
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
			dealer.add(deck.get(currcard));
			deck.remove(currcard);
			currcard++;
			
			dealer.add(deck.get(currcard));
			deck.remove(currcard);
			currcard++;
			
			player.add(deck.get(currcard));
			deck.remove(currcard);
			currcard++;
			
			player.add(deck.get(currcard));
			deck.remove(currcard);
			currcard++;
	}
	
	public static int sum(ArrayList <Card> hand) {
		int count = 0;
		for(int i = 0; i < hand.size(); i++) {
			count += hand.get(i).getValue(); 
			//need to do Ace (1 or 11) edge case -> by default, ace is 1
		}
		return count;
	}
}

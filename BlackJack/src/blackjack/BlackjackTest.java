package blackjack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
//import org.kutsuki.akanana.organic.OrganicSearch;
//import org.kutsuki.akanana.shoe.AkaNanaShoe;

public class BlackjackTest {
    private static final BigDecimal BET = BigDecimal.TEN;
    private static final int DECKS = 2;
    private static final int PLAYABLE = 40 * DECKS;

    @Test
    public void testForcedActions() {
	StartGame os = new StartGame();
	// os.setStartingBet(BET);

	// Forced Stand Player Win by dealer bust
	os.setShoe(DECKS, PLAYABLE, 10, 10, 3, 4, 10);
	os.setBankroll(BigDecimal.ZERO);
	os.makeBet(BigDecimal.TEN);
	//os.distributeCards();
	
	os.run();
	os.playerAction(Action.STAND);
	os.startGame();
	
	testGameplay(os.getPlayerHands().get(0), 2, false, false, false, false, false, false);
	testGameplay(os.getDealerHand(), 3, false, true, false, false, false, false);
	os.clearHands();
	assertEquals("Wrong Payout", BigDecimal.TEN, os.getBankroll());

	// Forced Stand Dealer Win by higher card
	os.setShoe(DECKS, PLAYABLE, 10, 10, 3, 9);
	os.setBankroll(BigDecimal.ZERO);
	os.makeBet(BigDecimal.TEN);
	
	os.run();
	//os.distributeCards();
	os.playerAction(Action.STAND);
	os.startGame();
	
	testGameplay(os.getPlayerHands().get(0), 2, false, false, false, false, false, false);
	testGameplay(os.getDealerHand(), 2, false, false, false, false, false, false);
	os.clearHands();
	assertEquals("Wrong Payout", BigDecimal.TEN.negate(), os.getBankroll());

	// Forced Stand Push
	os.setShoe(DECKS, PLAYABLE, 14, 10, 7, 8);
	os.setBankroll(BigDecimal.ZERO);
	os.makeBet(BigDecimal.TEN);
	
	os.run();
	//os.distributeCards();
	os.playerAction(Action.STAND);
	os.startGame();
	
	testGameplay(os.getPlayerHands().get(0), 2, false, false, false, false, false, false);
	testGameplay(os.getDealerHand(), 2, false, false, false, false, false, false);
	os.clearHands();
	assertEquals("Wrong Payout", BigDecimal.ZERO, os.getBankroll());

	// Forced Stand Dealer Blackjack
	os.setShoe(DECKS, PLAYABLE, 10, 14, 3, 10);
	os.setBankroll(BigDecimal.ZERO);
	os.makeBet(BigDecimal.TEN);
	
	os.run();
	//os.distributeCards();
	os.playerAction(Action.STAND);
	os.startGame();
	
	testGameplay(os.getPlayerHands().get(0), 2, false, false, false, false, false, false);
	testGameplay(os.getDealerHand(), 2, true, false, false, false, false, false);
	os.clearHands();
	assertEquals("Wrong Payout", BigDecimal.TEN.negate(), os.getBankroll());

	// Forced Hit Player Win by higher card
	os.setShoe(DECKS, PLAYABLE, 10, 10, 9, 9, 2);
	os.setBankroll(BigDecimal.ZERO);
	os.makeBet(BigDecimal.TEN);
	
	os.run();
	//os.distributeCards();
	os.playerAction(Action.HIT);
	os.startGame();
	
	testGameplay(os.getPlayerHands().get(0), 3, false, false, false, false, false, false);
	testGameplay(os.getDealerHand(), 2, false, false, false, false, false, false);
	os.clearHands();
	assertEquals("Wrong Payout", BigDecimal.TEN, os.getBankroll());

	// Forced Hit Dealer Win by Player Bust
	os.setShoe(DECKS, PLAYABLE, 10, 10, 9, 9, 5);
	os.setBankroll(BigDecimal.ZERO);
	os.makeBet(BigDecimal.TEN);
	
	os.run();
	//os.distributeCards();
	os.playerAction(Action.HIT);
	os.startGame();
	
	testGameplay(os.getPlayerHands().get(0), 3, false, true, false, false, false, false);
	testGameplay(os.getDealerHand(), 2, false, false, false, false, false, false);
	os.clearHands();
	assertEquals("Wrong Payout", BigDecimal.TEN.negate(), os.getBankroll());

	// Forced Hit Push
	os.setShoe(DECKS, PLAYABLE, 10, 10, 7, 9, 2);
	os.setBankroll(BigDecimal.ZERO);
	os.makeBet(BigDecimal.TEN);
	
	os.run();
	//os.distributeCards();
	os.playerAction(Action.HIT);
	os.startGame();
	
	testGameplay(os.getPlayerHands().get(0), 3, false, false, false, false, false, false);
	testGameplay(os.getDealerHand(), 2, false, false, false, false, false, false);
	os.clearHands();
	assertEquals("Wrong Payout", BigDecimal.ZERO, os.getBankroll());

	// Forced Hit Dealer Blackjack
	os.setShoe(DECKS, PLAYABLE, 10, 14, 3, 10, 3);
	os.setBankroll(BigDecimal.ZERO);
	os.makeBet(BigDecimal.TEN);
	
	os.run();
	//os.distributeCards();
	os.playerAction(Action.HIT);
	os.startGame();
	
	testGameplay(os.getPlayerHands().get(0), 3, false, false, false, false, false, false);
	testGameplay(os.getDealerHand(), 2, true, false, false, false, false, false);
	os.clearHands();
	assertEquals("Wrong Payout", BigDecimal.TEN.negate(), os.getBankroll());

	// Forced Surrender
	os.setShoe(DECKS, PLAYABLE, 10, 10, 9, 9);
	os.setBankroll(BigDecimal.ZERO);
	os.makeBet(BigDecimal.TEN);
	
	os.run();
	//os.distributeCards();
	os.playerAction(Action.SURRENDER);
	os.startGame();
	
	testGameplay(os.getPlayerHands().get(0), 2, false, false, false, false, false, true);
	testGameplay(os.getDealerHand(), 2, false, false, false, false, false, false);
	os.clearHands();
	assertEquals("Wrong Payout", BigDecimal.valueOf(5).negate(), os.getBankroll());

	// Forced Surrender Dealer Blackjack
	os.setShoe(DECKS, PLAYABLE, 10, 14, 6, 10);
	os.setBankroll(BigDecimal.ZERO);
	os.makeBet(BigDecimal.TEN);
	
	os.run();
	//os.distributeCards();
	os.playerAction(Action.SURRENDER);
	os.startGame();
	
	testGameplay(os.getPlayerHands().get(0), 2, false, false, false, false, false, false);
	testGameplay(os.getDealerHand(), 2, true, false, false, false, false, false);
	os.clearHands();
	assertEquals("Wrong Payout", BigDecimal.TEN.negate(), os.getBankroll());

	// Forced Double Down Player Win by higher card
	os.setShoe(DECKS, PLAYABLE, 10, 10, 9, 9, 2);
	os.setBankroll(BigDecimal.ZERO);
	os.makeBet(BigDecimal.TEN);
	
	os.run();
	//os.distributeCards();
	os.playerAction(Action.DOUBLE);
	os.startGame();
	
	testGameplay(os.getPlayerHands().get(0), 3, false, false, true, false, false, false);
	testGameplay(os.getDealerHand(), 2, false, false, false, false, false, false);
	os.clearHands();
	assertEquals("Wrong Payout", BigDecimal.valueOf(20), os.getBankroll());

	// Forced Double Down Dealer Win by Higher Card
	os.setShoe(DECKS, PLAYABLE, 10, 10, 7, 5, 2, 5);
	os.setBankroll(BigDecimal.ZERO);
	os.makeBet(BigDecimal.TEN);
	
	os.run();
	//os.distributeCards();
	os.playerAction(Action.DOUBLE);
	os.startGame();
	
	testGameplay(os.getPlayerHands().get(0), 3, false, false, true, false, false, false);
	testGameplay(os.getDealerHand(), 3, false, false, false, false, false, false);
	os.clearHands();
	assertEquals("Wrong Payout", BigDecimal.valueOf(-20), os.getBankroll());

	// Forced Double Down Dealer Win by Player Bust
	os.setShoe(DECKS, PLAYABLE, 10, 10, 9, 9, 6);
	os.setBankroll(BigDecimal.ZERO);
	os.makeBet(BigDecimal.TEN);
	
	os.run();
	//os.distributeCards();
	os.playerAction(Action.DOUBLE);
	os.startGame();
	
	testGameplay(os.getPlayerHands().get(0), 3, false, true, true, false, false, false);
	testGameplay(os.getDealerHand(), 2, false, false, false, false, false, false);
	os.clearHands();
	assertEquals("Wrong Payout", BigDecimal.valueOf(-20), os.getBankroll());

	// Forced Double Down Push
	os.setShoe(DECKS, PLAYABLE, 10, 10, 7, 9, 2);
	os.setBankroll(BigDecimal.ZERO);
	os.makeBet(BigDecimal.TEN);
	
	os.run();
	//os.distributeCards();
	os.playerAction(Action.DOUBLE);
	os.startGame();
	
	testGameplay(os.getPlayerHands().get(0), 3, false, false, true, false, false, false);
	testGameplay(os.getDealerHand(), 2, false, false, false, false, false, false);
	os.clearHands();
	assertEquals("Wrong Payout", BigDecimal.ZERO, os.getBankroll());

	// Forced Double Down Dealer Blackjack
	os.setShoe(DECKS, PLAYABLE, 9, 14, 2, 10, 3);
	os.setBankroll(BigDecimal.ZERO);
	os.makeBet(BigDecimal.TEN);
	os.run();
	//os.distributeCards();
	os.playerAction(Action.DOUBLE);
	os.startGame();
	
	testGameplay(os.getPlayerHands().get(0), 3, false, false, true, false, false, false);
	testGameplay(os.getDealerHand(), 2, true, false, false, false, false, false);
	os.clearHands();
	assertEquals("Wrong Payout", BigDecimal.TEN.negate(), os.getBankroll());

	// Forced Split Player Win by Higher Card with a Double Down
	os.setShoe(DECKS, PLAYABLE, 2, 10, 2, 9, 9, 10, 6, 4, 8);
	os.setBankroll(BigDecimal.ZERO);
	os.makeBet(BigDecimal.TEN);
	os.run();
	//os.distributeCards();
	os.playerAction(Action.SPLIT);
	os.startGame();
	
	testGameplay(os.getPlayerHands().get(0), 3, false, false, true, false, true, false);
	testGameplay(os.getPlayerHands().get(1), 4, false, false, false, false, true, false);
	testGameplay(os.getDealerHand(), 2, false, false, false, false, false, false);
	os.clearHands();
	assertEquals("Wrong Payout", BigDecimal.valueOf(30), os.getBankroll());

	// Forced Split Dealer Win by Higher Card and Player Bust
	os.setShoe(DECKS, PLAYABLE, 10, 10, 10, 9, 3, 4, 4, 10);
	os.setBankroll(BigDecimal.ZERO);
	os.makeBet(BigDecimal.TEN);
	os.run();
	//os.distributeCards();
	os.playerAction(Action.SPLIT);
	os.startGame();
	
	testGameplay(os.getPlayerHands().get(0), 3, false, false, false, false, true, false);
	testGameplay(os.getPlayerHands().get(1), 3, false, true, false, false, true, false);
	testGameplay(os.getDealerHand(), 2, false, false, false, false, false, false);
	os.clearHands();
	assertEquals("Wrong Payout", BigDecimal.valueOf(-20), os.getBankroll());

	// Forced Resplits Player Win by Higher Card, one loss
	os.setShoe(DECKS, PLAYABLE, 10, 10, 10, 8, 10, 9, 7, 10);
	os.setBankroll(BigDecimal.ZERO);
	os.makeBet(BigDecimal.TEN);
	os.run();
	//os.distributeCards();
	os.playerAction(Action.SPLIT);
	os.startGame();

	testGameplay(os.getPlayerHands().get(0), 2, false, false, false, false, true, false);
	testGameplay(os.getPlayerHands().get(1), 2, false, false, false, false, true, false);
	testGameplay(os.getPlayerHands().get(2), 2, false, false, false, false, true, false);
	testGameplay(os.getDealerHand(), 2, false, false, false, false, false, false);
	os.clearHands();
	assertEquals("Wrong Payout", BigDecimal.valueOf(20), os.getBankroll());
   }
    
    // testGameplay blackjack, bust, doubleDown, insurance, split, surrender
    private void testGameplay(Hand hand, int expectedSize, boolean expectedBlackjack, boolean expectedBust,
	    boolean expectedDoubleDown, boolean expectedInsurance, boolean expectedSplit, boolean expectedSurrender) {
	assertEquals("Wrong number of Cards " + hand, expectedSize, hand.getHand().size());
	assertEquals("Wrong Blackjack " + hand, expectedBlackjack, hand.isBlackjack());
	assertEquals("Wrong Bust " + hand, expectedBust, hand.isBust());
	assertEquals("Wrong Double Down " + hand, expectedDoubleDown, hand.isDoubleDown());
	assertEquals("Wrong Insurance " + hand, expectedInsurance, hand.isInsurance());
	assertEquals("Wrong Split " + hand, expectedSplit, hand.isSplit());
	assertEquals("Wrong Surrender " + hand, expectedSurrender, hand.isSurrender());
    }

    private boolean isBlackjack(int rank1, int rank2) {
	return (rank1 == 11 && rank2 == 10) || (rank1 == 10 && rank2 == 11);
    }
}
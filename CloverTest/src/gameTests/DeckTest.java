package gameTests;

import game.Card;
import game.Deck;

import java.util.ArrayList;

import android.test.AndroidTestCase;

public class DeckTest extends AndroidTestCase {

	public void tearDown() throws Exception {
	    ///CLOVER:FLUSH
	    super.tearDown();
	}
	
	public void testConstructor() {
		Deck deck = new Deck();
		ArrayList<Card> restDeck = new ArrayList<Card>();
		//Check for duplicates
		for(int i=0; i<52; i++) {
			Card tmpCard = deck.getCard();
			for(int j=0; j<restDeck.size(); j++) {
				assertNotSame(tmpCard, restDeck.get(j));
			}
			restDeck.add(tmpCard);
		}
		
		//Can assert error? make sure the deck is empty?
	}
	
	public void testResetDeck() {
		Deck deck = new Deck();
		for(int i=0; i<52; i++) {
			deck.getCard();
		}
		deck.resetDeck();
		assertNotNull(deck.getCard());
	}

}

package game;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class CardTest {

	@Test
	public void testConstructor() {
		Card card = new Card(3, 2);
		
		assertTrue(card.getRank()==3);
		assertTrue(card.getSuit()==2);
	}
	
	@Test
	public void testGetterSetters() {
		Card card = new Card(0, 0);
		
		card.setRank(1);
		assertTrue(card.getRank()==1);
		
		card.setSuit(2);
		assertTrue(card.getSuit()==2);
	}

	@Test
	public void testComparator() {
		Card card1 = new Card(0, 6);
		Card card2 = new Card(3, 1);
		Card card3 = new Card(2, 7);
		
		Card[] cards = {card1, card2, card3};
		
		Arrays.sort(cards, new rankComparator());
		
		assertEquals(cards[0], card2);
		assertEquals(cards[1], card1);
		assertEquals(cards[2], card3);
		
		Arrays.sort(cards, new suitComparator());
		
		assertEquals(cards[0], card1);
		assertEquals(cards[1], card3);
		assertEquals(cards[2], card2);
	}
}

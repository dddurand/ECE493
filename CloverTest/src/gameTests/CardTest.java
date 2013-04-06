package gameTests;

import java.util.Arrays;

import game.Card;
import game.Card.RankComparator;
import game.Card.SuitComparator;
import android.test.AndroidTestCase;

public class CardTest extends AndroidTestCase {

	public void tearDown() throws Exception {
	    ///CLOVER:FLUSH
	    super.tearDown();
	}
	
	public void testConstructor() {
		Card card = new Card(3, 2);
		
		assertTrue(card.getRank()==3);
		assertTrue(card.getSuit()==2);
	}


	public void testGetterSetters() {
		Card card = new Card(0, 0);
		
		card.setRank(1);
		assertTrue(card.getRank()==1);
		
		card.setSuit(2);
		assertTrue(card.getSuit()==2);
	}

	public void testComparator() {
		Card card1 = new Card(6, 0);
		Card card2 = new Card(1, 3);
		Card card3 = new Card(7, 2);
		
		Card[] cards = {card1, card2, card3};
		
		Arrays.sort(cards, new Card.SuitComparator());
		
		
		assertTrue(cards[0].getRank()==card1.getRank() && cards[0].getSuit() ==card1.getSuit());
		assertTrue(cards[1].getRank()== card3.getRank() && cards[1].getSuit()==card3.getSuit());
		assertTrue(cards[2].getRank()== card2.getRank() && cards[2].getSuit()==card2.getSuit());
		
		Arrays.sort(cards, new Card.RankComparator());
		
		assertTrue(cards[0].getRank()==card2.getRank() && cards[0].getSuit() ==card2.getSuit());
		assertTrue(cards[1].getRank()==card1.getRank() && cards[1].getSuit() ==card1.getSuit());
		assertTrue(cards[2].getRank()==card3.getRank() && cards[2].getSuit() ==card3.getSuit());
		
		try{
			SuitComparator SC = new Card.SuitComparator();
			Integer int1 = 1;
			Integer int2 = 2;
			SC.compare(int1, int2);
			fail();
		} catch (ClassCastException e) {
			
		} catch (Exception e) {
			fail();
		}
		
		try{
			RankComparator RC = new Card.RankComparator();
			Integer int1 = 1;
			Integer int2 = 2;
			RC.compare(int1, int2);
			fail();
		} catch (ClassCastException e) {
			
		} catch (Exception e) {
			fail();
		}
	}
	
	public void testToString() {
		Card card1 = new Card(6, 0);
		Card card2 = new Card(1, 3);
		Card card3 = new Card(7, 2);
		
		Card[] cards = {card1, card2, card3};
		
		String string1 = card1.toString();
		String string2 = Card.getCardsString(cards);
		assertTrue(string1.equals("D8"));
		assertTrue(string2.equals("D8;S3;H9"));
	}
}

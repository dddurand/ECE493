package dataModels;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import dataModels.Card.Number;
import dataModels.Card.Suit;

public class CardTest {

	@Test
	public void testConstructor()
	{
		Card card = new Card(Card.Suit.CLUBS, Card.Number.ACE);
		assertTrue(card.getNumber() == Card.Number.ACE);
		assertTrue(card.getSuit() == Card.Suit.CLUBS);
	}
	
	@Test
	public void testStringConstructor()
	{
		Card card = new Card(null);
		assertTrue(card.getNumber() == Card.Number.UNKNOWN);
		assertTrue(card.getSuit() == Card.Suit.UNKNOWN);
		
		card = new Card("BALH");
		assertTrue(card.getNumber() == Card.Number.UNKNOWN);
		assertTrue(card.getSuit() == Card.Suit.UNKNOWN);
		
		card = new Card("HN");
		assertTrue(card.getNumber() == Card.Number.UNKNOWN);
		assertTrue(card.getSuit() == Card.Suit.UNKNOWN);
		
		card = new Card("X3");
		assertTrue(card.getNumber() == Card.Number.UNKNOWN);
		assertTrue(card.getSuit() == Card.Suit.UNKNOWN);
		
		card = new Card("H3");
		assertTrue(card.getNumber() == Card.Number.THREE);
		assertTrue(card.getSuit() == Card.Suit.HEARTS);
		
	}
	
	@Test
	public void testGenerateCards()
	{
		ArrayList<Card> cards = Card.generateCards("H3;CA");
		assertTrue(cards.get(0).getNumber() == Number.THREE);
		assertTrue(cards.get(0).getSuit() == Suit.HEARTS);
		
		assertTrue(cards.get(1).getNumber() == Number.ACE);
		assertTrue(cards.get(1).getSuit() == Suit.CLUBS);
		
		cards = Card.generateCards("");
		assertTrue(cards.size()==0);
		
	}
	
	@Test
	public void testGetSuit()
	{
		Card card = new Card(Suit.CLUBS, Number.ACE);
		assertTrue(card.getSuit() == Suit.CLUBS);
		
	}
	
	@Test
	public void testSetSuit()
	{
		Card card = new Card(Suit.CLUBS, Number.ACE);
		card.setSuit(Suit.HEARTS);
		
		assertTrue(card.getSuit() == Suit.HEARTS);
	}
	
	@Test
	public void testGetNumber()
	{
		Card card = new Card(Suit.CLUBS, Number.ACE);
		assertTrue(card.getNumber() == Number.ACE);
	}
	
	@Test
	public void testSetNumber()
	{
		Card card = new Card(Suit.CLUBS, Number.ACE);
		card.setNumber(Number.SEVEN);
		assertTrue(card.getNumber() == Number.SEVEN);
	}
	
	@Test
	public void testGetCardAsString()
	{
		Card card = new Card(Suit.CLUBS, Number.ACE);
		Card card2 = new Card(Suit.DIAMONDS, Number.QUEEN);
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(card);
		
		String cardString = Card.getCardsAsString(cards);
		
		assertTrue(cardString.equals("CA"));
		
		cards.add(card2);
		
		cardString = Card.getCardsAsString(cards);
		
		assertTrue(cardString.equals("CA;DQ"));
		
	}
	
	@Test
	public void testValidCardString()
	{
		assertTrue(Card.validCardString("CA"));
		assertTrue(Card.validCardString("CA;DQ"));
		assertFalse(Card.validCardString("CA;asdf"));
		assertFalse(Card.validCardString("asdf;DQ"));
		assertFalse(Card.validCardString("asdf"));
		assertFalse(Card.validCardString("a"));
		assertFalse(Card.validCardString(null));
	}

	
	@Test
	public void testToString()
	{
		Card card = new Card(Suit.CLUBS, Number.ACE);
		assertTrue(card.toString().equals("CA"));
	}
	
	@Test
	public void testNumber()
	{
		Number number = Number.ACE;
		assertTrue(number.getValue()=='A');
		assertTrue(number.getStringValue().equals("A"));	
		assertTrue(Number.ACE == Number.getNumber('A'));
		assertTrue(Number.UNKNOWN == Number.getNumber('X'));
	}
	
	@Test
	public void testSuit()
	{
		Suit suit = Suit.CLUBS;
		assertTrue(suit.getValue()=='C');
		assertTrue(suit.getStringValue().equals("C"));	
		assertTrue(Suit.CLUBS == Suit.getSuit('C'));
	}
	
}

package dataModels;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import dataModels.Card.Number;
import dataModels.Card.Suit;
import dataModels.GameAction.PokerAction;

public class GameActionTest {

	@Test
	public void emptyConstructor() {
		@SuppressWarnings("unused")
		GameAction gameAction = new GameAction();
		assertTrue(true);
	}

	@Test
	public void testConstructor() {
		PokerAction action = PokerAction.BET;
		int pot = 5;
		int bet = 3;
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card(Suit.CLUBS, Number.ACE));
		hand.add(new Card(Suit.CLUBS, Number.KING));
		
		ArrayList<Card> comm = new ArrayList<Card>();
		comm.add(new Card(Suit.CLUBS, Number.ACE));
		comm.add(new Card(Suit.CLUBS, Number.KING));
		comm.add(new Card(Suit.CLUBS, Number.QUEEN));
		
		GameAction gameAction = new GameAction(action, pot, bet, hand, comm);
		
		assertTrue(gameAction.getAction() == action);
		assertTrue(gameAction.getBet() == bet);
		assertTrue(gameAction.getPot() == pot);
		assertTrue(gameAction.getHand().equals(hand));
		assertTrue(gameAction.getCommunityCards().equals(comm));
	}
	
	@Test
	public void testSetBet()
	{
		PokerAction action = PokerAction.BET;
		int pot = 5;
		int bet = 3;
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card(Suit.CLUBS, Number.ACE));
		hand.add(new Card(Suit.CLUBS, Number.KING));
		
		ArrayList<Card> comm = new ArrayList<Card>();
		comm.add(new Card(Suit.CLUBS, Number.ACE));
		comm.add(new Card(Suit.CLUBS, Number.KING));
		comm.add(new Card(Suit.CLUBS, Number.QUEEN));
		
		GameAction gameAction = new GameAction(action, pot, bet, hand, comm);
		gameAction.setBet(43);
		
		assertTrue(gameAction.getAction() == action);
		assertTrue(gameAction.getBet() == 43);
		assertTrue(gameAction.getHand().equals(hand));
		assertTrue(gameAction.getCommunityCards().equals(comm));
	}
	
	@Test
	public void testSetAction()
	{
		PokerAction action = PokerAction.BET;
		int pot = 5;
		int bet = 3;
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card(Suit.CLUBS, Number.ACE));
		hand.add(new Card(Suit.CLUBS, Number.KING));
		
		ArrayList<Card> comm = new ArrayList<Card>();
		comm.add(new Card(Suit.CLUBS, Number.ACE));
		comm.add(new Card(Suit.CLUBS, Number.KING));
		comm.add(new Card(Suit.CLUBS, Number.QUEEN));
		
		GameAction gameAction = new GameAction(action, pot, bet, hand, comm);
		gameAction.setAction(PokerAction.END);
		
		assertTrue(gameAction.getAction() == PokerAction.END);
		assertTrue(gameAction.getBet() == bet);
		assertTrue(gameAction.getHand().equals(hand));
		assertTrue(gameAction.getCommunityCards().equals(comm));
	}
	
	@Test
	public void testSetPot()
	{
		PokerAction action = PokerAction.BET;
		int pot = 5;
		int bet = 3;
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card(Suit.CLUBS, Number.ACE));
		hand.add(new Card(Suit.CLUBS, Number.KING));
		
		ArrayList<Card> comm = new ArrayList<Card>();
		comm.add(new Card(Suit.CLUBS, Number.ACE));
		comm.add(new Card(Suit.CLUBS, Number.KING));
		comm.add(new Card(Suit.CLUBS, Number.QUEEN));
		
		GameAction gameAction = new GameAction(action, pot, bet, hand, comm);
		gameAction.setPot(43);
		
		assertTrue(gameAction.getPot() == 43);
		assertTrue(gameAction.getAction() == action);
		assertTrue(gameAction.getBet() == bet);
		assertTrue(gameAction.getHand().equals(hand));
		assertTrue(gameAction.getCommunityCards().equals(comm));
	}
	
	@Test
	public void testSetHand()
	{
		PokerAction action = PokerAction.BET;
		int pot = 5;
		int bet = 3;
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card(Suit.CLUBS, Number.ACE));
		hand.add(new Card(Suit.CLUBS, Number.KING));
		
		ArrayList<Card> comm = new ArrayList<Card>();
		comm.add(new Card(Suit.CLUBS, Number.ACE));
		comm.add(new Card(Suit.CLUBS, Number.KING));
		comm.add(new Card(Suit.CLUBS, Number.QUEEN));
		
		ArrayList<Card> hand2 = new ArrayList<Card>();
		hand2.add(new Card(Suit.DIAMONDS, Number.FIVE));
		hand2.add(new Card(Suit.SPADES, Number.SIX));
		
		GameAction gameAction = new GameAction(action, pot, bet, hand, comm);
		gameAction.setHand(hand2);
		
		assertTrue(gameAction.getPot() == pot);
		assertTrue(gameAction.getAction() == action);
		assertTrue(gameAction.getBet() == bet);
		assertTrue(gameAction.getHand().equals(hand2));
		assertTrue(gameAction.getCommunityCards().equals(comm));
	}
	
	@Test
	public void testSetComm()
	{
		PokerAction action = PokerAction.BET;
		int pot = 5;
		int bet = 3;
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card(Suit.CLUBS, Number.ACE));
		hand.add(new Card(Suit.CLUBS, Number.KING));
		
		ArrayList<Card> comm = new ArrayList<Card>();
		comm.add(new Card(Suit.CLUBS, Number.ACE));
		comm.add(new Card(Suit.CLUBS, Number.KING));
		comm.add(new Card(Suit.CLUBS, Number.QUEEN));
		
		ArrayList<Card> comm2 = new ArrayList<Card>();
		comm2.add(new Card(Suit.DIAMONDS, Number.EIGHT));
		comm2.add(new Card(Suit.SPADES, Number.FIVE));
		comm2.add(new Card(Suit.HEARTS, Number.FOUR));
		
		GameAction gameAction = new GameAction(action, pot, bet, hand, comm);
		gameAction.setCommunityCards(comm2);
		
		assertTrue(gameAction.getPot() == pot);
		assertTrue(gameAction.getAction() == action);
		assertTrue(gameAction.getBet() == bet);
		assertTrue(gameAction.getHand().equals(hand));
		assertTrue(gameAction.getCommunityCards().equals(comm2));
	}
	
	@Test
	public void testGetPokerActionValue()
	{
		PokerAction action = PokerAction.START;
		assertTrue(action.getValue().equals("START"));
		
	}
	
	@Test
	public void testGetPokerAction()
	{
		PokerAction action = PokerAction.START;
		assertTrue(PokerAction.getAction("START") == action);
		assertTrue(PokerAction.getAction("asdf") == PokerAction.UNKNOWN);
	}
	
}

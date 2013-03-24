package dataModels;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import dataModels.GameAction.PokerAction;

public class OptimalityTest {

	@Test
	public void preFlopTest()
	{
		Optimality op = new Optimality();
		
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card("HA"));
		hand.add(new Card("DA"));
		
		assertTrue(op.getPreFlopEV(hand) == 2.32);
		
		hand.add(new Card("CA"));
		assertTrue(op.getPreFlopEV(hand) == Double.NEGATIVE_INFINITY);
		
		hand.clear();
		
		hand.add(new Card("UK"));
		hand.add(new Card("U5"));
		assertTrue(op.getPreFlopEV(hand) == Double.NEGATIVE_INFINITY);
		
		hand.clear();
		
		hand.add(new Card("HK"));
		hand.add(new Card("H5"));
		assertTrue(op.getPreFlopEV(hand) == -0.05);
		
		hand.clear();
		
		hand.add(new Card("H5"));
		hand.add(new Card("HK"));
		assertTrue(op.getPreFlopEV(hand) == -0.05);
	}
	
	@Test
	public void preFlopRatingTest()
	{
		Optimality op = new Optimality();
		
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card("HA"));
		hand.add(new Card("DA"));
		
		assertTrue(op.preFlopOptimalityRating(hand) == Optimality.POSTIVE_ACTION_VALUE);
		
		
		hand.add(new Card("H5"));
		hand.add(new Card("HK"));
		assertTrue(op.preFlopOptimalityRating(hand) == Optimality.NEGATIVE_ACTION_VALUE);
	}
	
	@Test
	public void getHandEVTest()
	{
		Optimality op = new Optimality();
		int pot = 100;
		int bet = 5;
		
		/*
		 * Perfect Hand
		 */
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card("HA"));
		hand.add(new Card("HK"));
		
		ArrayList<Card> comm = new ArrayList<Card>();
		comm.add(new Card("HQ"));
		comm.add(new Card("HJ"));
		comm.add(new Card("HT"));
		comm.add(new Card("D2"));
		comm.add(new Card("D3"));
		
		assertTrue(op.isPositiveEV(pot, bet, hand, comm));
		
		
		/*
		 * One of the Worse Hand
		 */
		hand = new ArrayList<Card>();
		hand.add(new Card("C2"));
		hand.add(new Card("D9"));
		
		comm = new ArrayList<Card>();
		comm.add(new Card("HQ"));
		comm.add(new Card("H8"));
		comm.add(new Card("HT"));
		comm.add(new Card("D2"));
		comm.add(new Card("S3"));
		
		bet = 80;
		
		assertFalse(op.isPositiveEV(pot, bet, hand, comm));
		
		/*
		 * Terrible PreFlop
		 */
		hand = new ArrayList<Card>();
		hand.add(new Card("C2"));
		hand.add(new Card("D9"));
		
		comm = new ArrayList<Card>();
		
		bet = 80;
		
		assertFalse(op.isPositiveEV(pot, bet, hand, comm));
		
		/*
		 * After Flop
		 */
		hand = new ArrayList<Card>();
		hand.add(new Card("C2"));
		hand.add(new Card("D9"));
		
		comm = new ArrayList<Card>();
		comm.add(new Card("HQ"));
		comm.add(new Card("H8"));
		comm.add(new Card("HT"));
		
		
		bet = 80;
		
		assertFalse(op.isPositiveEV(pot, bet, hand, comm));
		
		/*
		 * Bad Value
		 */
		hand = new ArrayList<Card>();
		hand.add(new Card("C2"));
		hand.add(new Card("D9"));
		
		comm = new ArrayList<Card>();
		comm.add(new Card("HQ"));
		comm.add(new Card("H8"));
		comm.add(new Card("HT"));
		comm.add(new Card("HQ"));
		comm.add(new Card("H8"));
		comm.add(new Card("HT"));
		
		bet = 80;
		
		assertFalse(op.isPositiveEV(pot, bet, hand, comm));
		
	}
	
	@Test
	public void getOptimalRatingForGameTest()
	{
		Optimality op = new Optimality();
		String id = "ASDFG";
		int pot = 100;
		int bet = 50;
		
		ArrayList<GameAction> gameActions = new ArrayList<GameAction>();
		
		/**
		 * Perfect Hand - 100% Fake Optimal Game
		 */
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card("HA"));
		hand.add(new Card("HK"));
		
		ArrayList<Card> comm = new ArrayList<Card>();
		comm.add(new Card("HQ"));
		comm.add(new Card("HJ"));
		comm.add(new Card("HT"));
		comm.add(new Card("D2"));
		comm.add(new Card("D3"));
		
		gameActions.add(new GameAction(PokerAction.START, 0, 0, hand, comm));
		gameActions.add(new GameAction(PokerAction.BET, pot, bet, hand, comm));
		gameActions.add(new GameAction(PokerAction.END, 0, 0, hand, comm));
		
		Game game = new Game(gameActions, id);
		
		Double opValue = op.getOptimalRatingForGame(game);
		
		assertTrue(opValue == 100);
		
		/**
		 * Actionless game
		 */
		gameActions = new ArrayList<GameAction>();
		game.setGameActions(gameActions);
		
		opValue = op.getOptimalRatingForGame(game);
		assertTrue(opValue == 0);
		
		/**
		 * Terrible Hand - 0% FakeOptimal Game
		 */
		hand = new ArrayList<Card>();
		hand.add(new Card("H2"));
		hand.add(new Card("D9"));
		
		comm = new ArrayList<Card>();
		comm.add(new Card("DQ"));
		comm.add(new Card("SJ"));
		comm.add(new Card("DA"));
		comm.add(new Card("C5"));
		comm.add(new Card("S3"));
		
		//Crazy high bet for terrible hand
		bet = 200;
		
		gameActions.add(new GameAction(PokerAction.START, 0, 0, hand, comm));
		gameActions.add(new GameAction(PokerAction.BET, pot, bet, hand, comm));
		gameActions.add(new GameAction(PokerAction.END, 0, 0, hand, comm));
		
		game = new Game(gameActions, id);
		
		opValue = op.getOptimalRatingForGame(game);
		
		assertTrue(opValue == 0);
		
	}
	


}

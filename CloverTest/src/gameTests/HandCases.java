package gameTests;

import game.Card;
import game.GameMechanics;
import game.Player;

import java.util.ArrayList;

import android.test.AndroidTestCase;

public class HandCases extends AndroidTestCase {

	GameMechanics mech = new GameMechanics(0, 0, null, null, null);
	
	public void testSF()
	{
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card(0, 0));
		hand.add(new Card(1, 0));
		hand.add(new Card(2, 0));
		hand.add(new Card(3, 0));
		hand.add(new Card(4, 0));
		
		ArrayList<ArrayList<Card>> x = new ArrayList<ArrayList<Card>>();
		x.add(hand);
		
		int values[] = mech.getMaxRank(Player.CURRENT, x);
		
		assertTrue(values[0]==1);
		assertTrue(values[1]==4);
		assertTrue(values[2]==4);
		
		
	}
	
	public void testS()
	{
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card(0, 0));
		hand.add(new Card(1, 1));
		hand.add(new Card(2, 0));
		hand.add(new Card(3, 1));
		hand.add(new Card(4, 0));
		
		ArrayList<ArrayList<Card>> x = new ArrayList<ArrayList<Card>>();
		x.add(hand);
		
		int values[] = mech.getMaxRank(Player.CURRENT, x);
		
		assertTrue(values[0]==5);
		assertTrue(values[1]==4);
		assertTrue(values[2]==4);
		
		
	}
	
	public void testF()
	{
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card(0, 0));
		hand.add(new Card(1, 0));
		hand.add(new Card(2, 0));
		hand.add(new Card(9, 0));
		hand.add(new Card(4, 0));
		
		ArrayList<ArrayList<Card>> x = new ArrayList<ArrayList<Card>>();
		x.add(hand);
		
		int values[] = mech.getMaxRank(Player.CURRENT, x);
		
		assertTrue(values[0]==4);
		assertTrue(values[1]==9);
		assertTrue(values[2]==9);
		
		
	}
	
	public void test4()
	{
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card(0, 0));
		hand.add(new Card(0, 1));
		hand.add(new Card(0, 2));
		hand.add(new Card(0, 3));
		hand.add(new Card(12, 0));
		
		ArrayList<ArrayList<Card>> x = new ArrayList<ArrayList<Card>>();
		x.add(hand);
		
		int values[] = mech.getMaxRank(Player.CURRENT, x);
		
		assertTrue(values[0]==2);
		assertTrue(values[1]==0);
		assertTrue(values[2]==12);
		
		
	}
	
	public void test3()
	{
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card(9, 0));
		hand.add(new Card(7, 1));
		hand.add(new Card(3, 2));
		hand.add(new Card(3, 3));
		hand.add(new Card(3, 0));
		
		ArrayList<ArrayList<Card>> x = new ArrayList<ArrayList<Card>>();
		x.add(hand);
		
		int values[] = mech.getMaxRank(Player.CURRENT, x);
		
		assertTrue(values[0]==6);
		assertTrue(values[1]==3);
		assertTrue(values[2]==9);
		
		
	}
	
	public void test2()
	{
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card(9, 0));
		hand.add(new Card(7, 1));
		hand.add(new Card(12, 2));
		hand.add(new Card(12, 3));
		hand.add(new Card(1, 0));
		
		ArrayList<ArrayList<Card>> x = new ArrayList<ArrayList<Card>>();
		x.add(hand);
		
		int values[] = mech.getMaxRank(Player.CURRENT, x);
		
		assertTrue(values[0]==8);
		assertTrue(values[1]==12);
		assertTrue(values[2]==12);
		
		
	}
	
	public void testFH()
	{
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card(9, 0));
		hand.add(new Card(9, 1));
		hand.add(new Card(12, 2));
		hand.add(new Card(12, 3));
		hand.add(new Card(9, 0));
		
		ArrayList<ArrayList<Card>> x = new ArrayList<ArrayList<Card>>();
		x.add(hand);
		
		int values[] = mech.getMaxRank(Player.CURRENT, x);
		
		assertTrue(values[0]==3);
		assertTrue(values[1]==12);
		assertTrue(values[2]==12);
		
		
	}
	
	public void testHC()
	{
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card(1, 0));
		hand.add(new Card(3, 1));
		hand.add(new Card(4, 2));
		hand.add(new Card(7, 3));
		hand.add(new Card(9, 0));
		
		ArrayList<ArrayList<Card>> x = new ArrayList<ArrayList<Card>>();
		x.add(hand);
		
		int values[] = mech.getMaxRank(Player.CURRENT, x);
		
		assertTrue(values[0]==9);
		assertTrue(values[1]==9);
		assertTrue(values[2]==9);
		
		
	}
	
	public void test22()
	{
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card(12, 0));
		hand.add(new Card(5, 1));
		hand.add(new Card(10, 2));
		hand.add(new Card(10, 3));
		hand.add(new Card(5, 0));
		
		ArrayList<ArrayList<Card>> x = new ArrayList<ArrayList<Card>>();
		x.add(hand);
		
		int values[] = mech.getMaxRank(Player.CURRENT, x);
		
		assertTrue(values[0]==7);
		assertTrue(values[1]==10);
		assertTrue(values[2]==12);
		
		
	}
	
	
}

package game;

import org.junit.Test;

import android.test.AndroidTestCase;

import game.*;

public class PlayerTest extends AndroidTestCase{
	
	@Test
	public void TestConstructor() {
		Player pTest = new Player(1, "my name", 10);
		assertTrue(pTest.getAmountMoney()==10);
		assertTrue(pTest.getUsername().equals("my name"));
		assertTrue(pTest.getId()==1);
	}
	
	@Test
	public void TestGettersSetters() {
		Player pTest = new Player(0, null, 0);
		
		
		pTest.setActive(1);
		assertTrue(pTest.getActive()==1);
		
		Card cardOne = new Card(4,1);
		Card cardTwo = new Card(7, 2);
		pTest.setCard(cardOne, 0);
		pTest.setCard(cardTwo, 1);
		assertEquals(pTest.getCard(0), cardOne);
		assertEquals(pTest.getCard(1), cardTwo);
		
		Card[] hand = pTest.getHand();
		assertEquals(hand[0], cardOne);
		assertEquals(hand[1], cardTwo);
	}
	
	@Test
	public void AddRemoveMoney() {
		Player pTest = new Player(2, "Test", 100);
		
		pTest.addMoney(40);
		assertTrue(pTest.getAmountMoney()==140);
		
		pTest.removeMoney(30);
		assertTrue(pTest.getAmountMoney()==110);
	}
	
}

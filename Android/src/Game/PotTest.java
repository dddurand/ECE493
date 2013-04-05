package game;



import java.util.ArrayList;

import android.test.AndroidTestCase;

public class PotTest extends AndroidTestCase{

	public void testConstructor() {
		Pot pot = new Pot(1, 999);
		
		assertTrue(pot.getAmount()==999);
		
		assertTrue(pot.getOwner()==1);
	}
	
	public void testParticipants() {
		Pot pot = new Pot(1, 999);
		pot.addParticipants(2);
		pot.addParticipants(3);
		pot.addParticipants(5);
		
		assertTrue(pot.exist(1));
		assertTrue(pot.exist(2));
		assertTrue(pot.exist(3));
		assertTrue(pot.exist(5));
	}
	
	public void testGetterSetter() {
		Pot pot = new Pot(1, 999);
		
		pot.setAmount(50);
		
		assertTrue(pot.getAmount()==50);
		
		pot.take(20);
		
		assertTrue(pot.getTotal()==30);
		
		pot.addTotal(40);
		
		assertTrue(pot.getTotal()==70);
		
		
		pot.addParticipants(3);
		ArrayList<Integer> myArray= new ArrayList<Integer>();
		myArray.add(6);
		myArray.add(4);
		pot.addList(myArray);
		
		assertTrue(pot.exist(3));
		assertTrue(pot.exist(6));
		assertTrue(pot.exist(4));
		
		Player winner1 = new Player(1, "t1", 10);
		Player winner2 = new Player(3, "t2", 11);
				
		
		Player[] winners = {winner1,winner2};
		
		pot.setWinners(winners);
		
		assertTrue(winners[0].getId()==pot.getWinners()[0].getId());
		assertTrue(winners[1].getId()==pot.getWinners()[1].getId());
		
		assertTrue(pot.isWinner(1));
		assertTrue(pot.isWinner(3));
		
		assertTrue((pot.getLosers()[0]==4&&pot.getLosers()[1]==6)||(pot.getLosers()[0]==6 && pot.getLosers()[1]==4));
	}
	
	public void testPlayerAmount() {
		Pot pot = new Pot(1,999);
		
		pot.addParticipants(3);
		pot.addParticipants(6);
		pot.addParticipants(4);
		
		pot.setAmount(30);
		pot.setPlayerAmount(3, 20);
		pot.setPlayerAmount(6,  15);
		pot.setPlayerAmount(4, 25);
		pot.setPlayerAmount(1, 30);
		
		assertTrue(pot.getPlayerAmount(3)==20);
		assertFalse(pot.checkPlayersBet());
		
		pot.decrementPlayerAmount(10);
		assertTrue(pot.getPlayerAmount(3)==10);
		assertTrue(pot.getPlayerAmount(1)==20);
		assertTrue(pot.getPlayerAmount(6)==5);
		assertTrue(pot.getPlayerAmount(4)==15);
		
		pot.resetPlayerAmount();
		assertTrue(pot.getPlayerAmount(3)==0);
		assertTrue(pot.getPlayerAmount(1)==0);
		assertTrue(pot.getPlayerAmount(6)==0);
		assertTrue(pot.getPlayerAmount(4)==0);
		
		pot.setAmount(5);
		pot.setPlayerAmount(3, 5);
		pot.setPlayerAmount(6, 5);
		pot.setPlayerAmount(4, 5);
		pot.setPlayerAmount(1, 5);
		assertTrue(pot.checkPlayersBet());
	}
	
	
	

}

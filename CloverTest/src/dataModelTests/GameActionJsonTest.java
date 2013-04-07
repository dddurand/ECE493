package dataModelTests;

import org.json.JSONException;
import org.json.JSONObject;

import dataModels.GameActionJson;
import server.GameAction.PokerAction;
import android.test.AndroidTestCase;

public class GameActionJsonTest extends AndroidTestCase {

	public void tearDown() throws Exception {
	    ///CLOVER:FLUSH
	    super.tearDown();
	}
	
	public void testGets()
	{
		int id = 1;
		PokerAction action = PokerAction.ADDPLAYER;
		int pot = 5;
		int bet = 10;
		String hand = "good";
		String comm = "bad";
		
		GameActionJson actions = new GameActionJson(id, action, pot, bet, hand, comm);
		
		assertTrue(actions.getId() == id);
		assertTrue(actions.getAction() == action);
		assertTrue(actions.getPot() == pot);
		assertTrue(actions.getBet() == bet);
		assertTrue(actions.getHand().equals(hand));
		assertTrue(actions.getCommunityCards().equals(comm));
		
		
	}
	
	public void testSets()
	{
		int id = 1;
		PokerAction action = PokerAction.ADDPLAYER;
		int pot = 5;
		int bet = 10;
		String hand = "good";
		String comm = "bad";
		
		int id2 = 1;
		PokerAction action2 = PokerAction.BET;
		int pot2 = 15;
		int bet2 = 110;
		String hand2 = "1good";
		String comm2 = "1bad";
		
		GameActionJson actions = new GameActionJson(id, action, pot, bet, hand, comm);
		actions.setId(id2);
		actions.setAction(action2);
		actions.setBet(bet2);
		actions.setPot(pot2);
		actions.setHand(hand2);
		actions.setCommunityCards(comm2);
		
		assertTrue(actions.getId() == id2);
		assertTrue(actions.getAction() == action2);
		assertTrue(actions.getPot() == pot2);
		assertTrue(actions.getBet() == bet2);
		assertTrue(actions.getHand().equals(hand2));
		assertTrue(actions.getCommunityCards().equals(comm2));
		
		
	}
	
	public void testGetJson()
	{
		int id = 1;
		PokerAction action = PokerAction.ADDPLAYER;
		int pot = 5;
		int bet = 10;
		String hand = "good";
		String comm = "bad";
		
		GameActionJson actions = new GameActionJson(id, action, pot, bet, hand, comm);
		
		try {
			JSONObject obj = actions.getJson();
			
			assertTrue(obj.getString("action").equals(action.getValue()));
			assertTrue(obj.getInt("pot") == pot);
			assertTrue(obj.getInt("bet") == bet);
			assertTrue(obj.getString("hand").equals(hand));
			assertTrue(obj.getString("communityCards").equals(comm));
			
		} catch (JSONException e) {
			fail();
		}

	}
	
}

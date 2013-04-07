package dataModelTests;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import server.GameAction.PokerAction;
import android.test.AndroidTestCase;
import dataModels.GameActionJson;
import dataModels.GameJson;

public class GameJsonTest extends AndroidTestCase {


	public void tearDown() throws Exception {
	    ///CLOVER:FLUSH
	    super.tearDown();
	}
	
	public void testConstructor()
	{
		String gameID = "asdf";
		
		ArrayList<GameActionJson> actions = new ArrayList<GameActionJson>();
		
		int id = 1;
		PokerAction action = PokerAction.ADDPLAYER;
		int pot = 5;
		int bet = 10;
		String hand = "good";
		String comm = "bad";
		
		GameActionJson gameAction = new GameActionJson(id, action, pot, bet, hand, comm);

		actions.add(gameAction);
		
		GameJson gameJson = new GameJson(actions, gameID);
		
		
		assertTrue(gameJson.getGameID().equals(gameID));
		assertTrue(gameJson.getGameActions().equals(actions));
		
	}
	
	public void testSetters()
	{
		String gameID = "asdf";
		
		ArrayList<GameActionJson> actions = new ArrayList<GameActionJson>();
		
		int id = 1;
		PokerAction action = PokerAction.ADDPLAYER;
		int pot = 5;
		int bet = 10;
		String hand = "good";
		String comm = "bad";
		
		GameActionJson gameAction = new GameActionJson(id, action, pot, bet, hand, comm);

		actions.add(gameAction);
		
		GameJson gameJson = new GameJson(actions, gameID);
		
		String gameID2 = "asdf2";
		ArrayList<GameActionJson> actions2 = new ArrayList<GameActionJson>();
		gameJson.setGameID(gameID2);
		gameJson.setGameActions(actions2);
		
		assertTrue(gameJson.getGameID().equals(gameID2));
		assertTrue(gameJson.getGameActions().equals(actions2));
		
	}
	
	public void testGetJson()
	{
		String gameID = "asdf";
		ArrayList<GameActionJson> actions = new ArrayList<GameActionJson>();
		
		int id = 1;
		PokerAction action = PokerAction.ADDPLAYER;
		int pot = 5;
		int bet = 10;
		String hand = "good";
		String comm = "bad";
		
		GameActionJson gameAction = new GameActionJson(id, action, pot, bet, hand, comm);

		actions.add(gameAction);
		
		GameJson gameJson = new GameJson(actions, gameID);

		try {
			JSONObject obj = gameJson.getJson();
			JSONArray array = obj.getJSONArray("gameActions");
			
			for(int i = 0; i < array.length(); i++)
			{
				String actionJson = array.getString(i);
				String actionJson2 = actions.get(i).getJson().toString();
				assertTrue(actionJson.equals(actionJson2));
				
			}
			
		} catch (JSONException e) {
			fail();
		}
		
	}
	
}

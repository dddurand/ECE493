package serverTest;

import game.Player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import server.GameAction;
import server.GameAction.PokerAction;
import android.test.AndroidTestCase;

public class GameActionTest extends AndroidTestCase {

	public void testCons()
	{
		int position = 20;
		PokerAction pokerAction = PokerAction.BET;
		int value = 8;
		
		GameAction action = new GameAction(position, pokerAction, value);
		
		assertTrue(action.getAction() == pokerAction);
		assertTrue(action.getPosition() == position);
		assertTrue(action.getValue() == value);
	}
	
	public void testCons2()
	{
		Player player = new Player(1,"bob",2);
		
		GameAction action = new GameAction(player, false);
		
		assertTrue(action.getPlayer().equals(player));
		assertTrue(action.getAction() == PokerAction.REMOVEPLAYER);
	}
	
	public void testCons3()
	{
		PokerAction pokerAction = PokerAction.LOSS;
		
		GameAction action = new GameAction(pokerAction);
		
		assertTrue(action.getAction() == pokerAction);
	}
	
	public void testSetters()
	{
		PokerAction pokerAction = PokerAction.LOSS;
		Player player = new Player(1,"bob",2);
		int position = 20;
		PokerAction pokerAction2 = PokerAction.BET;
		int value = 8;
		
		
		GameAction action = new GameAction(pokerAction);
		action.setAction(pokerAction2);
		action.setPlayer(player);
		action.setValue(value);
		action.setPosition(position);
		
		assertTrue(action.getAction() == pokerAction2);
		assertTrue(action.getPosition() == position);
		assertTrue(action.getValue() == value);
		assertTrue(action.getPlayer().equals(player));
	}
	
	public void testPokerAction()
	{
		String stringAction = PokerAction.WIN.getValue();
		PokerAction action = PokerAction.getAction(stringAction);
		
		assertTrue(action == PokerAction.WIN);
		
		action = PokerAction.getAction("asdf");
		assertTrue(action == PokerAction.UNKNOWN);
		
	}
	
	public void testSerialization()
	{
		try{
			
			PipedInputStream keepInStream = new PipedInputStream(1000);
			
			PipedOutputStream keepOutStream = new PipedOutputStream(keepInStream);
			
			
			ObjectOutputStream aOut = new ObjectOutputStream(keepOutStream);
			aOut.flush();
			
			ObjectInputStream aIn = new ObjectInputStream(keepInStream);
			
			PokerAction pokerAction = PokerAction.LOSS;
			Player player = new Player(1,"bob",2);
			int position = 20;
			int value = 8;
			
			
			GameAction action = new GameAction(pokerAction);
			action.setAction(pokerAction);
			action.setPlayer(player);
			action.setValue(value);
			action.setPosition(position);
			
			aOut.writeObject(action);
			
			GameAction action2 = (GameAction)aIn.readObject();
			
			assertTrue(action2.getAction() == pokerAction);
			assertTrue(action2.getPosition() == position);
			assertTrue(action2.getValue() == value);
			assertTrue(action2.getPlayer().getUsername().equals(player.getUsername()));
			assertTrue(action2.getPlayer().getId() == (player.getId()));
			assertTrue(action2.getPlayer().getAmountMoney() == (player.getAmountMoney()));
			
			aOut.close();
			
			} catch(Exception e)
			{
				fail();
			}
	}
	
	
	
}

package client;

import game.Card;
import game.Player;
import game.Pot;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.UUID;

import server.GameAction;
import server.GameAction.PokerAction;
import server.GameState;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import client.mock.PlayAreaMock;

public class ClientListenerCasterTest  extends ActivityUnitTestCase<PlayAreaMock> implements ClientTaskListener  {

	public ClientListenerCasterTest() {
		super(PlayAreaMock.class);
	}
	
	public void testValid()
	{
		try{
		PlayAreaMock activity = this.startActivity(new Intent(), null, null);
			
		PipedInputStream keepInStream = new PipedInputStream(1000);
		
		PipedOutputStream sendOutStream = new PipedOutputStream(keepInStream);
		
		final ObjectOutputStream bOut = new ObjectOutputStream(sendOutStream);
		bOut.flush();
		sendOutStream.flush();
		
		ObjectInputStream aIn = new ObjectInputStream(keepInStream);
		
		PlayAreaMock pa = this.getActivity();
		
		ClientListener cl = new ClientListener(aIn, pa);
		
		UUID uuid = UUID.randomUUID();
		Player player = new Player(0, "bob", 22);
		ArrayList<String> empty = new ArrayList<String>();
		int currentTurn = 0;
		
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(player);
		
		int currentDealer = 0;
		int blindAmount = 5;
		Pot mainPot = new Pot(0, 25);
		ArrayList<Pot> sidePots = new ArrayList<Pot>();
		Card community[] = new Card[0];
		GameAction lastAction = new GameAction(PokerAction.BET);
		int timeoutSeconds = 5;
		int gameUpdateNumber = 0;
		
		final GameState state = new GameState(uuid, player, empty, currentTurn, players, currentDealer, blindAmount, mainPot, sidePots, community, lastAction, timeoutSeconds, gameUpdateNumber);
		
		
		 new Thread(new Runnable() {
			    public void run() {
			    	try {
						bOut.writeObject(state);
						bOut.close();
					} catch (Exception e) {
						fail();
					}
					
			    }
			  }).start();
		
		 
		 cl.addListener(this);
		cl.run();
		
		assertTrue(activity.isUpdated());
		GameState state2 = activity.getState();
		
		assertTrue(state.getBlindAmount() == state2.getBlindAmount());
		assertTrue(state.getUUID().toString().equals(state2.getUUID().toString()));
		assertTrue(state.getCurrentPlayerTurn()==state2.getCurrentPlayerTurn());
		assertTrue(state.getGameUpdateNumber()==state2.getGameUpdateNumber());
		
		} catch(IOException e){
			fail();
		}
		
		if(!this.isFinishCalled())
		{
			fail();
		}
	}

	public void testClosed()
	{
		try{
			PlayAreaMock activity = this.startActivity(new Intent(), null, null);
			
		PipedInputStream keepInStream = new PipedInputStream(1000);
		
		PipedOutputStream sendOutStream = new PipedOutputStream(keepInStream);
		
		final ObjectOutputStream bOut = new ObjectOutputStream(sendOutStream);
		bOut.flush();
		sendOutStream.flush();
		
		ObjectInputStream aIn = new ObjectInputStream(keepInStream);
		
		PlayAreaMock pa = this.getActivity();
		
		ClientListener cl = new ClientListener(aIn, pa);
		
		bOut.close();
		
		cl.addListener(this);
		cl.run();
		
		assertTrue(!activity.isUpdated());

		
		if(!this.isFinishCalled())
		{
			fail();
		}
		
		} catch(IOException e){
			fail();
		}
	}
	
	public void testCancel()
	{
		try{
			PlayAreaMock activity = this.startActivity(new Intent(), null, null);
			
		PipedInputStream keepInStream = new PipedInputStream(1000);
		
		PipedOutputStream sendOutStream = new PipedOutputStream(keepInStream);
		
		final ObjectOutputStream bOut = new ObjectOutputStream(sendOutStream);
		bOut.flush();
		sendOutStream.flush();
		
		ObjectInputStream aIn = new ObjectInputStream(keepInStream);
		
		PlayAreaMock pa = this.getActivity();
		
		final ClientListener cl = new ClientListener(aIn, pa);
		
		UUID uuid = UUID.randomUUID();
		Player player = new Player(0, "bob", 22);
		ArrayList<String> empty = new ArrayList<String>();
		int currentTurn = 0;
		
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(player);
		
		int currentDealer = 0;
		int blindAmount = 5;
		Pot mainPot = new Pot(0, 25);
		ArrayList<Pot> sidePots = new ArrayList<Pot>();
		Card community[] = new Card[0];
		GameAction lastAction = new GameAction(PokerAction.BET);
		int timeoutSeconds = 5;
		int gameUpdateNumber = 0;
		
		final GameState state = new GameState(uuid, player, empty, currentTurn, players, currentDealer, blindAmount, mainPot, sidePots, community, lastAction, timeoutSeconds, gameUpdateNumber);
		
		
		 new Thread(new Runnable() {
			    public void run() {
			    	try {
						bOut.writeObject(state);
						cl.cancel();
						
					} catch (Exception e) {
						fail();
					}
					
			    }
			  }).start();
		
		 
		 cl.addListener(this);
		 cl.run();
		
		assertTrue(activity.isUpdated());
		GameState state2 = activity.getState();
		
		assertTrue(state.getBlindAmount() == state2.getBlindAmount());
		assertTrue(state.getUUID().toString().equals(state2.getUUID().toString()));
		assertTrue(state.getCurrentPlayerTurn()==state2.getCurrentPlayerTurn());
		assertTrue(state.getGameUpdateNumber()==state2.getGameUpdateNumber());
		
		if(!this.isFinishCalled())
		{
			fail();
		}
		
		} catch(IOException e){
			fail();
		}
	}

	@Override
	public void onPlayerTaskClose() {
		this.getActivity().finish();
	}
	
}

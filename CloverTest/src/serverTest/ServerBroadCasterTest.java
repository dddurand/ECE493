package serverTest;

import game.Card;
import game.Player;
import game.Pot;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import server.GameAction;
import server.GameAction.PokerAction;
import server.GameState;
import server.ServerBroadCaster;
import serverTest.PlayerTaskCompleteNotifyTest.ActionPlayerCatcher;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import client.mock.PlayAreaMock;

public class ServerBroadCasterTest  extends ActivityUnitTestCase<PlayAreaMock> {

	private Player player1 = new Player(0, "bob", 1);
	private Player player2 = new Player(1, "bob1", 2);
	
	public ServerBroadCasterTest() {
		super(PlayAreaMock.class);
	}

	public void testNoPlayers()
	{

		PlayAreaMock activity = this.startActivity(new Intent(), null, null);
	
		LinkedBlockingQueue<GameState> queue = new LinkedBlockingQueue<GameState>();
		
		ServerBroadCaster cb = new ServerBroadCaster(queue, activity);
		
		
		ActionPlayerCatcher ct = new ActionPlayerCatcher();
		cb.addListener(ct);
		
		cb.removePlayer(player2);
		
		GameState state = this.getState();
		queue.add(state);
		
		Thread th = new Thread(cb);
		th.start();

		cb.cancel();
		
		if(ct.isRun())
			fail();
		
		try {
			th.join();
		} catch (InterruptedException e) {
			fail();
		}
		
		try {
			Thread.sleep(1000);
			Thread.yield();
		} catch (InterruptedException e) {
			fail();
		}

	}
	
	public void testValid()
	{
		try{
		PlayAreaMock activity = this.startActivity(new Intent(), null, null);
		
		PipedInputStream keepInStream = new PipedInputStream(10000);
		
		PipedOutputStream sendOutStream = new PipedOutputStream(keepInStream);
		
		final ObjectOutputStream bOut = new ObjectOutputStream(sendOutStream);
		bOut.flush();
		sendOutStream.flush();
		
		ObjectInputStream aIn = new ObjectInputStream(keepInStream);
		LinkedBlockingQueue<GameState> queue = new LinkedBlockingQueue<GameState>();
		
		ServerBroadCaster cb = new ServerBroadCaster(queue, activity);
			
		ActionPlayerCatcher ct = new ActionPlayerCatcher();
		cb.addListener(ct);
		

		cb.addPlayer(player1, bOut);
		
		GameState state = this.getState();
		state.setPlayer(player1);
		queue.add(state);
		
		
		Thread th = new Thread(cb);
		th.start();
		
		GameState state2 =(GameState) aIn.readObject();
		assertTrue(state2.getUUID().toString().equals(state.getUUID().toString()));
		
		try {
			cb.removePlayer(player1);
			queue.add(state);
			
			Thread.yield();
			cb.cancel();
			th.join();
		} catch (InterruptedException e1) {
			fail();
		}
		
		
		}catch(IOException e)
		{
			fail();
		} catch (ClassNotFoundException e) {
			fail();
		}
	}
	
	public void testValid2Players()
	{
		try{
		PlayAreaMock activity = this.startActivity(new Intent(), null, null);
		//p1
		PipedInputStream keepInStream = new PipedInputStream(20000);
		PipedOutputStream sendOutStream = new PipedOutputStream(keepInStream);
		final ObjectOutputStream bOut1 = new ObjectOutputStream(sendOutStream);
		bOut1.flush();
		sendOutStream.flush();
		ObjectInputStream aIn = new ObjectInputStream(keepInStream);
		
		//p2
		PipedInputStream keepInStream2 = new PipedInputStream(20000);
		PipedOutputStream sendOutStream2 = new PipedOutputStream(keepInStream2);
		final ObjectOutputStream bOut2 = new ObjectOutputStream(sendOutStream2);
		bOut2.flush();
		sendOutStream2.flush();
		ObjectInputStream aIn2 = new ObjectInputStream(keepInStream2);
		
		LinkedBlockingQueue<GameState> queue = new LinkedBlockingQueue<GameState>();
		
		ServerBroadCaster cb = new ServerBroadCaster(queue, activity);
			
		ActionPlayerCatcher ct = new ActionPlayerCatcher();
		cb.addListener(ct);
		

		cb.addPlayer(player1, bOut1);
		cb.addPlayer(player2, bOut2);
		
		GameState state = this.getState();
		state.setPlayer(player1);
		queue.add(state);
		
		GameState state2 = this.getState();
		state2.setPlayer(player2);
		queue.add(state2);
		
		
		Thread th = new Thread(cb);
		th.start();
		
		GameState stateFor1 =(GameState) aIn.readObject();
		assertTrue(state.getUUID().toString().equals(stateFor1.getUUID().toString()));
		
		GameState stateFor2 =(GameState) aIn2.readObject();
		assertTrue(state2.getUUID().toString().equals(stateFor2.getUUID().toString()));
		
		cb.removePlayer(player1);
			
		queue.add(state);
		queue.add(state2);	
		
		stateFor2 =(GameState) aIn2.readObject();
		assertTrue(state2.getUUID().toString().equals(stateFor2.getUUID().toString()));
		
		
		}catch(IOException e)
		{
			fail();
		} catch (ClassNotFoundException e) {
			fail();
		}
	}
	
	public void testClosed()
	{
		try{
		PlayAreaMock activity = this.startActivity(new Intent(), null, null);
		
		PipedInputStream keepInStream = new PipedInputStream(10000);
		
		PipedOutputStream sendOutStream = new PipedOutputStream(keepInStream);
		
		final ObjectOutputStream bOut = new ObjectOutputStream(sendOutStream);
		bOut.flush();
		sendOutStream.flush();
		
		ObjectInputStream aIn = new ObjectInputStream(keepInStream);
		LinkedBlockingQueue<GameState> queue = new LinkedBlockingQueue<GameState>();
		
		ServerBroadCaster cb = new ServerBroadCaster(queue, activity);
			
		ActionPlayerCatcher ct = new ActionPlayerCatcher();
		cb.addListener(ct);
		

		cb.addPlayer(player1, bOut);
		
		GameState state = this.getState();
		state.setPlayer(player1);
		
		Thread th = new Thread(cb);
		th.start();
		
		aIn.close();
		queue.add(state);
		
		}catch(IOException e)
		{
			fail();
		}
	}
	
	public GameState getState()
	{
		Player player = new Player(1, "BOB", 1);
		 int timeoutSeconds = 1;
		 int currentPlayerTurn = 2;
		 ArrayList<Player> players = new ArrayList<Player>();
		 players.add(player);
		 int currentDealer = 3;
		 int blindAmount = 4;
		 Pot mainPot = new Pot(1,0);
		 ArrayList<Pot> sidePots = new ArrayList<Pot>();
		 sidePots.add(mainPot);
		 Card[] community = new Card[3];
		 java.util.UUID gameID = java.util.UUID.randomUUID();
		 GameAction lastPokerGameAction = new GameAction(PokerAction.ADDPLAYER);
		 int gameUpdateNumber = 5;	
		ArrayList<String> history = new ArrayList<String>();
		history.add("asdf");
		
		return new GameState(gameID,
				player,
				new ArrayList<String>(),
				currentPlayerTurn, 
				players,
				currentDealer,
				blindAmount,
				mainPot,
				sidePots,
				community,
				lastPokerGameAction,
				timeoutSeconds,
				gameUpdateNumber);
		
	}
	
}

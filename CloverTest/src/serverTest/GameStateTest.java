package serverTest;

import game.Card;
import game.Player;
import game.Pot;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;

import server.GameAction;
import server.GameAction.PokerAction;
import server.GameState;
import android.test.AndroidTestCase;

public class GameStateTest extends AndroidTestCase {

	private int timeoutSeconds = 5;
	private int currentPlayerTurn = 3;
	private ArrayList<Player> players = new ArrayList<Player>();
	private int currentDealer = 2;
	private int blindAmount = 1;
	private Pot mainPot = new Pot(0,0);
	private ArrayList<Pot> sidePots = new ArrayList<Pot>();
	private Card[] community = new Card[2];
	private java.util.UUID gameID = java.util.UUID.randomUUID();
	private GameAction lastPokerGameAction = new GameAction(PokerAction.ADDPLAYER);
	private int gameUpdateNumber = 0;	
	private Player player = new Player(0, "BOB", 1);
	
	public void testCons()
	{
		GameState state = new GameState(gameID,
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
		
		assertTrue(state.getUUID().toString().equals(gameID.toString()));
		assertTrue(state.getPlayer().getId() == player.getId());
		assertTrue(state.getPlayerHistory().isEmpty());
		assertTrue(state.getPlayers().isEmpty());
		assertTrue(state.getCurrentDealer() == currentDealer);
		assertTrue(state.getBlindAmount() == blindAmount);
		assertTrue(state.getMainPot().getTotal() == 0);
		assertTrue(state.getSidePots().isEmpty());
		assertTrue(state.getCommunity().equals(community));
		assertTrue(state.getLastPokerGameAction() == lastPokerGameAction);
		assertTrue(state.getTimeoutSeconds() == timeoutSeconds);
		assertTrue(state.getCurrentPlayerTurn() == currentPlayerTurn);
		assertTrue(state.getGameUpdateNumber() == gameUpdateNumber);
		
	}
	
	public void testSetters()
	{
		GameState state = new GameState(gameID,
				player,
				new ArrayList<String>(),
				blindAmount, 
				players,
				currentDealer,
				blindAmount,
				mainPot,
				sidePots,
				community,
				lastPokerGameAction,
				timeoutSeconds,
				gameUpdateNumber);
		
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
		 
		 state.setUUID(gameID);
		 state.setPlayer(player);
		 state.setPlayerHistory(history);
		 state.setPlayers(players);
		 state.setCurrentDealer(currentDealer);
		 state.setBlindAmount(blindAmount);
		 state.setMainPot(mainPot);
		 state.setSidePots(sidePots);
		 state.setCommunity(community);
		 state.setLastPokerGameAction(lastPokerGameAction);
		 state.setTimeoutSeconds(timeoutSeconds);
		 state.setCurrentPlayerTurn(currentPlayerTurn);
		 state.setGameUpdateNumber(gameUpdateNumber);
		 
		 assertTrue(state.getUUID().toString().equals(gameID.toString()));
			assertTrue(state.getPlayer().getId() == 1);
			assertTrue(state.getPlayerHistory().size() == 1);
			assertTrue(state.getPlayers().size() == 1);
			assertTrue(state.getCurrentDealer() == currentDealer);
			assertTrue(state.getBlindAmount() == blindAmount);
			assertTrue(state.getMainPot().getOwner() == 1);
			assertTrue(state.getSidePots().size() == 1);
			assertTrue(state.getCommunity().equals(community));
			assertTrue(state.getLastPokerGameAction() == lastPokerGameAction);
			assertTrue(state.getTimeoutSeconds() == timeoutSeconds);
			assertTrue(state.getCurrentPlayerTurn() == currentPlayerTurn);
			assertTrue(state.getGameUpdateNumber() == gameUpdateNumber);
	}
	
	public void testSerialization()
	{
		GameState state = new GameState(gameID,
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
		
try{
			
			PipedInputStream keepInStream = new PipedInputStream(8000);
			
			PipedOutputStream keepOutStream = new PipedOutputStream(keepInStream);
			
			
			ObjectOutputStream aOut = new ObjectOutputStream(keepOutStream);
			aOut.flush();
			
			ObjectInputStream aIn = new ObjectInputStream(keepInStream);
			
			aOut.writeObject(state);
			aOut.flush();
			
			GameState state2 = (GameState)aIn.readObject();
			
			assertTrue(state2.getUUID().toString().equals(gameID.toString()));
			assertTrue(state2.getPlayer().getId() == player.getId());
			assertTrue(state2.getPlayerHistory().isEmpty());
			assertTrue(state2.getPlayers().isEmpty());
			assertTrue(state2.getCurrentDealer() == currentDealer);
			assertTrue(state2.getBlindAmount() == blindAmount);
			assertTrue(state2.getMainPot().getTotal() == 0);
			assertTrue(state2.getSidePots().isEmpty());
			assertTrue(state2.getCommunity().length == community.length);
			assertTrue(state2.getLastPokerGameAction().getAction() == lastPokerGameAction.getAction());
			assertTrue(state2.getTimeoutSeconds() == timeoutSeconds);
			assertTrue(state2.getCurrentPlayerTurn() == currentPlayerTurn);
			assertTrue(state2.getGameUpdateNumber() == gameUpdateNumber);
			
			aOut.close();
			
			} catch(Exception e)
			{
				fail();
			}
		
		
		
	}
	
}

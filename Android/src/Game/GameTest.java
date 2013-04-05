package game;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import server.GameAction;
import server.GameAction.PokerAction;
import server.GameState;
import server.Server;
import server.WatchDogTimer;

public class GameTest {

	@Test
	public void testConstructor() {
		BlockingQueue<GameState> blockingQueue = new LinkedBlockingQueue<GameState>();
		LinkedBlockingDeque<GameAction> gameActionQueue = new LinkedBlockingDeque<GameAction>();
		WatchDogTimer timer = new WatchDogTimer(gameActionQueue, 10);
		
		GameMechanics gameMechanics = new GameMechanics(1, 10, blockingQueue, timer, null);
		
		assertTrue(gameMechanics.getValidPlayerCount()==1);
	}
	
	@Test
	public void testAddRemovePlayer() {
		BlockingQueue<GameState> blockingQueue = new LinkedBlockingQueue<GameState>();
		LinkedBlockingDeque<GameAction> gameActionQueue = new LinkedBlockingDeque<GameAction>();
		WatchDogTimer timer = new WatchDogTimer(gameActionQueue, 10);
		GameMechanics gameMechanics = new GameMechanics(1, 10, blockingQueue, timer, null);
		
		Player myPlayer = new Player(2, "test name", 100);
		
		GameAction gameAction = new GameAction(myPlayer, true);
		
		
		//Can't test state because you don't update state when you add someone
		gameMechanics.processGameAction(gameAction);
		assertTrue(gameMechanics.getValidPlayerCount()==1);
		
		gameAction = new GameAction(myPlayer, false);
		
		//This is wrong the player will not be removed will be removed at start of game
		gameMechanics.processGameAction(gameAction);
		assertTrue(gameMechanics.getValidPlayerCount()==0);		
	}
	
	@Test
	public void testStartGame() {
		BlockingQueue<GameState> blockingQueue = new LinkedBlockingQueue<GameState>();
		LinkedBlockingDeque<GameAction> gameActionQueue = new LinkedBlockingDeque<GameAction>();
		WatchDogTimer timer = new WatchDogTimer(gameActionQueue, 90);
		GameMechanics gameMechanics = new GameMechanics(1, 10, blockingQueue, timer, null);
		
		Player myPlayer1 = new Player(1, "testname1", 100);
		Player myPlayer2 = new Player(2, "testname2", 100);
		
		GameAction gameAction = new GameAction(myPlayer1, true);
		gameMechanics.processGameAction(gameAction);
		
		gameAction = new GameAction(PokerAction.STARTGAME);
		
		//need to test bad start
		assertTrue(myPlayer1.getCard(0)==null);
		assertTrue(myPlayer1.getCard(1)==null);
		
		gameAction = new GameAction(myPlayer2, true);
		gameMechanics.processGameAction(gameAction);
		
		//now test good start
		assertTrue(myPlayer1.getCard(0)!=null);
		assertTrue(myPlayer1.getCard(1)!=null);
		
		/*outGoingList what is this ask dustin */
		
		//check list
		try {
			GameState state = blockingQueue.poll(5, TimeUnit.SECONDS);
			assertTrue(state.getLastPokerGameAction().getAction().getValue().equals(PokerAction.STARTGAME));
			ArrayList<Player> players = state.getPlayers();
			
			assertTrue(players.get(1).getUsername().equals("testname1"));
			assertTrue(players.get(2).getUsername().equals("testname2"));
			
		} catch (InterruptedException e) {
			fail();
		}
	}
	
	@Test
	public void testPlayerAction() {
		BlockingQueue<GameState> blockingQueue = new LinkedBlockingQueue<GameState>();
		LinkedBlockingDeque<GameAction> gameActionQueue = new LinkedBlockingDeque<GameAction>();
		WatchDogTimer timer = new WatchDogTimer(gameActionQueue, 90);
		GameMechanics gameMechanics = new GameMechanics(1, 10, blockingQueue, timer, null);
		
		Player myPlayer1 = new Player(1, "testname1", 100);
		Player myPlayer2 = new Player(2, "testname2", 100);
		
		GameAction gameAction = new GameAction(myPlayer1, true);
		gameMechanics.processGameAction(gameAction);
		gameAction = new GameAction(myPlayer2, true);
		gameMechanics.processGameAction(gameAction);
		
		gameAction = new GameAction(PokerAction.STARTGAME);
		gameMechanics.processGameAction(gameAction);
		
		//get ride of start game
		try {
			blockingQueue.poll(5, TimeUnit.SECONDS);	
		} catch (InterruptedException e) {
			fail();
		}
		
		//wrong player
		gameAction = new GameAction(2, PokerAction.FOLD);
		gameMechanics.processGameAction(gameAction);
		try{
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			assertTrue(state==null);
		} catch (Exception e) {
			fail();
		}
		
		gameAction = new GameAction(1, PokerAction.BET, 15);
		gameMechanics.processGameAction(gameAction);
		try{
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			if(state==null)
				fail();
			ArrayList<Player> players = state.getPlayers();
			assertTrue(players.get(1).getActive()==2);
			//assertTrue(state.getLastPokerGameAction().getAction().getValue().equals(PokerAction.BET));
			assertTrue(state.getPlayer().getId()==1);
		} catch (Exception e) {
			fail();
		}
		
		gameAction = new GameAction(2, PokerAction.RAISE, 20);
		gameMechanics.processGameAction(gameAction);
		try{
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			if(state==null)
				fail();
			ArrayList<Player> players = state.getPlayers();
			assertTrue(players.get(2).getActive()==2);
			assertTrue(state.getLastPokerGameAction().getAction().getValue().equals(PokerAction.RAISE));
			assertTrue(state.getPlayer().getId()==2);
		} catch (Exception e) {
			fail();
		}
		
		gameAction = new GameAction(1, PokerAction.RERAISE, 20);
		gameMechanics.processGameAction(gameAction);
		try{
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			if(state==null)
				fail();
			ArrayList<Player> players = state.getPlayers();
			assertTrue(players.get(1).getActive()==2);
			assertTrue(state.getLastPokerGameAction().getAction().getValue().equals(PokerAction.RERAISE));
			assertTrue(state.getPlayer().getId()==1);
		} catch (Exception e) {
			fail();
		}
		
		gameAction = new GameAction(2, PokerAction.CALL, 10);
		gameMechanics.processGameAction(gameAction);
		try{
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			if(state==null)
				fail();
			ArrayList<Player> players = state.getPlayers();
			assertTrue(players.get(2).getActive()==2);
			assertTrue(state.getLastPokerGameAction().getAction().getValue().equals(PokerAction.CALL));
			assertTrue(state.getPlayer().getId()==2);
		} catch (Exception e) {
			fail();
		}
		
		gameAction = new GameAction(1, PokerAction.CHECK);
		gameMechanics.processGameAction(gameAction);
		try{
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			if(state==null)
				fail();
			ArrayList<Player> players = state.getPlayers();
			assertTrue(players.get(1).getActive()==2);
			assertTrue(state.getLastPokerGameAction().getAction().getValue().equals(PokerAction.CHECK));
			assertTrue(state.getPlayer().getId()==1);
			state = blockingQueue.poll(2, TimeUnit.SECONDS);
		} catch (Exception e) {
			fail();
		}
		//gameAction = new GameAction
		
	}
	

}

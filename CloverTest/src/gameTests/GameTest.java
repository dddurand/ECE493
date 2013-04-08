package gameTests;

import game.GameMechanics;
import game.Player;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import server.GameAction;
import server.GameAction.PokerAction;
import server.GameState;
import server.WatchDogTimer;
import android.test.AndroidTestCase;

public class GameTest extends AndroidTestCase  {

	public void tearDown() throws Exception {
	    ///CLOVER:FLUSH
	    super.tearDown();
	}

	public void testConstructor() {
		BlockingQueue<GameState> blockingQueue = new LinkedBlockingQueue<GameState>();
		LinkedBlockingDeque<GameAction> gameActionQueue = new LinkedBlockingDeque<GameAction>();
		WatchDogTimer timer = new WatchDogTimer(gameActionQueue, 10);
		
		GameMechanics gameMechanics = new GameMechanics(1, 10, blockingQueue, timer, null);
		
		assertTrue(gameMechanics.getValidPlayerCount()==0);
	}
	
	public void testAddRemovePlayer() {
		BlockingQueue<GameState> blockingQueue = new LinkedBlockingQueue<GameState>();
		LinkedBlockingDeque<GameAction> gameActionQueue = new LinkedBlockingDeque<GameAction>();
		WatchDogTimer timer = new WatchDogTimer(gameActionQueue, 10);
		GameMechanics gameMechanics = new GameMechanics(1, 10, blockingQueue, timer, null);
		
		Player myServer = new Player(1, "server", 110);
		Player myPlayer = new Player(2, "test name", 100);
		Player myPlayer2 = new Player(3, "test name2", 100);
		
		GameAction gameAction = new GameAction(myServer,true);
		gameAction.setPosition(GameMechanics.SERVER_POSITION);
		GameAction gameAction2 = new GameAction(myPlayer, true);
		gameAction2.setPosition(GameMechanics.SERVER_POSITION);
		GameAction gameAction3 = new GameAction(myPlayer2, true);
		gameAction3.setPosition(GameMechanics.SERVER_POSITION);
		
		
		//Can't test state because you don't update state when you add someone
		gameMechanics.processGameAction(gameAction);
		gameMechanics.processGameAction(gameAction2);
		gameMechanics.processGameAction(gameAction3);
		assertTrue(gameMechanics.getValidPlayerCount()==3);
		
		
		gameAction = new GameAction(myPlayer, false);
		gameAction.setPosition(GameMechanics.SERVER_POSITION);
		gameAction2 = new GameAction(GameMechanics.SERVER_POSITION, PokerAction.STARTTABLE);
		
		//This is wrong the player will not be removed will be removed at start of game
		gameMechanics.processGameAction(gameAction);
		gameMechanics.processGameAction(gameAction2);
		assertTrue(gameMechanics.getValidPlayerCount()==2);		
	}

	public void testStartGame() {
		BlockingQueue<GameState> blockingQueue = new LinkedBlockingQueue<GameState>();
		LinkedBlockingDeque<GameAction> gameActionQueue = new LinkedBlockingDeque<GameAction>();
		WatchDogTimer timer = new WatchDogTimer(gameActionQueue, 90);
		GameMechanics gameMechanics = new GameMechanics(1, 10, blockingQueue, timer, null);
		
		Player myPlayer1 = new Player(1, "testname1", 100);
		Player myPlayer2 = new Player(2, "testname2", 100);
		
		GameAction gameAction = new GameAction(myPlayer1, true);
		gameAction.setPosition(GameMechanics.SERVER_POSITION);
		gameMechanics.processGameAction(gameAction);
		
		gameAction = new GameAction(PokerAction.STARTTABLE);
		gameAction.setPosition(GameMechanics.SERVER_POSITION);
		
		gameMechanics.processGameAction(gameAction);
		
		//need to test bad start
		assertTrue(myPlayer1.getCard(0)==null);
		assertTrue(myPlayer1.getCard(1)==null);
		
		GameAction gameAction2 = new GameAction(myPlayer2, true);
		gameAction2.setPosition(GameMechanics.SERVER_POSITION);
		gameMechanics.processGameAction(gameAction2);
		gameMechanics.processGameAction(gameAction);
		
		//now test good start
		assertTrue(myPlayer1.getCard(0)!=null);
		assertTrue(myPlayer1.getCard(1)!=null);
		
		/*outGoingList what is this ask dustin */
		
		//check list
		try {
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
			GameState state = blockingQueue.poll(5, TimeUnit.SECONDS);
			assertTrue(state.getLastPokerGameAction().getAction().equals(PokerAction.STARTGAME));
			ArrayList<Player> players = state.getPlayers();
			
			assertTrue(players.get(0).getUsername().equals("testname1"));
			assertTrue(players.get(1).getUsername().equals("testname2"));
			
		} catch (InterruptedException e) {
			fail();
		}
	}

	public void testPlayerAction() {
		BlockingQueue<GameState> blockingQueue = new LinkedBlockingQueue<GameState>();
		LinkedBlockingDeque<GameAction> gameActionQueue = new LinkedBlockingDeque<GameAction>();
		WatchDogTimer timer = new WatchDogTimer(gameActionQueue, 90);
		GameMechanics gameMechanics = new GameMechanics(0, 10, blockingQueue, timer, null);
		
		Player myPlayer1 = new Player(0, "testname1", 230);
		Player myPlayer2 = new Player(1, "testname2", 200);
		Player myPlayer3 = new Player(2, "testname3", 120);
		
		GameAction gameAction = new GameAction(myPlayer1, true);
		gameAction.setPosition(GameMechanics.SERVER_POSITION);
		gameMechanics.processGameAction(gameAction);
		
		gameAction = new GameAction(myPlayer2, true);
		gameAction.setPosition(GameMechanics.SERVER_POSITION);
		gameMechanics.processGameAction(gameAction);
		
		gameAction = new GameAction(myPlayer3, true);
		gameAction.setPosition(GameMechanics.SERVER_POSITION);
		gameMechanics.processGameAction(gameAction);
		
		gameAction = new GameAction(PokerAction.STARTTABLE);
		gameAction.setPosition(GameMechanics.SERVER_POSITION);
		gameMechanics.processGameAction(gameAction);
		
		//get ride of start game
		try {
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			fail();
		}
		
		//wrong player
		gameAction = new GameAction(1, PokerAction.FOLD);
		gameMechanics.processGameAction(gameAction);
		try{
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			assertTrue(state==null);
		} catch (Exception e) {
			fail();
		}
		
		gameAction = new GameAction(2, PokerAction.FOLD);
		gameMechanics.processGameAction(gameAction);
		try{
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			if(state==null)
				fail();
			ArrayList<Player> players = state.getPlayers();
			assertTrue(players.get(2).getActive()==0);
			assertTrue(state.getLastPokerGameAction().getAction().equals(PokerAction.FOLD));
			assertTrue(state.getMainPot().getTotal()==15);
			//assertTrue(state.getPlayer().getId()==2);
		} catch (Exception e) {
			fail();
		}
		
		gameAction = new GameAction(0, PokerAction.BET, 15);
		gameMechanics.processGameAction(gameAction);
		try{
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			if(state==null)
				fail();
			ArrayList<Player> players = state.getPlayers();
			assertTrue(players.get(0).getActive()==2);
			assertTrue(state.getLastPokerGameAction().getAction().equals(PokerAction.BET));
			
			assertTrue(players.get(0).getAmountMoney()==205);
			assertTrue(state.getMainPot().getTotal()==30);
			//assertTrue(state.getPlayer().getId()==0);
		} catch (Exception e) {
			fail();
		}
		
		gameAction = new GameAction(1, PokerAction.RAISE, 30);
		gameMechanics.processGameAction(gameAction);
		try{
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			if(state==null)
				fail();
			ArrayList<Player> players = state.getPlayers();
			assertTrue(players.get(1).getActive()==2);
			assertTrue(state.getLastPokerGameAction().getAction().equals(PokerAction.RAISE));
			
			assertTrue(players.get(1).getAmountMoney()==170);
			assertTrue(state.getMainPot().getTotal()==60);
		} catch (Exception e) {
			fail();
		}
		
		gameAction = new GameAction(0, PokerAction.RERAISE, 45);
		gameMechanics.processGameAction(gameAction);
		try{
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			if(state==null)
				fail();
			ArrayList<Player> players = state.getPlayers();
			assertTrue(players.get(0).getActive()==2);
			assertTrue(state.getLastPokerGameAction().getAction().equals(PokerAction.RERAISE));
			
			assertTrue(players.get(0).getAmountMoney()==160);
			assertTrue(state.getMainPot().getTotal()==105);
			//assertTrue(state.getPlayer().getId()==0);
		} catch (Exception e) {
			fail();
		}
		
		gameAction = new GameAction(1, PokerAction.CALL, 40);
		gameMechanics.processGameAction(gameAction);
		try{
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			if(state==null)
				fail();
			ArrayList<Player> players = state.getPlayers();
			assertTrue(players.get(1).getActive()==2);
			assertTrue(state.getLastPokerGameAction().getAction().equals(PokerAction.CALL));
			
			assertTrue(players.get(1).getAmountMoney()==130);
			assertTrue(state.getMainPot().getTotal()==145);
			//assertTrue(state.getPlayer().getId()==1);
		} catch (Exception e) {
			fail();
		}
		
		gameAction = new GameAction(0, PokerAction.CHECK);
		gameMechanics.processGameAction(gameAction);
		try{
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			if(state==null)
				fail();
			ArrayList<Player> players = state.getPlayers();
			assertTrue(players.get(0).getActive()==2);
			assertTrue(state.getLastPokerGameAction().getAction().equals(PokerAction.CHECK));
			//assertTrue(state.getPlayer().getId()==0);
			//state = blockingQueue.poll(2, TimeUnit.SECONDS);
		} catch (Exception e) {
			fail();
		}
		
		gameAction = new GameAction(1, PokerAction.CHECK);
		gameMechanics.processGameAction(gameAction);
		try {
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			if(state==null)
				fail();
			ArrayList<Player> players = state.getPlayers();
			assertTrue(players.get(1).getActive()==2);
			assertTrue(state.getLastPokerGameAction().getAction().equals(PokerAction.CHECK));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gameAction = new GameAction(0, PokerAction.CHECK);
		gameMechanics.processGameAction(gameAction);
		try {
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			if(state==null)
				fail();
			ArrayList<Player> players = state.getPlayers();
			assertTrue(players.get(0).getActive()==2);
			assertTrue(state.getLastPokerGameAction().getAction().equals(PokerAction.CHECK));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gameAction = new GameAction(1, PokerAction.CHECK);
		gameMechanics.processGameAction(gameAction);
		try {
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			if(state==null)
				fail();
			ArrayList<Player> players = state.getPlayers();
			assertTrue(players.get(1).getActive()==2);
			assertTrue(state.getLastPokerGameAction().getAction().equals(PokerAction.CHECK));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gameAction = new GameAction(0, PokerAction.CHECK);
		gameMechanics.processGameAction(gameAction);
		try {
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			if(state==null)
				fail();
			ArrayList<Player> players = state.getPlayers();
			assertTrue(players.get(0).getActive()==2);
			assertTrue(state.getLastPokerGameAction().getAction().equals(PokerAction.CHECK));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gameAction = new GameAction(1, PokerAction.CHECK);
		gameMechanics.processGameAction(gameAction);
		try {
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			if(state==null)
				fail();
			ArrayList<Player> players = state.getPlayers();
			assertTrue(players.get(1).getActive()==2);
			assertTrue(state.getLastPokerGameAction().getAction().equals(PokerAction.CHECK));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testALLIN() {
		BlockingQueue<GameState> blockingQueue = new LinkedBlockingQueue<GameState>();
		LinkedBlockingDeque<GameAction> gameActionQueue = new LinkedBlockingDeque<GameAction>();
		WatchDogTimer timer = new WatchDogTimer(gameActionQueue, 90);
		GameMechanics gameMechanics = new GameMechanics(0, 10, blockingQueue, timer, null);
		
		Player myPlayer1 = new Player(0, "testname1", 230);
		Player myPlayer2 = new Player(1, "testname2", 200);
		Player myPlayer3 = new Player(2, "testname3", 120);
		
		GameAction gameAction = new GameAction(myPlayer1, true);
		gameAction.setPosition(GameMechanics.SERVER_POSITION);
		gameMechanics.processGameAction(gameAction);
		
		gameAction = new GameAction(myPlayer2, true);
		gameAction.setPosition(GameMechanics.SERVER_POSITION);
		gameMechanics.processGameAction(gameAction);
		
		gameAction = new GameAction(myPlayer3, true);
		gameAction.setPosition(GameMechanics.SERVER_POSITION);
		gameMechanics.processGameAction(gameAction);
		
		gameAction = new GameAction(PokerAction.STARTTABLE);
		gameAction.setPosition(GameMechanics.SERVER_POSITION);
		gameMechanics.processGameAction(gameAction);
		
		//get ride of start game
		try {
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			fail();
		}
		
		gameAction = new GameAction(2, PokerAction.BET, 115);
		gameMechanics.processGameAction(gameAction);
		try{
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			if(state==null)
				fail();
			ArrayList<Player> players = state.getPlayers();
			assertTrue(players.get(2).getActive()==1);
			assertTrue(state.getLastPokerGameAction().getAction().equals(PokerAction.BET));
			//assertTrue(state.getMainPot().getTotal()==130);
			//assertTrue(state.getPlayer().getId()==2);
		} catch (Exception e) {
			fail();
		}
		
		gameAction = new GameAction(0, PokerAction.CALL, 110);
		gameMechanics.processGameAction(gameAction);
		try{
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			//assertTrue(state.getPlayer().getId()==2);
		} catch (Exception e) {
			fail();
		}
		
		gameAction = new GameAction(1, PokerAction.CALL, 120);
		gameMechanics.processGameAction(gameAction);
		try{
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			//assertTrue(state.getPlayer().getId()==2);
		} catch (Exception e) {
			fail();
		}
		
		gameAction = new GameAction(0, PokerAction.CHECK);
		gameMechanics.processGameAction(gameAction);
		try{
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			//assertTrue(state.getPlayer().getId()==2);
		} catch (Exception e) {
			fail();
		}
		
		gameAction = new GameAction(1, PokerAction.CHECK);
		gameMechanics.processGameAction(gameAction);
		try{
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			//assertTrue(state.getPlayer().getId()==2);
		} catch (Exception e) {
			fail();
		}
		gameAction = new GameAction(0, PokerAction.CHECK);
		gameMechanics.processGameAction(gameAction);
		try{
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			//assertTrue(state.getPlayer().getId()==2);
		} catch (Exception e) {
			fail();
		}
		
		try{
		gameAction = new GameAction(1, PokerAction.CHECK);
		gameMechanics.processGameAction(gameAction);
		try{
			blockingQueue.poll(1, TimeUnit.SECONDS);
			blockingQueue.poll(1, TimeUnit.SECONDS);
			GameState state  = blockingQueue.poll(2, TimeUnit.SECONDS);
			//assertTrue(state.getPlayer().getId()==2);
		} catch (Exception e) {
			fail();
		}
		} catch (NullPointerException e1) { 
			///might try to boot all in player if they fail this is okay server doesnt actually exist
		}
		
	}
	

}

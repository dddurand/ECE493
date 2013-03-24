package dataModels;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class GameTest {

	@Test
	public void emptyGameConstructor() {
		@SuppressWarnings("unused")
		Game game = new Game();
		assertTrue(true);
	}
	
	@Test
	public void gameConstructor() {
		
		GameAction action = new GameAction();
		ArrayList<GameAction> gameActions = new ArrayList<GameAction>();
		gameActions.add(action);
		
		String id = "GAME_ID";
		
		Game game = new Game(gameActions, id);

		assertTrue(game.getGameID().equals(id));
		assertTrue(game.getGameActions().equals(gameActions));

	}
	
	@Test
	public void testGetGameID() {
		
		GameAction action = new GameAction();
		ArrayList<GameAction> gameActions = new ArrayList<GameAction>();
		gameActions.add(action);
		
		String id = "GAME_ID";
		
		Game game = new Game(gameActions, id);
		assertTrue(game.getGameActions().equals(gameActions));

	}
	
	@Test
	public void testSetGameID() {
		
		GameAction action = new GameAction();
		ArrayList<GameAction> gameActions = new ArrayList<GameAction>();
		gameActions.add(action);
		
		String id2 = "GAME_ID2";
		String id = "GAME_ID";
		
		Game game = new Game(gameActions, id);
		game.setGameID(id2);
		
		assertTrue(game.getGameID().equals(id2));

	}
	
	@Test
	public void testGetGameActions() {
		
		GameAction action = new GameAction();
		ArrayList<GameAction> gameActions = new ArrayList<GameAction>();
		gameActions.add(action);
		
		String id = "GAME_ID";
		
		Game game = new Game(gameActions, id);

		ArrayList<GameAction> gameActions2 = game.getGameActions();
		
		assertTrue(gameActions2.equals(gameActions));

	}
	
	@Test
	public void testSetGameActions() {
		
		GameAction action = new GameAction();
		ArrayList<GameAction> gameActions = new ArrayList<GameAction>();
		gameActions.add(action);
		
		String id = "GAME_ID";
		
		Game game = new Game(gameActions, id);

		ArrayList<GameAction> gameActions2 = new ArrayList<GameAction>();
		gameActions2.add(action);
		gameActions2.add(action);
		gameActions2.add(action);
		
		game.setGameActions(gameActions2);
		
		ArrayList<GameAction> gameActions3 = game.getGameActions();
		
		assertTrue(gameActions3.equals(gameActions2));

	}


}

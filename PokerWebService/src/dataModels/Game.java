package dataModels;

import java.util.ArrayList;


/**
 * Data model for a set of game data
 * 
 * @author dddurand
 *
 */
public class Game {

	private String gameID;
	private ArrayList<GameAction> gameActions;
	
	/**
	 * General Constructor
	 */
	public Game() {}
	
	public Game(ArrayList<GameAction> gameActions, String gameID) 
	{
		this.gameActions = gameActions;
		this.gameID = gameID;
	}

	public String getGameID() {
		return gameID;
	}

	public void setGameID(String gameID) {
		this.gameID = gameID;
	}

	public ArrayList<GameAction> getGameActions() {
		return gameActions;
	}

	public void setGameActions(ArrayList<GameAction> gameActions) {
		this.gameActions = gameActions;
	}
	
	
	
}

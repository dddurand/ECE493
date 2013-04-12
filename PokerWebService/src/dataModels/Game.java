package dataModels;

import java.util.ArrayList;


/**
 * Data model for a set of game data
 * @SRS 3.2.2.3 -> 3.2.2.6
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
	
	/**
	 * General Constructor
	 * 
	 * @param gameActions List of actions in the game for the user
	 * @param gameID The ID of the game
	 */
	public Game(ArrayList<GameAction> gameActions, String gameID) 
	{
		this.gameActions = gameActions;
		this.gameID = gameID;
	}

	/**
	 * Getter for the game id
	 * 
	 * @return
	 */
	public String getGameID() {
		return gameID;
	}

	/**
	 * Setter for the game id
	 * 
	 * @param gameID
	 */
	public void setGameID(String gameID) {
		this.gameID = gameID;
	}

	/**
	 * Getter for game action collection
	 * @return
	 */
	public ArrayList<GameAction> getGameActions() {
		return gameActions;
	}

	/**
	 * Setter for the game action collection
	 * 
	 * @param gameActions
	 */
	public void setGameActions(ArrayList<GameAction> gameActions) {
		this.gameActions = gameActions;
	}
	
}

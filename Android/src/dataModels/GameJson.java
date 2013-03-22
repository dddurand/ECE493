package dataModels;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A general data model that is only used for passing information between the database
 * and the web server.
 * 
 * @author dddurand
 *
 */
public class GameJson {

	private String gameID;
	private ArrayList<GameActionJson> gameActions;
	
	/**
	 * General Constructor
	 * 
	 * @param gameActions List of actions in the game for the user
	 * @param gameID The ID of the game
	 */
	public GameJson(ArrayList<GameActionJson> gameActions, String gameID) 
	{
		this.gameActions = gameActions;
		this.gameID = gameID;
	}

	/**
	 * Retrieves the UUID id of the game
	 * 
	 * @return
	 */
	public String getGameID() {
		return gameID;
	}

	/**
	 * Sets the UUID of the game
	 * 
	 * @param gameID
	 */
	public void setGameID(String gameID) {
		this.gameID = gameID;
	}

	/**
	 * Retrieves the list of game actions that this game contains.
	 * 
	 * @return
	 */
	public ArrayList<GameActionJson> getGameActions() {
		return gameActions;
	}

	/**
	 * Sets the game actions this game currently contains.
	 * @param gameActions
	 */
	public void setGameActions(ArrayList<GameActionJson> gameActions) {
		this.gameActions = gameActions;
	}
	
	/**
	 * Returns the json representation of this objects game data.
	 * 
	 * @return
	 * @throws JSONException
	 */
	public JSONObject getJson() throws JSONException
	{
		JSONObject obj = new JSONObject();
		obj.put("gameID", this.gameID);
		
		JSONArray actionArray = new JSONArray();
		
		for(GameActionJson action :  this.gameActions)
		{
			actionArray.put(action.getJson());
		}
		
		obj.put("gameActions", actionArray);
		
		return obj;
	}
	
	
	
}

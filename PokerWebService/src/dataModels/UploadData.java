package dataModels;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import dataModels.GameAction.PokerAction;

/**
 * Acts as the data representation for the game & misc data uploaded to the web service.
 * This class also contains the code to convert the JSON provided to the webservice, into
 * the UploadData model.
 * 
 * @author dddurand
 *
 */
public class UploadData {

	private ArrayList<Game> games;
	private ArrayList<MiscGameData> miscDatas;

	/**
	 * General Constructor
	 */
	public UploadData() {}

	/**
	 * General Constructor
	 * 
	 * @param games The games data to be uploaded
	 * @param otherdata The misc data to be uploaded
	 */
	public UploadData(ArrayList<Game> games, ArrayList<MiscGameData> otherdata)
	{
		this.games = games;
		this.miscDatas = otherdata;
	}

	
	/**
	 * Games Getter
	 * 
	 * @return
	 */
	public ArrayList<Game> getGames() {
		return games;
	}

	/**
	 * Games Setter
	 * 
	 * @param games
	 */
	public void setGames(ArrayList<Game> games) {
		this.games = games;
	}

	/**
	 * Misc Datas Getter
	 * 
	 * @return
	 */
	public ArrayList<MiscGameData> getMiscDatas() {
		return miscDatas;
	}
	
	/**
	 * MiscDatas Setter
	 * 
	 * @param miscDatas
	 */
	public void setMiscDatas(ArrayList<MiscGameData> miscDatas) {
		this.miscDatas = miscDatas;
	}

	/**
	 * Custom Deserializer that takes the provided json,
	 * and converts it into a UploadData object.
	 * 
	 * @author dddurand
	 *
	 */
	public static class UploadDataDeserializer implements JsonDeserializer<UploadData> {

		/**
		 * Generates a game from the json data
		 * 
		 * @param gameObject
		 * @return
		 */
		private Game createGame(JsonObject gameObject)
		{
			String game_id = gameObject.get("gameID").getAsString();

			if(game_id == null || game_id.isEmpty())
				throw new JsonParseException("Invalid Game ID");

			JsonArray actions = gameObject.get("gameActions").getAsJsonArray();
			ArrayList<GameAction> actionList = createGameActionList(actions);

			Game game = new Game(actionList, game_id);

			return game;
		}

		/**
		 * Generates a game action collection from the json data
		 * 
		 * @param actions
		 * @return
		 */
		private ArrayList<GameAction> createGameActionList(JsonArray actions)
		{
			int actionsCount = actions.size();
			ArrayList<GameAction> actionList = new ArrayList<GameAction>();

			for(int j = 0; j < actionsCount; j++)
			{
				JsonObject actionObject =  actions.get(j).getAsJsonObject();

				GameAction action =  createGameAction(actionObject);

				actionList.add(action);
			}

			return actionList;
		}

		/**
		 * Generates a game action from the json data
		 * 
		 * @param actionObject
		 * @return
		 */
		private GameAction createGameAction(JsonObject actionObject)
		{
			String actionString = actionObject.get("action").getAsString();

			PokerAction pokerAction = PokerAction.getAction(actionString);

			if(pokerAction == PokerAction.UNKNOWN)
				throw new JsonParseException("Invalid game action");

			int pot = actionObject.get("pot").getAsInt();
			int bet = actionObject.get("bet").getAsInt();
			String hand = actionObject.get("hand").getAsString();
			String comm = actionObject.get("communityCards").getAsString();

			if(hand != null && hand.length() != 5 && !Card.validCardString(hand))
				throw new JsonParseException("Invalid Hand");
			if(comm != null && !comm.isEmpty() && !Card.validCardString(comm))
				throw new JsonParseException("Invalid Community Card Data");

			ArrayList<Card> handList = Card.generateCards(hand);
			ArrayList<Card> commList = Card.generateCards(comm);

			GameAction action = new GameAction(pokerAction, pot, bet, handList, commList);

			return action;
		}

		/**
		 * Generates a list of game objects from the json data
		 * 
		 * @param games
		 * @return
		 */
		private ArrayList<Game> generateGameList(JsonArray games)
		{
			int gamesCount = games.size();
			ArrayList<Game> gameList = new ArrayList<Game>();

			for(int i = 0; i < gamesCount; i++)
			{
				try{
					JsonObject gameObject =  games.get(i).getAsJsonObject();	
					Game game = createGame(gameObject);
					gameList.add(game);
				}
				catch (JsonParseException e)
				{
					gameList.add(null);
				}
			}

			return gameList;
		}

		/**
		 * Generates a collection of MiscGameData objects from the provided json
		 * 
		 * @param miscDatas
		 * @return
		 */
		private ArrayList<MiscGameData> createMiscList(JsonArray miscDatas)
		{
			int miscCount = miscDatas.size();
			ArrayList<MiscGameData> miscList = new ArrayList<MiscGameData>();

			for(int i = 0; i < miscCount; i++)
			{
				try{
					JsonObject miscObj = miscDatas.get(i).getAsJsonObject();

					String name = miscObj.get("name").getAsString();
					String value = miscObj.get("value").getAsString();


					MiscGameData miscData = new MiscGameData(name, value);
					miscList.add(miscData);
				}
				catch (JsonParseException e)
				{
					miscList.add(null);
				}
			}

			return miscList;
		}

		/**
		 * The custom serializer that converts the provided json,
		 * into a UploadData object.
		 * 
		 */
		@Override
		public UploadData deserialize(JsonElement element, Type type,
				JsonDeserializationContext context) throws JsonParseException {

			JsonObject uploadDataObj = element.getAsJsonObject();

			JsonArray games = uploadDataObj.get("games").getAsJsonArray();

			JsonArray miscDatas = uploadDataObj.get("miscDatas").getAsJsonArray();

			ArrayList<Game> gameList = generateGameList(games);
			ArrayList<MiscGameData> miscList = createMiscList(miscDatas);

			UploadData uploadData = new UploadData(gameList, miscList);

			return uploadData;
		}
	}

}

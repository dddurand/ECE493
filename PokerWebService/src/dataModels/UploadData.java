package dataModels;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import dataModels.GameAction.PokerAction;

public class UploadData {

	private ArrayList<Game> games;
	private ArrayList<MiscGameData> miscDatas;
	
	public UploadData() {}
	
	public UploadData(ArrayList<Game> games, ArrayList<MiscGameData> otherdata)
	{
		this.games = games;
		this.miscDatas = otherdata;
	}
	
	public static class UploadDataDeserializer implements JsonDeserializer<UploadData> {

		
		private Game createGame(JsonObject gameObject)
		{
			String game_id = gameObject.get("gameID").getAsString();
			
			if(game_id == null)
				throw new JsonParseException("Invalid Game ID");
			
			JsonArray actions = gameObject.get("gameActions").getAsJsonArray();
			ArrayList<GameAction> actionList = createGameActionList(actions);
			
			Game game = new Game(actionList, game_id);
			
			return game;
		}
		
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
		
		private GameAction createGameAction(JsonObject actionObject)
		{
			String actionString = actionObject.get("action").getAsString();
			
			PokerAction pokerAction = PokerAction.getAction(actionString);
			
			if(pokerAction == PokerAction.UNKNOWN)
				throw new JsonParseException("Invalid game action");
			
			int pot = actionObject.get("pot").getAsInt();
			String hand = actionObject.get("hand").getAsString();
			String comm = actionObject.get("communityCards").getAsString();

			if(hand != null && !hand.isEmpty() && !Card.validCardString(hand))
				throw new JsonParseException("Invalid Hand");
			if(comm != null && !comm.isEmpty() && !Card.validCardString(comm))
				throw new JsonParseException("Invalid Community Card Data");
			
			ArrayList<Card> handList = Card.generateCards(hand);
			ArrayList<Card> commList = Card.generateCards(comm);
			
			GameAction action = new GameAction(pokerAction, pot, handList, commList);
			
			return action;
		}
		
		private ArrayList<Game> generateGameList(JsonArray games)
		{
			int gamesCount = games.size();
			ArrayList<Game> gameList = new ArrayList<Game>();
			
			for(int i = 0; i < gamesCount; i++)
			{
				JsonObject gameObject =  games.get(i).getAsJsonObject();	
				Game game = createGame(gameObject);
				gameList.add(game);
				
			}
			
			return gameList;
		}
		
		private ArrayList<MiscGameData> createMiscList(JsonArray miscDatas)
		{
			int miscCount = miscDatas.size();
			ArrayList<MiscGameData> miscList = new ArrayList<MiscGameData>();
			
			for(int i = 0; i < miscCount; i++)
			{
				JsonObject miscObj = miscDatas.get(i).getAsJsonObject();
				
				String name = miscObj.get("name").getAsString();
				String value = miscObj.get("value").getAsString();
				
				
				MiscGameData miscData = new MiscGameData(name, value);
				miscList.add(miscData);
			}
			
			return miscList;
		}
		
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

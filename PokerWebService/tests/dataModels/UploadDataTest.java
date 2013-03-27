package dataModels;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class UploadDataTest {

	@Test
	public void constructorTest() {
		@SuppressWarnings("unused")
		UploadData data = new UploadData();
		assertTrue(true);
	}
	
	@Test
	public void fullConstructorTest() {
		
		ArrayList<Game> games = new ArrayList<Game>();
		games.add(new Game());
		
		ArrayList<MiscGameData> otherdata = new ArrayList<MiscGameData>();
		UploadData data = new UploadData(games, otherdata);
	
		ArrayList<Game> games2 = data.getGames();
		ArrayList<MiscGameData> otherdata2 = data.getMiscDatas();
		
		assertTrue(games2.equals(games));
		assertTrue(otherdata2.equals(otherdata));
	}
	
	@Test
	public void testSetGamesTest() {
		
		ArrayList<Game> games = new ArrayList<Game>();
		games.add(new Game());
		
		ArrayList<MiscGameData> otherdata = new ArrayList<MiscGameData>();
		UploadData data = new UploadData(games, otherdata);
	
		ArrayList<Game> games2 = new ArrayList<Game>();
		
		data.setGames(games2);
		
		assertTrue(data.getGames().equals(games2));
	}
	
	@Test
	public void testSetMiscTest() {
		
		ArrayList<Game> games = new ArrayList<Game>();
		games.add(new Game());
		
		ArrayList<MiscGameData> otherdata = new ArrayList<MiscGameData>();
		otherdata.add(new MiscGameData());
		
		UploadData data = new UploadData(games, otherdata);
	
		ArrayList<MiscGameData> otherdata2 = new ArrayList<MiscGameData>();
		
		data.setMiscDatas(otherdata2);
		
		assertTrue(data.getMiscDatas().equals(otherdata2));
	}
	
	@Test
	public void UploadDataDeserializerTest()
	{
		JsonObject obj = new JsonObject();
		
		JsonObject game = new JsonObject();
		game.addProperty("gameID", "asdfg");
		
		JsonArray miscArray = new JsonArray();
		
		JsonArray gameArray = new JsonArray();
		JsonArray gameActionArray = new JsonArray();
		JsonObject gameActionObj = new JsonObject();
		
		gameActionObj.addProperty("pot", 50);
		gameActionObj.addProperty("bet", 10);
		gameActionObj.addProperty("hand", "CA;C4");
		gameActionObj.addProperty("communityCards", "S2;D6;CT");
		gameActionObj.addProperty("action", "BET");
		
		gameActionArray.add(gameActionObj);
		
		game.add("gameActions", gameActionArray);
		gameArray.add(game);
		
		JsonObject miscActionObj = new JsonObject();
		miscActionObj.addProperty("name", "bob");
		miscActionObj.addProperty("value", 12);
		
		miscArray.add(miscActionObj);
		
		obj.add("games", gameArray);
		obj.add("miscDatas", miscArray);
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(UploadData.class, new UploadData.UploadDataDeserializer());
		Gson gson = gsonBuilder.create();
		
		UploadData data = gson.fromJson(obj, UploadData.class);
		
		ArrayList<Game> games2 = data.getGames();
		Game temp = games2.get(0);
		assertTrue(temp.getGameID().equals("asdfg"));
		
		ArrayList<GameAction> gameActions = temp.getGameActions();
		GameAction action = gameActions.get(0);
		
		assertTrue(action.getAction().getValue().equals("BET"));
		assertTrue(action.getBet() == 10);
		assertTrue(action.getPot() == 50);
		
		ArrayList<Card> hand = action.getHand();
		ArrayList<Card> comm = action.getCommunityCards();
		
		assertTrue(Card.getCardsAsString(hand).equals("CA;C4"));
		assertTrue(Card.getCardsAsString(comm).equals("S2;D6;CT"));
		
		ArrayList<MiscGameData> miscData = data.getMiscDatas();
		
		assertTrue(miscData.get(0).getName().equals("bob"));
		assertTrue(miscData.get(0).getValue().equals("12"));
	}
	
	@Test
	public void UploadDataDeserializerNoGameIDTest()
	{
		JsonObject obj = new JsonObject();
		
		JsonObject game = new JsonObject();
		
		JsonArray miscArray = new JsonArray();
		
		JsonArray gameArray = new JsonArray();
		JsonArray gameActionArray = new JsonArray();
		JsonObject gameActionObj = new JsonObject();
		
		gameActionObj.addProperty("pot", 50);
		gameActionObj.addProperty("bet", 10);
		gameActionObj.addProperty("hand", "CA;C4");
		gameActionObj.addProperty("communityCards", "S2;D6;CT");
		gameActionObj.addProperty("action", "BET");
		
		gameActionArray.add(gameActionObj);
		
		game.add("gameActions", gameActionArray);
		gameArray.add(game);
		
		JsonObject miscActionObj = new JsonObject();
		miscActionObj.addProperty("name", "bob");
		miscActionObj.addProperty("value", 12);
		
		miscArray.add(miscActionObj);
		
		obj.add("games", gameArray);
		obj.add("miscDatas", miscArray);
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(UploadData.class, new UploadData.UploadDataDeserializer());
		Gson gson = gsonBuilder.create();
		
		UploadData data = gson.fromJson(obj, UploadData.class);
		
		ArrayList<Game> games2 = data.getGames();
		assertTrue(games2.isEmpty());
		
	}
	
	@Test
	public void UploadDataDeserializerInvalidGameActionTest()
	{
		JsonObject obj = new JsonObject();
		
		JsonObject game = new JsonObject();
		game.addProperty("gameID", "asdfg");
		
		JsonArray miscArray = new JsonArray();
		
		JsonArray gameArray = new JsonArray();
		JsonArray gameActionArray = new JsonArray();
		JsonObject gameActionObj = new JsonObject();
		
		gameActionObj.addProperty("pot", 50);
		gameActionObj.addProperty("bet", 10);
		gameActionObj.addProperty("hand", "CA;C4");
		gameActionObj.addProperty("communityCards", "S2;D6;CT");
		gameActionObj.addProperty("action", "asdfg");
		
		gameActionArray.add(gameActionObj);
		
		game.add("gameActions", gameActionArray);
		gameArray.add(game);
		
		JsonObject miscActionObj = new JsonObject();
		miscActionObj.addProperty("name", "bob");
		miscActionObj.addProperty("value", 12);
		
		miscArray.add(miscActionObj);
		
		obj.add("games", gameArray);
		obj.add("miscDatas", miscArray);
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(UploadData.class, new UploadData.UploadDataDeserializer());
		Gson gson = gsonBuilder.create();
		
		UploadData data = gson.fromJson(obj, UploadData.class);
		
		ArrayList<Game> games2 = data.getGames();
		assertTrue(games2.isEmpty());
	}
	
	@Test
	public void UploadDataDeserializerInvalidHandTest()
	{
		JsonObject obj = new JsonObject();
		
		JsonObject game = new JsonObject();
		game.addProperty("gameID", "asdfg");
		
		JsonArray miscArray = new JsonArray();
		
		JsonArray gameArray = new JsonArray();
		JsonArray gameActionArray = new JsonArray();
		JsonObject gameActionObj = new JsonObject();
		
		gameActionObj.addProperty("pot", 50);
		gameActionObj.addProperty("bet", 10);
		gameActionObj.addProperty("hand", "C");
		gameActionObj.addProperty("communityCards", "S2;D6;CT");
		gameActionObj.addProperty("action", "BET");
		
		gameActionArray.add(gameActionObj);
		
		game.add("gameActions", gameActionArray);
		gameArray.add(game);
		
		JsonObject miscActionObj = new JsonObject();
		miscActionObj.addProperty("name", "bob");
		miscActionObj.addProperty("value", 12);
		
		miscArray.add(miscActionObj);
		
		obj.add("games", gameArray);
		obj.add("miscDatas", miscArray);
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(UploadData.class, new UploadData.UploadDataDeserializer());
		Gson gson = gsonBuilder.create();
		
		UploadData data = gson.fromJson(obj, UploadData.class);
		
		ArrayList<Game> games2 = data.getGames();
		assertTrue(games2.isEmpty());
		
	}

	@Test
	public void UploadDataDeserializerInvalidCommTest()
	{
		JsonObject obj = new JsonObject();
		
		JsonObject game = new JsonObject();
		game.addProperty("gameID", "asdfg");
		
		JsonArray miscArray = new JsonArray();
		
		JsonArray gameArray = new JsonArray();
		JsonArray gameActionArray = new JsonArray();
		JsonObject gameActionObj = new JsonObject();
		
		gameActionObj.addProperty("pot", 50);
		gameActionObj.addProperty("bet", 10);
		gameActionObj.addProperty("hand", "CA;C4");
		gameActionObj.addProperty("communityCards", "S2;D");
		gameActionObj.addProperty("action", "BET");
		
		gameActionArray.add(gameActionObj);
		
		game.add("gameActions", gameActionArray);
		gameArray.add(game);
		
		JsonObject miscActionObj = new JsonObject();
		miscActionObj.addProperty("name", "bob");
		miscActionObj.addProperty("value", 12);
		
		miscArray.add(miscActionObj);
		
		obj.add("games", gameArray);
		obj.add("miscDatas", miscArray);
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(UploadData.class, new UploadData.UploadDataDeserializer());
		Gson gson = gsonBuilder.create();
		
		UploadData data = gson.fromJson(obj, UploadData.class);
		
		ArrayList<Game> games2 = data.getGames();
		assertTrue(games2.isEmpty());
		
	}
	
	@Test
	public void UploadDataDeserializerBadMiscTest()
	{
		JsonObject obj = new JsonObject();
		
		JsonObject game = new JsonObject();
		game.addProperty("gameID", "asdfg");
		
		JsonArray miscArray = new JsonArray();
		
		JsonArray gameArray = new JsonArray();
		JsonArray gameActionArray = new JsonArray();
		JsonObject gameActionObj = new JsonObject();
		
		gameActionObj.addProperty("pot", 50);
		gameActionObj.addProperty("bet", 10);
		gameActionObj.addProperty("hand", "CA;C4");
		gameActionObj.addProperty("communityCards", "S2;D");
		gameActionObj.addProperty("action", "BET");
		
		gameActionArray.add(gameActionObj);
		
		game.add("gameActions", gameActionArray);
		gameArray.add(game);
		
		JsonObject miscActionObj = new JsonObject();
		miscActionObj.addProperty("value", 12);
		
		miscArray.add(miscActionObj);
		
		obj.add("games", gameArray);
		obj.add("miscDatas", miscArray);
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(UploadData.class, new UploadData.UploadDataDeserializer());
		Gson gson = gsonBuilder.create();
		
		UploadData data = gson.fromJson(obj, UploadData.class);
		
		ArrayList<MiscGameData> miscData = data.getMiscDatas();
		assertTrue(miscData.isEmpty());
		
	}
	
	@Test
	public void UploadDataDeserializerBadPotTest()
	{
		JsonObject obj = new JsonObject();
		
		JsonObject game = new JsonObject();
		game.addProperty("gameID", "asdfg");
		
		JsonArray miscArray = new JsonArray();
		
		JsonArray gameArray = new JsonArray();
		JsonArray gameActionArray = new JsonArray();
		JsonObject gameActionObj = new JsonObject();
		
		gameActionObj.addProperty("bet", 10);
		gameActionObj.addProperty("hand", "CA;C4");
		gameActionObj.addProperty("communityCards", "S2;D6;CT");
		gameActionObj.addProperty("action", "BET");
		
		gameActionArray.add(gameActionObj);
		
		game.add("gameActions", gameActionArray);
		gameArray.add(game);
		
		JsonObject miscActionObj = new JsonObject();
		miscActionObj.addProperty("name", "bob");
		miscActionObj.addProperty("value", 12);
		
		miscArray.add(miscActionObj);
		
		obj.add("games", gameArray);
		obj.add("miscDatas", miscArray);
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(UploadData.class, new UploadData.UploadDataDeserializer());
		Gson gson = gsonBuilder.create();
		
		UploadData data = gson.fromJson(obj, UploadData.class);
		
		ArrayList<Game> games2 = data.getGames();
		assertTrue(games2.isEmpty());
	}
	
	@Test
	public void UploadDataDeserializerBadBetTest()
	{
		JsonObject obj = new JsonObject();
		
		JsonObject game = new JsonObject();
		game.addProperty("gameID", "asdfg");
		
		JsonArray miscArray = new JsonArray();
		
		JsonArray gameArray = new JsonArray();
		JsonArray gameActionArray = new JsonArray();
		JsonObject gameActionObj = new JsonObject();
		
		gameActionObj.addProperty("pot", 50);
		gameActionObj.addProperty("hand", "CA;C4");
		gameActionObj.addProperty("communityCards", "S2;D6;CT");
		gameActionObj.addProperty("action", "BET");
		
		gameActionArray.add(gameActionObj);
		
		game.add("gameActions", gameActionArray);
		gameArray.add(game);
		
		JsonObject miscActionObj = new JsonObject();
		miscActionObj.addProperty("name", "bob");
		miscActionObj.addProperty("value", 12);
		
		miscArray.add(miscActionObj);
		
		obj.add("games", gameArray);
		obj.add("miscDatas", miscArray);
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(UploadData.class, new UploadData.UploadDataDeserializer());
		Gson gson = gsonBuilder.create();
		
		UploadData data = gson.fromJson(obj, UploadData.class);
		
		ArrayList<Game> games2 = data.getGames();
		assertTrue(games2.isEmpty());
	}
	
	@Test
	public void UploadDataDeserializerBadHandTest()
	{
		JsonObject obj = new JsonObject();
		
		JsonObject game = new JsonObject();
		game.addProperty("gameID", "asdfg");
		
		JsonArray miscArray = new JsonArray();
		
		JsonArray gameArray = new JsonArray();
		JsonArray gameActionArray = new JsonArray();
		JsonObject gameActionObj = new JsonObject();
		
		gameActionObj.addProperty("pot", 50);
		gameActionObj.addProperty("bet", 10);
		gameActionObj.addProperty("communityCards", "S2;D6;CT");
		gameActionObj.addProperty("action", "BET");
		
		gameActionArray.add(gameActionObj);
		
		game.add("gameActions", gameActionArray);
		gameArray.add(game);
		
		JsonObject miscActionObj = new JsonObject();
		miscActionObj.addProperty("name", "bob");
		miscActionObj.addProperty("value", 12);
		
		miscArray.add(miscActionObj);
		
		obj.add("games", gameArray);
		obj.add("miscDatas", miscArray);
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(UploadData.class, new UploadData.UploadDataDeserializer());
		Gson gson = gsonBuilder.create();
		
		UploadData data = gson.fromJson(obj, UploadData.class);
		
		ArrayList<Game> games2 = data.getGames();
		assertTrue(games2.isEmpty());
	}
	
	@Test
	public void UploadDataDeserializerBadCommTest()
	{
		JsonObject obj = new JsonObject();
		
		JsonObject game = new JsonObject();
		game.addProperty("gameID", "asdfg");
		
		JsonArray miscArray = new JsonArray();
		
		JsonArray gameArray = new JsonArray();
		JsonArray gameActionArray = new JsonArray();
		JsonObject gameActionObj = new JsonObject();
		
		gameActionObj.addProperty("pot", 50);
		gameActionObj.addProperty("bet", 10);
		gameActionObj.addProperty("hand", "CA;C4");
		gameActionObj.addProperty("action", "BET");
		
		gameActionArray.add(gameActionObj);
		
		game.add("gameActions", gameActionArray);
		gameArray.add(game);
		
		JsonObject miscActionObj = new JsonObject();
		miscActionObj.addProperty("name", "bob");
		miscActionObj.addProperty("value", 12);
		
		miscArray.add(miscActionObj);
		
		obj.add("games", gameArray);
		obj.add("miscDatas", miscArray);
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(UploadData.class, new UploadData.UploadDataDeserializer());
		Gson gson = gsonBuilder.create();
		
		UploadData data = gson.fromJson(obj, UploadData.class);
		
		ArrayList<Game> games2 = data.getGames();
		assertTrue(games2.isEmpty());
	}
	
	@Test
	public void UploadDataDeserializerBadValueTest()
	{
		JsonObject obj = new JsonObject();
		
		JsonObject game = new JsonObject();
		game.addProperty("gameID", "asdfg");
		
		JsonArray miscArray = new JsonArray();
		
		JsonArray gameArray = new JsonArray();
		JsonArray gameActionArray = new JsonArray();
		JsonObject gameActionObj = new JsonObject();
		
		gameActionObj.addProperty("pot", 50);
		gameActionObj.addProperty("bet", 10);
		gameActionObj.addProperty("hand", "CA;C4");
		gameActionObj.addProperty("action", "BET");
		
		gameActionArray.add(gameActionObj);
		
		game.add("gameActions", gameActionArray);
		gameArray.add(game);
		
		JsonObject miscActionObj = new JsonObject();
		miscActionObj.addProperty("name", "bob");
		
		miscArray.add(miscActionObj);
		
		obj.add("games", gameArray);
		obj.add("miscDatas", miscArray);
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(UploadData.class, new UploadData.UploadDataDeserializer());
		Gson gson = gsonBuilder.create();
		
		UploadData data = gson.fromJson(obj, UploadData.class);
		
		ArrayList<MiscGameData> miscData = data.getMiscDatas();
		assertTrue(miscData.isEmpty());
	}
	
	@Test
	public void UploadDataDeserializerBadGameActionTest()
	{
		JsonObject obj = new JsonObject();
		
		JsonObject game = new JsonObject();
		game.addProperty("gameID", "asdfg");
		
		JsonArray miscArray = new JsonArray();
		
		JsonArray gameArray = new JsonArray();
	
		gameArray.add(game);
		
		JsonObject miscActionObj = new JsonObject();
		miscActionObj.addProperty("name", "bob");
		miscActionObj.addProperty("value", 12);
		
		miscArray.add(miscActionObj);
		
		obj.add("games", gameArray);
		obj.add("miscDatas", miscArray);
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(UploadData.class, new UploadData.UploadDataDeserializer());
		Gson gson = gsonBuilder.create();
		
		UploadData data = gson.fromJson(obj, UploadData.class);
		
		ArrayList<Game> games2 = data.getGames();
		assertTrue(games2.isEmpty());
	}
	
}

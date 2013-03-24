package server;

import game.GameMechanics;
import game.Player;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.util.Hashtable;
import java.util.concurrent.LinkedBlockingDeque;

import server.GameAction.PokerAction;

import android.app.Activity;

/**
 * Main server architecture.
 * 
 * This object facilitates the various listeners and broadcasts of the service.
 * 
 * 
 * @author dddurand
 *
 */
public class Server implements PlayerTaskListener {

	private PokerEngineTask gameEngineTask;
	private ServerBroadCaster gameBroadCaster;
	
	private LinkedBlockingDeque<GameAction> gameActionQueue;
	private LinkedBlockingDeque<GameState> gameBroadCastQueue;
	
	private Activity activity;
	private Hashtable<Integer, ServerClientListener> playerListenerTasks;
	
	private Player serverPlayer;
	
	/**
	 * General Constructor
	 * @param activity
	 */
	public Server(Activity activity)
	{
		this.activity = activity;
		this.serverPlayer = new Player(GameMechanics.SERVER_POSITION, "", 0);
		initialize();
	}
	
	/**
	 * Initializes all the various parts needed by the server
	 * This includes the various queues, threads, listeners, and tasks.
	 */
	private void initialize()
	{
		
		
		gameBroadCastQueue = new LinkedBlockingDeque<GameState>();
		
		gameActionQueue = new LinkedBlockingDeque<GameAction>();
		playerListenerTasks = new Hashtable<Integer, ServerClientListener>();
		
		WatchDogTimer playTimer = new WatchDogTimer(gameActionQueue, 10);
		
		GameMechanics gameEngine = new GameMechanics( 0, 30, gameBroadCastQueue, playTimer);
		
		gameEngineTask = new PokerEngineTask(gameActionQueue, gameEngine);
		gameBroadCaster = new ServerBroadCaster(gameBroadCastQueue, activity);
		
		Thread gameEngineThread = new Thread(gameEngineTask);
		gameEngineThread.start();
		
		gameBroadCaster.addListener(this);
		
		Thread gameBroadCasterThread = new Thread(gameBroadCaster);
		gameBroadCasterThread.start();
	}
	
	/**
	 * Causes an new player to be added to the server.
	 * 
	 * @param player The new player to be added
	 * @param inStream The associated inputStream
	 * @param outStream The associated outputStream
	 */
	public void addPlayer(Player player, InputStream inStream, OutputStream outStream)
	{
		
		try {
			ServerClientListener task = new ServerClientListener(inStream, gameActionQueue, player, activity);
			playerListenerTasks.put(player.getId(), task);
			gameBroadCaster.addPlayer(player, outStream);
			
			task.addListener(this);
			
			GameAction addPlayerAction = new GameAction(player, true);
			addPlayerAction.setPosition(this.serverPlayer.getId());
			gameActionQueue.add(addPlayerAction);
			
			Thread clientThread = new Thread(task);
			clientThread.start();
		} catch (StreamCorruptedException e) {
			return;
		} catch (IOException e) {
			return;
		}
		
	}
	
	/**
	 * Informs the server that the game should stop.
	 * 
	 */
	public void gameStop()
	{
		
		//Generate game action to remove player
		GameAction endGameAction = new GameAction(PokerAction.STOPTABLE);
		endGameAction.setPlayer(this.serverPlayer);
		endGameAction.setPosition(this.serverPlayer.getId());
		gameActionQueue.add(endGameAction);

	}
	
	/**
	 * Informs the server that the game should start.
	 */
	public void gameStart()
	{
		
		//Generate game action to remove player
		GameAction startGameAction = new GameAction(PokerAction.STARTTABLE);
		startGameAction.setPlayer(this.serverPlayer);
		startGameAction.setPosition(this.serverPlayer.getId());
		gameActionQueue.add(startGameAction);
		
		
	}
	
	/**
	 * Informs the server that a player should be removed from the server.
	 * @param player
	 */
	public void removePlayer(Player player)
	{
		
		if(playerListenerTasks.contains(player))
		{
		//Generate game action to remove player
		GameAction removePlayerAction = new GameAction(player, false);
		removePlayerAction.setPlayer(this.serverPlayer);
		removePlayerAction.setPosition(this.serverPlayer.getId());
		
		ServerClientListener task = playerListenerTasks.remove(player);
		task.cancel();
		gameActionQueue.add(removePlayerAction);
		gameBroadCaster.removePlayer(player);
		}
		
	}
	
	

	/**
	 * Called when a player task has closed.
	 */
	@Override
	public void onPlayerTaskClose(Player player) {	
		this.removePlayer(player);
	}
	
	/**
	 * Test Method
	 * @param player
	 */
	public void testState(Player player)
	{
		//GameState test = new GameState(43);
		//test.setPlayer(player);
		//gameBroadCastQueue.add(test);
	}
	

	
	
}

package server;

import game.GameMechanics;
import game.Player;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.util.Hashtable;
import java.util.concurrent.LinkedBlockingDeque;

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
	private Hashtable<Player, ServerClientListener> playerListenerTasks;
	
	/**
	 * General Constructor
	 * @param activity
	 */
	public Server(Activity activity)
	{
		this.activity = activity;
		initialize();
	}
	
	/**
	 * Initializes all the various parts needed by the server
	 * This includes the various queues, threads, listeners, and tasks.
	 */
	private void initialize()
	{
		GameMechanics gameEngine = new GameMechanics(new Player[0], 0, 20);
		gameActionQueue = new LinkedBlockingDeque<GameAction>();
		playerListenerTasks = new Hashtable<Player, ServerClientListener>();
		gameBroadCastQueue = new LinkedBlockingDeque<GameState>();
		
		
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
			playerListenerTasks.put(player, task);
			gameBroadCaster.addPlayer(player, outStream);
			
			task.addListener(this);
			
			//GameAction removePlayerAction = new GameAction();
			//gameActionQueue.add(removePlayerAction);
			
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
		GameAction endGameAction = new GameAction();
		gameActionQueue.add(endGameAction);

	}
	
	/**
	 * Informs the server that the game should start.
	 */
	public void gameStart()
	{
		
		//Generate game action to remove player
		GameAction startGameAction = new GameAction();
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
		GameAction removePlayerAction = new GameAction();
		
		
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
		GameState test = new GameState(43);
		test.setPlayer(player);
		gameBroadCastQueue.add(test);
	}
	

	
	
}

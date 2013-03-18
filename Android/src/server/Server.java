package server;

import game.GameMechanics;
import game.Player;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.util.Hashtable;
import java.util.concurrent.LinkedBlockingDeque;

import server.ServerClientListener.PlayerTaskListener;
import android.app.Activity;

public class Server implements PlayerTaskListener {

	private PokerEngineTask gameEngineTask;
	private ServerBroadCaster gameBroadCaster;
	
	private LinkedBlockingDeque<GameAction> gameActionQueue;
	private LinkedBlockingDeque<GameState> gameBroadCastQueue;
	
	private Activity activity;
	
	
	private Hashtable<Player, ServerClientListener> playerListenerTasks;
	
	public Server(Activity activity)
	{
		this.activity = activity;
		initialize();
	}
	
	private void initialize()
	{
		GameMechanics gameEngine = new GameMechanics(new Player[0], 0, 20);
		gameActionQueue = new LinkedBlockingDeque<GameAction>();
		playerListenerTasks = new Hashtable<Player, ServerClientListener>();
		gameBroadCastQueue = new LinkedBlockingDeque<GameState>();
		
		gameEngineTask = new PokerEngineTask(gameActionQueue, gameEngine);
		gameBroadCaster = new ServerBroadCaster(gameBroadCastQueue, activity);
		
		gameEngineTask.run();
		
		gameBroadCaster.addListener(this);
		gameBroadCaster.run();
	}
	
	public void addPlayer(Player player, InputStream inStream, OutputStream outStream)
	{
		
		try {
			ServerClientListener task = new ServerClientListener(inStream, gameActionQueue, player, activity);
			playerListenerTasks.put(player, task);
			gameBroadCaster.addPlayer(player, outStream);
			
			task.addListener(this);
			task.run();
		} catch (StreamCorruptedException e) {
			return;
		} catch (IOException e) {
			return;
		}
		
	}
	
	public void gameStop()
	{
		
		//Generate game action to remove player
		GameAction endGameAction = new GameAction();
		gameActionQueue.add(endGameAction);

	}
	
	public void gameStart()
	{
		
		//Generate game action to remove player
		GameAction startGameAction = new GameAction();
		gameActionQueue.add(startGameAction);
		
		
	}
	
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
	
	

	@Override
	public void onPlayerTaskClose(Player player) {	
		this.removePlayer(player);
	}
	
	
	
}

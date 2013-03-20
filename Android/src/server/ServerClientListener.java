package server;

import game.Player;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.concurrent.BlockingQueue;

import android.app.Activity;
import android.util.Log;

public class ServerClientListener extends ListenableThread<PlayerTaskListener> {

	private ObjectInputStream objectInputStream;
	private boolean cancelled = false;
	private boolean userCancelled = false;
	private BlockingQueue<GameAction> queue;
	private Player player;
	
	/**
	 * 
	 * A single runnable that waits for communication from clients.
	 * 
	 * @param inStream The inputstream for a given player
	 * @param queue The queue of GameActions where recieved gameActions will be placed
	 * @param player The player that this thread is for.
	 * @param activity The activity to be used as context.
	 * @throws StreamCorruptedException
	 * @throws IOException
	 */
	public ServerClientListener(InputStream inStream, BlockingQueue<GameAction> queue, Player player, Activity activity) 
			throws StreamCorruptedException, IOException
	{
		super(activity);
		objectInputStream = new ObjectInputStream(inStream);
		this.queue = queue;
		this.player = player;
	}

	/**
	 * The method that continually waits for new GameActions from clients, and adds them to the game mechanics.
	 */
	@Override
	public void run() {
		
		while(!cancelled && !userCancelled)
		{
			try {
				GameAction gameAction = (GameAction) objectInputStream.readObject();
				this.queue.add(gameAction);
	
			} catch (OptionalDataException e) {
				Log.e("Optional Data Exception:", "ServerClientListener");
				this.cancelled = true;
			} catch (ClassNotFoundException e) {
				Log.e("ClassNotFoundException:", "ServerClientListener");
				this.cancelled = true;
			} catch (IOException e) {
				Log.e("IOException", "ServerClientListener");
				this.cancelled = true;
			}
		}
		
		PlayerTaskCompleteNotify runnable = new PlayerTaskCompleteNotify(player);
		informListeners(runnable);
		
		try {
			objectInputStream.close();
		} catch (IOException e) {}
		
	}
	
	/**
	 * Causes the thread to cancel.
	 */
	public void cancel()
	{
		this.cancelled = true;
	}
	
	
	

	
}

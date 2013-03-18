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

public class ServerClientListener extends ListenableThread {

	private ObjectInputStream objectInputStream;
	private boolean cancelled = false;
	private boolean userCancelled = false;
	private BlockingQueue<GameAction> queue;
	private Player player;
	
	public ServerClientListener(InputStream inStream, BlockingQueue<GameAction> queue, Player player, Activity activity) 
			throws StreamCorruptedException, IOException
	{
		super(activity);
		objectInputStream = new ObjectInputStream(inStream);
		this.queue = queue;
		this.player = player;
	}

	@Override
	public void run() {
		
		while(!cancelled || !userCancelled)
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
		
	}
	
	public void cancel()
	{
		this.cancelled = true;
	}
	
	public interface PlayerTaskListener {
		public void onPlayerTaskClose(Player player);

	}
	
	
	

	
}

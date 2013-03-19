package server;

import game.Player;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.concurrent.BlockingQueue;

import android.app.Activity;

public class ServerBroadCaster extends ListenableThread<PlayerTaskListener> {

	private final BlockingQueue<GameState> queue;
	private final Hashtable<Player, ObjectOutputStream> players;
	private boolean canceled = false;
	
	public ServerBroadCaster(BlockingQueue<GameState> queue, Activity activity)
	{
		super(activity);
		this.queue = queue;
		players = new Hashtable<Player, ObjectOutputStream>();
	}
	
	public void addPlayer(Player player, OutputStream stream) throws IOException
	{
		ObjectOutputStream objStream = new ObjectOutputStream(stream);
		
		synchronized (this) {
			players.put(player, objStream);
		}
	}
	
	public void removePlayer(Player player)
	{
		synchronized (this) {
			players.remove(player);
		}
	}
	
	public void cancel()
	{
		this.canceled = true;
	}
	
	
	@Override
	public void run() 
	{
			ObjectOutputStream stream;
			while(!canceled)
			{
				GameState state = queue.remove();
				synchronized (this) 
				{
					Player player = state.getPlayer();
					stream = players.get(player);
					
					try {
						stream.writeObject(state);
					} catch (IOException e) {
						PlayerTaskCompleteNotify runnable = new PlayerTaskCompleteNotify(player);
						informListeners(runnable);
					}
					
				}
				
				
			}
	}

}

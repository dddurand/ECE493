package server;

import game.Player;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import android.app.Activity;

/**
 * The Main thread that handles all communications to the clients.
 * The object continually waits for new GameActions, and sends them to a corresponding client.
 * 
 * @author dddurand
 *
 */
public class ServerBroadCaster extends ListenableThread<PlayerTaskListener> {

	private final BlockingQueue<GameState> queue;
	private final Hashtable<Integer, ObjectOutputStream> players;
	private boolean canceled = false;
	private TimeUnit timeUnit = TimeUnit.SECONDS;
	private long time = 5;
	
	/**
	 * General Constructor
	 * @param queue The queue of GameActions to wait on.
	 * @param activity The activity that is used for context
	 */
	public ServerBroadCaster(BlockingQueue<GameState> queue, Activity activity)
	{
		super(activity);
		this.queue = queue;
		players = new Hashtable<Integer, ObjectOutputStream>();
	}
	
	/**
	 * Adds a player to people to broadcast to.
	 * 
	 * @param player The player to broadcast to.
	 * @param stream The players associated output stream
	 * @throws IOException
	 */
	public void addPlayer(Player player, ObjectOutputStream stream) throws IOException
	{
		synchronized (this) {
			players.put(player.getId(), stream);
		}
	}
	
	/**
	 * Removes a given player from the broadcast object
	 * 
	 * @param player
	 */
	public void removePlayer(Player player)
	{
		synchronized (this) {
			ObjectOutputStream stream = players.remove(player.getId());
			
			if(stream == null) return;
			
			try{
				stream.close();
			} catch (Exception e)
			{}
			
		}
	}
	
	/**
	 * Causes the broadcast thread to terminate
	 */
	public void cancel()
	{
		this.canceled = true;
		
		for(Integer playerID : players.keySet())
		{
			ObjectOutputStream stream = players.get(playerID);
			if(stream == null) continue;
			
			try
			{
				stream.close();
			} catch (Exception e){}
			
			
		}
		
	}
	
	/**
	 * The method that continually waits for new GameActions. Once a new one is recieved
	 * it is sent to the corresponding client.
	 */
	@Override
	public void run() 
	{
			ObjectOutputStream stream;
			while(!canceled)
			{
				
				GameState state;
				try {
					state = queue.poll(this.time, this.timeUnit);
					if(state == null) continue;
				} catch (InterruptedException e1) {
					continue;
				}
				
				synchronized (this) 
				{
					Player player = state.getPlayer();
					
					if(player == null) continue;
					
					stream = players.get(player.getId());
					
					if(stream == null)
					{
						PlayerTaskCompleteNotify runnable = new PlayerTaskCompleteNotify(player);
						informListeners(runnable);
						continue;
					}
					
					try {
						stream.writeObject(state);
						stream.flush();
						stream.reset();
					} catch (IOException e) {
						PlayerTaskCompleteNotify runnable = new PlayerTaskCompleteNotify(player);
						informListeners(runnable);
						
						try{
							stream.close();
						} catch(Exception f){}
						
					}
					
				}
				
				
			}
			
	}

}

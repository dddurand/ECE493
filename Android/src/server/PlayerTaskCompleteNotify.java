package server;

import game.Player;
import server.ListenableThread.ListenerRunnable;

/**
 * A specialized ListenerRunnable that is used for when a PlayerTask in the server completes.
 * 
 * 
 * @author dddurand
 *
 */
public class PlayerTaskCompleteNotify implements ListenerRunnable<PlayerTaskListener>
{

	private PlayerTaskListener listener;
	private Player player;
	
	/**
	 * General Constructor
	 * @param player
	 */
	public PlayerTaskCompleteNotify(Player player)
	{
	this.player = player;
	}
	
	/**
	 * Sets the listener associated with this runnable, which will be run
	 * when the attached runnable stops.
	 * 
	 */
	public void setListener(PlayerTaskListener listener)
	{
		this.listener = listener;
	}
	
	/**
	 * Calls the listeners OnPlayerTaskClose method
	 */
	@Override
	public void run() {
		listener.onPlayerTaskClose(player);
		
	}

}

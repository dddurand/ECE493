package server;

import game.Player;
import server.ListenableThread.ListenerRunnable;

public class PlayerTaskCompleteNotify implements ListenerRunnable<PlayerTaskListener>
{

	private PlayerTaskListener listener;
	private Player player;
	
	public PlayerTaskCompleteNotify(Player player)
	{
	this.player = player;
	}
	
	public void setListener(PlayerTaskListener listener)
	{
		this.listener = listener;
	}
	
	@Override
	public void run() {
		listener.onPlayerTaskClose(player);
		
	}

}

package server;

import game.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;

import server.ServerClientListener.PlayerTaskListener;

public abstract class ListenableThread implements Runnable {

	private List<PlayerTaskListener> listeners = Collections.synchronizedList( new ArrayList<PlayerTaskListener>() );
	private Activity activity;
	
	public ListenableThread(Activity activity)
	{
		this.activity = activity;
	}
	
	public void addListener( PlayerTaskListener listener ){
		listeners.add(listener);
	}
	
	public void removeListener( PlayerTaskListener listener ){
		listeners.remove(listener);
	}
	
	public void informListeners(ListenerRunnable runnable) {

		synchronized ( listeners ){
			for (PlayerTaskListener listener : listeners) 
			{
				runnable.setListener(listener);
				//ListenerNotifyRunnable runnable = new ListenerNotifyRunnable(listener);
				activity.runOnUiThread(runnable);
			}		
						
		}
	}
	
	interface ListenerRunnable extends Runnable {
		public void setListener(PlayerTaskListener listener);
	}
	
	public class PlayerTaskCompleteNotify implements ListenerRunnable
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
	
}

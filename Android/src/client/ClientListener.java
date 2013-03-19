package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

import server.GameState;
import server.ListenableThread;
import android.util.Log;

import com.example.bluetoothpoker.PlayingArea;

public class ClientListener extends ListenableThread<ClientTaskListener> {

	private ObjectInputStream objectInputStream;
	private boolean cancelled = false;
	private boolean userCancelled = false;
	private PlayingArea activity;
	
	public ClientListener(InputStream inStream, PlayingArea activity) 
			throws StreamCorruptedException, IOException
	{
		super(activity);
		this.activity = activity;
	}

	@Override
	public void run() {
		
		while(!cancelled || !userCancelled)
		{
			try {
				GameState gameState = (GameState) objectInputStream.readObject();

				UpdateGUIRunnable guiUpdate = new UpdateGUIRunnable(gameState, activity);
				this.activity.runOnUiThread(guiUpdate);
	
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
		
		ClientListenerTaskCompleteNotify runnable = new ClientListenerTaskCompleteNotify();
		informListeners(runnable);
		
	}
	
	public void cancel()
	{
		this.cancelled = true;
	}
	
	private class UpdateGUIRunnable implements Runnable
	{
		private PlayingArea activity;
		private GameState state;
		
		public UpdateGUIRunnable(GameState state, PlayingArea activity)
		{
			this.state = state;
			this.activity = activity;
		}
		
		@Override
		public void run() {
			this.activity.updateAll(state);
		}
		
	}
	
	
}

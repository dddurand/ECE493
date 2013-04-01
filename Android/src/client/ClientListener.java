package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

import server.GameState;
import server.ListenableThread;
import android.util.Log;

import com.example.bluetoothpoker.PlayingArea;

/**
 * This runnable continually waits on the provided input stream,
 * and attempts to read game state objects.
 * 
 * When a game state is retrieved, the activity is informed and updated.
 * 
 * @author dddurand
 *
 */
public class ClientListener extends ListenableThread<ClientTaskListener> {

	private ObjectInputStream objectInputStream;
	private boolean cancelled = false;
	private boolean userCancelled = false;
	private PlayingArea activity;
	
	/**
	 * General Constructor
	 * 
	 * @param inStream The input stream to continually read on.
	 * @param activity The activity to be updated when a game state object is recieved
	 * @throws StreamCorruptedException
	 * @throws IOException
	 */
	public ClientListener(ObjectInputStream inStream, PlayingArea activity) 
			throws StreamCorruptedException, IOException
	{
		super(activity);
		this.activity = activity;
		this.objectInputStream = inStream;
	}

	/**
	 * The workhorse of the runnable, that blocks on the stream.
	 * Upon receiving a new game state a new update runnable is created
	 * and given to the activity.
	 * 
	 */
	@Override
	public void run() {
		
		while(!cancelled && !userCancelled)
		{
			try {
				GameState gameState = (GameState) objectInputStream.readObject();

				UpdateGUIRunnable guiUpdate = new UpdateGUIRunnable(gameState, activity);
				this.activity.runOnUiThread(guiUpdate);
	
			} catch (OptionalDataException e) {
				Log.e("Optional Data Exception:", "ClientListener");
				this.cancelled = true;
			} catch (ClassNotFoundException e) {
				Log.e("ClassNotFoundException:", "ClientListener");
				this.cancelled = true;
			} catch (IOException e) {
				Log.e("IOException", "ClientListener");
				this.cancelled = true;
			}
		}
		
		/**
		 * Notify listeners that the runnable has stopped.
		 */
		ClientListenerTaskCompleteNotify runnable = new ClientListenerTaskCompleteNotify();
		informListeners(runnable);
		
		try {
			objectInputStream.close();
		} catch (IOException e) {}
		
	}
	
	/**
	 * Causes the runnable to terminate.
	 */
	public void cancel()
	{
		this.cancelled = true;
	}
	
	/**
	 * A GUI Runnable that thats an update function in the activity with a provided
	 * GameState object.
	 * 
	 * @author dddurand
	 *
	 */
	private class UpdateGUIRunnable implements Runnable
	{
		private PlayingArea activity;
		private GameState state;
		
		/**
		 * General Constructor
		 * @param state The GameState to be given to the activity to update the GUI.
		 * @param activity The Activity to be updated.
		 */
		public UpdateGUIRunnable(GameState state, PlayingArea activity)
		{
			this.state = state;
			this.activity = activity;
		}
		
		/**
		 * Calls the update method on the activity.
		 */
		@Override
		public void run() {
			this.activity.updateAll(state);
		}
		
	}
	
	
}

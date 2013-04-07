package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;

/**
 * An extension of runnable that is used to track and informs users when the extended runnable
 * has completed.
 * 
 * 
 * @author dddurand
 *
 * @param <T> The type of listener that will be associated with the object
 */
public abstract class ListenableThread<T extends TaskListener> implements Runnable {

	private List<T> listeners = Collections.synchronizedList( new ArrayList<T>() );
	private Activity activity;
	
	public ListenableThread(Activity activity)
	{
		this.activity = activity;
	}
	
	public void addListener( T listener ){
		listeners.add(listener);
	}
	
	public void removeListener( T listener ){
		listeners.remove(listener);
	}
	
	public void informListeners(ListenerRunnable<T> runnable) {

		synchronized ( listeners ){
			for (T listener : listeners) 
			{
				runnable.setListener(listener);
				activity.runOnUiThread(runnable);
			}		
						
		}
	}
	
	/**
	 * This must be extended by the listenerRunnable with the appropriate listener attached.
	 * 
	 * @author dddurand
	 *
	 * @param <Z>
	 */
	public interface ListenerRunnable<Z extends TaskListener> extends Runnable {
		public void setListener(Z listener);
	}
	
}

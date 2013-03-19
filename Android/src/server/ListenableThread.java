package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;

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
				//ListenerNotifyRunnable runnable = new ListenerNotifyRunnable(listener);
				activity.runOnUiThread(runnable);
			}		
						
		}
	}
	
	public interface ListenerRunnable<Z extends TaskListener> extends Runnable {
		public void setListener(Z listener);
	}
	
}

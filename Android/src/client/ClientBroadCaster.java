package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import server.GameAction;
import server.ListenableThread;
import android.app.Activity;

/**
 * A consumer that takes GameActions off of the queue, as they become avaliable,
 * and sends them on the provided output stream.
 * 
 * @author dddurand
 *
 */
public class ClientBroadCaster extends ListenableThread<ClientTaskListener> {

	private final BlockingQueue<GameAction> queue;
	private boolean canceled = false;
	private ObjectOutputStream objStream;
	private TimeUnit timeUnit = TimeUnit.SECONDS;
	private long time = 5;
	
	/**
	 * General Constructor
	 * 
	 * @param queue The queue that the consumer will block on.
	 * @param activity The activity that will be used for context purposes.
	 * @param stream The stream that all data will be output on.
	 * @throws IOException
	 */
	public ClientBroadCaster(BlockingQueue<GameAction> queue, Activity activity, ObjectOutputStream stream) throws IOException
	{
		super(activity);
		this.queue = queue;
		this.objStream = stream;
		objStream.flush();
	}
	
	/**
	 * Causes this thread to terminate.
	 * 
	 */
	public void cancel()
	{
		this.canceled = true;
	}
	
	/**
	 * The workhorse of the function.
	 * This function waits on the queue for gameaction from the GUI.
	 * Every five seconds the queue fails, and restarts. This is to allow the checking
	 * of the cancelled variable.
	 * 
	 */
	@Override
	public void run() 
	{
			while(!canceled)
			{
				GameAction state;
				try {
					state = queue.poll(time, timeUnit);
					if(state == null) continue;
				} catch (InterruptedException e1) {
					continue;
				}
				
				synchronized (this) 
				{
					try {
						objStream.writeObject(state);
					} catch (IOException e) {
						ClientListenerTaskCompleteNotify runnable = new ClientListenerTaskCompleteNotify();
						informListeners(runnable);
					}				
				}		
			}
			
			/**
			 * Notify the client object that the task stopped.
			 */
			ClientListenerTaskCompleteNotify runnable = new ClientListenerTaskCompleteNotify();
			informListeners(runnable);
	}

}

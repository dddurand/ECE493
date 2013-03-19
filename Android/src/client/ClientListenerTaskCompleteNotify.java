package client;

import server.ListenableThread.ListenerRunnable;

/**
 * A Client task listener that calls the onPlayerTaskClose method
 * on a listener when the runnable that this is attached to stops.
 * 
 * 
 * @author dddurand
 *
 */
public class ClientListenerTaskCompleteNotify implements ListenerRunnable<ClientTaskListener>
{
	private ClientTaskListener listener;
	
	/**
	 * General Constructor
	 */
	public void setListener(ClientTaskListener listener)
	{
		this.listener = listener;
	}
	
	/**
	 * Call
	 */
	@Override
	public void run() {
		listener.onPlayerTaskClose();
		
	}
	
	/**
	 * An extended interface for TaskListenerRunnables
	 * that is specific for ClientTaskListener
	 * 
	 * @author dddurand
	 *
	 */
	public interface ClientListenerRunnable extends Runnable {
		public void setListener(ClientTaskListener listener);
	}

}

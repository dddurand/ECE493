package client;

import server.ListenableThread.ListenerRunnable;

public class ClientListenerTaskCompleteNotify implements ListenerRunnable<ClientTaskListener>
{
	private ClientTaskListener listener;
	
	public void setListener(ClientTaskListener listener)
	{
		this.listener = listener;
	}
	
	@Override
	public void run() {
		listener.onPlayerTaskClose();
		
	}
	
	public interface ClientListenerRunnable extends Runnable {
		public void setListener(ClientTaskListener listener);
	}

}

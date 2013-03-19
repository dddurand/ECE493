package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

import server.GameAction;
import server.ListenableThread;
import android.app.Activity;

public class ClientBroadCaster extends ListenableThread<ClientTaskListener> {

	private final BlockingQueue<GameAction> queue;
	private boolean canceled = false;
	private ObjectOutputStream objStream;
	
	public ClientBroadCaster(BlockingQueue<GameAction> queue, Activity activity, OutputStream stream) throws IOException
	{
		super(activity);
		this.queue = queue;
		this.objStream = new ObjectOutputStream(stream);
	}
	
	public void cancel()
	{
		this.canceled = true;
	}
	
	
	@Override
	public void run() 
	{
			while(!canceled)
			{
				GameAction state = queue.remove();
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
			
			ClientListenerTaskCompleteNotify runnable = new ClientListenerTaskCompleteNotify();
			informListeners(runnable);
	}

}

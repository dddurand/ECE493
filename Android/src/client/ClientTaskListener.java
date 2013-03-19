package client;

import server.TaskListener;

/**
 * An extended TaskListener that is specific for Clients that use
 * the onPlayerTaskClose()
 * 
 * @author dddurand
 *
 */
public interface ClientTaskListener extends TaskListener {
	public void onPlayerTaskClose();

}
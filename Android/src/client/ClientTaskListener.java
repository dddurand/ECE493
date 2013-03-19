package client;

import server.TaskListener;

public interface ClientTaskListener extends TaskListener {
	public void onPlayerTaskClose();

}
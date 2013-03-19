package client;

import server.TaskListener;

public interface ClientTaskListern extends TaskListener {
	public void onPlayerTaskClose();

}
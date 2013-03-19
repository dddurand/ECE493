package server;

import game.Player;

public interface PlayerTaskListener extends TaskListener {
	public void onPlayerTaskClose(Player player);

}
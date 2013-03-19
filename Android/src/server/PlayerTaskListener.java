package server;

import game.Player;

/**
 * Extended TaskListener for the specific PlayerTask Case
 * @author dddurand
 *
 */
public interface PlayerTaskListener extends TaskListener {
	public void onPlayerTaskClose(Player player);

}
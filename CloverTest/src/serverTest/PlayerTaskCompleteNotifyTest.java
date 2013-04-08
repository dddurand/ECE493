package serverTest;

import game.Player;
import server.PlayerTaskCompleteNotify;
import server.PlayerTaskListener;
import android.test.AndroidTestCase;

public class PlayerTaskCompleteNotifyTest extends AndroidTestCase {

	private Player player = new Player(0, "bob", 1);
	
	public void testBasic()
	{
		PlayerTaskCompleteNotify notify = new PlayerTaskCompleteNotify(player);
		
		ActionPlayerCatcher ct = new ActionPlayerCatcher();
		notify.setListener(ct);
		notify.run();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			fail();
		}
		
		if(!ct.isRun())
		{
			fail();
		}
		
		
		
	}

 public static class ActionPlayerCatcher implements PlayerTaskListener
 {

	 private boolean isRun = false;
	 
	 private Player player;
	 
	/**
	 * @return the isRun
	 */
	public boolean isRun() {
		return isRun;
	}

	/**
	 * @param isRun the isRun to set
	 */
	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}

	@Override
	public void onPlayerTaskClose(Player player) {
		this.isRun = true;
		this.player = player;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	
	
	 
 }
	
}

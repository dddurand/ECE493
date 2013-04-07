package client.mock;

import game.Player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;

import server.Server;

public class ServerMock extends Server {

	private boolean isPlayerAdded = false;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Player player;
	
	public ServerMock(Activity activity) {
		super(activity);
	}

	@Override
	public void addPlayer(Player player, ObjectInputStream inStream,
			ObjectOutputStream outStream) {	
		this.player = player;
		isPlayerAdded = true;
		this.in = inStream;
		this.out = outStream;
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

	public boolean isPlayerAdded()
	{
		return isPlayerAdded;
	}

	/**
	 * @return the out
	 */
	public ObjectOutputStream getOut() {
		return out;
	}

	/**
	 * @param out the out to set
	 */
	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}

	/**
	 * @return the in
	 */
	public ObjectInputStream getIn() {
		return in;
	}

	/**
	 * @param in the in to set
	 */
	public void setIn(ObjectInputStream in) {
		this.in = in;
	}
	
	

}

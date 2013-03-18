package server;

import game.Player;

import java.io.IOException;
import java.io.Serializable;

public class GameState implements Serializable  {

	//Who its for
	private Player player;
	
	//All the stuff we need
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4305757269635174737L;

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		// write 'this' to 'out'...
		
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	
	
}

package server;

import game.Player;

import java.io.IOException;
import java.io.Serializable;

public class GameState implements Serializable  {

	private static final long serialVersionUID = -4305757269635174737L;
	
	//Who its for
	private Player player;
	private int totalPlayers;
	//list of players
	//pot
	//current bet amount
	//my current bet
	//etc....
	
			
	public GameState(int numberPlayers)
	{
		this.totalPlayers = numberPlayers;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/*******Total Players******/
	public int getTotalPlayers() {
		return totalPlayers;
	}
	public void setTotalPlayers(int totalPlayers) {
		this.totalPlayers = totalPlayers;
	}
	
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		// write 'this' to 'out'...
		
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
	}
	
	
}

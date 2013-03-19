package server;

import game.Player;

import java.io.IOException;
import java.io.Serializable;

/**
 * 
 * This object represents the state of the game, and contains all information needed
 * to update the GUI of the clients.
 * 
 * This includes players, balances, pots, community cards, your cards, etc.
 * It also contains a list of actions & contexts(history) made by current user.
 * 
 * @author dddurand
 *
 */
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
	
	//We also need a list of actions and their contexts for each user for statistics
	//AKA the history of actions for this game for this user
	
			
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
	
	/**
	 * This method serializes the current object.
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeInt(totalPlayers);
		//out.writeObject(player);
		
	}

	/**
	 * This method deserializes the current object.
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.totalPlayers=in.readInt();
		//this.player = (Player) in.readObject();
		
	}
	
	
}

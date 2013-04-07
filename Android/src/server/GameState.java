package server;

import game.Card;
import game.Player;
import game.Pot;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

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
	
	//The player this is intended for.
	private Player player;
	
	//@TODO
	private ArrayList<String> playerHistory;
	
	/*General*/
	private int timeoutSeconds;
	private int currentPlayerTurn;
	private ArrayList<Player> players;
	private int currentDealer;
	private int blindAmount;
	private Pot mainPot;
	private ArrayList<Pot> sidePots;
	private Card[] community;
	private java.util.UUID UUID;
	private GameAction lastPokerGameAction;
	private int gameUpdateNumber;	
	
	public GameState(java.util.UUID UUID,
					Player player, 
					ArrayList<String> playerHistory,
					int currentPlayerTurn,
					ArrayList<Player> players,
					int currentDealer,
					int blindAmount,
					Pot mainPot,
					ArrayList<Pot> sidePots,
					Card[] community,
					GameAction lastAction,
					int timeoutSeconds,
					int gameUpdateNumber
					)
	{
		this.UUID = UUID;
		this.player = player;
		this.playerHistory = playerHistory;
		this.currentPlayerTurn = currentPlayerTurn;
		this.players = players;
		this.currentDealer = currentDealer;
		this.blindAmount = blindAmount;
		this.mainPot = mainPot;
		this.sidePots = sidePots;
		this.community = community;
		this.timeoutSeconds = timeoutSeconds;
		this.lastPokerGameAction = lastAction;
		this.gameUpdateNumber = gameUpdateNumber;
	}
	
	
	public int getGameUpdateNumber() {
		return gameUpdateNumber;
	}

	public void setGameUpdateNumber(int gameUpdateNumber) {
		this.gameUpdateNumber = gameUpdateNumber;
	}





	public GameAction getLastPokerGameAction() {
		return lastPokerGameAction;
	}

	public void setLastPokerGameAction(GameAction lastPokerGameAction) {
		this.lastPokerGameAction = lastPokerGameAction;
	}

	public java.util.UUID getUUID() {
		return UUID;
	}

	public void setUUID(java.util.UUID uUID) {
		UUID = uUID;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public ArrayList<String> getPlayerHistory() {
		return playerHistory;
	}



	public void setPlayerHistory(ArrayList<String> playerHistory) {
		this.playerHistory = playerHistory;
	}



	public int getTimeoutSeconds() {
		return timeoutSeconds;
	}



	public void setTimeoutSeconds(int timeoutSeconds) {
		this.timeoutSeconds = timeoutSeconds;
	}



	public int getCurrentPlayerTurn() {
		return currentPlayerTurn;
	}



	public void setCurrentPlayerTurn(int currentPlayerTurn) {
		this.currentPlayerTurn = currentPlayerTurn;
	}



	public ArrayList<Player> getPlayers() {
		return players;
	}



	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}



	public int getCurrentDealer() {
		return currentDealer;
	}



	public void setCurrentDealer(int currentDealer) {
		this.currentDealer = currentDealer;
	}



	public int getBlindAmount() {
		return blindAmount;
	}



	public void setBlindAmount(int blindAmount) {
		this.blindAmount = blindAmount;
	}



	public Pot getMainPot() {
		return mainPot;
	}



	public void setMainPot(Pot mainPot) {
		this.mainPot = mainPot;
	}



	public ArrayList<Pot> getSidePots() {
		return sidePots;
	}



	public void setSidePots(ArrayList<Pot> sidePots) {
		this.sidePots = sidePots;
	}



	public Card[] getCommunity() {
		return community;
	}



	public void setCommunity(Card[] community) {
		this.community = community;
	}



	/**
	 * This method serializes the current object.
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {

		out.writeObject(this.player);
		out.writeObject(this.playerHistory);
		out.writeInt(this.currentPlayerTurn);
		out.writeObject(this.players);
		out.writeInt(this.currentDealer);
		out.writeInt(this.blindAmount);
		out.writeObject(this.mainPot);
		out.writeObject(this.sidePots);
		out.writeObject(this.community);
		out.writeInt(this.timeoutSeconds);
		out.writeObject(this.UUID);
		out.writeObject(this.lastPokerGameAction);
		out.writeInt(this.gameUpdateNumber);
	}

	/**
	 * This method deserializes the current object.
	 * 
	 * @param out
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.player = (Player)in.readObject();
		this.playerHistory = (ArrayList<String>) in.readObject();
		this.currentPlayerTurn = in.readInt();
		this.players = (ArrayList<Player>) in.readObject();
		this.currentDealer = in.readInt();
		this.blindAmount = in.readInt();
		this.mainPot = (Pot)in.readObject();
		this.sidePots = (ArrayList<Pot>)in.readObject();
		this.community = (Card[]) in.readObject();
		this.timeoutSeconds = in.readInt();
		this.UUID = (java.util.UUID) in.readObject();
		this.lastPokerGameAction = (GameAction) in.readObject();
		this.gameUpdateNumber = in.readInt();
	}
	
	
}

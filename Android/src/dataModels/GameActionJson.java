package dataModels;

import org.json.JSONException;
import org.json.JSONObject;

import server.GameAction.PokerAction;

/**
 * An data object that is specific to uploaded data to the web service.
 * This object represents an action made by the user and its context within a game.
 * 
 * @author dddurand
 *
 */
public class GameActionJson {
	
	private PokerAction action;
	private int pot;
	private int bet;
	private String hand;
	private String communityCards;
	private int id;
	
	/**
	 * General Constructor
	 */
	public GameActionJson(int id, PokerAction action, int pot, int bet, String hand, String communityCards)
	{
		this.action = action;
		this.pot = pot;
		this.hand = hand;
		this.communityCards = communityCards;
		this.bet = bet;
		this.id = id;
	}

	
	/**
	 * Retrieves the database id of the game action
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the database id of the game action
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets type of poker action this object is
	 * 
	 * @return
	 */
	public PokerAction getAction() {
		return action;
	}

	/**
	 * Sets what type of poker action this object represents
	 * 
	 * @param action
	 */
	public void setAction(PokerAction action) {
		this.action = action;
	}

	/**
	 * Retrieves the current pot when this action was made
	 * 
	 * @return
	 */
	public int getPot() {
		return pot;
	}

	/**
	 * Sets the current pot
	 * 
	 * @param pot
	 */
	public void setPot(int pot) {
		this.pot = pot;
	}

	/**
	 * Gets the current amount bet during this action
	 * 
	 * @return
	 */
	public int getBet() {
		return bet;
	}

	/**
	 * Sets the bet amount
	 * 
	 * @param bet
	 */
	public void setBet(int bet) {
		this.bet = bet;
	}

	/**
	 * Get the users current hand during this action
	 * 
	 * @return
	 */
	public String getHand() {
		return hand;
	}

	/**
	 * Sets the users current hand during this action
	 * @param hand
	 */
	public void setHand(String hand) {
		this.hand = hand;
	}

	/**
	 * Gets the current state of the community cards during this actions
	 * 
	 * @return
	 */
	public String getCommunityCards() {
		return communityCards;
	}

	/**
	 * Sets the current community cards during this action
	 * @param communityCards
	 */
	public void setCommunityCards(String communityCards) {
		this.communityCards = communityCards;
	}
	
	/**
	 * Returns the json representation of this object.
	 * 
	 * @return
	 * @throws JSONException
	 */
	public JSONObject getJson() throws JSONException
	{
		JSONObject obj = new JSONObject();
		obj.put("action", this.action.getValue());
		obj.put("pot", pot);
		obj.put("bet", bet);
		obj.put("hand", hand);
		obj.put("communityCards", communityCards);

		return obj; 
	}
	

}

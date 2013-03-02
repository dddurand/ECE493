package dataModels;

import java.util.ArrayList;

/**
 * The Action that a user makes in a game.
 * The current knowledge of the player is also present for that action,
 * including pot, hand, and community cards.
 * 
 * @author dddurand
 *
 */
public class GameAction {

	private PokerAction action;
	private int pot;
	private ArrayList<Card> hand;
	private ArrayList<Card> communityCards;

	/**
	 * General Constructor
	 */
	public GameAction() {}
	
	/**
	 * General Constructor
	 */
	public GameAction(PokerAction action, int pot, ArrayList<Card> hand, ArrayList<Card> communityCards)
	{
		this.action = action;
		this.pot = pot;
		this.hand = hand;
		this.communityCards = communityCards;
	}
	
	
	/**
	 * action Getter
	 * @return
	 */
	public PokerAction getAction() {
		return action;
	}

	/**
	 * action Setter
	 * @param action
	 */
	public void setAction(PokerAction action) {
		this.action = action;
	}

	/**
	 * Pot getter 
	 * @return
	 */
	public int getPot() {
		return pot;
	}

	/**
	 * Pot Setter
	 * @param pot
	 */
	public void setPot(int pot) {
		this.pot = pot;
	}

	/**
	 * Hand Getter
	 * @return
	 */
	public ArrayList<Card> getHand() {
		return hand;
	}

	/**
	 * Hand Setter
	 */
	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}

	/**
	 * Community Card Getter
	 * @return
	 */
	public ArrayList<Card> getCommunityCards() {
		return communityCards;
	}
	
	/**
	 * Community Card Setter
	 * @param communityCards
	 */
	public void setCommunityCards(ArrayList<Card> communityCards) {
		this.communityCards = communityCards;
	}



	/**
	 * Various actions possible in a poker 
	 * 
	 * @author dddurand
	 *
	 */
	public enum PokerAction
	{
		BET("BET"), CHECK("CHECK"), CALL("CALL"), FOLD("FOLD"), RAISE("RAISE"), RERAISE("RERAISE"),
		WIN("WIN"), LOSS("LOSS"), END("END"), START("START"),
		UNKNOWN("U");
		
		private String value;
		
		/**
		 * General Enum Constructor
		 * @param action
		 */
		PokerAction(String action)
		{
			this.value = action;
		}
		
		/**
		 * Retrieves the string representation of the enum
		 * 
		 * @return
		 */
	    public String getValue() {
	        return value;
	    }
	    
	    /**
	     * Returns the PokerAction for a given string value
	     * 
	     * @param value
	     * @return
	     */
	    public static PokerAction getAction(String value)
	    {
	    	for (PokerAction action : PokerAction.values()) {
	            if (action.getValue().equals(value)) {
	                return action;
	            }
	        }
	    	
	    	return PokerAction.UNKNOWN;
	    }
	}
	
}

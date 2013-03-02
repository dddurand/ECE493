package dataModels;

import java.util.ArrayList;

public class GameAction {

	private PokerAction action;
	private int pot;
	private ArrayList<Card> hand;
	private ArrayList<Card> communityCards;

	
	public GameAction() {}
	
	public GameAction(PokerAction action, int pot, ArrayList<Card> hand, ArrayList<Card> communityCards)
	{
		this.action = action;
		this.pot = pot;
		this.hand = hand;
		this.communityCards = communityCards;
	}
	
	
	
	public PokerAction getAction() {
		return action;
	}

	public void setAction(PokerAction action) {
		this.action = action;
	}

	public int getPot() {
		return pot;
	}

	public void setPot(int pot) {
		this.pot = pot;
	}

	public ArrayList<Card> getHand() {
		return hand;
	}

	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}

	public ArrayList<Card> getCommunityCards() {
		return communityCards;
	}

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
		
		PokerAction(String action)
		{
			this.value = action;
		}
		
	    public String getValue() {
	        return value;
	    }
	    
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

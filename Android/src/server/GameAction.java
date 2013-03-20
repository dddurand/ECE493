package server;

import game.Card;
import game.Player;
import game.Pot;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * An object representing an action that has been made on the GUI.
 * This includes the actions itself, and any associated information
 * such as the amount bet or raised.
 * 
 * @author dddurand
 *
 */
public class GameAction implements Serializable {

	private static final long serialVersionUID = -236273271024732735L;
	
	private int position = -1;
	private int value = 0;
	private PokerAction action;
	private Player player = new Player(-1, "" , 0);
	
	/*Test Constructor*/
	public GameAction(int position, PokerAction action)
	{
		this.action = action;
	}
	
	/**
	 * General Constructor
	 */
	public GameAction(int position, PokerAction action, int value)
	{
		this.action = action;
	}
	
	/**
	 * A specific constructor for adding/removing players
	 */
	public GameAction(Player player, boolean addUser)
	{
		this.player = player;
		
		if(addUser) this.action = PokerAction.ADDPLAYER;
		else this.action = PokerAction.REMOVEPLAYER;
		
	}
	
	/**
	 * For use with actions with no parameters
	 * 
	 * @param action
	 */
	public GameAction(PokerAction action)
	{
		this.action = action;
	}

	
	
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public PokerAction getAction() {
		return action;
	}

	public void setAction(PokerAction action) {
		this.action = action;
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
		WIN("WIN"), LOSS("LOSS"), ENDGAME("END"), STARTGAME("START"),
		ADDPLAYER("ADD_PLAYER"), REMOVEPLAYER("REMOVE_PLAYER"),
		STARTTABLE("STARTTABLE"), STOPTABLE("STOPTABLE"),
		TIMEOUT("TIME_OUT"),
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

	
	/**
	 * This method is used in the serialization of the object
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.write(this.position);
		out.write(this.value);
		out.writeUnshared(this.action.getValue());
		out.writeObject(this.player);
	}

	
	/**
	 * This method is used in the deserialization of the object
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.position = in.readInt();
		this.value = in.readInt();
		
		String action = in.readUTF();
		this.action = PokerAction.getAction(action);
		
		this.player = (Player) in.readObject();
		
	}

}

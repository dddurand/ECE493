package game;

import java.io.IOException;
import java.io.Serializable;
/**
 * object to hold player information
 * @author Lawton
 *
 */
public class Player implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3606637057497020633L;
	private Card[] hand = new Card[2];
	private int id;
	private int money;
	private int active;
	private String username=null;
	
	public static final int FOLDED = 0;
	public static final int ALL_IN = 1;
	public static final int CURRENT = 2;
	/**
	 * Constructor set username amount of money and id to distinguish
	 * @param id
	 * @param username
	 * @param money
	 */
	public Player(int id, String username, int money) {
		this.id=id;
		this.money = money;
		this.username = username;
	}
	
	/**
	 * get the string username of the player
	 * @return
	 */
	public String getUsername()
	{
		return this.username;
	}
	
	/**
	 * get the id of the player which represents the position of the player
	 * at the table
	 * @return
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * set the cards for the player
	 * @param card
	 * @param pos
	 */
	public void setCard(Card card, int pos) {
		this.hand[pos]=card;
	}
	
	/**
	 * returns card belonging to the player at given position
	 * player has two cards position 0 and 1
	 * @param pos
	 * @return
	 */
	public Card getCard(int pos) {
		return this.hand[pos];
	}
	
	/**
	 * returns entire hand of the player
	 * @return
	 */
	public Card[] getHand() {
		return this.hand;
	}
	
	/**
	 * add money to the player amount
	 * @param money
	 */
	public void addMoney(int money) {
		this.money+= money;
	}
	
	/**
	 * returns the amount of money the player has
	 * @return
	 */
	public int getAmountMoney() {
		return this.money;
	}
	
	/**
	 * remove anmount from total amount of money player has
	 * @param money
	 */
	public void removeMoney(int money) {
		this.money-=money;
	}
	
	/**
	 * set the mode of the player
	 * use folded, all_in or current
	 * @param mode
	 */
	public void setActive(int mode) {
		this.active = mode;
	}
	
	/**
	 * get the mode of the player
	 * 0 - folded
	 * 1 - all in
	 * 2 - current
	 */
	public int getActive() {
		return active;
	}
	public String toString() {
		return "id: " + this.id + "\nmoney: " + this.money + "\nhand: " + this.hand[0].toString() + " and " + this.hand[1].toString();
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(hand);
		out.writeInt(id);
		out.writeInt(money);
		out.writeInt(active);
		out.writeUTF(username);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.hand = (Card[]) in.readObject();
		this.id = in.readInt();
		this.money = in.readInt();
		this.active = in.readInt();
		this.username = in.readUTF();
	}
	
}

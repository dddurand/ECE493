package game;

import java.io.IOException;
import java.io.Serializable;

public class Player implements Serializable{
	private Card[] hand = new Card[2];
	private int id;
	private int money;
	private int active;
	private String username;
	
	public Player(int id, String username, int money) {
		this.id=id;
		this.money = money;
		this.username = username;
	}
	
	
	public String getUsername()
	{
		return this.username;
	}
	
	public int getId() {
		return this.id;
	}
	protected void setCard(Card card, int pos) {
		this.hand[pos]=card;
	}
	
	public Card getCard(int pos) {
		return this.hand[pos];
	}
	
	protected void addMoney(int money) {
		this.money+= money;
	}
	
	public int getAmountMoney() {
		return this.money;
	}
	
	protected void removeMoney(int money) {
		this.money-=money;
	}
	
	protected void setActive(int mode) {
		this.active = mode;
	}
	
	/*
	 * 0 - folded
	 * 1 - all in
	 * 2 - current
	 */
	protected int getActive() {
		return active;
	}
	public String toString() {
		return "id: " + this.id + "\nmoney: " + this.money + "\nhand: " + this.hand[0].toString() + " and " + this.hand[1].toString();
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.write(id);
		out.write(money);
		out.write(active);
		out.writeUTF(username);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.id = in.readInt();
		this.money = in.readInt();
		this.active = in.readInt();
		this.username = in.readUTF();
	}
	
}

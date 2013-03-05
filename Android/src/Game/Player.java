package Game;

public class Player {
	private Card[] hand = new Card[2];
	private int id;
	private int money;
	private int active;
	
	public Player(int id, int money) {
		this.id=id;
		this.money = money;
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
}
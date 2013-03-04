package Game;

public class Pot {
	private int owner;
	private int amount;
	public Pot(int owner, int amount) {
		this.owner = owner;
		this.amount = amount;
	}
	
	protected int addAmount(int amount) {
		this.amount += amount;
		return this.amount;
	}
	
	protected int getAmount() {
		return this.amount;
	}
	
	protected int getOwner() {
		return this.owner;
	}
}

package dataModels;

public class MoneyGenerated {

	private final String name = "MoneyGenerated";
	private int value;
	private Account account;
	private int id;

	public MoneyGenerated(int value, int id)
	{
		this.id = id;
		this.value = value;
	}
	
	public MoneyGenerated(int value, Account account)
	{
		this.account = account;
		this.value = value;
	}

	public int getValue() {
		return value;
	}


	public void setValue(int value) {
		this.value = value;
	}


	public Account getAccount() {
		return account;
	}


	public void setAccount(Account account) {
		this.account = account;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}
	
	
	
	
	
}

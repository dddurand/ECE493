package dataModels;

public class Account {

	private String username;
	private String authenticationToken;
	private int balance;
	private boolean isOnline;
	private String password;
	
	/**
	 * General Constructor
	 */
	public Account()
	{
		this.clear();
	}
	
	public void clear()
	{
		this.username = "";
		this.authenticationToken = "";
		this.balance = -1;
		this.isOnline = false;
		this.password = "";
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}
	
	/**
	 * Create online account
	 * @param username
	 * @param authenticationToken
	 * @param balance
	 */
	public Account(String username, String authenticationToken, int balance)
	{
		this.username = username;
		this.authenticationToken = authenticationToken;
		this.balance = balance;
		this.isOnline = true;
	}
	
	/**
	 * Create offline account
	 * @param username
	 * @param balance
	 */
	public Account(String username, int balance)
	{
		this.username = username;
		this.balance = balance;
		this.isOnline = false;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAuthenticationToken() {
		return authenticationToken;
	}
	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public boolean isOnline() {
		return isOnline;
	}
	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	
	public void addBalance(int balance) {
		this.balance += balance;
	}
	
	
}

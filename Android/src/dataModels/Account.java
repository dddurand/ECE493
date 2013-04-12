package dataModels;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class acts as a data representation of an user account. 
 * This includes online and offline accounts.
 * 
 * @SRS 3.2.1.1
 * @author dddurand
 *
 */
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
	
	/**
	 * Clears all details within the current account object.
	 */
	public void clear()
	{
		this.username = "";
		this.authenticationToken = "";
		this.balance = -1;
		this.isOnline = false;
		this.password = "";
	}
	
	/**
	 * Retrieves the password for the account.
	 * This field is not automatically loaded through any contstructor,
	 * and is only present when added manually.
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the accounts current password.
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Retrieves the current username of the account
	 * @return
	 */
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
	
	/**
	 * Username setter
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Get Auth Token
	 * @return
	 */
	public String getAuthenticationToken() {
		return authenticationToken;
	}
	
	/**
	 * Set Auth Token
	 * @param authenticationToken
	 */
	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}
	
	/**
	 * Gets current balance
	 * @return
	 */
	public int getBalance() {
		return balance;
	}
	
	/**
	 * Set current balance
	 * @param balance
	 */
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	/**
	 * Returns whether the current account is an online or offline account.
	 * @return
	 */
	public boolean isOnline() {
		return isOnline;
	}
	
	/**
	 * Set whether the current account is online or offline.
	 * 
	 * @param isOnline
	 */
	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	
	/**
	 * Adds a given amount to the account balance.
	 * @param balance
	 */
	public void addBalance(int balance) {
		this.balance += balance;
	}
	
	/**
	 * Retrieves the json representation of the object
	 * @return
	 * @throws JSONException
	 */
	public JSONObject getJson() throws JSONException
	{
		JSONObject json = new JSONObject();
		json.put("username", this.username);
		json.put("password", this.password);
		json.put("authenticationToken", this.authenticationToken);
		
		return json;
	}
	
	
}

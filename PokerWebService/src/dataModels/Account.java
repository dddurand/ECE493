package dataModels;

import util.PasswordUtil;

/**
 * Data model for a user account.
 * 
 * @SRS 3.2.2.1 -> 3.2.2.8
 * @author dddurand
 *
 */
public class Account {

	private String username;
	private String password;
	private String authenticationToken;

	private transient int accountID;
	private transient String ecryptedPassword;
	private transient boolean hasEncryptedPassword;
	private transient PasswordUtil passUtil;


	/**
	 * Generic Empty Constructor
	 * Required by GSON
	 */
	public Account()
	{
		passUtil = new PasswordUtil();
	}

	/**
	 * Generic Constructor
	 * @param username Username of the account
	 * @param password Password of the account
	 */
	public Account(String username, String password, boolean isEncryptedPassword)
	{
		this.username = username;

		if(isEncryptedPassword)
		{
			this.ecryptedPassword = password;
			this.hasEncryptedPassword = true;
		}
		else
		{
			this.password = password;
			this.hasEncryptedPassword = false;
		}

		this.password = password;
		passUtil = new PasswordUtil();
	}

	/**
	 * Generic Constructor
	 * @param username Username of the account
	 * @param password Password of the account
	 * @param authToken Current AuthToken for the account
	 */
	public Account(String username, String password, String authToken, boolean isEncryptedPassword)
	{
		this.username = username;
		this.authenticationToken = authToken;
		passUtil = new PasswordUtil();

		if(isEncryptedPassword)
		{
			this.ecryptedPassword = password;
			this.hasEncryptedPassword = true;
		}
		else
		{
			this.password = password;
			this.hasEncryptedPassword = false;
		}


	}

	/**
	 * Generic Constructor
	 * @param username Username of the account
	 * @param password Password of the account
	 * @param authToken Current AuthToken for the account
	 */
	public Account(int accountID, String username, String password, String authToken, boolean isEncryptedPassword)
	{
		this.accountID = accountID;
		this.username = username;
		this.authenticationToken = authToken;
		passUtil = new PasswordUtil();

		if(isEncryptedPassword)
		{
			this.ecryptedPassword = password;
			this.hasEncryptedPassword = true;
		}
		else
		{
			this.password = password;
			this.hasEncryptedPassword = false;
		}


	}

	/**
	 * Retrieves the user's account ID
	 * 
	 * @return
	 */
	public int getAccountID() {
		return accountID;
	}

	/**
	 * Updates the accounts current account ID
	 * 
	 * @param accountID
	 */
	public void setAccountID(int accountID) {
		this.accountID = accountID;
	}

	/**
	 * Generic Constructor
	 * @param username Username of the account
	 */
	public Account(String username)
	{
		this.username = username;
	}


	/**
	 * Retrieves username of the account
	 * @return Username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Set the username for the account
	 * @param username Username of the account
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Retrieves the password for the account
	 * @return The password of the account
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Get encrypted version of current password.
	 * 
	 * @return
	 */
	public String getEncyptedPassword() {
		if(hasEncryptedPassword)
			return ecryptedPassword;
		else 
		{
			this.hasEncryptedPassword = true;
			this.ecryptedPassword = passUtil.encrypt(this.password);
		}

		return this.ecryptedPassword;

	}

	/**
	 * Sets the password for the account
	 * @param password Password for the account
	 */
	public void setPassword(String password, boolean isEncryptedPassword) {
		if(isEncryptedPassword)
		{
			this.ecryptedPassword = password;
			this.hasEncryptedPassword = true;
		}
		else
		{
			this.password = password;
			this.hasEncryptedPassword = false;
		}
	}

	/**
	 * Retrieves AuthToken for the account
	 * @return AuthToken for the account
	 */
	public String getAuthenticationToken() {
		return authenticationToken;
	}

	/**
	 * Sets the authenticationToken for the account
	 * @param authenticationToken
	 */
	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

	/**
	 * Object must be an account object,
	 * Password, Username, and AuthToken must all match exactly for two Accounts.
	 * 
	 * @param object Account object to compare
	 * @return true on match, false otherwise
	 */
	public boolean exactCompare(Object object)
	{
		if(object == null) return false;

		if (object instanceof Account) {
			Account accountOther = (Account) object;

			String otherUsername = accountOther.getUsername();

			String otherPassword = accountOther.getEncyptedPassword();

			String otherAuthToken = accountOther.getAuthenticationToken();

			if(!isEqualUsername(username,otherUsername))
				return false;

			if(!isEqualString(this.getEncyptedPassword(),otherPassword))
				return false;

			if(!isEqualString(authenticationToken,otherAuthToken))
				return false;

			return true;
		}

		return false;
	}


	/**
	 * Determines if the provided account object has the same username
	 * and password.
	 * 
	 * @param otherAccount Account to be compared against.
	 * @return True on match, false otherwise
	 */
	public boolean compareLogin(Account otherAccount)
	{
		if(otherAccount == null) return false;

		String otherUsername = otherAccount.getUsername();
		String otherPassword = otherAccount.getEncyptedPassword();

		if(!isEqualUsername(username,otherUsername))
			return false;

		if(!isEqualString(this.getEncyptedPassword(),otherPassword))
			return false;

		return true;
	}

	/**
	 * Determines if the provided account has the same username, and
	 * authentication token.
	 * 
	 * @param otherAccount Account to compare against
	 * @return True on match, false otherwise
	 */
	public boolean compareAuthenticated(Account otherAccount)
	{		
		if(otherAccount == null) return false;
		
		String otherUsername = otherAccount.getUsername();
		String otherAuthToken = otherAccount.getAuthenticationToken();

		if(!isEqualUsername(username,otherUsername))
			return false;

		if(!isEqualString(authenticationToken,otherAuthToken))
			return false;

		return true;
	}

	/**
	 * Determines if two string are equal
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 */
	public boolean isEqualString(String value1, String value2)
	{
		if(value1 == null && value2 != null)
			return false;
		else if(!value1.equals(value2))
			return false;

		return true;
	}

	/**
	 * Determines if two usernames are the same
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 */
	public boolean isEqualUsername(String value1, String value2)
	{
		if(value1 == null || value2 == null)
			return false;
		else if(!value1.equals(value2))
			return false;

		return true;
	}

}

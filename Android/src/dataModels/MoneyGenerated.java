package dataModels;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * An object representing a generation of money to an account.
 * 
 * @author dddurand
 *
 */
public class MoneyGenerated {

	private final String name = "MoneyGenerated";
	private int value;
	private Account account;
	private int id;

	/**
	 * General Constructor
	 * @param value The amount to add to the balance
	 * @param id The id of the money generated action the database
	 */
	public MoneyGenerated(int value, int id)
	{
		this.id = id;
		this.value = value;
	}
	
	/**
	 * General Constructor
	 * 
	 * @param value The value of money to add to the balance
	 * @param account The account which the money action is tied to.
	 */
	public MoneyGenerated(int value, Account account)
	{
		this.account = account;
		this.value = value;
	}

	/**
	 * Retrieves the amount of value added to balance
	 * @return
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets the amount of value added to balance
	 * 
	 * @param value
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * Gets the account the money generation is tied to.
	 * 
	 * @return
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * Sets the account the money generation is tied to.
	 * 
	 * @param account
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * Gets the database id of the money generation action
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set the database id
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Get the database id
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	public JSONObject getJson() throws JSONException
	{
		JSONObject obj = new JSONObject();
		obj.put("name", this.name);
		obj.put("value", this.value);
		
		return obj;
	}
	
}

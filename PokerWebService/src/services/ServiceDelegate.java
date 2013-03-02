package services;

import com.google.gson.Gson;

import dataModels.Account;
import database.DatabaseInterface;
import database.DatabaseInterface.DatabaseInterfaceException;

/**
 * The framework for a service delegate.
 * This object can be instantiated, but all its methods simply
 * return "Not Implemented". 
 * 
 * This object is mean't to be extended, and then added into the ServiceFactory.
 * These delegate objects act as the backbone of a service, and provide their main
 * functionality. Any custom additions needed by GSON objects should be added in the factory.
 * 
 * @author dddurand
 *
 */
public class ServiceDelegate {

	protected Gson gson;
	protected DatabaseInterface dbInterface; 
	
	/**
	 * General constructor
	 * 
	 * @param gson Gson object used by the serviceDelegate
	 * @param dbInterface DatabaseInterface used by the delegate
	 */
	public ServiceDelegate(Gson gson, DatabaseInterface dbInterface)
	{
		this.gson = gson;
		this.dbInterface = dbInterface;
	}
	
	/**
	 * Setter for the Gson Object
	 * 
	 * @param gson
	 */
	public void setGson(Gson gson)
	{
		this.gson = gson;
	}
	
	/**
	 * Setter for the Database interface
	 * 
	 * @param dbInterface
	 */
	public void setDatabaseInterface(DatabaseInterface dbInterface)
	{
		this.dbInterface = dbInterface;
	}
	
	/**
	 * This function is called by a SecureService when it has determined the connection
	 * has a valid username and auth token.
	 * 
	 * @param postAccount Authenticated account provided at login
	 * @param postData Data provided to the web service
	 * @return JSON Result
	 * @throws DatabaseInterfaceException
	 */
	public String applyAuthProcess(Account postAccount, String postData) throws DatabaseInterfaceException
	{
		return "Not Implemented.";
	}
	
	/**
	 * This function is called by a SecureService when it has determined the connection
	 * has a valid username and password.
	 * 
	 * @param postAccount Validated login account
	 * @param postData Data provided to the web service
	 * @return JSON Result
	 * @throws DatabaseInterfaceException
	 */
	public String applyLoginProcess(Account account, String postData) throws DatabaseInterfaceException
	{
		return "Not Implemented.";
	}
	
	/**
	 * This function is called by a SecureService without any checks.
	 * @param postAccount Authenticated account provided at login
	 * @param postData Data provided to the web service
	 * @return JSON Result
	 * @throws DatabaseInterfaceException
	 */
	public String unsecureProcess(Account account, String postData) throws DatabaseInterfaceException
	{
		return "Not Implemented.";
	}
}

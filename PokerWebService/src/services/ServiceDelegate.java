package services;

import com.google.gson.Gson;

import dataModels.Account;
import database.DatabaseInterface;
import database.DatabaseInterface.DatabaseInterfaceException;

public class ServiceDelegate {

	protected Gson gson;
	protected DatabaseInterface dbInterface; 
	
	public ServiceDelegate(Gson gson, DatabaseInterface dbInterface)
	{
		this.gson = gson;
		this.dbInterface = dbInterface;
	}
	
	public void setGson(Gson gson)
	{
		this.gson = gson;
	}
	
	public void setDatabaseInterface(DatabaseInterface dbInterface)
	{
		this.dbInterface = dbInterface;
	}
	
	
	public String applyAuthProcess(Account postAccount, String postData) throws DatabaseInterfaceException
	{
		return "Not Implemented.";
	}
	
	public String applyLoginProcess(Account account, String postData) throws DatabaseInterfaceException
	{
		return "Not Implemented.";
	}
	
	public String unsecureProcess(Account account, String postData) throws DatabaseInterfaceException
	{
		return "Not Implemented.";
	}
}

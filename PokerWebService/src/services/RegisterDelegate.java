package services;

import util.Codes;

import com.google.gson.Gson;

import dataModels.Account;
import database.DatabaseInterface;
import database.DatabaseInterface.DatabaseInterfaceException;
import database.ResponseObject;

/**
 * The ServiceDelegate extension that deals with the process of creating new accounts.
 * 
 * @author dddurand
 *
 * @SRS 3.2.2.2
 *
 */
public class RegisterDelegate extends ServiceDelegate{

	/**
	 * General Constructor
	 * 
	 * @param gson The gson object to be used to convert from / to JSON strings.
	 * @param dbInterface The interface to the external database.
	 */
	public RegisterDelegate(Gson gson, DatabaseInterface dbInterface) {
		super(gson, dbInterface);
	}

	/**
	 * The process used to generate a new account for the webservice
	 */
	@Override
	public String unsecureProcess(Account account, String postData)
			throws DatabaseInterfaceException {
			
			if(account.getUsername() == null || account.getUsername().isEmpty())
			{
				ResponseObject response = new ResponseObject(false, "Invalid Username Provided", Codes.INVALID_USERNAME);
				return gson.toJson(response, ResponseObject.class);
			}
			
			if(account.getPassword() == null || account.getPassword().isEmpty())
			{
				ResponseObject response = new ResponseObject(false, "Invalid Password Provided", Codes.INVALID_PASSWORD);
				return gson.toJson(response, ResponseObject.class);
			}
			
			if(dbInterface.isDuplicateUsername(account.getUsername()))
			{
				ResponseObject response = new ResponseObject(false, "Username already in use.", Codes.DUPLICATE_USERNAME);
				return gson.toJson(response, ResponseObject.class);
			}
			
			dbInterface.addAccount(account);
			ResponseObject response = new ResponseObject(true, ResponseObject.SUCCESS, Codes.SUCCESS);
			return gson.toJson(response, ResponseObject.class);
		
	}

}

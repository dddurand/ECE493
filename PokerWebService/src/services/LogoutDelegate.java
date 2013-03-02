package services;

import com.google.gson.Gson;

import dataModels.Account;
import database.DatabaseInterface;
import database.DatabaseInterface.DatabaseInterfaceException;
import database.ResponseObject;


/**
 * The ServiceDelegate extension that deals with the process of logging out a user,
 * through the invalidation/removal of the current authentication token for the account.
 * 
 * @author dddurand
 *
 */
public class LogoutDelegate extends ServiceDelegate{

	/**
	 * General Constructor
	 * @param gson
	 * @param dbInterface
	 */
	public LogoutDelegate(Gson gson, DatabaseInterface dbInterface) {
		super(gson, dbInterface);
	}

	/**
	 * The process that invalidates the current account's auth token.
	 */
	@Override
	public String unsecureProcess(Account account, String postData)
			throws DatabaseInterfaceException {
		
		dbInterface.invalidateAuth(account.getUsername());
		ResponseObject response = new ResponseObject(true, ResponseObject.SUCCESS);
		return gson.toJson(response, ResponseObject.class);
	}

}
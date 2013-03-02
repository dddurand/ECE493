package services;

import java.util.UUID;

import com.google.gson.Gson;

import dataModels.Account;
import database.DatabaseInterface;
import database.ResponseObject;
import database.DatabaseInterface.DatabaseInterfaceException;

/**
 * The ServiceDelegate extension that deals with the process of loggin in users,
 * through the generation of new authentication tokens.
 * 
 * @author dddurand
 *
 */
public class LoginDelegate extends ServiceDelegate{

	/**
	 * General Constructor
	 * 
	 * @param gson
	 * @param dbInterface
	 */
	public LoginDelegate(Gson gson, DatabaseInterface dbInterface) {
		super(gson, dbInterface);
	}

	/**
	 * Starts the process of logging in the provided account.
	 */
	@Override
	public String applyLoginProcess(Account account, String postData)
			throws DatabaseInterfaceException {
		
		String authToken = generateAuthenticationToken(account);
		
		ResponseObject response = new ResponseObject(true, ResponseObject.SUCCESS);
		response.setAuthenticationToken(authToken);
		return gson.toJson(response, ResponseObject.class);
	}
	
	/**
	 * Generates a unique authentication token for a given account, and stores it to the database.
	 * 
	 * @param account Account to tie the new authToken to.
	 * @return The AuthToken Generated
	 * @throws DatabaseInterfaceException
	 */
	private String generateAuthenticationToken(Account account) throws DatabaseInterfaceException
	{
		String authToken = UUID.randomUUID().toString();
		
		dbInterface.setAuth(account.getUsername(), authToken);
		
		return authToken;
	}
	
}

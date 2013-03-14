package services;

import java.util.UUID;

import javax.servlet.ServletException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import dataModels.Account;
import database.DatabaseInterface;
import database.DatabaseInterface.DatabaseInterfaceException;
import database.DatabaseTest;
import database.ResponseObject;

/**
 * The OLD service that deals with the authentication for the webservice,
 * including registration, login, and logout.
 * 
 * The service also generates the auth tokens for account.
 * 
 * THIS OBJECT IS NO LONGER IN USE, OTHER THAN FOR THE DATABASE TEST
 * THAT IS TRIGGERED BY A GET TO THE LOGIN SERVLET
 * 
 * @author dddurand
 *
 */
public class AuthenticationService {

	private DatabaseInterface dbInterface;
	private Gson gson;
	
	/**
	 * General constructor
	 * 
	 * @throws ServletException
	 */
	public AuthenticationService() throws ServletException
	{ 
		try {
			//Generate GSON builder with additional custom add-ons needed for custom serialization
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(ResponseObject.class, new ResponseObject.ResponseSerializer());
			gson = gsonBuilder.create();
			
			dbInterface = new DatabaseInterface();
		} catch (DatabaseInterfaceException e) {
			//@TODO Generate Error JSON of some kind
			throw new ServletException(e);
		}
	}
	
	
	public String testDatabaseConnection()
	{
		String result = DatabaseTest.Test();
		dbInterface.close();
		return result;
		
	}

	
}

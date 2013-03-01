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
 * The service that deals with the authentication for the webservice,
 * including registration, login, and logout.
 * 
 * The service also generates the auth tokens for account.
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
	
	/**
	 * Given request data containing JSON with a valid account username and password,
	 * the function will attempt to login the user. On successful login, a persistent
	 * authentication token is generated and returned to the user in JSON format.
	 * 
	 * Ex Failure: {"Success":"FALSE","Message":"Invalid Username \u0026 Password"}
	 * Ex Success: {"Success":"TRUE","Message":"TRUE","AuthenticationToken":"551f7422-70d9-4c97-bf1d-88da21104ead"}
	 * 
	 * @param requestData
	 * @return JSON data
	 */
	public String login(String requestData)
	{
		try
		{
			//Load account from post data
			Account account = gson.fromJson(requestData, Account.class);
			
			//get account from database
			Account accountFromDB = dbInterface.getAccount(account.getUsername());
			
			//No Valid Account
			if(accountFromDB == null)
			{
				ResponseObject response = new ResponseObject(false, "User Not Found");
				return gson.toJson(response, ResponseObject.class);
			} 
			//Valid account
			else
			{
				//Account login valid
				if(accountFromDB.compareLogin(account))
				{
					String authToken = generateAuthenticationToken(account);
					
					ResponseObject response = new ResponseObject(true, ResponseObject.SUCCESS);
					response.setAuthenticationToken(authToken);
					return gson.toJson(response, ResponseObject.class);
				}
				//Invalid username/password combo
				else
				{
					ResponseObject response = new ResponseObject(false, "Invalid Username & Password");
					return gson.toJson(response, ResponseObject.class);
				}
			}
			
			
		} catch (DatabaseInterfaceException e) {
			ResponseObject response = new ResponseObject(false, e.getMessage());
			return gson.toJson(response, ResponseObject.class);
		}
		catch (JsonSyntaxException e)
		{
			ResponseObject response = new ResponseObject(false, "Invalid POST DATA Provided");
			return gson.toJson(response, ResponseObject.class);
		}
		
	}
	
	/**
	 * Given request data containing JSON with a valid account username and auth token,
	 * the function will logout the users.
	 * 
	 * Ex Failure: {"Success":"FALSE","Message":"Invalid Username \u0026 Authentication Token"}
	 * Ex Success: {"Success":"TRUE","Message":"TRUE"}
	 * 
	 * @param requestData
	 * @return JSON data
	 */
	public String logout(String requestData)
	{
		try
		{
			//Load account from post data
			Account account = gson.fromJson(requestData, Account.class);
			
			//get account from database
			Account accountFromDB = dbInterface.getAccount(account.getUsername());
			
			//No Valid Account
			if(accountFromDB == null)
			{
				ResponseObject response = new ResponseObject(false, "User Not Found");
				return gson.toJson(response, ResponseObject.class);
			} 
			//Valid account
			else
			{
				//Account login valid
				if(accountFromDB.compareAuthenticated(account))
				{
					dbInterface.invalidateAuth(account.getUsername());
					ResponseObject response = new ResponseObject(true, ResponseObject.SUCCESS);
					return gson.toJson(response, ResponseObject.class);
				}
				//Invalid username/password combo
				else
				{
					ResponseObject response = new ResponseObject(false, "Invalid Username & Authentication Token");
					return gson.toJson(response, ResponseObject.class);
				}
			}
			
			
		} catch (DatabaseInterfaceException e) {
			ResponseObject response = new ResponseObject(false, e.getMessage());
			return gson.toJson(response, ResponseObject.class);
		}
		catch (JsonSyntaxException e)
		{
			ResponseObject response = new ResponseObject(false, "Invalid POST DATA Provided");
			return gson.toJson(response, ResponseObject.class);
		}
		
	}
	
	/**
	 * Given request data containing JSON with a valid account username and password token,
	 * the function will generate a new account.
	 * 
	 * Ex Failure: {"Success":"FALSE","Message":"Invalid Username \u0026 Authentication Token"}
	 * Ex Success: {"Success":"TRUE","Message":"TRUE"}
	 * 
	 * @param requestData
	 * @return JSON data
	 */
	public String register(String requestData)
	{
		
		try
		{
			Account account = gson.fromJson(requestData, Account.class);
			
			if(account.getUsername().trim().isEmpty())
			{
				ResponseObject response = new ResponseObject(false, "Invalid Username Provided");
				return gson.toJson(response, ResponseObject.class);
			}
			
			if(account.getPassword().trim().isEmpty())
			{
				ResponseObject response = new ResponseObject(false, "Invalid Password Provided");
				return gson.toJson(response, ResponseObject.class);
			}
			
			if(dbInterface.isDuplicateUsername(account.getUsername()))
			{
				ResponseObject response = new ResponseObject(false, "Username already in use.");
				return gson.toJson(response, ResponseObject.class);
			}
			
			dbInterface.addAccount(account);
			ResponseObject response = new ResponseObject(true, ResponseObject.SUCCESS);
			return gson.toJson(response, ResponseObject.class);
			
		} catch (DatabaseInterfaceException e) {
			ResponseObject response = new ResponseObject(false, e.getMessage());
			return gson.toJson(response, ResponseObject.class);
		}
		catch (JsonSyntaxException e)
		{
			ResponseObject response = new ResponseObject(false, "Invalid POST DATA Provided");
			return gson.toJson(response, ResponseObject.class);
		}
		
		
	}
	
	/**
	 * Returns true if account details - username & authToken - are valid.
	 * Otherwise it returns false
	 * 
	 * @param requestData Post data containing account information.
	 * @return
	 */
	public boolean isAuthenticated(String requestData)
	{
		try
		{
			Account account = gson.fromJson(requestData, Account.class);
			Account accountFromDB = dbInterface.getAccount(account.getUsername());
			
			//No Valid Account
			if(accountFromDB == null)
			{
				return false;
			} 
			//Valid account
			else
			{
				//Account login valid
				if(accountFromDB.compareLogin(account))
				{
					return true;
				}
				//Invalid username/password combo
				else
				{
					return false;
				}
			}
		} catch (DatabaseInterfaceException e) {
			return false;
		}
		catch (JsonSyntaxException e)
		{
			return false;
		}
	}
	
	public String testDatabaseConnection()
	{
				
		return DatabaseTest.Test();
		
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

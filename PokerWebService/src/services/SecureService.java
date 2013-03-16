package services;

import util.Codes;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import dataModels.Account;
import database.DatabaseInterface;
import database.ResponseObject;
import database.DatabaseInterface.DatabaseInterfaceException;

/**
 * The main logic for authentication to the web service. Other than the logic for
 * authentication, the work done by the service is determined by the the delegate
 * provided at creation.
 * 
 * @author dddurand
 *
 */
public class SecureService {

	private ServiceDelegate service;
	
	private Gson gson;
	private DatabaseInterface dbInterface;
	
	/**
	 * General Constructor
	 * 
	 * @param delegate The workhorse of the function, providing the main functionality.
	 * @param gson The object to serialize/deserialize JSON
	 * @param dbInterface The main interface to the database
	 */
	public SecureService(ServiceDelegate delegate, Gson gson, DatabaseInterface dbInterface)
	{
		this.service = delegate;
		this.gson = gson;
		this.dbInterface = dbInterface;
	}
	
	/**
	 * Getter for the GSON object
	 * 
	 * @return
	 */
	protected Gson getGson()
	{
		return this.gson;
	}
	
	/**
	 * Getter for the databaseInterface
	 * @return
	 */
	protected DatabaseInterface getdbInterface()
	{
		return this.dbInterface;
	}
	
	/**
	 * Given request data containing JSON with a valid account username and auth token,
	 * this function will call the delegates auth function. This will be overwritten
	 * in delegate to provide the desired functionality, that should only occur when a 
	 * valid username and auth token is valid.
	 * 
	 * On Failure the user will be informed.
	 * Ex Failure: {"Success":"FALSE","Message":"Invalid Username \u0026 Authentication Token"}
	 * 
	 * @param requestData
	 * @return JSON data
	 */
	public final String authSecuredProcess(String postData)
	{
			try
			{
				//Pull the account information out of the json
				Account account = gson.fromJson(postData, Account.class);
				
				//empty username
				if(account == null || account.getUsername()==null || account.getUsername().isEmpty())
				{
					return generateError("Invalid username", Codes.INVALID_USERNAME);
				}
				
				//empty auth token
				if(account.getAuthenticationToken()==null || account.getAuthenticationToken().isEmpty())
				{
					return generateError("Invalid auth token", Codes.INVALID_AUTH);
				}
				
				
				//Get the account with the same username
				Account accountFromDB = dbInterface.getAccount(account.getUsername());
				
				//No account was found for given username
				if(accountFromDB == null)
				{
					return generateUsernameNotFoundError();
				} 
				//Valid account
				else
				{	
					
					//Account login valid
					if(accountFromDB.compareAuthenticated(account))
					{
						return service.applyAuthProcess(accountFromDB, postData);
					}
					//Invalid username/password combo
					else
					{
						return generateInvalidAuthError();
					}
				}
				
				
			} catch (DatabaseInterfaceException e) {
				return generateError(e.getMessage(), e.getCode());
			}
			catch (JsonSyntaxException e)
			{
				return generateInvalidDataError();
			}
			catch (JsonParseException e)
			{
				return generateInvalidDataError();
			}
	}
	
	/**
	 * Given request data containing JSON with a valid account username and password,
	 * this function will call the delegates login function. This will be overwritten
	 * in delegate to provide the desired functionality, that should only occur when a 
	 * valid username and password is valid.
	 * 
	 * On Failure the user will be informed.
	 * Ex Failure: {"Success":"FALSE","Message":"Invalid Username \u0026 Authentication Token"}
	 * 
	 * @param requestData
	 * @return JSON data
	 */
	public final String loginSecuredProcess(String postData)
	{
		try
		{
			//Load account from post data
			Account account = gson.fromJson(postData, Account.class);
			
			
			
			//empty username
			if(account == null || account.getUsername()==null || account.getUsername().isEmpty())
			{
				return generateError("Invalid username", Codes.INVALID_USERNAME);
			}
			
			//empty password
			if(account.getPassword()==null || account.getPassword().isEmpty())
			{
				return generateError("Invalid password", Codes.INVALID_PASSWORD);
			}
			
			//get account from database
			Account accountFromDB = dbInterface.getAccount(account.getUsername());
			
			//No Valid Account
			if(accountFromDB == null)
			{
				ResponseObject response = new ResponseObject(false, "User Not Found", Codes.NO_ACCOUNT);
				return gson.toJson(response, ResponseObject.class);
			} 
			//Valid account
			else
			{
				//Account login valid
				if(accountFromDB.compareLogin(account))
				{
					return this.service.applyLoginProcess(accountFromDB, postData);
				}
				//Invalid username/password combo
				else
				{
					return generateInvalidLoginError();
				}
			}
			
			
		} catch (DatabaseInterfaceException e) {
			return generateError(e.getMessage(), e.getCode());
		}
		catch (JsonSyntaxException e)
		{
			return generateInvalidDataError();
		}
		catch (JsonParseException e)
		{
			return generateInvalidDataError();
		}
	}
	
	/**
	 * Does not explicitly checks for any auth verification.
	 * The call is simply passed on to the delegate, once the account
	 * provided is determined.
	 * 
	 * @param postData Data passed to the web service.
	 * @return
	 */
	public final String unsecuredProcess(String postData)
	{
		try
		{
			//Load account from post data
			Account account = gson.fromJson(postData, Account.class);
			
			if(account == null) return generateError("Invalid Username Provided", Codes.INVALID_USERNAME);
			
			
			return this.service.unsecureProcess(account, postData);
			
		} catch (DatabaseInterfaceException e) {
			return generateError(e.getMessage(), e.getCode());
		}
		catch (JsonSyntaxException e)
		{
			return generateInvalidDataError();
		}
		catch (JsonParseException e)
		{
			return generateInvalidDataError();
		}
	}
	
	/**
	 * Returns the error for the case when a username has no matching account
	 * @return
	 */
	private String generateUsernameNotFoundError()
	{
		return generateError("The provided username is not registered.", Codes.NO_ACCOUNT);
	}
	
	/**
	 * Returns the error when a username/password pair is invalid.
	 * @return
	 */
	private String generateInvalidLoginError()
	{
		return generateError("Invalid Login.", Codes.INVALID_LOGIN);
	}
	
	/**
	 * Returns the error used when an username/auth is invalid.
	 * @return
	 */
	private String generateInvalidAuthError()
	{
		return generateError("The provided authtoken and username is invalid.", Codes.NOT_AUTHED);
	}
	
	/**
	 * Returns the error used when the data provided is invalid.
	 * @return
	 */
	protected String generateInvalidDataError()
	{
		return generateError("Invalid post data.", Codes.INVALID_DATA);
	}
	
	/**
	 * Returns and error
	 * 
	 * @param message
	 * @return
	 */
	protected String generateError(String message, int code)
	{
		ResponseObject response = new ResponseObject(false, message, code);
		return gson.toJson(response, ResponseObject.class);
	}
	
	/**
	 * Clean up method for the service.
	 * All actions made on this object after calling this method is undefined.
	 * 
	 */
	public void close()
	{
		this.service.close();
	}
	
}

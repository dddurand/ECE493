package services;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import dataModels.Account;
import database.DatabaseInterface;
import database.ResponseObject;
import database.DatabaseInterface.DatabaseInterfaceException;

public class SecureService {

	private ServiceDelegate service;
	
	private Gson gson;
	private DatabaseInterface dbInterface;
	
	public SecureService(ServiceDelegate delegate, Gson gson, DatabaseInterface dbInterface)
	{
		this.service = delegate;
		this.gson = gson;
		this.dbInterface = dbInterface;
	}
	
	protected Gson getGson()
	{
		return this.gson;
	}
	
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
				if(account.getUsername().isEmpty() || account.getUsername()==null)
				{
					return generateError("Invalid username");
				}
				
				//empty auth token
				if(account.getAuthenticationToken().isEmpty() || account.getAuthenticationToken()==null)
				{
					return generateError("Invalid auth token");
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
						return generateInvalidLoginError();
					}
				}
				
				
			} catch (DatabaseInterfaceException e) {
				return generateError(e.getMessage());
			}
			catch (JsonSyntaxException e)
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
			if(account.getUsername().isEmpty() || account.getUsername()==null)
			{
				return generateError("Invalid username");
			}
			
			//empty password
			if(account.getPassword().isEmpty() || account.getPassword()==null)
			{
				return generateError("Invalid password");
			}
			
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
					return this.service.applyLoginProcess(accountFromDB, postData);
				}
				//Invalid username/password combo
				else
				{
					return generateInvalidLoginError();
				}
			}
			
			
		} catch (DatabaseInterfaceException e) {
			return generateError(e.getMessage());
		}
		catch (JsonSyntaxException e)
		{
			return generateInvalidDataError();
		}
	}
	
	public final String unsecuredProcess(String postData)
	{
		try
		{
			//Load account from post data
			Account account = gson.fromJson(postData, Account.class);
			return this.service.unsecureProcess(account, postData);
			
		} catch (DatabaseInterfaceException e) {
			return generateError(e.getMessage());
		}
		catch (JsonSyntaxException e)
		{
			return generateInvalidDataError();
		}
	}
	
	private String generateUsernameNotFoundError()
	{
		return generateError("The provided username is not registered.");
	}
	
	private String generateInvalidLoginError()
	{
		return generateError("Invalid Login.");
	}
	
	private String generateInvalidAuthError()
	{
		return generateError("The provided username is not registered.");
	}
	
	protected String generateInvalidDataError()
	{
		return generateError("Invalid post data.");
	}
	
	protected String generateError(String message)
	{
		ResponseObject response = new ResponseObject(false, message);
		return gson.toJson(response, ResponseObject.class);
	}
	
}

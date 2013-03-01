package services;

import com.google.gson.Gson;

import dataModels.Account;
import database.DatabaseInterface;
import database.DatabaseInterface.DatabaseInterfaceException;
import database.ResponseObject;

public class RegisterDelegate extends ServiceDelegate{

	public RegisterDelegate(Gson gson, DatabaseInterface dbInterface) {
		super(gson, dbInterface);
	}

	@Override
	public String unsecureProcess(Account account, String postData)
			throws DatabaseInterfaceException {
			
			if(account.getUsername().isEmpty())
			{
				ResponseObject response = new ResponseObject(false, "Invalid Username Provided");
				return gson.toJson(response, ResponseObject.class);
			}
			
			if(account.getPassword().isEmpty())
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
		
	}

}

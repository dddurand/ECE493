package services;

import com.google.gson.Gson;

import dataModels.Account;
import database.DatabaseInterface;
import database.DatabaseInterface.DatabaseInterfaceException;

public class LogoutDelegate extends ServiceDelegate{

	public LogoutDelegate(Gson gson, DatabaseInterface dbInterface) {
		super(gson, dbInterface);
	}

	@Override
	public String unsecureProcess(Account account, String postData)
			throws DatabaseInterfaceException {
		
		return null;
	}

}
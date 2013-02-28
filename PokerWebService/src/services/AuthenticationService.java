package services;

import database.DatabaseTest;

public class AuthenticationService {

	public AuthenticationService()
	{
		
	}
	
	public String testDatabaseConnection()
	{
				
		return DatabaseTest.Test();
		
	}
	
	
}

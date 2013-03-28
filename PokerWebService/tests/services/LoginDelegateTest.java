package services;

import static org.junit.Assert.*;

import mockObjects.MockDbInterface;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dataModels.Account;
import dataModels.Filter;
import dataModels.UploadData;
import database.ResponseObject;
import database.DatabaseInterface.DatabaseInterfaceException;

public class LoginDelegateTest {

	private static Gson gson;
	private static MockDbInterface dbInterface;
	
	@BeforeClass
	public static void setup() throws DatabaseInterfaceException
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ResponseObject.class, new ResponseObject.ResponseSerializer());
		gsonBuilder.registerTypeAdapter(UploadData.class, new UploadData.UploadDataDeserializer());
		gsonBuilder.registerTypeAdapter(Filter.class, new Filter.FilterDeserializer());
		gson = gsonBuilder.create();
		
		dbInterface = new MockDbInterface(false);
		
	}
	
	@Test
	public void constructor() {
		LoginDelegate delegate = new LoginDelegate(gson, dbInterface);
		
		String data = "{\"username\":\"asdf\",\"password\":\"asdf\"}";
		Account account = new Account("bob");
		
		
		try {
			delegate.applyLoginProcess(account, data);
			
			Account account2 = dbInterface.getAccount("bob");
			
			assertTrue(account2.getAuthenticationToken()!=null);
			
		} catch (DatabaseInterfaceException e) {
			fail();
		}
		
		
	}

}

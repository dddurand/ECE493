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

public class UploadDelegateTest {

	private static Gson gson;
	private static MockDbInterface db;
	
	@BeforeClass
	public static void setup() throws DatabaseInterfaceException
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ResponseObject.class, new ResponseObject.ResponseSerializer());
		gsonBuilder.registerTypeAdapter(UploadData.class, new UploadData.UploadDataDeserializer());
		gsonBuilder.registerTypeAdapter(Filter.class, new Filter.FilterDeserializer());
		gson = gsonBuilder.create();
		
		db = new MockDbInterface(false);
		
	}
	
	@Test
	public void test() {
		
		UploadDelegate upDel = new UploadDelegate(gson, db);
		
		Account account = new Account("bob");
		
		String data = "{\"username\":\"bob\",\"password\":\"asdf\",\"authenticationToken\":\"62b4197a-1f73-4d8f-83b7-8f3e5996276e\",\"games\": [{\"gameID\": 1,\"gameActions\": [{\"action\": \"START\",\"pot\": 122,\"bet\": 20,\"hand\": \"DA;S2\",\"communityCards\": \"\"}]}],\"miscDatas\": [{\"name\": \"MoneyGenerated\",\"value\": 1000000}]}";
		
		try {
			String result = upDel.applyAuthProcess(account, data);
			
			String expected = "{\"Code\":200,\"Success\":\"TRUE\",\"Message\":\"Upload Results\",\"game_success_uploads\":[\"1\"],\"misc_success_uploads\":[0]}";
			
			assertTrue(result.equals(expected));
			
			int value = db.getMoneyGenerated(account, new Filter());
			
			assertTrue(1000000 == value);
			
		} catch (DatabaseInterfaceException e) {
			fail();
		}
		
	}

}

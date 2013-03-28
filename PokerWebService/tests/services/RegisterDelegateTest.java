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

public class RegisterDelegateTest {

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
	public void success() {
		
		String data = "{\"username\":\"asdf1\",\"password\":\"asdf\"}";
		
		Account account = new Account("asdf1","password",false);
		RegisterDelegate test = new RegisterDelegate(gson, db);
		try {
			String json = test.unsecureProcess(account, data);
			
			String result = "{\"Code\":200,\"Success\":\"TRUE\",\"Message\":\"TRUE\"}";
			
			assertTrue(result.equals(json));
			
			assertTrue(result.equals(json));
			
			Account account2 = db.getAccount("asdf1");
			assertTrue(account2!=null);
			assertTrue(account2.getUsername().equals("asdf1"));
			
			
		} catch (DatabaseInterfaceException e) {
			fail();
		}
	}
		
		@Test
		public void failPass() {
			
			String data = "{\"username\":\"asdf2\",\"password\":\"asdf\"}";
			
			Account account = new Account("asdf2","",false);
			RegisterDelegate test = new RegisterDelegate(gson, db);
			try {
				String json = test.unsecureProcess(account, data);
				
				String result = "{\"Code\":303,\"Success\":\"FALSE\",\"Message\":\"Invalid Password Provided\"}";
				
				assertTrue(result.equals(json));
				
				Account account2 = db.getAccount("asdf2");
				assertTrue(account2==null);
				
			} catch (DatabaseInterfaceException e) {
				fail();
			}
		}
			
		@Test
		public void failuser(){
			String data = "{\"username\":\"\",\"password\":\"asdf\"}";
			
			Account account = new Account("","pass",false);
			RegisterDelegate test = new RegisterDelegate(gson, db);
			try {
				String json = test.unsecureProcess(account, data);
				
				String result = "{\"Code\":302,\"Success\":\"FALSE\",\"Message\":\"Invalid Username Provided\"}";
				
				assertTrue(result.equals(json));
				
				Account account2 = db.getAccount("");
				assertTrue(account2== null);
				
			} catch (DatabaseInterfaceException e) {
				fail();
			}
		}


		
		
		@Test
			public void dupUser() {
				
				String data = "{\"username\":\"bob\",\"password\":\"asdf\"}";
				
				Account account = new Account("bob","pass",false);
				RegisterDelegate test = new RegisterDelegate(gson, db);
				try {
					String json = test.unsecureProcess(account, data);
					
					String result = "{\"Code\":301,\"Success\":\"FALSE\",\"Message\":\"Username already in use.\"}";
					
					assertTrue(result.equals(json));
					
					Account account2 = db.getAccount("bob");
					assertTrue(account2.getUsername().equals("bob"));
					
				} catch (DatabaseInterfaceException e) {
					fail();
				}
			}
	
	}



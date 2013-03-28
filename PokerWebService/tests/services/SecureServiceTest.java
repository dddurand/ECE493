package services;

import static org.junit.Assert.*;

import mockObjects.MockDbInterface;
import mockObjects.MockDelegate;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dataModels.Filter;
import dataModels.UploadData;
import database.ResponseObject;
import database.DatabaseInterface.DatabaseInterfaceException;

public class SecureServiceTest {

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
	public void gettersSetters() {
		
		MockDelegate delegate = new MockDelegate(gson, db);
		SecureService secureService = new SecureService(delegate, gson, db);
		
		assertTrue(secureService.getdbInterface().equals(db));
		assertTrue(secureService.getGson().equals(gson));
		
		
		secureService.close();
	}
	
	
	@Test
	public void successLogin() {
		
		String secureData = "{\"username\":\"bob\",\"password\":\"pass\"}";
		
		MockDelegate delegate = new MockDelegate(gson, db);
		SecureService secureService = new SecureService(delegate, gson, db);
		String result = secureService.loginSecuredProcess(secureData);
		
		assertTrue(result.equals("SUCCESS"));
		
	}
	
	@Test
	public void loginBadUser() {
		
		String secureData = "{\"password\":\"asdf\"}";
		
		MockDelegate delegate = new MockDelegate(gson, db);
		SecureService secureService = new SecureService(delegate, gson, db);
		String result = secureService.loginSecuredProcess(secureData);
		
		String expected = "{\"Code\":302,\"Success\":\"FALSE\",\"Message\":\"Invalid username\"}";
		
		assertTrue(result.equals(expected));
		
	}
	
	@Test
	public void loginBadPass() {
		
		String secureData = "{\"username\":\"bob\"}";
		
		MockDelegate delegate = new MockDelegate(gson, db);
		SecureService secureService = new SecureService(delegate, gson, db);
		String result = secureService.loginSecuredProcess(secureData);
		
		String expected = "{\"Code\":303,\"Success\":\"FALSE\",\"Message\":\"Invalid password\"}";
		
		assertTrue(result.equals(expected));
		
	}
	
	@Test
	public void loginNoUser() {
		
		String secureData = "{\"username\":\"asdf\",\"password\":\"pass\"}";
		
		MockDelegate delegate = new MockDelegate(gson, db);
		SecureService secureService = new SecureService(delegate, gson, db);
		String result = secureService.loginSecuredProcess(secureData);
		
		String expected = "{\"Code\":304,\"Success\":\"FALSE\",\"Message\":\"User Not Found\"}";
		
		assertTrue(result.equals(expected));
		
	}
	
	@Test
	public void loginInvalidLog() {
		
		String secureData = "{\"username\":\"bob\",\"password\":\"sadf\"}";
		
		MockDelegate delegate = new MockDelegate(gson, db);
		SecureService secureService = new SecureService(delegate, gson, db);
		String result = secureService.loginSecuredProcess(secureData);
		
		String expected = "{\"Code\":300,\"Success\":\"FALSE\",\"Message\":\"Invalid Login.\"}";
		
		assertTrue(result.equals(expected));
		
	}
	
	@Test
	public void loginDBExps() {
		String secureData = "{\"username\":\"bob\",\"password\":\"pass\"}";
		MockDelegate delegate = new MockDelegate(gson, db,true, false, false);
		SecureService secureService = new SecureService(delegate, gson, db);
		String result = secureService.loginSecuredProcess(secureData);
		
		String expected = "{\"Code\":0,\"Success\":\"FALSE\",\"Message\":\"FAIL\"}";
		assertTrue(result.equals(expected));
		
		delegate = new MockDelegate(gson, db,false, true, false);
		secureService = new SecureService(delegate, gson, db);
		result = secureService.loginSecuredProcess(secureData);
		
		expected = "{\"Code\":400,\"Success\":\"FALSE\",\"Message\":\"Invalid post data.\"}";
		assertTrue(result.equals(expected));
		
		delegate = new MockDelegate(gson, db,false, false, true);
		secureService = new SecureService(delegate, gson, db);
		result = secureService.loginSecuredProcess(secureData);
	
		assertTrue(result.equals(expected));
		
	}
	
	@Test
	public void successAuth() {
		
		String secureData = "{\"username\": \"bob\",\"authenticationToken\": \"12345\"}";
		
		MockDelegate delegate = new MockDelegate(gson, db);
		SecureService secureService = new SecureService(delegate, gson, db);
		String result = secureService.authSecuredProcess(secureData);
		
		assertTrue(result.equals("SUCCESS"));
		
	}
	
	@Test
	public void authBadUser() {
		
		String secureData = "{\"authenticationToken\": \"12345\"}";
		
		MockDelegate delegate = new MockDelegate(gson, db);
		SecureService secureService = new SecureService(delegate, gson, db);
		String result = secureService.authSecuredProcess(secureData);
		
		String expected = "{\"Code\":302,\"Success\":\"FALSE\",\"Message\":\"Invalid username\"}";
		
		assertTrue(result.equals(expected));
		
	}
	
	@Test
	public void authBadAuth() {
		
		String secureData = "{\"username\": \"bob\"}";
		
		MockDelegate delegate = new MockDelegate(gson, db);
		SecureService secureService = new SecureService(delegate, gson, db);
		String result = secureService.authSecuredProcess(secureData);
		
		String expected = "{\"Code\":305,\"Success\":\"FALSE\",\"Message\":\"Invalid auth token\"}";
		
		assertTrue(result.equals(expected));
		
	}
	
	@Test
	public void authNoUser() {
		
		String secureData = "{\"username\": \"asdf\",\"authenticationToken\": \"12345\"}";
		
		MockDelegate delegate = new MockDelegate(gson, db);
		SecureService secureService = new SecureService(delegate, gson, db);
		String result = secureService.authSecuredProcess(secureData);
		
		String expected = "{\"Code\":304,\"Success\":\"FALSE\",\"Message\":\"The provided username is not registered.\"}";
		
		assertTrue(result.equals(expected));
		
	}
	
	@Test
	public void authInvalidLog() {
		
		String secureData = "{\"username\": \"bob\",\"authenticationToken\": \"123435\"}";
		
		MockDelegate delegate = new MockDelegate(gson, db);
		SecureService secureService = new SecureService(delegate, gson, db);
		String result = secureService.authSecuredProcess(secureData);
		
		String expected = "{\"Code\":306,\"Success\":\"FALSE\",\"Message\":\"The provided authtoken and username is invalid.\"}";
		
		assertTrue(result.equals(expected));
		
	}
	
	@Test
	public void authDBExps() {
		String secureData = "{\"username\": \"bob\",\"authenticationToken\": \"12345\"}";
		MockDelegate delegate = new MockDelegate(gson, db,true, false, false);
		SecureService secureService = new SecureService(delegate, gson, db);
		String result = secureService.authSecuredProcess(secureData);
		
		String expected = "{\"Code\":0,\"Success\":\"FALSE\",\"Message\":\"FAIL\"}";
		assertTrue(result.equals(expected));
		
		delegate = new MockDelegate(gson, db,false, true, false);
		secureService = new SecureService(delegate, gson, db);
		result = secureService.authSecuredProcess(secureData);
		
		expected = "{\"Code\":400,\"Success\":\"FALSE\",\"Message\":\"Invalid post data.\"}";
		assertTrue(result.equals(expected));
		
		delegate = new MockDelegate(gson, db,false, false, true);
		secureService = new SecureService(delegate, gson, db);
		result = secureService.authSecuredProcess(secureData);
	
		assertTrue(result.equals(expected));
		
	}
	
	@Test
	public void unsecureProcess() {
		
		String secureData = "{\"username\":\"bob\",\"password\":\"pass\"}";
		
		MockDelegate delegate = new MockDelegate(gson, db);
		SecureService secureService = new SecureService(delegate, gson, db);
		String result = secureService.unsecuredProcess(secureData);
		
		assertTrue(result.equals("SUCCESS"));
	}
	
	@Test
	public void unSecureDBExps() {
		String secureData = "{\"username\": \"bob\",\"authenticationToken\": \"12345\"}";
		MockDelegate delegate = new MockDelegate(gson, db,true, false, false);
		SecureService secureService = new SecureService(delegate, gson, db);
		String result = secureService.unsecuredProcess(secureData);
		
		String expected = "{\"Code\":0,\"Success\":\"FALSE\",\"Message\":\"FAIL\"}";
		assertTrue(result.equals(expected));
		
		delegate = new MockDelegate(gson, db,false, true, false);
		secureService = new SecureService(delegate, gson, db);
		result = secureService.unsecuredProcess(secureData);
		
		expected = "{\"Code\":400,\"Success\":\"FALSE\",\"Message\":\"Invalid post data.\"}";
		assertTrue(result.equals(expected));
		
		delegate = new MockDelegate(gson, db,false, false, true);
		secureService = new SecureService(delegate, gson, db);
		result = secureService.unsecuredProcess(secureData);
	
		assertTrue(result.equals(expected));
		
	}

}

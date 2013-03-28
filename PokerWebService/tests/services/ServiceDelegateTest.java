package services;

import static org.junit.Assert.*;

import mockObjects.MockDbInterface;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dataModels.Filter;
import dataModels.UploadData;
import database.ResponseObject;
import database.DatabaseInterface.DatabaseInterfaceException;

public class ServiceDelegateTest {

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
	public void test() {
		ServiceDelegate sd = new ServiceDelegate(gson, dbInterface);
		
		try {
			String test = sd.applyAuthProcess(null, null);
			test.equals("Not Implemented.");
			
			test = sd.applyLoginProcess(null, null);
			test.equals("Not Implemented.");
			
			test = sd.unsecureProcess(null, null);
			test.equals("Not Implemented.");
			
			sd.setGson(gson);
			sd.setDatabaseInterface(dbInterface);
			
			
		} catch (DatabaseInterfaceException e) {
			fail();
		}
		
	}

}

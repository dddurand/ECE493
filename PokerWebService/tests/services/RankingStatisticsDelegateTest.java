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

public class RankingStatisticsDelegateTest {

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
	public void constructor() {
		
		String data = "{\"username\":\"asdf\",\"password\":\"asdf\"}";
		
		Account account = new Account("bob");
		RankingStatisticsDelegate test = new RankingStatisticsDelegate(gson, db);
		try {
			String json = test.applyAuthProcess(account, data);
			
			String result = "{\"Code\":200,\"Success\":\"TRUE\",\"Message\":\"Ranking Results\",\"ranked_statistics\":[{\"position\":1,\"username\":\"bob\",\"rankValue\":1.0},{\"position\":1,\"username\":\"george\",\"rankValue\":2.0},{\"position\":1,\"username\":\"fred\",\"rankValue\":3.0}],\"my_ranked_statistics\":{\"position\":0,\"username\":\"bob\",\"rankValue\":-25.0}}";
			
			assertTrue(result.equals(json));
			
		} catch (DatabaseInterfaceException e) {
			fail();
		}
	}

}

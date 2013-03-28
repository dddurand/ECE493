package services;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import mockObjects.MockDbInterface;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dataModels.Account;
import dataModels.Filter;
import dataModels.UploadData;
import database.DatabaseInterface.DatabaseInterfaceException;
import database.ResponseObject;

public class CommunityStatisticsDelegateTest {

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
		
		String data = "{\"timeframe\": \"ALL\",\"username\":\"bob\",\"authenticationToken\":\"c00f1bb0-bd51-4c25-b0c4-e8f6c4992072\"}";
		
		Account account = new Account("bob");
		CommunityStatisticsDelegate test = new CommunityStatisticsDelegate(gson, db);
		try {
			String json = test.applyAuthProcess(account, data);
			
			String result = "{\"Code\":200,\"Success\":\"TRUE\",\"Message\":\"SUCCESS\",\"community_statistics\":{\"totalDollarsBetOnCalls\":20,\"totalDollarsBetOnBets\":20,\"totalDollarsBetOnRaises\":20,\"totalDollarsBetOnReRaises\":20,\"totalDollarsBet\":20,\"avgDollarsBetOnCalls\":20,\"avgDollarsBetOnBets\":20,\"avgDollarsBetOnRaises\":20,\"avgDollarsBetOnReRaises\":20,\"totalNumberOfBets\":60,\"totalNumberOfChecks\":60,\"totalNumberOfCalls\":60,\"totalNumberOfFolds\":60,\"totalNumberOfRaise\":60,\"totalNumberOfReRaise\":60,\"totalNumberOfPotsWon\":60,\"totalNumberOfPotsLoss\":60,\"totalNumberOfPots\":180,\"avgPotOnChecks\":20,\"avgPotOnCalls\":20,\"avgPotOnBets\":20,\"avgPotOnRaises\":20,\"avgPotOnReRaises\":20,\"avgPotOnFolds\":20,\"avgPotOnWins\":20,\"avgPotOnLoses\":20,\"totalDollarsWon\":20,\"totalDollarsLoss\":20,\"totalDollarsFolded\":20,\"moneyGenerated\":20,\"gamesPlayed\":5,\"netMoney\":-40,\"avgBet\":20}}";
			
			assertTrue(result.equals(json));
			
		} catch (DatabaseInterfaceException e) {
			fail();
		}
	}

}

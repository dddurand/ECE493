package database;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import mockObjects.MockDbInterface;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dataModels.Account;
import dataModels.CommunityStatistics;
import dataModels.Filter;
import dataModels.PersonalStatistics;
import dataModels.RankingStatistics;
import dataModels.RankingStatistics.RankedDataRow;
import database.DatabaseInterface.DatabaseInterfaceException;

public class ResponseObjectTest {

	@Test
	public void emptyConstructorTest() {
		@SuppressWarnings("unused")
		ResponseObject response = new ResponseObject();
		assertTrue(true);
	}
	
	@Test
	public void constructorTest() {
		boolean success = true;
		String message = "message";
		int code = 5;
		
		ResponseObject response = new ResponseObject(success, message, code);
		
		assertTrue(response.getMessage().equals(message));
		assertTrue(response.getCode() == code);
		assertTrue(response.isSuccess());
		
		response = new ResponseObject(false, message, code);
		assertTrue(response.getMessage().equals(message));
		assertTrue(response.getCode() == code);
		assertTrue(!response.isSuccess());
	}
	
	@Test
	public void setCodeTest()
	{
		boolean success = true;
		String message = "message";
		int code = 5;
		
		ResponseObject response = new ResponseObject(success, message, code);
		response.setCode(10);
		
		assertTrue(response.getMessage().equals(message));
		assertTrue(response.getCode() == 10);
		assertTrue(response.isSuccess());
	}
	
	@Test
	public void setGetCommunityStatistics()
	{
		boolean success = true;
		String message = "message";
		int code = 5;
		
		ResponseObject response = new ResponseObject(success, message, code);
		
		MockDbInterface db;
		try {
			db = new MockDbInterface(false);
			
			Filter filter = new Filter();
			CommunityStatistics stats = new CommunityStatistics(db, filter);
			
			response.setCommunityStatistics(stats);
			
			assertTrue(response.getCommunityStatistics().equals(stats));
			
		} catch (DatabaseInterfaceException e) {
			fail();
		}
	}
		
		@Test
		public void setGetRakingStatistics()
		{
			boolean success = true;
			String message = "message";
			int code = 5;
			
			ResponseObject response = new ResponseObject(success, message, code);
			
			MockDbInterface db;
			try {
				db = new MockDbInterface(false);

				RankingStatistics stats = new RankingStatistics();
				
				response.setRankingStatistics(stats);
				
				assertTrue(response.getRankingStatistics().equals(stats));
				
			} catch (DatabaseInterfaceException e) {
				fail();
			}
			
		
			
	}
		
		@Test
		public void uploadGameSuccessTest()
		{
			ArrayList<String> test = new ArrayList<String>();
			test.add("A");
			test.add("B");
			test.add("C");
			
			boolean success = true;
			String message = "message";
			int code = 5;
			
			ResponseObject response = new ResponseObject(success, message, code);
			
			MockDbInterface db;
			try {
				db = new MockDbInterface(false);

				RankingStatistics stats = new RankingStatistics();
				
				response.setUploadGameSuccess(test);
				
				assertTrue(response.getUploadGameSuccess().equals(test));
				
			} catch (DatabaseInterfaceException e) {
				fail();
			}
			
		}
		
		@Test
		public void uploadMiscSuccess()
		{
			ArrayList<Integer> test = new ArrayList<Integer>();
			test.add(1);
			test.add(2);
			test.add(3);
			
			boolean success = true;
			String message = "message";
			int code = 5;
			
			ResponseObject response = new ResponseObject(success, message, code);
			
			MockDbInterface db;
			try {
				db = new MockDbInterface(false);
				
				response.setUploadMiscSuccess(test);
				
				assertTrue(response.getUploadMiscSuccess().equals(test));
				
			} catch (DatabaseInterfaceException e) {
				fail();
			}
			
		}
		
		@Test
		public void SuccessTest()
		{
			
			boolean success = true;
			String message = "message";
			int code = 5;
			
			ResponseObject response = new ResponseObject(success, message, code);
			
			MockDbInterface db;
			try {
				db = new MockDbInterface(false);
				
				response.setSuccess(true);
				
				assertTrue(response.isSuccess());
				
				response.setSuccess(false);
				
				assertTrue(!response.isSuccess());
				
			} catch (DatabaseInterfaceException e) {
				fail();
			}
			
		}
		
		@Test
		public void MessageTest()
		{
			
			boolean success = true;
			String message = "message";
			int code = 5;
			
			ResponseObject response = new ResponseObject(success, message, code);
			
			MockDbInterface db;
			try {
				db = new MockDbInterface(false);
				
				response.setMessage("TEST");
				assertTrue(response.getMessage().equals("TEST"));
				
			} catch (DatabaseInterfaceException e) {
				fail();
			}
			
		}
		
		@Test
		public void AuthTest()
		{
			
			boolean success = true;
			String message = "message";
			int code = 5;
			
			ResponseObject response = new ResponseObject(success, message, code);
			
			MockDbInterface db;
			try {
				db = new MockDbInterface(false);
				
				response.setAuthenticationToken("ASDFGG");
				assertTrue(response.getAuthenticationToken().equals("ASDFGG"));
				
			} catch (DatabaseInterfaceException e) {
				fail();
			}
			
		}
		
		@Test
		public void personalTest()
		{
			
			boolean success = true;
			String message = "message";
			int code = 5;
			
			Account account = new Account();
			Filter filer = new Filter();
			ResponseObject response = new ResponseObject(success, message, code);
			
			MockDbInterface db;
			try {
				db = new MockDbInterface(false);
				
				PersonalStatistics stats = new PersonalStatistics(account, db, filer);
				
				response.setPersonalStatistics(stats);
				assertTrue(response.getPersonalStatistics().equals(stats));
				
			} catch (DatabaseInterfaceException e) {
				fail();
			}
			
		}
		
		@Test
		public void gsonTest()
		{
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(ResponseObject.class, new ResponseObject.ResponseSerializer());
			Gson gson = gsonBuilder.create();
			
			ResponseObject response = new ResponseObject(true, "test", 5);
			String json = gson.toJson(response);
			
			String jsonResult = "{\"Code\":5,\"Success\":\"TRUE\",\"Message\":\"test\"}";
			assertTrue(json.equals(jsonResult));
			
			response = new ResponseObject(false, "test", 5);
			response.setAuthenticationToken("ASDFG");
			json = gson.toJson(response);

			jsonResult = "{\"Code\":5,\"Success\":\"FALSE\",\"Message\":\"test\",\"AuthenticationToken\":\"ASDFG\"}";
			assertTrue(json.equals(jsonResult));
			
			response = new ResponseObject(true, "test", 5);
			
			ArrayList<String> ids = new ArrayList<String>();
			ids.add("1");
			ids.add("2");
			ids.add("3");
			
			
			response.setUploadGameSuccess(ids);
			json = gson.toJson(response);

			jsonResult = "{\"Code\":5,\"Success\":\"TRUE\",\"Message\":\"test\",\"game_success_uploads\":[\"1\",\"2\",\"3\"]}";
			assertTrue(json.equals(jsonResult));
			
			
			ArrayList<Integer> miscId = new ArrayList<Integer>();
			miscId.add(1);
			miscId.add(2);
			miscId.add(3);

			response = new ResponseObject(true, "test", 5);
			response.setUploadMiscSuccess(miscId);
			
			json = gson.toJson(response);
			
			jsonResult = "{\"Code\":5,\"Success\":\"TRUE\",\"Message\":\"test\",\"misc_success_uploads\":[1,2,3]}";
			assertTrue(json.equals(jsonResult));
			
			Account account = new Account();
			MockDbInterface db;
			try {
				db = new MockDbInterface(false);
				
				Filter filter = new Filter();
				
				PersonalStatistics stats = new PersonalStatistics(account, db, filter);
				response = new ResponseObject(true, "test", 5);
				response.setPersonalStatistics(stats);
				
				json = gson.toJson(response);
				
				jsonResult = "{\"Code\":5,\"Success\":\"TRUE\",\"Message\":\"test\",\"personal_statistics\":{\"totalDollarsBetOnCalls\":0,\"totalDollarsBetOnBets\":0,\"totalDollarsBetOnRaises\":0,\"totalDollarsBetOnReRaises\":0,\"totalDollarsBet\":0,\"avgDollarsBetOnCalls\":0,\"avgDollarsBetOnBets\":0,\"avgDollarsBetOnRaises\":0,\"avgDollarsBetOnReRaises\":0,\"totalNumberOfBets\":0,\"totalNumberOfChecks\":0,\"totalNumberOfCalls\":0,\"totalNumberOfFolds\":0,\"totalNumberOfRaise\":0,\"totalNumberOfReRaise\":0,\"totalNumberOfPotsWon\":0,\"totalNumberOfPotsLoss\":0,\"totalNumberOfPots\":0,\"avgPotOnChecks\":0,\"avgPotOnCalls\":0,\"avgPotOnBets\":0,\"avgPotOnRaises\":0,\"avgPotOnReRaises\":0,\"avgPotOnFolds\":0,\"avgPotOnWins\":0,\"avgPotOnLoses\":0,\"totalDollarsWon\":0,\"totalDollarsLoss\":0,\"totalDollarsFolded\":0,\"winPercentage\":0.0,\"moneyGenerated\":0,\"gamesPlayed\":0,\"netMoney\":0,\"avgBet\":0,\"netMoneyRanking\":0,\"optimalityRanking\":0,\"optimality\":0.0}}";
				assertTrue(json.equals(jsonResult));
				
				
			} catch (DatabaseInterfaceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			account = new Account();
			try {
				db = new MockDbInterface(false);
				
				ArrayList<RankedDataRow> data = new ArrayList<RankedDataRow>();
				RankedDataRow test = new RankedDataRow(1, "asdf", 20);
				data.add(test);
				
				RankingStatistics stats = new RankingStatistics(data, test);
				response = new ResponseObject(true, "test", 5);
				response.setRankingStatistics(stats);
				
				json = gson.toJson(response);
				
				jsonResult = "{\"Code\":5,\"Success\":\"TRUE\",\"Message\":\"test\",\"ranked_statistics\":[{\"position\":1,\"username\":\"asdf\",\"rankValue\":20.0}],\"my_ranked_statistics\":{\"position\":1,\"username\":\"asdf\",\"rankValue\":20.0}}";
				assertTrue(json.equals(jsonResult));
				
				
			} catch (DatabaseInterfaceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			account = new Account();
			try {
				db = new MockDbInterface(false);
				
				Filter filter = new Filter();
				
				CommunityStatistics stats = new CommunityStatistics(db, filter);
				response = new ResponseObject(true, "test", 5);
				response.setCommunityStatistics(stats);
				
				json = gson.toJson(response);
				
				jsonResult = "{\"Code\":5,\"Success\":\"TRUE\",\"Message\":\"test\",\"community_statistics\":{\"totalDollarsBetOnCalls\":0,\"totalDollarsBetOnBets\":0,\"totalDollarsBetOnRaises\":0,\"totalDollarsBetOnReRaises\":0,\"totalDollarsBet\":0,\"avgDollarsBetOnCalls\":0,\"avgDollarsBetOnBets\":0,\"avgDollarsBetOnRaises\":0,\"avgDollarsBetOnReRaises\":0,\"totalNumberOfBets\":0,\"totalNumberOfChecks\":0,\"totalNumberOfCalls\":0,\"totalNumberOfFolds\":0,\"totalNumberOfRaise\":0,\"totalNumberOfReRaise\":0,\"totalNumberOfPotsWon\":0,\"totalNumberOfPotsLoss\":0,\"totalNumberOfPots\":0,\"avgPotOnChecks\":0,\"avgPotOnCalls\":0,\"avgPotOnBets\":0,\"avgPotOnRaises\":0,\"avgPotOnReRaises\":0,\"avgPotOnFolds\":0,\"avgPotOnWins\":0,\"avgPotOnLoses\":0,\"totalDollarsWon\":0,\"totalDollarsLoss\":0,\"totalDollarsFolded\":0,\"moneyGenerated\":0,\"gamesPlayed\":0,\"netMoney\":0,\"avgBet\":0}}";
				assertTrue(json.equals(jsonResult));
				
				
			} catch (DatabaseInterfaceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			int test = 0; test++;
			
		}
		

}

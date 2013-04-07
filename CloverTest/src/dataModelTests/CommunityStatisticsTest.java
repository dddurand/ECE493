package dataModelTests;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.test.AndroidTestCase;
import dataModels.Account;
import dataModels.CommunityStatistics;
import dataModels.CommunityStatistics.CommuntyStatisticRequest;
import dataModels.SimpleStatistic;
import dataModels.TimeFrame;

public class CommunityStatisticsTest extends AndroidTestCase  {

	private Hashtable<String, Double> values = new Hashtable<String, Double>();
	
	public void tearDown() throws Exception {
	    ///CLOVER:FLUSH
	    super.tearDown();
	}
	
	public void testValid()
	{
		JSONObject obj = this.getJSON();
		Context context = this.getContext();
		
		
		CommunityStatistics stats = new CommunityStatistics(obj, context);
		ArrayList<SimpleStatistic> statsList = stats.getAllStatistics();
		
		for(SimpleStatistic stat : statsList)
		{
			String key = stat.getIdentifier();
			Double value = stat.getValueAsDouble();
			String display = stat.getDisplayName();
			
			assertTrue(key!=null);
			assertTrue(values.keySet().contains(key));
			
			assertTrue(values.get(key).equals(value));
			
			String packageName = context.getPackageName();
		    int resId = context.getResources().getIdentifier(key, "string", packageName);
			String display2 = context.getString(resId);
		    
			assertTrue(display.equals(display2));
			
		}
		
	}
	
	public void testNoStats()
	{
		JSONObject obj = new JSONObject();
		Context context = this.getContext();
		
		
		CommunityStatistics stats = new CommunityStatistics(obj, context);
		ArrayList<SimpleStatistic> statsList = stats.getAllStatistics();
	
		assertTrue(statsList.isEmpty());
		
	}
	
	public void testInvalid()
	{
		JSONObject obj = new JSONObject();
		Context context = this.getContext();
		
		JSONObject invalid = new JSONObject();
		
		try {
			obj.put("community_statistics", invalid);
		} catch (JSONException e) {
			fail();
		}
		
		CommunityStatistics stats = new CommunityStatistics(obj, context);
		ArrayList<SimpleStatistic> statsList = stats.getAllStatistics();
	
		assertTrue(statsList.isEmpty());
		
	}
	
	private JSONObject getJSON()
	{
		JSONObject obj = new JSONObject();
		for(String key : values.keySet())
		{
			try {
				obj.put(key, values.get(key));
			} catch (JSONException e) {
				fail();
			}
		}
		
		JSONObject objwrapper = new JSONObject();
		try {
			objwrapper.put("community_statistics", obj);
		} catch (JSONException e) {
			fail();
		}
		
		return objwrapper;
	}
	
	public void setUp()
	{
		
		values = new Hashtable<String, Double>();
		values.put("totalDollarsBetOnCalls", 1.0);
		values.put("totalDollarsBetOnBets", 2.0);
		values.put("totalDollarsBetOnRaises", 3.0);
		values.put("totalDollarsBetOnReRaises", 4.0);
		values.put("totalDollarsBet", 5.0);
		values.put("avgDollarsBetOnCalls", 6.0);
		values.put("avgDollarsBetOnBets", 7.0);
		values.put("avgDollarsBetOnRaises", 8.0);
		values.put("avgDollarsBetOnReRaises", 9.0);
		values.put("totalNumberOfBets", 10.0);
		values.put("totalNumberOfChecks", 11.0);
		values.put("totalNumberOfCalls", 12.0);
		values.put("totalNumberOfFolds", 13.0);
		values.put("totalNumberOfRaise", 14.0);
		values.put("totalNumberOfReRaise", 15.0);
		values.put("totalNumberOfPotsWon", 16.0);
		values.put("totalNumberOfPotsLoss", 17.0);
		values.put("totalNumberOfPots", 18.0);
		values.put("avgPotOnChecks", 19.0);
		values.put("avgPotOnCalls", 20.0);
		values.put("avgPotOnBets", 21.0);
		values.put("avgPotOnRaises", 22.0);
		values.put("avgPotOnReRaises", 23.0);
		values.put("avgPotOnFolds", 24.0);
		values.put("avgPotOnWins", 25.0);
		values.put("avgPotOnLoses", 26.0);
		values.put("totalDollarsWon", 27.0);
		values.put("totalDollarsLoss", 28.0);
		values.put("totalDollarsFolded", 29.0);
		values.put("winPercentage", 30.0);
		values.put("moneyGenerated", 31.0);
		values.put("gamesPlayed", 32.0);
		values.put("netMoney", 33.0);
		values.put("avgBet", 34.0);
	}
	
	public void testRequestSetTimeFrame()
	{
		Account account = new Account("user", "auth", 0);
		
		CommuntyStatisticRequest request = new CommuntyStatisticRequest(TimeFrame.ALL, account);
		request.setTimeFrame(TimeFrame.MONTH);
		
		try {
			JSONObject obj = request.getJSON();
			assertTrue(obj.getString("timeframe").equals(TimeFrame.MONTH.getValue()));
		
		} catch (JSONException e) {
			fail();
		}
	}
	
	public void testRequest()
	{
		Account account = new Account("user", "auth", 0);
		
		CommuntyStatisticRequest request = new CommuntyStatisticRequest(TimeFrame.DAY, account);
		
		try {
			JSONObject obj = request.getJSON();
			assertTrue(obj.getString("username").equals("user"));
			assertTrue(obj.getString("authenticationToken").equals("auth"));
			assertTrue(obj.getString("timeframe").equals(TimeFrame.DAY.getValue()));
		
		} catch (JSONException e) {
			fail();
		}
		
		
		
		
	}
	
}

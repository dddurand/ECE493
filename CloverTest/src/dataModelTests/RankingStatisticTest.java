package dataModelTests;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.test.AndroidTestCase;
import dataModels.Account;
import dataModels.RankingStatistics;
import dataModels.RankingStatistics.RankingStatisticRequest;
import dataModels.RankingStatistics.RankingStatisticRequest.RankType;
import dataModels.SimpleRankStatistic;
import dataModels.TimeFrame;

public class RankingStatisticTest extends AndroidTestCase {

private ArrayList<SimpleRankStatistic> statsList = new ArrayList<SimpleRankStatistic>();
private SimpleRankStatistic myStats;

	public void tearDown() throws Exception {
	    ///CLOVER:FLUSH
	    super.tearDown();
	}
	
	public void testValid()
	{
		JSONObject obj = this.getJSON();
		
		
		RankingStatistics stats = new RankingStatistics(obj);
		ArrayList<SimpleRankStatistic> statsList = stats.getAllStatistics();
		
		assertTrue(statsList.size() == this.statsList.size());
		
		for(int i = 0; i< statsList.size(); i++)
		{
			SimpleRankStatistic a = this.statsList.get(i);
			SimpleRankStatistic b = statsList.get(i);
			
			assertTrue(a.getUsername().equals(b.getUsername()));
			assertTrue(a.getUsername().equals(b.getUsername()));
			assertTrue(a.getRankPosition() == b.getRankPosition());
			assertTrue(a.getRankValue().doubleValue() == b.getRankValue().doubleValue());
		}
		
		SimpleRankStatistic me = stats.getMyRankStats();
		assertTrue(me.getUsername().equals(this.myStats.getUsername()));
		assertTrue(me.getUsername().equals(this.myStats.getUsername()));
		assertTrue(me.getRankPosition() == this.myStats.getRankPosition());
		assertTrue(me.getRankValue().doubleValue() == this.myStats.getRankValue().doubleValue());
		
	}
	
	public void testNoStats()
	{
		JSONObject obj = new JSONObject();
		
		
		RankingStatistics stats = new RankingStatistics(obj);
		ArrayList<SimpleRankStatistic> statsList = stats.getAllStatistics();
	
		assertTrue(statsList.isEmpty());
		
	}
	
	public void testInvalid()
	{
		JSONObject obj = new JSONObject();
		
		JSONObject invalid = new JSONObject();
		
		try {
			obj.put("ranked_statistics", invalid);
		} catch (JSONException e) {
			fail();
		}
		
		try {
			obj.put("my_ranked_statistics", invalid);
		} catch (JSONException e) {
			fail();
		}
		
		RankingStatistics stats = new RankingStatistics(obj);
		ArrayList<SimpleRankStatistic> statsList = stats.getAllStatistics();
	
		assertTrue(statsList.isEmpty());
		assertTrue(stats.getMyRankStats() == null);
		
	}
	
	private JSONObject getJSON()
	{
		JSONObject objWrap = new JSONObject();
		JSONArray array = new JSONArray();
		
		for(SimpleRankStatistic stat : this.statsList)
		{
			JSONObject obj = new JSONObject();
			try{
			obj.put("position", stat.getRankPosition());
			obj.put("username", stat.getUsername());
			obj.put("rankValue", stat.getRankValue());
			} catch (Exception w){fail();}
			
			array.put(obj);
		}
		
		try {
			objWrap.put("ranked_statistics", array);
		} catch (JSONException e) {
			fail();
		}
		
		JSONObject obj = new JSONObject();
		try{
		obj.put("position", this.myStats.getRankPosition());
		obj.put("username", this.myStats.getUsername());
		obj.put("rankValue", this.myStats.getRankValue());
		} catch (Exception w){fail();}
		
		try {
			objWrap.put("my_ranked_statistics", obj);
		} catch (JSONException e) {
			fail();
		}
		
		return objWrap;
	}
	
	public void setUp()
	{
		
		for(int i = 0; i < 100; i++)
		{
			SimpleRankStatistic stat = new SimpleRankStatistic("user"+i, Double.valueOf(i*i), i);
			this.statsList.add(stat);
		}
		
		myStats = new SimpleRankStatistic("me", Double.valueOf(123.2), 1);
		
	}
	
	public void testRequestCons()
	{
		TimeFrame time = TimeFrame.ALL;
		String user = "user";
		String auth = "auth";
		
		
		Account account = new Account(user, auth, 0);
		
		RankingStatisticRequest request = new RankingStatisticRequest(time, account);
		try {
			JSONObject obj = request.getJSON();
			String time2 = obj.getString("timeframe");
			int skip = obj.getInt("skip");
			int max = obj.getInt("max");
			String type = obj.getString("rank_type");
			
			assertTrue(time2.equals(time.getValue()));
			assertTrue(skip == 0);
			assertTrue(max == Integer.MAX_VALUE);
			assertTrue(type.equals(RankType.NET_MONEY.getValue()));
			
		} catch (JSONException e) {
			fail();
		}
		
		
		
	}
	
	public void testRequestCons2()
	{
		
		TimeFrame time = TimeFrame.ALL;
		String user = "user";
		String auth = "auth";
		
		int max = 2;
		int skip = 1;
		RankType type = RankType.OPTIMALITY;
		
		Account account = new Account(user, auth, 0);
		
		RankingStatisticRequest request = new RankingStatisticRequest(time, account,skip,max,type);
		try {
			JSONObject obj = request.getJSON();
			String time2 = obj.getString("timeframe");
			int skip2 = obj.getInt("skip");
			int max2 = obj.getInt("max");
			String type2 = obj.getString("rank_type");
			
			assertTrue(time2.equals(time.getValue()));
			assertTrue(skip == skip2);
			assertTrue(max == max2);
			assertTrue(type2.equals(type.getValue()));
			
		} catch (JSONException e) {
			fail();
		}
		
	}
	
	public void testRequestSetters()
	{
		
		TimeFrame time = TimeFrame.ALL;
		String user = "user";
		String auth = "auth";
		
		int max = 2;
		int skip = 1;
		RankType type = RankType.OPTIMALITY;
		
		Account account = new Account(user, auth, 0);
		
		RankingStatisticRequest request = new RankingStatisticRequest(time, account,skip,max,type);
		request.setRankType(RankType.NET_MONEY);
		request.setTimeFrame(TimeFrame.DAY);
		
		try {
			JSONObject obj = request.getJSON();
			String time2 = obj.getString("timeframe");
			int skip2 = obj.getInt("skip");
			int max2 = obj.getInt("max");
			String type2 = obj.getString("rank_type");
			
			assertTrue(time2.equals(TimeFrame.DAY.getValue()));
			assertTrue(skip == skip2);
			assertTrue(max == max2);
			assertTrue(type2.equals(RankType.NET_MONEY.getValue()));
			
		} catch (JSONException e) {
			fail();
		}
		
		
		
		
		
	}
	
}

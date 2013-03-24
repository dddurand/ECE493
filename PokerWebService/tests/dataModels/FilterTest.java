package dataModels;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import dataModels.Filter.RankType;
import dataModels.Filter.TimeFrame;

public class FilterTest {

	@Test
	public void testFilterConstructor() 
	{
		Filter filter = new Filter();
		
		assertTrue(filter.getTimeFrame() == TimeFrame.ALL);
		assertTrue(filter.getSkip() == 0);
		assertTrue(filter.getMax() == Integer.MAX_VALUE);
		assertTrue(filter.getRankType() == RankType.NET_MONEY);
		
	}
	
	@Test
	public void testFilterTimeFrameConstructor() 
	{
		TimeFrame timeFrame = TimeFrame.MONTH;
		Filter filter = new Filter(timeFrame.getValue());
		
		assertTrue(filter.getTimeFrame() == TimeFrame.MONTH);
		assertTrue(filter.getSkip() == 0);
		assertTrue(filter.getMax() == Integer.MAX_VALUE);
		assertTrue(filter.getRankType() == RankType.NET_MONEY);
		
	}
	
	@Test
	public void testFilterTF_SK_MX_Constructor() 
	{
		int skip = 1;
		int max = 5;
		TimeFrame timeFrame = TimeFrame.MONTH;
		Filter filter = new Filter(timeFrame.getValue(), skip, max);
		
		assertTrue(filter.getTimeFrame() == TimeFrame.MONTH);
		assertTrue(filter.getSkip() == skip);
		assertTrue(filter.getMax() == max);
		assertTrue(filter.getRankType() == RankType.NET_MONEY);
		
	}
	
	@Test
	public void testFilterFullConstructor() 
	{
		int skip = 1;
		int max = 5;
		TimeFrame timeFrame = TimeFrame.MONTH;
		RankType type = RankType.OPTIMALITY;
		
		Filter filter = new Filter(timeFrame.getValue(), skip, max, type);
		
		assertTrue(filter.getTimeFrame() == TimeFrame.MONTH);
		assertTrue(filter.getSkip() == skip);
		assertTrue(filter.getMax() == max);
		assertTrue(filter.getRankType() == type);
		
	}
	
	@Test
	public void testGetTimeFrame() 
	{
		int skip = 1;
		int max = 5;
		TimeFrame timeFrame = TimeFrame.MONTH;
		RankType type = RankType.OPTIMALITY;
		
		Filter filter = new Filter(timeFrame.getValue(), skip, max, type);
		assertTrue(filter.getTimeFrame() == TimeFrame.MONTH);
	}
	
	@Test
	public void testSetTimeFrame() 
	{
		int skip = 1;
		int max = 5;
		TimeFrame timeFrame = TimeFrame.MONTH;
		RankType type = RankType.OPTIMALITY;
		
		Filter filter = new Filter(timeFrame.getValue(), skip, max, type);
		filter.setTimeFrame(TimeFrame.ALL);
		assertTrue(filter.getTimeFrame() == TimeFrame.ALL);
	}
	
	@Test
	public void testSetTimeFrameString() 
	{
		int skip = 1;
		int max = 5;
		TimeFrame timeFrame = TimeFrame.MONTH;
		RankType type = RankType.OPTIMALITY;
		
		Filter filter = new Filter(timeFrame.getValue(), skip, max, type);
		filter.setTimeFrame("MONTH");
		assertTrue(filter.getTimeFrame() == TimeFrame.MONTH);
	}
	
	@Test
	public void testGetRankType() 
	{
		int skip = 1;
		int max = 5;
		TimeFrame timeFrame = TimeFrame.MONTH;
		RankType type = RankType.OPTIMALITY;
		
		Filter filter = new Filter(timeFrame.getValue(), skip, max, type);
		assertTrue(filter.getRankType() == RankType.OPTIMALITY);
		
		filter.setRankType("asf");
		assertTrue(filter.getRankType() == RankType.NET_MONEY);
	}
	
	@Test
	public void testSetRankType() 
	{
		int skip = 1;
		int max = 5;
		TimeFrame timeFrame = TimeFrame.MONTH;
		RankType type = RankType.OPTIMALITY;
		
		Filter filter = new Filter(timeFrame.getValue(), skip, max, type);
		filter.setRankType(RankType.NET_MONEY);
		assertTrue(filter.getRankType() == RankType.NET_MONEY);
	}
	
	@Test
	public void testSetRankTypeString() 
	{
		int skip = 1;
		int max = 5;
		TimeFrame timeFrame = TimeFrame.MONTH;
		RankType type = RankType.OPTIMALITY;
		
		Filter filter = new Filter(timeFrame.getValue(), skip, max, type);
		filter.setRankType(RankType.NET_MONEY.getValue());
		assertTrue(filter.getRankType() == RankType.NET_MONEY);
	}
	
	@Test
	public void testGetSkip() 
	{
		int skip = 1;
		int max = 5;
		TimeFrame timeFrame = TimeFrame.MONTH;
		RankType type = RankType.OPTIMALITY;
		
		Filter filter = new Filter(timeFrame.getValue(), skip, max, type);
		assertTrue(filter.getSkip() == skip);
	}

	@Test
	public void testSetSkip() 
	{
		int skip = 1;
		int max = 5;
		TimeFrame timeFrame = TimeFrame.MONTH;
		RankType type = RankType.OPTIMALITY;
		
		Filter filter = new Filter(timeFrame.getValue(), skip, max, type);
		filter.setSkip(5);
		assertTrue(filter.getSkip() == 5);
	}
	
	@Test
	public void testGetMax() 
	{
		int skip = 1;
		int max = 5;
		TimeFrame timeFrame = TimeFrame.MONTH;
		RankType type = RankType.OPTIMALITY;
		
		Filter filter = new Filter(timeFrame.getValue(), skip, max, type);
		assertTrue(filter.getMax() == max);
	}

	@Test
	public void testSetMax() 
	{
		int skip = 1;
		int max = 5;
		TimeFrame timeFrame = TimeFrame.MONTH;
		RankType type = RankType.OPTIMALITY;
		
		Filter filter = new Filter(timeFrame.getValue(), skip, max, type);
		filter.setMax(6);
		assertTrue(filter.getMax() == 6);
	}
	
	@Test
	public void testgetSqlTimeFrameFilter() 
	{
		String day = " and date_uploaded >= DATE_ADD(CURRENT_DATE(), INTERVAL -1 DAY) ";
		String week = " and date_uploaded >= DATE_ADD(CURRENT_DATE(), INTERVAL -1 WEEK) ";
		String month = " and date_uploaded >= DATE_ADD(CURRENT_DATE(), INTERVAL -1 MONTH) ";
		String year = " and date_uploaded >= DATE_ADD(CURRENT_DATE(), INTERVAL -1 YEAR) ";
		
		
		Filter filter = new Filter(TimeFrame.MONTH.getValue());
		assertTrue(filter.getSqlTimeFrameFilter().equals(month));
		
		filter = new Filter(TimeFrame.WEEK.getValue());
		assertTrue(filter.getSqlTimeFrameFilter().equals(week));
		
		filter = new Filter(TimeFrame.DAY.getValue());
		assertTrue(filter.getSqlTimeFrameFilter().equals(day));
		
		filter = new Filter(TimeFrame.YEAR.getValue());
		assertTrue(filter.getSqlTimeFrameFilter().equals(year));
		
		filter = new Filter("asdf");
		assertTrue(filter.getSqlTimeFrameFilter().equals(""));
	}
	
	@Test
	public void testGetSkipSqlFilter() 
	{
		int skip = 1;
		int max = 5;
		TimeFrame timeFrame = TimeFrame.MONTH;
		RankType type = RankType.OPTIMALITY;
		
		Filter filter = new Filter(timeFrame.getValue(), skip, max, type);
		
		String value = " OFFSET " + skip + " ";
		
		assertTrue(filter.getSkipSqlFilter().equals(value));
		
	}
	
	@Test
	public void testGetMaxSqlFilter() 
	{
		int skip = 1;
		int max = 5;
		TimeFrame timeFrame = TimeFrame.MONTH;
		RankType type = RankType.OPTIMALITY;
		
		Filter filter = new Filter(timeFrame.getValue(), skip, max, type);
		
		String value = " LIMIT " + max + " ";
		
		assertTrue(filter.getMaxSqlFilter().equals(value));
		
	}
	
	@Test
	public void testTimeFrame() 
	{
		TimeFrame timeFrame = TimeFrame.DAY;
		assertTrue(timeFrame.getValue().equals("DAY"));
		
		assertTrue(TimeFrame.getTimeFrame(TimeFrame.DAY.getValue())==TimeFrame.DAY);	
	}
	
	@Test
	public void testRankType() 
	{
		RankType type = RankType.OPTIMALITY;
		
		assertTrue(type.getValue().equals("optimality"));	
		assertTrue(RankType.getRankType(RankType.OPTIMALITY.getValue())==RankType.OPTIMALITY);	
	}
	
	@Test
	public void testDeserialize()
	{
		int skip = 1;
		int max = 5;
		TimeFrame timeFrame = TimeFrame.MONTH;
		RankType type = RankType.OPTIMALITY;
		
		JsonObject obj = new JsonObject();
		obj.add("timeframe", new JsonPrimitive(timeFrame.getValue()));
		obj.add("skip", new JsonPrimitive(skip));
		obj.add("max", new JsonPrimitive(max));
		obj.add("rank_type", new JsonPrimitive(type.getValue()));
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Filter.class, new Filter.FilterDeserializer());
		Gson gson = gsonBuilder.create();
		
		Filter filter = gson.fromJson(obj, Filter.class);
		
		assertTrue(filter.getMax() == max);
		assertTrue(filter.getSkip() == skip);
		assertTrue(filter.getRankType() == type);
		assertTrue(filter.getTimeFrame() == timeFrame);
		
	}
	
}

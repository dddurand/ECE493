package dataModelTests;

import dataModels.SimpleRankStatistic;
import android.test.AndroidTestCase;

public class SimpleRankStatisticTest extends AndroidTestCase {

	public void testCons()
	{
		String username = "user";
		Double value = 10.0;
		int position = 2;
		
		SimpleRankStatistic srs = new SimpleRankStatistic(username, value, position);
		
		int position2 = srs.getRankPosition();
		double value2 = srs.getRankValue().doubleValue();
		String username2 = srs.getUsername();
		
		assertTrue(position == position2);
		assertTrue(value.doubleValue() == value2);
		assertTrue(username.equals(username2));
		
	}
	
	public void testSets()
	{
		String username = "user";
		Double value = 10.0;
		int position = 2;
		
		SimpleRankStatistic srs = new SimpleRankStatistic(username, value, position);
		String username_ = "user1";
		Double value_ = 110.0;
		int position_ = 21;
		
		srs.setRankPosition(position_);
		srs.setUsername(username_);
		srs.setRankValue(value_);
		
		int position2 = srs.getRankPosition();
		double value2 = srs.getRankValue().doubleValue();
		String username2 = srs.getUsername();
		
		assertTrue(position_ == position2);
		assertTrue(value_.doubleValue() == value2);
		assertTrue(username_.equals(username2));
		
	}
	
}

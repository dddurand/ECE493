package misc;

import android.test.AndroidTestCase;

public class RankStatsRowObjectTest extends AndroidTestCase {

	public void consTest()
	{
		String position = "a";
		String score = "good";
		String user = "user";
		
		RankStatsRowObject data = new RankStatsRowObject(position,user, score);
		
		assertTrue(data.getPos().equals(position));
		assertTrue(data.getScore().equals(score));
		assertTrue(data.getUser().equals(user));
		
	}
	
}

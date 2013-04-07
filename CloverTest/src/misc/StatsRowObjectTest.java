package misc;

import android.test.AndroidTestCase;

public class StatsRowObjectTest  extends AndroidTestCase {

	public void consTest()
	{
		String left = "left";
		String right = "right";

		StatsRowObject data = new StatsRowObject(left, right);
		
		assertTrue(data.getProp1().equals(left));
		assertTrue(data.getProp2().equals(right));
		
	}
	
	
}

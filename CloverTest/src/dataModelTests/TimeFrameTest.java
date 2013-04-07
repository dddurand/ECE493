package dataModelTests;

import dataModels.TimeFrame;
import android.test.AndroidTestCase;

public class TimeFrameTest extends AndroidTestCase {

	public void testBasic()
	{
		TimeFrame time = TimeFrame.ALL;
		String timeString = time.getValue();
		assertTrue(TimeFrame.getTimeFrame(timeString) == time);	
	}
	
}

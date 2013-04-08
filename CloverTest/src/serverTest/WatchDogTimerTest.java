package serverTest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import android.test.AndroidTestCase;

import server.GameAction;
import server.WatchDogTimer;
import server.GameAction.PokerAction;

public class WatchDogTimerTest extends AndroidTestCase {

	public void testValid()
	{
		BlockingQueue<GameAction> queue = new LinkedBlockingQueue<GameAction>();
		int timeout = 1;
		WatchDogTimer wd = new WatchDogTimer(queue, timeout);
		
		wd.startTimer();
	
		
		try {
			GameAction action = queue.poll(2500, TimeUnit.MILLISECONDS);
			assertTrue(action.getAction() == PokerAction.TIMEOUT);
			
		} catch (InterruptedException e) {
			fail();
		}
		
		
	}
	
	public void testCanceled()
	{
		BlockingQueue<GameAction> queue = new LinkedBlockingQueue<GameAction>();
		int timeout = 1;
		WatchDogTimer wd = new WatchDogTimer(queue, timeout);
		
		wd.startTimer();
		wd.cancel();
		wd.cancel();
		
		try {
			GameAction action = queue.poll(2500, TimeUnit.MILLISECONDS);
			assertTrue(action == null);
			
		} catch (InterruptedException e) {
			fail();
		}
		
		
	}

}

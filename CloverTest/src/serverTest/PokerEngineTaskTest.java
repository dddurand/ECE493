package serverTest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import server.GameAction;
import server.PokerEngineTask;
import server.GameAction.PokerAction;
import serverTest.mock.MockGameMechanics;
import android.test.AndroidTestCase;

public class PokerEngineTaskTest extends AndroidTestCase {

	public void testValid()
	{
		BlockingQueue<GameAction> queue = new LinkedBlockingQueue<GameAction>();
		MockGameMechanics mech = new MockGameMechanics();
		
		PokerEngineTask task = new PokerEngineTask(queue, mech);
		
		Thread th = new Thread(task);
		
		GameAction action = new GameAction(PokerAction.FOLD);
		queue.add(action);
		
		th.start();
		
		Thread.yield();
		
		try {
			Thread.sleep(1000);
			task.cancel();
			th.join();
		} catch (InterruptedException e) {
			fail();
		}
		
		if(!mech.isProcessCalled())
			fail();
		
		if(!mech.getAction().equals(action))
			fail();
		
	}
	
}

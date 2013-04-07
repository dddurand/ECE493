package client;

import java.util.concurrent.Semaphore;

import android.test.AndroidTestCase;

public class ClientListenerTaskCompleteNotifyTest extends AndroidTestCase{

	public void testBasic()
	{
		ClientListenerTaskCompleteNotify notify = new ClientListenerTaskCompleteNotify();
		
		ActionCatcher ct = new ActionCatcher();
		notify.setListener(ct);
		notify.run();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			fail();
		}
		
		ct.isRun();
		
		
		
	}

 public static class ActionCatcher implements ClientTaskListener
 {

	 private boolean isRun = false;
	 
	@Override
	public void onPlayerTaskClose() {
		this.isRun = true;
	}

	/**
	 * @return the isRun
	 */
	public boolean isRun() {
		return isRun;
	}

	/**
	 * @param isRun the isRun to set
	 */
	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}
	
	
	 
 }
	
}

package applicationTest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Semaphore;

import android.bluetooth.BluetoothSocket;
import android.test.AndroidTestCase;
import application.PokerApplication;
import dataModels.Account;

public class applicationTest extends AndroidTestCase {

	public void GetsSetsTest()
	{
		
		Account account = new Account("bob", 0);
		boolean isLoggedIn = true;
		Semaphore uploadServiceSemaphore = new Semaphore(0);
		BluetoothSocket blueSocket[] = new BluetoothSocket[1];
		ObjectOutputStream outStream[] = new ObjectOutputStream[2];
		ObjectInputStream inStream[] = new ObjectInputStream[3];
		
		PokerApplication app = new PokerApplication();
		app.setAccount(account);
		app.setInStream(inStream);
		app.setOutStream(outStream);
		app.setSocket(blueSocket);
		app.setLoggedIn(isLoggedIn);
		
		assertTrue(account.equals(app.getAccount()));
		assertTrue(isLoggedIn == app.isLoggedIn());
		assertTrue(uploadServiceSemaphore.equals(app.getUploadServiceSemaphore()));
		assertTrue(blueSocket.equals(app.getSocket()));
		assertTrue(outStream.equals(app.getOutStream()));
		assertTrue(inStream.equals(app.getInStream()));
		
		
	}
	
	
	
}

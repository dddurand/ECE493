package bluetoothTest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import android.test.AndroidTestCase;
import bluetooth.holder;

public class HolderTest extends AndroidTestCase {

	public void testCons()
	{
		int balance = 25;
		String username = "bob";
		holder Holder = new holder(balance, username);
		
		assertTrue(Holder.getBalance() == balance);
		assertTrue(Holder.getName().equals(username));	
	}
	
	public void testWriteObject()
	{
		
		try{
		int balance = 25;
		String username = "bob";
		holder Holder = new holder(balance, username);
		
		PipedInputStream keepInStream = new PipedInputStream(1000);
		
		PipedOutputStream keepOutStream = new PipedOutputStream(keepInStream);
		
		
		ObjectOutputStream aOut = new ObjectOutputStream(keepOutStream);
		aOut.flush();
		
		ObjectInputStream aIn = new ObjectInputStream(keepInStream);
		
		aOut.writeObject(Holder);
		
		holder holder2 = (holder)aIn.readObject();
		
		assertTrue(holder2.getName().equals(username));
		assertTrue(holder2.getBalance() == balance);
		
		aOut.close();
		
		} catch(Exception e)
		{
			fail();
		}
		
	}
	


}

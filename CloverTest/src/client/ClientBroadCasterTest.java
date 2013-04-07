package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.LinkedBlockingQueue;

import server.GameAction;
import server.GameAction.PokerAction;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import client.ClientListenerTaskCompleteNotifyTest.ActionCatcher;
import client.mock.PlayAreaMock;

public class ClientBroadCasterTest  extends ActivityUnitTestCase<PlayAreaMock> implements ClientTaskListener  {

	public ClientBroadCasterTest() {
		super(PlayAreaMock.class);
	}
	
	public void testValid()
	{
		try{
		PlayAreaMock activity = this.startActivity(new Intent(), null, null);
		
		PipedInputStream keepInStream = new PipedInputStream(1000);
		
		PipedOutputStream sendOutStream = new PipedOutputStream(keepInStream);
		
		final ObjectOutputStream bOut = new ObjectOutputStream(sendOutStream);
		bOut.flush();
		sendOutStream.flush();
		
		ObjectInputStream aIn = new ObjectInputStream(keepInStream);
		LinkedBlockingQueue<GameAction> queue = new LinkedBlockingQueue<GameAction>();
		
		ClientBroadCaster cb = new ClientBroadCaster(queue, activity, bOut);
		
		GameAction action = new GameAction(PokerAction.BET);
		
		ActionCatcher ct = new ActionCatcher();
		cb.addListener(ct);
		
		queue.add(action);
		
		Thread th = new Thread(cb);
		th.start();
		
		GameAction action2 =(GameAction) aIn.readObject();
		
		cb.cancel();
		queue.add(action2);
		
		assertTrue(action.getAction() == action2.getAction());
		assertTrue(action.getPosition() == action2.getPosition());
		
		
		try {
			th.join();
		} catch (InterruptedException e) {
			fail();
		}
		
		try {
			Thread.sleep(1000);
			Thread.yield();
		} catch (InterruptedException e) {
			fail();
		}
		
		}catch(IOException e)
		{
			fail();
		} catch (ClassNotFoundException e) {
			fail();
		}
	}
	
	public void testCanceled()
	{
		try{
		PlayAreaMock activity = this.startActivity(new Intent(), null, null);
		
		PipedInputStream keepInStream = new PipedInputStream(1000);
		
		PipedOutputStream sendOutStream = new PipedOutputStream(keepInStream);
		
		final ObjectOutputStream bOut = new ObjectOutputStream(sendOutStream);
		bOut.flush();
		sendOutStream.flush();
		
		ObjectInputStream aIn = new ObjectInputStream(keepInStream);
		LinkedBlockingQueue<GameAction> queue = new LinkedBlockingQueue<GameAction>();
		
		ClientBroadCaster cb = new ClientBroadCaster(queue, activity, bOut);
		
		GameAction action = new GameAction(PokerAction.BET);
		
		cb.addListener(this);
		
		queue.add(action);
		
		cb.cancel();
		Thread th = new Thread(cb);
		th.start();
		
		if(activity.isUpdated())
		{
			fail();
		}
		
		}catch(IOException e)
		{
			fail();
		}
	}
	
	public void testClosed()
	{
		try{
		PlayAreaMock activity = this.startActivity(new Intent(), null, null);
		
		PipedInputStream keepInStream = new PipedInputStream(1000);
		
		PipedOutputStream sendOutStream = new PipedOutputStream(keepInStream);
		
		final ObjectOutputStream bOut = new ObjectOutputStream(sendOutStream);
		bOut.flush();
		sendOutStream.flush();
		
		ObjectInputStream aIn = new ObjectInputStream(keepInStream);
		LinkedBlockingQueue<GameAction> queue = new LinkedBlockingQueue<GameAction>();
		
		ClientBroadCaster cb = new ClientBroadCaster(queue, activity, bOut);
		
		GameAction action = new GameAction(PokerAction.BET);
		
		cb.addListener(this);
		
		queue.add(action);
		
		aIn.close();
		Thread th = new Thread(cb);
		th.start();
		
		if(activity.isUpdated())
		{
			fail();
		}
		
		}catch(IOException e)
		{
			fail();
		}
	}

	@Override
	public void onPlayerTaskClose() {
		this.getActivity().finish();
	}
	
	
}

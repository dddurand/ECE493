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
import android.util.Log;
import application.PokerApplication;
import client.mock.PlayAreaMock;
import client.mock.ServerMock;

import com.example.bluetoothpoker.PlayingArea;

import dataModels.Account;

public class ClientTest extends ActivityUnitTestCase<PlayAreaMock> {

	public ClientTest() {
		super(PlayAreaMock.class);
	}

	private PlayingArea activity;


	protected void setUp()
	{
		try {
			super.setUp();
		} catch (Exception e) {
			fail();
		}
		this.activity = this.getActivity();
	}
	
	public void testBlueToothClient()
	{
		
		try {
			Intent intent = new Intent();
			this.activity = this.startActivity(intent, null, null);
			
		PipedInputStream keepInStream = new PipedInputStream(1000);
		PipedInputStream sendInStream = new PipedInputStream(1000);
		
		PipedOutputStream keepOutStream = new PipedOutputStream(sendInStream);
		PipedOutputStream sendOutStream = new PipedOutputStream(keepInStream);
		
		ObjectOutputStream bOut = new ObjectOutputStream(sendOutStream);
		bOut.flush();
		sendOutStream.flush();
		
		@SuppressWarnings("unused")
		ObjectOutputStream aOut = new ObjectOutputStream(keepOutStream);
		ObjectInputStream bIn = new ObjectInputStream(sendInStream);
		
		LinkedBlockingQueue<GameAction> queue = new LinkedBlockingQueue<GameAction>();
		
		
		
		Client client = new Client(activity, bIn,bOut, queue);
		ObjectInputStream aIn = new ObjectInputStream(keepInStream);
		
		GameAction action = new GameAction(0, PokerAction.WIN);
		
		queue.add(action);
		
		GameAction action2 =(GameAction) aIn.readObject();
		
		assertTrue(action.getAction() == action2.getAction());
		assertTrue(action.getPosition() == action2.getPosition());
		
		client.close();
		
		if(!this.isFinishCalled())
		{
			fail();
		}
		
		} catch (Exception e) {
			Log.e("A", "A", e);
			fail();
		}
		
		
		
		
	}
	
	public void testBlueToothLocalClient()
	{
		
		try {
			Intent intent = new Intent();
			
			PokerApplication app = new PokerApplication();
			app.setAccount(new Account("bob", 50));
			
			this.setApplication(app);
			this.activity = this.startActivity(intent, null, null);
			
		ServerMock server = new ServerMock(this.activity);

		
		LinkedBlockingQueue<GameAction> queue = new LinkedBlockingQueue<GameAction>();	
		Client client = new Client(activity, server, queue, 0);
		
		assertTrue(server.isPlayerAdded());
		assertTrue(server.getIn() != null);
		assertTrue(server.getOut() != null);
		assertTrue(server.getPlayer().getUsername().equals("bob"));
		assertTrue(server.getPlayer().getAmountMoney() == 50);
		
		client.close();
		
		if(!this.isFinishCalled())
		{
			fail();
		}
		
		} catch (IOException e) {
			Log.e("A", "A", e);
			fail();
		}
		
		
		
		
	}
	
}

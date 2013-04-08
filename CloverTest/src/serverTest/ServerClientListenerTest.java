package serverTest;

import game.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import server.GameAction;
import server.GameAction.PokerAction;
import server.ServerClientListener;
import serverTest.PlayerTaskCompleteNotifyTest.ActionPlayerCatcher;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import client.mock.PlayAreaMock;

public class ServerClientListenerTest  extends ActivityUnitTestCase<PlayAreaMock> {

	private Player player = new Player(0, "user", 1);
	
	public ServerClientListenerTest() {
		super(PlayAreaMock.class);
	}

	public void testValid()
	{
		try {
			PlayAreaMock pa = this.startActivity(new Intent(), null, null);
			
		PipedInputStream keepInStream = new PipedInputStream(10000);
		
		PipedOutputStream sendOutStream = new PipedOutputStream(keepInStream);
		
		final ObjectOutputStream bOut = new ObjectOutputStream(sendOutStream);
		bOut.flush();
		sendOutStream.flush();
		
		ObjectInputStream aIn = new ObjectInputStream(keepInStream);
		BlockingQueue<GameAction> queue = new LinkedBlockingQueue<GameAction>();
		
		ServerClientListener scl = new ServerClientListener(aIn, queue, player, pa);
		
		Thread th = new Thread(scl);
		th.start();
		
		ActionPlayerCatcher ct = new ActionPlayerCatcher();
		scl.addListener(ct);
		
		GameAction action = new GameAction(PokerAction.BET);
		
		bOut.writeObject(action);
		bOut.flush();
		
		try {
			GameAction action2 = queue.poll(3000, TimeUnit.MILLISECONDS);
			
			assertTrue(action.getAction() == action2.getAction());
		
			scl.cancel();
			bOut.close();
			th.join();
			
		} catch (InterruptedException e) {
			fail();
		}
		
		
		} catch (IOException e)
		{
			fail();
		}
	}
	
	public void testClosed()
	{
		try {
			PlayAreaMock pa = this.startActivity(new Intent(), null, null);
			
		PipedInputStream keepInStream = new PipedInputStream(10000);
		
		PipedOutputStream sendOutStream = new PipedOutputStream(keepInStream);
		
		final ObjectOutputStream bOut = new ObjectOutputStream(sendOutStream);
		bOut.flush();
		sendOutStream.flush();
		
		ObjectInputStream aIn = new ObjectInputStream(keepInStream);
		BlockingQueue<GameAction> queue = new LinkedBlockingQueue<GameAction>();
		
		ServerClientListener scl = new ServerClientListener(aIn, queue, player, pa);
		
		Thread th = new Thread(scl);
		th.start();
		
		ActionPlayerCatcher ct = new ActionPlayerCatcher();
		scl.addListener(ct);
		
		bOut.close();
		
		try {
			GameAction action2 = queue.poll(3000, TimeUnit.MILLISECONDS);
			
			assertTrue(action2 == null);
		
			scl.cancel();
			th.join();
		} catch (InterruptedException e) {
			fail();
		}
		
		
		} catch (IOException e)
		{
			fail();
		}
	}
	
}

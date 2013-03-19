package client;

import game.Player;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.StreamCorruptedException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import server.GameAction;
import server.GameState;
import server.Server;
import application.PokerApplication;

import com.example.bluetoothpoker.PlayingArea;

import dataModels.Account;

public class Client implements ClientTaskListener {

	private PlayingArea activity;
	private InputStream inStream;
	private OutputStream outStream;
	private int LOCAL_PIPE_BUFFER = 8000;
	
	private BlockingQueue<GameAction> queue;
	private ClientListener listener;
	private ClientBroadCaster broadCaster;
	
	/**
	 * Generates an local client that does not use bluetooth
	 * 
	 * @param activity
	 * @throws IOException 
	 */
	public Client(PlayingArea activity, Server server, LinkedBlockingQueue<GameAction> queue) throws IOException {
		this.activity = activity;
		this.queue = queue;
		intializeLocalClient(server);
		initialize();
	}
	
	/**
	 * Generates an database client that does not use bluetooth
	 * 
	 * @param activity
	 * @throws IOException 
	 * @throws StreamCorruptedException 
	 */
	public Client(PlayingArea activity, InputStream inStream, OutputStream outStream, LinkedBlockingQueue<GameAction> queue) throws StreamCorruptedException, IOException {
		this.activity = activity;
		this.inStream = inStream;
		this.outStream = outStream;
		this.queue = queue;
		initialize();
	}
	
	private void initialize() throws StreamCorruptedException, IOException
	{
		listener = new ClientListener(this.inStream, this.activity);
		broadCaster = new ClientBroadCaster(queue, activity, outStream);
	}
	
	private void intializeLocalClient(Server server) throws IOException
	{
		PipedInputStream keepInStream = new PipedInputStream(LOCAL_PIPE_BUFFER);
		PipedInputStream sendInStream = new PipedInputStream(LOCAL_PIPE_BUFFER);
		
		PipedOutputStream keepOutStream = new PipedOutputStream(sendInStream);
		PipedOutputStream sendOutStream = new PipedOutputStream(keepInStream);
		
		this.inStream = keepInStream;
		this.outStream = keepOutStream;
		
		PokerApplication app = (PokerApplication) activity.getApplication();
		Account account = app.getAccount();
		Player player = new Player(0, account.getUsername(), account.getBalance());
		
		server.addPlayer(player, sendInStream, sendOutStream);
	}

	@Override
	public void onPlayerTaskClose() {
		//signal activity that we disconnected
		
	}



}

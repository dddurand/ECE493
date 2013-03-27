package client;

import game.Player;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.StreamCorruptedException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import server.GameAction;
import server.Server;
import application.PokerApplication;

import com.example.bluetoothpoker.PlayingArea;

import dataModels.Account;

/**
 * Acts as the interface between the server and the GUI. This class
 * listens for game state updates from the server, and sends broadcasts
 * of actions made by the user.
 * 
 * @author dddurand
 *
 */
public class Client implements ClientTaskListener {

	private PlayingArea activity;
	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	private int LOCAL_PIPE_BUFFER = 1000000;
	
	private BlockingQueue<GameAction> queue;
	private ClientListener listener;
	private ClientBroadCaster broadCaster;
	
	/**
	 * Generates an local client that does not use bluetooth
	 * 
	 * @param activity
	 * @throws IOException 
	 */
	public Client(PlayingArea activity, Server server, LinkedBlockingQueue<GameAction> queue, int position) throws IOException {
		this.activity = activity;
		this.queue = queue;
		intializeLocalClient(server, position);
		initialize();
	}
	
	/**
	 * Generates an database client that does not use bluetooth
	 * 
	 * @param activity
	 * @throws IOException 
	 * @throws StreamCorruptedException 
	 */
	public Client(PlayingArea activity, ObjectInputStream inStream, ObjectOutputStream outStream, LinkedBlockingQueue<GameAction> queue) throws StreamCorruptedException, IOException {
		this.activity = activity;
		this.inStream = inStream;
		this.outStream = outStream;
		
		this.queue = queue;
		initialize();
	}
	
	/**
	 * Initialize that is used by both constructor.
	 * Should be called at the end.
	 * 
	 * @throws StreamCorruptedException
	 * @throws IOException
	 */
	private void initialize() throws StreamCorruptedException, IOException
	{
		listener = new ClientListener(this.inStream, this.activity);
		Thread listenerThread = new Thread(listener);
		listenerThread.start();
		
		listener.addListener(this);
		
		broadCaster = new ClientBroadCaster(queue, activity, outStream);
		Thread broadCasterThread = new Thread(broadCaster);
		broadCasterThread.start();
		
		broadCaster.addListener(this);
	}
	
	/**
	 * Specialized Constructor for the case when the client is on the server device.
	 * We are generating pipes to mimic bluetooth communication
	 * 
	 * @param server
	 * @throws IOException
	 */
	private void intializeLocalClient(Server server, int position) throws IOException
	{
		PipedInputStream keepInStream = new PipedInputStream(LOCAL_PIPE_BUFFER);
		PipedInputStream sendInStream = new PipedInputStream(LOCAL_PIPE_BUFFER);
		
		PipedOutputStream keepOutStream = new PipedOutputStream(sendInStream);
		//keepOutStream.flush();
		PipedOutputStream sendOutStream = new PipedOutputStream(keepInStream);
		//sendOutStream.flush();
		
		ObjectOutputStream sendOutObjectStream = new ObjectOutputStream(sendOutStream);
		sendOutObjectStream.flush();
		sendOutStream.flush();
		
		this.outStream = new ObjectOutputStream(keepOutStream);
		this.outStream.flush();
		
		
		PokerApplication app = (PokerApplication) activity.getApplication();
		Account account = app.getAccount();
		Player player = new Player(position, account.getUsername(), account.getBalance());
		
		ObjectInputStream sendInObjectStream=  new ObjectInputStream(sendInStream);

		
		server.addPlayer(player, sendInObjectStream, sendOutObjectStream);
		this.inStream = new ObjectInputStream(keepInStream);
	}

	/**
	 * The function called when something fails
	 * 
	 */
	@Override
	public void onPlayerTaskClose() {
		//signal activity that we disconnected
		
	}
	
	public void close()
	{
		listener.cancel();
		broadCaster.cancel();
	}



}

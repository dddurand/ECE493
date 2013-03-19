package com.example.bluetoothpoker;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import client.Client;
import server.GameAction;
import server.GameState;
import server.Server;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import fragments.PlayerFragment;
import fragments.River;

public class PlayingArea extends Activity implements OnClickListener {
	
	private FrameLayout[] playerLayouts;
	private FrameLayout riverLayout;
	private River riverObject;
	private PlayerFragment[] playerObjects;
	private final int maxPlayers = 6;
	private FragmentManager fm;
	
	private boolean debugServer = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    
	    if(debugServer)
	    	this.debugServer();
	    
	    
	    setContentView(R.layout.playing_area);
	    //Get intent
	   // Intent intent = getIntent();
	    
	    /***********Set listeners for buttons****************/
	    Button b1 = (Button)findViewById(R.id.button1);
	    Button b2 = (Button)findViewById(R.id.button2);
	    b1.setOnClickListener(this);
	    b2.setOnClickListener(this);
	    
	    
	    /**********************Initialize fragment related variables************************/
	    fm = getFragmentManager();
	    
	    //Initialize layouts array
	    playerLayouts = new FrameLayout[maxPlayers];
	    //Save reference to layouts in array
	    playerLayouts[0] = (FrameLayout)findViewById(R.id.localPlayer);
	    playerLayouts[1] = (FrameLayout)findViewById(R.id.player2);
	    playerLayouts[2] = (FrameLayout)findViewById(R.id.player3);
	    playerLayouts[3] = (FrameLayout)findViewById(R.id.player4);
	    playerLayouts[4] = (FrameLayout)findViewById(R.id.player5);
	    playerLayouts[5] = (FrameLayout)findViewById(R.id.player6);
	    //River
	    riverLayout = (FrameLayout)findViewById(R.id.riverLayout);
	    
	    /**********Instantiate Players Array*******/
	    playerObjects = new PlayerFragment[maxPlayers];
	    
	    playerObjects[0] = new PlayerFragment(true,"local");
	    for (int i=1;i<maxPlayers;i++)
	    {
	    	playerObjects[i] = new PlayerFragment(false,"Player "+Integer.toString(i));
	    }
	    
	    initializeFragments(maxPlayers);
	}
	
	private void debugServer()
	{
		LinkedBlockingQueue<GameAction> actionQueue = new LinkedBlockingQueue<GameAction>();
		Server server = new Server(this);
		try {
			Client client = new Client(this, server, actionQueue);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GameAction action = new GameAction();
		actionQueue.add(action);
		
	}
	
	/**
	 * Initializes the playing area by placing the fragments. only called by constructor
	 */
	private void initializeFragments(int totalPlayers){
		
		//Transaction begins here
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.setTransitionStyle(FragmentTransaction.TRANSIT_NONE);
		
		//First create the new player object, then replace its respective fragment.
		for (int i=0;i<totalPlayers;i++)
		{
			transaction.replace(playerLayouts[i].getId(), playerObjects[i]);
		}
		
		//River
		riverObject = new River();
		transaction.replace(riverLayout.getId(),riverObject);
		
		transaction.commit();
		fm.executePendingTransactions();
	}
	
	/*********************************************************Methods for Game Mechanics***********************************************************/
	
	/**
	 * Method that updates everything in the view according to what's passed in the data object.
	 * This is the primary method to be used from the game mechanics.
	 * @param data
	 */
	public void updateAll(GameState data){
		//Clear all players first
		clearAllPlayers();
		//Then clear the river
		clearRiver();
		//Then make all the players visible
		setVisiblePlayers(data.getTotalPlayers());
	}
	
	/**
	 * Sets the number of players that are visible on the playing area
	 * @param totalPlayers
	 */
	private void setVisiblePlayers(int totalPlayers){
		
		for (int i=0;i<totalPlayers;i++){
			playerLayouts[i].setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * Sets the desired card into the river
	 */
	public void updateRiver(){
	}
	
	/**
	 * Sets the first 3 cards of the river to the face down image, and removes the last 2.
	 */
	public void clearRiver(){
		riverObject.setCard(0, "back");
		riverObject.setCard(1, "back");
		riverObject.setCard(2, "back");
		riverObject.removeCard(3);
		riverObject.removeCard(4);
	}
	
	/**
	 * Sets a player card according to player number [0,totalPlayers] (0 means the local player), the card number (0 for left, 1 for right)
	 * and the card name (according to convention)
	 */
	public void setPlayerCard(int playerNumber, int cardNumber, String c){
		playerObjects[playerNumber].setCard(cardNumber, c);
	}
	
	/**
	 * Sets ALL players to invisible.
	 */
	private void clearAllPlayers(){
		for (int i=0;i<this.maxPlayers;i++)
		{
			playerObjects[i].setCard(0, "back");
			playerObjects[i].setCard(1, "back");
			playerLayouts[i].setVisibility(View.INVISIBLE);
		}
	}
	

	@Override
	public void onClick(View v) {
		
		switch (v.getId()){
		
		case R.id.button1:
			//test commands
			GameState d = new GameState(5);
			updateAll(d);
			setPlayerCard(2,0,"c5");
			setPlayerCard(0,0,"hk");
			setPlayerCard(0,1,"hq");
			break;
			
		case R.id.button2:
			riverObject.setCard(3, "hk");
			riverObject.setCard(4, "hq");
			playerObjects[3].setCard(1, "sk");
			break;
		
		}
		
		
	}
	
}

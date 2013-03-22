package com.example.bluetoothpoker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import server.GameAction;
import server.GameAction.PokerAction;
import server.GameState;
import server.Server;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import application.PokerApplication;
import client.Client;
import dataModels.Account;
import fragments.OnlineMode;
import fragments.PlayerFragment;
import fragments.River;
import game.Card;
import game.Player;

public class PlayingArea extends Activity implements OnClickListener {
	
	private FrameLayout[] playerLayouts;
	private FrameLayout riverLayout;
	private River riverObject;
	private PlayerFragment[] playerObjects;
	private final int maxPlayers = 6;
	private FragmentManager fm;
	private int current = 0;
	
	private boolean debugServer = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	      
	    setContentView(R.layout.playing_area);
	    //Get intent
	   // Intent intent = getIntent();
	    
	    /***********Set listeners for buttons****************/
	    Button b1 = (Button)findViewById(R.id.button1);
	    Button b2 = (Button)findViewById(R.id.button2);
	    Button b3 = (Button)findViewById(R.id.button3);
	    b1.setOnClickListener(this);
	    b2.setOnClickListener(this);
	    b3.setOnClickListener(this);
	    
	    
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
	    //clearRiver();
	    
	    if(debugServer)
	    	{
	    	Thread thread = new Thread(new DebugRunnable());
	    	thread.start();
	    	}
	}
	
	/**
	 * A test for the server instrastructure
	 */
	
	
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
		
		//Log.d("GUI UPDATE CALLED", "TotalPlayers: " + data.getTotalPlayers());
		
		//Clear all players first
		clearAllPlayers();
		//Then clear the river
		clearRiver();
		//Then make all the players visible
		//setVisiblePlayers(data.getTotalPlayers());
		
		ArrayList<Player> players = data.getPlayers();
		
		setVisiblePlayers(players);
		
		for(Player player : players)
		{
			if(player == null) continue;
			
			if(player.getCard(0) == null)
			{
				this.setPlayerCard(player.getId(), 0, "back");
				this.setPlayerCard(player.getId(), 1, "back");
				continue;
			}
			
			Card card = player.getCard(0);
			this.setPlayerCard(player.getId(), 0, card.toString().toLowerCase());
			
			card = player.getCard(1);
			this.setPlayerCard(player.getId(), 1, card.toString().toLowerCase());
		}
		
		Card comm[] = data.getCommunity();
		updateRiver(comm);
	}
	
	/**
	 * Sets the number of players that are visible on the playing area
	 * @param totalPlayers
	 */
	private void setVisiblePlayers(ArrayList<Player> players){
		
		/**
		 * This won't work.
		 * Itterate over the given list of players in the gamestate and check which arn't null
		 * Dustin
		 * 
		 */
		for(Player player : players)
		{
			playerLayouts[player.getId()].setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * Sets the desired card into the river
	 */
	public void updateRiver(Card comm[]){
		
		for(int i = 0; i < comm.length; i++)
		{
			if(comm[i]==null) break;
			riverObject.setCard(i, comm[i].toString().toLowerCase());
			
		}	
		int test = 0;;
		test++;
	}
	
	/**
	 * Removes all 5 cards from the river
	 */
	public void clearRiver(){
		riverObject.removeCard(0);
		riverObject.removeCard(1);
		riverObject.removeCard(2);
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
	
	/**
	 * Animates the progress bar for the given player in the parameter.
	 * @param player The player's progress bar to be animated
	 */
	public void animateProgressBar(int player){
		//Get view
		final ProgressBar pb = (ProgressBar) playerLayouts[player].findViewById(R.id.progressBarTimeLeft);
		//Set it visible
		pb.setVisibility(View.VISIBLE);
		
		//Start the countdown timer and start
		new CountDownTimer(7000,1){
			
			public void onTick(long millisUntilFinished){
				pb.setProgress((int)millisUntilFinished);
			}
			
			public void onFinish(){
				pb.setVisibility(View.INVISIBLE);
				//Player didnt take turn, fold
			}
		}.start();
		
	}
	
	/**
	 * Changes background of selected player to indicate it's that player's turn.
	 * @param n The player number [0 for local, 1-5 for the other 5]
	 */
	private void setActivePlayerBackground(int n){
		playerLayouts[n].setBackgroundResource(R.drawable.active_player_border);
	}
	
	/**
	 * Resets the background of the selected player to a transparent one. This should be called
	 * after each player's turn to clear the background
	 * @param n
	 */
	private void clearActivePlayerBackground(int n){
		playerLayouts[n].setBackgroundColor(Color.parseColor("#00000000"));
	}
	
	private void animateAll(){
		for (int i=0;i<this.maxPlayers;i++)
			setActivePlayerBackground(i);
	}
	
	private void clearAll(){
		for (int i=0;i<this.maxPlayers;i++)
			clearActivePlayerBackground(i);
	}
	

	@Override
	public void onClick(View v) {
		
		switch (v.getId()){
		
		case R.id.button1:
			//test commands
			clearAll();
			break;
			
		case R.id.button2:
			riverObject.setCard(3, "hk");
			riverObject.setCard(4, "hq");
			playerObjects[3].setCard(1, "sk");
			break;
			
		case R.id.button3:
//			animateProgressBar(1);
			if (current!=0) clearActivePlayerBackground(current-1);
			if (current>=6) current=0;
			setActivePlayerBackground(current);
			animateProgressBar(current++);
			break;
		
		}
		
		
	}
	
	@Override
	public void onBackPressed(){
		
		new AlertDialog.Builder(this)
		.setTitle("Really Exit?")
		.setMessage("Are you sure you want to leave the game?")
		.setNegativeButton(android.R.string.no, null)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface arg0, int arg1) {
				//Add stuff for cleaning up here
				PlayingArea.super.onBackPressed();
			}
		}).create().show();
	}
	
	private class DebugRunnable implements Runnable
	{
		public void run()
		{
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			LinkedBlockingQueue<GameAction> actionQueue = new LinkedBlockingQueue<GameAction>();
			LinkedBlockingQueue<GameAction> actionQueue2 = new LinkedBlockingQueue<GameAction>();
			Server server = new Server(PlayingArea.this);
			try {
				Account account = ((PokerApplication) PlayingArea.this.getApplication()).getAccount();
				account.setUsername("BOB1");
				account.setBalance(500);
				Client client = new Client(PlayingArea.this, server, actionQueue, 0);
				
				account = ((PokerApplication) PlayingArea.this.getApplication()).getAccount();
				account.setUsername("BOB2");
				account.setBalance(500);
				Client client2 = new Client(PlayingArea.this, server, actionQueue2, 1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ArrayList<GameAction> actions = new ArrayList<GameAction>();
			
			server.gameStart();
			
			GameAction action = new GameAction(0, PokerAction.BET, 15);
			actions.add(action);
			
			action = new GameAction(0, PokerAction.BET, 15);
			actions.add(action);
			
			action = new GameAction(1, PokerAction.BET, 5);
			actions.add(action);
			
			action = new GameAction(0, PokerAction.CALL, 5);
			actions.add(action);
			
			action = new GameAction(1, PokerAction.CHECK, 0);
			actions.add(action);
			
			action = new GameAction(0, PokerAction.CHECK, 0);
			actions.add(action);
			
			action = new GameAction(1, PokerAction.CHECK, 0);
			actions.add(action);
			
			action = new GameAction(0, PokerAction.CHECK, 0);
			actions.add(action);
			
			action = new GameAction(1, PokerAction.CHECK, 0);
			actions.add(action);
			
			action = new GameAction(0, PokerAction.CHECK, 0);
			actions.add(action);
			
			action = new GameAction(1, PokerAction.CHECK, 0);
			actions.add(action);

			for(int i = 0; i < actions.size(); i++)
			{
				GameAction temp = actions.get(i);
				
				Log.e("ACTION: ", i+"");
				Log.e("ACTION: ", (i % 2)+"");
				if(i % 2 == 1)
				{
					Log.e("queue2", (i % 2)+"");
					actionQueue2.add(temp);
				}
				else
				{
					Log.e("queue", (i % 2)+"");
					actionQueue.add(temp);	
				}
					
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
			
			
		}
	}
	
}

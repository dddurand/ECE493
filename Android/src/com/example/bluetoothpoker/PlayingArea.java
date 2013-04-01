package com.example.bluetoothpoker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

<<<<<<< HEAD
import bluetooth.DiscoverableList;

=======
import misc.AnimationTimer;
>>>>>>> Added AnimationTimer class, plus implemented more of the updateAll method based on assumptions
import misc.CustomAdapter;
import misc.StatsRowObject;
import misc.TurnTimer;
import server.GameAction;
import server.GameAction.PokerAction;
import server.GameState;
import server.Server;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import application.PokerApplication;
import bluetooth.DiscoverableList;
import client.Client;
import dataModels.Account;
import database.DatabaseDataSource;
import database.PreferenceConstants;
import fragments.PlayerFragment;
import fragments.River;
import game.Card;
import game.GameMechanics;
import game.Player;
import game.Pot;

public class PlayingArea extends Activity implements OnClickListener {
	
	private FrameLayout[] playerLayouts;
	private FrameLayout riverLayout;
	private River riverObject;
	private PlayerFragment[] playerObjects;
	private final int maxPlayers = 6;
	private FragmentManager fm;
	private int current = 0;
	
	//Turn Timers
	private TurnTimer turnTimer=null;
	private AnimationTimer animationTimer=null;
	
	private LinkedBlockingQueue<GameAction> actionQueue;
	
	private int myPositionAtTable;
	
	//NOTE: This value MUST be the same as in the XML file
	//ie. android:max=10000
	private final int turnLength = 10000; //In MS
	
	private PokerApplication pokerApp;
	private Account account;
	private DatabaseDataSource dbInterface;
	private SharedPreferences preferences;
	
	private boolean debugServer = false;

	private Button callCheckButton;
	private Button foldButton;
	private Button raiseButton;
	
	private PokerAction raiseState;
	private PokerAction checkCallState;
	
	private int minimumBet;
	
	private LinkedBlockingQueue<GameAction> actionQueue;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	      
	    setContentView(R.layout.playing_area);
	    //Get intent
	   // Intent intent = getIntent();
	    
	    this.pokerApp = (PokerApplication) this.getApplication();
	    this.account = pokerApp.getAccount();
	    dbInterface = pokerApp.getDataSource();
	    this.preferences = this.getPreferences(Context.MODE_PRIVATE);
	    
	    /***********Set listeners for buttons****************/
	    callCheckButton = (Button)findViewById(R.id.callCheckButton);
	    foldButton = (Button)findViewById(R.id.foldButton);
	    raiseButton = (Button)findViewById(R.id.raiseButton);
	    
	    callCheckButton.setOnClickListener(this);
	    foldButton.setOnClickListener(this);
	    raiseButton.setOnClickListener(this);
	    
	    
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
	    
	    ArrayList<Player> myPlayer = (ArrayList<Player>)this.getIntent().getSerializableExtra(DiscoverableList.PLAYER_HOLDER);
<<<<<<< HEAD

=======
	    //Player myPlayer[] =(Player[])this.getIntent().getSerializableExtra(DiscoverableList.PLAYER_HOLDER);
>>>>>>> Added AnimationTimer class, plus implemented more of the updateAll method based on assumptions
	    PokerApplication pA = (PokerApplication) this.getApplication();

	    ObjectInputStream inStream[] = pA.getInStream();
<<<<<<< HEAD
	    ObjectOutputStream outStream[] = pA.getOutStream();
=======
	    ObjectOutputStream outStream[] =pA.getOutStream();
>>>>>>> Added AnimationTimer class, plus implemented more of the updateAll method based on assumptions
	    actionQueue = new LinkedBlockingQueue<GameAction>();
	    if(getIntent().getBooleanExtra(DiscoverableList.IS_CLIENT, true)) {
	    	//Client
	    	try {
	    		this.myPositionAtTable = getIntent().getIntExtra(DiscoverableList.CLIENT_POS, 0);
				Client client = new Client(this, inStream[0], outStream[0], actionQueue);
			} catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    } else {
	    	//Server
	    	Server server = new Server(this);
	    	
	    	try {
				Client client = new Client(this, server, actionQueue, 0);
				this.myPositionAtTable = 0;
				for (int i=0; i<myPlayer.size(); i++) {
					//LinkedBlockingQueue<GameAction> FARTS = new LinkedBlockingQueue<GameAction>();
					//Client tmp = new Client(this, mySockets[i].getInputStream(), mySockets[i].getOutputStream(),FARTS);
					Player tmp = myPlayer.get(i);
					int pos = tmp.getId()-1;
					server.addPlayer(tmp, inStream[pos], outStream[pos]);
				}
				server.gameStart();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
	/**
	 * A test for the server instrastructure
	 */
	
	
	/**
	 * Initializes the playing area by placing the fragments. only called by constructor
	 */
	private void initializeFragments(int totalPlayers){
		
		//Transaction begins heref
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
	
	/**
	 * Updates the account balance based on the state of the game
	 * 
	 * @param state
	 */
	private void updateAccount(GameState state)
	{
		ArrayList<Player> players = state.getPlayers();
		
		for(Player player: players)
		{
			if(player.getId() == this.myPositionAtTable && player.getUsername().equals(this.account.getUsername()))
			{
				this.account.setBalance(player.getAmountMoney());
				if(this.account.isOnline())
					dbInterface.updateAccount(account);
				else
				{
					Editor editor = this.preferences.edit();
					editor.putInt(PreferenceConstants.OFFLINE_BALANCE, account.getBalance());
					editor.commit();
				}
				
				break;
			}
		}
		
	}
	
	/**
	 * Stores the various data to the database based on the state provided
	 * to the client.
	 * 
	 * This function does nothing if the last action made was not for this user,
	 * or if the current account is not online.
	 * 
	 * @param state
	 */
	private void storeGameState(GameState state)
	{
		if(!this.account.isOnline()) return;//offline account
		GameAction gameAction = state.getLastPokerGameAction();
		int playerPosition = gameAction.getPosition();
		//If other players action (or not server action) ignore.
		if(playerPosition != myPositionAtTable && playerPosition != GameMechanics.SERVER_POSITION) return;
		
		PokerAction action = gameAction.getAction();
		
		switch(action)
		{
		case STARTGAME:
			saveGameHistory(state);
			break;
			
		case ENDGAME:
			
			saveEndGameHistory(state);
			
			break;
			
		case BET:
		case CALL:
		case CHECK:
		case FOLD:
		case RAISE:
		case RERAISE:
			
			saveGameHistory(state);
			
			break;
			
		default:
			break;
			
		}
		

		
	}
	
	/**
	 * This function saves all data at the end of a game.
	 * This includes saving which pot the player has won, loss, etc
	 * 
	 * @param state
	 */
	private void saveEndGameHistory(GameState state)
	{
		GameAction gameAction = state.getLastPokerGameAction();
		PokerAction action = gameAction.getAction();
		ArrayList<Pot> sidePots = state.getSidePots();
		Pot mainPot = state.getMainPot();
		
		Player player = state.getPlayer();
		
		int gameID = dbInterface.addGame(state.getUUID().toString(), account);
		if(gameID == -1) return;
		Card hand[] = state.getPlayer().getHand();
		Card comm[] = state.getCommunity();
		
		int bet = gameAction.getValue();
		int updateNum = state.getGameUpdateNumber();
		
		int subActionCount = 0;
		if(player.getActive() != Player.FOLDED)
		{
			for(Pot sidePot : sidePots)
			{
				if(sidePot.exist(this.myPositionAtTable))
				{
					if(sidePot.isWinner(this.myPositionAtTable))
						dbInterface.addGameAction(gameID, updateNum+subActionCount, sidePot.getTotal(), bet, hand, comm, PokerAction.WIN);
					else
						dbInterface.addGameAction(gameID, updateNum+subActionCount, sidePot.getTotal(), bet, hand, comm, PokerAction.LOSS);
					
					subActionCount++;
				}
			}
			
			if(mainPot.isWinner(this.myPositionAtTable))
				dbInterface.addGameAction(gameID, updateNum+subActionCount, mainPot.getTotal(), bet, hand, comm, PokerAction.WIN);
			else
				dbInterface.addGameAction(gameID, updateNum+subActionCount, mainPot.getTotal(), bet, hand, comm, PokerAction.LOSS);
			
			subActionCount++;
		}
		
		int potAmount = 0;
		for(Pot pot : sidePots)
			potAmount += pot.getTotal();
		
		potAmount += mainPot.getTotal();
		
		dbInterface.addGameAction(gameID, updateNum, potAmount, bet, hand, comm, action);
	}
	
	/**
	 * Saves a given state to the database
	 * 
	 * This function is for the normal moves in poker:
	 * check, bet, fold, raise, reraise....
	 * 
	 * This should only be called if the player is online
	 * 
	 * @param state
	 */
	private void saveGameHistory(GameState state)
	{
		GameAction gameAction = state.getLastPokerGameAction();
		PokerAction action = gameAction.getAction();
		ArrayList<Pot> sidePots = state.getSidePots();
		Pot mainPot = state.getMainPot();
		int potAmount = 0;
		for(Pot pot : sidePots)
			potAmount += pot.getTotal();
		
		potAmount += mainPot.getTotal();
		
		int bet = gameAction.getValue();
		int updateNum = state.getGameUpdateNumber();
		
		int gameID = dbInterface.addGame(state.getUUID().toString(), account);
		if(gameID == -1) return;
		
		Card hand[] = state.getPlayer().getHand();
		Card comm[] = state.getCommunity();
		
		dbInterface.addGameAction(gameID, updateNum, potAmount, bet, hand, comm, action);
	}
	
	/*********************************************************Methods for Game Mechanics***********************************************************/
	
	/**
	 * Populates the pot list with the given data in the array.
	 */
	public void populateList(){
		
		ListView listView = (ListView)findViewById(R.id.potList);
		ArrayList<StatsRowObject> rowObjects = new ArrayList<StatsRowObject>();
		StatsRowObject row;
		
		for (int i=0;i<35;i++)
		{
			row = new StatsRowObject("Pot "+i,"lol"+i);
			rowObjects.add(row);
		}
		
		CustomAdapter adapter = new CustomAdapter(this,rowObjects);
		listView.setAdapter(adapter);
	}
	
	public int getPositionAtTable(){
		return this.myPositionAtTable;
	}
	
	public ProgressBar getPlayerProgressBar(int pos){
		return (ProgressBar) playerLayouts[pos].findViewById(R.id.progressBarTimeLeft);
	}
	
	/**
	 * Method that updates everything in the view according to what's passed in the data object.
	 * This is the primary method to be used from the game mechanics.
	 * @param data
	 */
	public void updateAll(GameState data){
		
		//Clear all players first
		clearAllPlayers();
		
		clearAll();
		
		//Then clear the river
		clearRiver();
<<<<<<< HEAD
		updateButtons(data);
		
		
=======
		//Get players and make the active ones visible
>>>>>>> Added AnimationTimer class, plus implemented more of the updateAll method based on assumptions
		ArrayList<Player> players = data.getPlayers();
		setVisiblePlayers(players);
		
		//----------------------------------------------------------------------------------------------------------
		//---------------------------------------------Update Players-----------------------------------------------
		//----------------------------------------------------------------------------------------------------------
		for(Player player : players)
		{
			if(player == null) continue;
			
			if(player.getCard(0) == null)
			{
				this.setPlayerCard(player.getId(), 0, "back");
				this.setPlayerCard(player.getId(), 1, "back");
			}
<<<<<<< HEAD
			else
			{
=======
			
			//Sets the names for all the active players
			this.setPlayerName(player.getId(),player.getUsername());
			
>>>>>>> Added AnimationTimer class, plus implemented more of the updateAll method based on assumptions
			Card card = player.getCard(0);
			this.setPlayerCard(player.getId(), 0, card.toString().toLowerCase(Locale.CANADA));
			
			card = player.getCard(1);
			this.setPlayerCard(player.getId(), 1, card.toString().toLowerCase(Locale.CANADA));
			}
			
			if(player.getActive() == Player.FOLDED)
			{
				this.foldPlayer(player.getId());
			}
			
			this.clearActivePlayerBackground(player.getId());
			this.setPlayerName(player.getId(), player.getUsername());
			this.setPlayerAmount(player.getId(), player.getAmountMoney());
		}
		
<<<<<<< HEAD
		this.takeTurn(data.getCurrentPlayerTurn());
		this.setActivePlayerBackground(data.getCurrentPlayerTurn());
=======
		//----------------------------------------------------------------------------------------------------------
		//---------------------------------------------Progress Bar-------------------------------------------------
		//----------------------------------------------------------------------------------------------------------
		//If local player: wait for move
		if (data.getCurrentPlayerTurn()==this.myPositionAtTable)
		{
			this.takeTurn(0);
			this.setActivePlayerBackground(0);
		}
		else
		//Someone else's turn
		{
			this.animatePB(data.getCurrentPlayerTurn());
			this.setActivePlayerBackground(data.getCurrentPlayerTurn());
		}
		
		//----------------------------------------------------------------------------------------------------------
		//-------------------------------------------------River----------------------------------------------------
		//----------------------------------------------------------------------------------------------------------
>>>>>>> Added AnimationTimer class, plus implemented more of the updateAll method based on assumptions
		Card comm[] = data.getCommunity();
		updateRiver(comm);
		
		/*
		 * Update Account
		 */
		//updateAccount(data);
		
		/*
		 * Disabled until we have bluetooth clients...
		 */
		//storeGameState(data);
	}
	
	/**
	 * Sets the number of players that are visible on the playing area
	 * @param totalPlayers
	 */
	private void setVisiblePlayers(ArrayList<Player> players){
		
		for(Player player : players)
		{
			if(player == null) continue;
			playerLayouts[player.getId()].setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * Sets the player name to the specified string provided
	 */
	private void setPlayerName(int pos, String name){
		this.playerObjects[pos].setName(name);
	}
	
	/**
	 * Updates the view of the player when it folds and changes the name to  "Fold"
	 * Note: does not changes the name if it is the local player (ie. pos==0)
	 * @param pos
	 */
	public void foldPlayer(int pos){
		this.playerObjects[pos].setCard(0, "folded");
		this.playerObjects[pos].setCard(1, "folded");
		if (pos!=0) this.playerObjects[pos].setName("Fold");
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
	
	public void setPlayerAmount(int playerNumber, int amount){
		playerObjects[playerNumber].setAmount(amount);
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
			this.clearActivePlayerBackground(i);
		}
	}
	
	/**
	 * Waits for the turn of the given player and animates the progress bar for the given player in the parameter.
	 * Parameter should always be 0 (the local player)
	 * @param player The player's position on the table
	 */
	public void takeTurn(int player){
		ProgressBar pb = (ProgressBar)playerLayouts[player].findViewById(R.id.progressBarTimeLeft);
		pb.setVisibility(View.VISIBLE);
		
		turnTimer = new TurnTimer(this.turnLength,1,this,player);
		turnTimer.start();
	}
	
	/**
	 * Simply animates a progress bar for the given player. Should be called to all players except the local one on this view.
	 * @param player
	 */
	public void animatePB(int player){
		final ProgressBar pb = (ProgressBar)playerLayouts[player].findViewById(R.id.progressBarTimeLeft);
		pb.setVisibility(View.VISIBLE);
		
		animationTimer = new AnimationTimer(this.turnLength,1,this,player);
		animationTimer.start();
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
	
	/**
	 * Click listener for the buttons on the main playing area. Send serializable objects according to what the user presses.
	 */
	@Override
	public void onClick(View v) {
		
		GameAction action;
		
		switch (v.getId()){
		
		/******************Call/check****************/
		case R.id.callCheckButton:
			action = new GameAction(this.myPositionAtTable,checkCallState,minimumBet);
			actionQueue.add(action);
			break;
			
		/******************Fold*******************/
		case R.id.foldButton:
<<<<<<< HEAD
			action = new GameAction(this.myPositionAtTable,PokerAction.FOLD,0);
			actionQueue.add(action);
=======
			if (turnTimer!=null) {
				action = new GameAction(this.myPositionAtTable,PokerAction.FOLD,0);
				actionQueue.add(action);
			}
>>>>>>> Added AnimationTimer class, plus implemented more of the updateAll method based on assumptions
			break;
		
		/*******************Raise Button***************/
		case R.id.raiseButton:
			action = new GameAction(this.myPositionAtTable,raiseState,0);
			actionQueue.add(action);
			break;
		
		}
		
	}
	
	/**
	 * Shows a dialog confirming user to exit the game. The clean up methods necessary when a player leaves should be here.
	 */
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
	
	private void updateButtons(GameState state)
	{
		if(state == null || state.getPlayer().getActive() != Player.CURRENT)
			{
			disableButtons();
			return;
			}
		
		if(state.getCurrentPlayerTurn() == myPositionAtTable)
			updateButtonState(state);
		else
			disableButtons();
		
		
	}
	
	private void updateButtonState(GameState state)
	{
		ArrayList<Pot> sidePots = state.getSidePots();
		Pot mainPot = state.getMainPot();
		
		int myBetAmount = 0;
		int potAmount = 0;
		for(Pot pot : sidePots)
			{
			potAmount += pot.getAmount();
			myBetAmount += pot.getPlayerAmount(myPositionAtTable);
			}
		
		potAmount += mainPot.getAmount();
		myBetAmount += mainPot.getPlayerAmount(myPositionAtTable);
		
		minimumBet = potAmount - myBetAmount;
		
		if(myBetAmount == potAmount)
		{
			//raise && check
			callCheckButton.setEnabled(true);
			callCheckButton.setText(R.string.check);
			
		    foldButton.setEnabled(true);
		    raiseButton.setEnabled(true);
			
		    raiseState = PokerAction.RAISE;
			checkCallState = PokerAction.CHECK;
		    
		}
		else if(myBetAmount < potAmount)
		{
			//call & reraise
			callCheckButton.setEnabled(true);
			callCheckButton.setText(R.string.call);
			
		    foldButton.setEnabled(true);
		    raiseButton.setEnabled(true);
		    
		    raiseState = PokerAction.RERAISE;
			checkCallState = PokerAction.CALL;
		}
		else
		{
			Log.e("Game State", "Current bet amount more than pots");
		}
		
	}
	
	private void disableButtons()
	{
		callCheckButton.setEnabled(false);
		foldButton.setEnabled(false);
		raiseButton.setEnabled(false);
	}
	
}

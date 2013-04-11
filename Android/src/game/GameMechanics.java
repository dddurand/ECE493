package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import android.util.Log;

import server.GameAction;
import server.GameState;
import server.Server;
import server.WatchDogTimer;
import server.GameAction.PokerAction;

/**
 * Game mechanics used for all of the game logic
 * @author lawton
 */
public class GameMechanics {

	public static final int ACTION_FOLD = -1;
	public static final int SERVER_POSITION = -3;

	private Deck myDeck = new Deck();

	private int positionOfCurrentPlayer;
	private int currentTurn = -1;
	private int currentDealer;
	private int blindAmount;


	private int[] tempBets = new int[6];
	private Card[] communityCards = new Card[5];


	private boolean lastGame = false;
	private boolean noBets =false;

	private Pot mainPot;
	private GameAction lastPokerGameAction;
	private UUID gameUUID;
	private WatchDogTimer playTimer;
	
	private int gameUpdateCount;

	private boolean isAllFolded = false;

	private ArrayList<Integer> playerBetsInARound;
	private ArrayList<Player> outGoingList = new ArrayList<Player>(6);
	private ArrayList<Player> playerList = new ArrayList<Player>(6);
	private ArrayList<Pot> currentSidePots;
	private BlockingQueue<GameState> queue;

	private Server server;
	
	private boolean postStart = false;
	private boolean terminateSignaled = false;
	
	//private ArrayList<Integer> playerBetsInARound;
	
	/**
	 * create game mechanics
	 * @param playerList
	 * @param Dealer
	 * @param blindAmount
	 */
	public GameMechanics(int Dealer, int blindAmount, BlockingQueue<GameState> queue, WatchDogTimer playTimer, Server server) {
		this.playerList = new ArrayList<Player>(6);
		
		for(int i = 0; i < 6; i++) this.playerList.add(null);

		this.currentDealer = Dealer;
		this.blindAmount = blindAmount;
		this.queue = queue;
		this.playTimer = playTimer;
		this.server = server;
	}
	
	private void newGameReset()
	{
		currentTurn = -1;
		currentSidePots = new ArrayList<Pot>();
		tempBets = new int[6];
		communityCards = new Card[5];
		mainPot = new Pot(0,0);
	}

	/**
	 * The main interface for processing actions from the clients.
	 * 
	 * These GameActions provided to this object drives the whole GameMechanics engine, 
	 * and must be processed through this interface.
	 * 
	 * 
	 * @param action
	 */
	public void processGameAction(GameAction action)
	{
		if(terminateSignaled) return;
		
		PokerAction pokerAction = action.getAction();

		/*
		 * Some basic commands that are more server based
		 */
		if(action.getPosition() == SERVER_POSITION)
		{
			switch(pokerAction)

			{
			case ADDPLAYER:
				Player player = action.getPlayer();
				player.setActive(Player.FOLDED);
				this.addPlayer(player);
				break;

			case REMOVEPLAYER:
				
				if(this.playerList.get(action.getPlayer().getId())==null)
					return;
				
				this.outGoingList.add(action.getPlayer());
				int position = action.getPlayer().getId();
				if(positionOfCurrentPlayer == position && currentTurn!= -1)
					this.processBet(ACTION_FOLD);
				break;

			case STARTTABLE:
				currentTurn = 0;
				this.startGame();
				break;

			case STOPTABLE:
				this.lastGame = true;
				break;


			case TIMEOUT:
				this.lastPokerGameAction = new GameAction(positionOfCurrentPlayer, PokerAction.FOLD);
				lastPokerGameAction.setPlayer(this.playerList.get(positionOfCurrentPlayer));
				this.processBet(ACTION_FOLD);
				break;

			default:
				break;


			}
			return;
		}

		//If incorrect player ignore!
		if(this.positionOfCurrentPlayer != action.getPosition())
			return;

		if(this.playTimer!=null)
			this.playTimer.cancel();
		
		lastPokerGameAction = action;

		/*
		 * Player based Game Actions
		 */
		switch(pokerAction)
		{

		case CALL:
		case BET:
		case RAISE:
		case RERAISE:
			this.processBet(action.getValue());
			break;

		case FOLD:
			this.processBet(ACTION_FOLD);
			break;

		case CHECK:
			this.processBet(0);
			break;

		default:
			break;

		}
	}

	/**
	 * Returns the number of players that are actually present
	 * 
	 * @return
	 */
	public int getValidPlayerCount()
	{
		int count = 0;

		for(int i = 0; i < this.playerList.size(); i++)
		{
			if(this.playerList.get(i) != null) count++;
		}

		return count;
	}

	/**
	 * Retrieves the next user that is not all in or folded
	 * 
	 * @return
	 */
	private int getNextPositionTurn()
	{	
		int temp = this.positionOfCurrentPlayer;
		while(true)
		{
			temp++;
			int next = (temp) % playerList.size();

			if(next == this.positionOfCurrentPlayer)
			{
				//.e("GameMechanics", "Player turn ended at original person");
				return this.positionOfCurrentPlayer;
			}

			Player player = playerList.get(next);
			if(player == null) continue;

			if(player.getActive() == Player.CURRENT)
				return next;
		}
	}

	/**
	 * Retrieves the next valid user after the given position
	 * 
	 * @param position
	 * @return
	 */
	public int getValidUser(int position)
	{
		int temp = position;
		while(true)
		{
			temp++;
			int next = (temp) % playerList.size();

			if(next == position)
			{
				Log.e("GameMechanics", "Next Dealer ended at original person");
				return position;
			}

			Player player = playerList.get(next);
			if(player == null) continue;

			return next;
		}
	}

	/**
	 * Generates the GameState for each client and adds them to queue
	 * 
	 */
	private void updateState(boolean unfilteredCurrentPlayers)
	{
		gameUpdateCount++;
		ArrayList<Player> playersData = new ArrayList<Player>();

		/*
		 * For each player generate update
		 */
		for(Player player : playerList)
		{
			if(player == null) continue;
			playersData.clear();
			/*
			 * Add player with without their cards, unless ALL IN or is the current player
			 */
			for(int i = 0; i < playerList.size(); i++)
			{
				Player temp = playerList.get(i);
				if(temp == null){
					playersData.add(null);
					continue;
				}

				Player limitedPlayerData = new Player(temp.getId(), temp.getUsername(), temp.getAmountMoney());
				
				/*
				 * If all in or current player - include cards
				 * 
				 * Or if unfiltered - include all players who are all in or current (GAME WON)
				 * 
				 */
				if((temp.getActive() == Player.ALL_IN || limitedPlayerData.getId() == player.getId())
						||
						((temp.getActive() == Player.ALL_IN || temp.getActive() == Player.CURRENT) && unfilteredCurrentPlayers)
						)
				{
					limitedPlayerData.setCard(temp.getCard(0), 0);
					limitedPlayerData.setCard(temp.getCard(1), 1);
				}

				limitedPlayerData.setActive(temp.getActive());

				playersData.add(limitedPlayerData);	
			}
			GameState gameState = new GameState(
					gameUUID,
					player, 
					null, 
					this.positionOfCurrentPlayer, 
					(ArrayList<Player>)playersData.clone(), 
					currentDealer, 
					blindAmount, 
					mainPot, 
					currentSidePots,
					this.communityCards.clone(), 
					this.lastPokerGameAction,
					30,
					gameUpdateCount);

			this.queue.add(gameState);
		}
	}

	/**
	 * Generates the GameState for each client and adds them to queue
	 * 
	 */
	private void updateState()
	{
		updateState(false);
	}



	/**
	 * add player to game
	 * @param p - player to be added
	 * @return the new size of the game or -1 if already full
	 */
	private void addPlayer(Player p) {
		this.playerList.add(p.getId(), p);

		if(this.getValidPlayerCount() != 1 && this.currentTurn == 0)
		{
			this.startGame();
		}
	}

	/**
	 * remove player from game
	 * @param p - player removed
	 * @return new list of players present
	 */
	private void removePlayer(Player p) {
		this.playerList.remove(p.getId());

	}

	/**
	 * set up the game
	 */
	private void startGame() {

		this.gameUUID = UUID.randomUUID();
		gameUpdateCount = 0;

		for(Player player : this.playerList)
		{
			if(player == null) continue;
			
			if(player.getAmountMoney() == 0)
			{
				this.server.removePlayerThreads(player);
				this.outGoingList.add(player);
			}
		}
		
		
		for(Player player : this.outGoingList)
		{
			//Not sure if this is correct just reseting to null not removing
			this.playerList.set(player.getId(),null);
		}
		this.outGoingList.clear();

		newGameReset();
		if(this.getValidPlayerCount() == 1) 
			{
			updateState();
			return;
			}

		for(int i=0; this.playerList.size()>i;i++) {
			Player player = this.playerList.get(i);
			if(player == null) continue;
			player.setActive(Player.CURRENT);
		}

		this.currentDealer = getValidUser(this.currentDealer);
		this.positionOfCurrentPlayer = getValidUser(this.currentDealer);
		
		/*
		 * get deck shuffle
		 * setup pots and start betting
		 */
		
		myDeck.resetDeck();
		this.myDeck.shuffle();
		this.mainPot = new Pot(0, 0);
		for(int i=0; this.playerList.size()>i;i++) {
			Player player = this.playerList.get(i);
			if(player == null) continue;
			this.mainPot.addParticipants(this.playerList.get(i).getId());
		}
		this.mainPot.mainPot();
		this.currentSidePots = new ArrayList<Pot>();
		this.currentTurn=1;
		for (int i = 0; i<this.playerList.size(); i++) {
			Player p = this.playerList.get(i);
			if(p == null) continue;

			p.setCard(this.myDeck.getCard(), 0);
			p.setCard(this.myDeck.getCard(), 1);
		}

		this.nextTurn();
		this.blinds();
		lastPokerGameAction = new GameAction(PokerAction.STARTGAME);
		lastPokerGameAction.setPosition(SERVER_POSITION);
		updateState();
		
		postStart = true;
		
		this.playTimer.startTimer();
	}

	/**
	 * go to the next round
	 */
	private void nextTurn() {
		
		noBets = false;
		
		if (this.currentTurn%2 == 1){
			this.mainPot.resetPlayerAmount();
			//first round only
			if(currentTurn == 1)
				this.mainPot.setAmount(this.blindAmount);
			else
				this.mainPot.setAmount(0);
			this.playerBetsInARound = new ArrayList<Integer>();
			this.currentTurn++;
		} else if(this.currentTurn ==2) {
			this.communityCards[0] = this.myDeck.getCard();
			this.communityCards[1] = this.myDeck.getCard();
			this.communityCards[2] = this.myDeck.getCard();

			//Display something
			this.currentTurn++;
			this.nextTurn();

		} else if (this.currentTurn ==4) {
			this.communityCards[3] = this.myDeck.getCard();

			//Display something
			this.currentTurn++;
			this.nextTurn();

		} else if (this.currentTurn == 6) {
			this.communityCards[4] = this.myDeck.getCard();

			//Display something
			this.currentTurn++;
			this.nextTurn();

		} else {
			System.out.println("End Game");
			this.currentTurn = 0;
			this.endGame();
		}

	}
	/**
	 * finish the game and give the winner their money
	 */
	private void endGame() {

		this.playTimer.cancel();
		if(isAllFolded&& this.currentSidePots.size()==0) {
			int countfolds=0;
			int inPlayer =-1;
			for(int i=0; i<this.playerList.size();i++) {
				Player player = this.playerList.get(i);
				if(player == null) continue;

				if(player.getActive()<=1)
					countfolds++;
				else {
					inPlayer =i;
				}
			}
			this.winAllPots(inPlayer);
		} else {
		
			ArrayList<Player>currentPlayer = new ArrayList<Player>();
			for (int i =0; i<this.playerList.size(); i++) {
				Player player = this.playerList.get(i);
				if(player == null) continue;
	
				if (player.getActive()!=Player.FOLDED) {
					currentPlayer.add(player);
				}
			}
			for (int i = 0; i<this.currentSidePots.size();i++) {
				//Arra
				Player[] winners = determineWinners(currentPlayer);
				this.currentSidePots.get(i).setWinners(winners);
				for(int j=0; j<winners.length; j++) {
					System.out.println("Players" +winners[j] + "Won");
					winners[j].addMoney(this.currentSidePots.get(i).getTotal()/winners.length);
				}
				currentPlayer.remove(this.currentSidePots.get(i).getOwner());
				/*WTF - why is this player folding? - ASK LAWTON*/
				//this.playerList.get(i).setActive(0);
			}
			Player[] winners = determineWinners(currentPlayer);
			this.mainPot.setWinners(winners);
			for(int j=0; j<winners.length; j++) {
				System.out.println("Players" +winners[j].getId() + "Won");
				winners[j].addMoney(this.mainPot.getTotal()/winners.length);
			}
		}
		
		//@TODO
		//ADDED LATE
		updateState(true);
		
		isAllFolded = false;
		lastPokerGameAction = new GameAction(PokerAction.ENDGAME);
		lastPokerGameAction.setPosition(SERVER_POSITION);
		updateState(true);

		if(this.playTimer!=null)
			this.playTimer.cancel();
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {}

		if(terminateSignaled) return;
		
		currentTurn = -1;
		
		if(!lastGame)
			this.startGame();

	}


	/**
	 * determine the winner between all the hands
	 * @return player array of winners 
	 */
	private Player[] determineWinners(ArrayList<Player> currentPlayer) {
		int rank[] = {11,11,11,11,11,11,11};
		int highcard[] = {-1,-1,-1,-1,-1,-1,-1};
		for (int i=0; i<currentPlayer.size();i++) {

			Player CPlayer = currentPlayer.get(i);
			if(CPlayer == null) continue;

			Card hand[] = {CPlayer.getCard(0),CPlayer.getCard(1), this.communityCards[0], this.communityCards[1], this.communityCards[2], this.communityCards[3], this.communityCards[4]};
			Comparator rankcompare = new Card.RankComparator();
			Comparator suitcompare = new Card.SuitComparator();
			if (CPlayer.getActive()!=Player.FOLDED) {
				boolean flush = false;
				boolean straight = false;
				boolean fullhouse = false;
				int count=0;
				int suit=-1;
				Card st_hand[];
				Card tempHand[] = new Card[7];
				Arrays.sort(hand, suitcompare);
				//flush
				for(int j= 0; j<hand.length && 7-j>=5-count;j++) {
					if (hand[j].getSuit()!=suit) {
						if(count>=5) {
							flush =true;
							break;
						}else {
							count =1;
							suit = hand[j].getSuit();
							tempHand[0] = hand[j];
						}
					} else {
						tempHand[count] = hand[j];
						count++;
					}
				}
				if(flush) {
					st_hand = new Card[count];
					for(int k=0; k<count; k++)
						st_hand[k]=tempHand[k];
				} else{
					st_hand = hand;
				}

				//straight
				count =0;
				Arrays.sort(st_hand, rankcompare);
				int next=-1;
				int last=-1;
				int straighthigh=-1;
				for(int j=st_hand.length-1; j>=0;j--) {
					Card tempCard = st_hand[j];
					if(tempCard.getRank()==next) {
						count++;
						next = tempCard.getRank()-1;
						last = tempCard.getRank();
						if(count==5 ||(next==-1&& count==4&& st_hand[st_hand.length-1].getRank()==12)) {
							straight=true;
							break;
						}

					} else if(tempCard.getRank()==last){
						continue;
					} else {
						straighthigh = tempCard.getRank();
						count = 1;
					}
				}
				if(flush&&straight) {
					rank[i]=1;
					continue;
				} 

				//pairs
				int pairs =0;
				boolean triple=false;
				count =0;
				last =-1;
				Arrays.sort(hand, rankcompare);
				for(int j=hand.length-1; j>=0;j--) {
					Card tempCard = hand[j];
					if (last ==tempCard.getRank()) {
						count++;
						continue;
					} else if(count==4) {
						highcard[i]=hand[hand.length-1].getRank();
						break;
					} else if(count==3) {
						if(triple||pairs!=0){
							fullhouse=true;
						}else {
							highcard[i]=tempCard.getRank();
						}
						triple =true;
					} else if(count==2){
						pairs++;
						highcard[i]=tempCard.getRank();
					}
					count=1;
					last =tempCard.getRank();
				}

				Arrays.sort(hand, rankcompare);
				System.out.println("HERE IS THERE HAND:");
				for(int k=0; k<hand.length;k++) {	
					System.out.print(hand[k]);
				}
				//make ranks
				if(count==4) {
					rank[i]=2;
				} else if(fullhouse) {
					rank[i]=3;
				} else if(flush){
					rank[i]=4;
					highcard[i]=st_hand[st_hand.length-1].getRank();
				} else if (straight) {
					rank[i]=5;
					highcard[i] = straighthigh;
				} else if(triple) {
					rank[i]=6;
					highcard[i]=hand[hand.length-1].getRank();
				} else if(pairs>1) {
					rank[i]=7;
					highcard[i]=hand[hand.length-1].getRank();
				} else if(pairs==1) {
					rank[i]=8;
					highcard[i]=hand[hand.length-1].getRank();
				} else {
					rank[i]=9;
					highcard[i]=hand[hand.length-1].getRank();
				}
				System.out.println("\n HERE IS THE RANK" + rank[i]);

			}
		}
		int max=10;
		boolean tie=false;
		ArrayList<Player> BestPlayers = new ArrayList<Player>();

		//Player[] BestPlayer = new Player[1];
		int curHigh =-1;
		for(int i=0; i<rank.length;i++) {
			if(rank[i]<max) {
				BestPlayers = new ArrayList<Player>();
				BestPlayers.add(currentPlayer.get(i));
				curHigh = highcard[i];
				max=rank[i];
				tie =false;
			} else if(rank[i]==max) {
				if(highcard[i]>curHigh) {
					BestPlayers = new ArrayList<Player>();
					BestPlayers.add(currentPlayer.get(i));
					curHigh = highcard[i];
					tie=false;
				} else if(highcard[i]==curHigh) {
					BestPlayers.add(currentPlayer.get(i));
					tie=true;
				}
			}
		}
		System.out.println("Best hand: " +max);
		System.out.println("High: " +max);
		Player[] BestPlayer = new Player[BestPlayers.size()];
		for(int i=0; i<BestPlayer.length; i++) {
			BestPlayer[i]=BestPlayers.get(i);
		}
		return BestPlayer;
	}

	private void placeBet(int bet)
	{
		int isCurrentPlayer = this.playerList.get(positionOfCurrentPlayer).getActive();
		int playerCurrentBetInPot = this.mainPot.getPlayerAmount(this.playerList.get(positionOfCurrentPlayer).getId());


		//		/*BROKEN FOR RERAISING - in case that amount == amount but user is attempting to reraise on his turn*/
		if(isCurrentPlayer!=Player.CURRENT || this.mainPot.getAmount()== playerCurrentBetInPot && bet == 0) {
			return;
		}


		if(bet==ACTION_FOLD) {
			this.playerList.get(positionOfCurrentPlayer).setActive(Player.FOLDED);
			if(checkForFolds()){
				isAllFolded = true;
				noBets=true;
				if(this.currentSidePots.size()==0) {
					endGame();
				}
				return;
			}
		} else {
			System.out.println(this.playerList.get(positionOfCurrentPlayer).getActive());
			this.playerList.get(positionOfCurrentPlayer).removeMoney(bet);
			for(int j=0; j<this.currentSidePots.size();j++) {
				Pot sidePot = this.currentSidePots.get(j);
				int amountOwing = (sidePot.getAmount()-sidePot.getPlayerAmount(positionOfCurrentPlayer));
				if(bet>=amountOwing) {
					bet-=amountOwing;
					sidePot.addTotal(amountOwing);
					sidePot.setPlayerAmount(positionOfCurrentPlayer, amountOwing);
					//sidePot.addParticipants(this.playerList.get(positionOfCurrentPlayer).getId());
				} else {
					//sidePot.setAmount(sidePot.getAmount()-bet);
					//sidePot.take(bet*sidePot.size());
					this.currentSidePots.add(j, new Pot(this.playerList.get(positionOfCurrentPlayer).getId(), bet));
					this.currentSidePots.get(j).transfer(sidePot);
					this.playerList.get(positionOfCurrentPlayer).setActive(Player.ALL_IN);
					if(checkForFolds()){
						isAllFolded = true;
						noBets=true;
						return;
					}
					break;
				}
			}
			if (this.playerList.get(positionOfCurrentPlayer).getActive()!=Player.ALL_IN) {
				int totalbet = this.mainPot.getPlayerAmount(this.playerList.get(positionOfCurrentPlayer).getId()) + bet;
				int amountOwing = this.mainPot.getAmount()-this.mainPot.getPlayerAmount(this.playerList.get(positionOfCurrentPlayer).getId());
				if(this.playerList.get(positionOfCurrentPlayer).getAmountMoney()==0) {
					this.currentSidePots.add(new Pot(this.playerList.get(positionOfCurrentPlayer).getId(), totalbet));
					this.currentSidePots.get(this.currentSidePots.size()-1).transfer(this.mainPot);
					this.playerList.get(positionOfCurrentPlayer).setActive(Player.ALL_IN);
					if(checkForFolds()){
						isAllFolded = true;
						noBets=true;
					}
				} else if(totalbet>this.mainPot.getAmount()) {
					this.mainPot.setAmount(totalbet);
					this.mainPot.setPlayerAmount(this.playerList.get(positionOfCurrentPlayer).getId(), totalbet);
					this.mainPot.addTotal(bet);
				} else if (totalbet==this.mainPot.getAmount()) {
					this.mainPot.addTotal(bet);
					this.mainPot.setPlayerAmount(this.playerList.get(positionOfCurrentPlayer).getId(),totalbet);
				} else {
					this.currentSidePots.add(new Pot(this.playerList.get(positionOfCurrentPlayer).getId(), totalbet));
					this.currentSidePots.get(this.currentSidePots.size()-1).transfer(this.mainPot);
					this.playerList.get(positionOfCurrentPlayer).setActive(Player.ALL_IN);
					if(checkForFolds()){
						isAllFolded = true;
						noBets=true;
					}
				}
			}
		}
	}

	/**
	 * Start off the betting round
	 */
	private void processBet(int bet) {

		postStart = false;
		
		if(bet == ACTION_FOLD) {
			for (int i = 0; i<this.currentSidePots.size(); i++) {
				this.currentSidePots.get(i).removeParticipants(this.positionOfCurrentPlayer);
			}
			this.mainPot.removeParticipants(this.positionOfCurrentPlayer);
		}

		if(isAllFolded)
		{
			
			this.nextTurn();
			if(isAllFolded)
				this.processBet(0);
			return;
		}

		this.placeBet(bet);
		int next = this.getNextPositionTurn();

		if(isAllFolded && this.currentSidePots.size()!=0)
		{
			this.processBet(0);
			return;
		}
		
		if(postStart) return;
		
		/*If we have already seen a this person bet, and the above 
		 * !this.mainPot.checkPlayersBet()&&!noBets evaluated false
		 * 
		 * -We know it time for the next round.
		 */

		if(playerBetsInARound.contains(next))
		{
			if(this.mainPot.checkPlayersBet() || noBets)
			{
				this.nextTurn();
				if(this.lastPokerGameAction.getAction() == PokerAction.ENDGAME || 
				   this.lastPokerGameAction.getAction() == PokerAction.STARTGAME) return;
			}
			else 
				playerBetsInARound.clear();
		}else
		{
			playerBetsInARound.add(this.positionOfCurrentPlayer);
		}

		this.positionOfCurrentPlayer = next;
		this.updateState();
		this.playTimer.startTimer();
	}
	

	/**
	 * Checks to see if all other players have folded
	 * @return
	 */
	private boolean checkForFolds() {
		int countfolds =0;
		for(int i=0; i<this.playerList.size();i++) {
			Player player = this.playerList.get(i);
			if(player == null) continue;

			if(player.getActive()<=1)
				countfolds++;
		}
		if(countfolds==(getValidPlayerCount()-1)) {
			return true;
		} else {
			return false;
		}
	}

	private void winAllPots(int player) {
		for(int i=0; this.currentSidePots.size()>i;i++) {
			this.playerList.get(player).addMoney(this.currentSidePots.get(i).getTotal());
			this.currentSidePots.get(i).setWinners(new Player[] {this.playerList.get(player)});
		}
		this.playerList.get(player).addMoney(this.mainPot.getTotal());
		this.mainPot.setWinners(new Player[] {this.playerList.get(player)});
	}

	/**
	 * Just some fake stuff to mimic the gui
	 * @param id2
	 * @return
	 */
	private int gui(int id2) {
		int bet =0;
		System.out.println("Who is in for " + this.mainPot.getPlayerAmount(id2));
		System.out.println("Main Pot " + this.mainPot.getAmount());
		System.out.println("Please enter bet for (-1 to fold): Player " + id2);
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		try {
			String s = in.readLine();
			if(s.equals("e")) {
				System.exit(0);
			}
			bet = Integer.parseInt(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bet;
	}
	/**
	 * get the blinds 
	 */
	private void blinds(){
		int bigBlind;
		int smallBlind;
		smallBlind = getValidUser(this.currentDealer);
		bigBlind = getValidUser(smallBlind);

		bigBlind(bigBlind);
		smallBlind(smallBlind);
	}

	/**
	 * get the small blind
	 * @param smallBlind
	 */
	private void smallBlind(int smallBlind) {
		Player p = this.playerList.get(smallBlind);
		if(p.getAmountMoney()>this.blindAmount/2) {
			this.tempBets[smallBlind] = this.blindAmount/2;
			p.removeMoney(this.blindAmount/2);
		} else {
			this.tempBets[smallBlind] = p.getAmountMoney();
			p.setActive(Player.ALL_IN);
			this.currentSidePots.add(new Pot(this.playerList.get(positionOfCurrentPlayer).getId(), tempBets[smallBlind]));
			this.currentSidePots.get(this.currentSidePots.size()-1).transfer(this.mainPot);
			this.playerList.get(positionOfCurrentPlayer).setActive(Player.ALL_IN);
			if(checkForFolds()){
				isAllFolded = true;
				noBets=true;
			}
		}

		this.mainPot.setPlayerAmount(smallBlind, this.tempBets[smallBlind]);
		this.mainPot.addTotal(this.tempBets[smallBlind]);
	}

	/**
	 * get the big blind
	 * @param bigBlind
	 */
	private void bigBlind(int bigBlind) {
		Player p = this.playerList.get(bigBlind);
		if(p.getAmountMoney()>this.blindAmount) {
			this.tempBets[bigBlind] = this.blindAmount;
			p.removeMoney(this.blindAmount);
		} else {
			this.tempBets[bigBlind] = p.getAmountMoney();
			p.setActive(Player.ALL_IN);
			this.currentSidePots.add(new Pot(this.playerList.get(positionOfCurrentPlayer).getId(), tempBets[bigBlind]));
			this.currentSidePots.get(this.currentSidePots.size()-1).transfer(this.mainPot);
			this.playerList.get(positionOfCurrentPlayer).setActive(Player.ALL_IN);
			if(checkForFolds()){
				isAllFolded = true;
				noBets=true;
			}
		}

		this.mainPot.setPlayerAmount(bigBlind, this.tempBets[bigBlind]);
		this.mainPot.addTotal(this.tempBets[bigBlind]);

	}
	
	public void terminate()
	{
		this.terminateSignaled = true;
	}

}

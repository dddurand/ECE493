package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import server.GameAction;
import server.GameState;
import server.GameAction.PokerAction;

/**
 * 
 * @author lawton
 *
 *
 */
public class GameMechanics {

	public static final int ACTION_FOLD = -1;
	public static final int SERVER_POSITION = -3;
	
	private Deck myDeck = new Deck();
	
	private int id;
	private int positionOfCurrentPlayer;
	private int currentTurn = -1;
	private int currentDealer;
	private int blindAmount;
	
	
	private int[] tempBets = new int[6];
	private Card[] communityCards = new Card[5];
	
	private int currentBet;
	
	private boolean lastGame = false;
	private boolean noBets =false;

	private Pot mainPot;
	private GameAction lastPokerGameAction;
	private UUID gameUUID;
	
	private int gameUpdateCount;

	private boolean isAllFolded = false;
	
	private ArrayList<Integer> playerBetsInARound;
	private ArrayList<Player> outGoingList = new ArrayList<Player>();
	private ArrayList<Player> playerList = new ArrayList<Player>();
	private ArrayList<Pot> sidePots;
	private ArrayList<Pot> currentSidePots;
	private BlockingQueue<GameState> queue;
	
	//private ArrayList<Integer> playerBetsInARound;

	private void newGameReset()
	{
		currentTurn = -1;
		sidePots = new ArrayList<Pot>();
		currentSidePots = new ArrayList<Pot>();
		tempBets = new int[6];
		communityCards = new Card[5];
	}
	
	public static void main(String[] args) {

		GameMechanics mech = new GameMechanics(new Player[6], 0, 30, null);

		Player player = new Player(0, "BOB", 1000);
		Player player1 = new Player(1, "Fred", 1000);
		Player player2 = new Player(2, "John", 1000);



		GameAction addPlayer0 = new GameAction(player, true);
		GameAction addPlayer1 = new GameAction(player1, true);

		mech.processGameAction(addPlayer0);
		mech.processGameAction(addPlayer1);

		GameAction actionStart = new GameAction(PokerAction.STARTTABLE);
		mech.processGameAction(actionStart);

		while(true)
		{
			int position = mech.positionOfCurrentPlayer;
			int bet = mech.gui(position);
			GameAction x = new GameAction(position, PokerAction.CALL, bet);
			mech.processGameAction(x);
		}

		//GameAction action = new GameAction(player, true);

	}

	/**
	 * The main interface for processing actions from the clients.
	 * 
	 * These GameActions provided to this object drivse the whole GameMechanics engine, 
	 * and must be processed through this interface.
	 * 
	 * 
	 * @param action
	 */
	public void processGameAction(GameAction action)
	{
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
				this.outGoingList.add(action.getPlayer());
				int position = action.getPlayer().getId();
				if(positionOfCurrentPlayer == position)
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
				this.processBet(ACTION_FOLD);
				this.lastPokerGameAction = new GameAction(positionOfCurrentPlayer, PokerAction.FOLD);
				updateState();
				break;

			default:
				break;


			}
			return;
		}
		
		//If incorrect player ignore!
		if(this.positionOfCurrentPlayer != action.getPosition())
			return;

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
			updateState();
			break;

		case FOLD:
			this.processBet(ACTION_FOLD);
			updateState();
			break;

		case CHECK:
			this.processBet(0);
			updateState();
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
	 * Retreves the next user that is not all in or folded
	 * 
	 * @return
	 */
	public int getNextPositionTurn()
	{	
		int temp = this.positionOfCurrentPlayer;
		while(true)
		{
			temp++;
			int next = (temp) % playerList.size();

			if(next == this.positionOfCurrentPlayer)
			{
				//Log.e("GameMechanics", "Player turn ended at original person");
				return this.positionOfCurrentPlayer;
			}

			Player player = playerList.get(next);
			if(player == null) continue;

			if(player.getActive() == Player.CURRENT)
				return next;
		}
	}

	/**
	 * Retrieves the next valid user afer the given position
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
				//Log.e("GameMechanics", "Next Dealer ended at original person");
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
	public void updateState(boolean unfilteredCurrentPlayers)
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
				if(temp == null) continue;
				
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

				limitedPlayerData.setActive(player.getActive());

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
					sidePots, 
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
	public void updateState()
	{
		updateState(false);
	}

	/**
	 * create game mechanics
	 * @param playerList
	 * @param Dealer
	 * @param blindAmount
	 */
	public GameMechanics(Player[] playerList, int Dealer, int blindAmount, BlockingQueue<GameState> queue) {
		this.playerList = new ArrayList<Player>(Arrays.asList(playerList));;
		this.currentDealer = Dealer;
		this.blindAmount = blindAmount;
		this.queue = queue;
	}

	/**
	 * add player to game
	 * @param p - player to be added
	 * @return the new size of the game or -1 if already full
	 */
	public void addPlayer(Player p) {
		this.playerList.add(p.getId(), p);

		if(this.getValidPlayerCount() == 1 && this.currentTurn == 0)
		{
			this.startGame();
		}
	}

	/**
	 * remove player from game
	 * @param p - player removed
	 * @return new list of players present
	 */
	public void removePlayer(Player p) {
		this.playerList.remove(p.getId());

	}

	/**
	 * set up the game
	 */
	public void startGame() {

		this.gameUUID = UUID.randomUUID();
		gameUpdateCount = 0;
		
		for(Player player : this.outGoingList)
		{
			this.playerList.remove(player.getId());
		}
		this.outGoingList.clear();

		if(this.getValidPlayerCount() == 1) return;

		for(int i=0; this.playerList.size()>i;i++) {
			Player player = this.playerList.get(i);
			if(player == null) continue;
			player.setActive(Player.CURRENT);
		}

		this.currentDealer = getValidUser(this.currentDealer);

		newGameReset();
		myDeck.resetDeck();
		this.myDeck.shuffle();
		this.mainPot = new Pot(this.playerList.get(0).getId(), 0);
		for(int i=0; this.playerList.size()>i;i++) {
			Player player = this.playerList.get(i);
			if(player == null) continue;
			this.mainPot.addParticipants(this.playerList.get(i).getId());
		}
		this.mainPot.mainPot();
		this.sidePots = new ArrayList<Pot>();
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
	}

	/**
	 * go to the next round
	 */
	public void nextTurn() {
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

		isAllFolded = false;

		ArrayList<Player>currentPlayer = new ArrayList<Player>();
		for (int i =0; i<this.playerList.size(); i++) {
			Player player = this.playerList.get(i);
			if(player == null) continue;

			if (player.getActive()!=Player.FOLDED) {
				currentPlayer.add(player);
			}
		}
		for (int i = 0; i<this.sidePots.size();i++) {
			Player[] winners = determineWinners();
			this.sidePots.get(i).setWinners(winners);
			for(int j=0; j<winners.length; j++) {
				//int pos=-1;
				//for(int k=0; k<this.playerList.size();k++) {
				//if(this.playerList.get(k).getId()==winners)
				//}
				System.out.println("Players" +winners[j] + "Won");
				winners[j].addMoney(this.sidePots.get(i).getTotal()/winners.length);
			}
			/*WTF - why is this player folding? - ASK LAWTON*/
			this.playerList.get(i).setActive(0);
		}
		Player[] winners = determineWinners();
		this.mainPot.setWinners(winners);
		for(int j=0; j<winners.length; j++) {
			System.out.println("Players" +winners[j].getId() + "Won");
			winners[j].addMoney(this.mainPot.getTotal()/winners.length);
		}

		lastPokerGameAction = new GameAction(PokerAction.ENDGAME);
		lastPokerGameAction.setPosition(SERVER_POSITION);
		updateState(true);

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {}
		
		if(!lastGame)
			this.startGame();

	}

	/**
	 * determine the winner between all the hands
	 * @return player array of winners 
	 */
	private Player[] determineWinners() {
		int rank[] = {11,11,11,11,11,11,11};
		int highcard[] = {-1,-1,-1,-1,-1,-1,-1};
		for (int i=0; i<this.playerList.size();i++) {

			Player CPlayer = this.playerList.get(i);
			if(CPlayer == null) continue;

			Card hand[] = {CPlayer.getCard(0),CPlayer.getCard(1), this.communityCards[0], this.communityCards[1], this.communityCards[2], this.communityCards[3], this.communityCards[4]};
			Comparator rankcompare = new rankComparator();
			Comparator suitcompare = new suitComparator();
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
				BestPlayers.add(this.playerList.get(i));
				curHigh = highcard[i];
				max=rank[i];
				tie =false;
			} else if(rank[i]==max) {
				if(highcard[i]>curHigh) {
					BestPlayers = new ArrayList<Player>();
					BestPlayers.add(this.playerList.get(i));
					curHigh = highcard[i];
					tie=false;
				} else if(highcard[i]==curHigh) {
					BestPlayers.add(this.playerList.get(i));
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

	public void placeBet(int bet)
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
				return;
			}
		} else{
			System.out.println(this.playerList.get(positionOfCurrentPlayer).getActive());
			this.playerList.get(positionOfCurrentPlayer).removeMoney(bet);
			for(int j=0; j<this.currentSidePots.size();j++) {
				Pot sidePot = this.currentSidePots.get(j);
				if(bet>=sidePot.getAmount() &&!sidePot.exist(this.playerList.get(positionOfCurrentPlayer).getId())) {
					bet-=sidePot.getAmount();
					sidePot.addTotal(sidePot.getAmount());
					sidePot.addParticipants(this.playerList.get(positionOfCurrentPlayer).getId());
				} else {
					sidePot.setAmount(sidePot.getAmount()-bet);
					sidePot.take(bet*sidePot.size());
					this.currentSidePots.add(j, new Pot(this.playerList.get(positionOfCurrentPlayer).getId(), bet));
					this.currentSidePots.get(j).addList(sidePot.getParticipants());
					this.playerList.get(positionOfCurrentPlayer).setActive(Player.ALL_IN);
					break;
				}
			}
			if (this.playerList.get(positionOfCurrentPlayer).getActive()!=1) {
				int totalbet = this.mainPot.getPlayerAmount(this.playerList.get(positionOfCurrentPlayer).getId()) + bet;
				int amountOwing = this.mainPot.getAmount()-this.mainPot.getPlayerAmount(this.playerList.get(positionOfCurrentPlayer).getId());
				if(totalbet>this.mainPot.getAmount()) {
					this.mainPot.setAmount(totalbet);
					this.mainPot.setPlayerAmount(this.playerList.get(positionOfCurrentPlayer).getId(), totalbet);
					this.mainPot.addTotal(bet);
				} else if (totalbet==this.mainPot.getAmount()) {
					this.mainPot.addTotal(bet);
					this.mainPot.setPlayerAmount(this.playerList.get(positionOfCurrentPlayer).getId(),totalbet);
				} else {
					this.currentSidePots.add(new Pot(this.playerList.get(positionOfCurrentPlayer).getId(), totalbet));
					this.currentSidePots.get(this.currentSidePots.size()-1).addList(this.mainPot.getMainParticipants());
					this.mainPot.decrementPlayerAmount(totalbet);
					this.playerList.get(positionOfCurrentPlayer).setActive(Player.ALL_IN);
				}
			}
		}
	}

	/**
	 * Start off the betting round
	 */
	private void processBet(int bet) {

		System.out.println("Who is in for " + this.mainPot.getPlayerAmount(positionOfCurrentPlayer));
		System.out.println("Main Pot " + this.mainPot.getAmount());
		System.out.println("Please enter bet for (-1 to fold): Player " + positionOfCurrentPlayer);
		System.out.println("BET: "+bet);
		
		if(isAllFolded)
		{
			this.nextTurn();
			if(isAllFolded)
				this.processBet(0);
			return;
		}

		this.placeBet(bet);
		int next = this.getNextPositionTurn();

		if(isAllFolded)
		{
			this.processBet(0);
			return;
		}

		/*If we have already seen a this person bet, and the above 
		 * !this.mainPot.checkPlayersBet()&&!noBets evaluated false
		 * 
		 * -We know it time for the next round.
		 */
		System.out.println(positionOfCurrentPlayer+": ");
		System.out.print("[");
		for(Integer i : playerBetsInARound)
		{
			System.out.print(i+",");
		}
		System.out.println("]");

		if(playerBetsInARound.contains(next))
		{
			if(this.mainPot.checkPlayersBet() || noBets)
			{
				this.nextTurn();
			}
			else 
				playerBetsInARound.clear();
		}else
		{
			playerBetsInARound.add(this.positionOfCurrentPlayer);
		}

		this.positionOfCurrentPlayer = next;
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
		for(int i=0; this.sidePots.size()>i; i++) {
			this.playerList.get(player).addMoney(this.sidePots.get(i).getTotal());
		}
		this.playerList.get(player).addMoney(this.mainPot.getTotal());
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
		}

		this.mainPot.setPlayerAmount(smallBlind, this.tempBets[smallBlind]);
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
		}

		this.mainPot.setPlayerAmount(bigBlind, this.tempBets[bigBlind]);

	}

	public String getcommunityCards() {
		return this.communityCards[0].toString() +" "+ this.communityCards[1].toString() +" "+this.communityCards[2].toString();
	}
	public String getcommunityCards2() {
		return this.communityCards[0].toString() +" "+ this.communityCards[1].toString() +" "+this.communityCards[2].toString() +" "+this.communityCards[3].toString();
	}
	public String getcommunityCards3() {
		return this.communityCards[0].toString() +" "+ this.communityCards[1].toString() +" "+this.communityCards[2].toString() +" "+this.communityCards[3].toString()+" "+ this.communityCards[4].toString();
	}

}

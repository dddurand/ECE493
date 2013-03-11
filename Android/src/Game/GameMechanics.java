package Game;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class GameMechanics {
	private Deck myDeck = new Deck();
	private ArrayList<Player> playerList = new ArrayList<Player>();
	private int id;
	private int currentPlayer;
	private int currentTurn;
	private int currentDealer;
	private int blindAmount;
	private Pot mainPot;
	private ArrayList<Pot> sidePots;
	private ArrayList<Pot> currentSidePots;
	private int[] tempBets = new int[6];
	private int currentBet;
	private Card[] communityCards = new Card[5];
	boolean noBets =false;
	
	public String getcommunityCards() {
		return this.communityCards[0].toString() +" "+ this.communityCards[1].toString() +" "+this.communityCards[2].toString();
	}
	public String getcommunityCards2() {
		return this.communityCards[0].toString() +" "+ this.communityCards[1].toString() +" "+this.communityCards[2].toString() +" "+this.communityCards[3].toString();
	}
	public String getcommunityCards3() {
		return this.communityCards[0].toString() +" "+ this.communityCards[1].toString() +" "+this.communityCards[2].toString() +" "+this.communityCards[3].toString()+" "+ this.communityCards[4].toString();
	}
	/*
	 * create game mechanics
	 */
	public GameMechanics(Player[] playerList, int Dealer, int blindAmount) {
		this.playerList = new ArrayList<Player>(Arrays.asList(playerList));;
		this.currentDealer = Dealer;
		this.blindAmount = blindAmount;
		for(int i=0; this.playerList.size()>i;i++) {
			this.playerList.get(i).setActive(2);
		}
	}
	
	/*
	 * add player to game
	 */
	public int addPlayer(Player p) {
		if(this.playerList.size() ==6) {
			return -1;
		} else {
			this.playerList.add(p);
			return this.playerList.size();
		}
	}
	
	/*
	 * remove player from game
	 */
	public int removePlayer(Player p) {
		for (int i =0; i<this.playerList.size(); i++) {
			if(p.getId() == this.playerList.get(i).getId()) {
				this.playerList.remove(i);
				return this.playerList.size();
			}
		}
		return -1;
	}
	
	/*
	 * set up the game
	 */
	public void startGame() {
		this.myDeck.shuffle();
		this.mainPot = new Pot(this.playerList.get(0).getId(), 0);
		for(int i =1; this.playerList.size()>i;i++) {
			this.mainPot.addParticipants(this.playerList.get(i).getId());
		}
		this.mainPot.mainPot();
		this.sidePots = new ArrayList<Pot>();
		this.currentSidePots = new ArrayList<Pot>();
		this.currentTurn=1;
		for (int i = 0; i<this.playerList.size(); i++) {
			Player p = this.playerList.get(i);
			p.setCard(this.myDeck.getCard(), 0);
			p.setCard(this.myDeck.getCard(), 1);
		}
		this.blinds();
	}
	
	/*
	 * go to the next round
	 */
	public void nextTurn() {
		if (this.currentTurn%2 == 1){
			this.mainPot.resetPlayerAmount();
			this.mainPot.setAmount(this.blindAmount);
			this.startBetting();
		} else if(this.currentTurn ==2) {
			this.communityCards[0] = this.myDeck.getCard();
			this.communityCards[1] = this.myDeck.getCard();
			this.communityCards[2] = this.myDeck.getCard();
		} else if (this.currentTurn ==4) {
			this.communityCards[3] = this.myDeck.getCard();
		} else if (this.currentTurn == 6) {
			this.communityCards[4] = this.myDeck.getCard();
		} else {
			System.out.println("End Game");
			this.endGame();
		}
		this.currentTurn++;
	}
	/*
	 * finish the game and give the winner their money
	 */
	private void endGame() {
		ArrayList<Player>currentPlayer = new ArrayList<Player>();
		for (int i =0; i<this.playerList.size(); i++) {
			if (this.playerList.get(i).getActive()!=0) {
				currentPlayer.add(this.playerList.get(i));
			}
		}
		for (int i = 0; i<this.sidePots.size();i++) {
			Player[] winners = determineWinners();
			for(int j=0; j<winners.length; j++) {
				//int pos=-1;
				//for(int k=0; k<this.playerList.size();k++) {
					//if(this.playerList.get(k).getId()==winners)
				//}
				System.out.println("Players" +winners[j] + "Won");
				winners[j].addMoney(this.sidePots.get(i).getTotal()/winners.length);
			}
			this.playerList.get(i).setActive(0);
		}
		Player[] winners = determineWinners();
		for(int j=0; j<winners.length; j++) {
			System.out.println("Players" +winners[j].getId() + "Won");
			winners[j].addMoney(this.mainPot.getTotal()/winners.length);
		}
		
	}

	/*
	 * determine the winner between all the hands
	 */
	private Player[] determineWinners() {
		int rank[] = {11,11,11,11,11,11,11};
		int highcard[] = {-1,-1,-1,-1,-1,-1,-1};
		for (int i=0; i<this.playerList.size();i++) {
			Player CPlayer = this.playerList.get(i);
			Card hand[] = {CPlayer.getCard(0),CPlayer.getCard(1), this.communityCards[0], this.communityCards[1], this.communityCards[2], this.communityCards[3], this.communityCards[4]};
			Comparator rankcompare = new rankComparator();
			Comparator suitcompare = new suitComparator();
			if (this.playerList.get(i).getActive()!=0) {
				boolean flush = false;
				boolean straight = false;
				boolean fullhouse = false;
				int count=0;
				int suit=0;
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

	/*
	 * Start off the betting round
	 */
	private void startBetting() {

		if(noBets)
			return;
		int start = (this.currentDealer+3)%this.playerList.size();
		for(int i =0; i<this.playerList.size();i++) {
			if(this.playerList.get(i).getActive()!=2 || this.mainPot.getAmount()==this.mainPot.getPlayerAmount(this.playerList.get(i).getId())) {
				continue;
			}
			int bet = gui(this.playerList.get(i).getId());
			if(bet==-1) {
				this.playerList.get(i).setActive(0);
				if(checkForFolds()){
					//determineWinners();
					noBets=true;
					break;
				}
			} else{
				System.out.println("HERE");
				System.out.println(this.playerList.get(i).getActive());
				this.playerList.get(i).removeMoney(bet);
				for(int j=0; j<this.currentSidePots.size();j++) {
					Pot sidePot = this.currentSidePots.get(j);
					if(bet>=sidePot.getAmount() &&!sidePot.exist(this.playerList.get(i).getId())) {
						bet-=sidePot.getAmount();
						sidePot.addTotal(sidePot.getAmount());
						sidePot.addParticipants(this.playerList.get(i).getId());
					} else {
						sidePot.setAmount(sidePot.getAmount()-bet);
						sidePot.take(bet*sidePot.size());
						this.currentSidePots.add(j, new Pot(this.playerList.get(i).getId(), bet));
						this.currentSidePots.get(j).addList(sidePot.getParticipants());
						this.playerList.get(i).setActive(1);
						break;
					}
				}
				if (this.playerList.get(i).getActive()!=1) {
					int totalbet = this.mainPot.getPlayerAmount(this.playerList.get(i).getId()) + bet;
					int amountOwing = this.mainPot.getAmount()-this.mainPot.getPlayerAmount(this.playerList.get(i).getId());
					if(totalbet>this.mainPot.getAmount()) {
						this.mainPot.setAmount(totalbet);
						this.mainPot.setPlayerAmount(this.playerList.get(i).getId(), totalbet);
						this.mainPot.addTotal(bet);
					} else if (totalbet==this.mainPot.getAmount()) {
						this.mainPot.addTotal(bet);
						this.mainPot.setPlayerAmount(this.playerList.get(i).getId(),totalbet);
					} else {
						this.currentSidePots.add(new Pot(this.playerList.get(i).getId(), totalbet));
						this.currentSidePots.get(this.currentSidePots.size()-1).addList(this.mainPot.getMainParticipants());
						this.mainPot.decrementPlayerAmount(totalbet);
						this.playerList.get(i).setActive(1);
					}
				}
			}
			
		}
		
		if(!this.mainPot.checkPlayersBet()&&!noBets) {
			startBetting();
		}
		//this.mainPot.decrementPlayerAmount(this.mainPot.getAmount());
		//this.mainPot.setAmount(0);
	}
	/*
	 * Checks to see if all other players have folded
	 */
	private boolean checkForFolds() {
		int countfolds =0;
		for(int i=0; i<this.playerList.size();i++) {
			if(this.playerList.get(i).getActive()<=1)
				countfolds++;
		}
		if(countfolds==(this.playerList.size()-1)) {
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

	/*
	 * Just some fake stuff to mimic the gui
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
	/*
	 * get the blinds 
	 */
	private void blinds(){
		int bigBlind;
		int smallBlind;
		smallBlind = (this.currentDealer+1)%this.playerList.size();
		bigBlind = (this.currentDealer+2)%this.playerList.size();
		/*
		if(this.currentDealer == 0) {
			smallBlind = this.playerList.size()-1;
			bigBlind = this.playerList.size()-2;
		} else if(this.currentDealer ==1) {
			smallBlind = 0;
			bigBlind = this.playerList.size()-1;
		} else {
			smallBlind = this.currentDealer-1;
			bigBlind = this.currentDealer -2;
		}
		*/
		
		bigBlind(bigBlind);
		smallBlind(smallBlind);
	}

	/*
	 * get the small blind
	 */
	private void smallBlind(int smallBlind) {
		Player p = this.playerList.get(smallBlind);
		if(p.getAmountMoney()>this.blindAmount/2) {
			this.tempBets[smallBlind] = this.blindAmount/2;
			p.removeMoney(this.blindAmount/2);
		} else {
			this.tempBets[smallBlind] = p.getAmountMoney();
			p.setActive(1);
		}
	}

	private void bigBlind(int bigBlind) {
		Player p = this.playerList.get(bigBlind);
		if(p.getAmountMoney()>this.blindAmount) {
			this.tempBets[bigBlind] = this.blindAmount;
			p.removeMoney(this.blindAmount);
		} else {
			this.tempBets[bigBlind] = p.getAmountMoney();
			p.setActive(1);
		}
		
	}
}

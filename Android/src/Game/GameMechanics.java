package Game;
import java.util.ArrayList;


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
	
	public GameMechanics(ArrayList<Player> playerList, int Dealer, int blindAmount) {
		this.playerList = playerList;
		this.currentDealer = Dealer;
		this.blindAmount = blindAmount;
	}
	
	public int addPlayer(Player p) {
		if(this.playerList.size() ==6) {
			return -1;
		} else {
			this.playerList.add(p);
			return this.playerList.size();
		}
	}
	
	public int removePlayer(Player p) {
		for (int i =0; i<this.playerList.size(); i++) {
			if(p.getId() == this.playerList.get(i).getId()) {
				this.playerList.remove(i);
				return this.playerList.size();
			}
		}
		return -1;
	}
	
	public void startGame() {
		this.myDeck.shuffle();
		this.mainPot = new Pot(-1, 0);
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
	
	public void nextTurn() {
		if (this.currentTurn%2 == 1){
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
			this.endGame();
		}
	}
	
	private void endGame() {
		ArrayList<Player>currentPlayer = new ArrayList<Player>();
		for (int i =0; i<this.playerList.size(); i++) {
			if (this.playerList.get(i).getActive()!=0) {
				currentPlayer.add(this.playerList.get(i));
			}
		}
		for (int i = 0; i<this.sidePots.size();i++) {
			//determineWinner();
		}
		
		
	}

	private void startBetting() {
		int start = (this.currentDealer+3)%this.playerList.size();
		for(int i =0; i<this.playerList.size();i++) {
			
		}
	}

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

package game;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
/**
 * Class to hold pot objects in poker game
 * @author Lawton
 *
 */
public class Pot implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5740591961315229336L;
	
	
	private int amount;
	private int totalAmount=0;
	private ArrayList<Integer> participants = new ArrayList<Integer>();
	private Hashtable<Integer, Integer> playeramount = new Hashtable<Integer, Integer>();
	private Player[] winners = new Player[0];
	private int[] losers = null;
	
	
	public Pot(int owner, int amount) {
		this.participants.add(owner);
		this.amount = amount;
	}
	
	
	
	/**
	 * @return the winners
	 */
	public Player[] getWinners() {
		return winners;
	}
	
	/**
	 * @return the losers null if not set
	 */
	public int[] getLosers() {
		return losers;
	}


	/**
	 * Sets both an array of winners and an array of losers in the pot
	 * @param winners the winners to set
	 */
	public void setWinners(Player[] winners) {
		this.winners = winners;
		losers = new int[participants.size()-winners.length];
		int j =0,k=0;
		for(int i=0; i<participants.size(); i++) {
			if(participants.get(i)==null) 
			{
				continue;
			}
			if(winners.length > j && participants.get(i)==winners[j].getId()) {
				j++;
			} else {
				losers[k]=participants.get(i);
				k++;
			}
		}
		
	}
	

	/**
	 * sets the pot to a mainpot
	 */
	protected void mainPot(){
		for(int i=0; i<this.participants.size();i++) {
			if(participants.get(i)==null) 
			{
				continue;
			}
			this.playeramount.put(this.participants.get(i), 0);
		}
	}
	
	/**
	 * resets the amount each player currently has in pot
	 */
	protected void resetPlayerAmount() {
		Enumeration<Integer> e = this.playeramount.keys();
		while(e.hasMoreElements()) {
			int id = (int) e.nextElement();
			this.playeramount.put(id, 0);
		}
	}
	/**
	 * checks to see if there are any participants who have not matched the current bet level
	 * @return boolean
	 */
	protected boolean checkPlayersBet() {
		Enumeration<Integer> e = this.playeramount.keys();
		while(e.hasMoreElements()) {
			int id = (int) e.nextElement();
			if(this.playeramount.get(id)<this.amount){
				return false;
			}
		}
		return true;
	}
	/**
	 * adds entire list to participants in the pot
	 * @param participants
	 */
	protected void addList(ArrayList<Integer> participants) {
		this.participants.addAll(participants);
	}
	/**
	 * sets how much money player has put in towards pot
	 * @param id
	 * @param amount
	 */
	protected void setPlayerAmount(int id, int amount) {
		this.playeramount.put(id, amount);
	}
	/**
	 * returns how much money player has put in towards pot
	 * @param id
	 * @return
	 */
	public int getPlayerAmount(int id) {
		return this.playeramount.get(id);
	}
	/**
	 * 
	 * @param newpot
	 */
	protected void decrementPlayerAmount(int newpot) {
		Enumeration<Integer> e = this.playeramount.keys();
		while(e.hasMoreElements()) {
			int id = (int) e.nextElement();
			this.playeramount.put(id, this.playeramount.get(id)-newpot);
		}
	}
	/**
	 * set amount needed to call
	 * @param amount
	 */
	protected void setAmount(int amount) {
		this.amount = amount;
	}
	/**
	 * get amount needed to call
	 * @return
	 */
	public int getAmount() {
		return this.amount;
	}
	
	/**
	 * add to the total amount of the pot
	 * @param amount
	 * @return
	 */
	protected int addTotal(int amount) {
		this.totalAmount += amount;
		return this.totalAmount;
	}
	
	/**
	 * subtract money from total amount of pot
	 * @param amount
	 * @return
	 */
	protected int take(int amount) {
		this.totalAmount-=amount;
		return this.totalAmount;
	}
	/**
	 * get the total amount of money in the pot
	 * @return
	 */
	public int getTotal() {
		return this.totalAmount;
	}
	
	/**
	 * get the id of the owner of this sidepot (will just return server's id if mainpot)
	 * @return
	 */
	protected int getOwner() {
		return this.participants.get(0);
	}
	/**
	 * returns ids of all Players who currently have money in the mainpot
	 * @return
	 */
	protected ArrayList<Integer> getMainParticipants() {
		ArrayList<Integer> temp = new ArrayList<Integer>();
		Enumeration<Integer> e = this.playeramount.keys();
		while(e.hasMoreElements()) {
			int id = (int) e.nextElement();
			if(this.playeramount.get(id)!=0)
				temp.add(id);
		}
		return temp;
	}
	
	/**
	 * return number of participants in pot
	 * @return
	 */
	protected int size() {
		return this.participants.size();
	}
	
	/**
	 * returnn arraylist of all participants in pot
	 * @return
	 */
	protected ArrayList<Integer> getParticipants() {
		return this.participants;
	}
	
	
	/**
	 * add player id to participant list
	 * @param id
	 */
	protected void addParticipants(int id) {
		this.participants.add(id);
	}


	/**
	 * checks to see if id exists in partcipants list
	 * @param id
	 * @return
	 */
	public boolean exist(int id) {
		for(int i=0; i<this.participants.size();i++) {
			if(this.participants.get(i)==id) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * return bollean of whether the id given is the winner of the pot
	 * @param playerPosition
	 * @return
	 */
	public boolean isWinner(int playerPosition)
	{
		for(int i=0; i<this.winners.length;i++) {
			int id = this.winners[i].getId();
			if(playerPosition == id) return true;
		}
		return false;
	}
	
	
	
	/**
	 * This method is used in the serialization of the object
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeInt(this.amount);
		out.writeInt(this.totalAmount);
		out.writeObject(this.playeramount);
		out.writeObject(this.winners);
	}

	
	/**
	 * This method is used in the deserialization of the object
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.amount = in.readInt();
		this.totalAmount = in.readInt();
		this.playeramount = (Hashtable<Integer, Integer>) in.readObject();
		this.winners = (Player[]) in.readObject();
	}
	
	

	
}

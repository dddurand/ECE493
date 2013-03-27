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
			if(participants.get(i)==winners[j].getId()) {
				j++;
			} else {
				losers[k]=participants.get(i);
				k++;
			}
		}
		
	}
	



	protected void mainPot(){
		for(int i=0; i<this.participants.size();i++) {
			if(participants.get(i)==null) 
			{
				continue;
			}
			this.playeramount.put(this.participants.get(i), 0);
		}
	}
	
	protected void resetPlayerAmount() {
		Enumeration<Integer> e = this.playeramount.keys();
		while(e.hasMoreElements()) {
			int id = (int) e.nextElement();
			this.playeramount.put(id, 0);
		}
	}
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
	protected void addList(ArrayList<Integer> participants) {
		this.participants.addAll(participants);
	}
	protected void setPlayerAmount(int id, int amount) {
		this.playeramount.put(id, amount);
	}
	public int getPlayerAmount(int id) {
		return this.playeramount.get(id);
	}
	protected void decrementPlayerAmount(int newpot) {
		Enumeration<Integer> e = this.playeramount.keys();
		while(e.hasMoreElements()) {
			int id = (int) e.nextElement();
			this.playeramount.put(id, this.playeramount.get(id)-newpot);
		}
	}
	protected void setAmount(int amount) {
		this.amount = amount;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	protected int addTotal(int amount) {
		this.totalAmount += amount;
		return this.totalAmount;
	}
	
	protected int take(int amount) {
		this.totalAmount-=amount;
		return this.totalAmount;
	}
	
	public int getTotal() {
		return this.totalAmount;
	}
	
	protected int getOwner() {
		return this.participants.get(0);
	}
	
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
	
	protected int size() {
		return this.participants.size();
	}
	
	protected ArrayList<Integer> getParticipants() {
		return this.participants;
	}
	
	
	protected void addParticipants(int id) {
		this.participants.add(id);
	}


	public boolean exist(int id) {
		for(int i=0; i<this.participants.size();i++) {
			if(this.participants.get(i)==id) {
				return true;
			}
		}
		return false;
	}
	
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

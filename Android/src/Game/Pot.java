package Game;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class Pot {
	private int amount;
	private int totalAmount=0;
	private ArrayList<Integer> participants = new ArrayList<Integer>();
	private Hashtable<Integer, Integer> playeramount = new Hashtable<Integer, Integer>();

	public Pot(int owner, int amount) {
		this.participants.add(owner);
		this.amount = amount;
	}
	
	protected void mainPot(){
		for(int i=0; i<this.participants.size();i++) {
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
				System.out.println("player" + id + "Ain't got enough");
				System.out.println(this.playeramount.get(id));
				System.out.println(this.amount);
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
	protected int getPlayerAmount(int id) {
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
	
	protected int getAmount() {
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
	
	protected int getTotal() {
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

	
}

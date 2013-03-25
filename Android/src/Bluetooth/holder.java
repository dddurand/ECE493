package bluetooth;

import game.Player;

import java.io.IOException;
import java.io.Serializable;

import server.GameAction.PokerAction;

public class holder implements Serializable{

	private int Balance;
	private String Name;
	private static final long serialVersionUID = 1L;
	public holder(int Balance, String Name) {
		this.Balance = Balance;
		this.Name = Name;
	}
	
	public String getName() {
		return this.Name;
	}
	
	public int getBalance() {
		return this.Balance;
	}

	/**
	 * This method is used in the serialization of the object
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeInt(this.Balance);
		out.writeObject(this.Name);
	}

	
	/**
	 * This method is used in the deserialization of the object
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.Balance = in.readInt();
		this.Name = (String)in.readObject();
		
	}
}

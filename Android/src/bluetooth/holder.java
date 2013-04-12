package bluetooth;

import java.io.IOException;
import java.io.Serializable;
/**
 * serializable class to hold the users information before sending over bluetooth socket
 * @author Lawton
 *	@SRS 3.2.1.6 / 3.2.1.7
 */
public class holder implements Serializable{

	private int Balance;
	private String Name;
	private static final long serialVersionUID = 1L;
	
	/**
	 * basic constructor pass the username and balance of player
	 * @param Balance
	 * @param Name
	 */
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

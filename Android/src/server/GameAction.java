package server;

import java.io.IOException;
import java.io.Serializable;

/**
 * An object representing an action that has been made on the GUI.
 * This includes the actions itself, and any associated information
 * such as the amount bet or raised.
 * 
 * @author dddurand
 *
 */
public class GameAction implements Serializable {

	//ALSO NEED TO INCLUDE CURRENT SEAT POSITION / PLAYER CLASS
	//WE ARE USING POSITION IN TABLE AS A PLAYER ID OF SORTS
	
	private static final long serialVersionUID = -236273271024732735L;
	
	private String testMessage = "DEFAULT";
	
	/*Test Constructor*/
	public GameAction() { }
	
	/*Test Constructor*/
	public GameAction(String test)
	{
		this.testMessage = test;
	}
	
	public String getMessage()
	{
		return this.testMessage;
	}
	


	
	/**
	 * This method is used in the serialization of the object
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeUTF(this.testMessage);
	}

	
	/**
	 * This method is used in the deserialization of the object
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		String message = in.readUTF();
		this.testMessage = message;
	}

}

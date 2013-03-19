package server;

import java.io.IOException;
import java.io.Serializable;

public class GameAction implements Serializable {

	
	private String testMessage = "DEFAULT";
	
	public GameAction()
	{
	}
	
	public GameAction(String test)
	{
		this.testMessage = test;
	}
	
	public String getMessage()
	{
		return this.testMessage;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -236273271024732735L;

	
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		// write 'this' to 'out'...
		
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
	}

}

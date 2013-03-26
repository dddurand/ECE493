package application;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Semaphore;

import android.app.Application;
import android.bluetooth.BluetoothSocket;
import dataModels.Account;
import database.DatabaseDataSource;

/**
 * This class provides global access for the android application.
 * Any activities of the android application will be access these instance
 * variable through this object.
 * 
 * @author dddurand
 *
 */
public class PokerApplication extends Application {

	public final int MAX_GEN_BALANCE = 1000;
	public final String PREFS_NAME = "SETTINGS";
	private Account account = new Account();
	private DatabaseDataSource dataSource;
	private boolean isLoggedIn = false;
	private Semaphore uploadServiceSemaphore;
	private BluetoothSocket blueSocket[]=null;
	private ObjectOutputStream outStream[]=null;
	private ObjectInputStream inStream[]=null;
	
	public ObjectOutputStream[] getOutStream() {
		return outStream;
	}

	public void setOutStream(ObjectOutputStream outStream[]) {
		this.outStream = outStream;
	}	
	
	public ObjectInputStream[] getInStream() {
		return inStream;
	}

	public void setInStream(ObjectInputStream[] inStream) {
		this.inStream = inStream;
	}

	/**
	 * set the sockets that clients are connected with
	 * @param blueSocket
	 */
	public void setSocket(BluetoothSocket blueSocket[]) {
		this.blueSocket = blueSocket;
	}
	
	/**
	 * returns sockets clients are connected to
	 * @return
	 */
	public BluetoothSocket[] getSocket() {
		return this.blueSocket;
	}
	/**
	 * Retrieves the lock that used to trigger the upload thread to start.
	 * 
	 * @return
	 */
	public Semaphore getUploadServiceSemaphore() {
		return uploadServiceSemaphore;
	}


	/**
	 * Sets the lock that used to trigger the upload thread to start.
	 * 
	 * @return
	 */
	public void setUploadServiceSemaphore(Semaphore uploadServiceSemaphore) {
		this.uploadServiceSemaphore = uploadServiceSemaphore;
	}

	/**
	 * Returns if the user is logged in.
	 * @return
	 */
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	
	/**
	 * Set if the user is currently logged into the application.
	 * 
	 * @param isLoggedIn
	 */
	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	
	/**
	 * Gets the current account object that is tied to the user.
	 * 
	 * @return
	 */
	public Account getAccount() {
		return account;
	}
	
	/**
	 * Assigns a new account for the application
	 * 
	 * @param account
	 */
	public void setAccount(Account account) {
		this.account = account;
	}
	
	/**
	 * Retrieves the DataSource for the application.
	 * This object acts as the interface to the sqlite database.
	 * 
	 * @return
	 */
	public DatabaseDataSource getDataSource() {
		return dataSource;
	}
	
	/**
	 * Sets the DataSource object for the application.
	 * 
	 * @param dataSource
	 */
	public void setDataSource(DatabaseDataSource dataSource) {
		this.dataSource = dataSource;
	}
}

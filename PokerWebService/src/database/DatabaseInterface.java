package database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import util.ServletConfiguration;
import util.ServletConfiguration.Database;
import dataModels.Account;


/**
 * The main interface to the external database.
 * Each connection retrieves a unique connection to the database.
 * 
 * @author dddurand
 *
 */
public class DatabaseInterface {

	private final String USER_TABLE = "user_table";
	
	private Connection dbConnection;
	
	/**
	 * General constructor
	 * 
	 * @throws DatabaseInterfaceException
	 */
	public DatabaseInterface() throws DatabaseInterfaceException
	{
		try{
			DataSource dataSource = this.getDataSource();
			dbConnection = dataSource.getConnection();
		} 
		catch (NamingException e)
		{
			throw new DatabaseInterfaceException(e);
		} catch (SQLException e) {
			throw new DatabaseInterfaceException("Error retrieving connection to database", e);
		}
	}
	
	/**
	 * Retrieves an account based on a given username.
	 * 
	 * @param username Identifying username
	 * @return Account if found, otherwise null
	 * @throws DatabaseInterfaceException
	 */
	public Account getAccount(String username) throws DatabaseInterfaceException
	{
		final String getAccountSQL = "SELECT * FROM "+ USER_TABLE+
									 " WHERE username = ? ";
		
		CallableStatement prepStat = null;
		try {
			prepStat = dbConnection.prepareCall(getAccountSQL);
			
			prepStat.setString(1, username);
			
			ResultSet resultSet = prepStat.executeQuery();
			
			if(resultSet.next())
            {
            	String user = resultSet.getString("username");
            	String pass = resultSet.getString("password");	
            	String authToken = resultSet.getString("auth_token");	
            	
            	prepStat.close();
            	return new Account(user, pass, authToken, true);
            }
			
			prepStat.close();
			return null;
			
			
		} catch (SQLException e) {
			throw new DatabaseInterfaceException("Error Retrieving Account From Database", e);
		}
		
	}
	
	/**
	 * Removes an account's Authentication token from the database.
	 * 
	 * @param username Account's Username
	 * @throws DatabaseInterfaceException
	 */
	public void invalidateAuth(String username) throws DatabaseInterfaceException
	{
		final String setAuthSQL = "UPDATE " + USER_TABLE+
				 					 " SET auth_token = ?" +	
									 " WHERE username = ? ";
		
		try {
			CallableStatement prepStat = dbConnection.prepareCall(setAuthSQL);
			
			prepStat.setNull(1, Types.NULL);
			prepStat.setString(2, username);
			
			prepStat.execute();
			
			prepStat.close();
		} catch (SQLException e) {
			throw new DatabaseInterfaceException("Error Storing Auth Token", e);
		}

	}
	
	/**
	 * Sets the auth token for a given account.
	 * 
	 * @param username
	 * @param authToken
	 * @throws DatabaseInterfaceException
	 */
	public void setAuth(String username, String authToken) throws DatabaseInterfaceException
	{
		final String setAuthSQL = "UPDATE " + USER_TABLE+
				 					 " SET auth_token = ?" +	
									 " WHERE username = ? ";
		
		try {
			CallableStatement prepStat = dbConnection.prepareCall(setAuthSQL);
			
			prepStat.setString(1, authToken);
			prepStat.setString(2, username);
			
			prepStat.execute();
			
			prepStat.close();
		} catch (SQLException e) {
			throw new DatabaseInterfaceException("Error Storing Auth Token", e);
		}

	}
	
	public boolean addAccount(Account account) throws DatabaseInterfaceException
	{
		String insertAccountSQL = "INSERT INTO user_table (username, password) VALUES (?,?);";

		try {
			CallableStatement prepStat = dbConnection.prepareCall(insertAccountSQL);

			prepStat.setString(1, account.getUsername());
			prepStat.setString(2, account.getEncyptedPassword());

			prepStat.execute();
			
		} catch (SQLException e) {
			throw new DatabaseInterfaceException("Unable to create account.", e);
		}

		return false;

	}
	
	public boolean isDuplicateUsername(String username) throws DatabaseInterfaceException
	{
		Account account = this.getAccount(username);
		
		if(account == null) return false;
		return true;
		
	}

	
	
	
	/**
	 * Retrieves the DataSource from tomcat. The DataSource contains the database
	 * connection details that are stored in context.ini for the project.
	 * 
	 * @return
	 * @throws NamingException
	 * @throws DatabaseInterfaceException
	 */
	private DataSource getDataSource() throws NamingException, DatabaseInterfaceException
	{
		DataSource dataSource;
		Context initContext  = new InitialContext();
        
        Context envContext  = (Context)initContext.lookup("java:/comp/env");
		
        //University Database
		if(ServletConfiguration.getDatabase() == Database.UNIVERSITY)
        	dataSource = (DataSource)envContext.lookup("jdbc/DatabaseUofA");
        
		//Local Dev Database
        else if(ServletConfiguration.getDatabase() == Database.HOME)
        	dataSource = (DataSource)envContext.lookup("jdbc/DatabaseHome");
        
        else
        	throw new DatabaseInterfaceException("Invalid Database Provided");
		
		return dataSource;
	}

	
	/**
	 * Centralizes all Exceptions caused by DatabaseInterface.
	 * 
	 * @author dddurand
	 *
	 */
	public class DatabaseInterfaceException extends Exception
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -3785824197570821055L;

		/**
		 * Empty Exception
		 */
		public DatabaseInterfaceException()
		{
			super();
		}
		
		/**
		 * New exception with a specific message
		 * @param message
		 */
		public DatabaseInterfaceException(String message)
		{
			super(message);
		}
		
		/**
		 * Generate an exception based on one that already exists, along with
		 * an additional message.
		 * 
		 * @param message
		 * @param e An existing exception.
		 */
		public DatabaseInterfaceException(String message, Exception e)
		{
			super(message, e);
		}
		
		/**
		 * Create a new exception from one that already exists.
		 * 
		 * @param e An existing exception.
		 */
		public DatabaseInterfaceException(Exception e)
		{
			super(e);
		}
	}
	
}

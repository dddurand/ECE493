package database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import util.ServletConfiguration;
import util.ServletConfiguration.Database;
import dataModels.Account;
import dataModels.Card;
import dataModels.Game;
import dataModels.GameAction;
import dataModels.GameAction.PokerAction;
import dataModels.MiscGameData;


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
				int id = resultSet.getInt("id");
            	String user = resultSet.getString("username");
            	String pass = resultSet.getString("password");	
            	String authToken = resultSet.getString("auth_token");	
            	
            	prepStat.close();
            	return new Account(id, user, pass, authToken, true);
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
	
	/**
	 * Adds a new account to the database
	 * 
	 * @param account
	 * @return
	 * @throws DatabaseInterfaceException
	 */
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
	
	/**
	 * Checks if a username is already in use by another account
	 * 
	 * @param username
	 * @return
	 * @throws DatabaseInterfaceException
	 */
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
	 * Saves the given game object into the database, for the provided account.
	 * 
	 * @param account Account to tie game data to.
	 * @param game Game data to be saved
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int saveGame(Account account, Game game) throws DatabaseInterfaceException
	{
		//if game id -1 = failed
		int gameID = this.saveGameGetID(account, game);
		
		if(gameID == -1)
			throw new DatabaseInterfaceException("Unable to store game");
		
		ArrayList<GameAction> actions = game.getGameActions();
		
		for(int i = 0; i < actions.size(); i++)
		{
			GameAction action = actions.get(i);
			this.saveGameAction(account, gameID, i, action);
		}
		
		return gameID;
	}
	
	/**
	 * Checks if a game action already exists
	 * 
	 * @param gameID Game id to tie action to
	 * @param accountID The account to tie the action to
	 * @param position The order in which the action occured.
	 * @return True if exists, false otherwise
	 * @throws DatabaseInterfaceException
	 */
	private boolean actionExists(int gameID, int accountID, int position) throws DatabaseInterfaceException
	{
		final String getActionCount = "SELECT count(*) FROM game_actions WHERE accountID = ? AND gameID = ? AND position = ?";
		
		try {
			CallableStatement prepStat = dbConnection.prepareCall(getActionCount);
			
			prepStat.setInt(1, accountID);
			prepStat.setInt(2, gameID);
			prepStat.setInt(3, position);
			
			ResultSet set = prepStat.executeQuery();
			
			if(set.next())
			{
				if(set.getInt(1) > 0)
				{
					prepStat.close();
					return true;
				}
				else
				{
					prepStat.close();
					return false;
				}
			}
			
			prepStat.close();
			return false;
			
			
		} catch (SQLException e) {
			throw new DatabaseInterfaceException("Error Storing Game Action", e);
		}
		
	}
	
	/**
	 * Saves a given game action to a certian game, at a given position, for a given account.
	 * 
	 * @param gameID Game id to tie action to
	 * @param accountID The account to tie the action to
	 * @param position The order in which the action occured.
	 *
	 * @throws DatabaseInterfaceException
	 */
	private void saveGameAction(Account account, int gameID, int position, GameAction action) throws DatabaseInterfaceException
	{
		int accountID = account.getAccountID();
		
		if(actionExists(gameID, accountID, position)) return;
		
		PokerAction actionType = action.getAction();
		String actionTypeAsString = actionType.getValue();
		
		ArrayList<Card> hand = action.getHand();
		String handAsString = Card.getCardsAsString(hand);
		
		ArrayList<Card> comm = action.getCommunityCards();
		String commAsString = Card.getCardsAsString(comm);
		
		int pot = action.getPot();
		
		String insertActionSQL = "INSERT INTO game_actions (accountID, gameID, type, position, hand, communityCards, pot)" +
									" VALUES (?, ?, ?, ?, ?, ?, ?);";
		
		try {
			CallableStatement prepStat = dbConnection.prepareCall(insertActionSQL);
			
			prepStat.setInt(1, accountID);
			prepStat.setInt(2, gameID);
			prepStat.setString(3, actionTypeAsString);
			prepStat.setInt(4, position);
			prepStat.setString(5, handAsString);
			prepStat.setString(6, commAsString);
			prepStat.setInt(7, pot);
			
			prepStat.execute();
			
			prepStat.close();
		} catch (SQLException e) {
			throw new DatabaseInterfaceException("Error Storing Game Action", e);
		}
		
	}
	
	/**
	 * Saves a given piece of misc data to an account.
	 * 
	 * @param account Account to tie the misc data to.
	 * @param misc The data to be saved.
	 * @throws DatabaseInterfaceException
	 */
	public void saveMiscData(Account account, MiscGameData misc) throws DatabaseInterfaceException
	{
		int accountID = account.getAccountID();
		String name = misc.getName();
		String value = misc.getValue();
		
		String insertMiscSQL = "INSERT INTO misc_data (accountID, name, value)" +
				" VALUES (?, ?, ?);";
		
		try {
			CallableStatement prepStat = dbConnection.prepareCall(insertMiscSQL);
			
			prepStat.setInt(1, accountID);
			prepStat.setString(2, name);
			prepStat.setString(3, value);
			
			prepStat.execute();
			
			prepStat.close();
		} catch (SQLException e) {
			throw new DatabaseInterfaceException("Error Storing Misc Data", e);
		}
		
	}
	
	/**
	 * Retrieves a game's specific database ID if it already exists.
	 * 
	 * @param game Game to get ID for.
	 * @return # > 0 if successful, -1 on failure.
	 * 
	 * @throws DatabaseInterfaceException
	 */
	private int getGameDatabaseID(Game game) throws DatabaseInterfaceException
	{
		if(game == null) return -1;
		
		final String getGameIdSQL = "SELECT id FROM "+ "games" +
				 " WHERE gameUUID = ?";
		
		try{
			CallableStatement prepStat = dbConnection.prepareCall(getGameIdSQL);
			prepStat.setString(1, game.getGameID());
			
			ResultSet set = prepStat.executeQuery();
			
			if(set.next())
				return set.getInt(1);
			else
				return -1;
			
			} catch (SQLException e) {
				throw new DatabaseInterfaceException("Error retrieving game ID", e);
			}
	}
	
	/**
	 * Saves a given game, for a given account, and returns the database id generated.
	 * 
	 * If the game is already saved, then no additional game is created.
	 * 
	 * @param account Account to tie game to.
	 * @param game The game to be saved.
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	private int saveGameGetID(Account account, Game game) throws DatabaseInterfaceException
	{
		if(game == null) return -1;
		
		String insertGameSQL = "INSERT INTO games (gameUUID) VALUES (?) ON DUPLICATE KEY UPDATE gameUUID=?;";
		String UUID = game.getGameID();
		
		try{
		CallableStatement prepStat = dbConnection.prepareCall(insertGameSQL);
		prepStat.setString(1, UUID);
		prepStat.setString(2, UUID);
		
		prepStat.execute();
		
		ResultSet set =  prepStat.getGeneratedKeys();
		
		int id = -1;
		if(set.next())
			id = set.getInt(1);
		//For the case when the game is already created
		else
			id = getGameDatabaseID(game);
		
		return id;
		
		} catch (SQLException e) {
			throw new DatabaseInterfaceException("Error storing game ID", e);
		}
		
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

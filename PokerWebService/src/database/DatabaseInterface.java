package database;

import java.io.UnsupportedEncodingException;
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

import util.PasswordUtil;
import util.ServletConfiguration;
import util.ServletConfiguration.Database;
import dataModels.Account;
import dataModels.Card;
import dataModels.Game;
import dataModels.GameAction;
import dataModels.GameAction.PokerAction;
import dataModels.MiscGameData;
import dataModels.TimeframeFilter;


/**
 * The main interface to the external database.
 * Each connection retrieves a unique connection to the database.
 * 
 * @author dddurand
 *
 */
public class DatabaseInterface {

	private final String USER_TABLE = "user_table";
	
	private final String GAME_ACTION_TABLE = "game_actions JOIN games ON game_actions.gameID = games.id ";
	

	
	private Connection dbConnection;
	private PasswordUtil passUtil;
	
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
			passUtil = new PasswordUtil();
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
            	byte[] pass = resultSet.getBytes("password");	
            	String authToken = resultSet.getString("auth_token");	
            	
            	String password = passUtil.getStringFromBytes(pass);
            	
            	prepStat.close();
            	return new Account(id, user, password, authToken, true);
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
			prepStat.setBytes(2, account.getEncyptedPassword().getBytes());

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
		int bet = action.getBet();
		
		String insertActionSQL = "INSERT INTO game_actions (accountID, gameID, type, position, hand, communityCards, pot, bet)" +
									" VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		
		try {
			CallableStatement prepStat = dbConnection.prepareCall(insertActionSQL);
			
			prepStat.setInt(1, accountID);
			prepStat.setInt(2, gameID);
			prepStat.setString(3, actionTypeAsString);
			prepStat.setInt(4, position);
			prepStat.setString(5, handAsString);
			prepStat.setString(6, commAsString);
			prepStat.setInt(7, pot);
			prepStat.setInt(8, bet);
			
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
		
		String insertMiscSQL = "INSERT INTO misc_data (accountID, name, value, date_uploaded)" +
				" VALUES (?, ?, ?, CURRENT_DATE());";
		
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
		
		String insertGameSQL = "INSERT INTO games (gameUUID, date_uploaded) VALUES (?,CURRENT_DATE()) ON DUPLICATE KEY UPDATE gameUUID=?;";
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
	 * Retrieves the ranking of a user based their delta money
	 * 
	 * @param account The account to get the ranking for.
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getUserDeltaMoneyRanking(Account account) throws DatabaseInterfaceException
	{
		String rank = "SELECT COUNT(*) FROM " + USER_TABLE + " u2 WHERE u2.delta_money > u1.delta_money";
		String sql = "SELECT (("+rank+") + 1) FROM " + USER_TABLE + " u1 WHERE id=?;";
		
		try
		{
			CallableStatement prepStat = dbConnection.prepareCall(sql);
			
			prepStat.setInt(1, account.getAccountID());
			
			ResultSet set = prepStat.executeQuery();
			
			if(set.next())
				return set.getInt(1);
			else
				throw new DatabaseInterfaceException("Error retrieving User Money ranking");

			} catch (SQLException e) {
				throw new DatabaseInterfaceException("Error retrieving User Money ranking", e);
			}
		
	}
	
	/**
	 * Updates the user's delta money. This storage is used to allow for more efficient ranking,
	 * since we don't need to determine the rank for all players, each time a call is made.
	 * 
	 * @param account The account to update the delta money for.
	 * @param deltaMoney The amount of delta money.
	 * @throws DatabaseInterfaceException
	 */
	public void updateUsersDeltaMoney(Account account, int deltaMoney, TimeframeFilter filter) throws DatabaseInterfaceException
	{
		final String updateDeltaMoneySQL = "UPDATE " + USER_TABLE+
				 " SET delta_money = ?" +	
				 " WHERE id = ?  "+filter.getSqlFilter()+" ;";
		
		try
		{
			CallableStatement prepStat = dbConnection.prepareCall(updateDeltaMoneySQL);
			
			prepStat.setInt(2, account.getAccountID());
			prepStat.setInt(1, deltaMoney);
			
			prepStat.execute();

			} catch (SQLException e) {
				throw new DatabaseInterfaceException("Error retrieving SUM statistic", e);
			}
		
	}
	
	/**
	 * Retrieves a specific parameter from the misc data. In this case this parameter
	 * is the amount of money that a user has generated.
	 * 
	 * @param account The account to retrieve money for.
	 * @return The amount of money generated by the given account.
	 * @throws DatabaseInterfaceException
	 */
	public int getMoneyGenerated(Account account, TimeframeFilter filter) throws DatabaseInterfaceException
	{
		String sql = "SELECT sum(value) from misc_data WHERE accountID = ? and name = ? "+ filter.getSqlFilter() +";";
	
	try
	{
		CallableStatement prepStat = dbConnection.prepareCall(sql);
		
		prepStat.setInt(1, account.getAccountID());
		prepStat.setString(2, "MoneyGenerated");
		
		ResultSet set = prepStat.executeQuery();
		int count = 0;
		
		if(set.next())
		{
			count = set.getInt(1);
		}
		
		return count;
		
		} catch (SQLException e) {
			throw new DatabaseInterfaceException("Error retrieving SUM statistic", e);
		}
	}

	/**
	 * Determines the number of games a provided user has played.
	 * 
	 * @param account
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getGamesPlayed(Account account, TimeframeFilter filter) throws DatabaseInterfaceException
	{
		String sql = "SELECT count(DISTINCT gameID) from " + GAME_ACTION_TABLE +
				 " WHERE accountID = ? "+filter.getSqlFilter()+" ;";
	
	try
	{
		CallableStatement prepStat = dbConnection.prepareCall(sql);
		
		prepStat.setInt(1, account.getAccountID());
		
		ResultSet set = prepStat.executeQuery();
		int count = 0;
		
		if(set.next())
		{
			count = set.getInt(1);
		}
		
		return count;
		
		} catch (SQLException e) {
			throw new DatabaseInterfaceException("Error retrieving SUM statistic", e);
		}
	}
	
	/**
	 * A generalized function that determines how many poker actions a user has made.
	 * 
	 * @param action The action to be counted.
	 * @param account The account to count the action for.
	 * @return The number of a specific poker action an given account has made.
	 * @throws DatabaseInterfaceException
	 */
	public int getGameActionsCount(PokerAction action, Account account, TimeframeFilter filter) throws DatabaseInterfaceException
	{
		String sql = "SELECT count(*) from " + GAME_ACTION_TABLE +
					 " WHERE accountID = ? AND type = ? "+filter.getSqlFilter()+" ;";
		
		try
		{
			CallableStatement prepStat = dbConnection.prepareCall(sql);
			
			prepStat.setInt(1, account.getAccountID());
			prepStat.setString(2, action.getValue());
			
			ResultSet set = prepStat.executeQuery();
			int count = 0;
			
			if(set.next())
			{
				count = set.getInt(1);
			}
			
			return count;
			
			} catch (SQLException e) {
				throw new DatabaseInterfaceException("Error retrieving SUM statistic", e);
			}
		
	}
	
	/**
	 * A generalized function that either sums, or averages a numerable column in the game_action table of the database.
	 * There is a filtering poker action that limits what values are determined. If null is provided for the PokerAction,
	 * all actions are considered. There is also a parameter that allows zero values to be ignored.
	 * 
	 * @param account The account to be considered.
	 * @param op The operation to be considered.
	 * @param column The column to run the operation on.
	 * @param action A filtering action.
	 * @param ignoreZeroValues Wheither to ignore zero values or not.
	 * @return The result of the operation.
	 * @throws DatabaseInterfaceException
	 */
	public int getGameActionsTypedNum(Account account, NumerableActionOperation op,
									NumerableActionColumn column, PokerAction action, boolean ignoreZeroValues, TimeframeFilter filter) throws DatabaseInterfaceException
	{
		String typeLimit = "";
		String ignoreZero = "";
		
		if(action != null)
			typeLimit = " type = ? and";
		
		if(ignoreZeroValues)
			ignoreZero = " and " + column.getValue() + "<> 0";
		
		String sql = "SELECT "+op.getValue()+"("+column.getValue()+") from " + GAME_ACTION_TABLE +
					 " WHERE "+typeLimit+" accountID = ? "+ignoreZero+" "+filter.getSqlFilter()+" ;";
		
		try
		{
			CallableStatement prepStat = dbConnection.prepareCall(sql);
			
			if(action != null)
			{
				prepStat.setString(1, action.getValue());
				prepStat.setInt(2, account.getAccountID());
			}
			else
				prepStat.setInt(1, account.getAccountID());
				
			
			ResultSet set = prepStat.executeQuery();
			int count = 0;
			
			if(set.next())
			{
				count = set.getInt(1);
			}
			
			return count;
			
			} catch (SQLException e) {
				throw new DatabaseInterfaceException("Error retrieving SUM statistic", e);
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
	
	public enum NumerableActionOperation
	{
		SUM("SUM"), AVG("AVG");
		
		private String value;
		
		NumerableActionOperation(String value)
		{
			this.value = value;
		}
		
		public String getValue()
		{
			return this.value;
		}
		
	}
	
	public enum NumerableActionColumn
	{
		POT("pot"), BET("bet");
		
		private String value;
		
		NumerableActionColumn(String value)
		{
			this.value = value;
		}
		
		public String getValue()
		{
			return this.value;
		}
		
	}
	
}

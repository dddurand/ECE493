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

import util.PasswordUtil;
import util.ServletConfiguration;
import util.ServletConfiguration.Database;
import dataModels.Account;
import dataModels.Card;
import dataModels.Filter;
import dataModels.Game;
import dataModels.GameAction;
import dataModels.Filter.RankType;
import dataModels.GameAction.PokerAction;
import dataModels.RankingStatistics.RankedDataRow;
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
	 * Retrieves all accounts
	 * 
	 * @return Account if found, otherwise null
	 * @throws DatabaseInterfaceException
	 */
	public ArrayList<Account> getAllAccounts() throws DatabaseInterfaceException
	{
		final String getAccountSQL = "SELECT * FROM "+ USER_TABLE;
		ArrayList<Account> accounts = new ArrayList<Account>();
		
		CallableStatement prepStat = null;
		try {
			prepStat = dbConnection.prepareCall(getAccountSQL);
			
			ResultSet resultSet = prepStat.executeQuery();
			
			while(resultSet.next())
            {
				int id = resultSet.getInt("id");
            	String user = resultSet.getString("username");
            	byte[] pass = resultSet.getBytes("password");	
            	String authToken = resultSet.getString("auth_token");	
            	
            	String password = passUtil.getStringFromBytes(pass);
            	
            	accounts.add(new Account(id, user, password, authToken, true));
            }
			
			prepStat.close();
			return accounts;
			
			
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
			byte[] password = passUtil.getBytesFromString(account.getEncyptedPassword());
			prepStat.setBytes(2, password);

			prepStat.execute();
			prepStat.close();
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
		int result = -1;
		if(game == null) return result;
		
		
		final String getGameIdSQL = "SELECT id FROM "+ "games" +
				 " WHERE gameUUID = ?";
		
		try{
			CallableStatement prepStat = dbConnection.prepareCall(getGameIdSQL);
			prepStat.setString(1, game.getGameID());
			
			ResultSet set = prepStat.executeQuery();
			
			if(set.next())
				result = set.getInt(1);
			else
				result = -1;
			
			prepStat.close();
			return result;
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
		
		prepStat.close();
		return id;
		
		} catch (SQLException e) {
			throw new DatabaseInterfaceException("Error storing game ID", e);
		}
		
	}
	
	/**
	 * Build the string SQL ranking query based on the provided user_rank_column, of either net_money or optimality,
	 * and the timeframe parameters in the filter.
	 * 
	 * @param user_rank_column
	 * @param filter
	 * @return
	 */
	private String getRankingQuery(String user_rank_column, Filter filter)
	{
		String gameCount = " select count(Distinct gameID) from game_actions where accountID = ";
		String columnName = user_rank_column+"_"+filter.getTimeFrame().getValue();
		
		//rank based on delta money, then if tied - number of more games
		String rank = " ( SELECT COUNT(Distinct u2.id) FROM " + USER_TABLE + " u2 LEFT JOIN game_actions ON u2.id = game_actions.accountID" +
				//More delta money
				//No games played, if u2 was registered first he is above
				" WHERE ( ("+gameCount+"u1.id) = 0 and ("+gameCount+"u2.id) = 0 and (u1.id > u2.id) ) " +
				//U2 has games, while u1 doesn't - therefore hes above
				" or ( ("+gameCount+"u1.id) = 0 and ("+gameCount+"u2.id) > 0) " +
				//Otherwise both have games. So we vote based on columns
				" or (( ("+gameCount+"u1.id) > 0 and ("+gameCount+"u2.id) > 0) and " +
						" (u2."+columnName+" > u1."+columnName+") " +
						//columns equal - if u2 has more games he's above
						"or ((u2."+columnName+" = u1."+columnName+") and ("+gameCount+"u1.id) < ("+gameCount+"u2.id))" +
						//otherwise if u2 is an older player
						"or ((u2."+columnName+" = u1."+columnName+") and ("+gameCount+"u1.id) = ("+gameCount+"u2.id) and u1.id > u2.id))" +
						") + 1";

		return rank;
	}
	
	/**
	 * Generates a ranked list of users based on the provided filter.
	 * The filter contains the number of elements to skip, the max elements to return, the ranking type to use,
	 * and the timeframe to be used.
	 * 
	 * @param filter The filter provides the limiting parameters for the query.
	 * @return The list of RankedDataRow representing the ranked users list.
	 * @throws DatabaseInterfaceException
	 */
	public ArrayList<RankedDataRow> getRankedUsers(Filter filter) throws DatabaseInterfaceException
	{
		ArrayList<RankedDataRow> rankedData = new ArrayList<RankedDataRow>();
		
		String order = getRankingQuery(filter.getRankType().getValue(), filter);
		String columnName = filter.getRankType().getValue()+"_"+filter.getTimeFrame().getValue();
		
		
		String rankSql = "SELECT u1.username, u1."+columnName+ " FROM "+ USER_TABLE+
						  " u1 ORDER BY " + order + 
						  " "+filter.getMaxSqlFilter() + filter.getSkipSqlFilter();
		
		try
		{
			CallableStatement prepStat = dbConnection.prepareCall(rankSql);
			
			
			ResultSet set = prepStat.executeQuery();
				
			int position = filter.getSkip() + 1;
			while(set.next())
			{
				String username = set.getString("username");
				double value = set.getDouble(columnName);
				
				RankedDataRow temp = new RankedDataRow(position, username, value);
				
				rankedData.add(temp);
				
				position++;
			}
			
			prepStat.close();

			} catch (SQLException e) {
				throw new DatabaseInterfaceException("Error building rank statistics", e);
			}
		
		return rankedData;
	}
	
	
	
	/**
	 * Retrieves the ranking of a user based their NetMoney
	 * 
	 * @param account The account to get the ranking for.
	 * @param filter The filter object that contains the limiting parameters for the query.
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getUserNetMoneyRanking(Account account, Filter filter) throws DatabaseInterfaceException
	{

		String rank = getRankingQuery(RankType.NET_MONEY.getValue(), filter);
		String sql = "SELECT ("+rank+") FROM " + USER_TABLE + " u1 WHERE id="+account.getAccountID()+";";
		try
		{
			CallableStatement prepStat = dbConnection.prepareCall(sql);
			
			ResultSet set = prepStat.executeQuery();
			
			if(set.next())
				{
				int value = set.getInt(1);
				prepStat.close();
				return value;
				}
				
			else
				{
				prepStat.close();
				throw new DatabaseInterfaceException("Error retrieving User Money ranking");
				}
				

			} catch (SQLException e) {
				throw new DatabaseInterfaceException("Error retrieving User Money ranking", e);
			}
		
	}
	
	/**
	 * Retrieves the ranking of a user based their optimality
	 * 
	 * @param account The account to get the ranking for.
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public double getUserOptimalityRanking(Account account, Filter filter) throws DatabaseInterfaceException
	{
		String rank = getRankingQuery(RankType.OPTIMALITY.getValue(), filter);
		String sql = "SELECT ("+rank+") FROM " + USER_TABLE + " u1 WHERE id="+account.getAccountID()+";";
		try
		{
			CallableStatement prepStat = dbConnection.prepareCall(sql);

			ResultSet set = prepStat.executeQuery();
			
			if(set.next())
				{
				double value = set.getDouble(1);
				prepStat.close();
				return value;
				}
				
			else
				{
				prepStat.close();
				throw new DatabaseInterfaceException("Error retrieving User Money ranking");
				}
				

			} catch (SQLException e) {
				throw new DatabaseInterfaceException("Error retrieving User Money ranking", e);
			}
		
	}
	
	/**
	 * Updates the user's net money. This storage is used to allow for more efficient ranking,
	 * since we don't need to determine the rank for all players, each time a call is made.
	 * 
	 * @param account The account to update the delta money for.
	 * @param NetMoney The amount of delta money.
	 * @throws DatabaseInterfaceException
	 */
	public void updateUsersNetMoney(Account account, int netMoney, Filter filter) throws DatabaseInterfaceException
	{
		String columnName = "net_money_"+filter.getTimeFrame().getValue();
		final String updateNetMoneySQL = "UPDATE " + USER_TABLE+
				 " SET "+columnName+" = ?" +	
				 " WHERE id = ?;";
		
		try
		{
			CallableStatement prepStat = dbConnection.prepareCall(updateNetMoneySQL);
			
			prepStat.setInt(2, account.getAccountID());
			prepStat.setInt(1, netMoney);
			
			prepStat.execute();
			prepStat.close();
			
			} catch (SQLException e) {
				throw new DatabaseInterfaceException("Error retrieving SUM statistic", e);
			}
		
	}
	
	public void updateRankCacheDate(Account account) throws DatabaseInterfaceException
	{
			final String setAuthSQL = "UPDATE " + USER_TABLE+
					 					 " SET rank_cache_data = CURDATE()" +	
										 " WHERE username = ?";
			
			try {
				CallableStatement prepStat = dbConnection.prepareCall(setAuthSQL);
				
				prepStat.setString(1, account.getUsername());
				
				prepStat.execute();
				
				prepStat.close();
			} catch (SQLException e) {
				throw new DatabaseInterfaceException("Error Storing Auth Token", e);
			}

	}
	
	public boolean isRankCacheCurrent(Account account) throws DatabaseInterfaceException
	{
			boolean isCurrent = false;
			final String setAuthSQL = "SELECT count(id) FROM " + USER_TABLE+
					 					 " where rank_cache_data = CURDATE()" +	
										 " and username = ?";
			
			try {
				CallableStatement prepStat = dbConnection.prepareCall(setAuthSQL);
				
				prepStat.setString(1, account.getUsername());
				
				ResultSet result = prepStat.executeQuery();
				
				if(result.next())
				{
					isCurrent = result.getBoolean(1);
				}
				
				prepStat.close();
	
			} catch (SQLException e) {
				throw new DatabaseInterfaceException("Error Storing Auth Token", e);
			}
			
			return isCurrent;

	}
	
	/**
	 * Updates the optimality value. This storage is used to allow for more efficient ranking,
	 * since we don't need to determine the rank for all players, each time a call is made.
	 * 
	 * @param account The account to update the delta money for.
	 * @param optimality The determined optimality number.
	 * @throws DatabaseInterfaceException
	 */
	public void updateUsersOptimality(Account account, double optimality, Filter filter) throws DatabaseInterfaceException
	{
		String columnName = "optimality_"+filter.getTimeFrame().getValue();
		final String updateNetMoneySQL = "UPDATE " + USER_TABLE+
				 " SET "+columnName+" = ?" +	
				 " WHERE id = ?;";
		
		try
		{
			CallableStatement prepStat = dbConnection.prepareCall(updateNetMoneySQL);
			
			prepStat.setInt(2, account.getAccountID());
			prepStat.setDouble(1, optimality);
			
			prepStat.execute();
			prepStat.close();
			
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
	public int getMoneyGenerated(Account account, Filter filter) throws DatabaseInterfaceException
	{
		String accountFiltering = "";
		
		if(account!=null)
			accountFiltering = " and accountID = " + account.getAccountID();
		
		String sql = "SELECT sum(value) from misc_data WHERE TRUE "+accountFiltering+" and name = ? "+ filter.getSqlTimeFrameFilter() +";";
	
	try
	{
		CallableStatement prepStat = dbConnection.prepareCall(sql);
		
		prepStat.setString(1, "MoneyGenerated");
		
		ResultSet set = prepStat.executeQuery();
		int count = 0;
		
		if(set.next())
		{
			count = set.getInt(1);
		}
		
		prepStat.close();
		return count;
		
		} catch (SQLException e) {
			throw new DatabaseInterfaceException("Error retrieving SUM statistic", e);
		}
	}

	/**
	 * Determines the number of games a provided user has played.
	 * 
	 * @param account The account to get the number of games played for.
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public int getGamesPlayed(Account account, Filter filter) throws DatabaseInterfaceException
	{
		String accountFiltering = "";
		
		if(account!=null)
			accountFiltering = " and accountID = " + account.getAccountID();
		
		String sql = "SELECT count(DISTINCT gameID) from " + GAME_ACTION_TABLE +
				 " WHERE TRUE "+accountFiltering+" "+filter.getSqlTimeFrameFilter()+" ;";
	
	try
	{
		CallableStatement prepStat = dbConnection.prepareCall(sql);
		
		ResultSet set = prepStat.executeQuery();
		int count = 0;
		
		if(set.next())
		{
			count = set.getInt(1);
		}
		
		prepStat.close();
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
	public int getGameActionsCount(PokerAction action, Account account, Filter filter) throws DatabaseInterfaceException
	{
		String accountFiltering = "";
		
		if(account!=null)
			accountFiltering = "and accountID = " + account.getAccountID();
		
		String sql = "SELECT count(*) from " + GAME_ACTION_TABLE +
					 " WHERE TRUE "+accountFiltering+" AND type = ? "+filter.getSqlTimeFrameFilter()+" ;";
		
		try
		{
			CallableStatement prepStat = dbConnection.prepareCall(sql);
			
			prepStat.setString(1, action.getValue());
			
			ResultSet set = prepStat.executeQuery();
			int count = 0;
			
			if(set.next())
			{
				count = set.getInt(1);
			}
			
			prepStat.close();
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
									NumerableActionColumn column, PokerAction action, boolean ignoreZeroValues, Filter filter) throws DatabaseInterfaceException
	{
		String typeLimit = "";
		String ignoreZero = "";
		String accountFiltering = "";
		
		if(action != null)
			typeLimit = " and type = '"+ action.getValue()+"' ";
		
		if(account !=null)
			accountFiltering = " and accountID = "+account.getAccountID()+" ";
		
		if(ignoreZeroValues)
			ignoreZero = " and " + column.getValue() + "<> 0";
		
		
		
		String sql = "SELECT "+op.getValue()+"("+column.getValue()+") from " + GAME_ACTION_TABLE +
					 " WHERE TRUE "+typeLimit+" "+accountFiltering+" "+ignoreZero+" "+filter.getSqlTimeFrameFilter()+" ;";
		
		try
		{
			CallableStatement prepStat = dbConnection.prepareCall(sql);
			
			ResultSet set = prepStat.executeQuery();
			int count = 0;
			
			if(set.next())
			{
				count = set.getInt(1);
			}
			
			prepStat.close();
			return count;
			
			} catch (SQLException e) {
				throw new DatabaseInterfaceException("Error retrieving SUM statistic", e);
			}
	}
	
	/**
	 * Adds a entry for game optimality
	 * 
	 * @param account
	 * @return
	 * @throws DatabaseInterfaceException
	 */
	public boolean setGameOptimalityForUser(Account account, Game game, double optimality) throws DatabaseInterfaceException
	{
		String insertAccountSQL = "INSERT INTO game_optimality (game_id, account_id, optimality) VALUES (?,?,?);";

		int gameId = getGameDatabaseID(game);
		
		try {
			CallableStatement prepStat = dbConnection.prepareCall(insertAccountSQL);

			prepStat.setInt(1, gameId);
			prepStat.setInt(2, account.getAccountID());
			prepStat.setDouble(3, optimality);

			prepStat.execute();
			prepStat.close();
		} catch (SQLException e) {
			throw new DatabaseInterfaceException("Unable to store optimality value for game.", e);
		}

		return false;

	}
	
	public void close()
	{
		try {
			this.dbConnection.close();
		} catch (SQLException e) { }
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
	
	/**
	 * All the possible numberable operations for querying
	 * 
	 * @author dddurand
	 *
	 */
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
	
	/**
	 * All possible columns that NumerableActionsOperations
	 * can act upon.
	 * 
	 * @author dddurand
	 *
	 */
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

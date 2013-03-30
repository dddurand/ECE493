package mockObjects;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.NamingException;
import javax.sql.DataSource;

import dataModels.Account;
import dataModels.Filter;
import dataModels.Filter.TimeFrame;
import dataModels.Game;
import dataModels.GameAction;
import dataModels.GameAction.PokerAction;
import dataModels.MiscGameData;
import dataModels.RankingStatistics.RankedDataRow;
import database.DatabaseInterface;

public class MockDbInterface extends DatabaseInterface {

		private Hashtable<String, Account> accounts;
		private Hashtable<String, Game> games;
		private Hashtable<String, GameAction> gameActions;
		private Hashtable<String, MiscGameData> miscData;
		
		private Hashtable<String, Number> optimality;
		private Hashtable<String, Number> netValue;
		
		private boolean throwException = false;
		private boolean isCacheUpdated = false;
		
		public MockDbInterface(boolean throwException) throws DatabaseInterfaceException
		{
			super(false);
			
			this.throwException = throwException;
			
			accounts = new Hashtable<String, Account>();
			games = new Hashtable<String, Game>();
			gameActions = new Hashtable<String, GameAction>();
			miscData = new Hashtable<String, MiscGameData>();
			
			optimality = new Hashtable<String, Number>();
			netValue = new Hashtable<String, Number>();
			
			accounts.put("bob", new Account(0, "bob", "pass", "12345", false));
			accounts.put("fred", new Account(1, "fred", "pass1", "123451", false));
			accounts.put("george", new Account(2, "george", "pass2", "123452", false));
			
		}
		
		
		
		public boolean isCacheUpdated() {
			return isCacheUpdated;
		}



		public void setCacheUpdated(boolean isCacheUpdated) {
			this.isCacheUpdated = isCacheUpdated;
		}



		public Account getAccount(String username) throws DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();
			
			Account account = accounts.get(username);
			return account;
		}
		
		public ArrayList<Account> getAllAccounts() throws DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();
			
			ArrayList<Account> accounts = new ArrayList<Account>(this.accounts.values());
			return accounts;
			
		}
		
		public void invalidateAuth(String username) throws DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();
			Account account = accounts.get(username);
			
			if(account == null) return;
			
			account.setAuthenticationToken(null);

		}

		public void setAuth(String username, String authToken) throws DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();
			
			Account account = accounts.get(username);
			
			if(account == null) return;
			
			account.setAuthenticationToken(authToken);

		}

		public boolean addAccount(Account account) throws DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();

			accounts.put(account.getUsername(), account);
			
			return true;
		}
		

		public boolean isDuplicateUsername(String username) throws DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();
			
			if(accounts.get(username)!=null)
				return true;
			else
				return false;
			
		}

	
		/*
		 * @TODO
		 */
		protected DataSource getDataSource() throws NamingException, DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();
			return null;
		}

		public int saveGame(Account account, Game game) throws DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();		
			
			Game game2 = games.put(account.getUsername(), game);
			
			if(game2 == null)
				return 1;
			else 
				return -1;

	
		}

		private boolean actionExists(int gameID, int accountID, int position) throws DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();
			
			if(gameID == 1) 
				return true;
			else 
				return false;
			
		}
		
		private boolean optimalityExists(int gameID, int accountID) throws DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();
			if(gameID == 1)
				return true;
			else
				return false;
			
		}
		
		private void saveGameAction(Account account, int gameID, int position, GameAction action) throws DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();
			
			GameAction action2 = this.gameActions.put(account.getUsername(), action);
			
			return;
		}
		
		public void saveMiscData(Account account, MiscGameData misc) throws DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();
			
			MiscGameData data = this.miscData.put(account.getUsername(), misc);
			
		}
		
		private int getGameDatabaseID(Game game) throws DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();
			
			if(this.games.contains(game))
				return 1;
			else
				return -1;
			
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
			if(this.throwException) throw new DatabaseInterfaceException();
			
			Game game2 = games.put(account.getUsername(), game);
			
			if(game2 == null)
				return 1;
			else 
				return -1;
			
		}
		
		public ArrayList<RankedDataRow> getRankedUsers(Filter filter) throws DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();
			
			ArrayList<RankedDataRow> ranked = new ArrayList<RankedDataRow>();
			
			int i =1;
			for(String username : this.accounts.keySet())
			{
				
				ranked.add(new RankedDataRow(1, username, i));
				i++;
			}
			
			return ranked;
			
			
		}
		
		
		

		public int getUserNetMoneyRanking(Account account, Filter filter) throws DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();
			
			Number number = this.netValue.get(account.getUsername());
			
			if(number == null) return 0;
			
			return number.intValue();
			
		}
		
		
		
		/**
		 * Retrieves the ranking of a user based their optimality of gameplay
		 * 
		 * @param account The account to get the ranking for.
		 * @return
		 * @throws DatabaseInterfaceException
		 */
		public int getUserOptimalityRanking(Account account, Filter filter) throws DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();
			
			Number number = this.optimality.get(account.getUsername());
			
			if(number == null) return 0;
			
			return number.intValue();
			
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
			if(this.throwException) throw new DatabaseInterfaceException();
			
			this.optimality.put(account.getUsername(), netMoney);
			
		}
		
		public void updateRankCacheDate(Account account) throws DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();	
			this.isCacheUpdated = true;
			return;
			
		}
		
		public boolean isRankCacheCurrent(Account account) throws DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();

			if(account.getUsername().equals("FALSE"))
				return false;
			else 
				return true;
			
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
			if(this.throwException) throw new DatabaseInterfaceException();
			
			this.optimality.put(account.getUsername(), optimality);
			
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
			if(this.throwException) throw new DatabaseInterfaceException();
			
			if(account == null) return 20;
			
			MiscGameData data = this.miscData.get(account.getUsername());
			
			
			if(data == null) return 5;
			
			return Integer.valueOf(data.getValue());
			
			
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
			if(this.throwException) throw new DatabaseInterfaceException();
			
			return 5;
			
		}
		

		public int getGameActionsCount(PokerAction action, Account account, Filter filter) throws DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();
			
			if(filter.getTimeFrame() == TimeFrame.MONTH)
			{
				return 0;
			}
			
			return 60;
			
		}
		
	
		public int getGameActionsTypedNum(Account account, NumerableActionOperation op,
										NumerableActionColumn column, PokerAction action, boolean ignoreZeroValues, Filter filter) throws DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();
			
			return 20;
			
		}
		

		public boolean setGameOptimalityForUser(Account account, Game game, double optimality) throws DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();

			this.optimality.put(account.getUsername(), optimality);
			
			return true;
			
		}
		
		/**
		 * Retrieves an optimality rating for the user, based on a given timeframe in the filter
		 * 
		 * @param account
		 * @return
		 * @throws DatabaseInterfaceException
		 */
		public double getUserOptimality(Account account, Filter filter) throws DatabaseInterfaceException
		{
			if(this.throwException) throw new DatabaseInterfaceException();
			
			Number number = this.optimality.get(account.getUsername());

			if(number == null) return 0;
			
			return number.doubleValue();
			
		}
		
		public void close()
		{
			return;
		}
		

}

package database;

import game.Card;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import server.GameAction.PokerAction;

import dataModels.Account;
import dataModels.GameActionJson;
import dataModels.GameJson;
import dataModels.MoneyGenerated;
import database.DatabaseContract.DatabaseDBHelper;
import database.DatabaseContract.RetrieveDatabaseAsync;
import database.DatabaseContract.RetrieveDatabaseAsyncCallBack;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * This file acts as the interface between the sqlite database and the android application.
 * 
 * @author dddurand
 *
 */
public class DatabaseDataSource {

	  // Database fields
	  private SQLiteDatabase db;
	  private DatabaseDBHelper dbHelper;
	  private Dialog loadingDialog;

	  /**
	   * Acts as the interface to the sqlite database.
	   * Since the opening of the database requires a delay, a dialog must be provided.
	   * This "loading dialog" will be shown while loading, and then dismissed when completed.
	   * 
	   * @param context Context the database is called from.
	   * @param loadingDialog
	   */
	  public DatabaseDataSource(Context context, Dialog loadingDialog) {
	    dbHelper = new DatabaseDBHelper(context);
	    this.loadingDialog = loadingDialog;
	  }

	  /**
	   * THe function that opens the database, and loads it into the PokerApplication object
	   * to be used by all activities.
	   * @throws SQLException
	   */
	  public void open() throws SQLException {
		  LoginDatabaseAsyncCallBack callback = new LoginDatabaseAsyncCallBack();
		  RetrieveDatabaseAsync dbOpenTask = new RetrieveDatabaseAsync(callback);
		 
		  loadingDialog.show();
		  try {
			dbOpenTask.execute(dbHelper).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  
	  }

	  /**
	   * Closes the connection to the database.
	   */
	  public void close() {
	    dbHelper.close();
	  }
	  
	  /**
	   * Updates an account to the database.
	   * If the account doesn't exist - it is added.
	   * Only the username, balance, and auth token are stored.
	   * 
	   * @param account
	   */
	  public void updateAccount(Account account)
	  {
		  if(!accountExists(account)) addAccount(account);
		  
		  ContentValues values = new ContentValues();
		  values.put(DatabaseContract.AccountContract.COLUMN_NAME_USERNAME, account.getUsername());
		  values.put(DatabaseContract.AccountContract.COLUMN_NAME_BALANCE, account.getBalance());
		  values.put(DatabaseContract.AccountContract.COLUMN_NAME_AUTH_TOKEN, account.getAuthenticationToken());
		  
		  String where = DatabaseContract.AccountContract.COLUMN_NAME_USERNAME + " = ? ";
		  
		  db.update(DatabaseContract.AccountContract.TABLE_NAME, 
				  values, 
				  where, 
				  new String[] {account.getUsername()});
	  }
	  
	  /**
	   * Adds the account to the database.
	   * 
	   * @param account
	   */
	  private void addAccount(Account account)
	  {
		  if(accountExists(account)) return;
		  
		  ContentValues values = new ContentValues();
		  values.put(DatabaseContract.AccountContract.COLUMN_NAME_USERNAME, account.getUsername());
		  values.put(DatabaseContract.AccountContract.COLUMN_NAME_BALANCE, account.getBalance());
		  values.put(DatabaseContract.AccountContract.COLUMN_NAME_AUTH_TOKEN, account.getAuthenticationToken());
		  
		  db.insert(DatabaseContract.AccountContract.TABLE_NAME,
				  null,
				  values);
		  
	  }
	  
	  /**
	   * Checks if a given account already exists in the database
	   * 
	   * @param account
	   * @return
	   */
	  public boolean accountExists(Account account)
	  {
		  if(this.getAccount(account.getUsername())!= null)
			  return true;
		  else
			  return false;
	  }
	  
	  /**
	   * Retrieves the provided account from the database by the username
	   * 
	   * @param username
	   * @return
	   */
	  public Account getAccount(String username)
	  {
		  Account account = null;
		  
		  String columns[] = new String[] {DatabaseContract.AccountContract.COLUMN_NAME_USERNAME,
				  							DatabaseContract.AccountContract.COLUMN_NAME_BALANCE,
				  							DatabaseContract.AccountContract.COLUMN_NAME_AUTH_TOKEN};
		  
		  String where = DatabaseContract.AccountContract.COLUMN_NAME_USERNAME + " = ? ";
		  
		  Cursor cursor = db.query(DatabaseContract.AccountContract.TABLE_NAME,
				  columns,
				  where,
				  new String[] {username},
				  null, null, null);
		
		  if(cursor.getCount() > 0)
		  {
			  cursor.moveToFirst();
			  
			  int userCol = cursor.getColumnIndex(DatabaseContract.AccountContract.COLUMN_NAME_USERNAME);
			  int balCol = cursor.getColumnIndex(DatabaseContract.AccountContract.COLUMN_NAME_BALANCE);
			 int authCol = cursor.getColumnIndex(DatabaseContract.AccountContract.COLUMN_NAME_AUTH_TOKEN);
			  
			 String usernameString = cursor.getString(userCol);
			 String authTokenString = cursor.getString(authCol);
			 int balance = cursor.getInt(balCol);
			 
			 account = new Account(usernameString, authTokenString, balance); 
		  }
		  
		  cursor.close();
		  return account;
	  }
	  
	  /**
	   * Retrieves the DATABASE id for the given account
	   * 
	   * @param account
	   * @return
	   */
	  public int getAccountID(Account account)
	  {
		  int id = -1;
		  String columns[] = new String[] {DatabaseContract.AccountContract._ID};
		  
		  String where = DatabaseContract.AccountContract.COLUMN_NAME_USERNAME + " = ? ";
		  
		  Cursor cursor = db.query(DatabaseContract.AccountContract.TABLE_NAME,
				  columns,
				  where,
				  new String[] {account.getUsername()},
				  null, null, null);
		
		  if(cursor.getCount() > 0)
		  {
			  cursor.moveToFirst();
			  
			  int idCol = cursor.getColumnIndex(DatabaseContract.AccountContract._ID);
			  id = cursor.getInt(idCol);
		  }
		  
		  cursor.close();
		  return id;
	  }
	  
	  /**
	   * Add generated money into database
	   * 
	   * @param moneyGen
	   */
	  public void addMoneyGenerated(MoneyGenerated moneyGen)
	  {
		  
		  int accountID = this.getAccountID(moneyGen.getAccount());
		  
		  ContentValues values = new ContentValues();
		  values.put(DatabaseContract.MiscContract.COLUMN_NAME_ACCOUNTID, accountID);
		  values.put(DatabaseContract.MiscContract.COLUMN_NAME_NAME, moneyGen.getName());
		  values.put(DatabaseContract.MiscContract.COLUMN_NAME_VALUE, moneyGen.getValue());
		  
		  db.insert(DatabaseContract.MiscContract.TABLE_NAME,
				  null,
				  values);
	  }
	  
	  /**
	   * Gets all money generations for an account currently in the database
	   * 
	   * @param account
	   * @return
	   */
	  public ArrayList<MoneyGenerated> getMoneyGenerates(Account account)
	  {
		  ArrayList<MoneyGenerated> list = new ArrayList<MoneyGenerated>();
		  
		  
		  String columns[] = new String[] {DatabaseContract.MiscContract.COLUMN_NAME_ACCOUNTID,
				  							DatabaseContract.MiscContract.COLUMN_NAME_NAME,
				  							DatabaseContract.MiscContract.COLUMN_NAME_VALUE,
				  							DatabaseContract.MiscContract._ID};
		  
		  int accountID = getAccountID(account);
		  
		  String where = DatabaseContract.MiscContract.COLUMN_NAME_ACCOUNTID + " = " + accountID;
		  
		  Cursor cursor = db.query(DatabaseContract.MiscContract.TABLE_NAME,
				  columns,
				  where,
				  null,
				  null, null, null);
		
		  cursor.moveToFirst();
		  while(!cursor.isAfterLast())
		  {
			 int valueCol = cursor.getColumnIndex(DatabaseContract.MiscContract.COLUMN_NAME_VALUE);
			 int idCol = cursor.getColumnIndex(DatabaseContract.MiscContract._ID);
			  
			 int value = cursor.getInt(valueCol);
			 int id = cursor.getInt(idCol);
			 
			 MoneyGenerated moneyGen = new MoneyGenerated(value, id);
			 list.add(moneyGen);
			 cursor.moveToNext();
		  }
		  
		  
		  cursor.close();
		  return list;
	  }
	  
	  /**
	   * Remove a money generation based on an id
	   * 
	   * @param generateID
	   */
	  public void removeMoneyGenerate(int generateID)
	  {
		  String where = DatabaseContract.MiscContract._ID + " = " + generateID;
		  
		  db.delete(DatabaseContract.MiscContract.TABLE_NAME,
				  where,
				  null);
	  }
	  
	  /**
	   * Adds a game to the database if it doesn't already exist
	   */
	  public int addGame(String UUID, Account account)
	  {
		  if(gameExists(UUID, account)) return this.getGameID(UUID, account);
		  
		  int accountID = this.getAccountID(account);
		  
		  ContentValues values = new ContentValues();
		  values.put(DatabaseContract.GameContract.COLUMN_NAME_UUID, UUID);
		  values.put(DatabaseContract.GameContract.COLUMN_NAME_ACCOUNT_ID, accountID);
		  
		  long id = db.insert(DatabaseContract.GameContract.TABLE_NAME,
				  null,
				  values);
		  
		  if(id == -1)
			  id = this.getGameID(UUID, account);
		  
		  return (int)id;
	  }
	  
	  /**
	   * Retrieves a games database id based on the unique uuid, and account provided.
	   * Returns -1 on non-existent.
	   * 
	   * @param UUID
	   * @param account
	   * @return
	   */
	  public int getGameID(String UUID, Account account)
	  {
		  String columns[] = new String[] {DatabaseContract.GameContract.COLUMN_NAME_UUID,
				  							DatabaseContract.GameContract._ID};

		  int accountID = this.getAccountID(account);
		  
		  String where = DatabaseContract.GameContract.COLUMN_NAME_UUID + " =  ? AND "
				  + DatabaseContract.GameContract.COLUMN_NAME_ACCOUNT_ID + " = " + accountID ;
		  
		  
		Cursor cursor =  db.query(DatabaseContract.GameContract.TABLE_NAME, 
				 columns, 
				 where, 
				 new String[] {UUID},
				 null, null, null);
		
		cursor.moveToFirst();
		if(!cursor.isAfterLast())
		{
			int idCol = cursor.getColumnIndex(DatabaseContract.GameContract._ID);
			int gameID = cursor.getInt(idCol);
			
			cursor.close();
			return gameID;
		}
		
		cursor.close();
		  return -1;
	  }
	  
	  /**
	   * Returns true if the game already exists in the database,
	   * otherwise return false
	   * 
	   * @return
	   */
	  public boolean gameExists(String uuid, Account account)
	  {
		  if(this.getGame(uuid, account)!=null)
			  return true;
		  else
			  return false;
	  }
	  
	  /**
	   * Retrieves a Set of game data from the database
	   * 
	   * @return
	   */
	  public GameJson getGame(String uuid, Account account)
	  {
		  GameJson game = null;
		  String columns[] = new String[] {DatabaseContract.GameContract.COLUMN_NAME_UUID,
				  							DatabaseContract.GameContract._ID};

		  int accountID = this.getAccountID(account);
		  
		  String where = DatabaseContract.GameContract.COLUMN_NAME_UUID + " =  ? AND "
				  + DatabaseContract.GameContract.COLUMN_NAME_ACCOUNT_ID + " = " + accountID ;
		  
		  
		Cursor cursor =  db.query(DatabaseContract.GameContract.TABLE_NAME, 
				 columns, 
				 where, 
				 new String[] {uuid},
				 null, null, null);
		
		cursor.moveToFirst();
		if(!cursor.isAfterLast())
		{
			int idCol = cursor.getColumnIndex(DatabaseContract.GameContract._ID);
			int gameID = cursor.getInt(idCol);
			
			ArrayList<GameActionJson> actions = this.getGameActions(gameID);
			game = new GameJson(actions, uuid);
		}
		
		cursor.close();
		  return game;
	  }
	  
	  /**
	   * Retrieves a Set of games data from the database
	   * 
	   * @return
	   */
	  public ArrayList<GameJson> getGames(Account account)
	  {
		  ArrayList<GameJson> games = new ArrayList<GameJson>();
		  String columns[] = new String[] {DatabaseContract.GameContract.COLUMN_NAME_UUID,
				  							DatabaseContract.GameContract._ID};

		  int accountID = this.getAccountID(account);
		  
		  String where = DatabaseContract.GameContract.COLUMN_NAME_ACCOUNT_ID + " = " + accountID ;
		  
		  
		Cursor cursor =  db.query(DatabaseContract.GameContract.TABLE_NAME, 
				 columns, 
				 where, 
				 null,
				 null, null, null);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			int idCol = cursor.getColumnIndex(DatabaseContract.GameContract._ID);
			int uuidCol = cursor.getColumnIndex(DatabaseContract.GameContract.COLUMN_NAME_UUID);
			
			int gameID = cursor.getInt(idCol);
			String uuid = cursor.getString(uuidCol);
			
			ArrayList<GameActionJson> actions = this.getGameActions(gameID);
			GameJson game = new GameJson(actions, uuid);
			games.add(game);
			cursor.moveToNext();
		}
		
		cursor.close();
		  return games;
	  }
	  
	  /**
	   * Removes game from the database with the provided UUID
	   * @param uuid
	   */
	  public void removeGame(String uuid, Account account)
	  {
		  int gameID = this.getGameID(uuid, account);
		  int accountID = this.getAccountID(account);
		  
		  String where = DatabaseContract.GameContract.COLUMN_NAME_UUID + " =  ? AND "
				  + DatabaseContract.GameContract.COLUMN_NAME_ACCOUNT_ID + " = " + accountID ;
		  
		  db.delete(DatabaseContract.GameContract.TABLE_NAME,
				  where,
				  new String[] {uuid});
		  
		  this.removeGameActions(gameID);
		  
	  }
	  
	  /**
	   * Gets the list of all games within the database for the provided user.
	   * 
	   * @param gameID
	   * @return
	   */
	  public ArrayList<GameActionJson> getGameActions(int gameID)
	  {
		  ArrayList<GameActionJson> actions = new ArrayList<GameActionJson>();
		  
		  String columns[] = new String[] {DatabaseContract.GameActionContract._ID,
				  							DatabaseContract.GameActionContract.COLUMN_NAME_ACTION,
				  							DatabaseContract.GameActionContract.COLUMN_NAME_BET,
				  							DatabaseContract.GameActionContract.COLUMN_NAME_COMM,
				  							DatabaseContract.GameActionContract.COLUMN_NAME_HAND,
				  							DatabaseContract.GameActionContract.COLUMN_NAME_POT
		  };

		  
		  String where = DatabaseContract.GameActionContract.COLUMN_NAME_GAME_ID + " = " + gameID;
		  
		  
		Cursor cursor =  db.query(DatabaseContract.GameActionContract.TABLE_NAME, 
				 columns, 
				 where, 
				 null,
				 null, 
				 null, 
				 DatabaseContract.GameActionContract.COLUMN_NAME_UPDATE_NUMBER);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			int idCol = cursor.getColumnIndex(DatabaseContract.GameActionContract._ID);
			int actionCol = cursor.getColumnIndex(DatabaseContract.GameActionContract.COLUMN_NAME_ACTION);
			int betCol = cursor.getColumnIndex(DatabaseContract.GameActionContract.COLUMN_NAME_BET);
			int commCol = cursor.getColumnIndex(DatabaseContract.GameActionContract.COLUMN_NAME_COMM);
			int handCol = cursor.getColumnIndex(DatabaseContract.GameActionContract.COLUMN_NAME_HAND);
			int potCol = cursor.getColumnIndex(DatabaseContract.GameActionContract.COLUMN_NAME_POT);
			
			int id = cursor.getInt(idCol);
			String action = cursor.getString(actionCol);
			int bet = cursor.getInt(betCol);
			String comm = cursor.getString(commCol);
			String hand = cursor.getString(handCol);
			int pot = cursor.getInt(potCol);
			
			GameActionJson temp = new GameActionJson(id, PokerAction.getAction(action), pot, bet, hand, comm);
			actions.add(temp);
			cursor.moveToNext();
		}
		cursor.close();
		  return actions;
	  }
	  
	  /**
	   * Creates a game action in the database for the given ID and updatePosition in game
	   * 
	   * @param gameID The game that action is for
	   * @param updatePosition The update number for the game
	   * @param pot The amount of pot when that action occurs
	   * @param bet The amount of bet, if any, that the user had during the event
	   * @param hand The users current hand
	   * @param community The current set of community cards
	   * @param action The type of actions this is.
	   */
	  public void addGameAction(int gameID, int updatePosition,
			   int pot, int bet, Card hand[], Card[] community, PokerAction action)
	  {
		  if(GameActionExists(gameID, updatePosition)) return;
		  
		  
		  ContentValues values = new ContentValues();
		  values.put(DatabaseContract.GameActionContract.COLUMN_NAME_ACTION, action.getValue());
		  values.put(DatabaseContract.GameActionContract.COLUMN_NAME_BET, bet);
		  values.put(DatabaseContract.GameActionContract.COLUMN_NAME_GAME_ID, gameID);
		  values.put(DatabaseContract.GameActionContract.COLUMN_NAME_POT, pot);
		  values.put(DatabaseContract.GameActionContract.COLUMN_NAME_UPDATE_NUMBER, updatePosition);
		  
		  values.put(DatabaseContract.GameActionContract.COLUMN_NAME_COMM, Card.getCardsString(community));
		  values.put(DatabaseContract.GameActionContract.COLUMN_NAME_HAND, Card.getCardsString(hand));
		  
		  db.insert(DatabaseContract.GameActionContract.TABLE_NAME,
				  null,
				  values);
	  }
	  
	  /**
	   * Removes all game actions for a given database game id.
	   * @param gameID
	   */
	  public void removeGameActions(int gameID)
	  {
		  String where = DatabaseContract.GameActionContract.COLUMN_NAME_GAME_ID + " = " + gameID;
		  
		  db.delete(DatabaseContract.GameActionContract.TABLE_NAME,
				  where,
				  null);
	  }
	  
	  /**
	   * Checks if an game action already exists for a given game id and updatePosition
	   * 
	   * @param gameID
	   * @param updatePosition
	   * @return
	   */
	  public boolean GameActionExists(int gameID, int updatePosition)
	  {
		  String columns[] = new String[] {DatabaseContract.GameActionContract._ID};


		  String where = DatabaseContract.GameActionContract.COLUMN_NAME_GAME_ID + " = " + gameID + " AND "+
			DatabaseContract.GameActionContract.COLUMN_NAME_UPDATE_NUMBER + " = " + updatePosition;


		  Cursor cursor =  db.query(DatabaseContract.GameActionContract.TABLE_NAME, 
				  columns, 
				  where, 
				  null,
				  null, 
				  null, 
				  null);

		  cursor.moveToFirst();
		  if(!cursor.isAfterLast()){
			  cursor.close();
			  return true;
		  }
		  
		  cursor.close();
		  return false;
	  }
	  

	  
	  /**
	   * Callback that occurs after the database has been properly set-up.
	   * 
	   * @author dddurand
	   *
	   */
	  private class LoginDatabaseAsyncCallBack implements RetrieveDatabaseAsyncCallBack 
	  {

		@Override
		public void databaseAsyncCallBack(SQLiteDatabase database) {
			DatabaseDataSource.this.db = database;
			loadingDialog.dismiss();
		}
		  
	  }
}
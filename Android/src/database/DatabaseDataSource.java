package database;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import dataModels.Account;
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

	  public void open() throws SQLException {
		  LoginDatabaseAsyncCallBack callback = new LoginDatabaseAsyncCallBack();
		  RetrieveDatabaseAsync dbOpenTask = new RetrieveDatabaseAsync(callback);
		  try {
			dbOpenTask.execute(dbHelper).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  loadingDialog.show();
		  
	  }

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
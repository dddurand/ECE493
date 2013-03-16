package database;

import java.util.ArrayList;

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
		  loadingDialog.show();
		  dbOpenTask.execute(dbHelper);
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
	  
	  public void addMoneyGenerated(MoneyGenerated moneyGen)
	  {
		  
	  }
	  
	  public ArrayList<MoneyGenerated> getMoneyGenerates(Account account)
	  {
		  return null;
	  }
	  
	  public void removeMoneyGenerate(int generateID)
	  {
		  
	  }
	  
	  
	  
	  private class LoginDatabaseAsyncCallBack implements RetrieveDatabaseAsyncCallBack 
	  {

		@Override
		public void databaseAsyncCallBack(SQLiteDatabase database) {
			DatabaseDataSource.this.db = database;
			loadingDialog.dismiss();
		}
		  
	  }
}
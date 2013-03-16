package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.provider.BaseColumns;

/**
 * Handles the database scheme, and management of the sqlite database.
 * 
 * @author dddurand
 *
 */
public class DatabaseContract {

	private DatabaseContract() {}
	
	/**
	 * Account Table DEFINES
	 */
	public static abstract class AccountContract implements BaseColumns {
	    public static final String TABLE_NAME = "accounts";
	    public static final String COLUMN_NAME_USERNAME = "username";
	    public static final String COLUMN_NAME_AUTH_TOKEN = "authToken";
	    public static final String COLUMN_NAME_BALANCE = "balance";
	}
	
	/**
	 * GAME Table DEFINES
	 */
	public static abstract class GameContract implements BaseColumns {
	    public static final String TABLE_NAME = "game";
	    public static final String COLUMN_NAME_ACCOUNT_ID = "accountID";
	    public static final String COLUMN_NAME_UUID = "uuid";
	}
	
	/**
	 * GameAction Table DEFINES
	 */
	public static abstract class GameActionContract implements BaseColumns {
	    public static final String TABLE_NAME = "game_action";
	    public static final String COLUMN_NAME_GAME_ID = "uuid";
	    public static final String COLUMN_NAME_POT = "pot";
	    public static final String COLUMN_NAME_BET = "bet";
	    public static final String COLUMN_NAME_HAND = "hand";
	    public static final String COLUMN_NAME_COMM = "comm";
	    public static final String COLUMN_NAME_ACTION = "action";
	}
	
	/**
	 * Misc Table DEFINES
	 */
	public static abstract class MiscContract implements BaseColumns {
	    public static final String TABLE_NAME = "misc";
	    public static final String COLUMN_NAME_NAME = "name";
	    public static final String COLUMN_NAME_VALUE = "value";
	    public static final String COLUMN_NAME_ACCOUNTID = "accountID";
	}
	
	private static final String TEXT_TYPE = " TEXT";
	private static final String INTEGER_TYPE = " INTEGER";
	
	private static final String COMMA_SEP = ",";
	
	/**
	 * Create account table
	 */
	private static final String SQL_CREATE_ACCOUNT =
	    "CREATE TABLE " + DatabaseContract.AccountContract.TABLE_NAME + " (" +
	    		DatabaseContract.AccountContract._ID + " INTEGER PRIMARY KEY," +
	    		DatabaseContract.AccountContract.COLUMN_NAME_USERNAME + TEXT_TYPE + COMMA_SEP +
	    		DatabaseContract.AccountContract.COLUMN_NAME_AUTH_TOKEN + TEXT_TYPE + COMMA_SEP +
	    		DatabaseContract.AccountContract.COLUMN_NAME_BALANCE + INTEGER_TYPE +
	    " );";
	
	/**
	 * Create game action table
	 */
	private static final String SQL_CREATE_GAME_ACTION =
		    "CREATE TABLE " + DatabaseContract.GameActionContract.TABLE_NAME + " (" +
		    		DatabaseContract.GameActionContract._ID + " INTEGER PRIMARY KEY," +
		    		DatabaseContract.GameActionContract.COLUMN_NAME_GAME_ID + INTEGER_TYPE + COMMA_SEP +
		    		DatabaseContract.GameActionContract.COLUMN_NAME_POT + INTEGER_TYPE + COMMA_SEP +
		    		DatabaseContract.GameActionContract.COLUMN_NAME_BET + INTEGER_TYPE + COMMA_SEP +
		    		DatabaseContract.GameActionContract.COLUMN_NAME_HAND + TEXT_TYPE + COMMA_SEP +
		    		DatabaseContract.GameActionContract.COLUMN_NAME_COMM + TEXT_TYPE + COMMA_SEP +
		    		DatabaseContract.GameActionContract.COLUMN_NAME_ACTION + TEXT_TYPE +
		    " );";
	
	/**
	 * Create game table
	 */
	private static final String SQL_CREATE_GAME =
		    "CREATE TABLE " + DatabaseContract.GameContract.TABLE_NAME + " (" +
		    		DatabaseContract.GameContract._ID + " INTEGER PRIMARY KEY," +
		    		DatabaseContract.GameContract.COLUMN_NAME_ACCOUNT_ID + INTEGER_TYPE + COMMA_SEP +
		    		DatabaseContract.GameContract.COLUMN_NAME_UUID + TEXT_TYPE +
		    " );";

	/**
	 * Create misc table
	 */
	private static final String SQL_CREATE_MISC =
		    "CREATE TABLE " + DatabaseContract.MiscContract.TABLE_NAME + " (" +
		    		DatabaseContract.MiscContract._ID + " INTEGER PRIMARY KEY," +
		    		DatabaseContract.MiscContract.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
		    		DatabaseContract.MiscContract.COLUMN_NAME_VALUE + INTEGER_TYPE + COMMA_SEP +
		    		DatabaseContract.MiscContract.COLUMN_NAME_ACCOUNTID + INTEGER_TYPE +
		    " );";
	
	//private static final String SQL_CREATE = SQL_CREATE_ACCOUNT+SQL_CREATE_GAME_ACTION+SQL_CREATE_GAME+SQL_CREATE_MISC;
	
	/**
	 * Async Task to retrieve Sqlite database
	 * 
	 * @author dddurand
	 *
	 */
	public static class RetrieveDatabaseAsync extends AsyncTask<DatabaseDBHelper, Integer, SQLiteDatabase> {
	     
		private RetrieveDatabaseAsyncCallBack callback;
		
		public RetrieveDatabaseAsync(RetrieveDatabaseAsyncCallBack callback)
		{
			this.callback = callback;
		}
		
	     @Override
	    protected void onPreExecute() {
	    	super.onPreExecute();
	    }
	     
	     @Override
	    protected void onPostExecute(SQLiteDatabase result) {
	    	 callback.databaseAsyncCallBack(result);
	    	super.onPostExecute(result);
	    }

		@Override
		protected SQLiteDatabase doInBackground(DatabaseDBHelper... dbHelpers) {
			if(dbHelpers.length == 0) return null;
			
			SQLiteDatabase db = null;
			for(int attempt = 0; attempt < 3; attempt++)
			{
				db = dbHelpers[0].getWritableDatabase();
				if(db != null) break;
			}
			
			return db;
		}
	 }
	
	/**
	 * Callback interface for RetrieveDatabaseAsync task
	 * 
	 * @author dddurand
	 *
	 */
	interface RetrieveDatabaseAsyncCallBack
	{
		public void databaseAsyncCallBack(SQLiteDatabase dbHelpers);
	}
	
	/**
	 * Database helper to manager sqlite database
	 * 
	 * @author dddurand
	 *
	 */
	public static class DatabaseDBHelper extends SQLiteOpenHelper {
	    // If you change the database schema, you must increment the database version.
	    public static final int DATABASE_VERSION = 9;
	    public static final String DATABASE_NAME = "Database.db";

	    public DatabaseDBHelper(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }
	    public void onCreate(SQLiteDatabase db) {
	        db.execSQL(SQL_CREATE_ACCOUNT);
	        db.execSQL(SQL_CREATE_GAME_ACTION);
	        db.execSQL(SQL_CREATE_GAME);
	        db.execSQL(SQL_CREATE_MISC);
	    }
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	       
	    	String drop = " DROP TABLE IF EXISTS ";
	    	String close = " ; ";
	    	db.execSQL(drop+DatabaseContract.MiscContract.TABLE_NAME+close);
	    	db.execSQL(drop+DatabaseContract.AccountContract.TABLE_NAME+close);
	    	db.execSQL(drop+DatabaseContract.GameActionContract.TABLE_NAME+close);
	    	db.execSQL(drop+DatabaseContract.GameContract.TABLE_NAME+close);
	    	
	    	 db.execSQL(SQL_CREATE_ACCOUNT);
		        db.execSQL(SQL_CREATE_GAME_ACTION);
		        db.execSQL(SQL_CREATE_GAME);
		        db.execSQL(SQL_CREATE_MISC);
	    }
	}
	
}

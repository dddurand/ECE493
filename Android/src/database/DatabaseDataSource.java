package database;

import database.DatabaseContract.DatabaseDBHelper;
import database.DatabaseContract.RetrieveDatabaseAsync;
import database.DatabaseContract.RetrieveDatabaseAsyncCallBack;
import android.app.Dialog;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseDataSource {

	  // Database fields
	  private SQLiteDatabase database;
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
	  
	  //@TODO
	  //Add the database methods to come!
	  
	  
	  private class LoginDatabaseAsyncCallBack implements RetrieveDatabaseAsyncCallBack 
	  {

		@Override
		public void databaseAsyncCallBack(SQLiteDatabase database) {
			DatabaseDataSource.this.database = database;
			loadingDialog.dismiss();
		}
		  
	  }
}
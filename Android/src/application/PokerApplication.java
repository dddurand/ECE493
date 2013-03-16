package application;

import android.app.Application;
import dataModels.Account;
import database.DatabaseDataSource;

public class PokerApplication extends Application {

	
	private final String PREFS_NAME = "SETTINGS";
	private Account account = new Account();
	private DatabaseDataSource dataSource;
	private boolean isLoggedIn = false;
	
	
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public DatabaseDataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DatabaseDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
	
}

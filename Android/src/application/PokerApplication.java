package application;

import android.app.Application;
import database.DatabaseDataSource;

public class PokerApplication extends Application {

	private String username, authenticationToken;
	private boolean isLoggedIn;
	private boolean isOnlineUser;
	private DatabaseDataSource dataSource;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAuthenticationToken() {
		return authenticationToken;
	}
	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	public boolean isOnlineUser() {
		return isOnlineUser;
	}
	public void setOnlineUser(boolean isOnlineUser) {
		this.isOnlineUser = isOnlineUser;
	}
	public DatabaseDataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DatabaseDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
	
}

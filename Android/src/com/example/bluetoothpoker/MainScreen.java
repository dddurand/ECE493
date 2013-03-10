package com.example.bluetoothpoker;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import fragments.CreateTable;
import fragments.JoinTable;
import fragments.Login;
import fragments.OfflineMode;
import fragments.OnlineMode;
import fragments.RegisterUser;

public class MainScreen extends Activity {
	
	public final static int LOGIN_SCREEN = 0;
	public final static int OFFLINE_SCREEN = 1;
	public final static int ONLINE_SCREEN = 2;
	public final static int REGISTER_SCREEN = 3;
	public final static int JOIN_TABLE_SCREEN = 4;
	public final static int CREATE_TABLE_SCREEN = 5;
	public final static int ONLINE_MODE = 6;
	
	//This variable will contain the username. This will be set by the login fragment class
	private static String username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
		
		if (findViewById(R.id.fragment_container) != null) {

			// However, if we're being restored from a previous state,
			// then we don't need to do anything and should return or else
			// we could end up with overlapping fragments.
			if (savedInstanceState != null) {
				return;
			}
			
			this.switchFragment(MainScreen.LOGIN_SCREEN);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_screen, menu);
		return true;
	}
	
	public static void setUsername(String uname){
		username=uname;
	}
	
	public static String getUsername(){
		return username;
	}
	
	/**
	 * Method for changing the fragment in the main screen. Fragment changed to specified code.
	 * @param screen
	 */
	public void switchFragment(int screen) {
		Fragment newFragment;
		
		//Set the fragment object appropriately
		switch (screen) {
		
		case MainScreen.LOGIN_SCREEN:
			newFragment = new Login();
			break;
		
		case MainScreen.OFFLINE_SCREEN:
			newFragment = new OfflineMode();
			break;
			
		case MainScreen.REGISTER_SCREEN:
			newFragment = new RegisterUser();
			break;
			
		case MainScreen.JOIN_TABLE_SCREEN:
			newFragment = new JoinTable();
			break;
			
		case MainScreen.CREATE_TABLE_SCREEN:
			newFragment = new CreateTable();
			break;
			
		case MainScreen.ONLINE_MODE:
			//newFragment = new OnlineMode(username);
			System.out.println(username);
			newFragment = new OnlineMode();
			break;
			
		default: newFragment = new Login(); 
		
		}
		//Change the fragment
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		
		transaction.replace(R.id.fragment_container, newFragment);
		//If changed to the login screen, then it doesn't need to be added to the backstack
		if (screen!=MainScreen.LOGIN_SCREEN) transaction.addToBackStack(null);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		
		transaction.commit();
	}

}

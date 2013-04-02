package com.example.bluetoothpoker;

import java.util.concurrent.Semaphore;

import networking.ServerCodes;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import application.PokerApplication;
import client.UploadService;
import database.DatabaseDataSource;
import fragments.CreateTable;
import fragments.JoinTable;
import fragments.Login;
import fragments.OfflineMode;
import fragments.OnlineMode;
import fragments.RegisterUser;
import fragments.WaitClient;
import fragments.WaitingServer;

public class MainScreen extends Activity implements View.OnClickListener {
	
	public final static int LOGIN_SCREEN = 0;
	public final static int OFFLINE_SCREEN = 1;
	public final static int REGISTER_SCREEN = 3;
	public final static int JOIN_TABLE_SCREEN = 4;
	public final static int CREATE_TABLE_SCREEN = 5;
	public final static int ONLINE_MODE = 6;
	public static final int WAIT_CLIENT = 7;
	public static final int WAIT_SERVER = 8;
	
	private ServerCodes serverCodes;
	private int currentScreen;
	
	
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
			
			View v = (View) findViewById(R.id.mainscreenLeft);
			v.setOnClickListener(this);
			
			this.serverCodes = new ServerCodes(this);
			this.switchFragment(MainScreen.LOGIN_SCREEN);
			initializeDataSource();
			initializeUploadService();
			
		}
	}
	
	/**
	 * Initializes the upload service for the whole application.
	 */
	private void initializeUploadService()
	{
		Semaphore sem = new Semaphore(0);
		
		PokerApplication application = (PokerApplication) this.getApplication();
		application.setUploadServiceSemaphore(sem);
		
		UploadService uploadService = new UploadService(sem, application);
		Thread thread = new Thread(uploadService);
		thread.start();
	}
	
	/**
	 * Initializes the datasource interface for the whole application.
	 * A "initializing" dialog may show up temporarily if the sql database takes too long to load.
	 * 
	 */
	private void initializeDataSource()
	{
		Dialog dialog = new AlertDialog.Builder(this).
				setMessage(R.string.initializing).
				setCancelable(false).
				create();
		
		DatabaseDataSource dataSource = new DatabaseDataSource(this.getApplicationContext(), dialog);
		dataSource.open();
		PokerApplication application = (PokerApplication) this.getApplication();
		application.setDataSource(dataSource);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_screen, menu);
		return true;
	}

	/**
	 * Method for changing the fragment in the main screen. Fragment changed to specified code.
	 * @param screen
	 */
	public void switchFragment(int screen) {
		Fragment newFragment;
		this.currentScreen=screen;
		
		//Set the fragment object appropriately
		switch (screen) {
		
		case MainScreen.LOGIN_SCREEN:
//			newFragment = new Login(serverCodes);
			newFragment = new Login();
			((Login)newFragment).setServerCodes(serverCodes);
			break;
		
		case MainScreen.OFFLINE_SCREEN:
			newFragment = new OfflineMode();
			break;
			
		case MainScreen.REGISTER_SCREEN:
			newFragment = new RegisterUser(serverCodes);
			break;
			
		case MainScreen.JOIN_TABLE_SCREEN:
			newFragment = new JoinTable();
			break;
			
		case MainScreen.CREATE_TABLE_SCREEN:
			newFragment = new CreateTable();
			break;
			
		case MainScreen.ONLINE_MODE:
			newFragment = new OnlineMode();
			break;
			
		case MainScreen.WAIT_CLIENT:
			this.getIntent();
			newFragment = new WaitClient();
			((WaitClient)newFragment).setTitle(this.getIntent().getStringExtra(CreateTable.TITLE_HOLDER));
			break;
			
		case MainScreen.WAIT_SERVER:
			newFragment = new WaitingServer();
			break;
			
		default:newFragment = new Login();
			((Login)newFragment).setServerCodes(serverCodes); 
			//newFragment = new Login(serverCodes); 
		break;
		
		}
		//Change the fragment
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		
		transaction.replace(R.id.fragment_container, newFragment);
		
		//If changed to the login screen, join table r create table screens, then it doesn't need to be added to the backstack
		if (screen!=MainScreen.LOGIN_SCREEN) 
				transaction.addToBackStack(null);
		
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		
		transaction.commit();
	}

	/**
	 * Method executed when the user presses the back button. Prompts the user to log out
	 * if the current screen is the online mode (ie. user is logged in).
	 */
	@Override
	public void onBackPressed() {
		if (currentScreen==MainScreen.ONLINE_MODE)
		{
			new AlertDialog.Builder(this)
			.setTitle("Really Exit?")
			.setMessage("Are you sure you want to log out?")
			.setNegativeButton(android.R.string.no, null)
			.setPositiveButton(android.R.string.yes, new OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					//Get current fragment
					OnlineMode f = (OnlineMode) getFragmentManager().findFragmentById(R.id.fragment_container);
					//Simulate logout button press
					f.onClick(findViewById(R.id.logoutButton));
				}
			}).create().show();
		}
		else MainScreen.super.onBackPressed();
	}


	@Override
	public void onClick(View v) {
		//TODO Backdoor to join table screen. remove!
		Intent i = new Intent(this,PlayingArea.class);
		startActivity(i);
	}

}

package com.example.bluetoothpoker;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import fragments.PlayerFragment;

public class PlayingArea extends Activity {
	
	private FrameLayout[] players;
	private PlayerFragment[] pfs;
	private final int maxPlayers = 6;
	private FragmentManager fm;
	private int totalPlayers=2;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.playing_area);
	    //Get intent
	    Intent i = getIntent();
	    
	    /**Initialize fragment related variables**/
	    fm = getFragmentManager();
	    
	    //Initialize layouts array
	    players = new FrameLayout[maxPlayers];
	    //Save reference to layouts in array
	    players[0] = (FrameLayout)findViewById(R.id.player1);
	    players[1] = (FrameLayout)findViewById(R.id.player2);
	    
	    pfs = new PlayerFragment[2];
	    pfs[0] = new PlayerFragment();
	    pfs[1] = new PlayerFragment();
	    
	    update();
	    
	}
	
	public void update(){
		
		//Transaction begins here
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.setTransitionStyle(FragmentTransaction.TRANSIT_NONE);
		
		for (int i=0;i<totalPlayers;i++)
		{
			transaction.replace(players[i].getId(), pfs[i]);
		}
		
		transaction.commit();
	}
	
}

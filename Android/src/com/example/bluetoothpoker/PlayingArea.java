package com.example.bluetoothpoker;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import fragments.LocalPlayerFragment;
import fragments.PlayerFragment;
import fragments.River;

public class PlayingArea extends Activity {
	
	private FrameLayout[] players;
	private FrameLayout river;
	private PlayerFragment[] pfs;
	private final int maxPlayers = 6;
	private FragmentManager fm;
	private int totalPlayers=6;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.playing_area);
	    //Get intent
	    Intent intent = getIntent();
	    
	    /**Initialize fragment related variables**/
	    fm = getFragmentManager();
	    
	    //Initialize layouts array
	    players = new FrameLayout[maxPlayers];
	    //Save reference to layouts in array
	    players[0] = (FrameLayout)findViewById(R.id.localPlayer);
	    players[1] = (FrameLayout)findViewById(R.id.player2);
	    players[2] = (FrameLayout)findViewById(R.id.player3);
	    players[3] = (FrameLayout)findViewById(R.id.player4);
	    players[4] = (FrameLayout)findViewById(R.id.player5);
	    players[5] = (FrameLayout)findViewById(R.id.player6);
	    //River
	    river = (FrameLayout)findViewById(R.id.riverLayout);
	    
	    pfs = new PlayerFragment[totalPlayers];
	    //Local Player
	    pfs[0] = new PlayerFragment(true);
	    //Rest of players
	    for (int i=1;i<totalPlayers;i++)
	    {
		    pfs[i] = new PlayerFragment(false);
	    }
	    
	    
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
		
		//River
		River r = new River();
		transaction.replace(river.getId(),r);
		
		transaction.commit();
	}
	
}

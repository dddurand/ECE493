package com.example.bluetoothpoker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import fragments.PlayerFragment;

public class PlayingArea extends Activity implements OnClickListener{
	
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
	    
	    /**Set Listeners**/
	   Button b1 = (Button)findViewById(R.id.button1);
	   Button b2 = (Button)findViewById(R.id.button2);
	   b1.setOnClickListener(this);
	   b2.setOnClickListener(this);
	    
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
	
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setTitle("Really Exit?")
	        .setMessage("Are you sure you want to exit?")
	        .setNegativeButton(android.R.string.no, null)
	        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					PlayingArea.super.onBackPressed();
				}
			}).create().show();
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId())
		{
		
		case R.id.button1:
			pfs[0].update();
			break;
			
		case R.id.button2:
			pfs[1].update();
			break;
		
		}
		
	}
	
}

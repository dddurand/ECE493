package com.example.bluetoothpoker;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import fragments.PlayerFragment;
import fragments.River;

public class PlayingArea extends Activity implements OnClickListener {
	
	private FrameLayout[] playerLayouts;
	private FrameLayout riverLayout;
	private River riverObject;
	private PlayerFragment[] playerObjects;
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
	    
	    /***********Set listeners for buttons****************/
	    Button b1 = (Button)findViewById(R.id.button1);
	    Button b2 = (Button)findViewById(R.id.button2);
	    b1.setOnClickListener(this);
	    b2.setOnClickListener(this);
	    
	    
	    /**********************Initialize fragment related variables************************/
	    fm = getFragmentManager();
	    
	    //Initialize layouts array
	    playerLayouts = new FrameLayout[maxPlayers];
	    //Save reference to layouts in array
	    playerLayouts[0] = (FrameLayout)findViewById(R.id.localPlayer);
	    playerLayouts[1] = (FrameLayout)findViewById(R.id.player2);
	    playerLayouts[2] = (FrameLayout)findViewById(R.id.player3);
	    playerLayouts[3] = (FrameLayout)findViewById(R.id.player4);
	    playerLayouts[4] = (FrameLayout)findViewById(R.id.player5);
	    playerLayouts[5] = (FrameLayout)findViewById(R.id.player6);
	    //River
	    riverLayout = (FrameLayout)findViewById(R.id.riverLayout);
	    
	    /**********Instatiate Objecst*******/
	    playerObjects = new PlayerFragment[totalPlayers];
	    //Local Player
	    playerObjects[0] = new PlayerFragment(true,"local");
	    //Rest of players
	    for (int i=1;i<totalPlayers;i++) playerObjects[i] = new PlayerFragment(false,"Player "+Integer.toString(i));
	    
	    initialize();
	}
	
	/**
	 * Initializes the playing area by placing downfacing cards for other players and the received cards
	 * for the local player. Also sets the river to 3 down facing cards.
	 */
	private void initialize(){
		
		//Transaction begins here
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.setTransitionStyle(FragmentTransaction.TRANSIT_NONE);
		
		//Place all fragments
		for (int i=0;i<totalPlayers;i++)
		{
			transaction.replace(playerLayouts[i].getId(), playerObjects[i]);
		}
		
		//River
		riverObject = new River();
		transaction.replace(riverLayout.getId(),riverObject);
		
		transaction.commit();
	}
	
	public void updateRiver(){
		
	}
	
	public void updatePlayers(){
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()){
		
		case R.id.button1:
			riverObject.setCard(1, "s1");
			playerObjects[3].setCard(0, "c1");
			break;
			
		case R.id.button2:
			riverObject.setCard(3, "hk");
			riverObject.setCard(4, "hq");
			playerObjects[3].setCard(1, "sk");
			break;
		
		}
		
		
	}
	
}

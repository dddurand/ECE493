package com.example.bluetoothpoker;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Stats extends Activity implements TabListener, OnGestureListener {
	
	public static final int PERSONAL_STATS = 0;
	public static final int COMMUNITY_STATS = 1;
	public static final int RANKING_STATS = 2;
	
	private ActionBar ab;
	private int currentTabPos=0;
	private GestureDetector detector;
	
	//Data Test
	String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
			  "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
			  "Linux", "OS/2","Android", "iPhone", "WindowsMobile",
			  "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
			  "Linux", "OS/2" };
	
	String[] values2 = new String[] { "Android2", "iPhone2", "Windows2Mobile",
			  "Blackb2erry", "WebOS", "Ubuntu", "Windows72", "Max OS X",
			  "Linu2x", "OS/2","Android", "iPhone", "WindowsMobile",
			  "Blackbe2rry", "WebOS", "Ubuntu", "Windows72", "Max OS X2",
			  "Linu2x", "OS/22" };
	
	String[] values3 = new String[] { "Andr3oid", "iP3hone", "WindowsMobi3le",
			  "Black3berry", "Web3OS", "Ubunt3u", "Windows7", "Max OS X",
			  "Linu3x", "OS/2","An3droid", "iPhone", "WindowsMobile",
			  "Blac3kberry", "WebO3S", "Ub3untu", "Win3dows7", "Max OS3 X",
			  "Lin3ux", "OS3/2" };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.stats_screen);
	    
	    //Get action bar
	    ab = getActionBar();
	    ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    
	    //Create all action bar items and set their respective icons
	    ActionBar.Tab personalStatsTab = ab.newTab().setText("Personal");
	    personalStatsTab.setIcon(R.drawable.ic_personal_stats);
	    ActionBar.Tab globalStatsTab = ab.newTab().setText("Community");
	    globalStatsTab.setIcon(R.drawable.ic_global_stats);
	    ActionBar.Tab rankingStatsTab = ab.newTab().setText("Ranking");
	    rankingStatsTab.setIcon(R.drawable.ic_ranking_stats);
	    
	    //Gesture on list
	    detector = new GestureDetector(this,this);
	    ListView list = (ListView)findViewById(R.id.statsListView);
	    list.setOnTouchListener(new OnTouchListener() {
	    	
	    	public boolean onTouch(View view, MotionEvent e){
	    		detector.onTouchEvent(e);
	    		return false;
	    	}
	    });
	    
	    //Set the tab listeners to this class
	    personalStatsTab.setTabListener(this);
	    globalStatsTab.setTabListener(this);
	    rankingStatsTab.setTabListener(this);
	    
	    //Finally add them to the action bar
	    ab.addTab(personalStatsTab);
	    ab.addTab(globalStatsTab);
	    ab.addTab(rankingStatsTab);
	}
	
	/**
	 * Method for changing the currently selected tab and its content in the list view.
	 * Done after a swipe.
	 * @param currentTabPos
	 */
	private void setTabContent(int currentTabPos){
		ab.setSelectedNavigationItem(currentTabPos);
		ListView list = (ListView)findViewById(R.id.statsListView);
		String[] v;
		
		switch (currentTabPos){
		
		case Stats.PERSONAL_STATS:v=values;
			break;
			
		case Stats.COMMUNITY_STATS:v=values2;
			break;
			
		case Stats.RANKING_STATS:v=values3;
			break;
			
			default:v=values;
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.stats_list_element, R.id.statsEntryText,v);
		
		list.setAdapter(adapter);
	}
	
	/**********************************************************************************************/
	/***************************************TAB LISTENERS******************************************/
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
	}

	//Update current position when tab is selected
	@Override
	public void onTabSelected(Tab t, FragmentTransaction arg1) {
		currentTabPos=t.getPosition();
		setTabContent(currentTabPos);
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
	}
	/***************************************TAB LISTENERS END**************************************/
	/**********************************************************************************************/
	
	/**************************************************************************************************************/
	/***************************************GESTURE LISTENERS******************************************************/

	/**
	 * Method for listening for flings/swipes in this activity.
	 */
	@Override
	public boolean onFling(MotionEvent me0, MotionEvent me1, float velX,
			float velY) {
		
		float x1 = me0.getX();
		float x2 = me1.getX();
		
		//Check velocity of swipe. If velocity of X is greater than Y, then it means a horizontal swipe
		if (Math.abs(velX) > Math.abs(velY)) {
			//right swipe
			if (x1<x2) currentTabPos++;
			//else left swipe
			else currentTabPos--;

			//Reset value of position if necessary
			if (currentTabPos>2) currentTabPos=0;
			if (currentTabPos<0) currentTabPos=2;

			//Set it
			setTabContent(currentTabPos);
		}
		
		return false;
	}
	
	@Override
	public boolean onDown(MotionEvent arg0) {
		return false;
	}
	

	@Override
	public void onLongPress(MotionEvent arg0) {
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}

}

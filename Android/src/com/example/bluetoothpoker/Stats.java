package com.example.bluetoothpoker;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import misc.CustomAdapter;
import misc.StatsRowObject;
import networking.NPersonalStats;
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
import android.widget.Button;
import android.widget.ListView;
import dataModels.PersonalStatistics;
import dataModels.TimeFrame;

public class Stats extends Activity implements TabListener, OnGestureListener, OnTouchListener, OnClickListener {
	
	public static final int PERSONAL_STATS = 0;
	public static final int COMMUNITY_STATS = 1;
	public static final int RANKING_STATS = 2;
	
	//Buttons. For saving state
	Button dayButton, weekButton, monthButton, yearButton, allButton;
	
	private ActionBar ab;
	private int currentTabPos=0;
	private GestureDetector detector;
	private int selectedTimeframe;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.stats_screen);
	    
	    /*********Get views*********/
	    dayButton = (Button)findViewById(R.id.statsDayButton);
	    weekButton = (Button)findViewById(R.id.statsWeekButton);
	    monthButton = (Button)findViewById(R.id.statsMonthButton);
	    yearButton = (Button)findViewById(R.id.statsYearButton);
	    allButton = (Button)findViewById(R.id.statsAllButton);
	    
	    /********Set button listeners*********/
	    dayButton.setOnClickListener(this);
	    dayButton.setOnTouchListener(this);
	    
	    weekButton.setOnClickListener(this);
	    weekButton.setOnTouchListener(this);
	    
	    monthButton.setOnClickListener(this);
	    monthButton.setOnTouchListener(this);
	    
	    yearButton.setOnClickListener(this);
	    yearButton.setOnTouchListener(this);
	    
	    allButton.setOnClickListener(this);
	    allButton.setOnTouchListener(this);
	    
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
	    
	    /***********Gesture on list**************/
	    detector = new GestureDetector(this,this);
	    ListView list = (ListView)findViewById(R.id.statsListView);
	    list.setOnTouchListener(new OnTouchListener() {
	    	
	    	public boolean onTouch(View view, MotionEvent e){
	    		detector.onTouchEvent(e);
	    		return false;
	    	}
	    });
	    
	    /*********Set the tab listeners to this class*********/
	    personalStatsTab.setTabListener(this);
	    globalStatsTab.setTabListener(this);
	    rankingStatsTab.setTabListener(this);
	    
	    
	    //Finally add them to the action bar
	    ab.addTab(personalStatsTab);
	    ab.addTab(globalStatsTab);
	    ab.addTab(rankingStatsTab);
	    
	    //Clean buttons up
	    clearTimeframeButtons();
	    //Initializes screen
	    this.onTouch(dayButton, null);
	    this.selectedTimeframe=dayButton.getId();
	}
	
	/**
	 * Method that sets all the buttons' backgrounds to 0.
	 */
	private void clearTimeframeButtons(){
		
		dayButton.setBackgroundResource(0);
		weekButton.setBackgroundResource(0);
		monthButton.setBackgroundResource(0);
		yearButton.setBackgroundResource(0);
		allButton.setBackgroundResource(0);
	}
	
	/**
	 * Method for changing the currently selected tab and its content in the list view.
	 * Done after a left/right swipe.
	 * @param currentTabPos
	 */
	private void setTabContent(int currentTabPos){
		ab.setSelectedNavigationItem(currentTabPos);
		ListView list = (ListView)findViewById(R.id.statsListView);
		StatsRowObject row;
		
		switch (currentTabPos){
		
		case Stats.PERSONAL_STATS:row = new StatsRowObject("Hello1","There1");
			break;
			
		case Stats.COMMUNITY_STATS:row = new StatsRowObject("Hello2","There2");
			break;
			
		case Stats.RANKING_STATS:row = new StatsRowObject("Hello3","There3");
			break;
			
			default:row = new StatsRowObject("Hello","There");
		}
		
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.stats_list_element, R.id.statsEntryText,v);
		ArrayList<StatsRowObject> objects = new ArrayList<StatsRowObject>();
		 
		for (int i=0; i<60;i++)
			objects.add(row);
		
		CustomAdapter customAdapter = new CustomAdapter(this,objects);
		list.setAdapter(customAdapter);
	}
	
	/**********************************************************************************************/
	/***************************************TAB LISTENERS******************************************/
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		//do nothing
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
	 * Selects the corresponding action bar item if the velocity of X in the swipe is
	 * greater than the velocity of Y.
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
	
	/*********************Click Listener********************/
	@Override
	public void onClick(View v) {
		
		this.selectedTimeframe=v.getId();
		
//		switch(v.getId()){
//		
//		case R.id.statsDayButton:
//			break;
//			
//		case R.id.statsWeekButton:
//			break;
//			
//		case R.id.statsMonthButton:
//			break;
//			
//		case R.id.statsYearButton:
//			break;
//			
//		case R.id.statsAllButton:
//			break;
//		}
		
		//TODO Ask Dustin about where the account is stored!
//		PersonalStatistics.PersonalStatisticRequest requestObject = new PersonalStatistics.PersonalStatisticRequest(TimeFrame.DAY,null);
//		NPersonalStats serverRequest = new NPersonalStats(getApplicationContext());
//		try {
//			PersonalStatistics response = serverRequest.execute(requestObject).get();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
	}
	
	/**
	 * On touch listener: changes the background of the touched button and clears
	 * the other buttons
	 */
	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		Button b = (Button)v;
		clearTimeframeButtons();
		b.setBackgroundResource(R.drawable.timeframe_button_border_selected);
		
		return false;
	}
	
	/**----------------------Other Listeners not being used but required by interface------------------*/
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

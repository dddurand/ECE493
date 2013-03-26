package com.example.bluetoothpoker;

import java.util.ArrayList;

import misc.CustomAdapter;
import misc.StatsRowObject;
import networking.NCommunityStats;
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
import android.widget.ProgressBar;
import application.PokerApplication;
import dataModels.Account;
import dataModels.CommunityStatistics;
import dataModels.PersonalStatistics;
import dataModels.RankingStatistics;
import dataModels.SimpleStatistic;
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
	private TimeFrame selectedTimeframe = TimeFrame.DAY;
	private ProgressBar pb;
	
	//Account
	private Account account;
	//Request objects
	private PersonalStatistics.PersonalStatisticRequest personalStatsRequest;
	private RankingStatistics.RankingStatisticRequest rankingStatsRequest;
	private CommunityStatistics.CommuntyStatisticRequest commStatsRequest;

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
	    pb = (ProgressBar)findViewById(R.id.statsProgressBar);
	    
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
	    
	    //Get account for request objects
	  	PokerApplication application = (PokerApplication)this.getApplication();
	  	account = application.getAccount();
	  	
	    //Creates request objects for all tabs
	    personalStatsRequest = new PersonalStatistics.PersonalStatisticRequest(this.selectedTimeframe,account);
	    rankingStatsRequest = new RankingStatistics.RankingStatisticRequest(this.selectedTimeframe,account);
	    commStatsRequest = new CommunityStatistics.CommuntyStatisticRequest(this.selectedTimeframe,account);
	    
	    //Initializes screen by simulating a touch and click to the first button
	    this.onTouch(dayButton, null);
	    this.onClick(dayButton);
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
	private void setListContent(ArrayList<SimpleStatistic> content){
		
		//Get List
		ListView list = (ListView)findViewById(R.id.statsListView);
		StatsRowObject row;
		int rowCount = content.size();
		
		ArrayList<StatsRowObject> rowObjects = new ArrayList<StatsRowObject>();
		 
		for (int i=0; i<rowCount;i++){
			row = new StatsRowObject(content.get(i).getDisplayName(),content.get(i).getValue().toString());
			rowObjects.add(row);
		}
			
		
		CustomAdapter customAdapter = new CustomAdapter(this,rowObjects);
		list.setAdapter(customAdapter);
	}
	
	private void clearList(){
		ListView list = (ListView)findViewById(R.id.statsListView);
		list.setAdapter(null);
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
		
		
		//Get the currently pressed button and send that as parameter to the
		//on click method
		switch(this.selectedTimeframe){
		
		case DAY:
			break;
			
		case WEEK:
			break;
			
		case MONTH:
			break;
			
		case YEAR:
			break;
			
		case ALL:
			break;
			
			default:
		
		}
		
		
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
			ab.setSelectedNavigationItem(currentTabPos);
		}
		
		return false;
	}
	
	/*********************Click Listener********************/
	/**
	 * Changes the time frame for the current stats request object and 
	 * gets new stats from the server
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		
		//Choose appropriate timeframe according to the pressed button
		switch(v.getId()){
		
		case R.id.statsDayButton:
			this.selectedTimeframe=TimeFrame.DAY;
			break;
			
		case R.id.statsWeekButton:
			this.selectedTimeframe=TimeFrame.WEEK;
			break;
			
		case R.id.statsMonthButton:
			this.selectedTimeframe=TimeFrame.MONTH;
			break;
			
		case R.id.statsYearButton:
			this.selectedTimeframe=TimeFrame.YEAR;
			break;
			
		case R.id.statsAllButton:
			this.selectedTimeframe=TimeFrame.ALL;
			break;
			
			default:this.selectedTimeframe=TimeFrame.DAY;
		}
		
		//Clear List before getting new data
		clearList();
		pb.setVisibility(View.VISIBLE);
		
		//Modify the appropiate request object and execute
		switch(ab.getSelectedNavigationIndex()){
		
		case Stats.PERSONAL_STATS:
			personalStatsRequest.setTimeFrame(selectedTimeframe);
			NPersonalStats personalServer = new NPersonalStats(getApplicationContext(),this);
			personalServer.execute(personalStatsRequest);
			break;
			
		case Stats.COMMUNITY_STATS:
			commStatsRequest.setTimeFrame(selectedTimeframe);
			NCommunityStats commServer = new NCommunityStats(getApplicationContext(),this);
			commServer.execute(commStatsRequest);
			break;
			
		case Stats.RANKING_STATS:
			rankingStatsRequest.setTimeFrame(selectedTimeframe);
			break;
		
		}
		
	}
	
	/**
	 * Method executed after the Asynctask is done retrieving the stats. This will take care of
	 * populating the list.
	 */
	public void onPostPersonalStatsRequest(PersonalStatistics result){
		//Hide ProgressBar
		pb.setVisibility(View.INVISIBLE);
		//Extract the statistics
		ArrayList<SimpleStatistic> stats = result.getAllStatistics();
		//Call method to change content
		setListContent(stats);
	}
	
	public void onPostCommunityStatsRequest(CommunityStatistics result){
		//Hide ProgressBar
		pb.setVisibility(View.INVISIBLE);
		//Extract the statistics
		ArrayList<SimpleStatistic> stats = result.getAllStatistics();
		//Call method to change content
		setListContent(stats);
	}
	
//	public void onPostRankingStatsRequest(RankingStatistics result){
//		//Hide ProgressBar
//		pb.setVisibility(View.INVISIBLE);
//		//Extract the statistics
//		ArrayList<SimpleRankStatistic> stats = result.getAllStatistics();
//		//Call method to change content
//		setListContent(stats);
//	}

	
	/**
	 * On touch listener: changes the background of the touched button and clears
	 * the other buttons. Does not consume the onClick listener.
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

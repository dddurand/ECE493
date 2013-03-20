package com.example.bluetoothpoker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TabHost;

public class Stats extends Activity {
	
	private TabHost tabHost;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.stats_screen);
	    
	    /**Get views*/
	    this.tabHost = (TabHost)findViewById(R.id.tabHost);
	}
	
	private void setupTabs(){
		tabHost.setup();
//		tabHost.addTab();
	}
	

}

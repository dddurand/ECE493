package client.mock;

import server.GameState;
import android.os.Bundle;
import android.view.Menu;

import com.example.bluetoothpoker.PlayingArea;
import com.example.bluetoothpoker.test.R;

public class PlayAreaMock extends PlayingArea {

	private boolean isUpdated = false;
	private GameState state;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public void updateAll(GameState state)
	{
		this.state = state;
		this.isUpdated = true;
	}
	
	public boolean isUpdated()
	{
		return isUpdated;
	}

	/**
	 * @return the state
	 */
	public GameState getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(GameState state) {
		this.state = state;
	}

	
	
}

package misc;

import android.os.AsyncTask;
import android.widget.ProgressBar;

public class ProgressBarAnimator extends AsyncTask<ProgressBar,Integer,ProgressBar> {
	
	private final int time = 7000; //7 seconds
	
	@Override
	protected void onPreExecute(){
		
	}

	@Override
	protected ProgressBar doInBackground(ProgressBar... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}

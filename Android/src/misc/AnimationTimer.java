package misc;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;

import com.example.bluetoothpoker.PlayingArea;

/**
 * Class that serves as the timer for animating a progress bar.
 * This is the class that should be used in a view when it is not the local player's turn.
 * (ie. does not send a fold game action to the actionQueue)
 * @author kennethrodas
 */
public class AnimationTimer extends CountDownTimer {
	
	private PlayingArea parent;
	private int pos;
	private ProgressBar pb;
	
	/**
	 * @param millisInFuture
	 * @param countDownInterval
	 * @param parent
	 * @param pos
	 */
	public AnimationTimer(long millisInFuture, long countDownInterval, PlayingArea parent, int pos) {
		super(millisInFuture, countDownInterval);
		//get all variables
		this.parent=parent;
		this.pos=pos;
		//get progress bar
		this.pb = parent.getPlayerProgressBar(pos);
	}

	@Override
	public void onFinish() {
		pb.setVisibility(View.INVISIBLE);
		//TODO player fold?
		parent.foldPlayer(pos);
	}

	@Override
	public void onTick(long millisUntilFinished) {
		pb.setProgress((int)millisUntilFinished);
	}
	
	/**
	 * Stops the timer and hides the view associated with the player position.
	 * Called when the user does an action before the timer expires.
	 */
	public void stop(){
		pb.setVisibility(View.INVISIBLE);
		this.cancel();
	}

}

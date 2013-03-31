package misc;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;

import com.example.bluetoothpoker.PlayingArea;

public class TurnTimer extends CountDownTimer {
	
	private int pos;
	public ProgressBar pb;
	private PlayingArea parent;

	/**
	 * Constructor that takes the activity for updating the corresponding position
	 */
	public TurnTimer(long millisInFuture, long countDownInterval, PlayingArea parent, int pos){
		super(millisInFuture,countDownInterval);
		this.parent = parent;
//		this.pos = parent.getPositionAtTable();
		this.pos=pos;
		this.pb = parent.getPlayerProgressBar(pos);
	}
	
	/**
	 * Returns the player position given in the constructor
	 * @return
	 */
	public int getPos(){
		return this.pos;
	}
	
	/**
	 * Hides the progress bar that this instance is currently associated with.
	 */
	public void hideProgressBar(){
		this.pb.setProgress(0);
		this.pb.setVisibility(View.INVISIBLE);
	}

	/**
	 * Method executed when timer finishes. Player automatically folds.
	 */
	@Override
	public void onFinish() {
		pb.setVisibility(View.INVISIBLE);
		//Player didnt take turn, fold
		parent.foldPlayer(pos);
//		GameAction action = new GameAction(pos,PokerAction.FOLD,0);
	}
	
	/**
	 * Updates the progress bar.
	 */
	@Override
	public void onTick(long millisUntilFinished) {
		pb.setProgress((int)millisUntilFinished);
	}

}

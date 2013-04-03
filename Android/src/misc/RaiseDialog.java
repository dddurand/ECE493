package misc;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.bluetoothpoker.PlayingArea;
import com.example.bluetoothpoker.R;

public class RaiseDialog extends Dialog {
	
	private TextView amountLabel;
	private int min, max;
	private ImageButton okButton, cancelButton;
	private SeekBar sb;
	
	public PlayingArea parent;
	
	public RaiseDialog(PlayingArea activity, int min, int max){
		
		//Set up dialog
		super(activity);
		this.parent=activity;
		this.setContentView(R.layout.raise_dialog);
		this.setTitle("Raise Amount");
		
		//Save values of variables
		this.min=min;
		this.max=max;
		
		//Get Views
		this.okButton=(ImageButton)this.findViewById(R.id.raiseDialogOKButton);
		this.cancelButton=(ImageButton)this.findViewById(R.id.raiseDialogCancelButton);
		this.amountLabel = (TextView) this.findViewById(R.id.raiseDialogAmountLabel);
		this.sb=(SeekBar)this.findViewById(R.id.raiseAmountSeekBar);
		
		//Initialize Views
		amountLabel.setText(Integer.toString(min));
		sb.setMax(max-min);
		sb.setProgress(0);
		sb.setOnSeekBarChangeListener(new SeekBarWatcher());
		
		/******************************Listeners for buttons*************************/
		//Cancel Button
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RaiseDialog.this.dismiss();
			}
		});
		
		//Ok Button:calls the activity raiseFromDialog
		okButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Get quantity
				int raiseAmount = Integer.parseInt(amountLabel.getText().toString());
				RaiseDialog.this.parent.raiseFromDialog(raiseAmount);
				RaiseDialog.this.dismiss();
			}
		});
	}
	
	
	/**
	 * The Seek Bar that is used to choose the amount to add to the account.
	 * Taken from AmountDialog.java by @author dddurand
	 */
	public class SeekBarWatcher implements OnSeekBarChangeListener {

		//When the seekbar is modified by the user
		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			TextView label = RaiseDialog.this.amountLabel;
			label.setText(Integer.toString(arg1+RaiseDialog.this.min));
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {}

	}

}

package Misc;

import android.app.Dialog;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.bluetoothpoker.R;

public class SeekBarWatcher implements OnSeekBarChangeListener {
	
	private int id;
	private Dialog d;
	
	//Constructor
	public SeekBarWatcher(int id, Dialog d)
	{
		this.id=id;
		this.d=d;
	}

	//When the seekbar is modified by the user
	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		int amount = arg1*10;
		TextView label = (TextView) this.d.findViewById(R.id.dialogAmountText);
		label.setText(Integer.toString(amount));
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		
	}

}

package fragments;

import Misc.SeekBarWatcher;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.example.bluetoothpoker.MainScreen;
import com.example.bluetoothpoker.R;

public class OfflineMode extends Fragment implements OnClickListener,TextWatcher {
	
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Load XML Layout into global variable
		this.view = inflater.inflate(R.layout.offline_fragment,container, false);
		
		/******************Set listener for buttons******************/
		ImageButton addFundsButton = (ImageButton) view.findViewById(R.id.addFundsButton);
		ImageButton joinTableButton = (ImageButton) view.findViewById(R.id.joinTableButton);
		ImageButton createTableButton = (ImageButton) view.findViewById(R.id.createTableButton);
		addFundsButton.setOnClickListener(this);
		joinTableButton.setOnClickListener(this);
		createTableButton.setOnClickListener(this);
		/***Set listener for edittext***/
		EditText offlineUsername = (EditText) view.findViewById(R.id.offlineUsernameTextField);
		offlineUsername.addTextChangedListener(this);
		
		return view;
	}
	
	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		
		//arg0 provides the current string
		System.out.println("Change: "+arg0.toString());
		
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {

		/*****Add funds button***/
		case R.id.addFundsButton:
			this.showAmountDialog("Choose desired initial amount");
			break;
			
		case R.id.joinTableButton:
			((MainScreen) getActivity()).switchFragment(MainScreen.JOIN_TABLE_SCREEN);
			break;
			
		case R.id.createTableButton:
			((MainScreen) getActivity()).switchFragment(MainScreen.CREATE_TABLE_SCREEN);
			break;
			
		}
		
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Method for displaying a dialog for the funds and obtaining amount from user
	 */
	private void showAmountDialog(String message){
		
		//Initialize dialog
		final Dialog d = new Dialog(getActivity());
		d.setContentView(R.layout.funds_dialog);
		d.setTitle(message);
		
		//Set listeners for different components
		//Cancel Button Listener
		ImageButton dialogCancelButton = (ImageButton)d.findViewById(R.id.fundsDialogCancelButton);
		dialogCancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				d.dismiss();
			}
		});
		//Seekbar listener
		SeekBar sb = (SeekBar)d.findViewById(R.id.amountSeekBar);
		sb.setOnSeekBarChangeListener(new SeekBarWatcher(R.id.amountSeekBar,d));
		
		d.show();
		
	}



}

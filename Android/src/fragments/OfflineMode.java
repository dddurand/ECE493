package fragments;

import misc.AmountDialog;
import misc.BalanceUpdatable;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import application.PokerApplication;

import com.example.bluetoothpoker.MainScreen;
import com.example.bluetoothpoker.R;

import dataModels.Account;
import database.PreferenceConstants;

public class OfflineMode extends Fragment implements OnClickListener,TextWatcher, BalanceUpdatable {
	
	private View view;
	private PokerApplication application;
	private SharedPreferences preferences;
	private EditText offlineUsername;
	private TextView offlineBalance;
	private Account account;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Load XML Layout into global variable
		this.view = inflater.inflate(R.layout.offline_fragment,container, false);
		
		/******************Set listener for buttons******************/
//		ImageButton addFundsButton = (ImageButton) view.findViewById(R.id.addFundsButton);
//		ImageButton joinTableButton = (ImageButton) view.findViewById(R.id.joinTableButton);
//		ImageButton createTableButton = (ImageButton) view.findViewById(R.id.createTableButton);
		Button addFundsButton = (Button) view.findViewById(R.id.addFundsButton);
		Button joinTableButton = (Button) view.findViewById(R.id.joinTableButton);
		Button createTableButton = (Button) view.findViewById(R.id.createTableButton);
		addFundsButton.setOnClickListener(this);
		joinTableButton.setOnClickListener(this);
		createTableButton.setOnClickListener(this);
		/***Set listener for edittext***/
		offlineUsername = (EditText) view.findViewById(R.id.offlineUsernameTextField);
		offlineBalance = (TextView) view.findViewById(R.id.fundsAmountTextview);
		
		offlineUsername.addTextChangedListener(this);
		
		preferences = this.getActivity().getSharedPreferences(PokerApplication.PREFS_NAME, Context.MODE_PRIVATE);
		this.application = (PokerApplication) this.getActivity().getApplication();
		account = application.getAccount();
		
		updateBalance();
		
		offlineUsername.setText(account.getUsername());
		
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
			this.showAmountDialog("How much do you want to add to your balance?");
			break;
			
		case R.id.joinTableButton:
			updateOfflineUsername();
			((MainScreen) getActivity()).switchFragment(MainScreen.JOIN_TABLE_SCREEN);
			break;
			
		case R.id.createTableButton:
			updateOfflineUsername();
			((MainScreen) getActivity()).switchFragment(MainScreen.CREATE_TABLE_SCREEN);
			break;
			
		}
		
	}

	private void updateOfflineUsername()
	{
		Editor editor = preferences.edit();
		editor.putString(PreferenceConstants.OFFLINE_USER_NAME, offlineUsername.getText().toString());
		editor.commit();
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
		AmountDialog dialog = new AmountDialog(message, this.getActivity(), this);
		dialog.show();
	}

	@Override
	public void updateBalance() {
		Account account = application.getAccount();
		offlineBalance.setText(Integer.toString(account.getBalance()));
	}



}

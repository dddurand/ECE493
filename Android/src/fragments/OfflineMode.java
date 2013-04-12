package fragments;

import misc.AmountDialog;
import misc.BalanceUpdatable;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import application.PokerApplication;

import com.example.bluetoothpoker.MainScreen;
import com.example.bluetoothpoker.R;

import dataModels.Account;
import database.PreferenceConstants;

/**
 * @SRS 3.2.1.5
 * @author dddurand
 *
 */
public class OfflineMode extends Fragment implements OnClickListener, BalanceUpdatable {
	
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
		
		preferences = this.getActivity().getSharedPreferences(PokerApplication.PREFS_NAME, Context.MODE_PRIVATE);
		this.application = (PokerApplication) this.getActivity().getApplication();
		account = application.getAccount();
		
		updateBalance();
		
		offlineUsername.setText(account.getUsername());
		
		return view;
	}

	@Override
	public void onResume() {
		updateBalance();
		super.onResume();
	}
	
	@Override
	public void onClick(View v) {
		
		String name = offlineUsername.getText().toString();
		
		switch(v.getId()) {

		/*****Add funds button***/
		//@SRS 3.2.1.5.3
		case R.id.addFundsButton:
			this.showAmountDialog("How much do you want to add to your balance?");
			break;
		
		//@SRS 3.2.1.5.1
		case R.id.joinTableButton:
			
			if(name.isEmpty())
			{
				Toast.makeText(this.getActivity(), R.string.empty_name, Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(this.account.getBalance() <= 0)
			{
				Toast.makeText(this.getActivity(), R.string.zero_balance, Toast.LENGTH_SHORT).show();
				return;
			}
			
			updateOfflineUsername();
			((MainScreen) getActivity()).switchFragment(MainScreen.JOIN_TABLE_SCREEN);
			break;
		
		//@SRS 3.2.1.5.2
		case R.id.createTableButton:
			
			if(name.isEmpty())
			{
				Toast.makeText(this.getActivity(), R.string.empty_name, Toast.LENGTH_SHORT).show();
				return;
			}
			
			updateOfflineUsername();
			((MainScreen) getActivity()).switchFragment(MainScreen.CREATE_TABLE_SCREEN);
			break;
			
		}
		
	}

	/**
	 * Updates username label
	 */
	private void updateOfflineUsername()
	{
		String username = offlineUsername.getText().toString();
		account.setUsername(username);
		Editor editor = preferences.edit();
		editor.putString(PreferenceConstants.OFFLINE_USER_NAME, username);
		editor.commit();
	}
	
	/**
	 * Method for displaying a dialog for the funds and obtaining amount from user
	 */
	private void showAmountDialog(String message){	
		AmountDialog dialog = new AmountDialog(message, this.getActivity(), this);
		dialog.show();
	}

	/**
	 * Updates balance label
	 */
	@Override
	public void updateBalance() {
		Account account = application.getAccount();
		offlineBalance.setText(Integer.toString(account.getBalance()));
	}



}

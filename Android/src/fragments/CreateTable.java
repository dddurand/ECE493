package fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import application.PokerApplication;

import com.example.bluetoothpoker.MainScreen;
import com.example.bluetoothpoker.R;

import dataModels.Account;

public class CreateTable extends Fragment implements OnClickListener {
	
	private View view;
	private PokerApplication application;
	private SharedPreferences preferences;
	private Account account;
	private EditText tableNameText;
	public static final String TITLE_HOLDER = "728a8372-9e83-47c0-998b-3b89eeca3278";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Load XML Layout into global variable
		this.view = inflater.inflate(R.layout.create_table_fragment,container, false);
		
		/******************Set listener for buttons******************/
		ImageButton createTableButton = (ImageButton) view.findViewById(R.id.createTableButton);
//		Button joinTableButton = (Button) view.findViewById(R.id.joinTableButton);
//		ImageButton offlineModeButton = (ImageButton) view.findViewById(R.id.offlineButton);
		createTableButton.setOnClickListener(this);
//		joinTableButton.setOnClickListener(this);
//		offlineModeButton.setOnClickListener(this);
		tableNameText = (EditText) view.findViewById(R.id.createTableNameField);
		preferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);
		this.application = (PokerApplication) this.getActivity().getApplication();
		account = application.getAccount();
		
		Log.e("USERNAME", account.getUsername());
		
		return view;
	}

	@Override
	public void onClick(View arg0) {
		//bluetooth stuff here
		String tableName = tableNameText.getText().toString();
		Intent intent = new Intent();
		intent.putExtra(CreateTable.TITLE_HOLDER,tableName);
		MainScreen ms = (MainScreen)getActivity();
		ms.setIntent(intent);
		ms.switchFragment(ms.WAIT_CLIENT);
		
	}

}

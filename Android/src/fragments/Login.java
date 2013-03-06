package fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.bluetoothpoker.*;

public class Login extends Fragment implements OnClickListener {
	
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 
		//Load XML Layout into global variable
		this.view = inflater.inflate(R.layout.login_fragment,container, false);
		
		/******************Set listener for buttons******************/
		ImageButton b = (ImageButton) view.findViewById(R.id.imageButton1);
		ImageButton registerButton = (ImageButton) view.findViewById(R.id.registerButton);
		ImageButton offlineModeButton = (ImageButton) view.findViewById(R.id.offlineButton);
		b.setOnClickListener(this);
		registerButton.setOnClickListener(this);
		offlineModeButton.setOnClickListener(this);
		
		return view;
	}
	
	//When imagebutton is pressed
	public void loginButtonAction(){
		//Get components from view
		EditText userField = (EditText) this.view.findViewById(R.id.usernameField);
		EditText passwordField = (EditText) this.view.findViewById(R.id.passwordField);
		//Get username and password
		String user_string=userField.getText().toString();
		String password_string=passwordField.getText().toString();
	}

	/**
	 * Methods for onClick listener.
	 */
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		
		/*****Login button action***/
		case R.id.imageButton1:
				this.loginButtonAction();
			break;
			
		/*****Offline mode button action***/
		case R.id.offlineButton:
			((MainScreen) getActivity()).switchFragment(MainScreen.OFFLINE_SCREEN);
			break;
		
		/*****Regster button action***/
		case R.id.registerButton:
			((MainScreen) getActivity()).switchFragment(MainScreen.REGISTER_SCREEN);
			break;
		}
		
	}

	
}

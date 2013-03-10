package fragments;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import Networking.NLogin;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bluetoothpoker.MainScreen;
import com.example.bluetoothpoker.R;

public class Login extends Fragment implements OnClickListener {
	
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 
		//Load XML Layout into global variable
		this.view = inflater.inflate(R.layout.login_fragment,container, false);
		
		/******************Set listener for buttons******************/
		ImageButton registerButton = (ImageButton) view.findViewById(R.id.registerButton);
		ImageButton offlineModeButton = (ImageButton) view.findViewById(R.id.offlineButton);
		ImageButton onlineModeButton = (ImageButton) view.findViewById(R.id.loginButton);
		//set listeners
		registerButton.setOnClickListener(this);
		offlineModeButton.setOnClickListener(this);
		onlineModeButton.setOnClickListener(this);
		
		return view;
	}
	
	//When imagebutton is pressed
	public void loginButtonAction(){
		
	}
	
	private boolean sendLoginRequest() throws JSONException, InterruptedException, ExecutionException, Exception {
		//Get components from view
		EditText userField = (EditText) this.view.findViewById(R.id.usernameField);
		EditText passwordField = (EditText) this.view.findViewById(R.id.passwordField);
		//Get username and password
		String userString=userField.getText().toString();
		String passwordString=passwordField.getText().toString();
		
		//Create JSON Object
		JSONObject obj = new JSONObject();
		obj.put("username", userString);
		obj.put("password", passwordString);
		
		//Execute class method for registering
		NLogin loginAction = new NLogin();
		loginAction.execute(obj);
		
		//Get response
		JSONObject response = loginAction.get();
		String responseSuccess = (String) response.get("Success");
		
		if (responseSuccess.compareTo("TRUE")==0) return true;
		else return false;
	}
	
	private void showLoginError(){
		//Get label first
		TextView label = (TextView)view.findViewById(R.id.loginErrorLabel);
		label.setText("Invalid Login Credentials.");
	}

	/**
	 * Methods for onClick listener.
	 */
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		
		/*****Offline mode button action***/
		case R.id.offlineButton:
			((MainScreen) getActivity()).switchFragment(MainScreen.OFFLINE_SCREEN);
			break;
		
		/*****Register button action***/
		case R.id.registerButton:
			((MainScreen) getActivity()).switchFragment(MainScreen.REGISTER_SCREEN);
			break;
			
		/*****Login Button******/	
		case R.id.loginButton:
			try {
				//Clear Label
				TextView label = (TextView)view.findViewById(R.id.loginErrorLabel);
				label.setText("");
				//Send request
				if (this.sendLoginRequest()) ;
				else this.showLoginError();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		
	}

	
}

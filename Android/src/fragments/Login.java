package fragments;

import java.util.concurrent.ExecutionException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import Networking.NLogin;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluetoothpoker.MainScreen;
import com.example.bluetoothpoker.PlayingArea;
import com.example.bluetoothpoker.R;

public class Login extends Fragment implements OnClickListener {
	
	private View view;
	
	private String username=null;
	private String password=null;
	private boolean isLoggedIn=false;
	
	@SuppressLint("ValidFragment")
	public Login(String username, String password, boolean loggedIn){
		this.username=username;
		this.password=password;
		this.isLoggedIn=loggedIn;
	}

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
		
		//Perform login button action if user is logged in
		if (this.isLoggedIn)
		{
			this.onClick(onlineModeButton);
		}
		
		return view;
	}
	
	private void sendLoginRequest(String userString, String passwordString) throws JSONException, InterruptedException, ExecutionException, ConnectTimeoutException {
		
		//Create JSON Object
		JSONObject obj = new JSONObject();
		obj.put("username", userString);
		obj.put("password", passwordString);
		
		//Execute class method for registering
		ProgressBar pb = (ProgressBar)this.view.findViewById(R.id.loginProgressBar);
		NLogin loginAction = new NLogin(pb,this);
		
		//Execute AsyncTask
		loginAction.execute(obj);
	}
	
	//This method will be executed on the onPostExecute method from the AsyncTask
	//Checks the response sent by the web service. If null then something was wrong with
	//the connection.
	public void onPostLoginRequest(JSONObject response){
		try {
			if (response!=null)
			{
				String responseSuccess = (String) response.get("Success");

				if (responseSuccess.compareTo("TRUE")==0) 
				{
					//Clear Label
					this.showLoginError("");
					//Modify Username and AuthTokebn in Main Screen Activity
					String authToken = (String) response.get("AuthenticationToken");
					MainScreen.setUsername(this.username);
					MainScreen.setAuthToken(authToken);
					//Switch fragments
					((MainScreen) getActivity()).switchFragment(MainScreen.ONLINE_MODE);
					
				} else this.showLoginError("Invalid Login Credentials");
			} 
			else this.showLoginError("Timeout. Please ensure you're connected to the Internet");

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	private void showLoginError(String s){
		//Get label first
		TextView label = (TextView)view.findViewById(R.id.loginErrorLabel);
		label.setText(s);
	}
	
	/**
	 * Checks if device is connected to a network. Returns true or false accordingly. 
	 * @return
	 */
	private boolean isConnectedInternet(){
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info!=null && info.isConnected());
	}
	
	/**
	 * Displays the given parameter in a short toast.
	 * @param text
	 */
	private void showToast(CharSequence text){
		Context context = getActivity().getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	/**
	 * Methods for onClick listener.
	 */
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		
		/*****Offline mode button action***/
		case R.id.offlineButton:
			//TODO Back door to playing area, remove!!
			//((MainScreen) getActivity()).switchFragment(MainScreen.OFFLINE_SCREEN);
			Intent i = new Intent(getActivity().getApplicationContext(),PlayingArea.class);
			//Add stuff here?
			startActivity(i);
			break;
		
		/*****Register button action***/
		case R.id.registerButton:
			((MainScreen) getActivity()).switchFragment(MainScreen.REGISTER_SCREEN);
			break;
			
		/*****Login Button******/	
		case R.id.loginButton:
			try {
				//Clear Label
				showLoginError("");
				String userString, passwordString;
				
				//Already logged in. Get username and password from class vars.
				if (this.isLoggedIn)
				{
					userString = this.username;
					passwordString = this.password;
				}
				//Not logged in. Get username and password from Views
					else 
					{
						//Get components from view only if user is not previously logged in
						EditText userField = (EditText) this.view.findViewById(R.id.usernameField);
						EditText passwordField = (EditText) this.view.findViewById(R.id.passwordField);
						//Get username and password (save username only)
						userString=userField.getText().toString();this.username=userString;
						passwordString=passwordField.getText().toString();
					}
				
				//Check for internet connection
			    if(isConnectedInternet()) {
					sendLoginRequest(userString,passwordString); 
			    } else showToast("You're offline. Please connect to a network before logging in.");
				
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				this.showLoginError("Connection Interrupted. Please try again");
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (ConnectTimeoutException e) {
				this.showLoginError("Timeout. Please ensure you're connected to the Internet");
			}
			break;
		}
		
	}

	
}

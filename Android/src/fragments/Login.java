package fragments;

import java.util.concurrent.ExecutionException;

import networking.NLogin;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
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
import com.example.bluetoothpoker.R;

public class Login extends Fragment implements OnClickListener {
	
	private View view;
	private String username;

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
	
	private void sendLoginRequest() throws JSONException, InterruptedException, ExecutionException, ConnectTimeoutException {
		//Get components from view
		EditText userField = (EditText) this.view.findViewById(R.id.usernameField);
		EditText passwordField = (EditText) this.view.findViewById(R.id.passwordField);
		//Get username and password (save username only)
		String userString=userField.getText().toString();this.username=userString;
		String passwordString=passwordField.getText().toString();
		
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
					String authToken = (String) response.get("authentificationToken");
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
	private boolean isConnectedWifi(){
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
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
			((MainScreen) getActivity()).switchFragment(MainScreen.OFFLINE_SCREEN);
			break;
		
		/*****Register button action***/
		case R.id.registerButton:
//			((MainScreen) getActivity()).switchFragment(MainScreen.REGISTER_SCREEN);
			//TODO Backdoor. REMOVE!!!
			MainScreen.setUsername("krodas");
			((MainScreen) getActivity()).switchFragment(MainScreen.ONLINE_MODE);
			break;
			
		/*****Login Button******/	
		case R.id.loginButton:
			try {
				//Clear Label
				showLoginError("");
				//Check for internet connection
			    if(isConnectedWifi()) {
					sendLoginRequest(); 
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

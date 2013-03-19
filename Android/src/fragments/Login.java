package fragments;

import java.util.concurrent.ExecutionException;

import networking.NLogin;
import networking.ServerCodes;
import networking.ServerCodes.Codes;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import application.PokerApplication;

import com.example.bluetoothpoker.MainScreen;
import com.example.bluetoothpoker.PlayingArea;
import com.example.bluetoothpoker.R;

import dataModels.Account;
import dataModels.MoneyGenerated;
import database.DatabaseDataSource;
import database.PreferenceConstants;

@SuppressLint("ValidFragment")
public class Login extends Fragment implements OnClickListener {
	
	private View view;
	
	private ServerCodes serverCodes;
	CheckBox rememberMeCheckBox;
	
	private PokerApplication application;
	private DatabaseDataSource dbInterface;
	private Account account;
	
	private boolean rememberMe = false;
	private boolean rememberUsername = false;
	
	SharedPreferences preferences;
	
	public Login(ServerCodes codes){
		this.serverCodes = codes;
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
		rememberMeCheckBox = (CheckBox) view.findViewById(R.id.remember_me_checkbox);
		
		//set listeners
		registerButton.setOnClickListener(this);
		offlineModeButton.setOnClickListener(this);
		onlineModeButton.setOnClickListener(this);
		
		application = (PokerApplication) (this.getActivity().getApplication());
		dbInterface = application.getDataSource();
		account = application.getAccount();
		preferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);

		//Perform login button action if user is logged in
		if (application.isLoggedIn())
		{
			this.onClick(onlineModeButton);
		}
		
		return view;
	}
	

	@Override
	public void onResume() {
		boolean isRememberedAccount = preferences.getBoolean(PreferenceConstants.IS_REMEMBERED_ACCOUNT, false);

		if(isRememberedAccount)
		{
			String username = preferences.getString(PreferenceConstants.REMEMBERED_USERNAME, "");
			Account account = dbInterface.getAccount(username);
			this.application.setAccount(account);
			this.application.setLoggedIn(true);
			((MainScreen) getActivity()).switchFragment(MainScreen.ONLINE_MODE);
		}
		
		boolean isRememberedUsername = preferences.getBoolean(PreferenceConstants.IS_REMEMBERED_USERNAME, false);
		
		if(isRememberedUsername)
		{
			String username = preferences.getString(PreferenceConstants.REMEMBERED_USERNAME, "");
			EditText userField = (EditText) this.view.findViewById(R.id.usernameField);
			userField.setText(username);
			
		}
		super.onResume();
	}
	
	private void sendLoginRequest(String userString, String passwordString) throws JSONException, InterruptedException, ExecutionException, ConnectTimeoutException {
		
		//Create JSON Object
		JSONObject obj = new JSONObject();
		obj.put("username", userString);
		obj.put("password", passwordString);
		
		account.setUsername(userString);
		account.setPassword(passwordString);
		application.setLoggedIn(false);
		
		//Execute class method for registering
		ProgressBar pb = (ProgressBar)this.view.findViewById(R.id.loginProgressBar);
		NLogin loginAction = new NLogin(pb,this);
		rememberMe = rememberMeCheckBox.isChecked();
		
		
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
				int code = response.getInt("Code");

				if (responseSuccess.compareTo("TRUE")==0 && code == Codes.SUCCESS) 
				{
					this.processSuccessfulLogin(response);
					//Hide Keyboard
					InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 
					inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
					
				} else this.showToast(serverCodes.getErrorMessage(code));
			} 
			else this.showToast("Timeout. Please ensure you're connected to the Internet");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Account account = application.getAccount();
		account.setPassword("");
	}
	
	private void processSuccessfulLogin(JSONObject response) throws JSONException
	{
		//Clear Label
		this.showLoginError("");
		
		Account account = application.getAccount();
		Editor editor = preferences.edit();
		
		if(dbInterface.accountExists(account))
		{
			account = dbInterface.getAccount(account.getUsername());
		}
		else
		{
			account.setBalance(application.MAX_GEN_BALANCE);
			dbInterface.updateAccount(account);
			MoneyGenerated moneyGen = new MoneyGenerated(account.getBalance(), account);
			dbInterface.addMoneyGenerated(moneyGen);
		}
		
		
		if(rememberMe)
		{
			editor.putBoolean(PreferenceConstants.IS_REMEMBERED_ACCOUNT, true);
			editor.putString(PreferenceConstants.REMEMBERED_USERNAME, account.getUsername());
			
		}
		
		//remember username
		if(rememberUsername)
		{
			editor.putBoolean(PreferenceConstants.IS_REMEMBERED_USERNAME, true);
			editor.putString(PreferenceConstants.REMEMBERED_USERNAME, account.getUsername());
		}
		
		editor.commit();
		
		String authToken = (String) response.get("AuthenticationToken");
		account.setAuthenticationToken(authToken);
		
		this.application.setAccount(account);
		dbInterface.updateAccount(account);
		
		
		//set auth in account
		//update account
		application.setLoggedIn(true);
		
		
		//Switch fragments
		((MainScreen) getActivity()).switchFragment(MainScreen.ONLINE_MODE);
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
		
		Account account;
		switch(v.getId()) {
		
		/*****Offline mode button action***/
		case R.id.offlineButton:
			//TODO Back door to playing area, remove!!
			
//			String offlineUsername = preferences.getString(PreferenceConstants.OFFLINE_USER_NAME, "OfflinePlayer");
//			int balance = preferences.getInt(PreferenceConstants.OFFLINE_BALANCE, 0);
//			account = new Account(offlineUsername, balance);
//			this.application.setAccount(account);
			
//			((MainScreen) getActivity()).switchFragment(MainScreen.OFFLINE_SCREEN);
			
			Intent i = new Intent(getActivity().getApplicationContext(),PlayingArea.class);
			//Add stuff here?
			startActivity(i);
			//((MainScreen) this.getActivity()).test();
			break;
		
		/*****Register button action***/
		case R.id.registerButton:
			Intent intent = new Intent(getActivity(),PlayingArea.class);
			startActivity(intent);
//TODO Remove this
//			((MainScreen) getActivity()).switchFragment(MainScreen.REGISTER_SCREEN);
			break;
			
		/*****Login Button******/	
		case R.id.loginButton:
			try {
				//Clear Label
				showLoginError("");
				String userString, passwordString;
				
				//Already logged in. Get username and password from class vars.
				if (application.isLoggedIn())
				{
					account = application.getAccount();
					userString = account.getUsername();
					passwordString = account.getPassword();
				}
				//Not logged in. Get username and password from Views
					else 
					{
						//Get components from view only if user is not previously logged in
						EditText userField = (EditText) this.view.findViewById(R.id.usernameField);
						EditText passwordField = (EditText) this.view.findViewById(R.id.passwordField);
						//Get username and password (save username only)
						userString=userField.getText().toString();
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

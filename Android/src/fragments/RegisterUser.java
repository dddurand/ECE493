package fragments;

import java.util.concurrent.ExecutionException;


import misc.GenericTextWatcher;
import networking.NRegister;
import networking.ServerCodes;
import networking.ServerCodes.Codes;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import application.PokerApplication;

import com.example.bluetoothpoker.MainScreen;
import com.example.bluetoothpoker.R;

import dataModels.Account;

@SuppressLint("ValidFragment")
public class RegisterUser extends Fragment implements OnClickListener {
	
	private View view;
	private PokerApplication application;
	private ServerCodes codes;
	
	public RegisterUser(ServerCodes codes)
	{
		this.codes = codes;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Load XML Layout into global variable
		this.view = inflater.inflate(R.layout.register_fragment,container, false);
		
		/******************Set listener for buttons******************/
		/***Set listener for button***/
		ImageButton registerButton = (ImageButton) view.findViewById(R.id.confirmRegistrationButton);
		registerButton.setOnClickListener(this);
		/***Set listener for password fields***/
		EditText passwordField = (EditText) view.findViewById(R.id.passwordField);
		passwordField.addTextChangedListener(new GenericTextWatcher(R.id.passwordField,view));
		EditText passwordConfirmField = (EditText) view.findViewById(R.id.passwordConfirmField);
		passwordConfirmField.addTextChangedListener(new GenericTextWatcher(R.id.passwordConfirmField,view));
		
		application = (PokerApplication) this.getActivity().getApplication();
		
		return view;
	}
	
	/**
	 * Method that creates a JSONObject and then sends it to the register server.
	 * @param username
	 * @param password
	 * @throws JSONException
	 */
	private boolean sendRegisterRequest(String username, String password) throws JSONException, InterruptedException, ExecutionException, ConnectTimeoutException {
		
		boolean validResponse=false;
		//Create JSON Object
		JSONObject obj = new JSONObject();
		obj.put("username", username);
		obj.put("password", password);
		
		//Execute class method for registering
		NRegister registerAction = new NRegister();
		
		//Get response
		JSONObject response = registerAction.execute(obj).get();
		
		//Check if response is valid
		if (response!=null)
		{
			String responseSuccess = (String) response.get("Success");
			int code = response.getInt("Code");
			
			if (responseSuccess.compareTo("TRUE")==0 && code == Codes.SUCCESS) {
				validResponse=true;
				Account account = new Account(username, null, -1);
				application.setAccount(account);
			}else
			{
				showToast(codes.getErrorMessage(code));
			}
				
		} else throw new ConnectTimeoutException();
		
		return validResponse;
	}
	
	/**
	 * For sending the JSON Object to webserver
	 */
	@Override
	public void onClick(View v) {
		//get text
		EditText newUsername = (EditText) view.findViewById(R.id.newUsernameField);
		EditText passwordField = (EditText) view.findViewById(R.id.passwordField);
		EditText confirmPasswordField = (EditText) view.findViewById(R.id.passwordConfirmField);
		
		String usernameStr = newUsername.getText().toString();
		String passwordStr = passwordField.getText().toString();
		String passwordConfirmStr = confirmPasswordField.getText().toString();
		
		try {
			//Check passwords first
			if (passwordStr.compareTo(passwordConfirmStr)!=0) showToast("Password mismatch. Please ensure both passwords are the same");
			else
				//Perform Request
				if (sendRegisterRequest(usernameStr,passwordStr)){
					//Successful if here
					Account account = new Account(usernameStr, null, -1);

					application.setAccount(account);
					account.setPassword(passwordStr);
					
					application.setLoggedIn(true);
					//Show toast
					showToast("New account created successfully");
					//Change screens
					((MainScreen) getActivity()).switchFragment(MainScreen.LOGIN_SCREEN);
				}
			
		} catch (JSONException e) {
			throw new RuntimeException(e);
		} catch (ConnectTimeoutException e) {
			showToast("Timeout. Please ensure you're connected to the Internet");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	private void showToast(CharSequence text){
		Context context = getActivity().getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

}

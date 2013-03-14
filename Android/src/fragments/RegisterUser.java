package fragments;

import java.util.concurrent.ExecutionException;

import misc.GenericTextWatcher;
import networking.NRegister;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

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

import com.example.bluetoothpoker.MainScreen;
import com.example.bluetoothpoker.R;

public class RegisterUser extends Fragment implements OnClickListener {
	
	private View view;

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
			
			if (responseSuccess.compareTo("TRUE")==0) {
				validResponse=true;
				MainScreen.setUsername(username);
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
		String usernameStr = newUsername.getText().toString();
		String passwordStr = passwordField.getText().toString();
		
		try {
			//Perform Request
			if (sendRegisterRequest(usernameStr,passwordStr)){
				//Successful if here
				MainScreen.setUsername(usernameStr);
				MainScreen.setPassword(passwordStr);
				MainScreen.setLoggedIn(true);
				//Show toast
				showToast("New account created successfully");
				//Change screens
				((MainScreen) getActivity()).switchFragment(MainScreen.LOGIN_SCREEN);
			}
			
			else showToast("Username "+usernameStr+" already taken.");
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

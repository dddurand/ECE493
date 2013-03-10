package fragments;

import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import Misc.GenericTextWatcher;
import Networking.NRegister;
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
	private void sendRegisterRequest(String username, String password) throws JSONException, InterruptedException, ExecutionException, Exception {
		
		//Create JSON Object
		JSONObject obj = new JSONObject();
		obj.put("username", username);
		obj.put("password", password);
		
		//Execute class method for registering
		NRegister registerAction = new NRegister();
		
		//Get response
		JSONObject response = registerAction.execute(obj).get();
		String responseSuccess = (String) response.get("Success");
		this.updateResponseLabel(responseSuccess,username);
		
		//Change to login screen here
		if (responseSuccess.compareTo("TRUE")==0) {
			MainScreen.setUsername(username);
			((MainScreen) getActivity()).switchFragment(MainScreen.ONLINE_MODE);
			//Clear back stack here?
		}
	}
	
	/**
	 * For sending the JSON Object to webserver
	 */
	@Override
	public void onClick(View v) {
		//get text
		EditText newUsername = (EditText) view.findViewById(R.id.newUsernameField);
		EditText passwordField = (EditText) view.findViewById(R.id.passwordField);
		
		try {
			this.sendRegisterRequest(newUsername.getText().toString(), passwordField.getText().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void updateResponseLabel(String status, String username){
		//get the label view
		TextView label = (TextView)this.view.findViewById(R.id.registerResponseLabel);
		if (status.compareTo("FALSE")==0) label.setText("Username '"+username+"' already taken."); 
		else label.setText("");
		
	}

}

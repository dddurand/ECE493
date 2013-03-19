package fragments;

import java.util.concurrent.ExecutionException;

import misc.AmountDialog;
import misc.BalanceUpdatable;
import networking.NLogout;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import application.PokerApplication;

import com.example.bluetoothpoker.MainScreen;
import com.example.bluetoothpoker.R;

import dataModels.Account;
import database.DatabaseDataSource;
import database.PreferenceConstants;

public class OnlineMode extends Fragment implements OnClickListener, BalanceUpdatable {
	
	private View view;
	private final String timeoutMessage = "Operation Timed Out. Please try again.";
	private PokerApplication application;
	private Account account;
	private SharedPreferences preferences;
	private DatabaseDataSource dbInterface;
	private TextView balanceLabel;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Load XML Layout into global variable
		this.view = inflater.inflate(R.layout.online_fragment,container, false);
		
		application = (PokerApplication) this.getActivity().getApplication();
		account = application.getAccount();
		preferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);
		dbInterface = this.application.getDataSource();
		
		/**Change User Label**/
		TextView usernameLabel = (TextView)view.findViewById(R.id.onlineModeUsername);
		usernameLabel.setText(account.getUsername());
		
		balanceLabel = (TextView)view.findViewById(R.id.onlineBalance);
		
		updateBalance();
		
		/******************Set listener for buttons******************/
		ImageButton logoutButton = (ImageButton) view.findViewById(R.id.logoutButton);
		ImageButton addFunds = (ImageButton) view.findViewById(R.id.addFundsButtonOnline);
//		ImageButton joinTable = (ImageButton) view.findViewById(id);
		
		
//		Button joinTableButton = (Button) view.findViewById(R.id.joinTableButton);
//		ImageButton offlineModeButton = (ImageButton) view.findViewById(R.id.offlineButton);
		logoutButton.setOnClickListener(this);
		addFunds.setOnClickListener(this);
//		joinTableButton.setOnClickListener(this);
//		offlineModeButton.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()){
		
		/**Logout Button**/
		case R.id.logoutButton:
			try {
				if (isConnectedInternet()) sendLogoutRequest();
				else showToast("Please connect to the Internet before logging out.");
			} catch (ConnectTimeoutException e) {
				showToast("Timeout. Please ensure you're connected to the Internet");
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			break;
			
		/*****Add funds button***/
		case R.id.addFundsButtonOnline:
			this.showAmountDialog("How much do you want to add to your balance?");
			break;
		}
		
	}
	
	private void showToast(CharSequence text){
		Context context = getActivity().getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	/**
	 * Checks if device is connected to a network. Returns true or false accordingly. 
	 * @return
	 */
	private boolean isConnectedInternet(){
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//		boolean result = (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) || (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected());
//		return cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info!=null && info.isConnected());
	}
	
	/**
	 * Creates and sends the JSONObject to the logout servlet.
	 * @throws JSONException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws ConnectTimeoutException
	 */
	private void sendLogoutRequest() throws JSONException, InterruptedException, ExecutionException, ConnectTimeoutException {
		//Create JSON Object
		JSONObject obj = new JSONObject();
		obj.put("username", account.getUsername());
		obj.put("authenticationToken", account.getAuthenticationToken());
		
		//Get ProgressBar
		ProgressBar pb = (ProgressBar)this.view.findViewById(R.id.logoutProgressBar);
		//Execute class method for registering
		NLogout logoutAction = new NLogout(this,pb);
		
		//Execute AsyncTask
		logoutAction.execute(obj);
	}
	
	//This method will be executed on the onPostExecute method from the AsyncTask
	//Checks the response sent by the web service. If null then something was wrong with
	//the connection.
	public void onPostLogoutRequest(JSONObject response){
		try {
			if (response!=null)
			{
				String responseSuccess;
				responseSuccess = (String) response.get("Success");

				if (responseSuccess.compareTo("TRUE")==0) 
				{
					//Switch fragments
					showToast("You have successfully logged out");
					//Tell mainscreen that user is no longer logged in
					application.setLoggedIn(false);
					
					Account account = this.application.getAccount();
					account.setAuthenticationToken("");
					
					dbInterface.updateAccount(account);

					this.application.getAccount().clear();
					
					Editor editor = preferences.edit();
					editor.putBoolean(PreferenceConstants.IS_REMEMBERED_ACCOUNT, false);
					editor.apply();
					
					((MainScreen) getActivity()).switchFragment(MainScreen.LOGIN_SCREEN);
				} else showToast("Logout Failed");
			} 
			else showToast(timeoutMessage);

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method for displaying a dialog for the funds and obtaining amount from user
	 */
	private void showAmountDialog(String message){	
		AmountDialog dialog = new AmountDialog(message, this.getActivity(), this);
		dialog.show();
	}
	
	@Override
	public void updateBalance() {
		Account account = application.getAccount();
		String balance = this.getString(R.string.current_balance_prefix) + account.getBalance();
		balanceLabel.setText(balance);
	}

}

package Misc;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.bluetoothpoker.R;

public class GenericTextWatcher implements TextWatcher {
	
	private View view;
	private int id;
	public static boolean passwordsMatch = false;
	public static boolean usernameAvailable = false;
	
	//Constructor
	public GenericTextWatcher(int id, View v){
		this.id=id;
		this.view=v;
	}
	

	@Override
	public void afterTextChanged(Editable arg0) {		
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
	}
	
	private void changeIconSrc(ImageView icon,boolean valid){
		icon.setVisibility(View.VISIBLE);
		
		if (valid)
			icon.setImageResource(R.drawable.ic_positive_green);
		else icon.setImageResource(R.drawable.ic_negative);
	}
	
	//Sets icon to green check-mark if both password fields' contents match
	private void updatePasswordIcon(String secondPassword){
		//Get the string in the first password field
		EditText passwordField = (EditText)view.findViewById(R.id.passwordField);
		String firstPassword = passwordField.getText().toString();
		//Compare
		if (secondPassword.compareTo(firstPassword)==0) passwordsMatch=true;
		else passwordsMatch=false;
		//Change icon
		this.changeIconSrc((ImageView)view.findViewById(R.id.validPasswordsIcon),passwordsMatch);
	}
	
	//Sets username icon to green mark if username is not taken.
	private void updateUsernameIcon(CharSequence text){
		//get imageview from view
		ImageView icon = (ImageView) view.findViewById(R.id.validUsernameIcon);
		if (text.length()%2==0) icon.setVisibility(View.VISIBLE);
		else icon.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		
		switch (this.id){
		
		case R.id.passwordConfirmField:
			this.updatePasswordIcon(arg0.toString());
			break;
			
		case R.id.newUsernameField:
			this.updateUsernameIcon(arg0.toString());
			break;
		}
	}

}

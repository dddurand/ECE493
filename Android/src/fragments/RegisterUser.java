package fragments;

import Misc.GenericTextWatcher;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.bluetoothpoker.R;

public class RegisterUser extends Fragment implements OnClickListener {
	
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Load XML Layout into global variable
		this.view = inflater.inflate(R.layout.register_fragment,container, false);
		//Get icons to modify later
//		validUsernameIcon = (ImageView) this.view.findViewById(R.id.validUsernameIcon);
		
		/******************Set listener for buttons******************/
//		ImageButton b = (ImageButton) view.findViewById(R.id.imageButton1);
//		ImageButton registerButton = (ImageButton) view.findViewById(R.id.registerButton);
//		ImageButton offlineModeButton = (ImageButton) view.findViewById(R.id.offlineButton);
//		b.setOnClickListener(this);
//		registerButton.setOnClickListener(this);
//		offlineModeButton.setOnClickListener(this);
		/***Set listener for username field***/
		EditText offlineUsername = (EditText) view.findViewById(R.id.newUsernameField);
		offlineUsername.addTextChangedListener(new GenericTextWatcher(R.id.newUsernameField,view));
		/***Set listener for password fields***/
		EditText passwordConfirmField = (EditText) view.findViewById(R.id.passwordConfirmField);
		passwordConfirmField.addTextChangedListener(new GenericTextWatcher(R.id.passwordConfirmField,view));
		
		
		return view;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		/**
		 * Register JSON stuff here. Ask Dustin
		 */
	}

}

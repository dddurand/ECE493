package fragments;

import com.example.bluetoothpoker.R;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

public class RegisterUser extends Fragment implements OnClickListener,TextWatcher {
	
	private View view;
	private ImageView validUsernameIcon;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Load XML Layout into global variable
		this.view = inflater.inflate(R.layout.register_fragment,container, false);
		//Get icons to modify later
		validUsernameIcon = (ImageView) this.view.findViewById(R.id.validUsernameIcon);
		
		/******************Set listener for buttons******************/
//		ImageButton b = (ImageButton) view.findViewById(R.id.imageButton1);
//		ImageButton registerButton = (ImageButton) view.findViewById(R.id.registerButton);
//		ImageButton offlineModeButton = (ImageButton) view.findViewById(R.id.offlineButton);
//		b.setOnClickListener(this);
//		registerButton.setOnClickListener(this);
//		offlineModeButton.setOnClickListener(this);
		/***Set listener for edittext***/
		EditText offlineUsername = (EditText) view.findViewById(R.id.newUsernameField);
		offlineUsername.addTextChangedListener(this);
		
		return view;
	}
	
	/**
	 * On text changed action
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
		if (s.length()==0)
			this.validUsernameIcon.setVisibility(View.INVISIBLE);
		else {
			this.validUsernameIcon.setVisibility(View.VISIBLE);
			if ((s.toString().length())%2==0)
				this.validUsernameIcon.setImageResource(R.drawable.ic_positive_green);
			else this.validUsernameIcon.setImageResource(R.drawable.ic_negative);
		
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}



}

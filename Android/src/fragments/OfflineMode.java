package fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.bluetoothpoker.R;

public class OfflineMode extends Fragment implements OnClickListener,TextWatcher {
	
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Load XML Layout into global variable
		this.view = inflater.inflate(R.layout.offline_fragment,container, false);
		
		/******************Set listener for buttons******************/
//		ImageButton b = (ImageButton) view.findViewById(R.id.imageButton1);
//		ImageButton registerButton = (ImageButton) view.findViewById(R.id.registerButton);
//		ImageButton offlineModeButton = (ImageButton) view.findViewById(R.id.offlineButton);
//		b.setOnClickListener(this);
//		registerButton.setOnClickListener(this);
//		offlineModeButton.setOnClickListener(this);
		/***Set listener for edittext***/
		EditText offlineUsername = (EditText) view.findViewById(R.id.offlineUsernameTextField);
		offlineUsername.addTextChangedListener(this);
		
		return view;
	}
	
	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		
		//arg0 provides the current string
		System.out.println("Change: "+arg0.toString());
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}



}

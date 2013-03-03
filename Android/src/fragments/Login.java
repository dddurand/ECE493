package fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.bluetoothpoker.R;

public class Login extends Fragment implements OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Load XML Layout
		View view = inflater.inflate(R.layout.login_fragment,container, false);
		//Set listener for button
		ImageButton b = (ImageButton) view.findViewById(R.id.imageButton1);
		b.setOnClickListener(this);
		
		return view;
	}
	
	//When imagebutton is pressed
	public void loginButtonAction(){
		
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		alert.setTitle("Window Size");
		alert.setMessage("Choose a new window size (odd number)");
		alert.show();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		
		/*****Login button action here***/
		case R.id.imageButton1:
				this.loginButtonAction();
			break;
		}
		
	}

	
}

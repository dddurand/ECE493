package fragments;

import com.example.bluetoothpoker.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class WaitingServer extends Fragment implements OnClickListener {

	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			//Load XML Layout into global variable
			this.view = inflater.inflate(R.layout.waitinggame,container, false);
			
			return view;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}

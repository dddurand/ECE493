package fragments;

import com.example.bluetoothpoker.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LocalPlayerFragment extends Fragment {
	
	private View view;
	
	public LocalPlayerFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Load XML Layout into global variable
		this.view = inflater.inflate(R.layout.local_player_fragment,container, false);
		
		return view;
	}


}

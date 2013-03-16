package fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bluetoothpoker.R;

public class PlayerFragment extends Fragment {
	
	private View view;
	private boolean local;
	
	public PlayerFragment(boolean local){
		this.local=local;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Load XML Layout into global variable
		if (this.local) this.view = inflater.inflate(R.layout.local_player_fragment,container, false);
		else this.view = inflater.inflate(R.layout.player_fragment,container, false);
		
		return view;
	}

}

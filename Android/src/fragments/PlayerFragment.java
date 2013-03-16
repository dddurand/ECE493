package fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.bluetoothpoker.R;

public class PlayerFragment extends Fragment {
	
	private View view;
	
	public PlayerFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Load XML Layout into global variable
		this.view = inflater.inflate(R.layout.player_fragment,container, false);
		
		return view;
	}
	
	public void update(){
		ImageView left = (ImageView)this.view.findViewById(R.id.leftCard);
		left.setImageResource(R.drawable.s5);
	}

}

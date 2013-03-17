package fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.bluetoothpoker.R;

public class River extends Fragment {
	
	private View view;
	
	public River(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Load XML Layout into global variable
		this.view = inflater.inflate(R.layout.river_fragment,container, false);
		
		return view;
	}
	
	/**
	 * Sets the card at position "pos" to the resource named "res".
	 * Variable pos ranges [0,4] and res is the name of the card.
	 * @param pos
	 * @param res
	 */
	public void setCard(int pos, String res){
		
		int id,resId;

		/********Get ImageView Resource*********/
		String viewName = "river"+Integer.toString(pos);
		id = getActivity().getResources().getIdentifier(viewName, "id", getActivity().getPackageName());
		
		/*********Get Drawable resource*********/
		resId = getActivity().getResources().getIdentifier(res, "drawable", getActivity().getPackageName());
		
		//Get ImageView and set image
		ImageView iv = (ImageView)this.view.findViewById(id);
		iv.setImageResource(resId);
	}


}

package fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bluetoothpoker.R;

/**
 * Fragment used for displaying other users connected in the same table
 * @SRS 3.2.1.9
 * @author kennethrodas
 *
 */

public class PlayerFragment extends Fragment {
	
	private View view;
	private boolean local;
	private String name;
	
	/**
	 * Constructor for fragment. If parameter is true, then it is instantiated as a local player,
	 * meaning a different (but awfully similar) layout will be loaded into the view.
	 * @param local
	 */
	public PlayerFragment(boolean local, String name){
		this.local=local;
		this.name=name;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Load XML Layout into global variable
		if (this.local) this.view = inflater.inflate(R.layout.local_player_fragment,container, false);
		else this.view = inflater.inflate(R.layout.player_fragment,container, false);
		
		this.setName(this.name);
		
		return view;
	}
	
	/**
	 * Sets the card at position "pos" to the resource named "res".
	 * Variable pos ranges [0,1] and res is the name of the card.
	 * Card position 0 is the left card, and 1 is the right card.
	 * @param pos
	 * @param res
	 */
	public void setCard(int pos, String res){
		
		int id,resId;
		String viewName;
		
		if (pos==0) viewName = "leftCard";
		else viewName = "rightCard";

		/********Get ImageView Resource*********/
		id = getActivity().getResources().getIdentifier(viewName, "id", getActivity().getPackageName());
		
		/*********Get Drawable resource*********/
		resId = getActivity().getResources().getIdentifier(res, "drawable", getActivity().getPackageName());
		
		//Get ImageView and set image
		ImageView iv = (ImageView)this.view.findViewById(id);
		iv.setImageResource(resId);
	}
	
	public void setName(String name){
		//No need to set resource if player is local
		if (!this.local)
		{
			TextView nameText = (TextView)view.findViewById(R.id.playerNameText);
			nameText.setText(name);
		}
	}
	
	public void setAmount(long amount){
		TextView moneyText = (TextView)view.findViewById(R.id.moneyAmountText);
		moneyText.setText(String.valueOf(amount));
	}

}

package fragments;
import com.example.bluetoothpoker.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * Fragment that is shown while the table waits to be populated
 * @author Lawton
 *
 */
public class WaitClient extends Fragment implements OnClickListener {

	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//Load XML Layout into global variable
		this.view = inflater.inflate(R.layout.list_client_fragment,container, false);
		
		/******************Set listener for buttons******************/
		ImageButton startTableButton = (ImageButton) view.findViewById(R.id.start_button);
		startTableButton.setEnabled(false);
//		Button joinTableButton = (Button) view.findViewById(R.id.joinTableButton);
//		ImageButton offlineModeButton = (ImageButton) view.findViewById(R.id.offlineButton);
		startTableButton.setOnClickListener(this);
//		joinTableButton.setOnClickListener(this);
//		offlineModeButton.setOnClickListener(this);
		return view;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}

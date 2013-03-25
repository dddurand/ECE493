package fragments;
import bluetooth.DiscoverableList;
import bluetooth.DiscoverableList.BluetoothInitializeException;

import com.example.bluetoothpoker.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

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
		Button startTableButton = (Button) view.findViewById(R.id.start_button);
		startTableButton.setEnabled(false);
		startTableButton.setOnClickListener(this);
		ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.device_name);

		ListView mListView = (ListView) view.findViewById(R.id.waiting_client);
		mListView.setAdapter(mArrayAdapter);
		DiscoverableList mDiscoverableList = new DiscoverableList(mArrayAdapter, null);
		if(!mDiscoverableList.checkBluetooth()) {
			Toast.makeText(getActivity(), "Error Bluetooth not supported", Toast.LENGTH_SHORT).show();
			return null;
		}
		try {
			mDiscoverableList.enableBluetooth(getActivity(),DiscoverableList.REQUEST_ENABLE_BT_SERVER);
			mDiscoverableList.makeDiscoverable(getActivity());
			mDiscoverableList.startServer();
		} catch (BluetoothInitializeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Button joinTableButton = (Button) view.findViewById(R.id.joinTableButton);
//		ImageButton offlineModeButton = (ImageButton) view.findViewById(R.id.offlineButton);
//		joinTableButton.setOnClickListener(this);
//		offlineModeButton.setOnClickListener(this);
		return view;
	}
	@Override
	public void onClick(View v) {
		// TODO Start game
		
	}

}

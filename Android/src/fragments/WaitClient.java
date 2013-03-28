package fragments;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Vector;

import game.Player;
import bluetooth.DiscoverableList;
import bluetooth.DiscoverableList.BluetoothInitializeException;
import bluetooth.ServerThread;

import com.example.bluetoothpoker.PlayingArea;
import com.example.bluetoothpoker.R;

import android.app.Fragment;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
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
import application.PokerApplication;

/**
 * Fragment that is shown while the table waits to be populated
 * @author Lawton
 *
 */
public class WaitClient extends Fragment implements OnClickListener {

	private View view;
	private Vector<ServerThread> mserver = new Vector<ServerThread>();
	ArrayAdapter<String> mArrayAdapter;
	String title = "untitle";
	public void setTitle (String title) {
		this.title = title;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//Load XML Layout into global variable
		this.view = inflater.inflate(R.layout.list_client_fragment,container, false);
		
		/******************Set listener for buttons******************/
		Button startTableButton = (Button) view.findViewById(R.id.start_button);
		startTableButton.setEnabled(false);
		startTableButton.setOnClickListener(this);
		this.mArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.device_name);

		ListView mListView = (ListView) view.findViewById(R.id.waiting_client);
		mListView.setAdapter(mArrayAdapter);
		DiscoverableList mDiscoverableList = new DiscoverableList(mArrayAdapter, null);
		if(!mDiscoverableList.checkBluetooth()) {
			Toast.makeText(getActivity(), "Error Bluetooth not supported", Toast.LENGTH_SHORT).show();
			return null;
		}
		try {
			mDiscoverableList.enableBluetooth(getActivity(),DiscoverableList.REQUEST_ENABLE_BT_SERVER);
			mDiscoverableList.makeDiscoverable(getActivity(),title);
			for (int i=0; i<DiscoverableList.MAX_CONNECTION;i++) {
				this.mserver.add(mDiscoverableList.startServer(startTableButton,i));
			}
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
		Intent intent = new Intent(getActivity(), PlayingArea.class);
		ObjectOutputStream[] outStream = new ObjectOutputStream[DiscoverableList.MAX_CONNECTION];
		ObjectInputStream[] inStream = new ObjectInputStream[DiscoverableList.MAX_CONNECTION];
		int test = mArrayAdapter.getCount();
		ArrayList<Player> otherPlayer = new ArrayList<Player>();
		for(int i =0; i<mArrayAdapter.getCount(); i++) {
			String msg = (String)mArrayAdapter.getItem(i);
			String info[] = msg.split("\\r?\\n");
			int pos = Integer.parseInt(info[2]) - 1;
			mserver.get(pos).sendStart();
			outStream[pos] = mserver.get(pos).getOutStream();
			inStream[pos] = mserver.get(pos).getInStream();
			otherPlayer.add(new Player(Integer.parseInt(info[2]), info[0], Integer.parseInt(info[1])));
			
		}
		intent.putExtra(DiscoverableList.IS_CLIENT, false);
		intent.putExtra(DiscoverableList.PLAYER_HOLDER, otherPlayer);
		
		//BluetoothSocket blueSocket[] = {mserver.getSocket()};
		//ObjectOutputStream outStream[] = {mserver.getOutStream()};
		//ObjectInputStream inStream[] = {mserver.getInStream()};
		//intent.putExtra(DiscoverableList.SOCKET_HOLDER, blueSocket);
		PokerApplication pokerApplication = (PokerApplication)getActivity().getApplication();
		//pokerApplication.setSocket(blueSocket);
		pokerApplication.setOutStream(outStream);
		pokerApplication.setInStream(inStream);
		//intent.putExtra(name, value)
		getActivity().startActivity(intent);
	}

}

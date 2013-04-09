package fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import application.PokerApplication;
import bluetooth.DiscoverableList;
import bluetooth.DiscoverableList.BluetoothInitializeException;

import com.example.bluetoothpoker.R;

import dataModels.Account;

public class JoinTable extends Fragment implements OnClickListener, OnItemClickListener {
	
	private View view;
	private ListView list;
	private int selectedPos = -1;
	private String tableName;
	private DiscoverableList mDiscoverableList;
	private String address=null;
	private String info=null;
	private Account account;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 
		//Load XML Layout into global variable
		this.view = inflater.inflate(R.layout.join_table_fragment,container, false);
		
		/***Populate List***/
		list = (ListView) view.findViewById(R.id.tablesList);
		list.setOnItemClickListener(this);
		
		/******************Set listener for buttons******************/
		Button joinButton = (Button) view.findViewById(R.id.joinButton);
		Button refreshButton = (Button) view.findViewById(R.id.refreshButton);
		joinButton.setOnClickListener(this);
		refreshButton.setOnClickListener(this);
		
		PokerApplication app = (PokerApplication)this.getActivity().getApplication();
		this.account = app.getAccount();
		
		return view;
	}
	
	private void refreshOpenTables(){
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.device_name);
		
		list.setAdapter(adapter);
		mDiscoverableList = new DiscoverableList(adapter, getActivity());
		if(!mDiscoverableList.checkBluetooth()) {
			Toast.makeText(getActivity(), "Error Bluetooth not supported", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			mDiscoverableList.enableBluetooth(getActivity(),DiscoverableList.REQUEST_ENABLE_BT_CLIENT);
			mDiscoverableList.setList(getActivity());
		} catch (BluetoothInitializeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDetach() {
		if(mDiscoverableList != null)
			mDiscoverableList.killThreads();
		super.onDetach();
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()){
		
		case R.id.joinButton:
			
			if(this.account.getBalance() <= 0)
			{
				Toast.makeText(this.getActivity(), R.string.zero_balance, Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(address==null){
				Toast.makeText(getActivity(), "Please select a device", Toast.LENGTH_SHORT).show();
			} else {
				mDiscoverableList.startClient(info, address);
			}
			break;
			
		case R.id.refreshButton:
			refreshOpenTables();
			break;
		
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
		//Save current position
 		this.selectedPos = pos;
 		info = (String)arg0.getAdapter().getItem(pos);
 		

        address = info.substring(info.length() - 17);
        //mDiscoverableList.startClient(info, address);
         /*
		//Save current position
		this.selectedPos = pos;
		//Save table name in string
		RelativeLayout rl = (RelativeLayout)v;
		TextView text = (TextView)rl.getChildAt(0);
		this.tableName=text.getText().toString();
		*/
	}

}

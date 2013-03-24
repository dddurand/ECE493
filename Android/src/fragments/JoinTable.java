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

import com.example.bluetoothpoker.R;

public class JoinTable extends Fragment implements OnClickListener, OnItemClickListener {
	
	private View view;
	private ListView list;
	private int selectedPos = -1;
	private String tableName;
	
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
		
		return view;
	}
	
	private void refreshOpenTables(){
		
		String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
				  "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
				  "Linux", "OS/2","Android", "iPhone", "WindowsMobile",
				  "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
				  "Linux", "OS/2" };
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.jointable_list_element, R.id.tableNameText,values);
		
		list.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()){
		
		case R.id.joinButton:
			Toast.makeText(getActivity(), tableName, Toast.LENGTH_SHORT).show();
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
		//Save table name in string
		RelativeLayout rl = (RelativeLayout)v;
		TextView text = (TextView)rl.getChildAt(0);
		this.tableName=text.getText().toString();
	}

}

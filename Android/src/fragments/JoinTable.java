package fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.bluetoothpoker.R;

public class JoinTable extends Fragment implements OnClickListener {
	
	
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 
		//Load XML Layout into global variable
		this.view = inflater.inflate(R.layout.join_table_fragment,container, false);
		
		/***Populate List***/
//		ListView list = (ListView) view.findViewById(R.id.tablesList);
//		String[] values = {"lol","wut","My little Pony","I Like turtles"};
		
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,null,null,values);
		
		
		/******************Set listener for buttons******************/
//		ImageButton b = (ImageButton) view.findViewById(R.id.imageButton1);
//		ImageButton registerButton = (ImageButton) view.findViewById(R.id.registerButton);
//		ImageButton offlineModeButton = (ImageButton) view.findViewById(R.id.offlineButton);
//		b.setOnClickListener(this);
//		registerButton.setOnClickListener(this);
//		offlineModeButton.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}

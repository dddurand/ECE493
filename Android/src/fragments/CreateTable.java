package fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.bluetoothpoker.R;

public class CreateTable extends Fragment implements OnClickListener {
	
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Load XML Layout into global variable
		this.view = inflater.inflate(R.layout.create_table_fragment,container, false);
		
		/******************Set listener for buttons******************/
		ImageButton createTableButton = (ImageButton) view.findViewById(R.id.createTableButton);
//		Button joinTableButton = (Button) view.findViewById(R.id.joinTableButton);
//		ImageButton offlineModeButton = (ImageButton) view.findViewById(R.id.offlineButton);
		createTableButton.setOnClickListener(this);
//		joinTableButton.setOnClickListener(this);
//		offlineModeButton.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onClick(View arg0) {
		//bluetooth stuff here
		
	}

}

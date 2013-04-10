package fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import application.PokerApplication;

import com.example.bluetoothpoker.MainScreen;
import com.example.bluetoothpoker.R;

public class WaitingServer extends Fragment implements OnClickListener {

	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			//Load XML Layout into global variable
			this.view = inflater.inflate(R.layout.waitinggame,container, false);
			
			return view;
	}
	
	@Override
	public void onClick(View v) {
		
	}
	
//	@Override
//	public void onPause(){
//		super.onPause();
//		//Get Fragment Manager
//		FragmentManager fm = getActivity().getFragmentManager();
//		//Get the last screen in the back stack
//		
//		
//		//get the type of account
//		PokerApplication app = (PokerApplication) (getActivity().getApplication());
//		if (app.getAccount().isOnline())
//		{
//			fm.popBackStack();fm.popBackStack();
//			//Tell the main screen we changed the screens
//			((MainScreen)getActivity()).setScreen(MainScreen.ONLINE_MODE);
//			
//		}
//		else {
//			fm.popBackStack();fm.popBackStack();
//			((MainScreen)getActivity()).setScreen(MainScreen.OFFLINE_SCREEN);
//		}
//		
//	}

}

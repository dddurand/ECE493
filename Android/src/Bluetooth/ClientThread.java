package bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;


import com.example.bluetoothpoker.MainScreen;
import com.example.bluetoothpoker.PlayingArea;
import com.example.bluetoothpoker.R;

import dataModels.Account;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import application.PokerApplication;


import fragments.JoinTable;
/**
 * 
 * @author Lawton
 *	waits on socket until interrupt or the server accepts connection
 */
	public class ClientThread extends AsyncTask<String, String, String>{
	    private BluetoothSocket mmSocket = null;
	    private final BluetoothDevice mDevice;
	    private ArrayList<UUID> mUuid = new ArrayList<UUID>();
		private String NAME = "BluetoothPoker";
		private BluetoothAdapter mBluetoothAdapter;
		private DiscoverableList mDiscoverableList;
		Activity mActivity;
		private PokerApplication pokerApp;
		PokerApplication application;
		Account account;
		private final UUID ServerhandshakeUUID = UUID.fromString("b98acff1-8557-4225-89aa-66f200a21765");
		private final UUID ClienthandshakeUUID = UUID.fromString("c36d53be-a1a5-4563-807a-6465115d1199");
		private final UUID startMsg = UUID.fromString("c860afe0-2877-4042-b618-721ab1609cb9");
		private TextView mText;
		private ObjectOutputStream[] outStream = new ObjectOutputStream[1];
		private ObjectInputStream[] inStream = new ObjectInputStream[1];
		private int pos;
		/**
		 * Basic constructor that creates socket from bluetooth device
		 * @param mBluetoothAdapter
		 * @param device
		 * @param mActivity
		 * @param mDiscoverabelList
		 */
	    public ClientThread(BluetoothAdapter mBluetoothAdapter, BluetoothDevice device, Activity mActivity, DiscoverableList mDiscoverableList) {
	    	mUuid.add(UUID.fromString("5bfeffb9-3fa3-4336-9e77-88620230d3bc"));
	        mUuid.add(UUID.fromString("296fa800-fe63-49f5-aa21-f7c405d70cff"));
	        mUuid.add(UUID.fromString("5d9c5a66-6daa-4e83-97b9-11f89af27fca"));
	        mUuid.add(UUID.fromString("624c879f-62f5-4c9e-93d3-de8366837c2e"));
	        mUuid.add(UUID.fromString("4d07c239-4760-4f09-8751-762d8a1b4cf3"));
	        //Change to a waiting on server
	        ((MainScreen)mActivity).switchFragment(MainScreen.WAIT_SERVER);
	        mText = (TextView)mActivity.findViewById(R.id.waiting_message);
	        this.mBluetoothAdapter = mBluetoothAdapter;
	        this.mActivity = mActivity;
	        this.pokerApp = (PokerApplication)this.mActivity.getApplication();
	        this.mDiscoverableList = mDiscoverableList;
	        // Use a temporary object that is later assigned to mmSocket,
	        // because mmSocket is final
	        BluetoothSocket tmp = null;
	        mDevice = device;
	        application = (PokerApplication) mActivity.getApplication();
			account = application.getAccount();
	    }
	 
	    /** Will cancel an in-progress connection, and close the socket */
	    public void cancel() {
	        try {
	        	if(mmSocket!=null) {
	        		mmSocket.close();
	        	}
	        } catch (IOException e) { }
	    }


		@Override
		protected String doInBackground(String... params) {
			 // Cancel discovery because it will slow down the connection
	        mBluetoothAdapter.cancelDiscovery();
	        //Try every socket
	        try {
	        	int i;
	        	for (i = 0; i < DiscoverableList.MAX_CONNECTION && mmSocket == null; i++) {
	        		for (int j = 0; j < 3 && mmSocket == null; j++) {
	        			mmSocket = mDevice.createRfcommSocketToServiceRecord(mUuid.get(i));
	        			try{
	        				mmSocket.connect();
	        			} catch(IOException connectionfail){
	        				mmSocket=null;
	        			}
	        			if (mmSocket == null) {
	        				try {
	        					Thread.sleep(200);
	        					} catch (InterruptedException e) {
	        						//Log.e("InterruptedException in connect", e);
	        					}
	                    }
	        		}
	        	}
	        	if(mmSocket==null) {
	        		return "BAD";
	        	} else {
	        		pos = i;
	        	}
	        	// Connect the device through the socket. This will block
	            // until it succeeds or throws an exception
	            //mmSocket.connect();
	        	
	        	this.pokerApp.addSocket(mmSocket);
	        	
	            publishProgress("wait");
	            OutputStream tmpOut = mmSocket.getOutputStream();
		        ObjectOutputStream streamOut = new ObjectOutputStream(tmpOut);
		        streamOut.flush();
		        InputStream tmpIn = mmSocket.getInputStream();
		        ObjectInputStream streamIn = new ObjectInputStream(tmpIn);
		        
            	
		        UUID tmp = (UUID)streamIn.readObject();
            	if(tmp.equals(ServerhandshakeUUID)) {
            		publishProgress("connected");
            		holder mHolder = new holder(account.getBalance(), account.getUsername());
	        		streamOut.writeObject(ClienthandshakeUUID);
	        		streamOut.flush();
            		streamOut.writeObject(mHolder);
	        		streamOut.flush();
	        		publishProgress("wait2");
	        		tmp = (UUID) streamIn.readObject();
	        		if(tmp.equals(this.startMsg)) {
	        			this.outStream[0] = streamOut;
	        			this.inStream[0] = streamIn;
	        			return "GO";
	        		}
	        		return "BAD";
            	}
	        } catch (IOException connectException) {
	            // Unable to connect; close the socket and get out
	            try {
	                mmSocket.close();
	            } catch (IOException closeException) { }
	            return "BAD";
	        } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
	        	 try {
		                mmSocket.close();
		            } catch (IOException closeException) { }
	        	e.printStackTrace();
				return "BAD";
			}
	 
	        // Do work to manage the connection (in a separate thread)
	        //manageConnectedSocket(mmSocket);
			return "BAD";
		}
		
		@Override
		protected void onProgressUpdate(String... params) {
			if(params[0].equals("wait")){
				Toast.makeText(mActivity, "Connected. Verifying server.", Toast.LENGTH_SHORT).show();
				//mText.setText("Connected. Verifying server.");
			}else if(params[0].equals("connected")) {
				Toast.makeText(mActivity, "Server verified. Sending credentials.", Toast.LENGTH_SHORT).show();
				//mText.setText("Server verified. Sending credentials");
			}else if(params[0].equals("wait2")){
				Toast.makeText(mActivity, "Waiting for server to start game.", Toast.LENGTH_SHORT).show();
				//mText.setText("Waiting for server to start game.");
			} else {
				//TextView  tv = (TextView) mActivity.findViewById(R.id.textView2);
				//tv.setText(params[0]);
			}
			//mActivity.setContentView(R.layout.sendstuff);
			//return null;
		}
		
		@Override
		protected void onPostExecute(String params) {
			if(params.equals("GO")) {
				//Server has started so start game on this end
				Intent intent = new Intent(mActivity, PlayingArea.class);
				intent.putExtra(DiscoverableList.IS_CLIENT, true);
				intent.putExtra(DiscoverableList.CLIENT_POS, pos);
				PokerApplication pokerApplication = (PokerApplication)mActivity.getApplication();
				pokerApplication.setInStream(inStream);
				pokerApplication.setOutStream(outStream);
				mActivity.startActivity(intent);
				//mDiscoverableList.connected(mmSocket);
			} else {
				cancel();
				//Bad connection
				Toast.makeText(mActivity, "Connection failed", Toast.LENGTH_SHORT).show();
				mActivity.onBackPressed();
				//mActivity.setContentView(R.layout.activity_main);
			}
		}
	}
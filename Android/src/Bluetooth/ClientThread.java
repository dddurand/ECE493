package bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.widget.TextView;


import fragments.JoinTable;
/**
 * 
 * @author Lawton
 *	waits on socket until interrupt or the server accepts connection
 */
	public class ClientThread extends AsyncTask<String, String, String>{
	    private final BluetoothSocket mmSocket;
	    private final BluetoothDevice mmDevice;
	    private ArrayList<UUID> mUuid = new ArrayList<UUID>();
		private String NAME = "BluetoothPoker";
		private BluetoothAdapter mBluetoothAdapter;
		private DiscoverableList mDiscoverableList;
		JoinTable mActivity;
		
	    public ClientThread(BluetoothAdapter mBluetoothAdapter, BluetoothDevice device, JoinTable mActivity, DiscoverableList mDiscoverabelList) {
	    	mUuid.add(UUID.fromString("5bfeffb9-3fa3-4336-9e77-88620230d3bc"));
	        mUuid.add(UUID.fromString("296fa800-fe63-49f5-aa21-f7c405d70cff"));
	        mUuid.add(UUID.fromString("5d9c5a66-6daa-4e83-97b9-11f89af27fca"));
	        mUuid.add(UUID.fromString("624c879f-62f5-4c9e-93d3-de8366837c2e"));
	        mUuid.add(UUID.fromString("4d07c239-4760-4f09-8751-762d8a1b4cf3"));
	        this.mBluetoothAdapter = mBluetoothAdapter;
	        this.mActivity = mActivity;
	        this.mDiscoverableList = mDiscoverableList;
	        // Use a temporary object that is later assigned to mmSocket,
	        // because mmSocket is final
	        BluetoothSocket tmp = null;
	        mmDevice = device;
	 
	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            // MY_UUID is the app's UUID string, also used by the server code
	            tmp = device.createRfcommSocketToServiceRecord(mUuid.get(0));
	        } catch (IOException e) { }
	        mmSocket = tmp;
	    }
	 
	    /** Will cancel an in-progress connection, and close the socket */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }


		@Override
		protected String doInBackground(String... params) {
			 // Cancel discovery because it will slow down the connection
	        mBluetoothAdapter.cancelDiscovery();
	 
	        try {
	            // Connect the device through the socket. This will block
	            // until it succeeds or throws an exception
	            mmSocket.connect();
	            publishProgress("wait");
		        InputStream tmpIn = mmSocket.getInputStream();
		        byte[] buffer = new byte[1024];
		        int bytes;
		        String msg;
		        //while (true) { 
		        bytes = tmpIn.read(buffer);
		        if (bytes!=-1) {
		        	//msg = new String(buffer, "UTF-8");
		        	msg = new String(buffer, 0, bytes, "UTF-8");
		        	if(msg.equals("GO!")) {
		        		//TODO Handshake send playername?
		        		return "GO";
		        		//publishProgress("GO");
		        	} //else {
		        		//publishProgress("GO");
		        	//}
		        	return msg;
		        }
		        	
		        //}
		            
	            //mActivity.setContentView(R.layout.sendstuff);
	        } catch (IOException connectException) {
	            // Unable to connect; close the socket and get out
	            try {
	                mmSocket.close();
	            } catch (IOException closeException) { }
	            return "BAD";
	        }
	 
	        // Do work to manage the connection (in a separate thread)
	        //manageConnectedSocket(mmSocket);
			return "BAD";
		}
		
		@Override
		protected void onProgressUpdate(String... params) {
			if(params[0].equals("wait")){
				//mActivity.setContentView(R.layout.waitinggame);
			}else if(params[0].equals("GO")){
				//mActivity.setContentView(R.layout.sendstuff);
				//mActivity.update("GO");
				mDiscoverableList.connected(mmSocket);
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
				//mActivity.setContentView(R.layout.sendstuff);
				//mActivity.update("GO");
				cancel();
			} else {
				//mActivity.setContentView(R.layout.activity_main);
			}
		}
	}
package bluetooth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import fragments.JoinTable;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Button;

/**
 * Listens on socket for clients then populates listview with them and enables start button
 * @author Lawton
 *
 */
public class ServerThread extends AsyncTask<String, BluetoothSocket, BluetoothDevice>{
		private final BluetoothServerSocket mmServerSocket;
		private ArrayList<UUID> mUuid = new ArrayList<UUID>();
		private String NAME = "BluetoothPoker";
		private DiscoverableList mdiscoverableList;
		private JoinTable mActivity;
		private BluetoothAdapter mBluetoothAdapter;
		private ArrayAdapter mArrayAdapter;
		
	public ServerThread(BluetoothAdapter mBluetoothAdapter, ArrayAdapter mArrayAdapter, JoinTable mActivity, DiscoverableList mDiscoverableList) {
			mUuid.add(UUID.fromString("5bfeffb9-3fa3-4336-9e77-88620230d3bc"));
	        mUuid.add(UUID.fromString("296fa800-fe63-49f5-aa21-f7c405d70cff"));
	        mUuid.add(UUID.fromString("5d9c5a66-6daa-4e83-97b9-11f89af27fca"));
	        mUuid.add(UUID.fromString("624c879f-62f5-4c9e-93d3-de8366837c2e"));
	        mUuid.add(UUID.fromString("4d07c239-4760-4f09-8751-762d8a1b4cf3"));
	        this.mActivity = mActivity;
	        this.mdiscoverableList = mDiscoverableList;
	        this.mBluetoothAdapter = mBluetoothAdapter;
	        this.mArrayAdapter = mArrayAdapter;
	        // Use a temporary object that is later assigned to mmServerSocket,
	        // because mmServerSocket is final
	        BluetoothServerSocket tmp = null;
	        try {
	            // MY_UUID is the app's UUID string, also used by the client code
	            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, mUuid.get(0));
	        } catch (IOException e) { }
	        mmServerSocket = tmp;
	    }
		@Override
		protected BluetoothDevice doInBackground(String... params) {
			BluetoothSocket socket = null;
			BluetoothDevice bdev = null;
	        // Keep listening until exception occurs or a socket is returned
	        while (true) {
	            try {
	                socket = mmServerSocket.accept();
	            } catch (IOException e) {
	                break;
	            }
	            // If a connection was accepted
	            if (socket != null) {
	            	//return socket;
	                // Do work to manage the connection (in a separate thread)
	                //manageConnectedSocket(socket);
	                try {
	                	publishProgress(socket);
						mmServerSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                break;
	            }
	        }
			
			return bdev;
		}

		@Override
		protected void onProgressUpdate(BluetoothSocket... params) {  
			//TODO
			mArrayAdapter.add(params[0].getRemoteDevice().getName() + "\n" + params[0].getRemoteDevice().getAddress());
			mArrayAdapter.notifyDataSetChanged();
			//Button start = (Button) mActivity.findViewById(R.id.start_button);
			//start.setEnabled(true);
			this.mdiscoverableList.connected(params[0]);
		}
		
		@Override
		protected void onPostExecute(BluetoothDevice params) {  
			//TODO
			//connected(socket, socket.getRemoteDevice());
			//mArrayAdapter.add(params.getName() + "\n" + params.getAddress());
			//mArrayAdapter.notifyDataSetChanged();
			
		}
	    /** Will cancel the listening socket, and cause the thread to finish */
	    public void cancel() {
	        try {
	            mmServerSocket.close();
	        } catch (IOException e) { }
	    }
	}
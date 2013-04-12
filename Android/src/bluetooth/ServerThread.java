package bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import application.PokerApplication;

import com.example.bluetoothpoker.MainScreen;

/**
 * Listens on socket for clients then populates listview with them and enables start button
 * @author Lawton
 *
 */
public class ServerThread extends AsyncTask<String, holder, BluetoothSocket>{
		private final BluetoothServerSocket mmServerSocket;
		private ArrayList<UUID> mUuid = new ArrayList<UUID>();
		private String NAME = "BluetoothPoker";
		@SuppressWarnings("unused")
		private DiscoverableList mdiscoverableList;
		private Activity mActivity;
		private PokerApplication pokerApp;
		@SuppressWarnings("unused")
		private BluetoothAdapter mBluetoothAdapter;
		private ArrayAdapter<String> mArrayAdapter;
		private final UUID ServerhandshakeUUID = UUID.fromString("b98acff1-8557-4225-89aa-66f200a21765");
		private final UUID ClienthandshakeUUID = UUID.fromString("c36d53be-a1a5-4563-807a-6465115d1199");
		private final UUID startMsg = UUID.fromString("c860afe0-2877-4042-b618-721ab1609cb9");
		private Button startButton;
		private ObjectOutputStream streamOut =null;
		private ObjectInputStream streamIn = null;
		private BluetoothSocket blueSocket;
		private String entry;
		private int pos;
		
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ServerThread(BluetoothAdapter mBluetoothAdapter, ArrayAdapter mArrayAdapter, Activity mActivity, DiscoverableList mDiscoverableList, Button startButton, int pos) {
			mUuid.add(UUID.fromString("5bfeffb9-3fa3-4336-9e77-88620230d3bc"));
	        mUuid.add(UUID.fromString("296fa800-fe63-49f5-aa21-f7c405d70cff"));
	        mUuid.add(UUID.fromString("5d9c5a66-6daa-4e83-97b9-11f89af27fca"));
	        mUuid.add(UUID.fromString("624c879f-62f5-4c9e-93d3-de8366837c2e"));
	        mUuid.add(UUID.fromString("4d07c239-4760-4f09-8751-762d8a1b4cf3"));
	        this.mActivity = mActivity;
	        pokerApp = (PokerApplication)this.mActivity.getApplication();
	        this.mdiscoverableList = mDiscoverableList;
	        this.mBluetoothAdapter = mBluetoothAdapter;
	        this.mArrayAdapter = mArrayAdapter;
	        this.startButton = startButton;
	        this.pos = pos;
	        // Use a temporary object that is later assigned to mmServerSocket,
	        // because mmServerSocket is final
	        BluetoothServerSocket tmp = null;
	        try {
	            // MY_UUID is the app's UUID string, also used by the client code
	            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, mUuid.get(pos));
	        } catch (IOException e) {
	        	
	        	//Log.e("listenUsingRfcommWithServiceRecord", "?", e);
	        	
	        	MainScreen ac = (MainScreen) this.mActivity;
	        	
	        	if(ac.getScreen() == MainScreen.WAIT_CLIENT)
	        	{
	        		Toast.makeText(this.mActivity, "Android Bluetooth System Failure", Toast.LENGTH_LONG).show();
	        		ac.onBackPressed();
	        	}
	        	
	        	mmServerSocket = null;
	        	return;
	        	
	        }
	        
	        mmServerSocket = tmp;
	        
	        
	    }
		@SuppressWarnings("unused")
		@Override
		protected BluetoothSocket doInBackground(String... params) {
			
			if(mmServerSocket == null)
			{
				return null;
			}
			
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
	                	OutputStream tmpOut = socket.getOutputStream();
	                	ObjectOutputStream streamOut = new ObjectOutputStream(tmpOut);
	                	streamOut.flush();
	                	InputStream tmpIn = socket.getInputStream();
	                	ObjectInputStream streamIn = new ObjectInputStream(tmpIn);
	                	streamOut.writeObject(ServerhandshakeUUID);
	                	UUID tmp = (UUID)streamIn.readObject();
	                	if(tmp.equals(ClienthandshakeUUID)) {
	                		holder mholder = (holder)streamIn.readObject();
	                		publishProgress(mholder);
	                		this.blueSocket = socket;
	                		this.streamOut = streamOut;
	                		this.streamIn = streamIn;
							mmServerSocket.close();
							
							this.pokerApp.addSocket(this.blueSocket);
							
							return socket;
	                	}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch(ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	                break;
                	}
	            }
	        return null;
	        }

		@Override
		protected void onProgressUpdate(holder... params) {  
			int rpos = pos+1;
			entry = params[0].getName() + "\n" + params[0].getBalance() + "\n" + rpos;
			mArrayAdapter.add(entry);
			mArrayAdapter.notifyDataSetChanged();
			//Button start = (Button) mActivity.findViewById(R.id.start_button);
			startButton.setEnabled(true);
			
		}
		public BluetoothSocket getSocket() {
			return this.blueSocket;
		}
		public ObjectOutputStream getOutStream() {
			return this.streamOut;
		}
		public ObjectInputStream getInStream() {
			return this.streamIn;
		}
		public void sendStart() {
			try {
				this.streamOut.writeObject(this.startMsg);
				this.streamOut.flush();
			} catch (IOException e) {
				Toast.makeText(mActivity, "Error sending start", Toast.LENGTH_SHORT).show();
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
		protected void onPostExecute(BluetoothSocket params) {
			try {
				mmServerSocket.close();
			} catch (Exception e) {}
			//this.mdiscoverableList.connected(params);
			//TODO
			//connected(socket, socket.getRemoteDevice());
			//mArrayAdapter.add(params.getName() + "\n" + params.getAddress());
			//mArrayAdapter.notifyDataSetChanged();
			
		}
	    /** Will cancel the listening socket, and cause the thread to finish */
	    public void cancel() {
	        try {
	            mmServerSocket.close();
	        } catch (Exception e) { }
	    }
	}
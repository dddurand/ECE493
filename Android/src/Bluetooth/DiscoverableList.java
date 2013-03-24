package bluetooth;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * with help from http://developer.android.com/guide/topics/connectivity/bluetooth.html#DiscoveringDevices
 * @author Lawton
 *
 */
public class DiscoverableList {
	
	private static int WAIT_TIME = 3600;
	private ArrayAdapter<String> mArrayAdapter;
	public final static int REQUEST_ENABLE_BT_SERVER = 50;
	public final static int REQUEST_ENABLE_BT_CLIENT =51;
	public static final String EXTRA_DEVICE_ADDRESS = "device_address";
	private Vector<BluetoothSocket> mSockets = new Vector<BluetoothSocket>();
	//private ConnectedThread mConnection;
	private int mState;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
	
    
    private synchronized void setState(int state) {
        mState = state;
        // Give the new state to the Handler so the UI Activity can update
        //mHandler.obtainMessage(BluetoothChat.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }
    
    private synchronized void addSocket(BluetoothSocket socket) {
    	mSockets.add(socket);
    }
    
	private BluetoothAdapter BlueAdapt=null;
	// Create a BroadcastReceiver for ACTION_FOUND
	
	public DiscoverableList(ArrayAdapter<String> arrayAdapter) {
		mArrayAdapter=arrayAdapter;
	}
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            // Get the BluetoothDevice object from the Intent
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            // Add the name and address to an array adapter to show in a ListView
	            mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	        }
	        if(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {
	        	//TODO if discoverable set to off
	        }
	    }
	    
	};
	
	
	public boolean checkBluetooth() {
		BlueAdapt = BluetoothAdapter.getDefaultAdapter();
		if (BlueAdapt == null) {
		    return false;
		}
		return true;
	}
	public void enableBluetooth(Activity a, int identifier) throws BluetoothInitializeException {
		if(BlueAdapt==null) {
			throw new BluetoothInitializeException();
		}
		if (!BlueAdapt.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    a.startActivityForResult(enableBtIntent, identifier);
		}
	}
	public void makeDiscoverable(Context c) {
		Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, WAIT_TIME);
		c.startActivity(discoverableIntent);
	}
	
	
	public void startServer() {
		ServerThread mServer = new ServerThread(BlueAdapt);
		mServer.run();
	}
	
	public void startClient(Intent data) {
		 // Get the device MAC address
        String address = data.getExtras().getString(EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = BlueAdapt.getRemoteDevice(address);
		ClientThread mClient = new ClientThread(BlueAdapt, device);
	}
	
	public void startConnection(BluetoothSocket bluetoothSocket) {
		
	}
	
	public void setList(Context c) {
		if(BlueAdapt.isDiscovering()) {
			BlueAdapt.cancelDiscovery();
		}
		
		BlueAdapt.startDiscovery();
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		c.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
	}
	
	public void destroyList (Context c) {
		BlueAdapt.cancelDiscovery();
		c.unregisterReceiver(mReceiver);
	}
	
	public class BluetoothInitializeException extends Exception {
		public BluetoothInitializeException() {
			super("The bluetooth adapter was not set. Must call checkBluetooth before calling enableBluetooth.");
		}
	}

	private class ServerThread extends Thread{
		private final BluetoothServerSocket mmServerSocket;
		private ArrayList<UUID> mUuid;
		private String NAME = "BluetoothPoker";
		private DiscoverableList mdiscoverableList;
		
		private ServerThread(BluetoothAdapter mBluetoothAdapter) {
			mUuid.add(UUID.fromString("5bfeffb9-3fa3-4336-9e77-88620230d3bc"));
	        mUuid.add(UUID.fromString("296fa800-fe63-49f5-aa21-f7c405d70cff"));
	        mUuid.add(UUID.fromString("5d9c5a66-6daa-4e83-97b9-11f89af27fca"));
	        mUuid.add(UUID.fromString("624c879f-62f5-4c9e-93d3-de8366837c2e"));
	        mUuid.add(UUID.fromString("4d07c239-4760-4f09-8751-762d8a1b4cf3"));
	        // Use a temporary object that is later assigned to mmServerSocket,
	        // because mmServerSocket is final
	        BluetoothServerSocket tmp = null;
	        try {
	            // MY_UUID is the app's UUID string, also used by the client code
	            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, mUuid.get(0));
	        } catch (IOException e) { }
	        mmServerSocket = tmp;
	    }
		
		public void run() {
	        BluetoothSocket socket = null;
	        // Keep listening until exception occurs or a socket is returned
	        while (true) {
	            try {
	                socket = mmServerSocket.accept();
	            } catch (IOException e) {
	                break;
	            }
	            // If a connection was accepted
	            if (socket != null) {
	                // Do work to manage the connection (in a separate thread)
	                //manageConnectedSocket(socket);
	                try {
	                	synchronized(DiscoverableList.this) {
	                		connected(socket, socket.getRemoteDevice());
	                		/* 
	                		switch (mState) {
	                         case STATE_LISTEN:
	                         case STATE_CONNECTING:
	                             // Situation normal. Start the connected thread.
	                             
	                        	 connected(socket, socket.getRemoteDevice());
	                             break;
	                         case STATE_NONE:
	                         case STATE_CONNECTED:
	                             // Either not ready or already connected. Terminate new socket.
	                             try {
	                                 socket.close();
	                             } catch (IOException e) {
	                                 //Log.e(TAG, "Could not close unwanted socket", e);
	                             }
	                             break;
	                         }
	                         */
	                	}
						mmServerSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                break;
	            }
	        }
	    }
	 
	    /** Will cancel the listening socket, and cause the thread to finish */
	    public void cancel() {
	        try {
	            mmServerSocket.close();
	        } catch (IOException e) { }
	    }
	}
	
	
	private class ClientThread extends Thread{
	    private final BluetoothSocket mmSocket;
	    private final BluetoothDevice mmDevice;
	    private ArrayList<UUID> mUuid;
		private String NAME = "BluetoothPoker";
		private BluetoothAdapter mBluetoothAdapter;
		private DiscoverableList mDiscoverableList;
		
	    private ClientThread(BluetoothAdapter mBluetoothAdapter, BluetoothDevice device) {
	    	mUuid.add(UUID.fromString("5bfeffb9-3fa3-4336-9e77-88620230d3bc"));
	        mUuid.add(UUID.fromString("296fa800-fe63-49f5-aa21-f7c405d70cff"));
	        mUuid.add(UUID.fromString("5d9c5a66-6daa-4e83-97b9-11f89af27fca"));
	        mUuid.add(UUID.fromString("624c879f-62f5-4c9e-93d3-de8366837c2e"));
	        mUuid.add(UUID.fromString("4d07c239-4760-4f09-8751-762d8a1b4cf3"));
	        this.mBluetoothAdapter = mBluetoothAdapter;
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


		public void run() {
	        // Cancel discovery because it will slow down the connection
	        mBluetoothAdapter.cancelDiscovery();
	 
	        try {
	            // Connect the device through the socket. This will block
	            // until it succeeds or throws an exception
	            mmSocket.connect();
	        } catch (IOException connectException) {
	            // Unable to connect; close the socket and get out
	            try {
	                mmSocket.close();
	            } catch (IOException closeException) { }
	            return;
	        }
	 
	        // Do work to manage the connection (in a separate thread)
	        //manageConnectedSocket(mmSocket);
	        
	    }
	 
	    /** Will cancel an in-progress connection, and close the socket */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	}
	
		
		private class ConnectedThread extends Thread {
		    private final BluetoothSocket mmSocket;
		    private final InputStream mmInStream;
		    private final OutputStream mmOutStream;
		 
		    private ConnectedThread(BluetoothSocket socket) {
		        mmSocket = socket;
		        InputStream tmpIn = null;
		        OutputStream tmpOut = null;
		 
		        // Get the input and output streams, using temp objects because
		        // member streams are final
		        try {
		            tmpIn = socket.getInputStream();
		            tmpOut = socket.getOutputStream();
		        } catch (IOException e) { }
		 
		        mmInStream = tmpIn;
		        mmOutStream = tmpOut;
		    }
		 
		    public void run() {
		        byte[] buffer = new byte[1024];  // buffer store for the stream
		        int bytes; // bytes returned from read()
		 
		        // Keep listening to the InputStream until an exception occurs
		        while (true) {
		            try {
		                // Read from the InputStream
		                bytes = mmInStream.read(buffer);
		                // Send the obtained bytes to the UI activity
		                //mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
		                  //      .sendToTarget();
		            } catch (IOException e) {
		                break;
		            }
		        }
		    }
		 
		    /* Call this from the main activity to send data to the remote device */
		    public void write(byte[] bytes) {
		        try {
		            mmOutStream.write(bytes);
		        } catch (IOException e) { }
		    }
		 
		    /* Call this from the main activity to shutdown the connection */
		    public void cancel() {
		        try {
		            mmSocket.close();
		        } catch (IOException e) { }
		    }
		}


	public synchronized void connected(BluetoothSocket socket, BluetoothDevice remoteDevice) {
		// TODO Auto-generated method stub
		mArrayAdapter.add(remoteDevice.getName() + "\n" + remoteDevice.getAddress());
		mArrayAdapter.notifyDataSetChanged();
		ConnectedThread connection = new ConnectedThread(socket);
		connection.run();
		addSocket(socket);
		
		
	}
}
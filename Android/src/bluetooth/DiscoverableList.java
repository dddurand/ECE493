package bluetooth;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

import fragments.JoinTable;


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
import android.graphics.BlurMaskFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import application.PokerApplication;

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
	public static final String CLIENT_POS = "client_position";
	
	private Vector<BluetoothSocket> mSockets = new Vector<BluetoothSocket>();
	private Vector<ServerThread> serverThreads = new Vector<ServerThread>();
	private Vector<ClientThread> clientThreads = new Vector<ClientThread>();
	private Vector<ConnectedThread> connectedThreads = new Vector<ConnectedThread>();
	public static final String PLAYER_HOLDER ="1f3d5846-83e5-4502-a998-c2c936e756f6";
	public static final String SOCKET_HOLDER ="dcff30c6-0de7-4345-a3a1-a7fcf77d6b7f";
	public static final String IS_CLIENT = "4d3b1623-8bfd-40f4-be5a-1c084b8c8e0a";
	public static int MAX_CONNECTION=5;
	//private ConnectedThread mConnection;
	private int mState;
	private Activity mActivity;
	private int mType =0;

	public static final int TYPE_NONE=0;
    public static final int TYPE_SERVER =1;
    public static final int TYPE_CLIENT=2;
	// Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
	
    
    public void writeAll(byte[] msg) {
    	for(int i=0; i<connectedThreads.size();i++) {
    		connectedThreads.get(i).write(msg);
    	}
    }
    private synchronized void setState(int state) {
        mState = state;
        // Give the new state to the Handler so the UI Activity can update
        //mHandler.obtainMessage(BluetoothChat.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }
    
    public void addSocket(BluetoothSocket socket) {
    	mSockets.add(socket);
    }
    
	private BluetoothAdapter BlueAdapt=null;
	// Create a BroadcastReceiver for ACTION_FOUND
	
	public DiscoverableList(ArrayAdapter<String> arrayAdapter, Activity mActivity) {
		this.mActivity= mActivity ;
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
	        if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
	        	//TODO if discoverable set to off
	        }
	        if(BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
	        	BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	        		//Toast.makeText(mActivity, "WE GOT A DISCONNECT!", Toast.LENGTH_SHORT).show();


	        	
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
	
	public void makeDiscoverable(Context c, String title) {
		BlueAdapt.setName(title);
		Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, WAIT_TIME);
		c.startActivity(discoverableIntent);
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		c.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
	}
	
	
	public ServerThread startServer(Button startButton, int pos) {
		mType =TYPE_SERVER;
		ServerThread mServer = new ServerThread(BlueAdapt, mArrayAdapter, mActivity, this, startButton, pos);
		mServer.execute("");
		this.serverThreads.add(mServer);
		return mServer;
	}
	
	public void startClient(String info, String address) {
		mType =TYPE_CLIENT;
		// Cancel discovery because it's costly and we're about to connect
        BlueAdapt.cancelDiscovery();

        BluetoothDevice device = BlueAdapt.getRemoteDevice(address);
        ClientThread mClient = new ClientThread(BlueAdapt, device, mActivity,DiscoverableList.this);
        mClient.execute("");
	}
	
	
	public void setList(Context c) {
		if(BlueAdapt.isDiscovering()) {
			BlueAdapt.cancelDiscovery();
		}
		
		BlueAdapt.startDiscovery();
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		c.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
	}
	
	public void destroyList (Context c) {
		BlueAdapt.cancelDiscovery();
		c.unregisterReceiver(mReceiver);
		killThreads();
	}
	
	public void killThreads() {
		BlueAdapt.cancelDiscovery();
		
		try{
		this.mActivity.unregisterReceiver(mReceiver);
		}catch(Exception e){}
		
		// TODO Auto-generated method stub
		for (int i=0; i<this.clientThreads.size();i++) {
			clientThreads.get(i).cancel();
		}
		for (int i=0; i<this.serverThreads.size();i++) {
			serverThreads.get(i).cancel();
		}
		for (int i=0; i<this.connectedThreads.size();i++) {
			connectedThreads.get(i).cancel();
		}	
	}
	
	public static void closeBluetoothSockets(Activity ac)
	{
		PokerApplication pokerApp = (PokerApplication)ac.getApplication();
		Vector<BluetoothSocket> mSockets = pokerApp.getSockets();
		for(BluetoothSocket socket : mSockets)
		{
			try{
				socket.close();
			}catch(Exception e){}
		}
		
		mSockets.clear();
		
	}
	
	public void sendStart() {
		try {
			byte[] msg = "GO!".getBytes("UTF-8");
			writeAll(msg);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public class BluetoothInitializeException extends Exception {
		public BluetoothInitializeException() {
			super("The bluetooth adapter was not set. Must call checkBluetooth before calling enableBluetooth.");
		}
	}
	public void connected(BluetoothSocket socket) {
		// TODO Auto-generated method stub
		//mArrayAdapter.add(remoteDevice.getName() + "\n" + remoteDevice.getAddress());
		//mArrayAdapter.notifyDataSetChanged();
		ConnectedThread connection = new ConnectedThread(socket, mActivity);
		this.connectedThreads.add(connection);
		connection.execute();
		addSocket(socket);
		
		
	}
	 // The on-click listener for all devices in the ListViews
    public OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            BlueAdapt.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            BluetoothDevice device = BlueAdapt.getRemoteDevice(address);
            ClientThread mClient = new ClientThread(BlueAdapt, device, mActivity,DiscoverableList.this);
            mClient.execute("");
        }
    };
}
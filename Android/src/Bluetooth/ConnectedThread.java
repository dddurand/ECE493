package bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import fragments.JoinTable;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.widget.TextView;

/**
 * Used for established connections continually listening for new messages
 * @author Lawton
 *
 */
public class ConnectedThread extends AsyncTask<String, String, Void> {
		    private final BluetoothSocket mmSocket;
		    private final InputStream mmInStream;
		    private final OutputStream mmOutStream;
			private Activity mActivity;
		 
		   public ConnectedThread(BluetoothSocket socket, Activity mActivity) {
		        mmSocket = socket;
		        InputStream tmpIn = null;
		        OutputStream tmpOut = null;
		        this.mActivity = mActivity;
		 
		        // Get the input and output streams, using temp objects because
		        // member streams are final
		        try {
		            tmpIn = socket.getInputStream();
		            tmpOut = socket.getOutputStream();
		        } catch (IOException e) { }
		 
		        mmInStream = tmpIn;
		        mmOutStream = tmpOut;
		    }
		 
		    /* Call this from the main activity to send data to the remote device */
		    public void write(byte[] bytes) {
		        try {
		            mmOutStream.write(bytes);
		            //mmOutStream.flush();
		        } catch (IOException e) { }
		    }
		 
		    /* Call this from the main activity to shutdown the connection */
		    public void cancel() {
		        try {
		            mmSocket.close();
		        } catch (IOException e) { }
		    }

			@Override
			protected Void doInBackground(String... params) {
		        byte[] buffer = new byte[1024];  // buffer store for the stream
		        int bytes; // bytes returned from read()
		 
		        // Keep listening to the InputStream until an exception occurs
		        while (true) {
		            try {
		                // Read from the InputStream
		            	
		                bytes = mmInStream.read(buffer);
		                String msg = new String(buffer, 0, bytes, "UTF-8");
		                publishProgress(msg);
		                // Send the obtained bytes to the UI activity
		                //mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
		                  //      .sendToTarget();
		            } catch (IOException e) {
		                break;
		            }
		        }
				return null;
			}
			
			@Override
			protected void onProgressUpdate(String... params) {
				//TextView  tv = (TextView) mActivity.findViewById(R.id.textView2);
				//tv.setText(params[0]);
			}
			
		}
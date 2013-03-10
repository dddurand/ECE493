package Networking;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.os.AsyncTask;

public class Register extends AsyncTask<JSONObject,Integer,JSONObject> {
	
	private int TIMEOUT_MILLISEC = 10000;
	private String serverUrl="http://labtest.ece.ualberta.ca/register";
	
	protected HttpParams httpParams;
	protected HttpClient client;

	/**
	 * Parameter to get: JSONObject with username and password
	 */
	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		
		if (params.length!=1) System.out.println("Error getting parameters");
		
		try {
			//Set up Connection
			httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
			client = new DefaultHttpClient(httpParams);

			//Send Request
			HttpPost request = new HttpPost(serverUrl);
			request.setEntity(new StringEntity(params[0].toString()));
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(request, responseHandler);
			JSONObject response = new JSONObject(responseBody);
			return response;
			
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	/**
	 * Method executed after value has been returned by the doInBackground method
	 */
//	protected void onPostExecute(JSONObject result){
//		
//		if (result==null) System.out.println("Register request didn't work");
//		else System.out.println("Response received successfully");
//			
//	}
	
}

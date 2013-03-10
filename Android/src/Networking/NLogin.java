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

public class NLogin extends AsyncTask<JSONObject,Integer,JSONObject> {
	
	private int TIMEOUT_MILLISEC = 5000; //5 seconds
	private String serverUrl="http://labtest.ece.ualberta.ca/login";
	
	protected HttpParams httpParams;
	protected HttpClient client;

	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		
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

}

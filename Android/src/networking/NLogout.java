package networking;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import fragments.OnlineMode;

public class NLogout extends AsyncTask<JSONObject,Integer,JSONObject>{
	
	private int TIMEOUT_MILLISEC = 10000; //5 seconds
	private String serverUrl="http://labtest.ece.ualberta.ca/logout";
	
	protected HttpParams httpParams;
	protected HttpClient client;
	
	private final OnlineMode parent;
	private final ProgressBar pb;
	
	/**
	 * Constructor. Takes ....
	 */
	public NLogout(OnlineMode parent, ProgressBar pb){
		this.parent=parent;
		this.pb=pb;
	}
	
	@Override
	protected void onPreExecute(){
		pb.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		
		try{
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
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (ClientProtocolException e) {
			return null;
		} catch (JSONException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(JSONObject result){
		pb.setVisibility(View.INVISIBLE);
		parent.onPostLogoutRequest(result);
	}

}

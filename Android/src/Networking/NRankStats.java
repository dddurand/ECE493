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
import dataModels.RankingStatistics;
import dataModels.RankingStatistics.RankingStatisticRequest;

public class NRankStats extends AsyncTask<RankingStatisticRequest,Integer, RankingStatistics> {

	private int TIMEOUT_MILLISEC = 10000;
	private String serverUrl="http://labtest.ece.ualberta.ca/rankstatistics";
	
	protected HttpParams httpParams;
	protected HttpClient client;
	
	/**
	 * Parameter to get: JSONObject with username and password.
	 * Returns null if anything goes wrong
	 */
	@Override
	protected RankingStatistics doInBackground(RankingStatisticRequest... params) {
		
		if (params.length!=1) System.out.println("Error getting parameters");
		
		try {
			//Set up Connection
			httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
			client = new DefaultHttpClient(httpParams);

			//Send Request
			HttpPost request = new HttpPost(serverUrl);
			request.setEntity(new StringEntity(params[0].getJSON().toString()));
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(request, responseHandler);
			JSONObject response = new JSONObject(responseBody);
			
			
			return new RankingStatistics(response);
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

}

package client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import networking.ServerCodes;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import application.PokerApplication;
import dataModels.Account;
import dataModels.GameJson;
import dataModels.MoneyGenerated;
import database.DatabaseDataSource;

/**
 * The service that attempts to load data from the database
 * into the web service. The server is a constant wait mode,
 * until unlocked.
 * 
 * @author dddurand
 *
 */
public class UploadService implements Runnable {

	private int TIMEOUT_MILLISEC = 60000; //60 seconds (could be long process time)
	private String serverUrl="http://labtest.ece.ualberta.ca/upload";
	
	protected HttpParams httpParams;
	protected HttpClient client;
	
	private Semaphore gateway;
	private PokerApplication pokerApp;

	/**
	 * General Constructor
	 * 
	 * @param lock When lock is unlocked, the server attempts an upload.
	 * @param activity The context of the application
	 */
	public UploadService(Semaphore lock, PokerApplication pokerApp)
	{
		this.gateway = lock;
		this.pokerApp = pokerApp;
	}

	/**
	 * When the upload service is triggered,
	 * this service will attempt to pull all the associated data from the database,
	 * convert it into json, and submit it.
	 * 
	 * If data is uploaded successfully, then its removed from the database.
	 * 
	 */
	@Override
	public void run() {

		while(true)
		{
			try {
				this.gateway.acquire();
			} catch (InterruptedException e1) {
				continue;
			}

			/*
			 * No Internet Currently - Do Nothing.
			 */
			if(!isNetworkConnected()) continue;
			
			/*
			 * Grab all global information needed
			 */
			Account account = pokerApp.getAccount();
			if(account == null) continue;
			if(!account.isOnline()) continue;

			DatabaseDataSource db = pokerApp.getDataSource();
			
			ArrayList<GameJson> games = db.getGames(account);
			ArrayList<MoneyGenerated> moneyGens =  db.getMoneyGenerates(account);
			
			try {
				JSONObject uploadJSON = this.generateJSON(games, moneyGens, account);
				JSONObject response = this.attemptUpload(uploadJSON);
				
				if(response == null) continue;
				
				processResponse(account, db, response, moneyGens);
				
			} catch (JSONException e) {
				Log.e("UploadService", "Failed to generate JSON for upload.");
				continue;
			}
		}
	}
	
	/**
	 * Generates a json object of data for the web service.
	 * 
	 * @param account The account the data will be tied to.
	 * @param games The game data to be uploaded
	 * @param moneyGens The money generation events to be uploaded.
	 * @return
	 * @throws JSONException
	 */
	private JSONObject generateJSON(ArrayList<GameJson> games,  ArrayList<MoneyGenerated> moneyGens,
									Account account) throws JSONException
	{
		
		
			JSONObject mainJsonObject = account.getJson();
		
			JSONArray gamesJsonArray = new JSONArray();
			JSONArray miscJsonArray = new JSONArray();
		
			for(GameJson game : games)
			{
				gamesJsonArray.put(game.getJson());
			}
			
			for(MoneyGenerated moneyGen : moneyGens)
			{
				miscJsonArray.put(moneyGen.getJson());
			}
			
			mainJsonObject.put("games", gamesJsonArray);
			mainJsonObject.put("miscDatas", miscJsonArray);
			
			return mainJsonObject;		
	}
	
	/**
	 * Makes a connection to the server, and uploads the given data.
	 * The response is returned, or null on unexpected error.
	 * 
	 * @param data
	 * @return
	 */
	private JSONObject attemptUpload(JSONObject data)
	{
		try{
			//Set up Connection
			httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
			client = new DefaultHttpClient(httpParams);

			//Send Request
			HttpPost request = new HttpPost(serverUrl);
			request.setEntity(new StringEntity(data.toString()));
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
	
	/**
	 * Processes the response retrieved from the server.
	 * If data has been successfully uploaded to the server, then it is
	 * removed from the local database.
	 * 
	 * @param account Account the data is tied to locally
	 * @param db The database interface object.
	 * @param response The JSON response from the server
	 * @param moneyGens The list of money generated items
	 * @throws JSONException
	 */
	private void processResponse(Account account, DatabaseDataSource db, JSONObject response, ArrayList<MoneyGenerated> moneyGens) throws JSONException
	{
		int code = response.getInt("Code");
		
		if(code != ServerCodes.Codes.SUCCESS) return;
		
		try{
			JSONArray sucessGameUploads = response.getJSONArray("game_success_uploads");
			
			for(int i = 0; i < sucessGameUploads.length(); i++)
			{
				String uuid = sucessGameUploads.getString(0);
				db.removeGame(uuid, account);
			}
		/*If it fails there was not elements uploaded successfully*/	
		} catch (JSONException e){}
		
		
		try{
			JSONArray sucessMiscUploads = response.getJSONArray("misc_success_uploads");
			
			for(int i = 0; i < sucessMiscUploads.length(); i++)
			{
				int position = sucessMiscUploads.getInt(0);
				
				if(position < 0 || position >= moneyGens.size()) continue;
				
				MoneyGenerated generated = moneyGens.get(position);
				
				db.removeMoneyGenerate(generated.getId());
			}
			
		/*If it fails there was not elements uploaded successfully*/	
		} catch (JSONException e){}
		
	}
	
	/**
	 * This function checks if a basic internet connection is available.
	 * https://forrst.com/posts/Android_test_if_internet_connection_is_availab-wjt
	 * 
	 * @return
	 */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)pokerApp.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected());
     }



}

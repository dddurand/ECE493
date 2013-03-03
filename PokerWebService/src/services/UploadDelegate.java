package services;

import java.util.ArrayList;

import com.google.gson.Gson;

import dataModels.Account;
import dataModels.Game;
import dataModels.MiscGameData;
import dataModels.PersonalStatistics;
import dataModels.UploadData;
import database.DatabaseInterface;
import database.DatabaseInterface.DatabaseInterfaceException;
import database.ResponseObject;


/**
 * This delegate deals with processing and storing the provided game data into the database.
 * 
 * @author dddurand
 *
 */
public class UploadDelegate extends ServiceDelegate{

	/**
	 * General Constructor
	 * @param gson
	 * @param dbInterface
	 */
	public UploadDelegate(Gson gson, DatabaseInterface dbInterface) {
		super(gson, dbInterface);
	}

	@Override
	public String applyAuthProcess(Account account, String postData)
			throws DatabaseInterfaceException {

		UploadData data = this.gson.fromJson(postData, UploadData.class);
		boolean success = false;


		ArrayList<Game> games = data.getGames();
		ArrayList<String> gameUploadResults = uploadGameData(account, games);


		ArrayList<MiscGameData> miscDatas = data.getMiscDatas();
		ArrayList<Integer> miscUploadResults = uploadMiscData(account, miscDatas);

		if(gameUploadResults.size() > 0 || miscUploadResults.size() > 0)
			success = true;
		
		ResponseObject obj = new ResponseObject(success, "Upload Results");
		obj.setUploadGameSuccess(gameUploadResults);
		obj.setUploadMiscSuccess(miscUploadResults);

		return gson.toJson(obj, ResponseObject.class);

	}

	private ArrayList<Integer> uploadMiscData(Account account, ArrayList<MiscGameData> miscDatas) throws DatabaseInterfaceException
	{
		ArrayList<Integer> miscUploadResults = new ArrayList<Integer>();

		for(int position = 0; position < miscDatas.size(); position++)
		{
			MiscGameData miscData = miscDatas.get(position);
			dbInterface.saveMiscData(account, miscData);

			try{
				dbInterface.saveMiscData(account, miscData);
				miscUploadResults.add(position);
			} 
			catch(DatabaseInterfaceException e) { }

		}

		return miscUploadResults;
	}
	private ArrayList<String> uploadGameData(Account account, ArrayList<Game> games) throws DatabaseInterfaceException
	{
		ArrayList<String> gameUploadResults = new ArrayList<String>();

		for(int position = 0; position < games.size(); position++)
		{
			Game game = games.get(position);

			try{
				dbInterface.saveGame(account, game);
				gameUploadResults.add(game.getGameID());
			} 
			catch(DatabaseInterfaceException e) { }
		}
		
		this.updateRankings(account);

		return gameUploadResults;
	}
	
	private void updateRankings(Account account) throws DatabaseInterfaceException
	{
		PersonalStatistics stats = new PersonalStatistics(account, dbInterface);
		int deltaMoney = stats.deltaMoney();
		
		dbInterface.updateUsersDeltaMoney(account, deltaMoney);
	}

}
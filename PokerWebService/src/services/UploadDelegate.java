package services;

import java.util.ArrayList;

import util.Codes;

import com.google.gson.Gson;

import dataModels.Account;
import dataModels.Filter;
import dataModels.Game;
import dataModels.MiscGameData;
import dataModels.Optimality;
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
	
	private Optimality optimality;
	
	/**
	 * General Constructor
	 * @param gson
	 * @param dbInterface
	 */
	public UploadDelegate(Gson gson, DatabaseInterface dbInterface) {
		super(gson, dbInterface);
		optimality = new Optimality();
	}

	/**
	 * Process that facilitates the actual processing and storing of the data.
	 * 
	 */
	@Override
	public String applyAuthProcess(Account account, String postData)
			throws DatabaseInterfaceException {

		UploadData data = this.gson.fromJson(postData, UploadData.class);
		boolean success = false;


		ArrayList<Game> games = data.getGames();
		ArrayList<String> gameUploadResults = uploadGameData(account, games);


		ArrayList<MiscGameData> miscDatas = data.getMiscDatas();
		ArrayList<Integer> miscUploadResults = uploadMiscData(account, miscDatas);

		this.updateRankings(account);
		
		if(gameUploadResults.size() > 0 || miscUploadResults.size() > 0)
			success = true;
		
		ResponseObject obj = new ResponseObject(success, "Upload Results", Codes.SUCCESS);
		obj.setUploadGameSuccess(gameUploadResults);
		obj.setUploadMiscSuccess(miscUploadResults);

		return gson.toJson(obj, ResponseObject.class);

	}

	/**
	 * Handles the processing and storing of the misc data
	 * 
	 * @param account The account to upload the misc data for.
	 * @param miscDatas The data to be processed and stored.
	 * @return The position of the misc data that was successfully uploaded.
	 * @throws DatabaseInterfaceException
	 */
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
	
	/**
	 * Facilitates the processing and storing of the game data.
	 * This function also triggers an update to the ranking for the current user.
	 * 
	 * @param account The account to tie game data to.
	 * @param games The game data to be stored.
	 * @return List of game id's that were successfully updated.
	 * @throws DatabaseInterfaceException
	 */
	private ArrayList<String> uploadGameData(Account account, ArrayList<Game> games) throws DatabaseInterfaceException
	{
		ArrayList<String> gameUploadResults = new ArrayList<String>();

		for(int position = 0; position < games.size(); position++)
		{
			Game game = games.get(position);

			try{
				dbInterface.saveGame(account, game);
				gameUploadResults.add(game.getGameID());
				
				double opRating = optimality.getOptimalRatingForGame(game);
				dbInterface.setGameOptimalityForUser(account, game, opRating);
			} 
			catch(DatabaseInterfaceException e) { }
		}
		
		

		return gameUploadResults;
	}
	
	/**
	 * A function that causes all the ranking information to be updated for the current used.
	 * This function is only called after new data has been processed, in order to be more efficient. (or during cron)
	 * 
	 * @param account
	 * @throws DatabaseInterfaceException
	 */
	private void updateRankings(Account account) throws DatabaseInterfaceException
	{
			Filter filter = new Filter();
			PersonalStatistics stats = new PersonalStatistics(account, dbInterface, filter);
			stats.updateRankings();
		
	}

}
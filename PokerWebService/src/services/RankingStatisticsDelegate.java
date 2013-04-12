package services;

import java.util.ArrayList;

import util.Codes;

import com.google.gson.Gson;

import dataModels.Account;
import dataModels.Filter;
import dataModels.PersonalStatistics;
import dataModels.RankingStatistics;
import dataModels.RankingStatistics.RankedDataRow;
import database.DatabaseInterface;
import database.DatabaseInterface.DatabaseInterfaceException;
import database.ResponseObject;

/**
 * This delegate ranking the creation of the ranking statistics from the
 * data contained in the database.
 * 
 * @SRS 3.2.2.4
 * 
 * @author dddurand
 *
 */
public class RankingStatisticsDelegate extends ServiceDelegate{

	public RankingStatisticsDelegate(Gson gson, DatabaseInterface dbInterface) {
		super(gson, dbInterface);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Processes data in the database, and returns an ordered list of users
	 * based on a ranking value.
	 * 
	 * 
	 */
	@Override
	public String applyAuthProcess(Account postAccount, String postData)
			throws DatabaseInterfaceException {

		Filter filter = gson.fromJson(postData, Filter.class);
		
		ArrayList<RankedDataRow> rankData = dbInterface.getRankedUsers(filter);

		PersonalStatistics personalStats = new PersonalStatistics(postAccount, dbInterface, filter);
		RankedDataRow myRankData = personalStats.getMyRating();
		
		RankingStatistics statistics = new RankingStatistics(rankData, myRankData);
		
		ResponseObject responseObject = new ResponseObject(true, "Ranking Results", Codes.SUCCESS);
		responseObject.setRankingStatistics(statistics);
		
		String result = gson.toJson(responseObject);
		
		return result;

	}


}

package dataModels;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

/**
 * A representation of the set of statistics return from the web service.
 * The object takes in a JSON object and parses out all the stats.
 * 
 * @author dddurand
 *
 */
public class PersonalStatistics {

private Context context;
private ArrayList<SimpleStatistic> statistics;
private static final String[] personalStatisticIdentifiers = 
	new String[] {
	"totalDollarsBetOnCalls",
	"totalDollarsBetOnBets",
	"totalDollarsBetOnRaises",
	"totalDollarsBetOnReRaises",
	"totalDollarsBet",
	"avgDollarsBetOnCalls",
	"avgDollarsBetOnBets",
	"avgDollarsBetOnRaises",
	"avgDollarsBetOnReRaises",
	"totalNumberOfBets",
	"totalNumberOfChecks",
	"totalNumberOfCalls",
	"totalNumberOfFolds",
	"totalNumberOfRaise",
	"totalNumberOfReRaise",
	"totalNumberOfPotsWon",
	"totalNumberOfPotsLoss",
	"totalNumberOfPots",
	"avgPotOnChecks",
	"avgPotOnCalls",
	"avgPotOnBets",
	"avgPotOnRaises",
	"avgPotOnReRaises",
	"avgPotOnFolds",
	"avgPotOnWins",
	"avgPotOnLoses",
	"totalDollarsWon",
	"totalDollarsLoss",
	"totalDollarsFolded",
	"winPercentage",
	"moneyGenerated",
	"gamesPlayed",
	"netMoney",
	"avgBet",
	"netMoneyRanking",
	"optimalityRanking",
	"optimality"
	};

	/**
	 * General Constructor
	 * 
	 * @param obj The VALID web service JSON response for a personal statistic request
	 * @param context 
	 */
	public PersonalStatistics(JSONObject obj, Context context)
	{
		this.context = context;
		JSONObject personalObj;
		try {
			personalObj = obj.getJSONObject("personal_statistics");
		} catch (JSONException e) {
			return;
		}
		
		for(String stringIdentifier : personalStatisticIdentifiers)
		{
			
			SimpleStatistic simpleStat = this.getStatistic(personalObj, stringIdentifier);
			if(simpleStat == null) continue;
			
			statistics.add(simpleStat);
		}
		
	}
	
	/**
	 * Retrieves all statistics loaded
	 * @return
	 */
	public ArrayList<SimpleStatistic> getAllStatistics()
	{
		return this.statistics;
	}
	
	/**
	 * Parses a single statistic from the JSON object
	 * 
	 * @param obj The VALID JSON response from a web service for a personal statistic
	 * @param identifier The identifier for the statistic attempting to be parsed.
	 * @return
	 */
	private SimpleStatistic getStatistic(JSONObject obj, String identifier)
	{
		Number number;
		try {
			number = obj.getDouble(identifier);
		} catch (Exception e) {
			return null;
		}
		
		String displayName = this.getStringResourceByName(identifier);

		return new SimpleStatistic(identifier, displayName, number);
	}
	
	/**
	 * Retrieve the display name based on the provided string identifier
	 * 
	 * @param resourceIdentifier The statistic identifier
	 * @return
	 */
	private String getStringResourceByName(String identifier) {
	      String packageName = this.context.getPackageName();
	      int resId = this.context.getResources().getIdentifier(identifier, "string", packageName);
	      return this.context.getString(resId);
	    }
	
	/**
	 * A data model that is used to construct the JSON request to the server
	 * for a personal statistics.
	 * 
	 * @author dddurand
	 *
	 */
	public static class PersonalStatisticRequest
	{
		private TimeFrame timeframe;
		private Account account;
		
		/**
		 * General Constructor
		 * 
		 * @param timeFrame The timeframe the personal statistics will be for.
		 * @param account The account the request is for.
		 */
		public PersonalStatisticRequest(TimeFrame timeFrame, Account account)
		{
			this.timeframe = timeFrame;
			this.account = account;
		}
		
		/**
		 * Retrieves the JSON for the personal statistics request.
		 * 
		 * @return
		 * @throws JSONException
		 */
		public JSONObject getJSON() throws JSONException
		{
			JSONObject obj = this.account.getJson();
			obj.put("timeframe", timeframe.getValue());
			
			return obj;
		}
		
	}
	
}

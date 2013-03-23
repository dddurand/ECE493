package dataModels;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

/**
 * A representation of the set of community statistics return from the web service.
 * The object takes in a JSON object and parses out all the stats
 * present in the json response.
 * 
 * @author dddurand
 *
 */
public class CommunityStatistics extends Statistic<SimpleStatistic> {

	private ArrayList<SimpleStatistic> statistics;
	private static final String[] communityStatisticIdentifiers = 
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
		};

		/**
		 * General Constructor
		 * 
		 * @param obj The VALID web service JSON response for a community statistic request
		 * @param context 
		 */
		public CommunityStatistics(JSONObject obj, Context context)
		{
			super(context);
			
			statistics = new ArrayList<SimpleStatistic>();
			this.context = context;
			JSONObject commObj;
			try {
				commObj = obj.getJSONObject("community_statistics");
			} catch (JSONException e) {
				return;
			}
			
			for(String stringIdentifier : communityStatisticIdentifiers)
			{
				
				SimpleStatistic simpleStat = this.getStatistic(commObj, stringIdentifier);
				if(simpleStat == null) continue;
				
				statistics.add(simpleStat);
			}
			
		}
		
		/**
		 * Retrieves all statistics loaded
		 * @return
		 */
		@Override
		public ArrayList<SimpleStatistic> getAllStatistics()
		{
			return this.statistics;
		}
		
		/**
		 * Parses a single statistic from the JSON object
		 * 
		 * @param obj The VALID JSON response from a web service for a community statistic
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
		 * A data model that is used to construct the JSON request to the server
		 * for a community statistics.
		 * 
		 * @author dddurand
		 *
		 */
		public static class CommuntyStatisticRequest
		{
			private TimeFrame timeframe;
			private Account account;
			
			/**
			 * General Constructor
			 * 
			 * @param timeFrame The timeframe the community statistics will be for.
			 * @param account The account the request is for.
			 */
			public CommuntyStatisticRequest(TimeFrame timeFrame, Account account)
			{
				this.timeframe = timeFrame;
				this.account = account;
			}
			
			/**
			 * Retrieves the JSON for the community statistics request.
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

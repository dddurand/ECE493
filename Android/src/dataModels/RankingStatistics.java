package dataModels;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A representation of the set of ranking statistics return from the web service.
 * The object takes in a JSON object and parses out all the stats
 * present in the json response.
 * 
 * @author dddurand
 *
 */
public class RankingStatistics {

	private ArrayList<SimpleRankStatistic> statistics;
	private SimpleRankStatistic myRankStats;
		/**
		 * General Constructor
		 * 
		 * @param obj The VALID web service JSON response for a ranking statistic request
		 * @param context 
		 */
		public RankingStatistics(JSONObject obj)
		{
			
			statistics = new ArrayList<SimpleRankStatistic>();

			JSONArray rankListObj;
			try {
				rankListObj = obj.getJSONArray("ranked_statistics");
			} catch (JSONException e) {
				return;
			}
			
			
			for(int i = 0; i < rankListObj.length(); i++)
			{
				JSONObject rankObj;
				try {
					rankObj = rankListObj.getJSONObject(i);
				} catch (JSONException e) {
					continue;
				}
				
				SimpleRankStatistic simpleStat = this.getStatistic(rankObj);
				if(simpleStat == null) continue;
				
				statistics.add(simpleStat);
			}
				
			try {
				JSONObject myRankObj = obj.getJSONObject("my_ranked_statistics");
				this.myRankStats = this.getStatistic(myRankObj);
			} catch (JSONException e) {
				this.myRankStats = null;
			}
			
			
			
			 
			
			
		}
		
		/**
		 * @return the myRankStats
		 */
		public SimpleRankStatistic getMyRankStats() {
			return myRankStats;
		}

		/**
		 * Retrieves all statistics loaded
		 * @return
		 */
		public ArrayList<SimpleRankStatistic> getAllStatistics()
		{
			return this.statistics;
		}
		
		/**
		 * Parses a single statistic from the JSON object
		 * 
		 * @param obj The VALID JSON response from a web service for a ranking statistic
		 * @param identifier The identifier for the statistic attempting to be parsed.
		 * @return
		 */
		private SimpleRankStatistic getStatistic(JSONObject obj)
		{
			int position;
			String username;
			Number value;
			
			try {
				position = obj.getInt("position");
				username = obj.getString("username");
				value = obj.getDouble("rankValue");
			} catch (Exception e) {
				return null;
			}

			return new SimpleRankStatistic(username, value, position);
		}
		
		/**
		 * A data model that is used to construct the JSON request to the server
		 * for a ranking statistics.
		 * 
		 * @author dddurand
		 *
		 */
		public static class RankingStatisticRequest
		{
			private TimeFrame timeframe;
			private Account account;
			private int skip;
			private int max;
			private RankType type;
			
			/**
			 * General Constructor
			 * 
			 * @param timeFrame The timeframe of the statistics that will be used.
			 * @param account The account the data is for
			 * @param skip The amount of positions to skip (paging)
			 * @param max The max amount of rank position to return
			 * @param type The type of ranking to be used
			 */
			public RankingStatisticRequest(TimeFrame timeFrame, Account account, int skip, int max, RankType type)
			{
				this.timeframe = timeFrame;
				this.account = account;
				this.skip = skip;
				this.max = max;
				this.type = type;
			}
			
			/**
			 * General Constructor
			 * 
			 * @param timeFrame The timeframe the ranking statistics will be for.
			 * @param account The account the request is for.
			 */
			public RankingStatisticRequest(TimeFrame timeFrame, Account account)
			{
				this.timeframe = timeFrame;
				this.account = account;
				this.max = Integer.MAX_VALUE;
				this.skip = 0;
				this.type = RankType.NET_MONEY;
			}
			
			/**
			 * Retrieves the JSON for the ranking statistics request.
			 * 
			 * @return
			 * @throws JSONException
			 */
			public JSONObject getJSON() throws JSONException
			{
				JSONObject obj = this.account.getJson();
				obj.put("timeframe", timeframe.getValue());
				obj.put("skip", this.skip);
				obj.put("max", this.max);
				obj.put("rank_type", this.type.getValue());
				
				return obj;
			}
			
			/**
			 * Setter for the time frame of the class
			 * @param timeFrame
			 */
			public void setTimeFrame(TimeFrame timeFrame){
				this.timeframe=timeFrame;
			}
			
			/**
			 * TimeFrame enum of all possible timeframes the object supports.
			 * 
			 * @author dddurand
			 *
			 */
			public enum RankType
			{
				NET_MONEY("net_money"), OPTIMALITY("optimality");
				
				private String value;
				
				/**
				 * General Enum Constructor
				 * @param suit
				 */
				RankType(String netMoney)
				{
					this.value = netMoney;
				}
				
				/**
				 * Returns the string representation of the TimeFrame
				 * 
				 * @return
				 */
			    public String getValue() {
			        return value;
			    }
			    
			    /**
			     * Returns the TimeFrame for a given string. If invalid, returns ALL.
			     * 
			     * @param character
			     * @return
			     */
			    public static RankType getRankType(String rankTypeString)
			    {
			    	for (RankType rankType : RankType.values()) {
			            if (rankType.getValue().equals(rankTypeString)) {
			                return rankType;
			            }
			        }
			    	
			    	return RankType.NET_MONEY;
			    }
				
			}
			
		}
}

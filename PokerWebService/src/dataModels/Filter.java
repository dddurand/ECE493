package dataModels;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * This class generates the SQL Code needed in WHERE clauses for filtering statistics. 
 * 
 * It can accept DAY, WEEK, MONTH, YEAR for "timeframe", with all other inputs being considered all. 
 * Detects skip, max parameters, and rank type (for rank statistics).
 * 
 * @SRS 3.2.2.3 -> 3.2.2.6
 * @author dddurand
 *
 */
public class Filter {

	private TimeFrame time_frame;
	private int skip;
	private int max;
	private RankType rankType;

	/**
	 * General Constructor
	 */
	public Filter() 
	{
		this.time_frame = TimeFrame.ALL;
		this.skip = 0;
		this.max = Integer.MAX_VALUE;
		this.rankType = RankType.NET_MONEY;
	}

	/**
	 * General Constructor
	 * 
	 * @param timeFrame
	 */
	public Filter(String timeFrame)
	{
		this.time_frame = TimeFrame.getTimeFrame(timeFrame);
		this.skip = 0;
		this.max = Integer.MAX_VALUE;
		this.rankType = RankType.NET_MONEY;
	}
	
	/**
	 * General Constructor
	 * 
	 * @param timeFrame DAY, WEEK, MONTH, YEAR, otherwise defaults to ALL
	 * @param skip The number of positions to be skipped before starting return
	 * @param max The max number of positions to return.
	 */
	public Filter(String timeFrame, int skip, int max)
	{
		this.time_frame = TimeFrame.getTimeFrame(timeFrame);
		this.skip = skip;
		this.max = max;
		this.rankType = RankType.NET_MONEY;
	}
	
	/**
	 * General Constructor
	 * 
	 * @param timeFrame DAY, WEEK, MONTH, YEAR, otherwise defaults to ALL
	 * @param skip The number of positions to be skipped before starting return
	 * @param max The max number of positions to return.
	 * @param rankType The rank type to be used to order data.
	 */
	public Filter(String timeFrame, int skip, int max, RankType rankType)
	{
		this.time_frame = TimeFrame.getTimeFrame(timeFrame);
		this.skip = skip;
		this.max = max;
		this.rankType = rankType;
	}
	
	/**
	 * Returns the TimeFrame of the current filter
	 * 
	 * @return
	 */
	public TimeFrame getTimeFrame()
	{
		return this.time_frame;
	}
	
	/**
	 * Updates the current TimeFrame
	 * 
	 * @param timeFrame The new timeframe object
	 */
	public void setTimeFrame(TimeFrame timeFrame)
	{
		this.time_frame = timeFrame;
	}
	
	/**
	 * Updates the current TimeFrame based on string
	 * 
	 * DAY,WEEK,MONTH,YEAR or defaults to ALL
	 * 
	 * @param timeFrame
	 */
	public void setTimeFrame(String timeFrame)
	{
		this.time_frame = TimeFrame.getTimeFrame(timeFrame);
	}
	
	/**
	 * Updates the type of rank of the filter
	 * 
	 * @param rankType
	 */
	public void setRankType(RankType rankType)
	{
		this.rankType = rankType;
	}
	
	/**
	 * Updates the current type of rank based on the string
	 * 
	 * net_money or optimality (defaults to net_money otherwise)
	 * 
	 * @param rankType
	 */
	public void setRankType(String rankType)
	{
		this.rankType = RankType.getRankType(rankType);
	}
	
	/**
	 * Gets the Rank type for the current object
	 * 
	 * @return
	 */
	public RankType getRankType()
	{
		return this.rankType;
	}
	
	/**
	 * Returns the number of elements to skip
	 * 
	 * @return
	 */
	public int getSkip() {
		return skip;
	}

	/**
	 * Sets the number of elements to skip
	 * 
	 * @param skip
	 */
	public void setSkip(int skip) {
		this.skip = skip;
	}

	/**
	 * Returns the max number of elements to return
	 * 
	 * @return
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Sets the max number of elements to return
	 * 
	 * @param max
	 */
	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * Generates a fragement of SQL that should be placed in the WHERE clause.
	 * It will cause the data to be filtered based on a column called date_uploaded,
	 * and the timeframe parameter provided at creation. 
	 * 
	 */
	public String getSqlTimeFrameFilter()
	{
		switch(this.time_frame)
		{
			case DAY:
				return " and date_uploaded >= DATE_ADD(CURRENT_DATE(), INTERVAL -1 DAY) ";
			case WEEK:
				return " and date_uploaded >= DATE_ADD(CURRENT_DATE(), INTERVAL -1 WEEK) ";
			case MONTH:
				return " and date_uploaded >= DATE_ADD(CURRENT_DATE(), INTERVAL -1 MONTH) ";
			case YEAR:
				return " and date_uploaded >= DATE_ADD(CURRENT_DATE(), INTERVAL -1 YEAR) ";
			default:
				return "";
					
		}
	}
	
	/**
	 * Returns the Sql filter for the number of elements to skip
	 * 
	 * @return
	 */
	public String getSkipSqlFilter()
	{
		return " OFFSET " + this.skip + " ";
	}
	
	/**
	 * Returns the sql for the max number of elements.
	 * 
	 * @return
	 */
	public String getMaxSqlFilter()
	{
		return " LIMIT " + this.max + " ";
	}
	
	/**
	 * TimeFrame enum of all possible timeframes the object supports.
	 * 
	 * @author dddurand
	 *
	 */
	public enum TimeFrame
	{
		DAY("DAY"), WEEK("WEEK"), MONTH("MONTH"), YEAR("YEAR"), ALL("ALL");
		
		private String value;
		
		/**
		 * General Enum Constructor
		 * @param suit
		 */
		TimeFrame(String timeFrame)
		{
			this.value = timeFrame;
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
	    public static TimeFrame getTimeFrame(String timeFrameString)
	    {
	    	for (TimeFrame timeFrame : TimeFrame.values()) {
	            if (timeFrame.getValue().equals(timeFrameString)) {
	                return timeFrame;
	            }
	        }
	    	
	    	return TimeFrame.ALL;
	    }
		
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
	
	/**
	 * Custom Deserializer that takes the provided json,
	 * and converts it into a Filter object.
	 * 
	 * @author dddurand
	 *
	 */
	public static class FilterDeserializer implements JsonDeserializer<Filter> {

		/**
		 * The custom serializer that converts the provided json,
		 * into a UploadData object.
		 * 
		 */
		@Override
		public Filter deserialize(JsonElement element, Type type,
				JsonDeserializationContext context) throws JsonParseException {

			JsonObject uploadDataObj = element.getAsJsonObject();

			JsonElement timeFrameElement = uploadDataObj.get("timeframe");
			
			JsonElement skipElement = uploadDataObj.get("skip");
			
			JsonElement maxElement = uploadDataObj.get("max");
			
			JsonElement rankTypeElement = uploadDataObj.get("rank_type");

			Filter filter = new Filter();
			
			if(timeFrameElement != null)
				filter.setTimeFrame(timeFrameElement.getAsString());
			
			if(skipElement != null)
				filter.setSkip(skipElement.getAsInt());
			
			if(filter.getSkip() < 0) throw new JsonParseException("Invalid Skip");
			
			if(maxElement != null)
				filter.setMax(maxElement.getAsInt());
			
			if(filter.getMax() < 0) throw new JsonParseException("Invalid Max");
			
			if(rankTypeElement != null)
				filter.setRankType(rankTypeElement.getAsString());
			
			return filter;
		}
	}
	
}

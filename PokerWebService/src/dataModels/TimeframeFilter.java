package dataModels;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * This class generates the SQL Code needed in WHERE clauses. It can accept DAY, WEEK, MONTH as timeframes,
 * with all other inputs being considered all. 
 * 
 * @author dddurand
 *
 */
public class TimeframeFilter {

	private TimeFrame time_frame;
	

	/**
	 * General Constructor
	 */
	public TimeframeFilter() 
	{
		this.time_frame = TimeFrame.ALL;
	}

	/**
	 * General Constructor
	 * 
	 * @param timeFrame
	 */
	public TimeframeFilter(String timeFrame)
	{
		this.time_frame = TimeFrame.getTimeFrame(timeFrame);
	}
	
	public TimeFrame getTimeFrame()
	{
		return this.time_frame;
	}
	
	/**
	 * Generates a fragement of SQL that should be placed in the WHERE clause.
	 * It will cause the data to be filtered based on a column called date_uploaded,
	 * and the timeframe parameter provided at creation. 
	 * 
	 */
	public String getSqlFilter()
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
	 * Custom Deserializer that takes the provided json,
	 * and converts it into a TimeframeFilter object.
	 * 
	 * @author dddurand
	 *
	 */
	public static class TimeframeFilterDeserializer implements JsonDeserializer<TimeframeFilter> {

		/**
		 * The custom serializer that converts the provided json,
		 * into a UploadData object.
		 * 
		 */
		@Override
		public TimeframeFilter deserialize(JsonElement element, Type type,
				JsonDeserializationContext context) throws JsonParseException {

			JsonObject uploadDataObj = element.getAsJsonObject();

			JsonElement stringElement = uploadDataObj.get("timeframe");

			if(stringElement == null)
				return new TimeframeFilter();
			else 
				return new TimeframeFilter(stringElement.getAsString());
		}
	}
	
}

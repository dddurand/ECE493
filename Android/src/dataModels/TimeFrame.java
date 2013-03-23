package dataModels;

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
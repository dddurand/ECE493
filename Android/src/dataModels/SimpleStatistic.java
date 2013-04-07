package dataModels;

/**
 * A representation of a simple name-value statistic.
 * The object contains its original json identifier, display text/name and its value.
 * 
 * @author dddurand
 *
 */
public class SimpleStatistic {

	private String identifier;
	private String displayName;
	private Number value;
	
	/**
	 * General Constructor
	 * 
	 * @param identifier The json identifier for this statistic
	 * @param displayName The display name or text associated with this stat
	 * @param value The value of the statistic
	 */
	public SimpleStatistic(String identifier, String displayName, Number value)
	{
		this.identifier = identifier;
		this.displayName = displayName;
		this.value = value;
	}

	/**
	 *  Gets json identifier
	 * @return
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Sets json identifier
	 * 
	 * @param identifier
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Gets the display name/text
	 * 
	 * @return
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display next/name
	 * 
	 * @param displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Gets the stat value
	 * 
	 * @return
	 */
	public Number getValue() {
		return value;
	}

	/**
	 * Sets the stat value
	 * 
	 * @param value
	 */
	public void setValue(Number value) {
		this.value = value;
	}
	
	/**
	 * Gets stat value as a double
	 * 
	 * @return
	 */
	public double getValueAsDouble() {
		return value.doubleValue();
	}
	
	/**
	 * Gets stat value as a float
	 * 
	 * @return
	 */
	public float getValueAsFloat() {
		return value.floatValue();
	}
	
	/**
	 * Gets stat value as an int
	 * 
	 * @return
	 */
	public int getValueAsInt() {
		return value.intValue();
	}

}

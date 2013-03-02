package dataModels;

/**
 * A Data representation for Misc Game Data
 * 
 * @author dddurand
 *
 */
public class MiscGameData {

	private String name;
	private String value;
	
	/**
	 * General Constructor
	 */
	public MiscGameData() { }
	
	/**
	 * General Constructor
	 * 
	 * @param name Name for misc game data
	 * @param value Value for misc game data
	 */
	public MiscGameData(String name, String value) 
	{ 
		this.name = name;
		this.value = value;
	}

	/**
	 * Returns the name of data
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the misc data
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the value of the misc data
	 * 
	 * @return
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the name of the misc data
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}

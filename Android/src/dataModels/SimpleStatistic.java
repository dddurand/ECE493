package dataModels;

public class SimpleStatistic {

	private String identifier;
	private String displayName;
	private Number value;
	
	public SimpleStatistic(String identifier, String displayName, Number value)
	{
		this.identifier = identifier;
		this.displayName = displayName;
		this.value = value;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Number getValue() {
		return value;
	}

	public void setValue(Number value) {
		this.value = value;
	}
	
	public double getValueAsDouble() {
		return value.doubleValue();
	}
	
	public float getValueAsFloat() {
		return value.floatValue();
	}
	
	public float getValueAsInt() {
		return value.intValue();
	}

}

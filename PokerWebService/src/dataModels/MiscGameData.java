package dataModels;

public class MiscGameData {

	private String name;
	private String value;
	
	public MiscGameData() { }
	
	public MiscGameData(String name, String value) 
	{ 
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}

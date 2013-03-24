package misc;

/**
 * Class that represents a row object in a list that contains 2 strings
 * @author kennethrodas
 */
public class StatsRowObject {
	
    private String prop1; 
    private String prop2;

    public StatsRowObject(String prop1, String prop2) {
        this.prop1 = prop1;
        this.prop2 = prop2;
    }

    public String getProp1() {
        return prop1;
    }

    public String getProp2() {
       return prop2;
    }

}

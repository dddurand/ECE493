package misc;

/**
 * Class that represents a row object in a list that contains 2 strings
 * @author kennethrodas
 */
public class StatsRowObject {
	
    private String prop1; 
    private String prop2;

    /**
     * General Constructor
     * 
     * @param prop1
     * @param prop2
     */
    public StatsRowObject(String prop1, String prop2) {
        this.prop1 = prop1;
        this.prop2 = prop2;
    }

    /**
     * Get left string in layout
     * 
     * @return
     */
    public String getProp1() {
        return prop1;
    }

    /**
     * Get right string in row layout
     * 
     * @return
     */
    public String getProp2() {
       return prop2;
    }

}

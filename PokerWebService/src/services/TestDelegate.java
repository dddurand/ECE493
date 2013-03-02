package services;

import com.google.gson.Gson;

import database.DatabaseInterface;


/**
 * This delegate deals with processing and storing the provided game data into the database.
 * 
 * @author dddurand
 *
 */
public class TestDelegate extends ServiceDelegate{

	/**
	 * General Constructor
	 * @param gson
	 * @param dbInterface
	 */
	public TestDelegate(Gson gson, DatabaseInterface dbInterface) {
		super(gson, dbInterface);
	}



}
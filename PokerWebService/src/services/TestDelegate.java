package services;

import com.google.gson.Gson;

import dataModels.Account;
import dataModels.PersonalStatistics;
import database.DatabaseInterface;
import database.DatabaseInterface.DatabaseInterfaceException;
import database.ResponseObject;


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

	@Override
	public String applyAuthProcess(Account postAccount, String postData)
			throws DatabaseInterfaceException {

		PersonalStatistics stat = new PersonalStatistics(postAccount, dbInterface);
		stat.generateAllStatistics();

		ResponseObject response = new ResponseObject(true, "SUCCESS");
		response.setPersonalStatistics(stat);

		return gson.toJson(response, ResponseObject.class);

	}



}
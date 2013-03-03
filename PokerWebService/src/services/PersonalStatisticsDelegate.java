package services;

import com.google.gson.Gson;

import dataModels.Account;
import dataModels.PersonalStatistics;
import database.DatabaseInterface;
import database.ResponseObject;
import database.DatabaseInterface.DatabaseInterfaceException;

/**
 * This delegate facilitates the creation of the personal statistics from the
 * data contained in the database.
 * 
 * @author dddurand
 *
 */
public class PersonalStatisticsDelegate extends ServiceDelegate {

	public PersonalStatisticsDelegate(Gson gson, DatabaseInterface dbInterface) {
		super(gson, dbInterface);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Processes data in the database into a series of person statistics
	 * 
	 */
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

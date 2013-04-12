package services;

import util.Codes;

import com.google.gson.Gson;

import dataModels.Account;
import dataModels.Filter;
import dataModels.PersonalStatistics;
import database.DatabaseInterface;
import database.DatabaseInterface.DatabaseInterfaceException;
import database.ResponseObject;

/**
 * This delegate facilitates the creation of the personal statistics from the
 * data contained in the database.
 * 
 * @SRS 3.2.2.6
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

		Filter filter = gson.fromJson(postData, Filter.class);
		
		PersonalStatistics stat = new PersonalStatistics(postAccount, dbInterface, filter);
		stat.generateAllStatistics();

		ResponseObject response = new ResponseObject(true, "SUCCESS", Codes.SUCCESS);
		response.setPersonalStatistics(stat);

		return gson.toJson(response, ResponseObject.class);

	}
}

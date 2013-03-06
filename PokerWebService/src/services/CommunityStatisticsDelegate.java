package services;

import com.google.gson.Gson;

import dataModels.Account;
import dataModels.CommunityStatistics;
import dataModels.Filter;
import database.DatabaseInterface;
import database.DatabaseInterface.DatabaseInterfaceException;
import database.ResponseObject;

/**
 * This delegate facilitates the creation of the community statistics from the
 * data contained in the database.
 * 
 * @author dddurand
 *
 */
public class CommunityStatisticsDelegate extends ServiceDelegate {

	public CommunityStatisticsDelegate(Gson gson, DatabaseInterface dbInterface) {
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
		
		CommunityStatistics stat = new CommunityStatistics(dbInterface, filter);
		stat.generateAllStatistics();

		ResponseObject response = new ResponseObject(true, "SUCCESS");
		response.setCommunityStatistics(stat);

		return gson.toJson(response, ResponseObject.class);

	}
}

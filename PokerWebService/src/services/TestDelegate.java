package services;

import com.google.gson.Gson;

import dataModels.Account;
import dataModels.PersonalStatistics;
import dataModels.TimeframeFilter;
import database.DatabaseInterface;
import database.DatabaseInterface.DatabaseInterfaceException;


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


		TimeframeFilter filter = gson.fromJson(postData, TimeframeFilter.class);
		
		PersonalStatistics stat = new PersonalStatistics(postAccount, dbInterface, filter);
		int money = stat.getMoneyGenerate();

		return ""+money;

	}



}
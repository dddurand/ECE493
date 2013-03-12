package services;

import java.util.ArrayList;

import com.google.gson.Gson;

import dataModels.Account;
import dataModels.Card;
import dataModels.Card.Number;
import dataModels.Card.Suit;
import dataModels.Optimality;
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

		return " ";
	}



}
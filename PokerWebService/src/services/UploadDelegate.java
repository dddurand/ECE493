package services;

import com.google.gson.Gson;

import dataModels.Account;
import dataModels.UploadData;
import database.DatabaseInterface;
import database.DatabaseInterface.DatabaseInterfaceException;
import database.ResponseObject;


/**
 * The ServiceDelegate extension that deals with the process of logging out a user,
 * through the invalidation/removal of the current authentication token for the account.
 * 
 * @author dddurand
 *
 */
public class UploadDelegate extends ServiceDelegate{

	/**
	 * General Constructor
	 * @param gson
	 * @param dbInterface
	 */
	public UploadDelegate(Gson gson, DatabaseInterface dbInterface) {
		super(gson, dbInterface);
	}

@Override
public String applyAuthProcess(Account postAccount, String postData)
		throws DatabaseInterfaceException {
	
	UploadData data = this.gson.fromJson(postData, UploadData.class);
	
	//save to database
	
	ResponseObject response = new ResponseObject(true, ResponseObject.SUCCESS);
	return gson.toJson(response, ResponseObject.class);
}

}
package mockObjects;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import dataModels.Account;
import database.DatabaseInterface;
import database.DatabaseInterface.DatabaseInterfaceException;
import services.ServiceDelegate;

public class MockDelegate extends ServiceDelegate {

	private boolean dbE = false;
	private boolean JSE = false;
	private boolean JPE = false;
	
	public MockDelegate(Gson gson, DatabaseInterface dbInterface) {
		super(gson, dbInterface);
		
	}
	
	public MockDelegate(Gson gson, DatabaseInterface dbInterface, boolean dbE, boolean JSE, boolean JPE) {
		super(gson, dbInterface); 
		this.dbE = dbE;
		this.JSE = JSE;
		this.JPE = JPE;  
	}

	@Override
	public String applyAuthProcess(Account postAccount, String postData)
			throws DatabaseInterfaceException {

		exception();
		return "SUCCESS";
	}
	
	@Override
	public String applyLoginProcess(Account account, String postData)
			throws DatabaseInterfaceException {

		exception();
		return "SUCCESS";
	}

	@Override
	public String unsecureProcess(Account account, String postData)
			throws DatabaseInterfaceException {
		
		exception();
		return "SUCCESS";
	}
	
	private void exception() throws DatabaseInterfaceException
	{
		if(dbE) throw new DatabaseInterfaceException("FAIL", 0);
		if(JSE) throw new JsonSyntaxException("FAIL");
		if(JPE) throw new JsonParseException("FAIL");
	}
	
}

package dataModelTests;

import org.json.JSONException;
import org.json.JSONObject;

import dataModels.Account;
import dataModels.MoneyGenerated;
import android.test.AndroidTestCase;

public class MoneyGeneratedTest extends AndroidTestCase {

	public void tearDown() throws Exception {
	    ///CLOVER:FLUSH
	    super.tearDown();
	}
	
	public void testCons()
	{
		int value = 20;
		String user = "user";
		String auth = "auth";
		int balance = 50;
		
		Account account = new Account(user, auth, balance);
		MoneyGenerated moneyGen = new MoneyGenerated(value, account);
		
		assertTrue(moneyGen.getName().equals("MoneyGenerated"));
		assertTrue(moneyGen.getAccount().equals(account));
		assertTrue(moneyGen.getValue() == value);
		
	}
	
	public void testCons2()
	{
		int value = 20;
		int id = 1;
		MoneyGenerated moneyGen = new MoneyGenerated(value, id);
		
		assertTrue(moneyGen.getName().equals("MoneyGenerated"));
		assertTrue(moneyGen.getId() == id);
		assertTrue(moneyGen.getValue() == value);
		
	}
	
	public void testSetters()
	{
		int value = 20;
		int id = 1;
		MoneyGenerated moneyGen = new MoneyGenerated(value, id);
		
		String user = "user";
		String auth = "auth";
		int balance = 50;
		
		int value2 = 2;
		
		Account account = new Account(user, auth, balance);
		
		moneyGen.setAccount(account);
		moneyGen.setId(id);
		moneyGen.setValue(value2);
		
		assertTrue(moneyGen.getName().equals("MoneyGenerated"));
		assertTrue(moneyGen.getId() == id);
		assertTrue(moneyGen.getAccount().equals(account));
		assertTrue(moneyGen.getValue() == value2);
	}
	
	public void testGetJson()
	{
		int value = 20;
		int id = 1;
		MoneyGenerated moneyGen = new MoneyGenerated(value, id);
		
		try {
			JSONObject obj = moneyGen.getJson();
			String name = obj.getString("name");
			int value2 = obj.getInt("value");
			
			assertTrue(name.equals("MoneyGenerated"));
			assertTrue(value2 == value);
			
		} catch (JSONException e) {
			fail();
		}

	}
	
}

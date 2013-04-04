package dataModelTests;

import org.json.JSONObject;

import dataModels.Account;

import android.test.AndroidTestCase;

public class AccountTest extends AndroidTestCase  {

	public void tearDown() throws Exception {
	    ///CLOVER:FLUSH
	    super.tearDown();
	}
	
	public void testAccount()
	{
		Account account = new Account();
		assertTrue(account.getUsername().isEmpty());
		assertTrue(account.getAuthenticationToken().isEmpty());
		assertTrue(account.getBalance() == -1);
		assertTrue(account.isOnline() == false);
		assertTrue(account.getPassword().isEmpty());
	}
	
	public void testSetPassword()
	{
		String test = "test";
		Account account = new Account();
		account.setPassword(test);

		assertTrue(account.getPassword().equals(test));
	}
	
	public void testAccountCons()
	{
		String username = "abc";
		String auth = "edf";
		int balance = 30;
		
		Account account = new Account(username, auth, balance);
		assertTrue(account.getUsername().equals(username));
		assertTrue(account.getAuthenticationToken().equals(auth));
		assertTrue(account.getBalance() == balance);
		assertTrue(account.isOnline() == true);
	}
	
	public void testAccountCons2()
	{
		String username = "abc";
		int balance = 30;
		
		Account account = new Account(username, balance);
		assertTrue(account.getUsername().equals(username));
		assertTrue(account.getBalance() == balance);
		assertTrue(account.isOnline() == false);
	}
	
	public void testSetUsername()
	{
		String username = "abc";
		
		Account account = new Account();
		account.setUsername(username);
		
		assertTrue(account.getUsername().equals(username));
	}
	
	public void testSetAuth()
	{
		String auth = "abc";
		
		Account account = new Account();
		account.setAuthenticationToken(auth);
		
		assertTrue(account.getAuthenticationToken().equals(auth));
	}
	
	public void testSetOnline()
	{
		boolean isOnline = true;
		
		Account account = new Account();
		account.setOnline(isOnline);
		
		assertTrue(account.isOnline() == isOnline);
	}
	
	public void testBalance()
	{
		int balance = 50;
		
		Account account = new Account();
		account.setBalance(balance);
		
		assertTrue(account.getBalance() == balance);
	}
	
	public void testAddalance()
	{
		int balance = 50;
		
		Account account = new Account();
		account.setBalance(balance);
		account.addBalance(balance);
		
		assertTrue(account.getBalance() == 2 * balance);
	}
	
	public void testGetJson()
	{
		String username = "abc";
		String pass = "12343";
		String auth = "edf";
		int balance = 30;
		
		Account account = new Account(username, auth, balance);
		account.setPassword(pass);
		
		try {
			JSONObject obj = account.getJson();
			String user2 = obj.get("username").toString();
			String pass2 = obj.get("password").toString();
			String auth2 = obj.get("authenticationToken").toString();
			
			assertTrue(user2.equals(username));
			assertTrue(pass2.equals(pass));
			assertTrue(auth2.equals(auth));
		} catch (Exception e) {
			fail();
		}
	}
	
	
}

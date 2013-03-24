/**
 * 
 */
package dataModels;

import static org.junit.Assert.*;

import org.junit.Test;

import util.PasswordUtil;

/**
 * @author dddurand
 *
 */
public class AccountTest {

	@Test
	public void testEmptyConstructor() {
		Account account = new Account();
		assertTrue(account.getAuthenticationToken() == null);
		assertTrue(account.getPassword() == null);
		assertTrue(account.getUsername() == null);
	}
	
	@Test
	public void usernameConstructor()
	{
		String name = "test";
		Account account = new Account(name);
		
		assertTrue(account.getUsername().equals(name));
		assertTrue(account.getAuthenticationToken() == null);
		assertTrue(account.getPassword() == null);
	}

	@Test
	public void userPassConstructor()
	{
		String name = "TEST";
		String pass = "PASSWORD";
		Account account = new Account(name, pass, false);
		
		assertTrue(account.getPassword().equals(pass));
		assertTrue(!account.getEncyptedPassword().equals(pass));
		
		PasswordUtil util = new PasswordUtil();
		String password = util.encrypt(pass);
		
		assertTrue(account.getEncyptedPassword().equals(password));
		
		
		account = new Account(name, pass, true);
		
		assertTrue(account.getPassword().equals(pass));
		assertTrue(account.getEncyptedPassword().equals(pass));
	}
	
	@Test
	public void userPassAuthConstructor()
	{
		String name = "TEST";
		String pass = "PASSWORD";
		String auth = "AUTH";
		Account account = new Account(name, pass, auth, false);
		
		assertTrue(account.getPassword().equals(pass));
		assertTrue(!account.getEncyptedPassword().equals(pass));
		assertTrue(account.getAuthenticationToken().equals(auth));
		
		
		PasswordUtil util = new PasswordUtil();
		String password = util.encrypt(pass);
		
		assertTrue(account.getEncyptedPassword().equals(password));
		
		
		account = new Account(name, pass, auth, true);
		
		assertTrue(account.getEncyptedPassword().equals(pass));
		assertTrue(account.getAuthenticationToken().equals(auth));
	}
	
	@Test
	public void fullConstructor()
	{
		String name = "TEST";
		String pass = "PASSWORD";
		String auth = "AUTH";
		int id = 5;
		Account account = new Account(id, name, pass, auth, false);
		
		assertTrue(account.getPassword().equals(pass));
		assertTrue(!account.getEncyptedPassword().equals(pass));
		assertTrue(account.getAuthenticationToken().equals(auth));
		assertTrue(account.getAccountID() == id);
		
		PasswordUtil util = new PasswordUtil();
		String password = util.encrypt(pass);
		
		assertTrue(account.getEncyptedPassword().equals(password));
		
		
		account = new Account(id, name, pass, auth, true);
		
		assertTrue(account.getEncyptedPassword().equals(pass));
		assertTrue(account.getAuthenticationToken().equals(auth));
		assertTrue(account.getAccountID() == id);
	}
	
	@Test
	public void testGetAccountID()
	{
		String name = "TEST";
		String pass = "PASSWORD";
		String auth = "AUTH";
		int id = 5;
		Account account = new Account(id, name, pass, auth, false);
		
		assertTrue(account.getAccountID() == id);
		
	}
	
	@Test
	public void testSetAccountID()
	{
		int id = 8;
		Account account = new Account();
		account.setAccountID(id);
		
		assertTrue(account.getAccountID() == id);
	}
	
	@Test
	public void testGetUsername()
	{
		String username = "NAME";
		Account account = new Account(username);
		
		
		assertTrue(account.getUsername().equals(username));
	}
	
	@Test
	public void testSetUsername()
	{
		String username = "NAME";
		Account account = new Account();
		account.setUsername(username);
		
		assertTrue(account.getUsername().equals(username));
	}
	
	@Test
	public void testGetPassword()
	{
		String name = "TEST";
		String pass = "PASSWORD";
		String auth = "AUTH";
		int id = 5;
		Account account = new Account(id, name, pass, auth, false);
		
		
		assertTrue(account.getPassword().equals(pass));
	}
	
	@Test
	public void testGetEncyptedPassword()
	{
		String name = "TEST";
		String pass = "PASSWORD";
		String auth = "AUTH";
		int id = 5;
		Account account = new Account(id, name, pass, auth, false);
		
		PasswordUtil util = new PasswordUtil();
		String password = util.encrypt(pass);
		
		assertTrue(account.getEncyptedPassword().equals(password));
		
		
		account = new Account(name, pass, true);

		assertTrue(account.getEncyptedPassword().equals(pass));
	}
	
	@Test
	public void testSetPassword()
	{
		String name = "TEST";
		String pass = "PASSWORD";
		Account account = new Account(name, pass, false);
		account.setPassword(pass, false);
		
		assertTrue(account.getPassword().equals(pass));
		
		account = new Account(name, pass, false);
		account.setPassword(pass, true);
		
		assertTrue(account.getEncyptedPassword().equals(pass));
		
	}
	
	@Test
	public void testSetAuth()
	{
		String name = "TEST";
		String pass = "PASSWORD";
		String auth = "AUTH";
		Account account = new Account(name, pass, false);
		account.setAuthenticationToken(auth);
		
		assertTrue(account.getPassword().equals(pass));
		
		account = new Account(name, pass, false);
		account.setPassword(pass, true);
		
		assertTrue(account.getEncyptedPassword().equals(pass));
		
	}
	
	@Test
	public void testGetAuthToken()
	{
		String name = "TEST";
		String pass = "PASSWORD";
		String auth = "AUTH";
		Account account = new Account(name, pass, auth, false);
		
		assertTrue(account.getAuthenticationToken().equals(auth));
	}
	
	@Test
	public void testExactCompare()
	{
		Account account1 = new Account("acc", "pass", "auth" , false);
		Account account2 = new Account("acc", "pass", "auth" , false);
		
		Account account3 = new Account("acc1", "pass", "auth" , false);
		Account account4 = new Account("acc", "pass1", "auth" , false);
		Account account5 = new Account("acc", "pass", "auth1" , false);
		
		assertTrue(account1.exactCompare(account2));
		
		assertTrue(!account1.exactCompare(account3));
		assertTrue(!account1.exactCompare(account4));
		assertTrue(!account1.exactCompare(account5));

		assertTrue(!account1.exactCompare(null));
		
		assertTrue(!account1.exactCompare(new String()));
	}
	
	@Test
	public void testLoginCompare()
	{
		Account account1 = new Account("acc", "pass", "auth" , false);
		Account account2 = new Account("acc", "pass", "auth" , false);
		
		Account account3 = new Account("acc1", "pass", "auth" , false);
		Account account4 = new Account("acc", "pass1", "auth" , false);
		Account account5 = new Account("acc", "pass", "auth1" , false);
		
		assertTrue(account1.compareLogin(account2));
		
		assertTrue(!account1.compareLogin(account3));
		assertTrue(!account1.compareLogin(account4));
		assertTrue(account1.compareLogin(account5));

		assertTrue(!account1.compareLogin(null));
	}
	
	@Test
	public void testCompareAuth()
	{
		Account account1 = new Account("acc", "pass", "auth" , false);
		Account account2 = new Account("acc", "pass", "auth" , false);
		
		Account account3 = new Account("acc1", "pass", "auth" , false);
		Account account4 = new Account("acc", "pass1", "auth" , false);
		Account account5 = new Account("acc", "pass", "auth1" , false);
		
		assertTrue(account1.compareAuthenticated(account2));
		
		assertTrue(!account1.compareAuthenticated(account3));
		assertTrue(account1.compareAuthenticated(account4));
		assertTrue(!account1.compareAuthenticated(account5));

		assertTrue(!account1.compareAuthenticated(null));
	}
	
	@Test
	public void isEqualString()
	{
		Account account1 = new Account("acc", "pass", "auth" , false);
		Account account2 = new Account(null, null, null , false);
		
		account1.isEqualString(account2.getAuthenticationToken(),account1.getAuthenticationToken());
		account2.isEqualString(account1.getAuthenticationToken(),account2.getAuthenticationToken());
		
		account1.isEqualUsername(account2.getAuthenticationToken(),account1.getAuthenticationToken());
		account2.isEqualUsername(account1.getAuthenticationToken(),account2.getAuthenticationToken());
	}
	
}

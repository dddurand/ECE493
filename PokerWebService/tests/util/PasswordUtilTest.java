package util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PasswordUtilTest {

	@Test
	public void compare() {
		String pass = "Password";
		PasswordUtil util = new PasswordUtil();
		String encryptedPass = util.encrypt(pass);
		
		assertTrue(util.compare(pass, encryptedPass));
		assertTrue(!util.compare("asdf", encryptedPass));
		assertTrue(!util.compare(pass, "asdf"));
	}

}

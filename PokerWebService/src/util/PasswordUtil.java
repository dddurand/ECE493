package util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Util class for dealing with the encryption, and comparision of encrypted passwords,
 * and plaintext passwords. It also contains functions for converting strings to and from byte arrays.
 * 
 * @author dddurand
 *
 */
public class PasswordUtil {

	private static final String ENCODING = "UTF-8";
	private static final String ALGORITHM = "SHA-512";
	private static final String SIMPLE_SALT = "O1E jtl|a0%0|+#$cT}?H+-nS2zxy;2+an/Z`s|nCS-*-f_`U/V}Xo+OB``p$i+_";
	
	public PasswordUtil() { }
	
	/**
	 * One way encrypts a plaintext password.
	 * The result is returned as a string.
	 * 
	 * @param password Plaintext password.
	 * @return Encrypted password as a string.
	 */
	public String encrypt(String password) {
		String encryptedPassword = null;
		byte[] saltedPasswordBytes;
		MessageDigest digest;

		try{
			String saltedPassword = SIMPLE_SALT + password;
			saltedPasswordBytes = getBytesFromString(saltedPassword);
			digest = MessageDigest.getInstance(ALGORITHM);
			digest.reset();
			digest.update(saltedPasswordBytes);

			byte[] message = digest.digest();

			encryptedPassword = getStringFromBytes(message);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 

		return encryptedPassword; 
	}
	
	/**
	 * Compares a plaintext password, and an encrypted password.
	 * 
	 * @param plainPassword Plain text password
	 * @param encryptedPassword encrypted passwrd
	 * @return true on match, false otherwise
	 */
	public boolean compare(String plainPassword, String encryptedPassword)
	{
		
		String newEncryptedPassword = this.encrypt(plainPassword);
		
		if(encryptedPassword.equals(newEncryptedPassword))
			return true;
		else
			return false;
	}
	
	/**
	 * Converts a set of bytes into a string representation of the password.
	 * 
	 * @param passwordBytes Array of bytes presenting the password.
	 * @return The string equivalent password.
	 */
	public String getStringFromBytes(byte[] passwordBytes)
	{
		try {
			return new String(passwordBytes, ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return new String(passwordBytes);
	}
	
	/**
	 * Converts a provided string password, and returns the equivalent byte array.
	 * 
	 * @param password Password as a string.
	 * @return Byte array equivalent of the provided string.
	 */
	public byte[] getBytesFromString(String password)
	{
		try {
			return password.getBytes(ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return password.getBytes();
	}
	
}

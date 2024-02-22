package com.hillspet.wearables.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SHAUtil {

	private static final String defaultSalt = "1234567890098765";
	private static final Logger LOGGER = LogManager.getLogger(SHAUtil.class);

	/**
	 * Method Name: getSHA512HashedString This method is used to generate SHA-512
	 * Hashed String using default salt
	 * 
	 * @param stringToHash
	 * @return hashedString
	 */
	public String getSHA512HashedString(String stringToHash) {
		// Declare String securedHash to hold the secured hash string
		String securedHash = null;
		try {
			// Getting the MessageDigest Instance by passing the Algorithm type
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			// Calling generateSecureHashedString to generate the secured hash string.
			securedHash = this.generateSecureHashedString(stringToHash, defaultSalt, md);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Exception while generating the secured hash using getSHA512HashedString" + e);
		}
		return securedHash;
	}

	/**
	 * Method Name: getSHA512HashedString This method is used to generate SHA-512
	 * Hashed String
	 * 
	 * @param stringToHash
	 * @param salt
	 * @return hashedString
	 * @throws Exception
	 */
	public String getSHA512HashedString(String stringToHash, String salt) throws Exception {
		// Declare String securedHash to hold the secured hash string
		String securedHash = null;
		try {
			// validating salt is a null or empty string
			if (salt == null || salt.trim().isEmpty()) {
				throw new Exception("Salt should not be null or empty");
			}
			// Getting the MessageDigest Instance by passing the Algorithm type
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			// Calling generateSecureHashedString to generate the secured hash string.
			securedHash = this.generateSecureHashedString(stringToHash, salt, md);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Exception while generating the secured hash using getSHA512HashedString" + e);
		}
		return securedHash;
	}

	/**
	 * Method Name: getSHA348HashedString This method is used to generate SHA-348
	 * Hashed String using default salt
	 * 
	 * @param stringToHash
	 * @return hashedString
	 */
	public String getSHA384HashedString(String stringToHash) {
		// Declare String securedHash to hold the secured hash string
		String securedHash = null;
		try {
			// Getting the MessageDigest Instance by passing the Algorithm type
			MessageDigest md = MessageDigest.getInstance("SHA-384");
			// Calling generateSecureHashedString to generate the secured hash
			// string.
			securedHash = this.generateSecureHashedString(stringToHash, defaultSalt, md);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Exception while generating the secured hash using getSHA384HashedString" + e);
		}
		return securedHash;
	}

	/**
	 * Method Name: getSHA348HashedString This method is used to generate SHA-348
	 * Hashed String
	 * 
	 * @param stringToHash
	 * @param salt
	 * @return hashedString
	 * @throws Exception
	 */
	public String getSHA384HashedString(String stringToHash, String salt) throws Exception {
		// Declare String securedHash to hold the secured hash string
		String securedHash = null;
		try {
			// validating salt is a null or empty string
			if (salt == null || salt.trim().isEmpty()) {
				throw new Exception("Salt should not be null or empty");
			}
			// Getting the MessageDigest Instance by passing the Algorithm type
			MessageDigest md = MessageDigest.getInstance("SHA-384");
			// Calling generateSecureHashedString to generate the secured hash
			// string.
			securedHash = this.generateSecureHashedString(stringToHash, salt, md);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Exception while generating the secured hash using getSHA384HashedString" + e);
		}
		return securedHash;
	}

	/**
	 * Method Name: getSHA256HashedString This method is used to generate SHA-256
	 * Hashed String using default salt
	 * 
	 * @param stringToHash
	 * @return hashedString
	 */
	public String getSHA256HashedString(String stringToHash) {
		// Declare String securedHash to hold the secured hash string
		String securedHash = null;
		try {
			// Getting the MessageDigest Instance by passing the Algorithm type
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			// Calling generateSecureHashedString to generate the secured hash
			// string.
			securedHash = this.generateSecureHashedString(stringToHash, defaultSalt, md);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Exception while generating the secured hash using getSHA256HashedString" + e);
		}
		return securedHash;
	}

	/**
	 * Method Name: getSHA256HashedString This method is used to generate SHA-256
	 * Hashed String
	 * 
	 * @param stringToHash
	 * @param salt
	 * @return hashedString
	 * @throws Exception
	 */
	public String getSHA256HashedString(String stringToHash, String salt) throws Exception {

		// Declare String securedHash to hold the secured hash string
		String securedHash = null;
		try {
			// validating salt is a null or empty string
			if (salt == null || salt.trim().isEmpty()) {
				throw new Exception("Salt should not be null or empty");
			}
			// Getting the MessageDigest Instance by passing the Algorithm type
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			// Calling generateSecureHashedString to generate the secured hash
			// string.
			securedHash = this.generateSecureHashedString(stringToHash, salt, md);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Exception while generating the secured hash using getSHA256HashedString" + e);
		}
		return securedHash;
	}

	/**
	 * Method Name: getSHA1HashedString This method is used to generate SHA-1 Hashed
	 * String using default salt
	 * 
	 * @param stringToHash
	 * @return hashedString
	 */
	public String getSHA1HashedString(String stringToHash) {
		// Declare String securedHash to hold the secured hash string
		String securedHash = null;
		try {
			// Getting the MessageDigest Instance by passing the Algorithm type
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			// Calling generateSecureHashedString to generate the secured hash
			// string.
			securedHash = this.generateSecureHashedString(stringToHash, defaultSalt, md);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Exception while generating the secured hash using getSHA1HashedString" + e);
		}
		return securedHash;
	}

	/**
	 * Method Name: getSHA1HashedString This method is used to generate SHA-1 Hashed
	 * String
	 * 
	 * @param stringToHash
	 * @param salt
	 * @return hashedString
	 * @throws Exception
	 */
	public String getSHA1HashedString(String stringToHash, String salt) throws Exception {

		// Declare String securedHash to hold the secured hash string
		String securedHash = null;
		try {
			// validating salt is a null or empty string
			if (salt == null || salt.trim().isEmpty()) {
				throw new Exception("Salt should not be null or empty");
			}
			// Getting the MessageDigest Instance by passing the Algorithm type
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			// Calling generateSecureHashedString to generate the secured hash string.
			securedHash = this.generateSecureHashedString(stringToHash, salt, md);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Exception while generating the secured hash using getSHA1HashedString" + e);
		}
		return securedHash;
	}

	/**
	 * Method Name: getSalt This method is used to generate default salt
	 * 
	 * @return saltString
	 */
	public String getSalt() throws NoSuchAlgorithmException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyhh:mm:ss.SSS");
		String currentTimeStamp1 = sdf.format(new Date());
		return currentTimeStamp1;
	}

	/**
	 * Method Name: generateSecureHashedString This method is used to generate the
	 * hashed String using the different Salts and MessageDigests.
	 * 
	 * @param stringToHash
	 * @param salt
	 * @param md
	 * @return hashedString
	 */
	private String generateSecureHashedString(String stringToHash, String salt, MessageDigest md) {
		String generatedSecureHash = null;
		try {
			md.update(salt.getBytes());
			byte[] bytes = md.digest(stringToHash.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; ++i) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedSecureHash = sb.toString();
		} catch (Exception e) {
			LOGGER.error("Exception while generating the secured hash using generateSecureHashedString" + e);
		}
		return generatedSecureHash;
	}

	public static String getSecurePassword(String password, byte[] salt) {

		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(salt);
			byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	private static byte[] getSalt(String randomSalt) throws NoSuchAlgorithmException {

		int[] digits = randomSalt.chars().map(c -> c - '0').toArray();

		byte[] bytes = new byte[randomSalt.length()];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) digits[i];
		}
		return bytes;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {

		//same salt should be passed 
		byte[] salt = getSalt("6615303611119297");
		String password1 = getSecurePassword("Password", salt);
		String password2 = getSecurePassword("Password", salt);
		System.out.println(" Password 1 -> " + password1);
		System.out.println(" Password 2 -> " + password2);
		if (password1.equals(password2)) {
			System.out.println("passwords are equal");
		}
	}

}

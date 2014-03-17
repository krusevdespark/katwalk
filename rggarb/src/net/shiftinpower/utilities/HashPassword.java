package net.shiftinpower.utilities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Base64;

public class HashPassword {
	public String password;

	public String computeSHAHash(String password) {
		MessageDigest mdSha1 = null;
		try {
			mdSha1 = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e1) {
		}
		try {
			mdSha1.update(password.getBytes("ASCII"));
		} catch (UnsupportedEncodingException e) {
		}
		byte[] data = mdSha1.digest();
		try {
			password = convertToHex(data);
		} catch (IOException e) {
		}
		return password;

	}

	private static String convertToHex(byte[] data) throws java.io.IOException {

		StringBuffer stringBuffer = new StringBuffer();
		String hex = null;
		hex = Base64.encodeToString(data, 0);
		stringBuffer.append(hex);
		return stringBuffer.toString();
	}
	
}  // End of Class

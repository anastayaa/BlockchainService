package bcs.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface SHA {

	
	public static StringBuilder sha256(String code) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("sha-256");
			byte[] hashInBytes = messageDigest.digest(code.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
	        for (byte b : hashInBytes) {
	            sb.append(String.format("%02x", b));
	        }
	        return sb;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}

package bcs.service;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.sql.Timestamp;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public final class KeysGenerator implements Serializable{

	
	
	private static final long serialVersionUID = -5485772192747687298L;
	public final static int KEY_LENGTH_LOW = 1024 ;
	public final static int KEY_LENGTH_MEDIUM = 2048 ;
	public final static int KEY_LENGTH_HIGHT = 4096 ;
	private final static String PUBLIC_KEY_FILE = SHA.sha256(new Timestamp(System.currentTimeMillis()).toString() + "publickey").toString() + ".key";
	private final static String PRIVATE_KEY_FILE = SHA.sha256(new Timestamp(System.currentTimeMillis()).toString() + "privatekey").toString() + ".key";
	private transient RSAPrivateKeySpec rsaPrivateKeySpec ;
	private transient RSAPublicKeySpec rsaPublicKeySpec ;
	private transient KeyPairGenerator keyPairGenerator ;
	private transient KeyPair keyPair ;
	
	
	
	
	public KeysGenerator(int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		this.keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		this.keyPairGenerator.initialize(keyLength);
		this.keyPair = this.keyPairGenerator.generateKeyPair();
		PrivateKey privateKey = this.keyPair.getPrivate();
		PublicKey publicKey = this.keyPair.getPublic();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		this.rsaPublicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
		this.rsaPrivateKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
		this.saveKeys(PRIVATE_KEY_FILE, this.rsaPrivateKeySpec.getModulus(), this.rsaPrivateKeySpec.getPrivateExponent());
		this.saveKeys(PUBLIC_KEY_FILE, this.rsaPublicKeySpec.getModulus(), this.rsaPublicKeySpec.getPublicExponent());
	}
	
	
	private void saveKeys(String fileName, BigInteger mod, BigInteger exp) throws IOException {
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName);
			oos = new ObjectOutputStream(new BufferedOutputStream(fos));
			oos.writeObject(mod);
			oos.writeObject(exp);
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if( oos != null) {
				oos.close();
				if (fos != null)
					fos.close();
			}	
		}
	}
	
	public String publicKeyModulus() {
		return this.rsaPublicKeySpec.getModulus().toString();
	}
	
	
	
	public PrivateKey getPrivateKey() throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException{
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		try {
			fis = new FileInputStream(new File(PRIVATE_KEY_FILE));
			ois = new ObjectInputStream(fis);
			BigInteger modulus = (BigInteger) ois.readObject();
			BigInteger exponent = (BigInteger) ois.readObject();
			RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(modulus, exponent);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePrivate(rsaPrivateKeySpec);
		} finally {
			if(ois != null) {
				ois.close();
				if(fis != null)
					fis.close();
			}
		}
		
	}
	
	
	public PublicKey getPublicKey() throws IOException, FileNotFoundException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {
		FileInputStream fis = null ;
		ObjectInputStream ois = null ;
		try {
			fis = new FileInputStream(new File(PUBLIC_KEY_FILE));
			ois = new ObjectInputStream(fis);
			BigInteger modulus = (BigInteger) ois.readObject();
			BigInteger exponent = (BigInteger) ois.readObject();
			RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePublic(rsaPublicKeySpec);
		} finally {
			if(ois != null) {
				ois.close();
				if(fis != null)
					fis.close();
			}
		} 
			
		
	}
	
	
	public static byte[] encrypt(String data, PublicKey publicKey)  {
		byte[] dataBytes = data.getBytes();
		byte[] encryptedData = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			encryptedData = cipher.doFinal(dataBytes);
			return encryptedData;
		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			return null;
		}
		
	}
	
	
	public static byte[] decrypt(byte[] data, PrivateKey privateKey) {
		byte[] decryptedData = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			decryptedData = cipher.doFinal(data);
			return decryptedData;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			return null;
		}
		
	}
	
	
	
	
	
	
	
}

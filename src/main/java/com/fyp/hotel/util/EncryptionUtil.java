package com.fyp.hotel.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class EncryptionUtil {

    @Value("${security.key}")
	private String KEY ;

	public String encrypt(String password) {
		try {
			SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encryptedPassword = cipher.doFinal(password.getBytes());
			return Base64.getEncoder().encodeToString(encryptedPassword);
		} catch (Exception e) {
			System.out.println("Error while encrypting password: " + e.toString());
			return null;
		}
	}

	public String decrypt(String encryptedPassword) {
		try {
			SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] decryptedPassword = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
			return new String(decryptedPassword);
		} catch (Exception e) {
			System.out.println("Error while decrypting password: " + e.toString());
			return null;
		}
	}
}

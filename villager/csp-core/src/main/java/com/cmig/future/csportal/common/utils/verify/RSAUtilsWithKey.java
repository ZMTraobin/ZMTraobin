
package com.cmig.future.csportal.common.utils.verify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtilsWithKey {

	private static Logger logger= LoggerFactory.getLogger(RSAUtilsWithKey.class);

	//#rsa sign key
	private static String rsa_public_key="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDsFPAVdqvWtUmFOjS98zgZSwM5qkCVhvszGGy5hoGh5Npf/Sat51gWUPg5uW0pK5t/unKrgfH6ksNq2arnZ0f9Qaigcxg4PfCahw2+hshSurcbJnkqe4Ew4VT0SU7Y3QQTq942LKVzA3Iz7oLr69/mwucb9TmoGs8LrztCH2HWUwIDAQAB";
	//#rsa sign key
	private static String ras_privete_key ="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOwU8BV2q9a1SYU6NL3zOBlLAzmqQJWG+zMYbLmGgaHk2l/9Jq3nWBZQ+Dm5bSkrm3+6cquB8fqSw2rZqudnR/1BqKBzGDg98JqHDb6GyFK6txsmeSp7gTDhVPRJTtjdBBOr3jYspXMDcjPuguvr3+bC5xv1OagazwuvO0IfYdZTAgMBAAECgYBR+vtcGLeXeaCsOQgzphAnCFt4XNzMj/EPTz4CNukxNO14Q8p4vUtjhH6OXQvBLjlzY2WKlTdYUjq2Ehw8+yqN5szcXfOcKzh2MT8lecmy3IE+jIBtYkOncPta5ELqQmyCs55+Pr3XUFx+W7X2/MIiUUZd8gPTVgELalc2Yd+PEQJBAPib0dOTZmGZBuaJiZ9YYJVi59iLZ6138E29oRc9t+/IDRFJzsXaTyCLEU05qME5JEc0aeocp/e+0pzw9yYZx7cCQQDzGcZsw7i+ew/cqO/kuXAszAK8J+sinYmBrJBqcrHdEpKVE6yc2q91hZPhbFQIGuFPwKca9EPEQRgekRtslQ5FAkAlyEiTRwuAOal6z9xCg0PqcCWcG1OeYWP77J3W0mJp5pVV00nBd/QelgQDIIgtFWoWCDkwgzpjxHRttZFaEQDxAkEAyHnzFvL5Qax+0jxHSVpb1bWQc9Z/pJZCNbw3hHF9mJCebW5CJUB6/fHgmEAbtGtmb7yykhnqYMhFub903EC3+QJAegkGkSjt8imUW+hEqRvmMwG9oEvhnVt7YAcyYo4ESkdBYzONP4BETzjBLUisoqJzhFrbxDLlxwRZCifS/a4UlA==";

	public static void main(String[] args) throws Exception {
		String content = "这是一段将要使用'秘钥字符串'进行加密的字符串!";

		String temp1 = encrypt(content);
		logger.debug(temp1);
		String temp2 = decrypt(temp1);
		logger.debug(temp2);
	}



	public static String decryptString(String encrypttext) {
		String result;
		// RSA私钥解密
		try {
			//新版本固定公私钥解密
			result= RSAUtilsWithKey.decrypt(encrypttext);
		}catch (Exception e){
			//如果是旧版本APP，则调用就版本动态解密
			result= RSAUtils.decryptString(encrypttext);
		}
		return result;
	}

	/**
	 * 此方法描述的是：明文加密
	 * @author:zhangtao107@126.com
	 * @param decrypttext 明文卡号
	 * @return String
	 */
	public static String encrypt(String decrypttext) throws Exception {
		return publicEncrypt(rsa_public_key,decrypttext);
	}

	/**
	 * 此方法描述的是：密文解密
	 * @author:zhangtao107@126.com
	 * @param encrypttext 密文解密
	 * @return String
	 */
	public static String decrypt(String encrypttext) throws Exception {
		return privateDECRYPT(ras_privete_key, encrypttext);
	}

	public static byte[] publicEncrypt(PublicKey key, String str) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(str.getBytes("UTF8"));
	}

	public static String publicEncrypt(String key, String str) throws Exception {
		PublicKey publicKey = getPublicKey(key);
		byte[] data = publicEncrypt(publicKey, str);
		return Base64.encode(data);
	}

	public static byte[] publicDECRYPT(PublicKey key, byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(data);
	}

	public static byte[] privateEncrypt(PrivateKey key, String str) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(str.getBytes("UTF8"));
	}

	public static byte[] privateDECRYPT(PrivateKey key, byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(data);
	}

	public static String privateDECRYPT(String key, String str) throws Exception {
		PrivateKey privateKey = getPrivateKey(key);
		byte[] data =Base64.decode(str);
		byte[] rs = privateDECRYPT(privateKey, data);
		return new String(rs,"UTF-8");
	}

	public static PrivateKey getPrivateKey(String key) throws Exception {

		byte[] keyBytes;

		keyBytes = Base64.decode(key);

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		return privateKey;
	}

	public static PublicKey getPublicKey(String key) throws Exception {

		byte[] keyBytes;

		keyBytes = Base64.decode(key);

		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		PublicKey publicKey = keyFactory.generatePublic(keySpec);

		return publicKey;
	}

}

package com.gary.util.code;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URLEncoder;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAUtil extends Coder {
	public static final String KEY_ALGORITHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	public static final String KET_FILE = "rsakey.key";
	public static String KEY_STORE;

	/**
	 * 用私钥对信息生成数字签名
	 * @param data 加密数据
	 * @param privateKey 私钥
	 * @throws Exception
	 * @return String
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {
		// 解密由base64编码的私钥
		byte[] keyBytes = decryptBASE64(privateKey);

		// 构造PKCS8EncodedKeySpec对象
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

		// KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		// 取私钥匙对象
		PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

		// 用私钥对信息生成数字签名
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(priKey);
		signature.update(data);

		return encryptBASE64(signature.sign());
	}

	/**
	 * 校验数字签名
	 * @param data 加密数据
	 * @param publicKey 公钥
	 * @param sign 数字签名
	 * @throws Exception
	 * @return boolean
	 */
	public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
		// 解密由base64编码的公钥
		byte[] keyBytes = decryptBASE64(publicKey);

		// 构造X509EncodedKeySpec对象
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

		// KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		// 取公钥匙对象
		PublicKey pubKey = keyFactory.generatePublic(keySpec);

		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(pubKey);
		signature.update(data);

		// 验证签名是否正常
		return signature.verify(decryptBASE64(sign));
	}

	/** 
     * 解密
     * 用私钥解密 
     * @param data 加密数据
     * @param key 私钥
     * @return 
     * @throws Exception 
     */  
    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {  
        // 对密钥解密  
        byte[] keyBytes = decryptBASE64(key);  
  
        // 取得私钥  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);  
  
        // 对数据解密  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, privateKey);  
  
        return cipher.doFinal(data);  
    }  
    
    /**
     * 用私钥解密 
     * @param data 加密数据
     * @throws Exception
     * @return byte[]
     */
    public static byte[] decryptByPrivateKey(byte[] data) throws Exception {
    	return decryptByPrivateKey(data, getPrivateKey());
    }
  
    /** 
     * 解密
     * 用公钥解密 
     * @param data 加密数据
     * @param key 公钥
     * @return 
     * @throws Exception 
     */  
    public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {  
        // 对密钥解密  
        byte[] keyBytes = decryptBASE64(key);  
  
        // 取得公钥  
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key publicKey = keyFactory.generatePublic(x509KeySpec);  
  
        // 对数据解密  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, publicKey);  
  
        return cipher.doFinal(data);  
    }  
    
    /**
     * 用公钥解密 
     * @param data 加密数据
     * @throws Exception
     * @return byte[]
     */
    public static byte[] decryptByPublicKey(byte[] data) throws Exception { 
    	return decryptByPublicKey(data, getPublicKey());
    }
  
    /** 
     * 加密
     * 用公钥加密 
     * @param data 原文
     * @param key  公钥
     * @return 
     * @throws Exception 
     */  
    public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {  
        // 对公钥解密  
        byte[] keyBytes = decryptBASE64(key);  
  
        // 取得公钥  
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key publicKey = keyFactory.generatePublic(x509KeySpec);  
  
        // 对数据加密  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
  
        return cipher.doFinal(data);  
    }  
  
    /**
     * 用公钥加密 
     * @param data 原文
     * @throws Exception
     * @return byte[]
     */
    public static byte[] encryptByPublicKey(byte[] data) throws Exception {  
    	return encryptByPublicKey(data, getPublicKey());
    }
    /** 
     * 加密
     * 用私钥加密 
     * @param data  原文
     * @param key  私钥
     * @throws Exception 
     */  
    public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {  
        // 对密钥解密  
        byte[] keyBytes = decryptBASE64(key);  
  
        // 取得私钥  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);  
  
        // 对数据加密  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);  
  
        return cipher.doFinal(data);  
    }
    
    /**
     * 用私钥加密
     * @param data 原文
     * @throws Exception
     * @return byte[]
     */
    public static byte[] encryptByPrivateKey(byte[] data) throws Exception {  
    	return encryptByPrivateKey(data, getPrivateKey());
    }

    /**
     * 获取公钥
     * @param keyPair Key钥
     * @throws Exception
     * @return String
     */
	public static String getPublicKey(KeyPair keyPair) throws Exception {
		return encryptBASE64(keyPair.getPublic().getEncoded());
	}
	/**
	 * 从上次KEY钥 获取公钥 没有则自动新建 
	 * @throws Exception
	 * @return String
	 */
	public static String getPublicKey() throws Exception {
		return encryptBASE64(getKeyPair().getPublic().getEncoded());
	}
	/**
	 * 获取私钥
	 * @param keyPair
	 * @throws Exception
	 * @return String
	 */
	public static String getPrivateKey(KeyPair keyPair) throws Exception {
		return encryptBASE64(keyPair.getPrivate().getEncoded());
	}
	/**
	 * 获取私钥
	 * @throws Exception
	 * @return String
	 */
	public static String getPrivateKey() throws Exception {
		return encryptBASE64(getKeyPair().getPrivate().getEncoded());
	}
	
	/**
	 * 获取KEY钥,没有则新建 存放地址: KEY_STORE,存放文件:KET_FILE
	 * @return KeyPair
	 */
	public static KeyPair getKeyPair() {  
		KeyPair kp = null;
		try {
			FileInputStream fis = new FileInputStream(new File(getKEY_STORE(), KET_FILE));
			ObjectInputStream oos = new ObjectInputStream(fis);  
			kp = (KeyPair) oos.readObject();
			oos.close();
			fis.close();
		} catch (Exception e) {
			try {
				kp = initKey();
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			}
		}  
        
        return kp;  
    }
	
	/**
	 * 保存Key钥
	 * @param kp
	 * @return void
	 */
	public static void saveKeyPair(KeyPair kp) {  
		try {
			FileOutputStream fos = new FileOutputStream(new File(getKEY_STORE(), KET_FILE));  
	        ObjectOutputStream oos = new ObjectOutputStream(fos);  
	        // 生成密钥  
	        oos.writeObject(kp);  
	        oos.close();  
	        fos.close();  
		} catch (Exception e) {
			e.printStackTrace();
		}  
    }

	/**
	 * 初始化KEY
	 * @throws NoSuchAlgorithmException
	 * @return KeyPair
	 */
	public static KeyPair initKey(boolean isSave) throws NoSuchAlgorithmException {
		System.err.println("初始化KEY...");
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);

		KeyPair keyPair = keyPairGen.generateKeyPair();
		if(isSave)
			saveKeyPair(keyPair);
		return keyPair;
	}
	
	public static KeyPair initKey() throws NoSuchAlgorithmException {
		return initKey(true);
	}

	public static void main(String[] args) throws Exception {
		String publicKey = getPublicKey();
		String privateKey = getPrivateKey();
		System.out.println("公钥:\r" + publicKey);
		System.out.println("私钥：\r" + privateKey);

		String inputStr = "111111";
		byte[] data = inputStr.getBytes();
		byte[] encodedData = RSAUtil.encryptByPublicKey(data, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCO24VgbBcvvSKeZJ8k6uAmTRo3m8X4p22acMfWQ5Tfrj0tbSM5ng0bQkozmzRxRhXTPFz3R87yarnMIkTgPwX/+rIrgd2DC0Sy9YceIhW77ATNwMcJTmXwmxSWY7cZiwAlTVEZb5rTTsyUABxL81s3eZal5K5U6MbfE03AfZyIMQIDAQAB");
		String encryptBASE64 = encryptBASE64(encodedData);
		System.out.println(encryptBASE64);
		System.out.println(URLEncoder.encode(encryptBASE64, "UTF-8"));
		byte[] decodedData = RSAUtil.decryptByPrivateKey(decryptBASE64(encryptBASE64));
		
		String outputStr = new String(decodedData);
		System.out.println("公钥加密前：" + inputStr);
		System.out.println("私钥解密后：" + outputStr);
		
		decodedData = RSAUtil.decryptByPrivateKey(decodeBase64(outputStr));
		System.out.println(new String(decodedData));
	}

	public static void setKEY_STORE(String kEY_STORE) {
		KEY_STORE = kEY_STORE;
	}

	public static String getKEY_STORE() {
		if(KEY_STORE == null || "classpath".equalsIgnoreCase(KEY_STORE)){
			return RSAUtil.class.getResource("/").getPath();
		}
		return KEY_STORE;
	}
}

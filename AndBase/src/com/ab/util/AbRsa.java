package com.ab.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class AbRsa {
	
	private static final String ALGORITHM = "RSA";
	
	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";
	
	/**
	 * 随机生成RSA密钥对
	 * @param keyLength 密钥长度，范围：512～2048 一般1024
	 * @return
	 */
	public static KeyPair generateRSAKeyPair(int keyLength){
		try{
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
			kpg.initialize(keyLength);
			return kpg.genKeyPair();
		} catch (NoSuchAlgorithmException e){
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 
	 * RSA 公钥加密.
	 * 用公钥对一个字串进行加密然后用私钥解密,是RSA加密,只能加密少量数据.一般是用来加密会话密钥
	 * @param content        内容
	 * @param publicKey      公钥 
	 * @return  加密的内容（待发送）
	 */
	public static String encrypt(String content, String publicKey) {
		try {
			PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, publicKey);
			return encrypt(content,pubkey);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * RSA 公钥加密.
	 * 用公钥对一个字串进行加密然后用私钥解密,是RSA加密,只能加密少量数据.一般是用来加密会话密钥
	 * @param content        内容
	 * @param publicKey      公钥 
	 * @return  加密的内容（待发送）
	 */
	public static String encrypt(String content, PublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte plaintext[] = content.getBytes("UTF-8");
			byte[] output = cipher.doFinal(plaintext);
			String s = new String(AbBase64.encode(output));
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * RSA 用私钥解密.
	 * @param encryptString
	 * @param privateKey
	 * @return
	 */
	public byte[] decrypt(String encryptString, String privateKey) {
		try {
			byte[] encryptByte = AbBase64.decode(encryptString);
			PrivateKey prikey = getPrivateKeyFromX509(ALGORITHM, privateKey);
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, prikey);
			return cipher.doFinal(encryptByte);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * RSA 用私钥解密.
	 * @param encryptString
	 * @param privateKey
	 * @return
	 */
	public byte[] decrypt(String encryptString, PrivateKey privateKey) {
		try {
			byte[] encryptByte = AbBase64.decode(encryptString);
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(encryptByte);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    /**
     * 
     * 获取数字签名.
     * 利用私钥对一个字串进行加密,得到数字签名.是用来证明信息的确来自密钥所有人.然后用公钥解密。
     * @param content     内容
     * @param privateKey  私钥
     * @return  数字签名
     */
	public static String sign(String content, String privateKey) {
		String charset = "utf-8";
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					AbBase64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(charset));

			byte[] signed = signature.sign();

			return AbBase64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * 验证
	 * @param content  内容
	 * @param sign     签名
	 * @param publicKey  公钥
	 * @return
	 */
	public static boolean doCheck(String content, String sign, String publicKey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			byte[] encodedKey = AbBase64.decode(publicKey);
			PublicKey pubKey = keyFactory
					.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update(content.getBytes("utf-8"));

			boolean bverify = signature.verify(AbBase64.decode(sign));
			return bverify;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * 
	 * 生成公钥.
	 * @param algorithm
	 * @param bysKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws Exception
	 */
	private static PublicKey getPublicKeyFromX509(String algorithm,
			String bysKey) throws NoSuchAlgorithmException, Exception {
		byte[] decodedKey = AbBase64.decode(bysKey);
		X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);

		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		return keyFactory.generatePublic(x509);
	}
	
	/**
	 * 
	 * 生成私钥.
	 * @param algorithm
	 * @param bysKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws Exception
	 */
	private static PrivateKey getPrivateKeyFromX509(String algorithm,
			String bysKey) throws NoSuchAlgorithmException, Exception {
		byte[] decodedKey = AbBase64.decode(bysKey);
		X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);

		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		return keyFactory.generatePrivate(x509);
	}
	
	/**
     * 通过公钥byte[](publicKey.getEncoded())将公钥还原，适用于RSA算法
     * 
     * @param keyBytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey getPublicKey(byte[] keyBytes) throws NoSuchAlgorithmException,
            InvalidKeySpecException{
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }
 
    /**
     * 通过私钥byte[]将公钥还原，适用于RSA算法
     * 
     * @param keyBytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey getPrivateKey(byte[] keyBytes) throws NoSuchAlgorithmException,
            InvalidKeySpecException{
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }
 
    /**
     * 使用N、e值还原公钥
     * 
     * @param modulus
     * @param publicExponent
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey getPublicKey(String modulus, String publicExponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException{
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }
 
    /**
     * 使用N、d值还原私钥
     * 
     * @param modulus
     * @param privateExponent
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey getPrivateKey(String modulus, String privateExponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException{
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }
 
    /**
     * 从字符串中加载公钥
     * 
     * @param publicKeyStr
     *            公钥数据字符串
     * @throws Exception
     *             加载公钥时产生的异常
     */
    public static PublicKey loadPublicKey(String publicKey) throws Exception{
        try {
            byte[] buffer = AbBase64.decode(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e){
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e){
            throw new Exception("公钥非法");
        } catch (NullPointerException e){
            throw new Exception("公钥数据为空");
        }
    }
 
    /**
     * 从字符串中加载私钥
 
     * 加载时使用的是PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。
     * 
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static PrivateKey loadPrivateKey(String privateKey) throws Exception{
        try{
            byte[] buffer = AbBase64.decode(privateKey);
            // X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e){
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e){
            throw new Exception("私钥非法");
        } catch (NullPointerException e){
            throw new Exception("私钥数据为空");
        }
    }
 
    /**
     * 从文件中输入流中加载公钥
     * 
     * @param in
     *            公钥输入流
     * @throws Exception
     *             加载公钥时产生的异常
     */
    public static PublicKey loadPublicKey(InputStream in) throws Exception{
        try{
            return loadPublicKey(readKey(in));
        } catch (IOException e){
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e){
            throw new Exception("公钥输入流为空");
        }
    }
 
    /**
     * 从文件中加载私钥
     * 
     * @param keyFileName
     *            私钥文件名
     * @return 是否成功
     * @throws Exception
     */
    public static PrivateKey loadPrivateKey(InputStream in) throws Exception{
        try {
            return loadPrivateKey(readKey(in));
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e){
            throw new Exception("私钥输入流为空");
        }
    }
 
    /**
     * 读取密钥信息
     * 
     * @param in
     * @return
     * @throws IOException
     */
    private static String readKey(InputStream in) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String readLine = null;
        StringBuilder sb = new StringBuilder();
        while ((readLine = br.readLine()) != null) {
            if (readLine.charAt(0) == '-'){
                continue;
            } else{
                sb.append(readLine);
                sb.append(' ');
            }
        }
 
        return sb.toString();
    }
 
    /**
     * 打印公钥信息
     * 
     * @param publicKey
     */
    public static void printPublicKeyInfo(PublicKey publicKey){
        RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
        System.out.println("----------RSAPublicKey----------");
        System.out.println("Modulus.length=" + rsaPublicKey.getModulus().bitLength());
        System.out.println("Modulus=" + rsaPublicKey.getModulus().toString());
        System.out.println("PublicExponent.length=" + rsaPublicKey.getPublicExponent().bitLength());
        System.out.println("PublicExponent=" + rsaPublicKey.getPublicExponent().toString());
    }
 
    public static void printPrivateKeyInfo(PrivateKey privateKey){
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
        System.out.println("----------RSAPrivateKey ----------");
        System.out.println("Modulus.length=" + rsaPrivateKey.getModulus().bitLength());
        System.out.println("Modulus=" + rsaPrivateKey.getModulus().toString());
        System.out.println("PrivateExponent.length=" + rsaPrivateKey.getPrivateExponent().bitLength());
        System.out.println("PrivatecExponent=" + rsaPrivateKey.getPrivateExponent().toString());
 
    }
    
    
    
}

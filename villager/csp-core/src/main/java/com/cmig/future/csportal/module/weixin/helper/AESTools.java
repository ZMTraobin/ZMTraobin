package com.cmig.future.csportal.module.weixin.helper;

import com.cmig.future.csportal.common.utils.verify.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 16:49 2017/12/21.
 * @Modified by zhangtao on 16:49 2017/12/21.
 */
public class AESTools {

	private static final  int INIT_SIZE = 128;
	private static final  String SECRET_IALGORITHM = "AES";
	private static final int ZERO = 0;
	private static final int ONE = 1;
	private static String derectory = "D:";

	public static void main(String[] args) {
		try {
//          byte[] key = initKey();
//          String  str = Base64.encodeBase64String(key);
			String  str = "d4d8af5c2da31d10";
			System.out.println(str);
			String sourceFilePath = derectory+ File.separator+"Mer_20160921_188888888888.txt";
			String targetFilePath = derectory+File.separator+"Mer_20160921_188888888889.txt";
			String finalFilePath = derectory+File.separator+"Mer_20160921_188888888890.txt";

			encryptFile(sourceFilePath,targetFilePath,str);
			//decriptFile(targetFilePath,finalFilePath,str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件处理方法
	 * code为加密或者解密的判断条件
	 * key 加密密钥
	 */
	private static void doFile(int code, String sourceFilePath,String targetFilePath, String key) throws Exception{

	/*	File file = new File(sourceFilePath);
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
				file));
		byte[] bytIn = new byte[(int) file.length()];
		bis.read(bytIn);
		bis.close();*/
		byte[] bytIn="aaaaaa".getBytes();
		//aes对称加密算法  加密和解密用到的密钥是相同的
		//构造密钥生成器，指定为AES算法,不区分大小写
		/*KeyGenerator kgen = KeyGenerator.getInstance(SECRET_IALGORITHM);
		//根据key规则初始化密钥生成器
		//生成一个128位的随机源,根据传入的字节数组
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(key.getBytes());
		kgen.init(INIT_SIZE, secureRandom);
//        kgen.init(INIT_SIZE, new SecureRandom(key.getBytes()));
		//产生原始对称密钥
		SecretKey skey = kgen.generateKey();
		//获得原始对称密钥的字节数组
		byte[] raw = skey.getEncoded();
		//根据字节数组生成AES密钥
		SecretKeySpec skeySpec = new SecretKeySpec(raw, SECRET_IALGORITHM);
*/
		System.out.println(new String(Base64.encode(key.getBytes())));
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), SECRET_IALGORITHM);

		//根据指定算法AES自成密码器
		Cipher cipher = Cipher.getInstance(SECRET_IALGORITHM);

		//初始化密码器，第一个参数为加密(ENCRYPT_MODE)或者解密(DECRYPT_MODE)操作，第二个参数为使用的KEY
		if(0 == code){
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		}else if(1 == code){
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		}
		//将数据加密或解密
		byte[] bytOut = cipher.doFinal(bytIn);

		System.out.println(new String(Base64.encode(bytOut)));

	/*	BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(new File(targetFilePath)));
		bos.write(bytOut);
		bos.close();*/

	}

	//文件加密
	public static void encryptFile(String sourceFilePath,String targetFilePath, String key) throws Exception{

		doFile(ZERO,sourceFilePath,targetFilePath,key);

	}

	//文件解密
	public static void decriptFile(String sourceFilePath,String targetFilePath, String key) throws Exception{

		doFile(ONE,sourceFilePath,targetFilePath,key);

	}

	public static byte[] initKey() throws Exception{
		//实例化
		KeyGenerator kgen = KeyGenerator.getInstance(SECRET_IALGORITHM);
		//设置密钥长度
		kgen.init(128);
		//生成密钥
		SecretKey skey = kgen.generateKey();
		//返回密钥的二进制编码
		return skey.getEncoded();
	}
}

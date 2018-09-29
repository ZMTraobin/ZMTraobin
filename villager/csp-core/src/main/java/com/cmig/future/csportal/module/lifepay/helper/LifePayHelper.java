package com.cmig.future.csportal.module.lifepay.helper;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.utils.HttpUtil;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 14:59 2017/11/21.
 * @Modified by zhangtao on 14:59 2017/11/21.
 */
public class LifePayHelper {

	private static final Logger logger= LoggerFactory.getLogger(LifePayHelper.class);

	public static String serverUrl= Global.getConfig("LIFE.PAY.serverUrl");
	public static String clientAbbr=Global.getConfig("LIFE.PAY.clientAbbr");
	public static String Privatekey=Global.getConfig("LIFE.PAY.Privatekey");
	public static String Publickey= Global.getConfig("LIFE.PAY.Publickey");

	public static String SUCCESS_CODE="0";
	public static Map errorMap=new HashMap();
	static {
		errorMap.put("0","请求成功");
		errorMap.put("10001","加密验证失败");
		errorMap.put("10002","客户端简称不正确");
		errorMap.put("1004","系统异常");
		errorMap.put("1005","参数错误");
		errorMap.put("20001","订单不存在");
		errorMap.put("554008","商户订单号已存在,无法重复下单");
		errorMap.put("554009","查无此记录");
		errorMap.put("554999","未查到符合条件的数据");
	}
	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 *
	 * @param paramsMap 需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	protected static String createLinkString(Map<String, String> paramsMap) {
		List<String> keys = new ArrayList<String>(paramsMap.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = paramsMap.get(key);

			if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		logger.debug(prestr);
		return prestr;
	}

	public static String encodeByRSA(Map<String, String> paramsMap) throws Exception {
		return encodeByRSA(createLinkString(paramsMap),Privatekey,null);
	}

	public static boolean verify(Map<String, String> paramsMap,String sign) throws Exception {
		return verify(createLinkString(paramsMap),sign,Publickey,null);
	}

	/**
	 * 根据私钥加密字符串,编码集为UTF-8
	 * @param data
	 * @param privateKey
	 * @param encyName       默认为 "MD5withRSA"
	 * @return
	 * @throws Exception
	 */
	public static String encodeByRSA(String data,String privateKey, String encyName) throws Exception {
		if (StringUtil.isBlank(encyName)) {
			encyName = "MD5withRSA";
		}
		BASE64Decoder base64Decoder = new BASE64Decoder();
		BASE64Encoder base64Encoder = new BASE64Encoder();
		KeyFactory keyf = KeyFactory.getInstance("RSA");
		PrivateKey privateKeyPair = keyf.generatePrivate(new PKCS8EncodedKeySpec(base64Decoder.decodeBuffer(privateKey)));
		java.security.Signature signet = java.security.Signature.getInstance(encyName);
		signet.initSign(privateKeyPair);
		signet.update(data.getBytes("utf-8"));
		return base64Encoder.encode(signet.sign());
	}



	/**
	 * 根据公钥验证加密串 ,编码集为UTF-8
	 * @param data
	 * @param sign
	 * @param publicKeyString
	 * @param encyName       默认为 "MD5withRSA"
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(String data, String sign, String publicKeyString, String encyName) throws Exception {
		if (StringUtil.isBlank(encyName)) {
			encyName = "MD5withRSA";
		}
		BASE64Decoder base64Decoder = new BASE64Decoder();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKeyStr = keyFactory.generatePublic(new X509EncodedKeySpec(base64Decoder.decodeBuffer(publicKeyString)));
		java.security.Signature signet = java.security.Signature.getInstance(encyName);
		signet.initVerify(publicKeyStr);
		signet.update(data.getBytes("utf-8"));
		return signet.verify(base64Decoder.decodeBuffer(sign));
	}

	public static void main(String[] args) {
		Map map=new HashMap();
		map.put("cityCode","021");
		map.put("clientAbbr",clientAbbr);
		try {
			map.put("Sign",encodeByRSA(map));
			HttpUtil.post("https://cs.keycoin.cn/plat/plat/getLiftFeeInfo",map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		map=new HashMap();
		map.put("cityCode","021");
		map.put("clientAbbr",clientAbbr);
		map.put("productId","1000000211");
		try {
			map.put("Sign",encodeByRSA(map));
			HttpUtil.post("https://cs.keycoin.cn/plat/plat/productQuery",map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		map=new HashMap();
		map.put("billOrgId","888880001602900");
		map.put("clientAbbr",clientAbbr);
		map.put("productId","1000000211");
		try {
			map.put("Sign",encodeByRSA(map));
			HttpUtil.post("https://cs.keycoin.cn/plat/plat/billConfQuery",map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map=new HashMap();
		map.put("billOrgId","888880001602900");
		map.put("clientAbbr",clientAbbr);
		map.put("productId","1000000211");
		map.put("startMonth","201701");
		//map.put("billNo","00000085");
		//map.put("searchType","0");
		map.put("barcode","0830201710012700129900006906");
		map.put("searchType","2");
		try {
			map.put("Sign",encodeByRSA(map));
			HttpUtil.post("https://cs.keycoin.cn/plat/plat/queryByBillNo",map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.debug(new Long(new Date().getTime()).toString());
	}
}

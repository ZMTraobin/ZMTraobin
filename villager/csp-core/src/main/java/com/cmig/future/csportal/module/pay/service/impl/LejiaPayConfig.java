package com.cmig.future.csportal.module.pay.service.impl;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 14:52 2017/5/27.
 * @Modified by zhangtao on 14:52 2017/5/27.
 */
public class LejiaPayConfig {

	public static String LEJIA_PAY_PARAMS_KEY="csp.lejiaPay.params";

	public static String DEFAULT_MERCHANT_ID="300010000001";
	//服务地址
	protected static String serverUrl;
	//交易类型 代扣
	protected static String transType="05";
	//成功状态码
	public static String SUCCESS_CODE="0000";

	static{
		Map map=new HashMap();
		String temp=Global.getConfig("lejiapay.merchantId_inchannel_signKey","");
		if(!StringUtils.isEmpty(temp)){
			String[] merchants=temp.split("#@#");
			if(null!=merchants&&merchants.length>0){
				for(String str:merchants){
					String merchantId=str.split("_")[0];
					String inchannel=str.split("_")[1];
					String signKey=str.split("_")[2];

					JSONObject jsonObject=new JSONObject();
					jsonObject.put("merchantId",merchantId);
					jsonObject.put("inchannel",inchannel);
					jsonObject.put("signKey",signKey);
					map.put(merchantId,jsonObject);
				}
			}
		}

		JedisUtils.setObject(LEJIA_PAY_PARAMS_KEY,map);
		serverUrl= Global.getConfig("lejiapay.serverUrl");
	}


	public static JSONObject getParams(String merchantId){
		Map mapParam= (Map) JedisUtils.getObject(LEJIA_PAY_PARAMS_KEY);
		JSONObject jsonObject= (JSONObject) mapParam.get(merchantId);
		return jsonObject;
	}

	//商户号
	public static String getMerchantId(String merchantId){
		JSONObject jsonObject=getParams(merchantId);
		return jsonObject.get("merchantId").toString();
	}

	//接入渠道
	public static String getInchannel(String merchantId){
		JSONObject jsonObject=getParams(merchantId);
		return jsonObject.get("inchannel").toString();
	}
	//签名密钥
	public static String getSignKey(String merchantId){
		JSONObject jsonObject=getParams(merchantId);
		return jsonObject.get("signKey").toString();
	}


	/**
	 * 卡认证状态 绑定:Y;解绑:N;审核中:P
	 */
	public static final String CARD_STATUS_Y="Y";
	public static final String CARD_STATUS_N="N";
	public static final String CARD_STATUS_P="P";
}

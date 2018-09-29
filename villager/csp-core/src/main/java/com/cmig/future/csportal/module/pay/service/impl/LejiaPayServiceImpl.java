package com.cmig.future.csportal.module.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.pay.service.IPayService;
import com.cmpay.common.security.util.SignUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 10:47 2017/5/27.
 * @Modified by zhangtao on 10:47 2017/5/27.
 */
@Service
public class LejiaPayServiceImpl implements IPayService {

	@Override
	public String pay(Map<String, Object> chargeMap)throws Exception {

		if(null==chargeMap){
			throw new DataWarnningException("参数不能为空");
		}

		if(StringUtils.isEmpty(chargeMap.get("userId"))){
			throw new DataWarnningException("userId 不能为空");
		}
		if(StringUtils.isEmpty(chargeMap.get("amount"))){
			throw new DataWarnningException("amount 不能为空");
		}
		if(StringUtils.isEmpty(chargeMap.get("cardNo"))){
			throw new DataWarnningException("cardNo 不能为空");
		}
		if(StringUtils.isEmpty(chargeMap.get("origiOrderId"))){
			throw new DataWarnningException("origiOrderId 不能为空");
		}
		if(StringUtils.isEmpty(chargeMap.get("orderIp"))){
			throw new DataWarnningException("orderIp 不能为空");
		}
		if(StringUtils.isEmpty(chargeMap.get("idNo"))){
			throw new DataWarnningException("idNo 不能为空");
		}
		if(StringUtils.isEmpty(chargeMap.get("idType"))){
			throw new DataWarnningException("idType 不能为空");
		}
		if(StringUtils.isEmpty(chargeMap.get("name"))){
			throw new DataWarnningException("name 不能为空");
		}
		if(StringUtils.isEmpty(chargeMap.get("bankMobile"))){
			throw new DataWarnningException("bankMobile 不能为空");
		}
		if(StringUtils.isEmpty(chargeMap.get("merchantId"))){
			throw new DataWarnningException("merchantId 不能为空");
		}
		String merchantId=chargeMap.get("merchantId").toString();
		Map<String,String> map=new HashMap<String,String>();
		map.put("merchantId", LejiaPayConfig.getMerchantId(merchantId));//商户号
		map.put("inchannel", LejiaPayConfig.getInchannel(merchantId));//接入渠道
		map.put("userId", chargeMap.get("userId").toString());//用户id
		map.put("amount", chargeMap.get("amount").toString());//金额
		map.put("cardNo", chargeMap.get("cardNo").toString());//卡号
		map.put("origiOrderId", chargeMap.get("origiOrderId").toString());//渠道订单号
		map.put("payCode", chargeMap.get("payCode")==null?"":chargeMap.get("payCode").toString());//支付渠道
		map.put("transType", chargeMap.get("transType")==null?LejiaPayConfig.transType:chargeMap.get("transType").toString());//交易类型
		map.put("orderIp", chargeMap.get("orderIp").toString());//请求ip
		map.put("idNo", chargeMap.get("idNo").toString());//证件号
		map.put("idType", chargeMap.get("idType").toString());//证件类型
		map.put("name", chargeMap.get("name").toString());//姓名
		map.put("bankMobile", chargeMap.get("bankMobile").toString());//银行预留手机号
		map.put("bankCode", chargeMap.get("bankCode")==null?"":chargeMap.get("bankCode").toString());//银行编码

		JSONObject jsonObject=(JSONObject)JSON.toJSON(map);
		String sign=sign(jsonObject,LejiaPayConfig.getMerchantId(merchantId));
		jsonObject.put("sign", sign);
		String responseText= HttpUtil.post(LejiaPayConfig.serverUrl+"/payment/payCut",jsonObject.toJSONString());
		JSONObject response=JSONObject.parseObject(responseText);
		if(checkSign(response,response.get("sign").toString(),LejiaPayConfig.getMerchantId(merchantId))){
			return responseText;
		}else{
			throw new DataWarnningException("验签未通过");
		}
	}

	@Override
	public boolean serverNotify(HttpServletRequest request, HttpServletResponse response) {
		return false;
	}

	@Override
	public String searchPayStatus(Map<String, Object> paramMap)throws Exception {

		if(null==paramMap){
			throw new DataWarnningException("参数不能为空");
		}

		if(StringUtils.isEmpty(paramMap.get("origOrderNo"))){
			throw new DataWarnningException("origOrderNo不能为空");
		}

		if(StringUtils.isEmpty(paramMap.get("merchantId"))){
			throw new DataWarnningException("merchantId 不能为空");
		}
		String merchantId=paramMap.get("merchantId").toString();

		Map<String,String> map=new HashMap<String,String>();
		map.put("merId", LejiaPayConfig.getMerchantId(merchantId));
		map.put("origOrderNo", paramMap.get("origOrderNo").toString());
		JSONObject jsonObject=(JSONObject)JSON.toJSON(map);
		String sign=sign(jsonObject,LejiaPayConfig.getMerchantId(merchantId));
		jsonObject.put("sign", sign);
		String responseText= HttpUtil.post(LejiaPayConfig.serverUrl+"/payment/queryOrder",jsonObject.toJSONString());
		JSONObject response=JSONObject.parseObject(responseText);
		if(checkSign(response,response.get("sign").toString(),LejiaPayConfig.getMerchantId(merchantId))){
			return responseText;
		}else{
			throw new DataWarnningException("验签未通过");
		}
	}

	@Override
	public String refund(Map<String, Object> refundMap) throws Exception {
		if(null==refundMap){
			throw new DataWarnningException("参数不能为空");
		}

		if(StringUtils.isEmpty(refundMap.get("userId"))){
			throw new DataWarnningException("userId不能为空");
		}

		if(StringUtils.isEmpty(refundMap.get("refundAmt"))){
			throw new DataWarnningException("refundAmt不能为空");
		}

		if(StringUtils.isEmpty(refundMap.get("origiOrderId"))){
			throw new DataWarnningException("origiOrderId不能为空");
		}

		if(StringUtils.isEmpty(refundMap.get("merchantId"))){
			throw new DataWarnningException("merchantId 不能为空");
		}
		String merchantId=refundMap.get("merchantId").toString();

		Map<String,String> map=new HashMap<String,String>();
		map.put("merId", LejiaPayConfig.getMerchantId(merchantId));
		map.put("inchannel", LejiaPayConfig.getInchannel(merchantId));
		map.put("userId", refundMap.get("userId").toString());
		map.put("refundAmt", refundMap.get("refundAmt").toString());
		map.put("origiOrderId", refundMap.get("origiOrderId").toString());
		map.put("orderIp", refundMap.get("orderIp")==null?"":refundMap.get("orderIp").toString());
		JSONObject jsonObject=(JSONObject)JSON.toJSON(map);
		String sign=sign(jsonObject,LejiaPayConfig.getMerchantId(merchantId));
		jsonObject.put("sign", sign);
		String responseText= HttpUtil.post(LejiaPayConfig.serverUrl+"/payment/payRefund",jsonObject.toJSONString());
		JSONObject response=JSONObject.parseObject(responseText);
		if(checkSign(response,response.get("sign").toString(),LejiaPayConfig.getMerchantId(merchantId))){
			return responseText;
		}else{
			throw new DataWarnningException("验签未通过");
		}
	}

	@Override
	public boolean refundNotify(HttpServletRequest request, HttpServletResponse response)throws Exception {
		return false;
	}

	@Override
	public boolean pageNotify(HttpServletRequest request, HttpServletResponse response)throws Exception {
		return false;
	}

	@Override
	public String check(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return null;
	}

	@Override
	public String cancelorder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return null;
	}

	@Override
	public String cardAuth(Map<String, Object> paramMap) throws Exception {
		if(null==paramMap){
			throw new DataWarnningException("参数不能为空");
		}

		if(StringUtils.isEmpty(paramMap.get("userId"))){
			throw new DataWarnningException("userId 不能为空");
		}
		if(StringUtils.isEmpty(paramMap.get("cardNo"))){
			throw new DataWarnningException("cardNo 不能为空");
		}
		if(StringUtils.isEmpty(paramMap.get("cardType"))){
			throw new DataWarnningException("cardType 不能为空");
		}
		if(StringUtils.isEmpty(paramMap.get("idNo"))){
			throw new DataWarnningException("idNo 不能为空");
		}
		if(StringUtils.isEmpty(paramMap.get("idType"))){
			throw new DataWarnningException("idType 不能为空");
		}
		if(StringUtils.isEmpty(paramMap.get("name"))){
			throw new DataWarnningException("name 不能为空");
		}
		if(StringUtils.isEmpty(paramMap.get("bankMobile"))){
			throw new DataWarnningException("bankMobile 不能为空");
		}
		if(StringUtils.isEmpty(paramMap.get("merchantId"))){
			throw new DataWarnningException("merchantId 不能为空");
		}
		if(StringUtils.isEmpty(paramMap.get("bankCode"))){
			throw new DataWarnningException("bankCode 不能为空");
		}
		String merchantId=paramMap.get("merchantId").toString();

		Map<String,String> map=new HashMap<String,String>();
		map.put("merchantId", LejiaPayConfig.getMerchantId(merchantId));//商户号
		map.put("userId", paramMap.get("userId").toString());//用户ID
		map.put("inchannel", LejiaPayConfig.getInchannel(merchantId));//接入渠道
		//认证渠道，目前交行和招行选金运通、其余选择银联接口
		if("0006".equals(paramMap.get("bankCode"))||"0012".equals(paramMap.get("bankCode"))){
			map.put("authChannel","CMPAY0002");
		}else{
			map.put("authChannel","CMPAY0001");
		}
		map.put("cardNo", paramMap.get("cardNo").toString());//卡号
		map.put("cardType", paramMap.get("cardType").toString());//卡类型
		map.put("idNo", paramMap.get("idNo").toString());//证件号
		map.put("idType", paramMap.get("idType").toString());//证件类型
		map.put("name", paramMap.get("name").toString());//姓名
		map.put("bankMobile", paramMap.get("bankMobile").toString());//预留手机号
		map.put("bankCode", paramMap.get("bankCode")==null?"":paramMap.get("bankCode").toString());//银行编码
		map.put("terminalType", paramMap.get("terminalType")==null?"01":paramMap.get("terminalType").toString());//终端类
		JSONObject jsonObject=(JSONObject)JSON.toJSON(map);
		String sign=sign(jsonObject,LejiaPayConfig.getMerchantId(merchantId));
		jsonObject.put("sign", sign);//签名
		String responseText= HttpUtil.post(LejiaPayConfig.serverUrl+"/payment/payAuth",jsonObject.toJSONString());
		JSONObject response=JSONObject.parseObject(responseText);
		if(checkSign(response,response.get("sign").toString(),LejiaPayConfig.getMerchantId(merchantId))){
			return responseText;
		}else{
			throw new DataWarnningException("验签未通过");
		}
	}

	@Override
	public String queryCardInfo(Map<String, Object> paramMap) throws Exception {
		if(null==paramMap||paramMap.get("cardNo")==null){
			throw new DataWarnningException("卡号不能为空");
		}
		if(StringUtils.isEmpty(paramMap.get("merchantId"))){
			throw new DataWarnningException("merchantId 不能为空");
		}
		String merchantId=paramMap.get("merchantId").toString();
		JSONObject jsonObject=(JSONObject)JSON.toJSON(paramMap);
		String responseText= HttpUtil.post(LejiaPayConfig.serverUrl+"/payment/queryCardInfoByCardNo",jsonObject.toJSONString());
		return responseText;
	}

	public String sign(JSONObject jsonObject,String merchantId) {
		String sign=SignUtil.genMd5Sign(jsonObject,LejiaPayConfig.getSignKey(merchantId));
		return sign;
	}

	public boolean checkSign(JSONObject jsonObject, String sign,String merchantId) {
		String thisSign=SignUtil.genMd5Sign(jsonObject,LejiaPayConfig.getSignKey(merchantId));
		if(thisSign.equals(sign)){
			return true;
		}
		return false;
	}
}

package com.cmig.future.csportal.api.app.user.service.impl;

import java.math.BigDecimal;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmig.future.csportal.api.app.user.dto.UserInfoReq;
import com.cmig.future.csportal.api.app.user.service.IAppUserInfoService;
import com.cmig.future.csportal.common.utils.ExceptionConstants;
import com.cmig.future.csportal.common.utils.RequestUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * class name: AppUserInfoServiceImpl 
 * author: fri
 * date: 2017年4月6日
 * function: 设置接口
 */
@Service
@Transactional
public class AppUserInfoServiceImpl implements IAppUserInfoService {
	
	@Value("#{configProperties['itou.res.core.user']}")
	private String iTOU_V2_USER;//核心接口请求地址
	
	@Value("#{configProperties['tgt.url']}")
	private String TGT_URL;
	
	@Value("#{configProperties['tgt.username']}")
	private String TGT_USERNAME;//
	
	@Value("#{configProperties['tgt.password']}")
	private String TGT_PASSWORD;//
	
	@Value("#{configProperties['tgt.granttype']}")
	private String TGT_GRANTTYPE;//
	
	@Value("#{configProperties['tgt.clientid']}")
	private String TGT_CLIENTID;//
	
	@Value("#{configProperties['tgt.clientsecret']}")
	private String TGT_CLIENTSECRET;//
	
	private Logger logger = LoggerFactory.getLogger(AppUserInfoServiceImpl.class);
	
	@Override
	public JSONObject queryUserMyWealth(Integer retype, UserInfoReq userInfoReq) {
		logger.info("我的财富:"+retype);
		String[] reParam = {"tgt"};
		String[] reParam2 = {"tgt","page","size"};
		JSONObject returnJson = new JSONObject();
		try {
			Boolean isOk = true;
			if(2 != retype){
				isOk = RequestUtils.isReParamOk(reParam, userInfoReq);
			}else{
				isOk = RequestUtils.isReParamOk(reParam2, userInfoReq);
			}
			if(!isOk){
				returnJson.put(RequestUtils.RE_MESSAGE, RequestUtils.TO_SUCCESS);
				returnJson.put(RequestUtils.RE_DATA, ExceptionConstants.DATA_IS_REQUIRED.getError());
				return returnJson;
			}
			String TGT = userInfoReq.getTgt();
			//通过TGT获取token/userId
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("username", TGT_USERNAME);
			jsonObject.put("password", TGT_PASSWORD);
			jsonObject.put("grant_type", TGT_GRANTTYPE);
			jsonObject.put("client_id", TGT_CLIENTID);
			jsonObject.put("client_secret", TGT_CLIENTSECRET);
			jsonObject.put("source", "WEB");
			jsonObject.put("tgt", TGT);
			JSONObject headerJson = RequestUtils.getTokenFromTgt(TGT_URL, jsonObject.toString());
			if(null == headerJson || headerJson.isNullObject()){
				returnJson.put(RequestUtils.RE_MESSAGE, RequestUtils.TO_SUCCESS);
				returnJson.put(RequestUtils.RE_DATA, ExceptionConstants.TGT_IS_GET_FAILED.getError());
				return returnJson;
			}
			String userId = headerJson.getString(RequestUtils.T_USER_ID);
			String token = headerJson.getString(RequestUtils.T_TOKEN);
			String requestHeader = iTOU_V2_USER+userId;
			if(retype == 2){
				//查询理财产品明细
				requestHeader = requestHeader+"/invest/list/"+userInfoReq.getPage()+"/"+userInfoReq.getSize()+"?status=SETTLED&status=OVERDUE&status=BREACH&access_token="+token;
				returnJson = getInvest(requestHeader);
			}else{
				//查询用户账户信息
				requestHeader = requestHeader+"/userfund?access_token="+token;
				returnJson = getUserfund(requestHeader);
			}
		} catch (Exception e) {
			returnJson.put(RequestUtils.RE_MESSAGE, RequestUtils.TO_SUCCESS);
			returnJson.put(RequestUtils.RE_DATA, ExceptionConstants.OBJ_IS_CHECK_FAILED.getError());
		}
		//财富总览[总资产+可用余额+待收收益+冻结金额]、理财产品明细[产品信息+投资金额+到期日]
		return returnJson;
	}

	/**
	 * class name: getUserfund
	 * author: fri
	 * date: 2017年4月16日
	 * function: 查询用户账户信息
	 * in:
	 *		parameter1: requestHeader 请求路径或方法
	 * out:
	 *		parameter: JSONObject
	 */
	private JSONObject getUserfund(String requestHeader){
		JSONObject returnJson = new JSONObject();
		returnJson = RequestUtils.commonHttpRequstReturnObj(RequestUtils.MT_V2, RequestUtils.GET, null, requestHeader, null, null,null);
		JSONObject data = returnJson.getJSONObject(RequestUtils.RE_DATA);
		if(null != data.get(RequestUtils.TO_ERROR)){
			return returnJson;
		}
		//联调处理 --> 财富总览[总资产+可用余额+待收收益+冻结金额]
		JSONObject reJson = new JSONObject();
		try{
			Double dsbj = data.getDouble("outstandingPrincipal");//待收本金
			Double djje = data.getDouble("frozenAmount");//冻结的金额
			BigDecimal totalDsbj = BigDecimal.valueOf(dsbj);
			BigDecimal totalDjje = BigDecimal.valueOf(djje);
			BigDecimal total = totalDsbj.add(totalDjje);
			String totalAssets = String.valueOf(total);//总资产（本金+冻结的金额）
			String availableBalance = String.valueOf(data.get("availableAmount"));//可用余额
			String incomeReceived = String.valueOf(data.get("outstandingInterest"));//待收收益（利息）
			String frozenAmount = String.valueOf(djje);//冻结金额
			reJson.put("totalAssets", totalAssets);
			reJson.put("availableBalance", availableBalance);
			reJson.put("incomeReceived", incomeReceived);
			reJson.put("frozenAmount", frozenAmount);
		}catch(Exception e){
		}finally {
			returnJson.put(RequestUtils.RE_MESSAGE, RequestUtils.TO_SUCCESS);
			returnJson.put(RequestUtils.RE_DATA, reJson);
		}
		return returnJson;
	}
	
	/**
	 * class name: getInvest
	 * author: fri
	 * date: 2017年4月16日
	 * function: 查询理财产品明细
	 * in:
	 *		parameter1: requestHeader 请求路径或方法
	 * out:
	 *		parameter: JSONObject
	 */
	private JSONObject getInvest(String requestHeader){
		JSONObject returnJson = new JSONObject();
		returnJson = RequestUtils.commonHttpRequstReturnObj(RequestUtils.MT_V2, RequestUtils.GET, null, requestHeader, null, null,null);
		JSONObject data2 = returnJson.getJSONObject(RequestUtils.RE_DATA);
		if(null != data2.get(RequestUtils.TO_ERROR)){
			return returnJson;
		}
		//联调处理 --> 理财产品明细[产品信息+投资金额+到期日]
		JSONArray reResult = new JSONArray();
		try{
			JSONArray result = data2.getJSONArray("results");
			if(null != result && result.size() > 0){
				for(@SuppressWarnings("unchecked")
				Iterator<JSONObject> car = result.iterator(); car.hasNext();){
					JSONObject dt = JSONObject.fromObject(car.next());
					String productInformation = String.valueOf(dt.get("loanTitle"));
					String investmentAmount = String.valueOf(dt.get("amount"));
					JSONArray repayArr = dt.getJSONArray("repayments");
					JSONObject repayJson = JSONObject.fromObject(repayArr.get(repayArr.size()-1));
					String dueDate = String.valueOf(repayJson.getJSONObject("repayment").get("dueDate"));
					JSONObject one = new JSONObject();
					one.put("productInformation", productInformation);//产品信息
					one.put("investmentAmount", investmentAmount);//投资金额
					one.put("dueDate", dueDate);//到期日
					reResult.add(one);
				}
			}
		}catch (Exception e) {
		}finally {
			returnJson.put(RequestUtils.RE_MESSAGE, RequestUtils.TO_SUCCESS);
			returnJson.put(RequestUtils.RE_DATA, reResult);
		}
		return returnJson;
	}
	
}

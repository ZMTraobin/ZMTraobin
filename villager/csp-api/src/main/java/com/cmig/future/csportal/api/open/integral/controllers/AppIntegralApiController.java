package com.cmig.future.csportal.api.open.integral.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.module.operate.integral.components.IntegralRuleOperation;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.RegExpValidatorUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.sign.CspSignUtil;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.integral.service.IIntegralRuleService;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zpf on 2017/4/25.
 */
@Controller
@RequestMapping(value = "${userPath}")
public class AppIntegralApiController extends BaseExtendController {
    @Autowired
    private IAppUserService appUserService;
    @Autowired
    private IIntegralRuleService integralRuleService;
    @Autowired
    private IntegralRuleOperation integralRuleOperation;

	/**
	 * 查询折扣条款
	 * @return
	 */
	@RequestMapping(value = "/discount/list", produces = {"application/json"}, method = RequestMethod.POST)
	@ResponseBody
	public RetApp findDiscountList(HttpServletRequest request) throws Exception {
		RetApp retApp=new RetApp();
		//appid
		String appid = getParam(request, "appid", "");
		String openid=getParam(request,"openid","");
		//业务场景
		String integralCode = getParam(request, "integralCode", "");
		//订单总额
		String orderAmount = getParam(request, "orderAmount", "");
		String discountType=getParam(request,"discountType","");
		//验证第三方签名
		OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
		if (StringUtils.isEmpty(openid)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"openid");
		}
		if(StringUtils.isEmpty(integralCode)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"业务场景");
		}
		if (StringUtils.isEmpty(orderAmount)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"订单金额");
		}else if(!RegExpValidatorUtils.IsIntNumber(orderAmount)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"订单金额格式不正确");
		}

		String sign=getParam(request,"sign","");
		Map paramMap=new HashMap();
		paramMap.put("openid",openid);
		paramMap.put("appid",appid);
		paramMap.put("integralCode",integralCode);
		paramMap.put("orderAmount",orderAmount);
		if(!StringUtils.isEmpty(discountType)) {
			paramMap.put("discountType", discountType);
		}
		boolean flag= CspSignUtil.checkSign(paramMap,openAppInfo.getAppSecret(),sign);
		if(flag) {
			JSONObject result=integralRuleService.getMaxAvailableIntegral(integralCode, orderAmount, openid);
			Integer maxAvailableNum=new Integer(result.get("maxAvailableNum").toString());//最大可抵扣积分
			Double maxAvailableAmount=new Double(result.get("maxAvailableAmount").toString());//最大可抵扣金额
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("due",orderAmount);//应收

			JSONArray discountList=new JSONArray();
			if(StringUtils.isEmpty(discountType)||discountType.indexOf("integralDiscount")>=0) {
				jsonObject.put("receipt",new Integer(orderAmount)-maxAvailableNum);//实收

				JSONObject integralDiscount=new JSONObject();
				integralDiscount.put("discountType","integralDiscount");//折扣类型：积分抵扣
				//如果最大可抵扣积分小于0，则置为0
				if(maxAvailableNum < 0){
					maxAvailableNum = 0;
					maxAvailableAmount = 0.00;
				}
				integralDiscount.put("integralNum",maxAvailableNum);//最大可抵扣积分数
				integralDiscount.put("amount",maxAvailableAmount);//最大可抵扣金额
				integralDiscount.put("activeParty",Global.getProductName());//活动方
				discountList.add(integralDiscount);
			}else{
				jsonObject.put("receipt",orderAmount);//实收
			}

			jsonObject.put("discount",discountList);
			retApp= RetAppUtil.success(jsonObject,"");
		}else{
			throw new ServiceException(RestApiExceptionEnums.SIGN_ERROR);
		}
		return retApp;
	}

	/**
	 * 积分冻结
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/discount/integral/freeze", produces = {"application/json"}, method = RequestMethod.POST)
	@ResponseBody
	public RetApp integralFreeze(HttpServletRequest request) throws Exception {
		RetApp retApp=new RetApp();
		//appid
		String appid = getParam(request, "appid", "");
		String openid=getParam(request,"openid","") ;
		//业务场景
		String integralCode = getParam(request, "integralCode", "");
		//冻结积分数
		String credits = getParam(request, "credits", "");
		String orderNumber=getParam(request,"orderNumber","");

		//验证第三方签名
		OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
		if (StringUtils.isEmpty(openid)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"openid");
		}
		if(StringUtils.isEmpty(integralCode)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"业务场景");
		}
		if (StringUtils.isEmpty(credits)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"冻结积分数");
		}else if(!RegExpValidatorUtils.IsIntNumber(credits)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"冻结积分数格式不正确");
		}

		AppUser appUser= appUserService.getBySourceSystemId(openid);
		if(null==appUser){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"openid");
		}

		//如果有积分冻结，需要再次校验冻结积分数不能超过积分余额和剩余可抵扣积分数
		if(new Integer(credits).intValue()> 0) {
			JSONObject result = integralRuleService.getMaxAvailableIntegral(integralCode, credits, openid);
			Integer maxAvailableNum = new Integer(result.get("maxAvailableNum").toString());//最大可抵扣积分
			if (new Integer(credits).compareTo(maxAvailableNum) > 0) {
				throw new DataWarnningException("冻结积分数超过最大可使用限额");
			}
		}

		String sign=getParam(request,"sign","");
		Map paramMap=new HashMap();
		paramMap.put("openid",openid);
		paramMap.put("appid",appid);
		paramMap.put("integralCode",integralCode);
		paramMap.put("credits",credits);
		paramMap.put("orderNumber", orderNumber);
		boolean flag= CspSignUtil.checkSign(paramMap,openAppInfo.getAppSecret(),sign);
		if(flag) {
			paramMap.clear();
			paramMap.put("out_trade_no",orderNumber);
			paramMap.put("credits",credits);
			paramMap.put("uid",appUser.getSourceSystemId());
			paramMap.put("type",integralCode);
			if(Boolean.valueOf(Global.getConfig("INTEGRAL.ISENABLE_OR_DISABLED"))){
				JSONObject jsonObject = integralRuleOperation.integralDJ(paramMap);
				String return_code = jsonObject.get("returnCode").toString();
				String message = jsonObject.get("message").toString();
				if(IntegralRuleOperation.SUCCESS_CODE.equals(return_code)){
					retApp.setStatus(OK);
					retApp.setMessage(message);
				}else{
					retApp.setStatus(FAIL);
					retApp.setMessage(message);
				}
			}else{
				throw new ServiceException(RestApiExceptionEnums.INTEGRAL_SYSTEM_UNABLE);
			}
		}else{
			throw new ServiceException(RestApiExceptionEnums.SIGN_ERROR);
		}
		return retApp;
	}

	/**
	 * 积分扣减
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/discount/integral/deduction", produces = {"application/json"}, method = RequestMethod.POST)
	@ResponseBody
	public RetApp integralDeduction(HttpServletRequest request) throws Exception {
		RetApp retApp=new RetApp();
		//appid
		String appid = getParam(request, "appid", "");
		String openid=getParam(request,"openid","") ;
		//业务场景
		String integralCode = getParam(request, "integralCode", "");
		//扣减金额（分）
		String orderAmount = getParam(request, "orderAmount", "");
		String orderNumber=getParam(request,"orderNumber","");

		//描述
		String description = getParam(request, "description", "");
		//附加数据
		String attach = getParam(request, "attach", "");
		//验证第三方签名
		OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
		if (StringUtils.isEmpty(openid)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"openid");
		}
		if(StringUtils.isEmpty(integralCode)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"业务场景");
		}
		if (StringUtils.isEmpty(orderAmount)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"订单金额");
		}else if(!RegExpValidatorUtils.IsIntNumber(orderAmount)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"订单金额格式不正确");
		}

		AppUser appUser= appUserService.getBySourceSystemId(openid);
		if(null==appUser){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"openid");
		}

		String sign=getParam(request,"sign","");
		Map paramMap=new HashMap();
		paramMap.put("openid",openid);
		paramMap.put("appid",appid);
		paramMap.put("integralCode",integralCode);
		paramMap.put("orderAmount",orderAmount);
		paramMap.put("orderNumber", orderNumber);
		if (!StringUtils.isEmpty(description)) {
			paramMap.put("description", description);
		}
		if (!StringUtils.isEmpty(attach)) {
			paramMap.put("attach", attach);
		}
		boolean flag= CspSignUtil.checkSign(paramMap,openAppInfo.getAppSecret(),sign);
		if(flag) {
			paramMap.clear();
			//订单编号
			paramMap.put("out_trade_no", orderNumber);
			paramMap.put("uid", openid);
			paramMap.put("credits", orderAmount);//扣减积分=扣减金额（分）
			paramMap.put("type", integralCode);
			paramMap.put("phone", appUser.getMobile());
			if (!StringUtils.isEmpty(description)) {
				paramMap.put("description", description);
			}
			if (!StringUtils.isEmpty(attach)) {
				paramMap.put("attach", attach);
			}
			//时间戳
			paramMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
			JSONObject object = integralRuleOperation.deduction(paramMap);
			String message = object.get("message").toString();
			String returnCode = object.get("returnCode").toString();
			if (IntegralRuleOperation.SUCCESS_CODE.equals(returnCode)) {
				String balance = object.get("balance").toString();
				retApp.setMessage("积分扣减成功，用户剩余积分为"+balance);
				retApp.setStatus(OK);
			} else {
				retApp.setMessage(message);
				retApp.setStatus(FAIL);
			}
		}else{
			throw new ServiceException(RestApiExceptionEnums.SIGN_ERROR);
		}
		return retApp;
	}


	/**
	 * 积分解冻
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/discount/integral/unfreeze", produces = {"application/json"}, method = RequestMethod.POST)
	@ResponseBody
	public RetApp integralUnfreeze(HttpServletRequest request) throws Exception {
		RetApp retApp=new RetApp();
		//appid
		String appid = getParam(request, "appid", "");
		String openid=getParam(request,"openid","") ;
		String orderNumber=getParam(request,"orderNumber","");

		if (StringUtils.isEmpty(openid)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"openid");
		}

		AppUser appUser= appUserService.getBySourceSystemId(openid);
		if(null==appUser){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"openid");
		}

		//验证第三方签名
		OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
		String sign=getParam(request,"sign","");
		Map paramMap=new HashMap();
		paramMap.put("appid",appid);
		paramMap.put("openid",openid);
		paramMap.put("orderNumber", orderNumber);
		boolean flag= CspSignUtil.checkSign(paramMap,openAppInfo.getAppSecret(),sign);
		if(flag) {
			paramMap.clear();
			paramMap.put("out_trade_no",orderNumber);
			paramMap.put("uid",appUser.getSourceSystemId());
			JSONObject jsonObject = integralRuleOperation.integralJD(paramMap);
			String return_code = jsonObject.get("returnCode").toString();
			String message = jsonObject.get("message").toString();
			if(IntegralRuleOperation.SUCCESS_CODE.equals(return_code)){
				retApp.setStatus(OK);
				retApp.setMessage(message);
			}else{
				retApp.setStatus(FAIL);
				retApp.setMessage(message);
			}
		}else{
			throw new ServiceException(RestApiExceptionEnums.SIGN_ERROR);
		}
		return retApp;
	}

}


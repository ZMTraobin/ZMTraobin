
package com.cmig.future.csportal.api.open.pay.controllers;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.oauth2.exceptions.OAuth2Exception;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.RegExpValidatorUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.sign.CspSignUtil;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.pay.components.CspLejiaPayServiceImpl;
import com.cmig.future.csportal.module.pay.service.IPayService;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 14:25 2017/4/6.
 * @Modified by zhangtao on 14:25 2017/4/6.
 */
@Controller
@RequestMapping(value = "${userPath}")
@ResponseBody
public class LejiaPayApiController extends BaseExtendController {

    private Logger logger= LoggerFactory.getLogger(LejiaPayApiController.class);

	@Autowired
	private CspLejiaPayServiceImpl cspLejiaPayServiceImpl;

	@Autowired
	private IPayService lejiaPayService;


	/**
	 * 乐家易付-单笔代扣
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/lejiaPay/submit", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp lejiaPaySubmit(HttpServletRequest request, HttpServletResponse response) {
		RetApp retApp = new RetApp();
		try {
			String appid = getParam(request, "appid", "");
			if (StringUtils.isEmpty(appid)) {
				throw new DataWarnningException("appid不能为空");
			}

			String origiOrderId =getParam(request,"origiOrderId","");
			if(StringUtils.isEmpty(origiOrderId)){
				throw new DataWarnningException("订单号不能为空！");
			}

			String cardId =getParam(request,"cardId","");
			if(StringUtils.isEmpty(cardId)){
				throw new DataWarnningException("cardId不能为空");
			}
			String merchantId =getParam(request,"merchantId","");
			if(StringUtils.isEmpty(merchantId)){
				throw new DataWarnningException("merchantId 不能为空");
			}

			String openid = getParam(request, "openid", "");
			if (StringUtils.isEmpty(openid)) {
				throw new DataWarnningException("openid不能为空");
			}

			//订单金额，单位为分
			String orderAmount = getParam(request, "orderAmount", "");
			if (StringUtils.isEmpty(orderAmount)) {
				throw new DataWarnningException("订单总金额不能为空");
			}else if(!RegExpValidatorUtils.IsIntNumber(orderAmount)){
				throw new DataWarnningException("订单总金额格式不正确");
			}

			String client_ip = getRemoteid(request);
			if(StringUtils.isEmpty(client_ip)){
				throw new DataWarnningException("客户端的IP地址不能为空");
			}
			//验证第三方签名
			OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
			String sign=getParam(request,"sign","");
			Map paramMap=new HashMap();
			paramMap.put("openid",openid);
			paramMap.put("appid",appid);
			paramMap.put("origiOrderId",origiOrderId);
			paramMap.put("cardId",cardId);
			paramMap.put("orderAmount",orderAmount);
			paramMap.put("merchantId",merchantId);
			boolean flag=CspSignUtil.checkSign(paramMap,openAppInfo.getAppSecret(),sign);
			if(flag) {
				Map<String, Object> chargeMap = new HashMap<String, Object>();
				chargeMap.put("origiOrderId", origiOrderId);// 推荐使用 8-20 位，要求数字或字母，不允许其他字符
				chargeMap.put("orderIp", client_ip); // 发起支付请求客户端的 IP 地址，格式为 IPV4，如: 127.0.0.1
				chargeMap.put("amount", new BigDecimal(orderAmount).divide(new BigDecimal(100)));//金额
				chargeMap.put("openid",openid);
				JSONObject responseJson = cspLejiaPayServiceImpl.pay(chargeMap, new Integer(cardId),merchantId);
				retApp.setData(responseJson);
				retApp.setStatus(OK);
				retApp.setMessage("");
			}else{
				retApp.setStatus(OK);
				retApp.setMessage("验签失败，签名不正确");
			}
		}catch (OAuth2Exception e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		} catch (DataWarnningException e) {
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage("创建失败");
		}
		return retApp;
	}

	/**
	 * 乐家易付-订单状态查询
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/lejiaPay/query", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp lejiaPayQuery(HttpServletRequest request, HttpServletResponse response) {
		RetApp retApp = new RetApp();
		try {
			String appid = getParam(request, "appid", "");
			if (StringUtils.isEmpty(appid)) {
				throw new DataWarnningException("appid不能为空");
			}

			String origOrderNo =getParam(request,"origOrderNo","");
			if(StringUtils.isEmpty(origOrderNo)){
				throw new DataWarnningException("订单号不能为空！");
			}

			String merchantId =getParam(request,"merchantId","");
			if(StringUtils.isEmpty(merchantId)){
				throw new DataWarnningException("merchantId 不能为空");
			}

			//验证第三方签名
			OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
			String sign=getParam(request,"sign","");
			Map paramMap=new HashMap();
			paramMap.put("appid",appid);
			paramMap.put("origOrderNo",origOrderNo);
			paramMap.put("merchantId",merchantId);
			boolean flag=CspSignUtil.checkSign(paramMap,openAppInfo.getAppSecret(),sign);
			if(flag) {
				JSONObject responseJson = cspLejiaPayServiceImpl.searchPayStatus(origOrderNo,merchantId);
				retApp.setData(responseJson);
				retApp.setStatus(OK);
				retApp.setMessage("");
			}else{
				retApp.setStatus(OK);
				retApp.setMessage("验签失败，签名不正确");
			}
		}catch (OAuth2Exception e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		} catch (DataWarnningException e) {
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage("创建失败");
		}
		return retApp;
	}

	/**
	 * 乐家易付-退款
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/lejiaPay/refund", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp lejiaPayRefund(HttpServletRequest request, HttpServletResponse response) {
		RetApp retApp = new RetApp();
		try {
			String appid = getParam(request, "appid", "");
			if (StringUtils.isEmpty(appid)) {
				throw new DataWarnningException("appid不能为空");
			}

			String origiOrderId =getParam(request,"origiOrderId","");
			if(StringUtils.isEmpty(origiOrderId)){
				throw new DataWarnningException("原订单号不能为空！");
			}

			String merchantId =getParam(request,"merchantId","");
			if(StringUtils.isEmpty(merchantId)){
				throw new DataWarnningException("merchantId 不能为空");
			}
			String openid = getParam(request, "openid", "");
			if (StringUtils.isEmpty(openid)) {
				throw new DataWarnningException("openid不能为空");
			}

			//退款金额，单位为分
			String refundAmt = getParam(request, "refundAmt", "");
			if (StringUtils.isEmpty(refundAmt)) {
				throw new DataWarnningException("退款金额不能为空");
			}else if(!RegExpValidatorUtils.IsIntNumber(refundAmt)){
				throw new DataWarnningException("退款金额格式不正确");
			}

			String client_ip = getRemoteid(request);
			if(StringUtils.isEmpty(client_ip)){
				throw new DataWarnningException("客户端的IP地址不能为空");
			}
			//验证第三方签名
			OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
			String sign=getParam(request,"sign","");
			Map paramMap=new HashMap();
			paramMap.put("appid",appid);
			paramMap.put("origiOrderId",origiOrderId);
			paramMap.put("refundAmt",refundAmt);
			paramMap.put("merchantId",merchantId);
			paramMap.put("openid",openid);
			boolean flag=CspSignUtil.checkSign(paramMap,openAppInfo.getAppSecret(),sign);
			if(flag) {
				Map<String, Object> refundMap = new HashMap<String, Object>();
				refundMap.put("origiOrderId", origiOrderId);// 推荐使用 8-20 位，要求数字或字母，不允许其他字符
				refundMap.put("orderIp", client_ip); // 发起支付请求客户端的 IP 地址，格式为 IPV4，如: 127.0.0.1
				refundMap.put("refundAmt", new BigDecimal(refundAmt).divide(new BigDecimal(100)));//金额
				refundMap.put("merchantId",merchantId);
				refundMap.put("userId",openid);
				String result= lejiaPayService.refund(refundMap);
				JSONObject jsonObject=parseObject(result);
				retApp.setData(jsonObject);
				retApp.setStatus(OK);
				retApp.setMessage("");
			}else{
				retApp.setStatus(OK);
				retApp.setMessage("验签失败，签名不正确");
			}
		}catch (OAuth2Exception e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		} catch (DataWarnningException e) {
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage("退款失败");
		}
		return retApp;
	}

	@RequestMapping(value = "/lejiaPay/enableFlag", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp lejiaPayEnableFlag(HttpServletRequest request, HttpServletResponse response) {
		RetApp retApp = new RetApp();
		try {
			JSONObject jsonObject=new JSONObject();
			if(Global.getBoolean("lejiapay.enableFlag",false)){
				jsonObject.put("flag","Y");
			}else{
				jsonObject.put("flag","N");
			}
			retApp.setMessage("");
			retApp.setStatus(OK);
			retApp.setData(jsonObject);
		}catch (OAuth2Exception e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		} catch (DataWarnningException e) {
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage("查询失败");
		}
		return retApp;
	}

}

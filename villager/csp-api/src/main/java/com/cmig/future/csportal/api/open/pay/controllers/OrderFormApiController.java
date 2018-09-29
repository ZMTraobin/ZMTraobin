package com.cmig.future.csportal.api.open.pay.controllers;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.oauth2.exceptions.OAuth2Exception;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.RegExpValidatorUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.sign.CspSignUtil;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.pay.components.CspLejiaPayServiceImpl;
import com.cmig.future.csportal.module.pay.conf.OrderFormHelper;
import com.cmig.future.csportal.module.pay.conf.PingppConfig;
import com.cmig.future.csportal.module.pay.order.dto.OrderForm;
import com.cmig.future.csportal.module.pay.order.service.IOrderFormService;
import com.cmig.future.csportal.module.pay.refund.dto.OrderFormRefund;
import com.cmig.future.csportal.module.pay.refund.service.IOrderFormRefundService;
import com.cmig.future.csportal.module.pay.service.impl.LejiaPayConfig;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.APIConnectionException;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.exception.AuthenticationException;
import com.pingplusplus.exception.ChannelException;
import com.pingplusplus.exception.InvalidRequestException;
import com.pingplusplus.exception.RateLimitException;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Refund;
import net.sf.json.JSONObject;
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

import static com.cmig.future.csportal.common.utils.sign.CspSignUtil.checkSign;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 14:25 2017/4/6.
 * @Modified by zhangtao on 14:25 2017/4/6.
 */
@Controller
@RequestMapping(value = "${userPath}")
@ResponseBody
public class OrderFormApiController extends BaseExtendController { 

    private Logger logger = LoggerFactory.getLogger(OrderFormApiController.class);

    @Autowired
    private IOrderFormService orderFormService;

	@Autowired
	private CspLejiaPayServiceImpl cspLejiaPayServiceImpl;

	@Autowired
	private IOrderFormRefundService orderFormRefundService;

    /**
     * 商户提交订单
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/order/orderSubmit", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp orderSubmit(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //必填项,需要验证
        String appid = getParam(request, "appid", "");
        String openid = getParam(request, "openid", "");
        String tradeNo = getParam(request, "tradeNo", "");
        String orderAmount = getParam(request, "orderAmount", "");
        String subject = getParam(request, "subject", "");
        String body = getParam(request, "body", "");
        String backUrl = getParam(request, "backUrl", "");
        String clientIp=getParam(request,"clientIp","");
        String sign = getParam(request, "sign", "");
        //可为空参数
        String frontUrl = getParam(request, "frontUrl", "");
        String orderType = getParam(request, "orderType", "");
        String description = getParam(request, "description", "");
        String timeExpire = getParam(request, "timeExpire", "");
        String integralAmount=getParam(request,"integralAmount","");//抵扣积分数

        Map<String, String> map = new HashMap();
        map.put("appid", appid);
        map.put("openid", openid);
        map.put("tradeNo", tradeNo);
        map.put("orderAmount", orderAmount);
        map.put("subject", subject);
        map.put("body", body);
        map.put("backUrl", backUrl);
        map.put("clientIp", clientIp);
        map.put("frontUrl", frontUrl);
        map.put("orderType", orderType);
        map.put("description", description);
        map.put("timeExpire", timeExpire);
        map.put("integralAmount", integralAmount);
        map.put("sign", sign);
		//提交订单
        OrderForm orderForm = orderFormService.orderSubmit(map);
	    Object object=orderFormService.getResultForSubmit(request, orderForm);
	    return  RetAppUtil.success(object,"提交成功");
    }


	/**
     * 商户查询订单支付状态
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/order/orderQuery", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp orderQuery(HttpServletRequest request, HttpServletResponse response) {
        //必填项,需要验证
        String appid = getParam(request, "appid", "");
        String tradeNo = getParam(request, "tradeNo", "");
        String sign = getParam(request, "sign", "");
        RetApp retApp = new RetApp();
        try {
            if(StringUtils.isEmpty(appid)){
                throw new DataWarnningException("appid不能为空");
            }

            if(StringUtils.isEmpty(tradeNo)){
                throw new DataWarnningException("订单号不能为空");
            }

            //验证第三方签名
            OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
            Map<String, String> map = new HashMap();
            map.put("appid", appid);
            map.put("tradeNo", tradeNo);
            if (!CspSignUtil.checkSign(map, openAppInfo.getAppSecret(), sign)) {
                throw new DataWarnningException("签名验证未通过");
            }

            JSONObject object = new JSONObject();
            String payStatus;
            OrderForm orderForm=new OrderForm();
            orderForm.setAppId(appid);
            orderForm.setSourceOrderNumber(tradeNo);
            orderForm=orderFormService.findBySourceOrderNumber(orderForm);
            if(null==orderForm){
                throw new DataWarnningException("该订单号不存在，请检查");
            }
	        //默认值
	        payStatus = "N";
	        object.put("payStatus", payStatus);
	        object.put("channel", orderForm.getPayChannel());
	        object.put("transeq", orderForm.getTranseq());
	        object.put("paidAmount", orderForm.getPaidAmount());
	        object.put("timePaid", orderForm.getTimePaid() == null ? "" : orderForm.getTimePaid().getTime());

	        if(OrderFormHelper.OREDER_STATUS_Y.equals(orderForm.getPayStatus())){
                payStatus="Y";
                object.put("payStatus", payStatus);
                object.put("channel", orderForm.getPayChannel());
                object.put("transeq", orderForm.getTranseq());
                object.put("paidAmount",orderForm.getPaidAmount());
                object.put("timePaid",orderForm.getTimePaid()==null?"":orderForm.getTimePaid().getTime());
            }else if(OrderFormHelper.PAY_CHANNEL_LEJIA_PAY.equals(orderForm.getPayChannel())) {
	            com.alibaba.fastjson.JSONObject thisObject = cspLejiaPayServiceImpl.searchPayStatus(orderForm,LejiaPayConfig.DEFAULT_MERCHANT_ID);
	            if(null!=thisObject&&LejiaPayConfig.SUCCESS_CODE.equals(thisObject.get("respCode"))) {
		            String status = thisObject.get("payStatus").toString();
		            if ("SUCC".equals(status)) {
			            object.put("payStatus", OrderFormHelper.OREDER_STATUS_Y);
		            } else if ("DEALING".equals(status)) {
			            object.put("payStatus", OrderFormHelper.OREDER_STATUS_P);
		            } else {
			            object.put("payStatus", OrderFormHelper.OREDER_STATUS_N);
		            }
		            object.put("channel", OrderFormHelper.PAY_CHANNEL_LEJIA_PAY);
		            object.put("transeq", "");
		            object.put("paidAmount", new BigDecimal(thisObject.get("transAmt").toString()).multiply(new BigDecimal(100)));
		            object.put("timePaid", null);
		            retApp.setData(object);
	            }
            }else if(!StringUtils.isEmpty(orderForm.getChargeId())) {
	            PingppConfig.initPingxxAppid(orderForm.getAppId());
	            Charge charge = Charge.retrieve(orderForm.getChargeId());
	            if (null != charge && charge.getPaid()) {
		            payStatus = "Y";
		            object.put("payStatus", payStatus);
		            object.put("channel", charge.getChannel());
		            object.put("transeq", charge.getTransactionNo());
		            object.put("paidAmount", charge.getAmountSettle());
		            object.put("timePaid", charge.getTimePaid() == null ? "" : charge.getTimePaid());
	            }
            }

            retApp.setData(object);
            retApp.setStatus(OK);
            retApp.setMessage("查询成功!");
        } catch (DataWarnningException e) {
            String msg = e.getMessage();
            retApp.setStatus(FAIL);
            retApp.setMessage(msg);
        } catch (OAuth2Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        }catch (Exception e) {
            retApp.setStatus(FAIL);
            retApp.setMessage("查询失败,请联系管理员");
        }
        return retApp;
    }


	/**
	 * 商户发起退款
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/pingpp/refundSubmit", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp refundSubmit(HttpServletRequest request, HttpServletResponse response) {
		//必填项,需要验证
		String appid = getParam(request, "appid", "");
		String tradeNo = getParam(request, "tradeNo", "");
		String amount = getParam(request, "amount", "");
		String description = getParam(request, "description", "");
		String backUrl = getParam(request, "backUrl", "");
		String sign = getParam(request, "sign", "");
		RetApp retApp = new RetApp();
		try {
			if(StringUtils.isEmpty(appid)){
				throw new DataWarnningException("appid不能为空");
			}

			if(StringUtils.isEmpty(tradeNo)){
				throw new DataWarnningException("商户订单号不能为空");
			}

			if(StringUtils.isEmpty(backUrl)){
				throw new DataWarnningException("通知地址不能为空");
			}

			if(StringUtils.isEmpty(description)||description.length()>255){
				throw new DataWarnningException("退款详情不能为空，最多255个字符");
			}

			//验证第三方签名
			OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
			Map<String, String> map = new HashMap();
			map.put("appid", appid);
			map.put("tradeNo", tradeNo);
			map.put("amount", amount);
			map.put("description", description);
			map.put("backUrl", backUrl);
			if (!checkSign(map, openAppInfo.getAppSecret(), sign)) {
				throw new DataWarnningException("签名验证未通过");
			}

			OrderForm orderForm=new OrderForm();
			orderForm.setAppId(appid);
			orderForm.setSourceOrderNumber(tradeNo);
			orderForm=orderFormService.findBySourceOrderNumber(orderForm);
			if(null==orderForm){
				throw new DataWarnningException("该订单号不存在，请检查");
			}else if(!OrderFormHelper.OREDER_STATUS_Y.equals(orderForm.getPayStatus())){
				throw new DataWarnningException("该订单未支付，不能退款");
			}

			if (StringUtils.isEmpty(amount)) {
				throw new DataWarnningException("退款金额不能为空");
			}else if(!RegExpValidatorUtils.IsIntNumber(amount)){
				throw new DataWarnningException("退款金额格式不正确");
			}else if(Long.valueOf(amount)>orderForm.getPaidAmount()){
				throw new DataWarnningException("退款金额不能超过已支付金额");
			}

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("description", description);
			if (!StringUtils.isEmpty(amount)) {
				params.put("amount", amount);// 退款的金额, 单位为对应币种的最小货币单位，例如：人民币为分（如退款金额为 1 元，此处请填 100）。必须小于等于可退款金额，默认为全额退款
			}

			OrderFormRefund orderFormRefund=new OrderFormRefund();
			orderFormRefund.setBackUrl(backUrl);
			if(OrderFormHelper.PAY_CHANNEL_LEJIA_PAY.equals(orderForm.getPayChannel())){
				com.alibaba.fastjson.JSONObject thisObject = cspLejiaPayServiceImpl.refund(orderForm,params,LejiaPayConfig.DEFAULT_MERCHANT_ID);
				com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();
				object.put("refundOrderNo", thisObject.get("refundOrderNo"));//退款订单号
				object.put("success", thisObject.get("success"));//退款是否成功
				object.put("refundStatus", thisObject.get("refundStatus"));//目前退款状态
				object.put("failure_code", thisObject.get("failure_code"));
				object.put("failure_msg", thisObject.get("failure_msg"));
				retApp.setData(object);
			}else if(!StringUtils.isEmpty(orderForm.getChargeId())){
				Refund refund = orderFormRefundService.createRefund(orderForm, params, orderFormRefund);
				//记录退款
				com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();
				object.put("refundOrderNo", refund.getOrderNo());//退款订单号
				object.put("success", refund.getSucceed());//退款是否成功
				object.put("refundStatus", refund.getStatus());//目前退款状态
				object.put("failure_code", refund.getFailureCode());
				object.put("failure_msg", refund.getFailureMsg());
				retApp.setData(object);
			}else{
				throw new DataWarnningException("该渠道不支持在线退款");
			}
			retApp.setStatus(OK);
			retApp.setMessage("");
		}catch (AuthenticationException e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		} catch (InvalidRequestException e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		} catch (APIConnectionException e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		} catch (APIException e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		} catch (ChannelException e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		} catch (RateLimitException e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		} catch (DataWarnningException e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		} catch (OAuth2Exception e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage("退款失败,请联系管理员");
		}
		return retApp;
	}

	/**
	 * 商户查询订单退款状态
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/pingpp/refundQuery", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp refundQuery(HttpServletRequest request, HttpServletResponse response) {
		//必填项,需要验证
		String appid = getParam(request, "appid", "");
		String refundOrderNo = getParam(request, "refundOrderNo", "");
		String sign = getParam(request, "sign", "");
		RetApp retApp = new RetApp();
		try {
			if(StringUtils.isEmpty(appid)){
				throw new DataWarnningException("appid不能为空");
			}

			if(StringUtils.isEmpty(refundOrderNo)){
				throw new DataWarnningException("退款单号不能为空");
			}

			//验证第三方签名
			OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
			Map<String, String> map = new HashMap();
			map.put("appid", appid);
			map.put("refundOrderNo", refundOrderNo);
			if (!checkSign(map, openAppInfo.getAppSecret(), sign)) {
				throw new DataWarnningException("签名验证未通过");
			}

			if(StringUtils.isEmpty(openAppInfo.getPaymentConfigId())){
				throw new DataWarnningException("收款帐号配置错误");
			}

			com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();
			OrderFormRefund orderFormRefund=orderFormRefundService.findByRefundOrderNo(refundOrderNo);
			if(null==orderFormRefund){
				throw new DataWarnningException("该退款单号不存在，请检查");
			}else if(OrderFormHelper.REFUND_STATUS_SUCCEEDED.equals(orderFormRefund.getStatus())){
				object.put("refundStatus", orderFormRefund.getStatus());
				object.put("refundAmount", orderFormRefund.getAmount());
				object.put("timeSucceed",orderFormRefund.getTimeSucceed()==null?"":orderFormRefund.getTimeSucceed().getTime());
				object.put("description", orderFormRefund.getDescription());
				object.put("failureCode", orderFormRefund.getFailureCode());
				object.put("failureMsg", orderFormRefund.getFailureMsg());
			}else{
				object.put("refundStatus", orderFormRefund.getStatus());
				object.put("refundAmount", orderFormRefund.getAmount());
				object.put("timeSucceed",orderFormRefund.getTimeSucceed()==null?"":orderFormRefund.getTimeSucceed().getTime());
				object.put("description", orderFormRefund.getDescription());
				object.put("failureCode", orderFormRefund.getFailureCode());
				object.put("failureMsg", orderFormRefund.getFailureMsg());

				if(!StringUtils.isEmpty(orderFormRefund.getChargeId())) {
					Pingpp.appId = openAppInfo.getPaymentConfigId();
					Charge charge = Charge.retrieve(orderFormRefund.getChargeId());
					Refund refund = charge.getRefunds().retrieve(orderFormRefund.getRefundId());
					if (null != charge && refund.getSucceed()) {
						object.put("refundStatus", refund.getStatus());
						object.put("refundAmount", refund.getAmount());
						object.put("timeSucceed", refund.getTimeSucceed() == null ? "" : refund.getTimeSucceed());
						object.put("description", refund.getDescription());
						object.put("failureCode", refund.getFailureCode());
						object.put("failureMsg", refund.getFailureMsg());
					}
				}
			}


			retApp.setData(object);
			retApp.setStatus(OK);
			retApp.setMessage("查询成功!");
		} catch (DataWarnningException e) {
			String msg = e.getMessage();
			retApp.setStatus(FAIL);
			retApp.setMessage(msg);
		} catch (OAuth2Exception e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		}catch (Exception e) {
			retApp.setStatus(FAIL);
			retApp.setMessage("查询失败,请联系管理员");
		}
		return retApp;
	}

	/**
	 * 商户取消订单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/order/cancel", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp orderCancel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//必填项,需要验证
		String appid = getParam(request, "appid", "");
		String tradeNo = getParam(request, "tradeNo", "");
		String sign = getParam(request, "sign", "");
		if(StringUtils.isEmpty(appid)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"appid");
		}
		if(StringUtils.isEmpty(tradeNo)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"tradeNo");
		}

		//验证第三方签名
		OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
		Map<String, String> map = new HashMap();
		map.put("appid", appid);
		map.put("tradeNo", tradeNo);
		if (!CspSignUtil.checkSign(map, openAppInfo.getAppSecret(), sign)) {
			throw new ServiceException(RestApiExceptionEnums.SIGN_ERROR);
		}
		orderFormService.orderCancel(appid,tradeNo);
		return RetAppUtil.success("订单取消成功");
	}

    /**
     * 商户查询所有订单
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/order/findAll", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp findAll(HttpServletRequest request, HttpServletResponse response) {
        //必填项,需要验证
        String appid = getParam(request, "appid", "");
        String pageSize = getParam(request, "pageSize", "10");
        String pageNo = getParam(request, "pageNo", "1");
        String channel = getParam(request, "channel", "");
        String payStatus = getParam(request, "payStatus", "");
        String refunded = getParam(request, "refunded", "");

        String sign = getParam(request, "sign", "");
        RetApp retApp = new RetApp();
        try {
            if(StringUtils.isEmpty(appid)){
                throw new DataWarnningException("appid不能为空");
            }

            //验证第三方签名
            OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
            Map<String, String> map = new HashMap();
            map.put("appid", appid);
            map.put("tradeNo", "");
            if (!CspSignUtil.checkSign(map, openAppInfo.getAppSecret(), sign)) {
                throw new DataWarnningException("签名验证未通过");
            }
            retApp.setStatus(OK);
            retApp.setMessage("查询成功!");
        } catch (DataWarnningException e) {
            String msg = e.getMessage();
            retApp.setStatus(FAIL);
            retApp.setMessage(msg);
        } catch (OAuth2Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        }catch (Exception e) {
            retApp.setStatus(FAIL);
            retApp.setMessage("查询失败,请联系管理员");
        }
        return retApp;
    }

}

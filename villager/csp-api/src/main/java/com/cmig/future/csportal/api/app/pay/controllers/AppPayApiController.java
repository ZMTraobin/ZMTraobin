
package com.cmig.future.csportal.api.app.pay.controllers;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
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
import com.cmig.future.csportal.module.pay.service.impl.LejiaPayConfig;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import com.cmig.future.csportal.module.sys.utils.UserTokenUtils;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Order;
import com.pingplusplus.model.Refund;
import com.pingplusplus.model.Webhooks;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 14:25 2017/4/6.
 * @Modified by zhangtao on 14:25 2017/4/6.
 */
@Controller
@RequestMapping(value = "${userPath}")
@ResponseBody
public class AppPayApiController extends BaseExtendController {

    private Logger logger= LoggerFactory.getLogger(AppPayApiController.class);

    @Autowired
    private IOrderFormService orderFormService;

	@Autowired
	private CspLejiaPayServiceImpl cspLejiaPayServiceImpl;

    /**
     * 从服务端发起支付请求，获取支付凭据
     * 如果是乐家易付，则直接发生代扣，同步无回调
     * @param request
     * @param response
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/st/pingpp/createCharge", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp createCharge(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderNumber =getParam(request,"orderNumber","");
        String cardId =getParam(request,"cardId","");
        String channel =getParam(request,"channel","");
        String token=getParam(request,"token","");
        String wxOpenId=getParam(request,"wx_open_id","");
        String successUrl=getParam(request,"success_url","");
        if(StringUtils.isEmpty(channel)){
            throw new DataWarnningException("支付渠道不能为空");
        }
        Object result=new JSONObject();
        logger.info("token {} 订单号 {} 渠道 {} cardId {} wxOpenId{} ",token,orderNumber,channel,cardId,wxOpenId);
        if(StringUtils.isEmpty(orderNumber)){
	        throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"订单号");
        }
        OrderForm orderForm = orderFormService.findByOrderNumber(orderNumber);
        if(orderForm==null) {
	        throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"订单号");
        }
        if(OrderFormHelper.OREDER_STATUS_C.equals(orderForm.getPayStatus())){
	        throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"该订单已失效，请重新提交订单！");
        }

        if(OrderFormHelper.OREDER_STATUS_Y.equals(orderForm.getPayStatus())){
	        throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"该订单已完成支付，请勿重复支付！");
        }
        String clientIp = getRemoteid(request);
        if(StringUtils.isEmpty(clientIp)){
	        throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"客户端IP地址");
        }
        orderForm.setClientIp(clientIp);
        switch (channel){
	        case OrderFormHelper.PAY_CHANNEL_LEJIA_PAY:
		        if(StringUtils.isEmpty(cardId)){
			        throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"cardId");
		        }
		        result = cspLejiaPayServiceImpl.pay(orderForm,new Integer(cardId),LejiaPayConfig.DEFAULT_MERCHANT_ID);
		        break;
	        case OrderFormHelper.PAY_CHANNEL_ALIPAY:
		        //发起交易请求
		        result= orderFormService.createCharge(orderForm, channel);
		        break;
	        case OrderFormHelper.PAY_CHANNEL_ALIPAY_WAP:
		        if(StringUtils.isEmpty(successUrl)){
			        throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"success_url");
		        }
		        orderForm.setSuccessUrl(successUrl);
		        //发起交易请求
		        result= orderFormService.createCharge(orderForm, channel);
		        break;
	        case OrderFormHelper.PAY_CHANNEL_WX:
		        //发起交易请求
		        result= orderFormService.createCharge(orderForm, channel);
		        break;
	        case OrderFormHelper.PAY_CHANNEL_INTEGRAL:
		        orderFormService.integralPay(orderForm);
		        break;
	        case OrderFormHelper.PAY_CHANNEL_WX_PUB:
		        if(StringUtils.isEmpty(wxOpenId)){
			        throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"wx_open_id");
		        }
		        orderForm.setWxOpenId(wxOpenId);
		        //发起交易请求
		        result= orderFormService.createCharge(orderForm, channel);
		        break;
	        default:
		        logger.info("token {} 订单号 {} 渠道 {} cardId {} ",token,orderNumber,channel,cardId);
		        break;

        }
        return RetAppUtil.success(result,"支付成功");
    }

	/**
	 * ping++ webhooks 通知
	 * @param request
	 * @param response
	 */
    @RequestMapping(value = "/pingpp/notify", produces = {"application/json"}, method = RequestMethod.POST)
    public void notify(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out=null ;
        try {
            request.setCharacterEncoding("UTF8");
	        response.setStatus(500);
            // 签名数据请从 request 的 header 中获取, key 为 X-Pingplusplus-Signature (请忽略大小写, 建议自己做格式化)
            String signature = "";

            //获取头部所有信息
            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = request.getHeader(key);
                if("X-Pingplusplus-Signature".equalsIgnoreCase(key)) {
                    signature = value;
                }
            }

            // 获得 http body 内容
            BufferedReader reader = request.getReader();
            StringBuffer buffer = new StringBuffer();
            String string;
            while ((string = reader.readLine()) != null) {
                buffer.append(string);
            }
            reader.close();
	        logger.debug(buffer.toString());
            boolean result = PingppConfig.verifyData(buffer.toString(), signature, PingppConfig.getPubKey());
            logger.debug("验签结果：" + (result ? "通过" : "失败"));

            out=response.getWriter();
            if(result) {
                // 解析异步通知数据
                Event event = Webhooks.eventParse(buffer.toString());
                Object obj = Webhooks.getObject(event.toString());
                logger.debug("回调事件：" + event.getType());
                logger.debug(event.toString());
	            logger.debug(obj.toString());
	            if ("charge.succeeded".equals(event.getType())||"order.succeeded".equals(event.getType())) {
		            // 解析 webhooks 可以采用如下方法
		            if (obj instanceof Charge) {
			            Charge charge=(Charge)obj;
			            orderFormService.chargeSucceeded(charge);
			            response.setStatus(200);
		            }else if(obj instanceof Order){
			            Order order=(Order)obj;
			            Charge charge=order.getCharges().getData().get(0);
			            orderFormService.chargeSucceeded(charge);
			            response.setStatus(200);
		            }
	            } else if ("refund.succeeded".equals(event.getType())||"order.refund.succeeded".equals(event.getType())) {
		            if (obj instanceof Refund) {
			            Refund refund=(Refund)obj;
			            orderFormService.refundSucceeded(refund);
			            response.setStatus(200);
		            }
	            }
            }
            out.print("");
        } catch (DataWarnningException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null!=out){
                out.close();
            }
        }
    }

	/**
	 * 订单全额积分抵扣
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/st/order/integral/pay", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp integralPay(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String orderNumber =getParam(request,"orderNumber","");
		if(StringUtils.isEmpty(orderNumber)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"订单号");
		}
		OrderForm orderForm = orderFormService.findByOrderNumber(orderNumber);
		if(orderForm==null) {
			throw new DataWarnningException("订单号不正确，不能支付！");
		}
		orderFormService.integralPay(orderForm);
		return RetAppUtil.success("订单支付成功");
	}


	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/pingpp/scanpay/createCharge", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp createChargeWithOutOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String channel =getParam(request,"channel","");
		String orderAmount=getParam(request,"orderAmount","");
		String successUrl=getParam(request,"successUrl","");
		String merchantNo=getParam(request,"merchantNo","");
		String token=getParam(request,"token","");
		String clientIp = getRemoteid(request);
		if(StringUtils.isEmpty(clientIp)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"客户端IP地址");
		}
		if(StringUtils.isEmpty(orderAmount)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"金额");
		}
		if(StringUtils.isEmpty(successUrl)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"successUrl");
		}
		if(StringUtils.isEmpty(channel)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"channel");
		}
		if(StringUtils.isEmpty(merchantNo)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"merchantNo");
		}
		//发起交易请求
		OrderForm orderForm=new OrderForm();
		orderForm.setOrderAmount(new Long(orderAmount));
		orderForm.setPayChannel(channel);
		orderForm.setSuccessUrl(StringEscapeUtils.unescapeHtml(successUrl).replaceAll("&","%26"));
		orderForm.setMerchantNo(merchantNo);
		orderForm.setClientIp(clientIp);
		orderForm.setBackUrl(Global.getProjectPath(request) + "/user/scanpay/payNotify");
		orderForm.setFrontUrl(Global.getProjectPath(request) + "/user/scanpay/payNotify");

		if(!StringUtils.isEmpty(token)){
			String appUserId= UserTokenUtils.getUserIdByToken(token);
			orderForm.setAppUserId(appUserId);
		}
		Charge charge= orderFormService.createChargeWithOutOrder(orderForm);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("charge", charge);
		return RetAppUtil.success(jsonObject,"支付成功");
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/scanpay/payNotify", method = RequestMethod.POST)
	public void payNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//必填项,需要验证
		String tradeNo = getParam(request, "tradeNo", "");
		String paidAmount = getParam(request, "paidAmount", "");
		String channel = getParam(request, "channel", "");
		String timePaid = getParam(request, "timePaid", "");
		String transeq = getParam(request, "transeq", "");
		String tradeStatus = getParam(request, "tradeStatus", "");
		String sign = getParam(request, "sign", "");
		String appid = OrderFormHelper.CSP_APP_ID;

		PrintWriter out = null;
		try {
			out = response.getWriter();
			//验证第三方签名
			OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
			//签名
			Map map = new HashMap();
			map.put("tradeNo", tradeNo);
			map.put("paidAmount", paidAmount);
			map.put("channel", channel);
			map.put("timePaid", timePaid);
			map.put("transeq", transeq);
			map.put("tradeStatus", tradeStatus);

			boolean result = CspSignUtil.checkSign(map, openAppInfo.getAppSecret(), sign);
			if (result) {
				logger.info("扫码支付-商户接收支付通知成功 {}", tradeNo);
				out.print("success");
			} else {
				logger.info("扫码支付-商户接收支付通知失败 {}", tradeNo);
				out.print("fail");
			}

		} catch (Exception e) {
			e.printStackTrace();
			out.print("fail");
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}

}

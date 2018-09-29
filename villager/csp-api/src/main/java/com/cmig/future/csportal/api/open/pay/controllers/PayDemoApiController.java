package com.cmig.future.csportal.api.open.pay.controllers;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.oauth2.exceptions.OAuth2Exception;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.sign.CspSignUtil;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.pay.components.CspLejiaPayServiceImpl;
import com.cmig.future.csportal.module.pay.order.service.IOrderFormService;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserCardService;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
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
import java.io.PrintWriter;
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
public class PayDemoApiController extends BaseExtendController {

    private Logger logger = LoggerFactory.getLogger(PayDemoApiController.class);

    @Autowired
    private IOrderFormService orderFormService;

    @Autowired
    private IAppUserService appUserService;

	@Autowired
	private CspLejiaPayServiceImpl cspLejiaPayServiceImpl;

	@Autowired
	private IAppUserCardService appUserCardService;

    /**
     * 支付demo，签名接口
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/order/orderSign", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp orderSign(HttpServletRequest request, HttpServletResponse response) {
        //必填项,需要验证
        String appid = getParam(request, "appid", "");
        String openid = getParam(request, "openid", "");
        String tradeNo = getParam(request, "tradeNo", "");
        String orderAmount = getParam(request, "orderAmount", "");
        String subject = getParam(request, "subject", "");
        String body = getParam(request, "body", "");
        String backUrl = getParam(request, "backUrl", "");
	    String clientIp=getParam(request,"clientIp","");

        //可为空参数
        String frontUrl = getParam(request, "frontUrl", "");
        String orderType = getParam(request, "orderType", "");
        String description = getParam(request, "description", "");
        String timeExpire = getParam(request, "timeExpire", "");
	    String integralAmount=getParam(request,"integralAmount","");//抵扣积分数
        RetApp retApp = new RetApp();
        try {
            //验证第三方签名
            OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
            Map<String, String> map = new HashMap();
            map.put("appid", appid);
            map.put("openid", openid);
            map.put("tradeNo", tradeNo);
            map.put("orderAmount", orderAmount);
            map.put("subject", subject);
            map.put("body", body);
            map.put("backUrl", backUrl);
	        map.put("clientIp", clientIp);
            if (!StringUtils.isEmpty(frontUrl)) {
                map.put("frontUrl", frontUrl);
            }
            if (!StringUtils.isEmpty(orderType)) {
                map.put("orderType", orderType);
            }
            if (!StringUtils.isEmpty(description)) {
                map.put("description", description);
            }
            if (!StringUtils.isEmpty(timeExpire)) {
                map.put("timeExpire", timeExpire);
            }
	        if (!StringUtils.isEmpty(integralAmount)) {
		        map.put("integralAmount", integralAmount);
	        }

            //计算签名
            String newSign = CspSignUtil.generateSign(map, openAppInfo.getAppSecret());
            JSONObject object = new JSONObject();
            object.put("sign", newSign);
            retApp.setData(object);
            retApp.setStatus(OK);
            retApp.setMessage("签名成功!");
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
            retApp.setMessage("签名失败,请联系管理员");
        }
        return retApp;
    }


    /**
     * 支付demo，接收通知接口
     * @param request
     * @param response
     */
    @RequestMapping(value = "/order/notifyDemo", produces = {"application/json"}, method = RequestMethod.POST)
    public void notifyDemo(HttpServletRequest request, HttpServletResponse response) {
        //必填项,需要验证
        String tradeNo = getParam(request, "tradeNo", "");
        String paidAmount = getParam(request, "paidAmount", "");
        String channel = getParam(request, "channel", "");
        String timePaid = getParam(request, "timePaid", "");
        String transeq = getParam(request, "transeq", "");
        String tradeStatus = getParam(request, "tradeStatus", "");
        String sign = getParam(request, "sign", "");
        String appid="146c74602a3c422b8e5aaf41d5eb73db";

        PrintWriter out=null ;
        try {
            out=response.getWriter();
            //验证第三方签名
            OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
            //签名
            Map map = new HashMap();
            map.put("tradeNo", tradeNo);
            map.put("paidAmount", paidAmount);
            map.put("channel",channel);
            map.put("timePaid",timePaid);
            map.put("transeq",transeq);
            map.put("tradeStatus", tradeStatus);

            boolean result = CspSignUtil.checkSign(map, openAppInfo.getAppSecret(),sign);
            if(result){
                logger.info("商户接收支付通知成功 {}",tradeNo);
                out.print("success");
            }else{
                logger.info("商户接收支付通知失败 {}",tradeNo);
                out.print("fail");
            }

        }catch (Exception e) {
            e.printStackTrace();
            out.print("fail");
        }finally {
            if(null!=out){
                out.close();
            }
        }
    }


    /**
     * 退款demo，接收通知接口
     * @param request
     * @param response
     */
    @RequestMapping(value = "/order/refundNotifyDemo", produces = {"application/json"}, method = RequestMethod.POST)
    public void refundNotifyDemo(HttpServletRequest request, HttpServletResponse response) {
        //必填项,需要验证
        String tradeNo = getParam(request, "tradeNo", "");
        String refundOrderNo = getParam(request, "refundOrderNo", "");
        String refundAmount = getParam(request, "refundAmount", "");
        String timeSucceed = getParam(request, "timeSucceed", "");
        String refundStatus = getParam(request, "refundStatus", "");
        String sign = getParam(request, "sign", "");
        String appid="146c74602a3c422b8e5aaf41d5eb73db";

        PrintWriter out=null ;
        try {
            out=response.getWriter();
            //验证第三方签名
            OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
            //签名
            Map map = new HashMap();
            map.put("tradeNo", tradeNo);
            map.put("refundOrderNo", refundOrderNo);
            map.put("refundAmount", refundAmount);
            map.put("timeSucceed",timeSucceed);
            map.put("refundStatus", refundStatus);

            boolean result = CspSignUtil.checkSign(map, openAppInfo.getAppSecret(),sign);
            if(result){
                logger.info("商户接收退款通知成功 {}",refundOrderNo);
                out.print("success");
            }else{
                logger.info("商户接收退款通知失败 {}",refundOrderNo);
                out.print("fail");
            }

        }catch (Exception e) {
            e.printStackTrace();
            out.print("fail");
        }finally {
            if(null!=out){
                out.close();
            }
        }
    }
}

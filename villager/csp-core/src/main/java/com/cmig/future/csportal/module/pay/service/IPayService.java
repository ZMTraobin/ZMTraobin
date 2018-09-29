package com.cmig.future.csportal.module.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.hand.hap.core.ProxySelf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 10:02 2017/5/27.
 * @Modified by zhangtao on 10:02 2017/5/27.
 */
public interface IPayService extends ProxySelf<IPayService> {

	/**
	 * 向第三方平台发送支付请求
	 * @param chargeMap
	 * @return 请求是否成功
	 */
	String pay(Map<String, Object> chargeMap)throws Exception;
	
	/**
	 * 接受服务端通知
	 * @param request
	 * @param response
	 * @return 通知是否成功
	 */
	boolean serverNotify(HttpServletRequest request, HttpServletResponse response)throws Exception;

	/**
	 * 查询订单支付状态
	 * @param paramMap
	 * @return
	 */
	String searchPayStatus(Map<String, Object> paramMap)throws Exception;


	/**
	 * 退款接口
	 * @param refundMap
	 * @return
	 */
	String refund(Map<String, Object> refundMap) throws Exception;

	/**
	 * 接受服务端退款通知通知
	 * @param request
	 * @param response
	 * @return 通知是否成功
	 */
	boolean refundNotify(HttpServletRequest request, HttpServletResponse response)throws Exception;
	
	/**
	 * 接收页面跳转通知
	 * @param request
	 * @param response
	 * @return 页面展示的bean
	 */
	boolean pageNotify(HttpServletRequest request, HttpServletResponse response)throws Exception;
	
	/**
	 * 对账
	 * @param request
	 * @param response
	 * @return
	 */
	String check(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	/**
	 * 取消订单
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	String cancelorder(HttpServletRequest request, HttpServletResponse response) throws Exception;


	/**
	 * 卡认证
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	String cardAuth(Map<String, Object> paramMap) throws Exception;


	/**
	 * 卡bin查询
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	String queryCardInfo(Map<String, Object> paramMap) throws Exception;


	/**
	 * 签名
	 * @param jsonObject
	 * @return
	 */
	String sign(JSONObject jsonObject, String merchantId);

	/**
	 * 验签
	 * @param jsonObject
	 * @param sign
	 * @return
	 */
	boolean checkSign(JSONObject jsonObject, String sign, String merchantId);

}

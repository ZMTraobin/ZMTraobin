package com.cmig.future.csportal.module.pay.order.service;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.module.pay.order.dto.OrderForm;
import com.cmig.future.csportal.module.pay.order.dto.OrderFormNotifyMc;
import com.cmig.future.csportal.module.pay.refund.dto.OrderFormRefund;
import com.cmig.future.csportal.module.pay.refund.dto.OrderFormRefundNotifyMc;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Refund;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IOrderFormService extends IBaseService<OrderForm>, ProxySelf<IOrderFormService>{

    void save(OrderForm orderForm);

    OrderForm findByOrderNumber(String orderNumber);

	JSONObject createCharge(OrderForm orderForm, String channel)throws Exception;

    void chargeSucceeded(Charge charge)throws Exception;

    void notifyToMerchant(final OrderForm orderForm);

    OrderForm findBySourceOrderNumber(OrderForm orderform);

    /**
     * 支付成功通知定时任务
     * @param orderFormNotifyMc
     */
    void excuteOrderNotifyJob(OrderFormNotifyMc orderFormNotifyMc);

    void refundSucceeded(Refund refund);

    void refundNotifyToMerchant(OrderFormRefund thisOrderFormRefund);
    /**
     * 退款成功通知定时任务
     * @param orderFormRefundNotifyMc
     */
    void excuteOrderRefundNotifyJob(OrderFormRefundNotifyMc orderFormRefundNotifyMc);

	/**
	 * 订单全额积分支付
	 * @param orderForm
	 */
	void integralPay(OrderForm orderForm)throws Exception;

	/**
	 * 商户订单提交
	 * @param map
	 * @return
	 * @throws Exception
	 */
	OrderForm orderSubmit(Map<String, String> map) throws Exception;

	/**
	 * 取消订单
	 * @param appid
	 * @param tradeNo
	 * @return
	 * @throws Exception
	 */
	OrderForm orderCancel(String appid, String tradeNo) throws Exception;

	/**
	 * 订单提交后返回值
	 * @param request
	 * @param orderForm
	 * @return
	 * @throws Exception
	 */
	Object getResultForSubmit(HttpServletRequest request, OrderForm orderForm)throws Exception;

	/**
	 * 取消订单
	 * @param orderForm
	 * @throws Exception
	 */
	void orderCancel(OrderForm orderForm)throws Exception;

	/**
	 * 订单超时未支付取消定时任务
	 */
	void excuteOrderCancelJob();

	List<OrderForm> selectByCondition(IRequest requestContext, OrderForm dto, int page, int pageSize);

	List<OrderForm> getLifePaidList(String appUserId);

	/**
	 * 订单支付成功处理
	 * @param orderForm
	 * @param payChannel
	 * @param timePaid
	 * @param transactionNo
	 * @param paidAmount
	 */
	void paySuccess(OrderForm orderForm, String payChannel, Date timePaid, String transactionNo, Long paidAmount)throws Exception ;

	Charge createChargeWithOutOrder(OrderForm orderForm) throws Exception;

	/**
	 * 生成商户收款二维码
	 * @param merchantNo
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	String bulidMerchantQr(String merchantNo) throws Exception;
}
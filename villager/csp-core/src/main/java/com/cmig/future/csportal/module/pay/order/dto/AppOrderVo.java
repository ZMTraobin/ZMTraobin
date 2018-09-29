package com.cmig.future.csportal.module.pay.order.dto;

import com.cmig.future.csportal.module.properties.payment.receivable.dto.MgtReceivableDetail;

import java.io.Serializable;
import java.util.List;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 10:22 2017/7/11.
 * @Modified by zhangtao on 10:22 2017/7/11.
 */
public class AppOrderVo implements Serializable {

	//添加无参的构造器
	public AppOrderVo(){
	}

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String token;
	private String appUserId;//用户
	private List<MgtReceivableDetail> receivableDetailList;//缴费明细清单
	private Long useIntegralNum;//抵扣积分数
	private String clientIp;	// 客户端ipv4地址
	private String paymentName;	// 支付人姓名
	private String backUrl;//服务器通知地址
	private String frontUrl;//页面通知地址

	private OrderForm orderForm;//订单详情

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(String appUserId) {
		this.appUserId = appUserId;
	}

	public List<MgtReceivableDetail> getReceivableDetailList() {
		return receivableDetailList;
	}

	public void setReceivableDetailList(List<MgtReceivableDetail> receivableDetailList) {
		this.receivableDetailList = receivableDetailList;
	}

	public Long getUseIntegralNum() {
		return useIntegralNum;
	}

	public void setUseIntegralNum(Long useIntegralNum) {
		this.useIntegralNum = useIntegralNum;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getPaymentName() {
		return paymentName;
	}

	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public String getFrontUrl() {
		return frontUrl;
	}

	public void setFrontUrl(String frontUrl) {
		this.frontUrl = frontUrl;
	}

	public OrderForm getOrderForm() {
		return orderForm;
	}

	public void setOrderForm(OrderForm orderForm) {
		this.orderForm = orderForm;
	}
}

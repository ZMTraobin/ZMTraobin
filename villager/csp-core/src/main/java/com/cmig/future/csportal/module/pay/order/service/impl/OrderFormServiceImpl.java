package com.cmig.future.csportal.module.pay.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.common.utils.RegExpValidatorUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.ZxingHandler;
import com.cmig.future.csportal.common.utils.fastdfs.FastDFSClient;
import com.cmig.future.csportal.common.utils.image.ImageUtils;
import com.cmig.future.csportal.common.utils.sign.CspSignUtil;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.operate.integral.components.IntegralRuleOperation;
import com.cmig.future.csportal.module.pay.conf.FailRetryHelper;
import com.cmig.future.csportal.module.pay.conf.OrderFormHelper;
import com.cmig.future.csportal.module.pay.conf.PingppConfig;
import com.cmig.future.csportal.module.pay.order.dto.OrderForm;
import com.cmig.future.csportal.module.pay.order.dto.OrderFormNotifyMc;
import com.cmig.future.csportal.module.pay.order.mapper.OrderFormMapper;
import com.cmig.future.csportal.module.pay.order.mapper.OrderFormNotifyMcMapper;
import com.cmig.future.csportal.module.pay.order.service.IOrderFormService;
import com.cmig.future.csportal.module.pay.refund.dto.OrderFormRefund;
import com.cmig.future.csportal.module.pay.refund.dto.OrderFormRefundNotifyMc;
import com.cmig.future.csportal.module.pay.refund.mapper.OrderFormRefundMapper;
import com.cmig.future.csportal.module.pay.refund.mapper.OrderFormRefundNotifyMcMapper;
import com.cmig.future.csportal.module.sys.code.CodeUtil;
import com.cmig.future.csportal.module.sys.openinfo.OpenAppUtils;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import com.cmig.future.csportal.module.sys.queue.TaskQueueDaemonThread;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.APIConnectionException;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.exception.AuthenticationException;
import com.pingplusplus.exception.ChannelException;
import com.pingplusplus.exception.InvalidRequestException;
import com.pingplusplus.exception.RateLimitException;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Order;
import com.pingplusplus.model.Refund;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Transactional
public class OrderFormServiceImpl extends BaseServiceImpl<OrderForm> implements IOrderFormService {

    private Logger logger = LoggerFactory.getLogger(OrderFormServiceImpl.class);

	private static final String appidTradeNoMapKey="csp.appid.tradeNo.map";

    // 创建一个可重用固定线程数的线程池
    static ExecutorService notifyPool = Executors.newFixedThreadPool(10);

    @Autowired
    private OrderFormMapper orderFormMapper;

    @Autowired
    private OrderFormNotifyMcMapper orderFormNotifyMcMapper;

    @Autowired
    private OrderFormRefundMapper orderFormRefundMapper;

	@Autowired
	private IAppUserService appUserService;

    @Autowired
    private OrderFormRefundNotifyMcMapper orderFormRefundNotifyMcMapper;

	@Autowired
	private IntegralRuleOperation integralRuleOperation;

    @Override
    public void save(OrderForm orderForm) {
        if (!StringUtils.isEmpty(orderForm.getId())) {
            orderFormMapper.updateByPrimaryKeySelective(orderForm);
        } else {
            super.mapper.insertSelective(orderForm);
	        //取消超时未支付的订单任务-添加到任务队列
	        addToTaskQueue(orderForm.getId(),orderForm.getTimeExpire(),new Date());
        }
    }


	class OrderCancelRunnable implements Runnable,Serializable {
    	private Long id;
		public OrderCancelRunnable(Long id) {
			this.id = id;
		}

		@Override
		public void run() {
			try {
				orderCancel(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public int hashCode() {
			return id.hashCode();
		}

		@Override
		public boolean equals(Object object) {
			if (object instanceof OrderCancelRunnable) {
				return object.hashCode() == hashCode() ? true : false;
			}
			return false;
		}
	}


	/**
	 * 取消超时未支付的订单任务-添加到任务队列
	 * @param orderId
	 * @param timeExpire
	 * @param createDate
	 */
	private void addToTaskQueue(Long orderId,Date timeExpire, Date createDate) {
		logger.debug("提交订单后向超时取消任务队列中添加该任务 orderId {} timeExpire {}  createDate {} ",orderId,DateUtils.formatDateTime(timeExpire),DateUtils.formatDateTime(createDate));
		TaskQueueDaemonThread.getInstance().put((timeExpire.getTime() - createDate.getTime()), new OrderCancelRunnable(orderId));
	}

	/**
	 * 支付成功后从超时取消任务队列中删除该任务
	 * @param orderId
	 * @return
	 */
	private boolean removeFromTaskQueue(Long orderId){
		boolean result=TaskQueueDaemonThread.getInstance().remove(new OrderCancelRunnable(orderId));
		logger.debug("支付成功后从超时取消任务队列中删除该任务 orderId {} result {} ",orderId,result);
		return result;
	}

	@Override
    public OrderForm findByOrderNumber(String orderNumber) {
        return orderFormMapper.findByOrderNumber(orderNumber);
    }

    /**
     * 创建ping++ charge
     * @param orderForm
     * @param channel
     * @return
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws RateLimitException
     */
    @Override
    public JSONObject createCharge(OrderForm orderForm, String channel) throws Exception {
	    Map<String, Object> chargeMap = new HashMap<String, Object>();
	    chargeMap.put("order_no", orderForm.getOrderNumber());// 推荐使用 8-20 位，要求数字或字母，不允许其他字符
	    chargeMap.put("channel", channel);// 支付使用的第三方支付渠道取值，请参考：https://www.pingxx.com/api#api-c-new
	    chargeMap.put("client_ip", orderForm.getClientIp()); // 发起支付请求客户端的 IP 地址，格式为 IPV4，如: 127.0.0.1
	    chargeMap.put("currency",PingppConfig.currency);//币种
	    chargeMap.put("amount", orderForm.getPayableAmount());//实付金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100）
	    chargeMap.put("subject", orderForm.getSubject());//商品的标题，该参数最长为 32 个 Unicode 字符，银联全渠道（ upacp / upacp_wap ）限制在 32 个字节
	    chargeMap.put("body", orderForm.getBody());//商品的描述信息，该参数最长为 128 个 Unicode 字符，yeepay_wap 对于该参数长度限制为 100 个 Unicode 字符。
	    Map<String, Object> extra = new HashMap<String, Object>();
	    if(OrderFormHelper.PAY_CHANNEL_WX_PUB.equals(channel)){
		    extra.put("open_id", orderForm.getWxOpenId());
	    }

	    if(OrderFormHelper.PAY_CHANNEL_ALIPAY_WAP.equals(channel)){
		    extra.put("success_url", orderForm.getSuccessUrl());
	    }

	    //isv_wap 线下扫码（固定码）
	    if(OrderFormHelper.PAY_CHANNEL_ISV_WAP.equals(channel)) {
		    if (OrderFormHelper.PAY_CHANNEL_ALIPAY.equals(orderForm.getPayChannel())) {
			    //具体支付渠道，支持： alipay 、 wx
			    extra.put("pay_channel", "alipay");
			    //前端通知地址。支付成功或失败后，需要跳转到的地址 URL。
			    extra.put("result_url", orderForm.getSuccessUrl());
			    //上送您系统维护的终端序列号，要求不同终端此号码不一样，会显示在对账单中，固定 8 位。如没有终端概念，可使用 00000001 。
			    extra.put("terminal_id", "00000001");
		    }

		    if (OrderFormHelper.PAY_CHANNEL_WX.equals(orderForm.getPayChannel())) {
			    //具体支付渠道，支持： alipay 、 wx
			    extra.put("pay_channel", "wx");
			    //前端通知地址。支付成功或失败后，需要跳转到的地址 URL。
			    extra.put("result_url", orderForm.getSuccessUrl());
			    //上送您系统维护的终端序列号，要求不同终端此号码不一样，会显示在对账单中，固定 8 位。如没有终端概念，可使用 00000001 。
			    extra.put("terminal_id", "00000001");
		    }
	    }

	    //extra.put("success_url", "http://127.0.0.1:8383/hap/common/paydemo/views/wap.html");
	    chargeMap.put("extra", extra);//额外参数
	    /**
	     * 订单失效时间，用 Unix 时间戳表示。时间范围在订单创建后的 1 分钟到 15 天，默认为 1 天，
	     创建时间以 Ping++ 服务器时间为准。 微信对该参数的有效值限制为 2 小时内；
	     银联对该参数的有效值限制为 1 小时内。
	     */
	    if(!StringUtils.isEmpty(orderForm.getTimeExpire())){
		    //微信
		    if(OrderFormHelper.PAY_CHANNEL_WX.equals(channel)){

		    }
		    //银联
		    if(OrderFormHelper.PAY_CHANNEL_UPACP.equals(channel)){

		    }
		    //chargeMap.put("time_expire",  orderForm.getTimeExpire().getTime());
	    }

	    Map<String, String> initialMetadata = new HashMap<String, String>();
	    initialMetadata.put("openid", orderForm.getAppUserId());
	    chargeMap.put("metadata", initialMetadata);
	    chargeMap.put("description", orderForm.getDescription());

        PingppConfig.initPingxxAppid(orderForm.getAppId());
        Map<String, String> app = new HashMap<String, String>();
        app.put("id", Pingpp.appId);//ping++ appid
        chargeMap.put("app", app);
        Charge charge=Charge.create(chargeMap);
        if (null != charge) {
            if (StringUtils.isEmpty(orderForm.getTimeExpire())) {
                orderForm.setTimeExpire(DateUtils.unixTimeToDate(charge.getTimeExpire()));
            }
            orderForm.setChargeId(charge.getId());
            orderForm.setPayChannel(charge.getChannel());
            orderForm.setClientIp(charge.getClientIp());
            save(orderForm);
        }
	    JSONObject jsonObject = new JSONObject();
	    jsonObject.put("charge", charge);
        return jsonObject;
    }

    /**
     * 处理支付成功通知
     * @param charge
     * @throws Exception
     */
    @Override
    public void chargeSucceeded(Charge charge) throws Exception {
        logger.debug("支付通知处理开始 {}", charge.getOrderNo());
        String orderNumber = charge.getOrderNo();
        OrderForm orderForm = orderFormMapper.findByOrderNumber(orderNumber);
        if (null == orderForm) {
            throw new DataWarnningException("订单号" + orderNumber + "不存在");
        }

        /*if (StringUtils.isEmpty(orderForm.getChargeId())) {
            throw new DataWarnningException("订单号" + orderNumber + "未能匹配ping++订单");
        } else if (!orderForm.getChargeId().equals(charge.getId())) {
            throw new DataWarnningException("订单号" + orderNumber + "未能匹配ping++订单");
        }*/
        if (OrderFormHelper.OREDER_STATUS_Y.equals(orderForm.getPayStatus())) {
            logger.debug("此订单已处理 {}", orderNumber);
        } else {
	        paySuccess(orderForm,charge.getChannel(),DateUtils.unixTimeToDate(charge.getTimePaid()),charge.getTransactionNo(),new Long(charge.getAmountSettle()));
        }
        logger.debug("支付通知处理结束 {}", charge.getOrderNo());
    }

    @Override
    public void paySuccess(OrderForm orderForm,String payChannel,Date timePaid,String transactionNo,Long paidAmount) throws Exception {
    	String trade_no="";
	    //通知积分系统扣减积分
	    if(null!=orderForm.getIntegralAmount()&&orderForm.getIntegralAmount().longValue()>0) {
		    trade_no=notifyToIntegral(orderForm.getOrderNumber(), orderForm.getAppUserId(), orderForm.getIntegralAmount());
	    }
	    //如果是全额积分支付，交易流水号为积分交易流水号
	    if(OrderFormHelper.PAY_CHANNEL_INTEGRAL.equals(payChannel)){
		    transactionNo=trade_no;
	    }

	    //csp更新订单状态
	    orderForm.setPayStatus(OrderFormHelper.OREDER_STATUS_Y);
	    if(!OrderFormHelper.PAY_CHANNEL_ISV_WAP.equals(payChannel)) {
		    orderForm.setPayChannel(payChannel);
	    }
	    orderForm.setTimePaid(timePaid);
	    orderForm.setTranseq(transactionNo);
	    orderForm.setPaidAmount(paidAmount);
	    save(orderForm);

	    //初始化异步通知记录
	    OrderFormNotifyMc orderFormNotifyMc = new OrderFormNotifyMc();
	    orderFormNotifyMc.setOrderId(orderForm.getId());
	    orderFormNotifyMc.setTimeNextNotify(new Date());// 第一次默认立即通知
	    orderFormNotifyMcMapper.insertSelective(orderFormNotifyMc);

	    // 通知商户
	    Runnable notifyRun = () -> notifyToMerchant(orderForm);
	    notifyPool.execute(notifyRun);

	    //删除队列中的任务
	    removeFromTaskQueue(orderForm.getId());
    }

	/**
	 * 支付成功后，通知积分系统扣减积分
	 * @param orderNumber
	 * @param appUserId
	 * @param integralAmount
	 * @throws DocumentException
	 */
	private String notifyToIntegral(String orderNumber, String appUserId,Long integralAmount) throws Exception {
		if(null!=integralAmount&&integralAmount.longValue()>0) {
			//回调积分系统，扣减积分
			AppUser appUser=appUserService.get(appUserId);
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("out_trade_no", orderNumber);
			paramMap.put("uid", appUser.getSourceSystemId());
			paramMap.put("credits", integralAmount.toString());
			paramMap.put("type", IntegralRuleOperation.INTEGRAL_TYPE_CSP_SHOP);
			logger.info("订单{}支付回调扣减积分{}开始...... ",orderNumber,integralAmount);
			JSONObject object = integralRuleOperation.deduction(paramMap);
			String message = object.get("message").toString();
			String returnCode = object.get("returnCode").toString();
			if(IntegralRuleOperation.SUCCESS_CODE.equals(returnCode)){
				String trade_no=object.get("trade_no")==null?"":object.get("trade_no").toString();
				logger.info("订单{}支付回调扣减积分{}结束，返回码 {} 返回消息 {} 交易流水号{}",orderNumber,integralAmount,returnCode,message,trade_no);
				return trade_no;
			}else{
				throw new ServiceException(RestApiExceptionEnums.INTEGRAL_DEDUCTION_FAIL,message);
			}
		}
		return null;
	}

	/**
	 * 订单全额积分支付
	 * @param orderForm
	 * @throws Exception
	 */
	@Override
	public void integralPay(OrderForm orderForm) throws Exception {
		if(OrderFormHelper.OREDER_STATUS_C.equals(orderForm.getPayStatus())){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"该订单已失效，请重新提交订单！");
		}

		if(OrderFormHelper.OREDER_STATUS_Y.equals(orderForm.getPayStatus())){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"该订单已完成支付，请勿重复支付！");
		}
		//通知积分系统扣减积分
		if(null!=orderForm.getIntegralAmount()&&orderForm.getIntegralAmount().longValue()>0) {
			paySuccess(orderForm, OrderFormHelper.PAY_CHANNEL_INTEGRAL, new Date(), null, new Long(0));
		}

	}

	/**
     * 通知商户支付成功
     * @param thisOrderForm
     */
    public void notifyToMerchant(OrderForm thisOrderForm) {
        logger.info("支付通知商户处理开始 {}", thisOrderForm.getOrderNumber());
        if (!StringUtils.isEmpty(thisOrderForm)) {
            try {
                // 查询订单信息
                //OrderForm thisOrderForm = orderFormMapper.selectByPrimaryKey(orderId);

                // 查询该订单是否已存在通知失败记录
                OrderFormNotifyMc orderFormNotifyMc = new OrderFormNotifyMc();
                orderFormNotifyMc.setOrderId(thisOrderForm.getId());
                orderFormNotifyMc = orderFormNotifyMcMapper.findByOrderId(orderFormNotifyMc);
                if (thisOrderForm != null && orderFormNotifyMc != null) {
                    if (!StringUtils.isEmpty(thisOrderForm.getBackUrl())) {
                        // 获取商户来源
                        OpenAppInfo openAppInfo = OpenAppUtils.getOpenAppInfo(thisOrderForm.getAppId());
                        if (null != openAppInfo) {
                            // 签名
                            Map<String,String> map = new HashMap();
                            map.put("tradeNo", thisOrderForm.getSourceOrderNumber());
                            map.put("paidAmount", thisOrderForm.getPaidAmount().toString());
                            map.put("channel",thisOrderForm.getPayChannel());
                            map.put("timePaid",new Long(thisOrderForm.getTimePaid().getTime()).toString());
                            map.put("transeq",thisOrderForm.getTranseq());
                            map.put("tradeStatus", "Y");
                            String sign = CspSignUtil.generateSign(map, openAppInfo.getAppSecret());
                            map.put("sign", sign);
                            // 调用接口通知商户
                            String result = HttpUtil.post(thisOrderForm.getBackUrl(), map);
                            // 通知返回成功
                            if (OrderFormHelper.OREDER_NOTIFY_SUCCESS.equals(result)) {
                                orderFormNotifyMc.setStatus(OrderFormHelper.NOTIFY_FAIL_Y);
                                orderFormNotifyMc.setTimes(orderFormNotifyMc.getTimes() + 1);
                                orderFormNotifyMc.setTimeNotified(new Date());
                                orderFormNotifyMcMapper.updateByPrimaryKeySelective(orderFormNotifyMc);
                                // 通知返回订单不存在
                            } else if (OrderFormHelper.OREDER_NOTIFY_NO_ORDER.equals(result)) {
                                orderFormNotifyMc.setStatus(OrderFormHelper.NOTIFY_MC_C);
                                orderFormNotifyMc.setTimes(orderFormNotifyMc.getTimes() + 1);
                                orderFormNotifyMcMapper.updateByPrimaryKeySelective(orderFormNotifyMc);
                                // 通知返回失败
                            } else {
                                orderFormNotifyMc.setStatus(OrderFormHelper.NOTIFY_MC_N);
                                orderFormNotifyMc.setTimes(orderFormNotifyMc.getTimes() + 1);

	                            // 下次通知时间
                                Date timeNextNotify = DateUtils.addSeconds(orderFormNotifyMc.getCreationDate(), FailRetryHelper.getNextTimeSeconds(orderFormNotifyMc.getTimes().intValue()));
                                orderFormNotifyMc.setTimeNextNotify(timeNextNotify);
                                orderFormNotifyMcMapper.updateByPrimaryKeySelective(orderFormNotifyMc);
                            }

                        }
                    } else {
                        orderFormNotifyMc.setStatus(OrderFormHelper.NOTIFY_MC_C);
                        orderFormNotifyMc.setTimes(orderFormNotifyMc.getTimes() + 1);
                        orderFormNotifyMcMapper.updateByPrimaryKeySelective(orderFormNotifyMc);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logger.info("***********订单编号为空*************");
        }
        logger.info("支付通知商户处理结束 {}", thisOrderForm.getOrderNumber());
    }

    @Override
    public OrderForm findBySourceOrderNumber(OrderForm orderform) {
        return orderFormMapper.findBySourceOrderNumber(orderform);
    }

    @Override
    public List<OrderForm> select(IRequest request, OrderForm condition, int pageNum,
            int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return orderFormMapper.select(condition);
    }

    /**
     * 处理退款通知
     * @param refund
     */
    public void refundSucceeded(Refund refund) {
        logger.info("退款通知处理开始 refundId:{} refundOrderNo:{}", refund.getId(),refund.getOrderNo());

        OrderFormRefund orderFormRefund = orderFormRefundMapper.findByRefundOrderNo(refund.getOrderNo());
        if (null == orderFormRefund) {
            throw new DataWarnningException("OrderNo:" + refund.getOrderNo() + "不存在");
        }

        if (StringUtils.isEmpty(orderFormRefund.getRefundOrderNo())) {
            throw new DataWarnningException("OrderNo:" + refund.getOrderNo() + "未能匹配ping++订单");
        } else if (!orderFormRefund.getRefundOrderNo().equals(refund.getOrderNo())) {
            throw new DataWarnningException("OrderNo:" + refund.getOrderNo() + "未能匹配ping++订单");
        }
        if (OrderFormHelper.REFUND_STATUS_SUCCEEDED.equals(orderFormRefund.getStatus())) {
            logger.info("此订单已处理 refundId:{} refundOrderNo:{}", refund.getId(),refund.getOrderNo());
        } else {
            OrderFormRefundNotifyMc orderFormRefundNotifyMc = new OrderFormRefundNotifyMc();
            orderFormRefundNotifyMc.setRefundOrderId(orderFormRefund.getId());
            orderFormRefundNotifyMc.setTimeNextNotify(new Date());// 第一次默认立即通知
            orderFormRefundNotifyMcMapper.insertSelective(orderFormRefundNotifyMc);

            orderFormRefund.setStatus(refund.getStatus());
            orderFormRefund.setTimeSucceed(DateUtils.unixTimeToDate(refund.getTimeSucceed()));
            orderFormRefund.setAmount(Long.valueOf(refund.getAmount()));
            orderFormRefund.setTransactionNo(refund.getTransactionNo());
            orderFormRefund.setFailureMsg(refund.getFailureMsg());
            orderFormRefund.setFailureCode(refund.getFailureCode());
            orderFormRefund.setDescription(refund.getDescription());
            orderFormRefundMapper.updateByPrimaryKeySelective(orderFormRefund);

	        // 通知商户
	        Runnable notifyRun = () -> refundNotifyToMerchant(orderFormRefund);
	        notifyPool.execute(notifyRun);
        }
        logger.info("退款通知处理结束 refundId:{} refundOrderNo:{}", refund.getId(),refund.getOrderNo());
    }

    /**
     * 通知商户退款成功
     * @param thisOrderFormRefund
     */
    public void refundNotifyToMerchant(OrderFormRefund thisOrderFormRefund) {
        logger.info("退款通知商户处理开始 {}", thisOrderFormRefund.getId());
        if (!StringUtils.isEmpty(thisOrderFormRefund)) {
            try {
                // 查询订单信息
                //OrderFormRefund thisOrderFormRefund = orderFormRefundMapper.selectByPrimaryKey(refundOrderId);

                // 查询该订单是否已存在通知记录
                OrderFormRefundNotifyMc orderFormRefundNotifyMc = new OrderFormRefundNotifyMc();
                orderFormRefundNotifyMc.setRefundOrderId(thisOrderFormRefund.getId());
                orderFormRefundNotifyMc = orderFormRefundNotifyMcMapper.findByRefundOrderId(orderFormRefundNotifyMc);
                if (thisOrderFormRefund != null && orderFormRefundNotifyMc != null) {
                    if (!StringUtils.isEmpty(thisOrderFormRefund.getBackUrl())) {
                        OrderForm orderForm=orderFormMapper.selectByPrimaryKey(thisOrderFormRefund.getOrderId());
                        // 获取商户来源
                        OpenAppInfo openAppInfo = OpenAppUtils.getOpenAppInfo(orderForm.getAppId());
                        if (null != openAppInfo) {
                            // 签名
                            Map<String,String> map = new HashMap();
                            map.put("tradeNo", orderForm.getSourceOrderNumber());
                            map.put("refundOrderNo", thisOrderFormRefund.getRefundOrderNo());
                            map.put("refundAmount", thisOrderFormRefund.getAmount().toString());
                            map.put("timeSucceed",new Long(thisOrderFormRefund.getTimeSucceed().getTime()).toString());
                            map.put("refundStatus",thisOrderFormRefund.getStatus());
                            String sign = CspSignUtil.generateSign(map, openAppInfo.getAppSecret());
                            map.put("sign", sign);
                            // 调用接口通知商户
                            String result = HttpUtil.post(thisOrderFormRefund.getBackUrl(), map);
                            // 通知返回成功
                            if (OrderFormHelper.OREDER_NOTIFY_SUCCESS.equals(result)) {
                                orderFormRefundNotifyMc.setStatus(OrderFormHelper.NOTIFY_FAIL_Y);
                                orderFormRefundNotifyMc.setTimes(orderFormRefundNotifyMc.getTimes() + 1);
                                orderFormRefundNotifyMc.setTimeNotified(new Date());
                                orderFormRefundNotifyMcMapper.updateByPrimaryKeySelective(orderFormRefundNotifyMc);
                                // 通知返回订单不存在
                            } else if (OrderFormHelper.OREDER_NOTIFY_NO_ORDER.equals(result)) {
                                orderFormRefundNotifyMc.setStatus(OrderFormHelper.NOTIFY_MC_C);
                                orderFormRefundNotifyMc.setTimes(orderFormRefundNotifyMc.getTimes() + 1);
                                orderFormRefundNotifyMcMapper.updateByPrimaryKeySelective(orderFormRefundNotifyMc);
                                // 通知返回失败
                            } else {
                                orderFormRefundNotifyMc.setStatus(OrderFormHelper.NOTIFY_MC_N);
                                orderFormRefundNotifyMc.setTimes(orderFormRefundNotifyMc.getTimes() + 1);

	                            // 下次通知时间
	                            Date timeNextNotify = DateUtils.addSeconds(orderFormRefundNotifyMc.getCreationDate(), FailRetryHelper.getNextTimeSeconds(orderFormRefundNotifyMc.getTimes().intValue()));
                                orderFormRefundNotifyMc.setTimeNextNotify(timeNextNotify);
                                orderFormRefundNotifyMcMapper.updateByPrimaryKeySelective(orderFormRefundNotifyMc);
                            }

                        }
                    } else {
                        orderFormRefundNotifyMc.setStatus(OrderFormHelper.NOTIFY_MC_C);
                        orderFormRefundNotifyMc.setTimes(orderFormRefundNotifyMc.getTimes() + 1);
                        orderFormRefundNotifyMcMapper.updateByPrimaryKeySelective(orderFormRefundNotifyMc);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logger.info("***********退款单号为空*************");
        }
        logger.info("退款通知商户处理结束 {}", thisOrderFormRefund.getId());
    }

    /**
     * 支付成功通知定时任务
     * @param orderFormNotifyMc
     */
    @Override
    public void excuteOrderNotifyJob(OrderFormNotifyMc orderFormNotifyMc) {
        logger.info("==========订单支付通知定时任务开始==========");
        List<OrderFormNotifyMc> list = orderFormNotifyMcMapper.getNotifyOrdersForJob(orderFormNotifyMc);
        if (!list.isEmpty()) {
            for (OrderFormNotifyMc order : list) {
                Runnable notifyRun = () -> {
	                OrderForm orderForm=orderFormMapper.selectByPrimaryKey(order.getOrderId());
                    notifyToMerchant(orderForm);
                };
                notifyPool.execute(notifyRun);
            }
        }
	    logger.info("==========订单支付通知定时任务结束==========");
    }

    /**
     * 退款成功通知定时任务
     * @param orderFormRefundNotifyMc
     */
    @Override
    public void excuteOrderRefundNotifyJob(OrderFormRefundNotifyMc orderFormRefundNotifyMc) {
        List<OrderFormRefundNotifyMc> list = orderFormRefundNotifyMcMapper.getNotifyOrdersForJob(orderFormRefundNotifyMc);
        if (!list.isEmpty()) {
            for (OrderFormRefundNotifyMc order : list) {
                Runnable notifyRun = new Runnable() {
                    @Override
                    public void run() {
	                    OrderFormRefund thisOrderFormRefund=orderFormRefundMapper.selectByPrimaryKey(order.getRefundOrderId());
                        refundNotifyToMerchant(thisOrderFormRefund);
                    }
                };
                notifyPool.execute(notifyRun);
            }
        }
    }


	/**
	 * 商户提交订单
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public OrderForm orderSubmit(Map<String,String> map) throws Exception {
		//必填参数
		String appid=map.get("appid").toString();//应用的唯一标识
		String openid=map.get("openid").toString();//用户唯一标识
		String tradeNo=map.get("tradeNo").toString();//商户唯一订单号
		String orderAmount=map.get("orderAmount").toString();//订单总金额（分）
		String subject=map.get("subject").toString();//商品的标题
		String body=map.get("body").toString();//商品的描述信息
		String backUrl=map.get("backUrl").toString();//通知商户的服务器端地址
		String clientIp=map.get("clientIp").toString();//客户端 ipv4 地址
		String sign=map.get("sign").toString();//	签名

		//非必填参数
		String frontUrl=map.get("frontUrl")==null?"":map.get("frontUrl").toString();//跳转商户的页面地址
		String orderType=map.get("orderType")==null?"":map.get("orderType").toString();//订单分类
		String description=map.get("description")==null?"":map.get("description").toString();//订单描述
		String timeExpire=map.get("timeExpire")==null?"":map.get("timeExpire").toString();//订单失效时间
		String integralAmount=StringUtils.isEmpty(map.get("integralAmount"))?"0":map.get("integralAmount").toString();//抵扣积分数
		String merchantNo=map.get("merchantNo")==null?"":map.get("merchantNo").toString();
		String payChannel=map.get("payChannel")==null?"":map.get("payChannel").toString();

		//验证第三方签名
		OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
		if (!CspSignUtil.checkSign(map, openAppInfo.getAppSecret(), sign)) {
			throw new ServiceException(RestApiExceptionEnums.SIGN_ERROR);
		}

		if (StringUtils.isEmpty(appid)) {
			throw new DataWarnningException("appid不能为空");
		}
		if (StringUtils.isEmpty(openid)) {
			throw new DataWarnningException("openid不能为空");
		}
		if (StringUtils.isEmpty(tradeNo)) {
			throw new DataWarnningException("商户订单号不能为空");
		}
		if (StringUtils.isEmpty(orderAmount)) {
			throw new DataWarnningException("订单总金额不能为空");
		}else if(!RegExpValidatorUtils.IsIntNumber(orderAmount)){
			throw new DataWarnningException("订单总金额格式不正确");
		}

		if(!StringUtils.isEmpty(integralAmount)) {
			if (!RegExpValidatorUtils.isIntNumberAndZero(integralAmount)) {
				throw new DataWarnningException("抵扣积分数格式不正确");
			} else if (new Integer(integralAmount).compareTo(new Integer(orderAmount)) > 0) {
				throw new DataWarnningException("抵扣积分数不能大于订单总金额");
			}

			//如果有积分抵扣，需要再次校验抵扣积分数不能超过积分余额和剩余可抵扣积分数
			/*if(new Integer(integralAmount).intValue()> 0) {
				JSONObject result = integralRuleService.getMaxAvailableIntegral(IntegralRuleOperation.INTEGRAL_TYPE_CSP_SHOP, orderAmount, openid);
				Integer maxAvailableNum = new Integer(result.get("maxAvailableNum").toString());//最大可抵扣积分
				if (new Integer(integralAmount).compareTo(maxAvailableNum) > 0) {
					throw new DataWarnningException("抵扣积分数超过最大限额");
				}
			}*/
		}

		if (StringUtils.isEmpty(subject)) {
			throw new DataWarnningException("商品的标题不能为空");
		} else if (subject.getBytes().length > 32) {
			throw new DataWarnningException("商品的标题不能超过32个字节");
		}
		if (StringUtils.isEmpty(body)) {
			throw new DataWarnningException("商品的描述信息不能为空");
		} else if (body.length() > 100) {
			throw new DataWarnningException("商品的描述信息不能超过100个字符");
		}
		if (StringUtils.isEmpty(backUrl)) {
			throw new DataWarnningException("服务端通知地址不能为空");
		}
		if (StringUtils.isEmpty(sign)) {
			throw new DataWarnningException("签名信息不能为空");
		}
		if (orderType.length() > 50) {
			throw new DataWarnningException("订单分类不能超过50个字符");
		}
		if (description.length() > 255) {
			throw new DataWarnningException("订单附加说明不能超过255个字符");
		}

		if (StringUtils.isEmpty(clientIp)) {
			throw new DataWarnningException("客户端ip不能为空");
		}

		if(!StringUtils.isEmpty(timeExpire)){
			if(!RegExpValidatorUtils.IsIntNumber(timeExpire)){
				throw new DataWarnningException("订单失效时间格式不正确");
			}
			Date maxTimeExpire= DateUtils.addDays(new Date(),15);
			if(maxTimeExpire.getTime()<Long.valueOf(timeExpire)){
				throw new DataWarnningException("订单失效时间最晚为订单创建后15天");
			}
		}

		AppUser appUser = appUserService.getBySourceSystemId(openid);
		if (StringUtils.isEmpty(appUser)) {
			throw new DataWarnningException("openid错误");
		}
		//验证缓存中是否存在该订单号，如果不存在则将该订单号放入缓存，如果存在则抛出异常
		if(tradeNoInCache(appid, tradeNo)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"商户订单号不能重复");
		}


		OrderForm orderForm=new OrderForm();
		//商户订单号唯一性校验
		OrderForm orderFormQuery=new OrderForm();
		orderFormQuery.setAppId(appid);
		orderFormQuery.setSourceOrderNumber(tradeNo);
		orderFormQuery=findBySourceOrderNumber(orderFormQuery);
		if(null!=orderFormQuery){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"商户订单号不能重复");
		}

		orderForm.setClientIp(clientIp);
		orderForm.setAppUserId(appUser.getId());
		orderForm.setOrderNumber(OrderFormHelper.getRandomNum());
		orderForm.setOrderAmount(Long.parseLong(orderAmount));
		orderForm.setIntegralAmount(Long.parseLong(integralAmount));
		orderForm.setDiscountAmount(new Long(0));
		//应付金额=订单总额-活动折扣金额-积分抵扣
		orderForm.setPayableAmount(orderForm.getOrderAmount()-orderForm.getDiscountAmount()-orderForm.getIntegralAmount());

		//是否开启测试模式
		if(JedisUtils.exists("csp.payment.test.model")&&orderForm.getPayableAmount()>1l){
			orderForm.setPayableAmount(1L);
			orderForm.setDiscountAmount(orderForm.getOrderAmount()-orderForm.getPayableAmount()-orderForm.getIntegralAmount());
		}

		orderForm.setSubject(subject);
		orderForm.setBody(body);
		orderForm.setBackUrl(backUrl);
		orderForm.setAppId(appid);
		orderForm.setSourceSystem(openAppInfo.getAppName());
		orderForm.setSourceOrderNumber(tradeNo);
		orderForm.setPayStatus(OrderFormHelper.OREDER_STATUS_N);
		if(!StringUtils.isEmpty(payChannel)){
			orderForm.setPayChannel(payChannel);
		}
		if (!StringUtils.isEmpty(merchantNo)) {
			orderForm.setMerchantNo(merchantNo);
		}
		if (!StringUtils.isEmpty(frontUrl)) {
			orderForm.setFrontUrl(frontUrl);
		}
		if (!StringUtils.isEmpty(orderType)) {
			orderForm.setOrderType(orderType);
		}
		if (!StringUtils.isEmpty(description)) {
			orderForm.setDescription(description);
		}
		if (!StringUtils.isEmpty(timeExpire)) {
			orderForm.setTimeExpire(DateUtils.unixTimeToDate(Long.valueOf(timeExpire)));
		}else{
			orderForm.setTimeExpire((DateUtils.addMinutes(new Date(),OrderFormHelper.ORDER_EXPIRE_MINTES)));//默认30分钟失效
		}
		//积分冻结
		if(orderForm.getIntegralAmount().intValue()>0) {
			Map integralMap=new HashMap();
			integralMap.put("uid",appUser.getSourceSystemId());
			integralMap.put("out_trade_no",orderForm.getOrderNumber());
			integralMap.put("credits",orderForm.getIntegralAmount().toString());
			integralMap.put("type",IntegralRuleOperation.INTEGRAL_TYPE_CSP_SHOP);
			JSONObject jsonObject =integralRuleOperation.integralDJ(integralMap);
			if(!IntegralRuleOperation.SUCCESS_CODE.equals(jsonObject.get("returnCode").toString())){
				//订单保存到数据库后，清除缓存中的订单号
				removeCache(appid,tradeNo);
				throw new ServiceException(RestApiExceptionEnums.INTEGRAL_DJ_FAIL,jsonObject.get("message").toString());
			}
		}
		save(orderForm);
		//订单保存到数据库后，清除缓存中的订单号
		removeCache(appid,tradeNo);
		return orderForm;
	}

	/**
	 * 判断订单号是否存在缓存中
	 * @param appid
	 * @param tradeNo
	 * @return
	 */
	private synchronized boolean tradeNoInCache(String appid, String tradeNo) {
		if(JedisUtils.hexists(appidTradeNoMapKey,appid+":"+tradeNo)){
			return true;
		}else {
			JedisUtils.hset(appidTradeNoMapKey, appid + ":" + tradeNo, "1", 0);
		}
		return false;
	}

	/**
	 * 删除缓存中的订单号
	 * @param appid
	 * @param tradeNo
	 */
	private boolean removeCache(String appid, String tradeNo) {
		return JedisUtils.hdel(appidTradeNoMapKey,appid+":"+tradeNo);
	}


	/**
	 * 订单提交后返回值
	 * @param request
	 * @param orderForm
	 * @return
	 * @throws Exception
	 */
	@Override
	public Object getResultForSubmit(HttpServletRequest request, OrderForm orderForm) throws Exception {
		boolean isIntegral=false;
		String chargeUrl=Global.getProjectPath(request) + "/user/st/pingpp/createCharge";
		//订单应付金额为0，表示全额积分支付
		if(orderForm.getPayableAmount().longValue()==0){
			isIntegral=true;
		}
		net.sf.json.JSONObject object = new net.sf.json.JSONObject();
		object.put("isIntegral",isIntegral);//是否积分抵扣完
		object.put("orderNumber", orderForm.getOrderNumber());//订单号
		object.put("orderAmount", orderForm.getOrderAmount());//订单总额
		object.put("discountAmount", orderForm.getDiscountAmount());//活动折扣金额
		object.put("integralAmount", orderForm.getIntegralAmount());//积分抵扣
		object.put("payableAmount", orderForm.getPayableAmount());//应付金额=订单总额-活动折扣金额-积分抵扣
		object.put("subject", orderForm.getSubject());
		object.put("body", orderForm.getBody());
		object.put("payChannel", orderForm.getPayChannel()==null?"":orderForm.getPayChannel());//支付渠道
		object.put("transeq", orderForm.getTranseq()==null?"":orderForm.getTranseq());//交易流水号
		object.put("paidAmount", orderForm.getPaidAmount()==null?"":orderForm.getPaidAmount());//实付金额
		object.put("chargeUrl", chargeUrl);
		return object;
	}


	/**
	 * 取消订单
	 * @param appid
	 * @param tradeNo
	 * @return
	 * @throws Exception
	 */
	@Override
	public OrderForm orderCancel(String appid,String tradeNo) throws Exception {
		//商户订单号唯一性校验
		OrderForm orderFormQuery=new OrderForm();
		orderFormQuery.setAppId(appid);
		orderFormQuery.setSourceOrderNumber(tradeNo);
		orderFormQuery=findBySourceOrderNumber(orderFormQuery);
		orderCancel(orderFormQuery);
		return orderFormQuery;
	}

	/**
	 * 取消订单
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private OrderForm orderCancel(Long id) throws Exception {
		//根据订单主键查询
		OrderForm orderFormQuery=orderFormMapper.selectByPrimaryKey(id);
		orderCancel(orderFormQuery);
		return orderFormQuery;
	}

	/**
	 * 订单超时未支付取消定时任务
	 */
	@Override
	public void excuteOrderCancelJob() {
		List<OrderForm> list = orderFormMapper.getCancelOrdersForJob();
		if (!list.isEmpty()) {
			for (OrderForm orderForm : list) {
				Runnable notifyRun = new Runnable() {
					@Override
					public void run() {
						try {
							orderCancel(orderForm);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				notifyPool.execute(notifyRun);
			}
		}
	}

	/**
	 * 订单取消
	 * @param orderForm
	 * @throws Exception
	 */
	@Override
	public void orderCancel(OrderForm orderForm) throws Exception {
		if(null==orderForm){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"商户订单号不存在");
		}else{
			logger.debug("订单取消 {}", orderForm.getOrderNumber());
			if(!OrderFormHelper.OREDER_STATUS_N.equals(orderForm.getPayStatus())){
				throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"只有待支付的订单可以取消");
			}
			//积分解冻
			if(orderForm.getIntegralAmount().intValue()>0) {
				AppUser appUser=appUserService.get(orderForm.getAppUserId());
				Map map=new HashMap();
				map.put("uid",appUser.getSourceSystemId());
				map.put("out_trade_no",orderForm.getOrderNumber());
				try {
					JSONObject jsonObject =integralRuleOperation.integralJD(map);
					if(!IntegralRuleOperation.SUCCESS_CODE.equals(jsonObject.get("returnCode").toString())){
						throw new ServiceException(RestApiExceptionEnums.INTEGRAL_JD_FAIL,jsonObject.get("message").toString());
					}
				}catch (ServiceException e){
					if(!e.getCode().equals(RestApiExceptionEnums.INTEGRAL_SYSTEM_UNABLE.getCode())){
						throw e;
					}
				}

			}

			orderForm.setPayStatus(OrderFormHelper.OREDER_STATUS_C);
			save(orderForm);
		}

	}

    @Override
    public List<OrderForm> selectByCondition(IRequest requestContext, OrderForm dto, int page, int pageSize) {
	    PageHelper.startPage(page,pageSize);
        return orderFormMapper.selectByCondition(dto);
    }


	@Override
	public List<OrderForm> getLifePaidList(String appUserId) {
		return orderFormMapper.getLifePaidList(appUserId);
	}

	@Override
	public Charge createChargeWithOutOrder(OrderForm orderForm) throws Exception {

		String successUrl=orderForm.getSuccessUrl();
		AppUser appUser;
		if(!StringUtils.isEmpty(orderForm.getAppUserId())){
			appUser=appUserService.get(orderForm.getAppUserId());
		}else{
			appUser=appUserService.getByMobile("18210225831");
		}

		String subject="扫码付款订单";
		Map<String, String> map = new HashMap();
		map.put("appid", OrderFormHelper.CSP_APP_ID);
		map.put("openid", appUser.getSourceSystemId());
		map.put("tradeNo", OrderFormHelper.getRandomNum());
		map.put("orderAmount", orderForm.getOrderAmount().toString());
		map.put("subject", subject);
		map.put("body", subject);
		map.put("backUrl", orderForm.getBackUrl());
		map.put("clientIp", orderForm.getClientIp());
		map.put("frontUrl", orderForm.getFrontUrl());
		map.put("orderType", OrderFormHelper.ORDER_TYPE_SCAN_ORDER_ISV_WAP);
		map.put("description", "");
		map.put("timeExpire", "");
		map.put("integralAmount", "");
		map.put("merchantNo",orderForm.getMerchantNo());
		map.put("payChannel", orderForm.getPayChannel());
		map.put("sign", CspSignUtil.generateSign(map,OrderFormHelper.CSP_APP_SECRET));
		//提交订单
		orderForm = orderSubmit(map);

		PingppConfig.initPingxxAppid(orderForm.getAppId());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", appUser.getSourceSystemId()); // 用户在当前 app 下的 User ID, 可选
		params.put("app", "app_O0mTiDWrjj50enbr"); // App ID, 必传
		params.put("merchant_order_no", orderForm.getOrderNumber()); // 商户订单号, 必传
		params.put("subject", subject); // 商品的标题, 必传
		params.put("body", subject); // 商品的描述信息, 必传
		params.put("amount", orderForm.getPayableAmount()); // 订单总金额，单位：分, 必传
		params.put("currency", "cny"); // 仅支持人民币 cny, 必传
		params.put("client_ip", orderForm.getClientIp()); // 客户端的 IP 地址 (IPv4 格式，要求商户上传真实的，渠道可能会判断), 必传
		params.put("receipt_app","app_uXjnDKrX5GqHfrrz");
		params.put("service_app","app_uXjnDKrX5GqHfrrz");
		Order obj = Order.create(params); // 创建 Order 对象 方法

		params = new HashMap<>();
		params.put("channel", OrderFormHelper.PAY_CHANNEL_ISV_WAP);
		params.put("charge_amount", orderForm.getPayableAmount());
		Map<String, Object> extra = new HashMap<>(); // extra: 根据各个渠道传入相应的参数
		//具体支付渠道，支持： alipay 、 wx
		extra.put("pay_channel", orderForm.getPayChannel());
		//前端通知地址。支付成功或失败后，需要跳转到的地址 URL。
		extra.put("result_url", successUrl);
		//上送您系统维护的终端序列号，要求不同终端此号码不一样，会显示在对账单中，固定 8 位。如没有终端概念，可使用 00000001 。
		extra.put("terminal_id", "00000001");

		params.put("extra", extra);
		Order order = Order.pay(obj.getId(), params); // 创建支付 Order 对象 方法
		Charge charge=order.getCharges().getData().get(0);
		if (null != charge&&!StringUtils.isEmpty(charge.getId())) {
			OrderForm updateOrder=new OrderForm();
			updateOrder.setId(orderForm.getId());
			updateOrder.setChargeId(charge.getId());
			orderFormMapper.updateByPrimaryKeySelective(updateOrder);
		}
		return charge;
	}

	@Override
	public String bulidMerchantQr(String merchantNo) throws Exception {
		String merchantName= CodeUtil.getDictLabel(merchantNo,"CSP.MERCHANT_NO",null,null);

		String CSP_H5_SERVER_URL= Global.getConfig("CSP.MY.CENTER.MGT.SERVER.URL","");
		String contents=CSP_H5_SERVER_URL+"/mgt/offLineActivity/activityPay.html?t="+System.currentTimeMillis()+"&merchantNo="+merchantNo+"&merchantName="+java.net.URLEncoder.encode(merchantName,"utf-8");

		String basePath = Global.getUserfilesTempDir() + "qrCode/";
		String fileName = IdGen.uuid() + ".png";
		// 二维码
		String codeImgPath = basePath + File.separator + "merchantNo" + File.separator;
		File file = new File(codeImgPath);
		if (!file.exists()) {
			file.mkdirs();
		}

		String filePath=codeImgPath + fileName;
		int width = 300, height = 300;
		ZxingHandler.encode2(contents, width, height, filePath);//生成二维码临时文件

		File qrcode=new File(filePath);
		ImageUtils.pressText(merchantName,filePath, "微软雅黑",Font.PLAIN, Color.black, 20, 260, 10);
		String fid = FastDFSClient.uploadFile(qrcode,fileName);//上传到文件服务器
		qrcode.delete();//删除临时文件
		return Global.getFullImagePath(fid);
	}
}
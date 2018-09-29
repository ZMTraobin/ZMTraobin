package com.cmig.future.csportal.module.pay.conf;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

/**
 * Created by zhangtao107@126.com on 2016/8/22.
 */
public class OrderFormHelper {

    /**
     * 商户订单分类
     */
    public static final String ORDER_TYPE_MGT_PAYMENT="mgtPayment";
	public static final String ORDER_TYPE_OTHER="other";
	public static final String ORDER_TYPE_LIFE_PAY ="lifePay_";
	public static final String ORDER_TYPE_SCAN_ORDER_ISV_WAP ="scan_order_isv_wap";// 线下扫码（固定码）;

    /**
     * 支付状态
     */
    public static final String OREDER_STATUS_Y ="Y";//已支付
    public static final String OREDER_STATUS_P="P";//支付处理中
    public static final String OREDER_STATUS_N ="N";//待支付
	public static final String OREDER_STATUS_C ="C";//已取消

    /**
     * 外部商户订单通知返回结果
     */
    public static final String OREDER_NOTIFY_SUCCESS ="success";//通知成功
    public static final String OREDER_NOTIFY_FAIL ="fail";//通知失败
    public static final String OREDER_NOTIFY_NO_ORDER ="no_order";//消费订单号不存在，无需重新通知

    /**
     * 商户通知失败原因
     */
    public static final String NOTIFY_MC_N ="N";//等待通知
    public static final String NOTIFY_FAIL_Y ="Y";//通知成功
    public static final String NOTIFY_MC_C ="C";//订单号不存在，无需重新通知


    /**
     * 支付渠道
     */
    public static final String PAY_CHANNEL_ALIPAY ="alipay";//	支付宝 APP 支付
    public static final String PAY_CHANNEL_WX ="wx";//	微信 APP 支付
    public static final String PAY_CHANNEL_UPACP ="upacp";//	银联支付，即银联 APP 支付
    public static final String PAY_CHANNEL_UPACP_WAP ="upacp_wap";//	银联手机网页支付
	public static final String PAY_CHANNEL_INTEGRAL ="integralPay";//	全额积分支付
	public static final String PAY_CHANNEL_LEJIA_PAY="lejiaPay";//乐家易付
	public static final String PAY_CHANNEL_WX_PUB="wx_pub";//微信公众号支付
	public static final String PAY_CHANNEL_WX_WAP="wx_wap";//微信wap
	public static final String PAY_CHANNEL_ALIPAY_WAP="alipay_wap";//支付宝wap
	public static final String PAY_CHANNEL_ISV_WAP="isv_wap";// 线下扫码（固定码）



    /**
     * 退款状态（目前支持三种状态: pending: 处理中; succeeded: 成功; failed: 失败）
     */
    public static final String REFUND_STATUS_PENDING="pending";
    public static final String REFUND_STATUS_SUCCEEDED="succeeded";
    public static final String REFUND_STATUS_FAILED="failed";

	/**
	 * CSP应用接入appid和secret
	 */
	public static final String CSP_APP_ID="65a17951724c4194a67434704c9dbf92";
	public static final String CSP_APP_SECRET="77851c52ab3e4ebe860ec7a50224f604";

	public static final int ORDER_NUMBER_MAX_LENGTH=7;//每日最大订单7位数
	public static final String ORDER_NUMBER_KEY_PREFIX="csp.orderNumber:";

    /**
     * 订单状态
     */
    public static final String ORDER_STATUS_WAITING_RECEIVE="WAITING_RECEIVE";//待接单
    public static final String ORDER_STATUS_RECEIVED="RECEIVED";//已接单
    public static final String ORDER_STATUS_RECEIVED_GOOD="RECEIVED_GOOD";//已收货

	/**
	 * 订单默认失效时长
	 */
	public static final int ORDER_EXPIRE_MINTES=30;//单位分钟

	private static SecureRandom random = new SecureRandom();

	public static String randomString(int length) {
		String str = new BigInteger(130, random).toString(32);
		return str.substring(0, length);
	}

	/**
	 * 获取订单号序号
	 * @return
	 */
	public static String getRandomNum(){
		return new Date().getTime() +String.format("%0"+ORDER_NUMBER_MAX_LENGTH+"d",random.nextInt(9999999));
	}

	/**
	 * 获取订单号序号
	 * @return
	 */
/*	public static String getRandomNum(){
		Calendar calendar= Calendar.getInstance();
		String date= DateUtils.formatDate(calendar.getTime(),"yyMMdd");
		long count= JedisUtils.incr(ORDER_NUMBER_KEY_PREFIX+date, DateUtils.getTodayLeftSecond(calendar));
		return date+String.format("%0"+ORDER_NUMBER_MAX_LENGTH+"d", count);
	}*/
}


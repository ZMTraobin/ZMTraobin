package com.cmig.future.csportal.module.pay.components;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.verify.RSAUtilsWithKey;
import com.cmig.future.csportal.module.pay.conf.OrderFormHelper;
import com.cmig.future.csportal.module.pay.order.dto.OrderForm;
import com.cmig.future.csportal.module.pay.order.mapper.OrderFormMapper;
import com.cmig.future.csportal.module.pay.order.service.IOrderFormService;
import com.cmig.future.csportal.module.pay.refund.dto.OrderFormRefund;
import com.cmig.future.csportal.module.pay.refund.dto.OrderFormRefundNotifyMc;
import com.cmig.future.csportal.module.pay.refund.mapper.OrderFormRefundMapper;
import com.cmig.future.csportal.module.pay.refund.mapper.OrderFormRefundNotifyMcMapper;
import com.cmig.future.csportal.module.pay.service.IPayService;
import com.cmig.future.csportal.module.pay.service.impl.LejiaPayConfig;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.dto.AppUserCard;
import com.cmig.future.csportal.module.user.appuser.mapper.AppUserCardMapper;
import com.cmig.future.csportal.module.user.appuser.mapper.AppUserMapper;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 18:06 2017/5/27.
 * @Modified by zhangtao on 18:06 2017/5/27.
 */

@Service
@Transactional
public class CspLejiaPayServiceImpl  {

	private static Logger log= LoggerFactory.getLogger(CspLejiaPayServiceImpl.class);


	//创建一个可重用固定线程数的线程池
	static ExecutorService notifyPool = Executors.newFixedThreadPool(10);

	@Autowired
	private AppUserCardMapper appUserCardMapper;

	@Autowired
	private AppUserMapper appUserMapper;

	@Autowired
	private OrderFormMapper orderFormMapper;

	@Autowired
	private IAppUserService appUserService;

	@Autowired
	private OrderFormRefundMapper orderFormRefundMapper;

	@Autowired
	private OrderFormRefundNotifyMcMapper orderFormRefundNotifyMcMapper;

	@Autowired
	private IPayService lejiaPayService;

	@Autowired
	private IOrderFormService orderFormService;

	/**
	 * 异步更新用户姓名
	 * @param appUserId
	 * @param userName
	 */
	public void updateUserName(final String appUserId,final String userName) {
		Runnable r= new Runnable(){
			public void run(){
				try {
					AppUser appUser=appUserService.get(appUserId);
					if (null!=appUser) {
						appUser.setUserName(userName);
						appUserService.save(appUser);
						log.info("更新用户【"+appUser.getMobile()+"】姓名成功");
					}

				} catch (Exception e) {
					log.info("更新用户【"+appUserId+"】姓名失败");
					e.printStackTrace();
				}
			}
		};
		notifyPool.execute(r);
	}
	/**
	 * 三网-乐家易付卡认证
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public AppUserCard userCardAuth(Map<String, Object> paramMap,String merchantId) throws Exception {
		String appUserId=paramMap.get("appUserId").toString();
		paramMap.put("userId",appUserId);

		paramMap.put("merchantId",merchantId);
		String result= lejiaPayService.cardAuth(paramMap);
		JSONObject jsonObject= parseObject(result);

		if(LejiaPayConfig.SUCCESS_CODE.equals(jsonObject.get("code"))) {
			//把所有的卡是否默认值设置为N
			appUserCardMapper.updateDefaultToN(appUserId);
			//绑定当前卡，并设置是否默认值为Y
			AppUserCard appUserCard = new AppUserCard();
			appUserCard.setAppUserId(appUserId);
			String cardNo=paramMap.get("cardNo").toString();
			//卡号加密存储
			appUserCard.setCardNo(RSAUtilsWithKey.encrypt(cardNo));
			appUserCard.setCardType(paramMap.get("cardType").toString());
			appUserCard.setIdNo(paramMap.get("idNo").toString());
			appUserCard.setIdType(paramMap.get("idType").toString());
			appUserCard.setName(paramMap.get("name").toString());
			appUserCard.setBankCode(paramMap.get("bankCode").toString());
			appUserCard.setBankMobile(paramMap.get("bankMobile").toString());
			appUserCard.setStatus(LejiaPayConfig.CARD_STATUS_Y);
			appUserCard.setDefaultFlag(Constants.YES);
			appUserCardMapper.insertSelective(appUserCard);
			//异步更新用户姓名
			updateUserName(appUserId,appUserCard.getName());
			return appUserCard;
		}else{
			throw new DataWarnningException(jsonObject.get("msg").toString());
		}

	}


	/**
	 * 三网-乐家易付支付
	 * @param orderForm
	 * @param cardId
	 * @param merchantId
	 * @return
	 * @throws Exception
	 */
	public JSONObject pay(OrderForm orderForm, int cardId,String merchantId)throws Exception {
		AppUserCard appUserCard= appUserCardMapper.selectByPrimaryKey(cardId);
		if(null==appUserCard){
			throw new DataWarnningException("cardId参数错误");
		}
		if(!appUserCard.getAppUserId().equals(orderForm.getAppUserId())){
			throw new DataWarnningException("只能支付自己的订单");
		}
		AppUser appUser=appUserMapper.selectByPrimaryKey(orderForm.getAppUserId());
		Map<String, Object> chargeMap = new HashMap<String, Object>();
		chargeMap.put("userId", appUser.getSourceSystemId());//用户中心id
		chargeMap.put("amount", new BigDecimal(orderForm.getPayableAmount()).divide(new BigDecimal(100)));//金额
		String cardNo=appUserCard.getCardNo();
		cardNo=RSAUtilsWithKey.decrypt(cardNo);
		chargeMap.put("cardNo", cardNo);//卡号
		chargeMap.put("origiOrderId", orderForm.getOrderNumber());//渠道订单号
		chargeMap.put("orderIp", orderForm.getClientIp());//请求ip
		chargeMap.put("idNo", appUserCard.getIdNo());//证件号
		chargeMap.put("idType", appUserCard.getIdType());//证件类型
		chargeMap.put("name", appUserCard.getName());//姓名
		chargeMap.put("bankMobile", appUserCard.getBankMobile());//银行预留手机号
		chargeMap.put("merchantId",merchantId);
		String result= lejiaPayService.pay(chargeMap);
		JSONObject jsonObject=parseObject(result);
		if(LejiaPayConfig.SUCCESS_CODE.equals(jsonObject.get("respCode"))) {
			updateOrderStatus(orderForm, jsonObject);

		}else{
			throw new DataWarnningException(jsonObject.get("payMsg").toString());
		}
		return jsonObject;
	}

	/**
	 * 三网-查询订单支付状态
	 * @param orderForm
	 * @return
	 * @throws Exception
	 */
	public JSONObject searchPayStatus(OrderForm orderForm,String merchantId)throws Exception {
		Map<String,Object> paramMap=new HashMap<>();
		paramMap.put("origOrderNo",orderForm.getOrderNumber());
		paramMap.put("merchantId",merchantId);
		String result= lejiaPayService.searchPayStatus(paramMap);
		JSONObject jsonObject=parseObject(result);
		if(!OrderFormHelper.OREDER_STATUS_Y.equals(orderForm.getPayStatus())){
			updateOrderStatus(orderForm, jsonObject);
		}
		return jsonObject;
	}

	/**
	 * 更新订单状态
	 * @param orderForm
	 * @param jsonObject
	 * @throws Exception
	 */
	private void updateOrderStatus(OrderForm orderForm, JSONObject jsonObject) throws Exception {
		String status=jsonObject.get("payStatus").toString();
		//orderForm.setOrderStatus(status);
		if("SUCC".equals(status)){
			String tradeNo=jsonObject.get("payOrderId")==null?"":jsonObject.get("payOrderId").toString();
			orderFormService.paySuccess(orderForm, OrderFormHelper.PAY_CHANNEL_LEJIA_PAY, new Date(), tradeNo, orderForm.getPayableAmount());
		}else if("DEALING".equals(status)){
			orderForm.setPayStatus(OrderFormHelper.OREDER_STATUS_P);
			orderFormMapper.updateByPrimaryKeySelective(orderForm);
		}else{
			orderForm.setPayStatus(OrderFormHelper.OREDER_STATUS_N);
			orderFormMapper.updateByPrimaryKeySelective(orderForm);
		}
	}

	/**
	 * 三网-退款
	 * @param orderForm
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public JSONObject refund(OrderForm orderForm,Map<String, Object> params,String merchantId) throws Exception {
		AppUser appUser=appUserMapper.selectByPrimaryKey(orderForm.getAppUserId());

		OrderFormRefund orderFormRefund=new OrderFormRefund();
		orderFormRefund.setRefundId(null);
		orderFormRefund.setRefundOrderNo("R_"+OrderFormHelper.getRandomNum());
		orderFormRefund.setAmount(Long.valueOf(params.get("amount").toString()));
		orderFormRefund.setStatus(OrderFormHelper.REFUND_STATUS_PENDING);
		orderFormRefund.setDescription(params.get("description").toString());
		orderFormRefund.setFailureCode(null);
		orderFormRefund.setFailureMsg(null);
		orderFormRefund.setTransactionNo(null);
		orderFormRefund.setOrderId(orderForm.getId());
		orderFormRefund.setOrderNumber(orderForm.getOrderNumber());
		orderFormRefund.setChargeId(orderForm.getChargeId());
		orderFormRefund.setBackUrl(params.get("backUrl").toString());
		orderFormRefundMapper.insertSelective(orderFormRefund);

		Map<String,Object> paramMap=new HashMap<>();
		paramMap.put("userId",appUser.getSourceSystemId());
		paramMap.put("refundAmt",orderFormRefund.getAmount()/100);
		paramMap.put("origiOrderId",orderForm.getOrderNumber());
		paramMap.put("merchantId",merchantId);
		String result= lejiaPayService.refund(paramMap);
		JSONObject jsonObject=parseObject(result);
		if(LejiaPayConfig.SUCCESS_CODE.equals(jsonObject.get("respCode").toString())){
			jsonObject.put("success",true);

			orderFormRefund.setTimeSucceed(new Date());
			orderFormRefund.setStatus(OrderFormHelper.REFUND_STATUS_SUCCEEDED);
			orderFormRefundMapper.updateByPrimaryKeySelective(orderFormRefund);
			//退款通知
			OrderFormRefundNotifyMc orderFormRefundNotifyMc = new OrderFormRefundNotifyMc();
			orderFormRefundNotifyMc.setRefundOrderId(orderFormRefund.getId());
			orderFormRefundNotifyMc.setTimeNextNotify(new Date());// 第一次默认立即通知
			orderFormRefundNotifyMcMapper.insertSelective(orderFormRefundNotifyMc);
		}else{
			orderFormRefund.setStatus(OrderFormHelper.REFUND_STATUS_FAILED);
			orderFormRefundMapper.updateByPrimaryKeySelective(orderFormRefund);
		}
		jsonObject.put("refundStatus",orderFormRefund.getStatus());
		jsonObject.put("refundOrderNo",orderFormRefund.getRefundOrderNo());
		jsonObject.put("failure_code", jsonObject.get("respCode").toString());
		jsonObject.put("failure_msg", jsonObject.get("respMsg").toString());
		return jsonObject;
	}
	/**
	 * 三网-查询卡bin信息
	 * @param cardNo
	 * @return
	 * @throws Exception
	 */
	public JSONObject queryCardInfo(String cardNo,String merchantId) throws Exception {
		Map<String,Object> paramMap=new HashMap<>();
		paramMap.put("cardNo",cardNo);
		paramMap.put("merchantId",merchantId);
		String result= lejiaPayService.queryCardInfo(paramMap);
		JSONObject jsonObject= parseObject(result);
		return jsonObject;
	}


	/**
	 * 三网-卡解绑
	 * @param cardId
	 * @param appUserId
	 * @throws Exception
	 */
	public void userCardUnbundled(String cardId,String appUserId) throws Exception {
		AppUserCard appUserCard =appUserCardMapper.selectByPrimaryKey(cardId);
		if(null==appUserCard) throw new DataWarnningException("参数不正确");
		if(appUserId.equals(appUserCard.getAppUserId())) {
			appUserCard.setStatus(LejiaPayConfig.CARD_STATUS_N);
			appUserCardMapper.updateByPrimaryKeySelective(appUserCard);
		}else{
			throw new DataWarnningException("只能解绑自己的银行卡");
		}
	}


	/**
	 * 三网-乐家易付支付
	 * @param chargeMap
	 * @param cardId
	 * @return
	 * @throws Exception
	 */
	public JSONObject pay(Map<String, Object> chargeMap, int cardId,String merchantId)throws Exception {
		AppUserCard appUserCard= appUserCardMapper.selectByPrimaryKey(cardId);
		if(null==appUserCard){
			throw new DataWarnningException("cardId参数错误");
		}
		String openid=chargeMap.get("openid").toString();
		AppUser appUser=appUserMapper.selectByPrimaryKey(appUserCard.getAppUserId());

		if(!appUser.getSourceSystemId().equals(openid)){
			throw new DataWarnningException("只能支付自己的订单");
		}

		chargeMap.put("userId", openid);//用户中心用户id
		String cardNo=appUserCard.getCardNo();
		cardNo=RSAUtilsWithKey.decrypt(cardNo);
		chargeMap.put("cardNo", cardNo);//卡号
		chargeMap.put("idNo", appUserCard.getIdNo());//证件号
		chargeMap.put("idType", appUserCard.getIdType());//证件类型
		chargeMap.put("name", appUserCard.getName());//姓名
		chargeMap.put("bankMobile", appUserCard.getBankMobile());//银行预留手机号
		chargeMap.put("merchantId",merchantId);
		String result= lejiaPayService.pay(chargeMap);
		JSONObject jsonObject=parseObject(result);
		return jsonObject;
	}

	/**
	 * 三网-查询订单支付状态
	 * @param origOrderNo
	 * @return
	 * @throws Exception
	 */
	public JSONObject searchPayStatus(String origOrderNo,String merchantId)throws Exception {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("origOrderNo", origOrderNo);
		paramMap.put("merchantId",merchantId);
		String result = lejiaPayService.searchPayStatus(paramMap);
		JSONObject jsonObject = parseObject(result);
		return jsonObject;
	}
}

package com.cmig.future.csportal.module.lifepay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.sign.CspSignUtil;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.lifepay.dto.BillResponse;
import com.cmig.future.csportal.module.lifepay.dto.LifePayBill;
import com.cmig.future.csportal.module.lifepay.helper.LifePayHelper;
import com.cmig.future.csportal.module.lifepay.mapper.LifePayBillMapper;
import com.cmig.future.csportal.module.lifepay.service.ILifePaymentService;
import com.cmig.future.csportal.module.pay.conf.OrderFormHelper;
import com.cmig.future.csportal.module.pay.order.dto.OrderForm;
import com.cmig.future.csportal.module.pay.order.dto.OrderFormLine;
import com.cmig.future.csportal.module.pay.order.mapper.OrderFormLineMapper;
import com.cmig.future.csportal.module.pay.order.service.IOrderFormService;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 14:29 2017/11/21.
 * @Modified by zhangtao on 14:29 2017/11/21.
 */
@Service
public class LifePaymentServiceImpl implements ILifePaymentService {

	private static final Logger logger= LoggerFactory.getLogger(LifePaymentServiceImpl.class);

	@Autowired
	private IOrderFormService orderFormService;

	@Autowired
	private IAppUserService appUserService;

	@Autowired
	private LifePayBillMapper lifePayBillMapper;

	@Autowired
	private OrderFormLineMapper orderFormLineMapper;

	@Override
	public String getLiftFeeInfo(String cityCode)throws Exception  {
		if(StringUtils.isEmpty(cityCode)){
			throw  new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}
		Map map = new HashMap();
		map.put("cityCode", cityCode);
		map.put("clientAbbr", LifePayHelper.clientAbbr);
		map.put("Sign", LifePayHelper.encodeByRSA(map));
		return HttpUtil.post(LifePayHelper.serverUrl + "plat/getLiftFeeInfo", map);
	}

	@Override
	public String productQuery(String cityCode, String productId)throws Exception  {
		if(StringUtils.isEmpty(cityCode)
				|| StringUtils.isEmpty(productId)){
			throw  new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}
		Map map = new HashMap();
		map.put("cityCode", cityCode);
		map.put("productId", productId);
		map.put("clientAbbr", LifePayHelper.clientAbbr);
		map.put("Sign", LifePayHelper.encodeByRSA(map));
		return HttpUtil.post(LifePayHelper.serverUrl + "plat/productQuery", map);
	}

	@Override
	public String billConfQuery(String billOrgId, String productId)throws Exception  {
		if(StringUtils.isEmpty(billOrgId)
				|| StringUtils.isEmpty(productId)){
			throw  new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}
		Map map = new HashMap();
		map.put("billOrgId", billOrgId);
		map.put("productId", productId);
		map.put("clientAbbr", LifePayHelper.clientAbbr);
		map.put("Sign", LifePayHelper.encodeByRSA(map));
		return HttpUtil.post(LifePayHelper.serverUrl + "plat/billConfQuery", map);
	}

	@Override
	public BillResponse queryBillNo(String billOrgId, String productId, String startMonth, String billNo, String barcode, String searchType)throws Exception  {
		//判断参数是否为空
		if(StringUtils.isEmpty(billOrgId)
				|| StringUtils.isEmpty(productId)
				|| StringUtils.isEmpty(searchType)){
			throw  new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}
		//0使用户号查询，其他使用条形码
		if(searchType.equals("0")){
			if(StringUtils.isEmpty(billNo)
					|| StringUtils.isEmpty(startMonth)){
				throw  new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"billNo and startMonth");
			}
		}else{
			if(StringUtils.isEmpty(barcode)){
				throw  new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"barcode");
			}
		}

		Map map=new HashMap();
		map.put("billOrgId",billOrgId);
		map.put("clientAbbr",LifePayHelper.clientAbbr);
		map.put("productId",productId);
		map.put("searchType",searchType);
		if("0".equals(searchType)){
			map.put("billNo",billNo);
			map.put("startMonth",startMonth);
		}else{
			map.put("barcode",barcode);
		}
		map.put("Sign",LifePayHelper.encodeByRSA(map));
		String result= HttpUtil.post(LifePayHelper.serverUrl+"plat/queryByBillNo",map);
		if(!StringUtils.isEmpty(result)){
			BillResponse billResponse=JSONObject.parseObject(result,BillResponse.class);
			return billResponse;
		}else{
			throw new ServiceException(RestApiExceptionEnums.UNKNOW_EXCEPTION);
		}
	}

	@Override
	public LifePayBill createBillOrder(HttpServletRequest request, LifePayBill lifePayBill)throws Exception  {
		//参数校验
		if(StringUtils.isEmpty(lifePayBill.getBillOrgId())
				||StringUtils.isEmpty(lifePayBill.getProductType())
				||StringUtils.isEmpty(lifePayBill.getProductId())
				||StringUtils.isEmpty(lifePayBill.getSearchType())
				||StringUtils.isEmpty(lifePayBill.getBackUrl())
				||StringUtils.isEmpty(lifePayBill.getFrontUrl())
				||StringUtils.isEmpty(lifePayBill.getClientIp())){
			throw  new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		if("0".equals(lifePayBill.getSearchType())){
			if(StringUtils.isEmpty(lifePayBill.getDoorCode())){
				throw  new ServiceException(RestApiExceptionEnums.ARGS_NULL,"doorCode");
			}

			if(StringUtils.isEmpty(lifePayBill.getBillMonth())){
				throw  new ServiceException(RestApiExceptionEnums.ARGS_NULL,"billMonth");
			}
		}else if(StringUtils.isEmpty(lifePayBill.getBarcode())){
			throw  new ServiceException(RestApiExceptionEnums.ARGS_NULL,"barcode");
		}
		//查询账单
		BillResponse billResponse= queryBillNo(lifePayBill.getBillOrgId(),lifePayBill.getProductId(),lifePayBill.getBillMonth(),lifePayBill.getDoorCode(),lifePayBill.getBarcode(),lifePayBill.getSearchType());
		if(null==billResponse||billResponse.getData()==null||billResponse.getData().size()==0){
			throw new ServiceException(RestApiExceptionEnums.LIFE_PAY_BILL_NULL);
		}
		BillResponse.BillInfo billInfo= billResponse.getData().get(0);
		AppUser appUser=appUserService.getByToken(lifePayBill.getToken());
		Map<String, String> map = new HashMap();
		map.put("appid", OrderFormHelper.CSP_APP_ID);
		map.put("openid", appUser.getSourceSystemId());
		map.put("tradeNo", OrderFormHelper.getRandomNum());
		map.put("orderAmount", new Long((billInfo.getBillAmt().multiply(new BigDecimal(100))).longValue()).toString());

		String subject=lifePayBill.getBillMonth()==null?"":lifePayBill.getBillMonth();
		if("waterRate".equals(lifePayBill.getProductType())){
			subject+=" 水费";
		}else if("powerRate".equals(lifePayBill.getProductType())){
			subject+=" 电费";
		}else if("gasBill".equals(lifePayBill.getProductType())){
			subject+=" 燃气费";
		}
		map.put("subject", subject);
		map.put("body", subject);
		map.put("backUrl", lifePayBill.getBackUrl());
		map.put("clientIp", lifePayBill.getClientIp());
		map.put("frontUrl", lifePayBill.getFrontUrl());
		map.put("orderType", OrderFormHelper.ORDER_TYPE_LIFE_PAY+lifePayBill.getProductType());
		map.put("description", "");
		map.put("timeExpire", "");
		map.put("integralAmount", lifePayBill.getUseIntegralNum()==null?"": lifePayBill.getUseIntegralNum().toString());
		map.put("sign", CspSignUtil.generateSign(map,OrderFormHelper.CSP_APP_SECRET));
		//提交订单
		OrderForm orderForm = orderFormService.orderSubmit(map);
		lifePayBill.setOrderForm(orderForm);

		//订单行插入
		lifePayBill.setBillId(billInfo.getBillId());
		lifePayBill.setBillOrgName(billInfo.getBillOrgName());
		lifePayBill.setBillNo(billInfo.getBillNo());
		lifePayBill.setBillMonth(billInfo.getBillMonth());
		lifePayBill.setBillAmt(orderForm.getOrderAmount());
		lifePayBill.setBillRecordTimes(billInfo.getBillRecordTimes());
		lifePayBill.setBillAddr(billInfo.getBillAddr());
		lifePayBill.setBillOwner(billInfo.getBillOwner());
		lifePayBill.setOverdueFee((billInfo.getOverdueFee()==null?BigDecimal.ZERO:billInfo.getOverdueFee().multiply(new BigDecimal(100))).longValue());
		lifePayBill.setBillStatus(billInfo.getBillStatus());
		lifePayBill.setIsInsurance(billInfo.getIsInsurance());
		saveOrder(lifePayBill,orderForm.getId());
		return lifePayBill;
	}

	private void saveOrder(LifePayBill lifePayBill, Long orderId) {
		//账单详情
		lifePayBillMapper.insertSelective(lifePayBill);
		//订单行
		OrderFormLine orderFormLine=new OrderFormLine();
		orderFormLine.setOrderId(orderId);
		orderFormLine.setReceivableId(lifePayBill.getId());
		orderFormLineMapper.insertSelective(orderFormLine);
	}

	@Override
	public String getRecords(String clientOrderNo, String thirdOrderNo)throws Exception  {
		//判断参数是否为空
		if(StringUtils.isEmpty(clientOrderNo)
				&& StringUtils.isEmpty(thirdOrderNo)){
			throw  new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}
		Map map=new HashMap();
		if(!StringUtils.isEmpty(clientOrderNo)){
			map.put("clientOrderNo",clientOrderNo);
		}
		if(!StringUtils.isEmpty(thirdOrderNo)){
			map.put("thirdOrderNo",thirdOrderNo);
		}
		map.put("clientAbbr",LifePayHelper.clientAbbr);
		map.put("Sign",LifePayHelper.encodeByRSA(map));
		return HttpUtil.post(LifePayHelper.serverUrl+"plat/getRecords",map);
	}

	@Override
	public String payNotify(Map map) throws Exception {
		String tradeNo = map.get("tradeNo").toString();
		String notifyUrl = map.get("notifyUrl").toString();
		logger.info("tradeNo {} 支付成功回调处理开始......", tradeNo);
		String timePaid = map.get("timePaid").toString();
		//商户订单号唯一性校验
		OrderForm orderFormQuery = new OrderForm();
		orderFormQuery.setAppId(OrderFormHelper.CSP_APP_ID);
		orderFormQuery.setSourceOrderNumber(tradeNo);
		orderFormQuery = orderFormService.findBySourceOrderNumber(orderFormQuery);
		if (null != orderFormQuery) {
			List<OrderFormLine> orderFormLineList=orderFormLineMapper.findByOrderId(orderFormQuery.getId());
			OrderFormLine orderFormLine=orderFormLineList.get(0);
			LifePayBill lifePayBill=lifePayBillMapper.selectByPrimaryKey(orderFormLine.getReceivableId());

			Map paramsMap=new HashMap();
			paramsMap.put("clientAbbr",LifePayHelper.clientAbbr);
			paramsMap.put("clientOrderNo",orderFormQuery.getOrderNumber());
			paramsMap.put("notifyUrl",notifyUrl);
			paramsMap.put("billId",lifePayBill.getBillId());
			paramsMap.put("billOrgId",lifePayBill.getBillOrgId());
			paramsMap.put("barCode", lifePayBill.getBarcode()==null?"":lifePayBill.getBarcode());
			paramsMap.put("doorCode", lifePayBill.getDoorCode()==null?"":lifePayBill.getDoorCode());
			if("waterRate".equals(lifePayBill.getProductType())){
				paramsMap.put("platType","1");
			}else if("powerRate".equals(lifePayBill.getProductType())){
				paramsMap.put("platType","2");
			}else if("gasBill".equals(lifePayBill.getProductType())){
				paramsMap.put("platType","3");
			}
			String sign=LifePayHelper.encodeByRSA(paramsMap);
			paramsMap.put("Sign",sign);
			//通知快币系统该账单已支付
			notifyToMerchant(lifePayBill, paramsMap);
			//更新订单业务状态
			orderFormQuery.setOrderStatus(lifePayBill.getBillStatus());
			orderFormService.save(orderFormQuery);
		}
		logger.info("tradeNo {} 支付成功回调处理结束......",tradeNo);
		return null;
	}

	/**
	 * 通知快币系统该账单已支付
	 * @param lifePayBill
	 * @param paramsMap
	 * @throws Exception
	 */
	private void notifyToMerchant(LifePayBill lifePayBill, Map paramsMap) throws Exception {
		String url= LifePayHelper.serverUrl+"plat/createBillOrder";
		String result= HttpUtil.post(url,paramsMap);
		String thirdOrderNo;
		JSONObject jsonObject;
		if(!StringUtils.isEmpty(result)){
			jsonObject=JSONObject.parseObject(result);
			String code=jsonObject.get("code").toString();
			//尝试3次
			if("1004".equals(code)&&lifePayBill.getRecursiveNum()<3){
				lifePayBill.setRecursiveNum(lifePayBill.getRecursiveNum()+1);
				Thread.sleep(lifePayBill.getRecursiveNum()*5000);
				notifyToMerchant(lifePayBill,paramsMap);
			}else if("554008".equals(code)||"554009".equals(code)||"554999".equals(code)){
				//这三种状态不抛出异常，查询快币系统订单状态做记录
			}else if(!LifePayHelper.SUCCESS_CODE.equals(code)){
				throw new DataWarnningException(LifePayHelper.errorMap.get(code).toString());
			}

			if(null!=jsonObject&&jsonObject.containsKey("data")) {
				JSONObject data = JSONObject.parseObject(jsonObject.get("data").toString());
				thirdOrderNo = data.get("thirdOrderNo") == null ? "" : data.get("thirdOrderNo").toString();
				lifePayBill.setThirdOrderNo(thirdOrderNo);
			}

			//查询快币系统账单处理状态
			result=getRecords(paramsMap.get("clientOrderNo").toString(),null);
			jsonObject=JSONObject.parseObject(result);
			if(null!=jsonObject&&jsonObject.containsKey("data")) {
				JSONObject data = JSONObject.parseObject(jsonObject.get("data").toString());
				String orderStatus = data.get("orderStatus") == null ? "" : data.get("orderStatus").toString();
				lifePayBill.setBillStatus(orderStatus);
			}
			lifePayBillMapper.updateByPrimaryKeySelective(lifePayBill);
		}else{
			throw new ServiceException(RestApiExceptionEnums.UNKNOW_EXCEPTION);
		}
	}

	@Override
	public String orderStatusNotify(String clientOrderNo, String thirdOrderNo, String orderStatus)throws Exception {
		//1、更新订单头状态
		OrderForm orderForm= orderFormService.findByOrderNumber(clientOrderNo);
		orderForm.setOrderStatus(orderStatus);
		orderFormService.save(orderForm);
		//2、更新账单行状态
		List<OrderFormLine> orderFormLineList=orderFormLineMapper.findByOrderId(orderForm.getId());
		for(OrderFormLine orderFormLine:orderFormLineList){
			LifePayBill lifePayBill=lifePayBillMapper.selectByPrimaryKey(orderFormLine.getReceivableId());
			lifePayBill.setOrderNotifyTime(new Date());
			lifePayBill.setOrderNotifyStatus(orderStatus);
			lifePayBillMapper.updateByPrimaryKeySelective(lifePayBill);
		}
		return null;
	}

	@Override
	public List<OrderForm> getLifePaidList(String appUserId, Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);
		return orderFormService.getLifePaidList(appUserId);
	}
}

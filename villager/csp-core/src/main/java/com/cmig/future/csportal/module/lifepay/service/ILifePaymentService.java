package com.cmig.future.csportal.module.lifepay.service;

import com.cmig.future.csportal.module.lifepay.dto.BillResponse;
import com.cmig.future.csportal.module.lifepay.dto.LifePayBill;
import com.cmig.future.csportal.module.pay.order.dto.OrderForm;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 14:18 2017/11/21.
 * @Modified by zhangtao on 14:18 2017/11/21.
 */
public interface ILifePaymentService {

	/**
	 * 根据城市代码查询支持的产品信息
	 * @param cityCode
	 * @return
	 */
	String getLiftFeeInfo(String cityCode)throws Exception ;

	/**
	 * 产品机构列表查询交易
	 * @param cityCode
	 * @param productId
	 * @return
	 */
	String productQuery(String cityCode,String productId)throws Exception ;

	/**
	 * 机构支持账单查询方式应答参数说明
	 * @param billOrgId
	 * @param productId
	 * @return
	 */
	String billConfQuery(String billOrgId,String productId)throws Exception ;

	/**
	 * 查询账单
	 * @param billOrgId
	 * @param productId
	 * @param startMonth
	 * @param doorCode
	 * @param barcode
	 * @param searchType
	 * @return
	 */
	BillResponse queryBillNo(String billOrgId, String productId, String startMonth, String doorCode, String barcode, String searchType)throws Exception ;

	/**
	 * 创建订单
	 * @param request
	 * @param lifePayBill
	 * @return
	 * @throws Exception
	 */
	LifePayBill createBillOrder(HttpServletRequest request, LifePayBill lifePayBill)throws Exception ;

	/**
	 * 查询缴费记录
	 * @param clientOrderNo
	 * @param thirdOrderNo
	 * @return
	 */
	String getRecords(String clientOrderNo,String thirdOrderNo)throws Exception ;

	/**
	 * 支付成功通知
	 * @param map
	 * @return
	 * @throws Exception
	 */
	String payNotify(Map map)throws Exception ;

	/**
	 * 订单状态后台通知
	 * @param clientOrderNo
	 * @param thirdOrderNo
	 * @param orderStatus
	 * @return
	 */
	String orderStatusNotify(String clientOrderNo,String thirdOrderNo,String orderStatus)throws Exception ;

	/**
	 * 获取已支付的生活缴费记录
	 * @param appUserId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<OrderForm> getLifePaidList(String appUserId, Integer page, Integer pageSize);
}

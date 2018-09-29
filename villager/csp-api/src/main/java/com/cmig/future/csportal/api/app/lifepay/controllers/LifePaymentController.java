package com.cmig.future.csportal.api.app.lifepay.controllers;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.sign.CspSignUtil;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.lifepay.dto.BillResponse;
import com.cmig.future.csportal.module.lifepay.dto.LifePayBill;
import com.cmig.future.csportal.module.lifepay.helper.LifePayHelper;
import com.cmig.future.csportal.module.lifepay.service.ILifePaymentService;
import com.cmig.future.csportal.module.pay.conf.OrderFormHelper;
import com.cmig.future.csportal.module.pay.order.dto.OrderForm;
import com.cmig.future.csportal.module.pay.order.service.IOrderFormService;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import com.cmig.future.csportal.module.sys.utils.UserTokenUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 14:31 2017/11/21.
 * @Modified by zhangtao on 14:31 2017/11/21.
 */
@RestController
@RequestMapping(value = "${userPath}")
public class LifePaymentController extends BaseExtendController {

	@Autowired
	private ILifePaymentService lifePaymentService;

	@Autowired
	private IOrderFormService orderFormService;

    /**
     * 根据城市代码查询支持的产品信息
     * https://cs.keycoin.cn/plat/plat
     *
     * @param request
     * @return
     * @throws Exception
     */
	@CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/lifepay/getLiftFeeInfo", method = RequestMethod.POST)
    public RetApp getLiftFeeInfo(HttpServletRequest request) throws Exception {
        String cityCode = getParam(request, "cityCode", "");
        String responseStr = lifePaymentService.getLiftFeeInfo(cityCode);
		JSONObject data = new JSONObject();
        if(!StringUtils.isEmpty(responseStr)){
			JSONObject jsonObject = JSONObject.fromObject(responseStr);
			data= (JSONObject) jsonObject.get("data");
		}
        return RetAppUtil.success(data, "获取产品信息成功");
    }

    /**
     * 产品机构列表查询交易
     *
     * @param request
     * @return
     * @throws Exception
     */
	@CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/lifepay/productQuery", method = RequestMethod.POST)
    public RetApp productQuery(HttpServletRequest request) throws Exception {
        String cityCode = getParam(request, "cityCode", "");
        String productId = getParam(request, "productId", "");
        String responseStr = lifePaymentService.productQuery(cityCode, productId);
		JSONArray data = new JSONArray();
		if(!StringUtils.isEmpty(responseStr)){
			JSONObject jsonObject = JSONObject.fromObject(responseStr);
			data= (JSONArray) jsonObject.get("data");
		}
        return RetAppUtil.success(data, "获取产品机构列表成功");
    }

    /**
     * 机构支持账单查询方式应答参数说明
     *
     * @param request
     * @return
     * @throws Exception
     */
	@CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/lifepay/billConfQuery", method = RequestMethod.POST)
    public RetApp billConfQuery(HttpServletRequest request) throws Exception {
        String billOrgId = getParam(request, "billOrgId", "");
        String productId = getParam(request, "productId", "");
        String responseStr = lifePaymentService.billConfQuery(billOrgId, productId);
		JSONArray data = new JSONArray();
		if(!StringUtils.isEmpty(responseStr)){
			JSONObject jsonObject = JSONObject.fromObject(responseStr);
			data= (JSONArray) jsonObject.get("data");
		}
        return RetAppUtil.success(data, "获取机构支持账单查询方式成功");
    }

    /**
     * 查询账单
     *
     * @param request
     * @return
     * @throws Exception
     */
	@CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/st/lifepay/queryBillNo", method = RequestMethod.POST)
    public RetApp queryBillNo(HttpServletRequest request) throws Exception {
        String billOrgId = getParam(request, "billOrgId", "");
        String productId = getParam(request, "productId", "");
        String startMonth = getParam(request, "startMonth", "");
        String billNo = getParam(request, "billNo", "");
        String barcode = getParam(request, "barcode", "");
        String searchType = getParam(request, "searchType", "");
        BillResponse billResponse = lifePaymentService.queryBillNo(billOrgId, productId,startMonth,billNo,barcode,searchType);
        return RetAppUtil.success(billResponse.getData(), "获取账单成功");
    }

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/st/lifepay/createBillOrder", method = RequestMethod.POST)
	public RetApp createBillOrder(HttpServletRequest request, @ModelAttribute LifePayBill lifePayBill) throws Exception {
		lifePayBill.setBackUrl(Global.getProjectPath(request) + "/user/lifepay/payNotify");
		lifePayBill.setFrontUrl(Global.getProjectPath(request) + "/user/lifepay/payNotify");
		lifePayBill.setClientIp(getRemoteid(request));
		lifePayBill = lifePaymentService.createBillOrder(request, lifePayBill);
		OrderForm orderForm = lifePayBill.getOrderForm();
		Object object = orderFormService.getResultForSubmit(request, orderForm);
		return RetAppUtil.success(object, "提交成功");
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/st/lifepay/getRecords", method = RequestMethod.POST)
	public RetApp getRecords(HttpServletRequest request) throws Exception {
		String clientOrderNo = getParam(request, "clientOrderNo", "");
		String thirdOrderNo = getParam(request, "thirdOrderNo", "");
		String responseStr = lifePaymentService.getRecords(clientOrderNo,thirdOrderNo);
		JSONObject data = new JSONObject();
		if(!StringUtils.isEmpty(responseStr)){
			JSONObject jsonObject = JSONObject.fromObject(responseStr);
			data= (JSONObject) jsonObject.get("data");
		}
		return RetAppUtil.success(data, "获取机构支持账单查询方式成功");
	}

	/**
	 * 处理支付成功通知
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/lifepay/payNotify", method = RequestMethod.POST)
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
				logger.info("商户接收支付通知成功 {}", tradeNo);
				map.put("notifyUrl",Global.getProjectPath(request) + "/user/lifepay/orderStatusNotify");
				lifePaymentService.payNotify(map);
				out.print("success");
			} else {
				logger.info("商户接收支付通知失败 {}", tradeNo);
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


	/**
	 * 订单状态后台通知
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/lifepay/orderStatusNotify",method = RequestMethod.POST)
	public RetApp orderStatusNotify(HttpServletRequest request)throws Exception {
		String clientAbbr=getParam(request,"clientAbbr","");
		String clientOrderNo=getParam(request,"clientOrderNo","");
		String thirdOrderNo=getParam(request,"thirdOrderNo","");
		String orderStatus=getParam(request,"orderStatus","");
		String Sign=getParam(request,"Sign","");
		logger.info("订单状态后台通知处理开始 clientOrderNo {} thirdOrderNo{}  orderStatus {} ",clientOrderNo,thirdOrderNo,orderStatus);
		//签名
		Map map = new HashMap();
		map.put("clientAbbr", clientAbbr);
		map.put("clientOrderNo", clientOrderNo);
		map.put("thirdOrderNo",thirdOrderNo);
		map.put("orderStatus",orderStatus);
		if(LifePayHelper.verify(map,Sign)) {
			lifePaymentService.orderStatusNotify(clientOrderNo,thirdOrderNo,orderStatus);
		}else{
			logger.info("订单状态后台通知处理失败 clientOrderNo {} thirdOrderNo{} orderStatus {} ",clientOrderNo,thirdOrderNo,orderStatus);
			return RetAppUtil.error(new ServiceException(RestApiExceptionEnums.SIGN_ERROR));
		}
		return RetAppUtil.success("订单状态后台通知处理成功");
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/st/lifepay/getPaidList", method = RequestMethod.POST)
	public RetApp getPaidList(HttpServletRequest request,
	                          @RequestParam(defaultValue = DEFAULT_PAGE) Integer page,
	                          @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) throws Exception {
		String token=getParam(request,"token","");
		String appUserId= UserTokenUtils.getUserIdByToken(token);
		List<OrderForm> list= lifePaymentService.getLifePaidList(appUserId,page,pageSize);
		return RetAppUtil.success(list,"");
	}
}

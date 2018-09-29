package com.cmig.future.csportal.api.app.properties.payment.controllers;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.sign.CspSignUtil;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.pay.conf.OrderFormHelper;
import com.cmig.future.csportal.module.pay.order.dto.AppOrderVo;
import com.cmig.future.csportal.module.pay.order.dto.OrderForm;
import com.cmig.future.csportal.module.pay.order.service.IOrderFormService;
import com.cmig.future.csportal.module.properties.base.customer.dto.BpHouseMap;
import com.cmig.future.csportal.module.properties.base.customer.service.IBpHouseMapService;
import com.cmig.future.csportal.module.properties.payment.receivable.dto.MgtReceivableDetail;
import com.cmig.future.csportal.module.properties.payment.receivable.service.IMgtReceivableDetailService;
import com.cmig.future.csportal.module.properties.payment.receivablecopy.dto.MgtReceivableCopy;
import com.cmig.future.csportal.module.properties.payment.receivablecopy.service.IMgtReceivableCopyService;
import com.cmig.future.csportal.module.sys.code.CodeUtil;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import com.cmig.future.csportal.module.sys.utils.UserTokenUtils;
import com.github.pagehelper.Page;
import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.dto.ResponseData;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "${userPath}")
public class MgtPayController extends BaseExtendController {

    @Autowired
    private IBpHouseMapService bpHouseMapService;
    @Autowired
    private IMgtReceivableDetailService mgtReceivableDetailService;

	@Autowired
	private IOrderFormService orderFormService;

	@Autowired
	private IMgtReceivableCopyService mgtReceivableCopyService;

    /**
     * 我的房屋
     * @param request
     * @param response
     * @return
     */
	@CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/st/pay/myHouse", produces = {"application/json" }, method = {RequestMethod.POST,RequestMethod.GET,RequestMethod.OPTIONS})
    @ResponseBody
    public RetApp queryMyHouse(HttpServletRequest request, HttpServletResponse response,MgtReceivableDetail detail){
        RetApp retApp = new RetApp();
        List<JSONObject> objList = new ArrayList<JSONObject>();
        //根据token获取userId
            List<BpHouseMap> list = bpHouseMapService.queryByUserId(detail);
            if(!list.isEmpty()){
                for(BpHouseMap map : list){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("buildingId", map.getHouseId());
                    jsonObject.put("houseCode", map.getHouseCode());
                    jsonObject.put("houseFullName", map.getHouseFullName());
                    jsonObject.put("paymentArea", map.getPaymentArea());
                    jsonObject.put("buildingArea", map.getBuildingArea());
                    String ownerName = bpHouseMapService.queryOwnerName(map.getHouseId());
                    jsonObject.put("ownerName", ownerName);
                    jsonObject.put("bpType", map.getBpType());
                    objList.add(jsonObject);
                }
            }
        
        retApp.setData(objList);
        retApp.setStatus(OK);
        retApp.setTotall((long)objList.size());
        retApp.setMessage("查询成功!");
        return retApp;
    }
    
    /**
     * 查询缴费（待缴/预缴）单信息
     * @param request
     * @param response
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/st/pay/queryBills", produces = {"application/json" }, method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public RetApp queryBills(HttpServletRequest request, HttpServletResponse response, MgtReceivableDetail detail,@RequestParam(defaultValue = DEFAULT_PAGE) Integer page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize){
        RetApp retApp = new RetApp();
        JSONObject objList = new JSONObject();
        List<JSONObject> types = new ArrayList<JSONObject>();
        List<JSONObject> details = new ArrayList<JSONObject>();
        Long buildingId = detail.getBuildingId();
        if(buildingId==null || "".equals(buildingId)){
            throw new DataWarnningException("房屋不能为空");
        }


	    if(!StringUtils.isEmpty(detail.getPeriodName())&&detail.getPeriodName().indexOf("-")<0){
		    detail.setPeriodName(detail.getPeriodName()+"-12");
	    }
        String expenditure = detail.getExpenditure();
        if(expenditure == null || StringUtils.isEmpty(expenditure)){
            List<CodeValue> codeValueList= CodeUtil.getDictList("MGT.EXPENDITURE_TYPE","");
            if (!StringUtils.isEmpty(codeValueList)) {
	            for (CodeValue dto : codeValueList) {
		            JSONObject jsonObjectType = new JSONObject();
		            jsonObjectType.put("expenditure", dto.getValue());
		            jsonObjectType.put("expenditureDesc", dto.getMeaning());
		            types.add(jsonObjectType);
	            }
	            objList.put("expenditureList", types);
	            detail.setExpenditure(codeValueList.get(0).getValue());
	            List<MgtReceivableDetail> bills = bpHouseMapService.queryBills(detail, page, pageSize);
	            if(!bills.isEmpty()){
		            for(MgtReceivableDetail bill : bills){
			            JSONObject jsonObject = new JSONObject();
			            jsonObject.put("receivableId", bill.getReceivableId());
			            jsonObject.put("expenditure", bill.getExpenditure());
			            jsonObject.put("expenditureDesc", CodeUtil.getDictLabel(bill.getExpenditure(),"MGT.EXPENDITURE_TYPE","",""));
			            jsonObject.put("priceAmout", bill.getPriceAmout());
			            jsonObject.put("area", bill.getArea());
			            jsonObject.put("totalAmount", bill.getTotalAmount());
			            jsonObject.put("discountAmount", bill.getDiscountAmount());
			            jsonObject.put("breakContractAmount", bill.getBreakContractAmount());
			            jsonObject.put("receivableAmount", bill.getReceivableAmount());
			            jsonObject.put("sourceSystem", bill.getSourceSystem());
			            jsonObject.put("periodName", bill.getPeriodName());
			            details.add(jsonObject);
		            }
	            }
	            objList.put("detailList", details);

	            //待缴/应缴
	            Long uncalledFee = bpHouseMapService.queryUncalledFee(detail);
	            Long queryPayableFee = bpHouseMapService.queryPayableFee(detail);
	            MgtReceivableDetail queryPeriod = bpHouseMapService.queryPeriod(detail);
	            objList.put("uncalledFee", uncalledFee==null?"":uncalledFee);
	            objList.put("payableFee", queryPayableFee==null?"":queryPayableFee);
	            objList.put("periodStart", queryPeriod.getPeriodStart()==null?"":queryPeriod.getPeriodStart());
	            objList.put("periodEnd", queryPeriod.getPeriodEnd()==null?"":queryPeriod.getPeriodEnd());
            }
        }else{
            List<CodeValue> codeValueList= CodeUtil.getDictList("MGT.EXPENDITURE_TYPE","");
            if (!StringUtils.isEmpty(codeValueList)) {
	            for (CodeValue dto : codeValueList) {
		            JSONObject jsonObjectType = new JSONObject();
		            jsonObjectType.put("expenditure", dto.getValue());
		            jsonObjectType.put("expenditureDesc", dto.getMeaning());
		            types.add(jsonObjectType);
	            }
	            objList.put("expenditureList", types);
            }

            List<MgtReceivableDetail> bills2 = bpHouseMapService.queryBills(detail,page,pageSize);
            if(!bills2.isEmpty()){
                for(MgtReceivableDetail bill : bills2){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("receivableId", bill.getReceivableId());
                    jsonObject.put("expenditure", bill.getExpenditure());
                    jsonObject.put("expenditureDesc",CodeUtil.getDictLabel(bill.getExpenditure(),"MGT.EXPENDITURE_TYPE","",""));
                    jsonObject.put("priceAmout", bill.getPriceAmout());
                    jsonObject.put("area", bill.getArea());
                    jsonObject.put("totalAmount", bill.getTotalAmount());
                    jsonObject.put("discountAmount", bill.getDiscountAmount());
                    jsonObject.put("breakContractAmount", bill.getBreakContractAmount());
                    jsonObject.put("receivableAmount", bill.getReceivableAmount());
                    jsonObject.put("sourceSystem", bill.getSourceSystem());
                    jsonObject.put("periodName", bill.getPeriodName());
                    details.add(jsonObject);
                }
            }
	        objList.put("detailList", details);

	        //待缴/应缴
	        Long uncalledFee = bpHouseMapService.queryUncalledFee(detail);
	        Long queryPayableFee = bpHouseMapService.queryPayableFee(detail);
	        MgtReceivableDetail queryPeriod = bpHouseMapService.queryPeriod(detail);
	        objList.put("uncalledFee", uncalledFee==null?"":uncalledFee);
	        objList.put("payableFee", queryPayableFee==null?"":queryPayableFee);
	        objList.put("periodStart", queryPeriod.getPeriodStart()==null?"":queryPeriod.getPeriodStart());
	        objList.put("periodEnd", queryPeriod.getPeriodEnd()==null?"":queryPeriod.getPeriodEnd());
        }
        retApp.setData(objList);
        retApp.setStatus(OK);
        retApp.setTotall((long)objList.size());
        retApp.setMessage("查询成功!");
        return retApp;
    }

    /**
     * 单子费用明细
     * @param request
     * @param response
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/st/pay/orderDetail", produces = {"application/json" }, method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public RetApp queryMyHouse(HttpServletRequest request, HttpServletResponse response, OrderForm orderForm){
        RetApp retApp = new RetApp();
        OrderForm order = bpHouseMapService.getOrderDetail(orderForm);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderId", order.getId());
        jsonObject.put("chargeId", order.getChargeId());
        jsonObject.put("orderNumber", order.getOrderNumber());
        jsonObject.put("appUserId", order.getAppUserId());
        jsonObject.put("payChannel", order.getPayChannel());
        jsonObject.put("orderAmount", order.getOrderAmount());
        jsonObject.put("discountAmount", order.getDiscountAmount());
        jsonObject.put("integralAmount", order.getIntegralAmount());
        jsonObject.put("payableAmount", order.getPayableAmount());
        jsonObject.put("paidAmount", order.getPaidAmount());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        if(order.getTimePaid()!=null){
            jsonObject.put("timePaid", sdf.format(order.getTimePaid()));
        }else{
            jsonObject.put("timePaid", "");
        }
        if(order.getTimeSettle()!=null){
            jsonObject.put("timeSettle", sdf.format(order.getTimeSettle()));
        }else{
            jsonObject.put("timeSettle", "");
        }
        if(order.getOrderType()!=null){
            jsonObject.put("orderType", order.getOrderType());
        }else{
            jsonObject.put("orderType", "");
        }
        if(order.getChannelName()!=null){
            jsonObject.put("payChannelName", order.getChannelName());
        }else{
            jsonObject.put("payChannelName", "");
        }
        if(order.getUserName()!=null){
            jsonObject.put("userName", order.getUserName());
        }else{
            jsonObject.put("userName", "");
        }
        if(order.getHouseName()!=null){
            jsonObject.put("houseName", order.getHouseName());
        }else{
            jsonObject.put("houseName", "");
        }
        if(order.getCommunityName()!=null){
            jsonObject.put("communityName", order.getCommunityName());
        }else{
            jsonObject.put("communityName", "");
        }
        if(order.getTranseq()!=null){
            jsonObject.put("transeq", order.getTranseq());
        }else{
            jsonObject.put("transeq", "");
        }
        jsonObject.put("receiveSide", Global.getProductName());
        retApp.setData(jsonObject);
        retApp.setStatus(OK);
        retApp.setMessage("查询成功!");
        return retApp;
    }
    
    /**
     * 我的缴费
     * @param request
     * @param response
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/st/pay/myBill", produces = {"application/json" }, method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public RetApp queryMyBill(HttpServletRequest request, HttpServletResponse response, MgtReceivableDetail detail,@RequestParam(defaultValue = DEFAULT_PAGE) Integer page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize){
        RetApp retApp = new RetApp();
        List<JSONObject> objList = new ArrayList<JSONObject>();
        List<MgtReceivableDetail> list = bpHouseMapService.getMyBill(detail,page,pageSize);
        if(!list.isEmpty()){
            for(MgtReceivableDetail bill : list){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("receivableId", bill.getReceivableId());
                jsonObject.put("expenditure", bill.getExpenditure());
                jsonObject.put("expenditureDesc", CodeUtil.getDictLabel(bill.getExpenditure(),"MGT.EXPENDITURE_TYPE","",""));
                jsonObject.put("priceAmout", bill.getPriceAmout());
                jsonObject.put("area", bill.getArea());
                jsonObject.put("totalAmount", bill.getTotalAmount());
                jsonObject.put("discountAmount", bill.getDiscountAmount());
                jsonObject.put("breakContractAmount", bill.getBreakContractAmount());
                jsonObject.put("receivableAmount", bill.getReceivableAmount());
                jsonObject.put("sourceSystem", bill.getSourceSystem());
                jsonObject.put("periodName", bill.getPeriodName());
                jsonObject.put("buildingId", bill.getBuildingId());
                jsonObject.put("houseFullName", bill.getHouseFullName());
                objList.add(jsonObject);
            }
        }
        retApp.setData(objList);
        retApp.setStatus(OK);
        if (list != null&&list instanceof Page) {
            retApp.setTotall(Long.valueOf(((Page)list).getTotal()));
        }
        retApp.setMessage("查询成功!");
        return retApp;
    }
    
    /**
     * 我的缴费
     * @param request
     * @param response
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/st/pay/myAllBill", produces = {"application/json" }, method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public RetApp queryMyAllBill(HttpServletRequest request, HttpServletResponse response, MgtReceivableDetail detail,@RequestParam(defaultValue = DEFAULT_PAGE) Integer page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize){
        RetApp retApp = new RetApp();
        List<JSONObject> objList = new ArrayList<JSONObject>();
        List<OrderForm> list = bpHouseMapService.getMyAllBill(detail,page,pageSize);
        if(!list.isEmpty()){
            for(OrderForm order : list){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("orderId", order.getId());
                jsonObject.put("chargeId", order.getChargeId());
                jsonObject.put("orderNumber", order.getOrderNumber());
                jsonObject.put("appUserId", order.getAppUserId());
                jsonObject.put("payChannel", order.getPayChannel());
                jsonObject.put("orderAmount", order.getOrderAmount());
                jsonObject.put("discountAmount", order.getDiscountAmount());
                jsonObject.put("integralAmount", order.getIntegralAmount());
                jsonObject.put("payableAmount", order.getPayableAmount());
                jsonObject.put("paidAmount", order.getPaidAmount());
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
                if(order.getTimePaid()!=null){
                    jsonObject.put("timePaid", sdf.format(order.getTimePaid()));
                }else{
                    jsonObject.put("timePaid", "");
                }
                if(order.getTimeSettle()!=null){
                    jsonObject.put("timeSettle", sdf.format(order.getTimeSettle()));
                }else{
                    jsonObject.put("timeSettle", "");
                }
                jsonObject.put("orderType", CodeUtil.getDictLabel(order.getOrderType(),"CSP.PAY.ORDER_TYPE","",""));
                objList.add(jsonObject);
            }
        }
        retApp.setData(objList);
        retApp.setStatus(OK);
        if (list != null&&list instanceof Page) {
            retApp.setTotall(Long.valueOf(((Page)list).getTotal()));
        }
        retApp.setMessage("查询成功!");
        return retApp;
    }

    /**
     * 我的缴费
     * @param request
     * @param response
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/st/pay/communityBill", produces = {"application/json" }, method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public RetApp queryCommunityBill(HttpServletRequest request, HttpServletResponse response, String communityId, @RequestParam(required=true) String token){
        RetApp retApp = new RetApp();
        JSONObject jsonObject = new JSONObject();
        String userId= UserTokenUtils.getUserIdByToken(token);
        Long amount = bpHouseMapService.queryCommunityBill(communityId,userId);
        jsonObject.put("amount",amount);
        retApp.setData(jsonObject);
        retApp.setStatus(OK);
        retApp.setMessage("查询成功!");
        return retApp;
    }


	/**
	 * 缴费订单提交
	 * @param request
	 * @param response
	 * @return
	 */
    @CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/st/mgt/payment/submit", produces = {"application/json" }, method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public RetApp paymentSubmit(HttpServletRequest request, HttpServletResponse response, @RequestBody AppOrderVo appOrderVo) throws Exception {
		appOrderVo.setBackUrl(Global.getProjectPath(request)+"/user/mgt/payment/notify");
		appOrderVo.setFrontUrl(Global.getProjectPath(request)+"/user/mgt/payment/notify");
		appOrderVo.setToken(getParam(request,"token",""));
		appOrderVo.setClientIp(getRemoteid(request));
		appOrderVo=mgtReceivableDetailService.paymentSubmit(appOrderVo);
		OrderForm orderForm=appOrderVo.getOrderForm();
		Object object=orderFormService.getResultForSubmit(request, orderForm);
		return  RetAppUtil.success(object,"提交成功");
	}

	/**
	 * 接收通知接口
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/mgt/payment/notify", produces = {"application/json"}, method = RequestMethod.POST)
	public void refundNotifyDemo(HttpServletRequest request, HttpServletResponse response) {
		//必填项,需要验证
		String tradeNo = getParam(request, "tradeNo", "");
		String paidAmount = getParam(request, "paidAmount", "");
		String channel = getParam(request, "channel", "");
		String timePaid = getParam(request, "timePaid", "");
		String transeq = getParam(request, "transeq", "");
		String tradeStatus = getParam(request, "tradeStatus", "");
		String sign = getParam(request, "sign", "");
		String appid= OrderFormHelper.CSP_APP_ID;

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
				mgtReceivableDetailService.notifySuccess(map);
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

	@RequestMapping(value = "/mgt/receivable/copy/test/add", method = RequestMethod.GET)
	public ResponseData testAdd(HttpServletRequest request) {
		logger.debug("应收账单新增接口测试开始。。。。。。");

		String serverUrl = Global.getProjectPath(request);
		List<MgtReceivableCopy> list = mgtReceivableCopyService.queryAll();
		for (MgtReceivableCopy copy : list) {
			String expenditure = copy.getExpenditure();
			String periodName = copy.getPeriodName();
			Long totalAmount = copy.getTotalAmount();
			String backUrl = copy.getBackUrl();
			String buildingType = copy.getBuildingType();//	建筑实体类型

			//验证第三方签名
			OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(copy.getAppId());

			Map map = new HashMap<String,Object>();
			map.put("appid", copy.getAppId());
			map.put("sourceReceivableId", copy.getReceivableId().toString());
			map.put("sourceBuildCode", copy.getSourceBuildCode());
			map.put("expenditure", expenditure.toString());
			map.put("periodName", periodName.toString());
			map.put("totalAmount", totalAmount.toString());
			map.put("discountAmount", copy.getDiscountAmount()==0?"":copy.getDiscountAmount().toString());
			map.put("breakContractAmount",copy.getBreakContractAmount()==0?"":copy.getBreakContractAmount().toString());
			map.put("backUrl", backUrl.toString());
			map.put("description", copy.getDescription()==null?"":copy.getDescription());
			map.put("buildingType", buildingType.toString());
			String sign = CspSignUtil.generateSign(map, openAppInfo.getAppSecret());
			map.put("sign",sign);
			HttpUtil.post(serverUrl + "/common/receivable/add",map);
		}
		logger.debug("应收账单新增接口测试结束。。。。。。num {}",list.size() );
		return new ResponseData();
	}


	/**
	 * 第三方物业公司接收通知接口demo
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/mgt/payment/receivable/notifyDemo", produces = {"application/json"}, method = RequestMethod.POST)
	public void notifyDemo(HttpServletRequest request, HttpServletResponse response) {
		//必填项,需要验证
		String tradeNo = getParam(request, "tradeNo", "");
		String sourceReceivableId = getParam(request, "sourceReceivableId", "");
		String channel = getParam(request, "channel", "");
		String timePaid = getParam(request, "timePaid", "");
		String transeq = getParam(request, "transeq", "");
		String tradeStatus = getParam(request, "tradeStatus", "");
		String sign = getParam(request, "sign", "");
		String appid="934dfcc043f941cfa5587e6c11762bad";

		logger.info("第三方物业公司接收通知开始 {}",tradeNo);
		PrintWriter out=null ;
		try {
			out=response.getWriter();
			//验证第三方签名
			OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
			//签名
			Map map = new HashMap();
			map.put("tradeNo", tradeNo);
			map.put("sourceReceivableId", sourceReceivableId);
			map.put("channel",channel);
			map.put("timePaid",timePaid);
			map.put("transeq",transeq);
			map.put("tradeStatus", tradeStatus);

			boolean result = CspSignUtil.checkSign(map, openAppInfo.getAppSecret(),sign);
			if(result){
				logger.info("第三方物业公司接收通知成功 sourceReceivableId {} tradeNo{} ",sourceReceivableId,tradeNo);
				mgtReceivableCopyService.notifySuccess(map);
				out.print("success");
			}else{
				logger.info("第三方物业公司接收通知失败 sourceReceivableId {} tradeNo{} ",sourceReceivableId,tradeNo);
				out.print("fail");
			}

		}catch (Exception e) {
			e.printStackTrace();
			out.print("fail");
		}finally {
			if(null!=out){
				out.close();
			}
			logger.info("第三方物业公司接收通知结束 sourceReceivableId {} tradeNo{} ",sourceReceivableId,tradeNo);
		}
	}

}
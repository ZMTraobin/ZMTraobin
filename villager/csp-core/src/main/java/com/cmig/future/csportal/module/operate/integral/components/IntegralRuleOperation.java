package com.cmig.future.csportal.module.operate.integral.components;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.common.utils.MD5;
import com.cmig.future.csportal.common.utils.RegExpValidatorUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.integral.dto.IntegralFlowing;
import com.cmig.future.csportal.module.integral.service.IIntegralFlowingService;
import com.cmig.future.csportal.module.operate.integral.IntegralRuleSign;
import com.cmig.future.csportal.module.operate.integral.IntegralUrl;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zpf 2017/6/1.
 */
@Component
public class IntegralRuleOperation {

	private static Logger logger= LoggerFactory.getLogger(IntegralRuleOperation.class);
	@Autowired
    private IIntegralFlowingService integralFlowingService;
    @Autowired
    private IAppUserService appUserService;

    //app_id
    private static String app_id = Global.getConfig("INTEGRAL.APP_ID");
    //service_id
    private static String service_id = Global.getConfig("INTEGRAL_SERVER_ID");
	//成功状态码
	public static String SUCCESS_CODE="0";
	//用户每日特定场景下积分抵扣剩余额度无上限
	public static String DAILY_DEDUCTION_BALANCE_NO_LIMIT="noLimit";
	//人民币比积分比例
	public static int RMB_TO_INTEGRAL=100;
	//第三方商户接入积分消耗场景代码
	public static String INTEGRAL_TYPE_CSP_SHOP="R0003";
    //积分返回消息
    public static String SUCCESS_MESSAGE="重发成功";

    //增加积分
    public JSONObject addIntegral(Map<String,String > map) throws Exception {
        Document document;
        JSONObject object = new JSONObject();
        StringBuilder builder = new StringBuilder();
        IntegralFlowing integralFlowing = new IntegralFlowing();
        //签名map
        Map<String,String>signMapParam = new HashMap<String,String>();
        //用户id
        String uid = map.get("uid");
        //消费金额
        String purchase_amount = map.get("purchase_amount");
        //场景类型
        String type = map.get("type");
        //订单编号
        String out_trade_no = map.get("out_trade_no");
        //描述
        String description = map.get("description");
        //附加数据
        String attach = map.get("attach");
        //时间戳
        String timestamp =  String.valueOf(System.currentTimeMillis());
        //消息发布字数
        String msg_length = map.get("msg_length");
        //消息图片数量
        String pic_num = map.get("pic_num");
        //是否是首评
        String first_comment = map.get("first_comment");
        //生成订单编号
        String  auto_out_trade_no = UUID.randomUUID().toString().replaceAll("-","");
        AppUser appUser = appUserService.getBySourceSystemId(uid);
        String phone = appUser.getMobile();
        if(StringUtils.isEmpty(auto_out_trade_no)){
            throw new DataWarnningException("订单编号不能为空");
        }else{
            signMapParam.put("out_trade_no",auto_out_trade_no);
        }
        if(StringUtils.isEmpty(uid)){
            throw  new DataWarnningException("用户id不能为空");
        }else{
           signMapParam.put("uid",uid);
        }
        if(StringUtils.isEmpty(phone)){
            throw  new DataWarnningException("手机不能为空");
        }else{
            signMapParam.put("phone",phone);
        }
        if (StringUtils.isEmpty(type)) {
            throw new DataWarnningException("业务场景编号不能为空");
        }else{
            signMapParam.put("type",type);
        }
        if (StringUtils.isEmpty(timestamp)) {
            throw new DataWarnningException("时间戳不能为空");
        }else{
            signMapParam.put("timestamp",timestamp);
        }
        if(!StringUtils.isEmpty(description)){
            signMapParam.put("description",description);
        }
        if(!StringUtils.isEmpty(purchase_amount)){
            if(!RegExpValidatorUtils.isNum(purchase_amount)){
                throw new DataWarnningException("请输入正确的金额");
            }else{
               /* String purchaseamount = String.valueOf(Double.parseDouble(purchase_amount) * 100);
                purchase_amount = purchaseamount.substring(0, purchaseamount.indexOf("."));*/
                signMapParam.put("purchase_amount",purchase_amount);
                integralFlowing.setPurchaseAmount(Long.parseLong(purchase_amount));
            }
        }
        if(!StringUtils.isEmpty(attach)){
            signMapParam.put("attach",attach);
        }
        if(!StringUtils.isEmpty(msg_length)){
            signMapParam.put("msg_length",msg_length);
        }
        if(!StringUtils.isEmpty(pic_num)){
            if(!RegExpValidatorUtils.IsIntNumber(pic_num)){
                throw new DataWarnningException("请输入正确的图片数量");
            }else{
                signMapParam.put("pic_num",pic_num);
            }
        }
        if(!StringUtils.isEmpty(first_comment)){
            if(!RegExpValidatorUtils.isBoolean(first_comment)){
                throw new DataWarnningException("请输入合法的值");
            }else{
                signMapParam.put("first_comment",first_comment);
            }
        }
        signMapParam.put("app_id",app_id);
        signMapParam.put("service_id",service_id);
        //获取签名
        String integralparam = IntegralRuleSign.formatMap(signMapParam,false,true);
        String strsign = integralparam+"&key="+ Global.getConfig("INTEGRAL.KEY");
        String sign = MD5.MD5Encode(strsign).toUpperCase();
        builder.append("<xml>");
        builder.append("<app_id>").append(app_id).append("</app_id>");
        builder.append("<service_id>").append(service_id).append("</service_id>");
        builder.append("<uid>").append(uid).append("</uid>");
        builder.append("<phone>").append(phone).append("</phone>");
        if(!StringUtils.isEmpty(purchase_amount)){
            builder.append("<purchase_amount>").append(purchase_amount).append("</purchase_amount>");
        }
        builder.append("<type>").append(type).append("</type>");
        builder.append("<out_trade_no>").append(auto_out_trade_no).append("</out_trade_no>");
        if(!StringUtils.isEmpty(description)){
            builder.append("<description>").append(description).append("</description>");
        }
        if(!StringUtils.isEmpty(attach)){
            builder.append("<attach>").append(attach).append("</attach>");
        }
        if(!StringUtils.isEmpty(msg_length)){
            builder.append("<msg_length>").append(Integer.parseInt(msg_length)).append("</msg_length>");
        }
        if(!StringUtils.isEmpty(pic_num)){
            builder.append("<pic_num>").append(Integer.parseInt(pic_num)).append("</pic_num>");
        }
        if(!StringUtils.isEmpty(first_comment)){
            builder.append("<first_comment>").append(Boolean.valueOf(first_comment)).append("</first_comment>");
        }
        builder.append("<timestamp>").append(timestamp).append("</timestamp>");
        builder.append("<sign>").append(sign).append("</sign>").append("</xml>");
	    logger.debug(builder.toString());
        integralFlowing.setAppId(app_id);
        integralFlowing.setStatus("");
        integralFlowing.setIntegralType("ADD");
        integralFlowing.setPhone(phone);
        integralFlowing.setDescription(description);
        integralFlowing.setOutTradeNo(out_trade_no);
        integralFlowing.setAutoTradeNo(auto_out_trade_no);
        integralFlowing.setServiceId(service_id);
        integralFlowing.setIntegralUrl(IntegralUrl.ADD_INTEGRAL);
        integralFlowing.setUid(uid);
        integralFlowing.setTimestamps(timestamp);
        integralFlowing.setType(type);
        integralFlowing.setAttach(attach);
        integralFlowing.setSign(sign);
        integralFlowing.setIntegralParame(builder.toString());
        integralFlowingService.save(integralFlowing);
        String resultXML =  HttpUtil.post(IntegralUrl.ADD_INTEGRAL,builder.toString(),IntegralUrl.headerMap);
        document = DocumentHelper.parseText(resultXML);
        //解析xml
        //获取根节点
        Element root = document.getRootElement();
        String result  = root.element("return_msg").getText();
        String returnCode  = root.element("return_code").getText();
        //积分余额
        if(SUCCESS_CODE.equals(returnCode)){
            String balance  = root.element("balance").getText();
            //增加了多少积分
            String credits = root.element("credits").getText();
            object.put("credits",credits);
            object.put("balance",balance);
        }
        //根据uid和sign查询积分流水信息
        IntegralFlowing inteFlowing = integralFlowingService.selectIntegralByUidAndSign(integralFlowing);
        if(inteFlowing!=null){
            inteFlowing.setStatus(returnCode);
            integralFlowingService.updateIntegralFlowingStatus(inteFlowing);
        }
        object.put("returnCode",returnCode);
        object.put("message",result);

        return object;
    }
    //扣除积分
    public JSONObject deduction(Map<String,String > map) throws Exception {
        Document document;
        JSONObject object = new JSONObject();
        IntegralFlowing integralFlowing = new IntegralFlowing();
        StringBuilder builder = new StringBuilder();
        //签名map
        Map<String,String>signMapParam = new HashMap<String,String>();
        //用户id
        String uid = map.get("uid");
        //场景类型
        String type = map.get("type");
        //订单编号
        String out_trade_no = map.get("out_trade_no");
        //扣除的积分数量
        String credits = map.get("credits");
        //描述
        String description = map.get("description");
        //附加数据
        String attach = map.get("attach");
        //时间戳
	    String timestamp =  String.valueOf(System.currentTimeMillis());
        AppUser appUser = appUserService.getBySourceSystemId(uid);
       //用户手机
        String phone = appUser.getMobile();
        if(StringUtils.isEmpty(uid)){
            throw  new DataWarnningException("用户id不能为空");
        }else{
            signMapParam.put("uid",uid);
        }
        if (StringUtils.isEmpty(type)) {
            throw new DataWarnningException("业务场景编号不能为空");
        }else{
            signMapParam.put("type",type);
        }
        if (StringUtils.isEmpty(out_trade_no)) {
            throw new DataWarnningException("订单编号不能为空");
        }else{
            signMapParam.put("out_trade_no",out_trade_no);
        }
        if(StringUtils.isEmpty(credits)){
            throw new DataWarnningException("扣除积分数量不能为空");
        }else if(!RegExpValidatorUtils.IsIntNumber(credits)){
            throw new DataWarnningException("请输入正确的积分数");
        }else{
            signMapParam.put("credits",credits);
        }
        if(StringUtils.isEmpty(timestamp)){
            throw new DataWarnningException("时间戳不能为空");
        }else{
           signMapParam.put("timestamp",timestamp);
        }
        if(!StringUtils.isEmpty(description)){
            signMapParam.put("description",description);
        }
        if(!StringUtils.isEmpty(attach)){
            signMapParam.put("attach",attach);
        }
        signMapParam.put("app_id",app_id);
        signMapParam.put("service_id",service_id);
        //生成订单编号
        String autoTrandeNo = UUID.randomUUID().toString().replaceAll("-","");
        //获取签名
        String integralparam = IntegralRuleSign.formatMap(signMapParam,false,true);
	    logger.debug(integralparam+"url参数");
        String strsign = integralparam+"&key="+Global.getConfig("INTEGRAL.KEY");
        String sign = MD5.MD5Encode(strsign).toUpperCase();
        builder.append("<xml>");
        builder.append("<app_id>").append(app_id).append("</app_id>");
        builder.append("<service_id>").append(service_id).append("</service_id>");
        builder.append("<uid>").append(uid).append("</uid>");
        builder.append("<credits>").append(credits).append("</credits>");
        builder.append("<type>").append(type).append("</type>");
        builder.append("<out_trade_no>").append(out_trade_no).append("</out_trade_no>");
        if(!StringUtils.isEmpty(description)){
            builder.append("<description>").append(description).append("</description>");
        }
        if(!StringUtils.isEmpty(attach)){
            builder.append("<attach>").append(attach).append("</attach>");
        }
        builder.append("<timestamp>").append(timestamp).append("</timestamp>");
        builder.append("<sign>").append(sign).append("</sign>").append("</xml>");
        logger.debug(builder.toString());
        integralFlowing.setAppId(app_id);
        integralFlowing.setDescription(description);
        integralFlowing.setServiceId(service_id);
        integralFlowing.setStatus("");
        integralFlowing.setUid(uid);
        integralFlowing.setOutTradeNo(out_trade_no);
        integralFlowing.setAutoTradeNo(autoTrandeNo);
        integralFlowing.setIntegralType("DEDUCTION");
        integralFlowing.setPhone(phone);
        integralFlowing.setIntegralUrl(IntegralUrl.DEDUCTION_INTEGRAL);
        integralFlowing.setIntegralParame(builder.toString());
        integralFlowing.setTimestamps(timestamp);
        integralFlowing.setType(type);
        integralFlowing.setAttach(attach);
        integralFlowing.setCredits(Long.parseLong(credits));
        integralFlowing.setSign(sign);
        integralFlowingService.save(integralFlowing);
        String resultXML =  HttpUtil.post(IntegralUrl.DEDUCTION_INTEGRAL,builder.toString(),IntegralUrl.headerMap);
        document = DocumentHelper.parseText(resultXML);
        //解析xml
        //获取根节点
        Element root = document.getRootElement();
        String result  = root.element("return_msg").getText();
        String returnCode = root.element("return_code").getText();
        //积分余额
        if(SUCCESS_CODE.equals(returnCode)){
            String balance  = root.element("balance").getText();
            object.put("balance",balance);
	        String trade_no=root.element("trade_no").getText();
	        object.put("trade_no",trade_no);
            //扣除了多少积分
            String deduction_credits = root.element("credits").getText();
            object.put("credits",deduction_credits);
        }
        object.put("returnCode",returnCode);
        object.put("message",result);
        //根据uid和sign查询积分流水信息
        IntegralFlowing inteFlowing = integralFlowingService.selectIntegralByUidAndSign(integralFlowing);
        if(inteFlowing!=null){
            inteFlowing.setStatus(returnCode);
            integralFlowingService.updateIntegralFlowingStatus(inteFlowing);
        }
        return object;
    }
    /**
     * 积分查询
     * @param map
     * @return
     * @throws DocumentException
     */
    public JSONObject integralSelect(Map<String,String > map) throws Exception {
        Document document;
        JSONObject object = new JSONObject();
        StringBuilder builder = new StringBuilder();
        //签名map
        Map<String,String>signMapParam = new HashMap<String,String>();
	    //时间戳
	    String timestamp =  String.valueOf(System.currentTimeMillis());
        //用户id
        String uid = map.get("uid");
        if(StringUtils.isEmpty(uid)){
            throw  new DataWarnningException("用户id不能为空");
        }else{
            signMapParam.put("uid",uid);
        }
        if(StringUtils.isEmpty(timestamp)){
            throw  new DataWarnningException("时间戳不能为空");
        }else{
            signMapParam.put("timestamp",timestamp);
        }
        signMapParam.put("app_id",app_id);
        signMapParam.put("service_id",service_id);
        //获取签名
        String integralparam = IntegralRuleSign.formatMap(signMapParam,false,true);
        String strsign = integralparam+"&key="+Global.getConfig("INTEGRAL.KEY");
        String sign = MD5.MD5Encode(strsign).toUpperCase();

        builder.append("<xml>");
        builder.append("<app_id>").append(app_id).append("</app_id>");
        builder.append("<service_id>").append(service_id).append("</service_id>");
        builder.append("<uid>").append(uid).append("</uid>");
        builder.append("<timestamp>").append(timestamp).append("</timestamp>");
        builder.append("<sign>").append(sign).append("</sign>").append("</xml>");
        String resultXML =  HttpUtil.post(IntegralUrl.QUERY_BLANCE,builder.toString(),IntegralUrl.headerMap);
        document = DocumentHelper.parseText(resultXML);
        /**
         * 解析xml，获取根节点
         */
        Element root = document.getRootElement();
        String result  = root.element("return_msg").getText();
        String returnCode  = root.element("return_code").getText();
        //积分余额
        if(SUCCESS_CODE.equals(returnCode)){
            String balance  = root.element("balance").getText();
            //用户被冻结积分总额
            String freezeAmount = root.element("freezeAmount").getText();
            object.put("freezeAmount",freezeAmount);//冻结金额
            object.put("balance",balance);//余额
	        object.put("usefulBalance",new Long(balance)-new Long(freezeAmount));//可用余额=余额-冻结金额
        }
        object.put("returnCode",returnCode);
        object.put("message",result);
        return object;
    }

    /**
     *调用积分登陆接口
     * @return
     */
    public JSONObject  integralLogin(Map<String,String>map) throws Exception {
        Document document;
        JSONObject jsonObject = new JSONObject();
        StringBuilder builder = new StringBuilder();
        //签名map
        Map<String,String>signMapParam = new HashMap<String,String>();
        //用户id
        String uid = map.get("uid");
        //用户手机
        AppUser appUser = appUserService.getBySourceSystemId(uid);
        String phone = appUser.getMobile();
        //指定返回界面
        String redirect = map.get("redirect");
	    //时间戳
	    String timestamp =  String.valueOf(System.currentTimeMillis());

        if(StringUtils.isEmpty(uid)){
            throw new DataWarnningException("uid不能为空");
        }else{
            signMapParam.put("uid",uid);
        }
        if(StringUtils.isEmpty(phone)){
            throw new DataWarnningException("phone不能为空");
        }else{
            signMapParam.put("phone",phone);
        }
        if(StringUtils.isEmpty(timestamp)){
            throw new DataWarnningException("时间戳不能为空");
        }else{
            signMapParam.put("timestamp",timestamp);
        }
        if(!StringUtils.isEmpty(redirect)){
            signMapParam.put("redirect",redirect);
        }
        signMapParam.put("app_id",app_id);
        String integralparam = IntegralRuleSign.formatMap(signMapParam,false,true);
        String strsign = integralparam+"&key="+Global.getConfig("INTEGRAL.KEY");
        String sign = MD5.MD5Encode(strsign).toUpperCase();
        builder.append("<xml>");
        builder.append("<app_id>").append(app_id).append("</app_id>");
        builder.append("<uid>").append(uid).append("</uid>");
        builder.append("<phone>").append(phone).append("</phone>");
        if(redirect!=null && !StringUtils.isEmpty(redirect)){
            builder.append("<redirect>").append(redirect).append("</redirect>");
        }
        builder.append("<timestamp>").append(timestamp).append("</timestamp>");
        builder.append("<sign>").append(sign).append("</sign>").append("</xml>");
        logger.debug("xml参数{}"+builder.toString());
        String resultXML =  HttpUtil.post(IntegralUrl.AUTO_LOGIN,builder.toString(),IntegralUrl.headerMap);
        logger.debug("返回的结果{}"+resultXML);
        document = DocumentHelper.parseText(resultXML);
        Element root = document.getRootElement();
        String result  = root.element("return_msg").getText();
        String returnCode  = root.element("return_code").getText();
        if(SUCCESS_CODE.equals(returnCode)){
            String url = root.element("url").getText();
            jsonObject.put("url",url);
        }
        jsonObject.put("message",result);
        jsonObject.put("returnCode",returnCode);
        return jsonObject;
    }

    /**
     * 查询每日特定场景下可抵扣的积分查询
     * @param map
     * @return
     */
    public JSONObject selectIntegralByType(Map<String,String>map) throws Exception {
        JSONObject jsonObject = new JSONObject();
        StringBuilder builder = new StringBuilder();
        //签名map
        Map<String,String>signMapParam = new HashMap<String,String>();
        //用户id
        String uid = map.get("uid");
	    String timestamp =  String.valueOf(System.currentTimeMillis());
        String type = map.get("type");
        if(StringUtils.isEmpty(uid)){
            throw  new DataWarnningException("用户id不能为空");
        }else{
            signMapParam.put("uid",uid);
        }
        if(StringUtils.isEmpty(timestamp)){
            throw  new DataWarnningException("时间戳不能为空");
        }else{
            signMapParam.put("timestamp",timestamp);
        }
        if(StringUtils.isEmpty(type)){
            throw new DataWarnningException("场景类型不能为空");
        }else{
            signMapParam.put("type",type);
        }
        signMapParam.put("app_id",app_id);
        signMapParam.put("service_id",service_id);
        //生成签名
        String integralparam = IntegralRuleSign.formatMap(signMapParam,false,true);
        String strsign = integralparam+"&key="+Global.getConfig("INTEGRAL.KEY");
        String sign = MD5.MD5Encode(strsign).toUpperCase();
        //拼接xml
        builder.append("<xml>");
        builder.append("<app_id>").append(app_id).append("</app_id>");
        builder.append("<service_id>").append(service_id).append("</service_id>");
        builder.append("<uid>").append(uid).append("</uid>");
        builder.append("<type>").append(type).append("</type>");
        builder.append("<timestamp>").append(timestamp).append("</timestamp>");
        builder.append("<sign>").append(sign).append("</sign>").append("</xml>");
        String resultXML = HttpUtil.post(IntegralUrl.DAILY_DEDUCTION_BALANCE,builder.toString(),IntegralUrl.headerMap);
        Document document = DocumentHelper.parseText(resultXML);
        Element root = document.getRootElement();
        String result  = root.element("return_msg").getText();
	    logger.debug("返回的xml {} ",resultXML);
        String returnCode  = root.element("return_code").getText();
	    if(SUCCESS_CODE.equals(returnCode)){
		    String deductionBalance  = root.element("deductionBalance").getText();
		    jsonObject.put("deductionBalance",deductionBalance);
	    }
        jsonObject.put("message",result);
        jsonObject.put("returnCode",returnCode);
        return jsonObject;
    }
    /**
     * 积分解冻
     * @param map
     * @return
     */
    public JSONObject integralJD(Map<String,String>map)throws Exception {
        JSONObject jsonObject = new JSONObject();
        StringBuilder builder = new StringBuilder();
        IntegralFlowing integralFlowing = new IntegralFlowing();
        //签名map
        Map<String,String>signMapParam = new HashMap<String,String>();
        //用户ID
        String uid = map.get("uid");
        //订单编号
        String outtradeno = map.get("out_trade_no");
        //时间戳
        String timestamp = String.valueOf(System.currentTimeMillis());
        //描述
        String description = map.get("description");
        //积分场景
        String type = map.get("type");
        //积分数量
        String credits = map.get("credits");
        //附加数据
        String attach = map.get("attach");
        //手机号
        AppUser appUser = appUserService.getBySourceSystemId(uid);
        String phone = appUser.getMobile();
        //生成订单编号
        String autoTrandeNo = UUID.randomUUID().toString().replaceAll("-","");
        if(StringUtils.isEmpty(uid)){
            throw  new DataWarnningException("用户id不能为空");
        }else{
            signMapParam.put("uid",uid);
        }
        if(StringUtils.isEmpty(timestamp)){
            throw  new DataWarnningException("时间戳不能为空");
        }else{
            signMapParam.put("timestamp",timestamp);
        }
        if(StringUtils.isEmpty(outtradeno)){
            throw new DataWarnningException("订单号不能为空");
        }else{
            signMapParam.put("out_trade_no",outtradeno);
        }
        signMapParam.put("app_id",app_id);
        signMapParam.put("service_id",service_id);
        //生成签名
        String integralparam = IntegralRuleSign.formatMap(signMapParam,false,true);
        String strsign = integralparam+"&key="+Global.getConfig("INTEGRAL.KEY");
        String sign = MD5.MD5Encode(strsign).toUpperCase();
        //拼接xml
        builder.append("<xml>");
        builder.append("<app_id>").append(app_id).append("</app_id>");
        builder.append("<service_id>").append(service_id).append("</service_id>");
        builder.append("<uid>").append(uid).append("</uid>");
        builder.append("<out_trade_no>").append(outtradeno).append("</out_trade_no>");
        builder.append("<timestamp>").append(timestamp).append("</timestamp>");
        builder.append("<sign>").append(sign).append("</sign>").append("</xml>");
        //保存解冻积分流水
        integralFlowing.setAppId(app_id);
        integralFlowing.setDescription(description);
        integralFlowing.setServiceId(service_id);
        integralFlowing.setStatus("");
        integralFlowing.setUid(uid);
        integralFlowing.setOutTradeNo(outtradeno);
        integralFlowing.setAutoTradeNo(autoTrandeNo);
        integralFlowing.setPhone(phone);
        integralFlowing.setIntegralType("UNFREEZE");
        integralFlowing.setIntegralUrl(IntegralUrl.UNFREEZE_INTEGRAL);
        integralFlowing.setIntegralParame(builder.toString());
        integralFlowing.setTimestamps(timestamp);
        integralFlowing.setType(type);
        integralFlowing.setAttach(attach);
        if(StringUtils.isEmpty(credits)){
            integralFlowing.setCredits(new Long("0"));
        }
        integralFlowing.setSign(sign);
        integralFlowingService.save(integralFlowing);

        String resultXML = HttpUtil.post(IntegralUrl.UNFREEZE_INTEGRAL,builder.toString(),IntegralUrl.headerMap);
        Document document = DocumentHelper.parseText(resultXML);
        Element root = document.getRootElement();
        String result  = root.element("return_msg").getText();
        logger.debug("返回的xml {} ",resultXML);
        String returnCode  = root.element("return_code").getText();
	    jsonObject.put("returnCode",returnCode);
        jsonObject.put("message",result);
        //根据uid和sign查询积分流水信息
        IntegralFlowing inteFlowing = integralFlowingService.selectIntegralByUidAndSign(integralFlowing);
        if(inteFlowing!=null){
            inteFlowing.setStatus(returnCode);
            integralFlowingService.updateIntegralFlowingStatus(inteFlowing);
        }
        return jsonObject;
    }
	/**
	 * 积分冻结
	 * @param map
	 * @return
	 */
	public JSONObject integralDJ(Map<String,String>map)throws Exception {
		JSONObject jsonObject = new JSONObject();
		StringBuilder builder = new StringBuilder();
        IntegralFlowing integralFlowing = new IntegralFlowing();
        //签名map
        Map<String,String>signMapParam = new HashMap<String,String>();
		//用户ID
		String uid = map.get("uid");
		//时间戳
        String timestamp = String.valueOf(System.currentTimeMillis());
        //积分数量
        String credits = map.get("credits");
        //订单编号
        String outtradeno = map.get("out_trade_no");
        //场景编号
        String type = map.get("type");
		//生成订单编号
		String autoTrandeNo = UUID.randomUUID().toString().replaceAll("-","");
		//描述
		String description = map.get("description");
		//附加数据
		String attach = map.get("attach");
        //手机号
        AppUser appUser = appUserService.getBySourceSystemId(uid);
        String phone = appUser.getMobile();
        if(StringUtils.isEmpty(uid)){
           throw new DataWarnningException("用户id不能为空");
        }else{
           signMapParam.put("uid",uid);
        }
        if(StringUtils.isEmpty(timestamp)){
            throw new DataWarnningException("时间戳不能为空");
        }else{
            signMapParam.put("timestamp",timestamp);
        }
        if(StringUtils.isEmpty(credits)){
            throw new DataWarnningException("积分数量不能为空");
        }else if(!RegExpValidatorUtils.IsIntNumber(credits)){
            throw new DataWarnningException("积分数量必须是整数");
        }else{
            signMapParam.put("credits",credits);
        }
        if(StringUtils.isEmpty(outtradeno)){
            throw  new DataWarnningException("订单编号不能为空");
        }else{
            signMapParam.put("out_trade_no",outtradeno);
        }
        if(StringUtils.isEmpty(type)){
            throw new DataWarnningException("场景类型不能为空");
        }else{
            signMapParam.put("type",type);
        }
        signMapParam.put("app_id",app_id);
        signMapParam.put("service_id",service_id);
		//生成签名
		String integralparam = IntegralRuleSign.formatMap(signMapParam,false,true);
		String strsign = integralparam+"&key="+Global.getConfig("INTEGRAL.KEY");
		String sign = MD5.MD5Encode(strsign).toUpperCase();
		//拼接xml
		builder.append("<xml>");
		builder.append("<app_id>").append(app_id).append("</app_id>");
		builder.append("<service_id>").append(service_id).append("</service_id>");
		builder.append("<uid>").append(uid).append("</uid>");
		builder.append("<out_trade_no>").append(outtradeno).append("</out_trade_no>");
		builder.append("<credits>").append(credits).append("</credits>");
		builder.append("<type>").append(type).append("</type>");
		builder.append("<timestamp>").append(timestamp).append("</timestamp>");
		builder.append("<sign>").append(sign).append("</sign>").append("</xml>");
		//保存冻结积分流水
		integralFlowing.setAppId(app_id);
		integralFlowing.setDescription(description);
		integralFlowing.setServiceId(service_id);
		integralFlowing.setStatus("");
		integralFlowing.setUid(uid);
		integralFlowing.setOutTradeNo(outtradeno);
		integralFlowing.setAutoTradeNo(autoTrandeNo);
		integralFlowing.setIntegralType("FREEZE");
        integralFlowing.setPhone(phone);
		integralFlowing.setIntegralUrl(IntegralUrl.FREEZE_INTEGRAL);
		integralFlowing.setIntegralParame(builder.toString());
		integralFlowing.setTimestamps(timestamp);
		integralFlowing.setType(type);
		integralFlowing.setAttach(attach);
		if(StringUtils.isEmpty(credits)){
			integralFlowing.setCredits(new Long("0"));
		}else{
            integralFlowing.setCredits(new Long(credits));
        }
		integralFlowing.setSign(sign);
		integralFlowingService.save(integralFlowing);
		String resultXML = HttpUtil.post(IntegralUrl.FREEZE_INTEGRAL,builder.toString(),IntegralUrl.headerMap);
		Document document = DocumentHelper.parseText(resultXML);
		Element root = document.getRootElement();
		String result  = root.element("return_msg").getText();
		logger.debug("返回的xml {} ",resultXML);
		String returnCode  = root.element("return_code").getText();
		jsonObject.put("returnCode",returnCode);
		jsonObject.put("message",result);
		//根据uid和sign查询积分流水信息
		IntegralFlowing inteFlowing = integralFlowingService.selectIntegralByUidAndSign(integralFlowing);
		if(inteFlowing!=null){
			inteFlowing.setStatus(returnCode);
			//更新积分状态
			integralFlowingService.updateIntegralFlowingStatus(inteFlowing);
		}
		return jsonObject;
	}
}

package com.cmig.future.csportal.api.common.integalrule.controllers;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.RegExpValidatorUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.integral.dto.IntegralFlowing;
import com.cmig.future.csportal.module.integral.dto.IntegralRule;
import com.cmig.future.csportal.module.integral.service.IIntegralFlowingService;
import com.cmig.future.csportal.module.integral.service.IIntegralRuleService;
import com.cmig.future.csportal.module.operate.integral.components.IntegralRuleOperation;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zpf on 2017/4/25.
 */
@Controller
@RequestMapping(value = "${userPath}")
public class AppIntegralRuleController extends BaseExtendController {
    @Autowired
    private IAppUserService appUserService;
    @Autowired
    private IIntegralRuleService integralRuleService;
    @Autowired
    private IIntegralFlowingService integralFlowingService;
	@Autowired
	private IntegralRuleOperation integralRuleOperation;

    /**
     * 增加积分
     *
     * @param
     * @param request
     * @param response
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "st/integral/integralrule/add", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp addIntegral(IntegralRule integralRule, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RetApp retApp = new RetApp();
        JSONObject jsonObject = new JSONObject();
        //Token
        String token = getParam(request, "token", "");
        if (StringUtils.isEmpty(token)) {
            throw new DataWarnningException("token 不能为空");
        }
        AppUser appUser = appUserService.getByToken(token);

        //用户ID
        String uid = appUser.getSourceSystemId();
        //消费金额
        String purchase_amount = getParam(request, "purchase_amount", "");
        //场景类型
        String type = getParam(request, "type", "");
        //订单编号
        String out_trade_no = getParam(request, "out_trade_no", "");
        //描述
        String integralDescription = getParam(request, "integralDescription", "");
        //附加数据
        String attach = getParam(request, "attach", "");
        //消息发布字数
        String msg_length = getParam(request, "msg_length", "");
        //消息图片数量
        String pic_num = getParam(request, "pic_num", "");
        //是否首评
        String first_comment = getParam(request, "first_comment", "");
        //操作业务板块的当前时间:(如果是登陆操作，也就是登陆的时间，如果是注册，也就是注册时间)
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("uid", uid);
        paramMap.put("type", type);
        paramMap.put("out_trade_no", out_trade_no);
        paramMap.put("description", integralDescription);
        paramMap.put("msg_length", msg_length);
        paramMap.put("attach", attach);
        paramMap.put("purchase_amount", purchase_amount);
        paramMap.put("pic_num", pic_num);
        paramMap.put("first_comment", first_comment);
       if(!StringUtils.isEmpty(out_trade_no)){
            if(checkouttradno(type,out_trade_no)){
                throw new DataWarnningException("订单号已经存在");
            }
        }
	    JSONObject object = integralRuleOperation.addIntegral(paramMap);
	    String message = object.get("message").toString();
	    String returnCode = object.get("returnCode").toString();
	    if (IntegralRuleOperation.SUCCESS_CODE.equals(returnCode)) {
		    String balance = object.get("balance").toString();
            String credits = object.get("credits").toString();
            //返回增加的积分数
            jsonObject.put("credits",credits);
		    jsonObject.put("balance", balance);
		    retApp.setData(jsonObject);
		    retApp.setMessage(message);
		    retApp.setStatus(OK);
	    } else {
		    retApp.setStatus(returnCode);
		    retApp.setMessage(message);
	    }
        return retApp;
    }

    /**
     * 扣除积分
     *
     * @param integralRule
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "st/integral/integralrule/deduIntegral", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp deductionIntegral(IntegralRule integralRule, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RetApp retApp = new RetApp();
        JSONObject jsonObject = new JSONObject();
        Map<String, String> paramMap = new HashMap<String, String>();
        //token
        String token = getParam(request, "token", "");
        //订单账号
        String out_trade_no = getParam(request, "out_trade_no", "");
        if (StringUtils.isEmpty(token)) {
            throw new DataWarnningException("token 不能为空");
        }
        if(StringUtils.isEmpty(out_trade_no)){
            throw  new DataWarnningException("订单号不能为空");
        }
        AppUser appUser = appUserService.getByToken(token);
        //用户ID
        String uid = appUser.getSourceSystemId();
        //积分数量
        String credits = getParam(request, "credits", "");
        //场景类型
        String type = getParam(request, "type", "");
        //描述
        String integralDescription = getParam(request, "integralDescription", "");
        //附加数据
        String attach = getParam(request, "attach", "");
        //订单编号
        paramMap.put("out_trade_no", out_trade_no);
        paramMap.put("uid", uid);
        paramMap.put("credits", credits);
        paramMap.put("type", type);
        if (!StringUtils.isEmpty(integralDescription)) {
            paramMap.put("description", integralDescription);
        }
        if (!StringUtils.isEmpty(attach)) {
            paramMap.put("attach", attach);
        }

	    JSONObject object = integralRuleOperation.deduction(paramMap);
	    String message = object.get("message").toString();
	    String returnCode = object.get("returnCode").toString();
	    if (IntegralRuleOperation.SUCCESS_CODE.equals(returnCode)) {
		    String balance = object.get("balance").toString();
            //返回扣除的积分数
            String deduction_credits = object.get("credits").toString();
            jsonObject.put("credits",deduction_credits);
            jsonObject.put("balance", balance);
		    retApp.setData(jsonObject);
		    retApp.setMessage(message);
		    retApp.setStatus(OK);
	    } else {
		    retApp.setStatus(returnCode);
		    retApp.setMessage(message);
	    }
        return retApp;
    }

    /**
     * 积分查询
     *
     * @param request
     * @param map
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "st/integral/integralrule/deduIntegralfind", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp initIntegralfind(HttpServletRequest request, Map<String, String> map) throws Exception {
        RetApp retApp = new RetApp();
        JSONObject jsonObject = new JSONObject();
        String token = getParam(request, "token", "");
        if (StringUtils.isEmpty(token)) {
            throw new DataWarnningException("token 不能为空");
        }
        AppUser appUser = appUserService.getByToken(token);
        //用户ID
        String uid = appUser.getSourceSystemId();
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("uid", uid);
	    JSONObject object = integralRuleOperation.integralSelect(paramMap);
	    String message = object.get("message").toString();
	    String returnCode = object.get("returnCode").toString();
	    if (IntegralRuleOperation.SUCCESS_CODE.equals(returnCode)) {
		    String balance = object.get("balance").toString();
		    String freezeAmount = object.get("freezeAmount").toString();
		    String usefulBalance = object.get("usefulBalance").toString();
		    jsonObject.put("freezeAmount",freezeAmount);
		    jsonObject.put("balance", balance);
		    jsonObject.put("usefulBalance", usefulBalance);
		    retApp.setData(jsonObject);
		    retApp.setMessage(message);
		    retApp.setStatus(OK);
	    } else {
		    retApp.setStatus(returnCode);
		    retApp.setMessage(message);
	    }
        return retApp;
    }

    /**
     * 积分登陆
     * @param request
     */
    @RequestMapping(value = "st/integral/integralrule/integralLogin", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp integralLogin(HttpServletRequest request) throws Exception {
        RetApp retApp = new RetApp();
        Map<String, String> paramMap = new HashMap<String, String>();
        JSONObject jsonObject = new JSONObject();
        //token
        String token = getParam(request, "token", "");
        //指定跳转界面
        String redirect = getParam(request, "redirect", "");
        if (StringUtils.isEmpty(token)) {
            throw new DataWarnningException("token不能为空");
        }
        AppUser appUser = appUserService.getByToken(token);
        //用户id
        String uid = appUser.getSourceSystemId();
        //用户手机号
        String phone = appUser.getMobile();
        paramMap.put("uid", uid);
        paramMap.put("phone", phone);
        if (redirect != null && !StringUtils.isEmpty(redirect)) {
            paramMap.put("redirect", redirect);
        }
	    JSONObject object = integralRuleOperation.integralLogin(paramMap);
	    String message = object.get("message").toString();
	    String returnCode = object.get("returnCode").toString();
	    if (IntegralRuleOperation.SUCCESS_CODE.equals(returnCode)) {
		    String url = object.get("url").toString();
		    jsonObject.put("url", url);
		    retApp.setData(jsonObject);
		    retApp.setStatus(OK);
	    } else {
		    retApp.setStatus(returnCode);
	    }
	    retApp.setMessage(message);
        return retApp;
    }

    /**
     * 计算当前订单最大可抵扣积分、金额
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/st/integral/integralrule/findIntegralByType", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp findIntegralByType(HttpServletRequest request) throws Exception {
        RetApp retApp = new RetApp();
        //token
        String token = getParam(request, "token", "");
        //业务场景
        String type = getParam(request, "type", "");
		//订单总额
        String orderAmount = getParam(request, "orderAmount", "");
        if(StringUtils.isEmpty(token)){
            throw new DataWarnningException("token不能为空");
        }
        if (StringUtils.isEmpty(orderAmount)) {
	        throw new DataWarnningException("订单总金额不能为空");
        }else if(!RegExpValidatorUtils.IsIntNumber(orderAmount)){
	        throw new DataWarnningException("订单总金额格式不正确");
        }
        //根据token获取appUser用户
        AppUser appUser = appUserService.getByToken(token);
        //用户id
        String uid = appUser.getSourceSystemId();
        JSONObject result=integralRuleService.getMaxAvailableIntegral(type, orderAmount, uid);
        retApp.setData(result);
        retApp.setMessage("计算成功！");
        retApp.setStatus(OK);
        return retApp;
    }

    /**
     * 积分冻结
     * @param request
     * @return
     */
    @RequestMapping(value = "integral/integralrule/integralFreeze", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
   public RetApp IntegralFrozen(HttpServletRequest request) throws Exception {
       RetApp retApp = new RetApp();
       //token
       String token = getParam(request, "token", "");
       //积分数量
       String credits =  getParam(request, "credits", "");
       //订单号
       String  out_trade_no = getParam(request, "out_trade_no", "");
        //场景编号
       String type = getParam(request, "type", "");

       //根据token获取appUser用户
       AppUser appUser = appUserService.getByToken(token);
       if(StringUtils.isEmpty(token)){
           throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"token");
       }
       if(StringUtils.isEmpty(credits)){
           throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"积分数量");
       }else if(!RegExpValidatorUtils.IsIntNumber(credits)){
           throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"积分必须是整数");
       }
        if(StringUtils.isEmpty(out_trade_no)){
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"订单号");
        }
        if(StringUtils.isEmpty(type)){
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"场景编号");
        }
       Map<String,String> paramMap = new HashMap<String,String>();
       paramMap.put("out_trade_no",out_trade_no);
       paramMap.put("credits",credits);
       paramMap.put("uid",appUser.getSourceSystemId());
       paramMap.put("type",type);
	    JSONObject jsonObject = integralRuleOperation.integralDJ(paramMap);
	    String return_code = jsonObject.get("returnCode").toString();
	    String message = jsonObject.get("message").toString();
	    if(IntegralRuleOperation.SUCCESS_CODE.equals(return_code)){
		    retApp.setStatus(OK);
		    retApp.setMessage(message);
	    }else{
		    retApp.setStatus(FAIL);
		    retApp.setMessage(message);
	    }
       return retApp;
   }
    /**
     * 积分解冻
     * @param request
     * @return
     */
    @RequestMapping(value = "integral/integralrule/integralunFreeze", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp IntegralunFrozen(HttpServletRequest request) throws Exception {
        RetApp retApp = new RetApp();
        //token
        String token = getParam(request, "token", "");
        //生成订单号
        String out_trade_no = getParam(request, "out_trade_no", "");
        //根据token获取appUser用户
        AppUser appUser = appUserService.getByToken(token);
        if(StringUtils.isEmpty(token)){
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"token");
        }
        if(StringUtils.isEmpty(out_trade_no)){
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"订单号");
        }
        Map<String,String> paramMap = new HashMap<String,String>();
        paramMap.put("out_trade_no",out_trade_no);
        paramMap.put("uid",appUser.getSourceSystemId());
	    JSONObject jsonObject = integralRuleOperation.integralJD(paramMap);
	    String return_code = jsonObject.get("returnCode").toString();
	    String message = jsonObject.get("message").toString();
	    if(IntegralRuleOperation.SUCCESS_CODE.equals(return_code)){
		    retApp.setStatus(OK);
		    retApp.setMessage(message);
	    }else{
		    retApp.setStatus(FAIL);
		    retApp.setMessage(message);
	    }
        return retApp;
    }

	@RequestMapping(value = "/integral/enableFlag", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp lejiaPayEnableFlag(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject=new JSONObject();
		if(Global.getBoolean("INTEGRAL.ISENABLE_OR_DISABLED",false)){
			jsonObject.put("flag","Y");
		}else{
			jsonObject.put("flag","N");
		}
		return RetAppUtil.success(jsonObject,"");
	}

    /**
     * 根据订单号和场景查看该积分流水是否存在
     * @param outtradno,type
     * @return
     */
    public boolean checkouttradno(String type, String outtradno){
        IntegralFlowing dto = new IntegralFlowing();
        dto.setOutTradeNo(outtradno);
        dto.setType(type);
        List<IntegralFlowing> list = integralFlowingService.getIntegralByTypeAndOutTradeno(dto);
        if(list.size()>0){
            return true;
        }else{
            return false;
        }
    }



}


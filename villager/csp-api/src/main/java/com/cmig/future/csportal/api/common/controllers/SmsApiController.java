package com.cmig.future.csportal.api.common.controllers;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.servlet.ValidateCodeServlet;
import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.common.utils.SmsUtil;
import com.cmig.future.csportal.common.zmcore.usercenter.CoreUserUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.entity.Sms;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUser;
import com.cmig.future.csportal.module.properties.mgtuser.service.IMgtUserService;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SmsApiController extends BaseExtendController {

    @Autowired
    private IAppUserService appUserService;

	@Autowired
	private IMgtUserService mgtUserService;

    /**
     * 注册发短信验证码
     */
    /*
	@RequestMapping(value = "${commonPath}/sms/sendSms", produces = { "application/json" }, method = RequestMethod.POST)
	@ResponseBody
	public RetApp sendSms(@ModelAttribute Sms sms, HttpServletRequest request, HttpServletResponse response) {
		RetApp retApp = new RetApp();
		String type = getParam(request, "type", SmsUtil.SMS_CODE_COMMON);
        try {
            String telno = sms.getMobile();
            if (StringUtils.isBlank(telno)) {
                logger.info(telno + " telno is null");
                retApp.setStatus(FAIL);
                retApp.setMessage("手机号不能为空");
                return retApp;
            }

            SmsUtil.sendSmsCode(type, sms.getMobile());

            retApp.setStatus(OK);
            retApp.setData(sms);
            retApp.setMessage("发送成功!");
            logger.debug("code {} " + "已发送");
        }catch (DataWarnningException e){
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage("发送失败!");
        }
		return retApp;
	}
*/
	public void timeLimit(String mobile) {
		long curTime = System.currentTimeMillis();
		saveExpxKeyCache(mobile, String.valueOf(curTime));
	}

	private void saveExpxKeyCache(String k, String v) {
		JedisUtils.set(k, v, 0);
	}

    /**
     * 发短信验证码
     *
     * @param sms
     * @param request
     * @param response
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "${userPath}/sendSmsCode", produces = { "application/json" }, method = RequestMethod.POST)
    @ResponseBody
    public RetApp sendSmsCode(@ModelAttribute Sms sms, HttpServletRequest request, HttpServletResponse response) {
        String type = getParam(request, "type", "");
        String mobile = getParam(request, "mobile", "");
	    String validateCode = getParam(request, "validateCode", "");
	    String ip=getRemoteid(request);

        if(StringUtils.isEmpty(type)
		        ||StringUtils.isEmpty(mobile)
		        ||StringUtils.isEmpty(validateCode)){
	        throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
        }

	    // 忘记密码需要验证手机号是否注册
	    if (SmsUtil.SMS_CODE_FORGET_PASSWORD.equals(type)) {
		    if (!CoreUserUtils.validationMobile(mobile)) {
			    throw new DataWarnningException("该手机号未注册");
		    }
	    }

	    // 忘记密码需要验证手机号是否注册
	    if (SmsUtil.SMS_CODE_FORGET_PASSWORD_MGT.equals(type)) {
		    MgtUser mgtUser = mgtUserService.getUserByMobile(mobile);
		    if (null == mgtUser) {
			    throw new DataWarnningException("该手机号未注册");
		    }
	    }

        boolean res = ValidateCodeServlet.validateAppImageCode(request, validateCode);
        if(!res){
	        throw new ServiceException(RestApiExceptionEnums.IMAGE_CODE_VALIDATION_ERROR);
        }

        SmsUtil.sendSmsCode(ip,type, mobile);
        return RetAppUtil.success(sms,"发送成功!");
    }


	/**
     * 验证短信验证码
     * @param request
     * @param response
     * @return
     */
	@CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "${userPath}/checkSmsCode", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp checkSmsValidationCode(HttpServletRequest request, HttpServletResponse response) {
        RetApp retApp = new RetApp();
        String smsValidationCode = getParam(request, "smsValidationCode", "");
	    String type = getParam(request, "type", "");
        String mobile = getParam(request, "mobile", "");
        try {

	        if(StringUtils.isEmpty(type)){
		        throw new DataWarnningException("验证码用途不能为空");
	        }

            if (StringUtils.isEmpty(mobile)) {
                throw new DataWarnningException("手机号不能为空");
            }

            if (smsValidationCode.isEmpty()) {
                throw new DataWarnningException("短信验证码不能为空");
            }
            String telnoKey = SmsUtil.SMS_VALIDATE_CODE + type+ "." + mobile;
            // 校验短信验证码
            SmsUtil.checkSmsCode(mobile, telnoKey, smsValidationCode);

	        if(SmsUtil.SMS_CODE_OPENID_LOGIN.equals(type)) {
		        String messageWarning = "请在2分钟内完成操作";
		        JSONObject jsonObject = new JSONObject();
		        // 密码验证后下一步操作授权码,2分钟有效
		        String nextOperateToken = IdGen.uuid();
		        JedisUtils.set(SmsUtil.SMS_CODE_NEXT_OPERATE +type+ mobile, nextOperateToken, 120);
		        jsonObject.put("nextOperateToken", nextOperateToken);
		        jsonObject.put("messageWarning", messageWarning);
		        retApp.setData(jsonObject);
	        }
            retApp.setStatus(OK);
            retApp.setMessage("短信验证码正确");
        }catch (DataWarnningException e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        } catch (Exception e) {
            String errMsg = e.getMessage();
            retApp.setStatus(FAIL);
            retApp.setMessage(errMsg);
            return retApp;
        }
        return retApp;
    }
}
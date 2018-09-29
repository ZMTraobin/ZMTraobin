/**
 * .
 */
package com.cmig.future.csportal.api.app.controllers;

import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.common.utils.SmsUtil;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.zmcore.usercenter.CoreUserUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.entity.SmsCodeEntity;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.sys.code.CodeUtil;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.dto.AppUserAuth;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserAuthService;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * app用户Controller
 *
 * @author zhengshujun
 * @version 2015-12-01
 */
@Controller
@RequestMapping(value = "${userPath}")
@ResponseBody
public class AppUserAuthApiController extends BaseExtendController {

    @Autowired
	private IAppUserAuthService appUserAuthService;

	@Autowired
	private IAppUserService appUserService;

	/**
	 * 检查该uuid是否已绑定用户
	 * @param request
	 * @return
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/check/uuid/exists" ,method = RequestMethod.POST)
	public RetApp checkUuidExists(HttpServletRequest request){
		String uuid=getParam(request,"uuid","");
		String identityType=getParam(request,"identityType","");

		checkNull(uuid);
		checkNull(identityType);

		if(!CodeUtil.checkDicValueExists("CSP.AUTH_TYPE",identityType)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"identityType");
		}

		JSONObject jsonObject=new JSONObject();
		AppUserAuth appUserAuth=appUserAuthService.findByUUIDAndType(uuid,identityType);
		if(null!=appUserAuth){
			jsonObject.put("flag","Y");
		}else{
			jsonObject.put("flag","N");
		}
		return RetAppUtil.success(jsonObject,"");
	}

	/**
	 * uuid登录
	 * @param request
	 * @return
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/uuid/login" ,method = RequestMethod.POST)
	public RetApp uuidLogin(HttpServletRequest request){
		String uuid=getParam(request,"uuid","");
		String identityType=getParam(request,"identityType","");

		checkNull(uuid);
		checkNull(identityType);

		if(!CodeUtil.checkDicValueExists("CSP.AUTH_TYPE",identityType)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"identityType");
		}

		AppUserAuth appUserAuth=appUserAuthService.findByUUIDAndType(uuid,identityType);
		if(null==appUserAuth){
			throw new ServiceException(RestApiExceptionEnums.UUID_LOGIN_FAIL);
		}else{
			AppUser appUser=appUserService.get(appUserAuth.getAppUserId());
			JSONObject jsonObject = appUserService.login(appUser,null,null);
			return RetAppUtil.success(jsonObject,"登录成功");
		}
	}


	/**
	 * 绑定已注册帐号
	 * @param request
	 * @return
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/uuid/bind/mobile" ,method = RequestMethod.POST)
	public RetApp uuidBindMobile(HttpServletRequest request) throws Exception {
		String uuid=getParam(request,"uuid","");
		String identityType=getParam(request,"identityType","");
		String mobile=getParam(request,"mobile","");
		String smsValidationCode=getParam(request,"smsValidationCode","");

		String nickName=getParam(request,"nickName","");
		String avatar=getParam(request,"avatar","");

		checkNull(uuid);
		checkNull(identityType);
		checkNull(mobile);
		checkNull(smsValidationCode);

		if(!CodeUtil.checkDicValueExists("CSP.AUTH_TYPE",identityType)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"identityType");
		}

		// redis code key
		String telnoKey = SmsUtil.SMS_VALIDATE_CODE +SmsUtil.SMS_CODE_UUID_BIND_MOBILE+"." + mobile;
		SmsUtil.checkSmsCode(mobile, telnoKey, smsValidationCode);

		AppUserAuth appUserAuth=appUserAuthService.findByUUIDAndType(uuid,identityType);
		if(null!=appUserAuth){
			AppUser appUser=appUserService.get(appUserAuth.getAppUserId());
			JSONObject jsonObject = appUserService.login(appUser,null,null);
			return RetAppUtil.success(jsonObject,"登录成功");
		}else{
			AppUser appUser=appUserService.getByMobile(mobile);
			if(null==appUser){
				String uid=CoreUserUtils.getUid(mobile);
				if (!StringUtils.isEmpty(uid)) {
					String ip = getRemoteid(request);
					appUser=appUserService.newAppUser(ip,mobile,null,null,null,null,uid);
				}else{
					telnoKey = SmsUtil.SMS_VALIDATE_CODE + SmsUtil.SMS_CODE_REGISTER + "." + mobile;
					//新增一个用户注册短信验证码
					String code = SmsUtil.authCode(6);
					SmsCodeEntity smsCodeEntity = new SmsCodeEntity();
					smsCodeEntity.setSmsCode(code);
					smsCodeEntity.setMobile(mobile);
					smsCodeEntity.setCreateTime(new Date());
					JedisUtils.setObject(telnoKey, smsCodeEntity, 600);

					//返回注册短信验证码
					JSONObject jsonObject =new JSONObject();
					jsonObject.put("smsValidationCode",code);
					return RetAppUtil.error(new ServiceException(RestApiExceptionEnums.MOBILE_WITH_OUT_REGISTER),jsonObject);
				}
			}
			//绑定第三方uuid和该手机号用户
			appUserAuth=new AppUserAuth();
			appUserAuth.setUuid(uuid);
			appUserAuth.setIdentityType(identityType);
			appUserAuth.setAppUserId(appUser.getId());
			if(!StringUtils.isEmpty(avatar)) {
				appUserAuth.setAvatar(avatar);
			}
			if(!StringUtils.isEmpty(nickName)) {
				appUserAuth.setNickName(nickName);
			}
			appUserAuthService.save(appUserAuth);

			//更新用户昵称
			if(!StringUtils.isEmpty(nickName)&&StringUtils.isEmpty(appUser.getNickName())) {
				appUser.setNickName(nickName);
				appUserService.save(appUser);
			}
			//绑定成功后登录
			JSONObject jsonObject = appUserService.login(appUser,null,null);
			return RetAppUtil.success(jsonObject,"绑定成功");
		}
	}


	/**
	 * 解绑
	 * @param request
	 * @return
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/st/uuid/unbundled" ,method = RequestMethod.POST)
	public RetApp unbundled(HttpServletRequest request){
		String uuid=getParam(request,"uuid","");
		String identityType=getParam(request,"identityType","");
		String token=getParam(request,"token","");

		checkNull(uuid);
		checkNull(identityType);

		if(!CodeUtil.checkDicValueExists("CSP.AUTH_TYPE",identityType)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"identityType");
		}

		appUserAuthService.unbundled(identityType,uuid,token);
		return RetAppUtil.success("解绑成功");
	}
}
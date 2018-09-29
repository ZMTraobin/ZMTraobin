/**
 * .
 */
package com.cmig.future.csportal.api.app.controllers;

import com.alibaba.fastjson.JSONArray;
import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.common.utils.SmsUtil;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.verify.RSAUtilsWithKey;
import com.cmig.future.csportal.common.zmcore.sso.CoreSSOClientUtils;
import com.cmig.future.csportal.common.zmcore.usercenter.CoreUserUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.entity.SmsCodeEntity;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.sys.code.CodeUtil;
import com.cmig.future.csportal.module.sys.service.SystemService;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.help.UserQRCodeHelper;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.cmig.future.csportal.module.user.appuser.service.IThirdUuidLoginService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * app用户Controller
 *
 * @author zhengshujun
 * @version 2015-12-01
 */
@Controller
@RequestMapping(value = "${userPath}")
@ResponseBody
@CrossOrigin(origins = "*", maxAge = 3600)
public class ThirdUserOauthApiController extends BaseExtendController {

	@Autowired
	@Qualifier("casThirdUuidLoginService")
	private IThirdUuidLoginService thirdUuidLoginService;

	@Autowired
	private IAppUserService appUserService;

	/**
	 * 检查该code是否已绑定用户
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/check/third/exists" ,method = RequestMethod.POST)
	public RetApp checkcodeExists(HttpServletRequest request){
		String thirdChannelCode=getParam(request,"thirdChannelCode","");
		String appId=getParam(request,"appId","");
		String code=getParam(request,"code","");

		checkNull(thirdChannelCode);
		checkNull(appId);
		checkNull(code);

		if(!CodeUtil.checkDicValueExists("CSP.AUTH_TYPE",thirdChannelCode)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"该渠道未注册");
		}
		JSONObject jsonObject=new JSONObject();
		boolean result= thirdUuidLoginService.isBind(thirdChannelCode,appId,code);
		if(result){
			jsonObject.put("flag","Y");
		}else{
			jsonObject.put("flag","N");
		}
		return RetAppUtil.success(jsonObject,"");
	}

	/**
	 * code登录
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/third/login" ,method = RequestMethod.POST)
	public RetApp codeLogin(HttpServletRequest request) throws Exception {
		String thirdChannelCode=getParam(request,"thirdChannelCode","");
		String appId=getParam(request,"appId","");
		String code=getParam(request,"code","");

		checkNull(thirdChannelCode);
		checkNull(appId);
		checkNull(code);

		if(!CodeUtil.checkDicValueExists("CSP.AUTH_TYPE",thirdChannelCode)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"该渠道未注册");
		}

		JSONObject jsonObject =thirdUuidLoginService.login(thirdChannelCode,appId,code);
		return RetAppUtil.success(jsonObject,"登录成功");
	}


	/**
	 * 登录状态绑定
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/st/third/bind" ,method = RequestMethod.POST)
	public RetApp bindUser(HttpServletRequest request) throws Exception {
		String thirdChannelCode=getParam(request,"thirdChannelCode","");
		String appId=getParam(request,"appId","");
		String code=getParam(request,"code","");
		String token=getParam(request,"token","");

		checkNull(thirdChannelCode);
		checkNull(appId);
		checkNull(code);

		if(!CodeUtil.checkDicValueExists("CSP.AUTH_TYPE",thirdChannelCode)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"该渠道未注册");
		}

		AppUser appUser= appUserService.getByToken(token);
		//绑定
		thirdUuidLoginService.bind(thirdChannelCode,appId,code, appUser.getSourceSystemId());
		return RetAppUtil.success("绑定成功");
	}

	/**
	 * 绑定手机号
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/third/bind/mobile" ,method = RequestMethod.POST)
	public RetApp codeBindMobile(HttpServletRequest request) throws Exception {
		String thirdChannelCode=getParam(request,"thirdChannelCode","");
		String appId=getParam(request,"appId","");
		String code=getParam(request,"code","");
		String mobile=getParam(request,"mobile","");
		String smsValidationCode=getParam(request,"smsValidationCode","");

		String nickName=getParam(request,"nickName","");
		String avatar=getParam(request,"avatar","");

		checkNull(thirdChannelCode);
		checkNull(appId);
		checkNull(code);
		checkNull(mobile);
		checkNull(smsValidationCode);

		if(!CodeUtil.checkDicValueExists("CSP.AUTH_TYPE",thirdChannelCode)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"该渠道未注册");
		}

		// redis code key
		String telnoKey = SmsUtil.SMS_VALIDATE_CODE +SmsUtil.SMS_CODE_UUID_BIND_MOBILE+"." + mobile;
		SmsUtil.checkSmsCode(mobile, telnoKey, smsValidationCode);

		String uid ;
		AppUser appUser=appUserService.getByMobile(mobile);
		if(null!=appUser) {
			uid=appUser.getSourceSystemId();
		}else{
			//乐家慧无该用户
			uid = CoreUserUtils.getUid(mobile);
			if (!StringUtils.isEmpty(uid)) {
				String ip = getRemoteid(request);
				appUser = new AppUser();
				appUser.setRelationFlag("0");
				appUser.setUserType("1");
				appUser.setRetTime(new Date());
				appUser.setMobile(mobile);
				appUser.setLastIp(ip);
				appUser.setLastTime(new Date());
				appUser.setPassWord(SystemService.entryptPassword(StringUtils.getRandomCharAndNum(8)));
				appUser.setSourceSystemId(uid);
				appUser.setUpdateCorePasswordFlag(Constants.YES);
				appUserService.save(appUser);
				//异步更新用户二维码
				UserQRCodeHelper.updateUserQRCode(mobile);
			}
		}
		if(null==appUser){
			telnoKey = SmsUtil.SMS_VALIDATE_CODE + SmsUtil.SMS_CODE_REGISTER + "." + mobile;
			//新增一个用户注册短信验证码
			String smsCode = SmsUtil.authCode(6);
			SmsCodeEntity smsCodeEntity = new SmsCodeEntity();
			smsCodeEntity.setSmsCode(smsCode);
			smsCodeEntity.setMobile(mobile);
			smsCodeEntity.setCreateTime(new Date());
			JedisUtils.setObject(telnoKey, smsCodeEntity, 600);

			//返回注册短信验证码
			JSONObject jsonObject =new JSONObject();
			jsonObject.put("smsValidationCode",smsCode);
			return RetAppUtil.error(new ServiceException(RestApiExceptionEnums.MOBILE_WITH_OUT_REGISTER),jsonObject);
		}else{
			//绑定并登录
			thirdUuidLoginService.bind(thirdChannelCode,appId,code, uid);
			JSONObject jsonObject = thirdUuidLoginService.login(thirdChannelCode,appId,code);
			return RetAppUtil.success(jsonObject,"绑定成功");
		}
	}


	/**
	 * 第三方登录注册用户
	 */
	@RequestMapping(value = "/appuser/third/register", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp uuidRegister(HttpServletRequest request, HttpServletResponse response)throws Exception {
		String ip = getRemoteid(request);
		String mobile = getParam(request, "mobile", "");
		String password = getParam(request, "password", "");
		String registrationInvitationCode = getParam(request, "registrationInvitationCode", "");
		String smsValidationCode = getParam(request, "smsValidationCode", "");

		String thirdChannelCode=getParam(request,"thirdChannelCode","");
		String appId=getParam(request,"appId","");
		String code=getParam(request,"code","");

		String nickName=getParam(request,"nickName","");
		String avatar=getParam(request,"avatar","");


		checkNull(thirdChannelCode);
		checkNull(appId);
		checkNull(code);

		if(!CodeUtil.checkDicValueExists("CSP.AUTH_TYPE",thirdChannelCode)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"该渠道未注册");
		}

		//1、用户注册
		AppUser appUser=appUserService.register(ip,mobile,password,registrationInvitationCode,smsValidationCode,thirdChannelCode,appId,code);
		//2、调用核心登录接口
		if (password.length() >= 128) {
			// RSA私钥解密
			password = RSAUtilsWithKey.decryptString(password);
		}
		Map ssoMap = CoreSSOClientUtils.getUuid(mobile, password);
		String tgt = ssoMap.get("tgt") == null ? "" : ssoMap.get("tgt").toString();
		String st = ssoMap.get("st") == null ? "" : ssoMap.get("st").toString();
		//3、返回登录信息
		JSONObject jsonObject =appUserService.login(appUser,tgt,st);
		return RetAppUtil.success(jsonObject,"注册成功");
	}


	/**
	 * 解绑
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/st/third/unbundled" ,method = RequestMethod.POST)
	public RetApp unbundled(HttpServletRequest request) throws Exception {
		String thirdChannelCode=getParam(request,"thirdChannelCode","");
		String token=getParam(request,"token","");

		checkNull(thirdChannelCode);

		if(!CodeUtil.checkDicValueExists("CSP.AUTH_TYPE",thirdChannelCode)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"thirdChannelCode");
		}

		AppUser appUser=appUserService.getByToken(token);
		thirdUuidLoginService.unbind(appUser.getSourceSystemId(),thirdChannelCode,null,null);
		return RetAppUtil.success("解绑成功");
	}

	/**
	 * 查询已绑定帐号信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/st/third/bindList" ,method = RequestMethod.POST)
	public RetApp findBindList(HttpServletRequest request){
		String thirdChannelCode=getParam(request,"thirdChannelCode","");
		String token=getParam(request,"token","");
		AppUser appUser=appUserService.getByToken(token);

		JSONArray jsonArray;
		if("sinaSdk".equals(thirdChannelCode)){
			thirdChannelCode="sina";
		}
		jsonArray=CoreUserUtils.userThirdQuery(appUser.getSourceSystemId(),null,null,thirdChannelCode);
		Long total=jsonArray==null?0L:jsonArray.size();
		return RetAppUtil.success(jsonArray,"",total) ;
	}

	/**
	 * 获取微信openid
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/third/getWxOpenId" ,method = RequestMethod.POST)
	public RetApp thirdGetWxOpenId(HttpServletRequest request) throws Exception {
		String thirdChannelCode=getParam(request,"thirdChannelCode","");
		String appId=getParam(request,"appId","");
		String code=getParam(request,"code","");

		checkNull(thirdChannelCode);
		checkNull(appId);
		checkNull(code);

		String wxOpenId=CoreSSOClientUtils.thirdGetWxOpenId(thirdChannelCode,appId,code);
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("wxOpenId",wxOpenId);
		return RetAppUtil.success(jsonObject,"获取成功");
	}
}
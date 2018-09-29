/**
 * .
 */
package com.cmig.future.csportal.api.app.controllers;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.servlet.ValidateCodeServlet;
import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.common.utils.RegExpValidatorUtils;
import com.cmig.future.csportal.common.utils.SmsUtil;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.verify.RSAUtilsWithKey;
import com.cmig.future.csportal.common.zmcore.sso.CoreSSOClientUtils;
import com.cmig.future.csportal.common.zmcore.usercenter.CoreUserUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.sys.code.CodeUtil;
import com.cmig.future.csportal.module.sys.service.SystemService;
import com.cmig.future.csportal.module.sys.utils.UserTokenUtils;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.cmig.future.csportal.module.user.attentionCommunity.dto.AttentionCommunityUser;
import com.cmig.future.csportal.module.user.attentionCommunity.service.IAttentionCommunityUserService;
import com.cmig.future.csportal.module.user.attentionCommunity.util.CommunityHelper;
import com.cmig.future.csportal.module.zyyh.ZyyhConstants;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * app用户Controller
 *
 * @author zhengshujun
 * @version 2015-12-01
 */
@RestController
@RequestMapping(value = "${userPath}")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AppUserApiController extends BaseExtendController {

    private static String update_password_PRIFIX = "updatePasswordToken_";

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private IAttentionCommunityUserService attentionCommunityUserService;

    /**
     * 个人信息修改
     */
    @RequestMapping(value = "/st/appuser/updateUser", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp updateUser(@ModelAttribute AppUser data, HttpServletRequest request) {
        if (data == null) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
        }
        String token = getParam(request, "token", "");
        AppUser appUser = appUserService.getByToken(token);
        if (appUser != null) {
            if(StringUtils.isNotEmpty(data.getNickName())){
                appUser.setNickName(data.getNickName());
            }
            if(StringUtils.isNotEmpty(data.getUserName())){
                appUser.setUserName(data.getUserName());
            }
            if(StringUtils.isNotEmpty(data.getSex())){
                appUser.setSex(data.getSex());
            }

            if(StringUtils.isNotEmpty(data.getPhone())){
                appUser.setPhone(data.getPhone());
            }

            if(StringUtils.isNotEmpty(data.getEmail())){
                appUser.setEmail(data.getEmail());
            }

            if(StringUtils.isNotEmpty(data.getSelfIntroduction())){
                appUser.setSelfIntroduction(data.getSelfIntroduction());
            }
            appUserService.save(appUser);
        }
        return RetAppUtil.success("修改用户成功!");
    }

    /**
     * 个人中心 获取个人信息
     */
    @RequestMapping(value = "/st/appuser/getAppUserInfo", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp getAppUserInfo(@ModelAttribute AppUser data, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String token = getParam(request, "token", "");
        AppUser appUser = appUserService.getByToken(token);
        jsonObject.put("userId", appUser.getId());
        jsonObject.put("userName", StringUtils.isEmpty(appUser.getUserName()) ? "" : appUser.getUserName());
        jsonObject.put("userType", StringUtils.isEmpty(appUser.getUserType()) ? "" : appUser.getUserType());
        jsonObject.put("relationFlag", StringUtils.isEmpty(appUser.getRelationFlag()) ? "" : appUser.getRelationFlag());//是否实名认证
        jsonObject.put("mobile", StringUtils.isEmpty(appUser.getMobile()) ? "" : appUser.getMobile());
        jsonObject.put("email", StringUtils.isEmpty(appUser.getEmail()) ? "" : appUser.getEmail());
        jsonObject.put("selfIntroduction", StringUtils.isEmpty(appUser.getSelfIntroduction()) ? "" : appUser.getSelfIntroduction());//自我介绍
        jsonObject.put("retTime", appUser.getRetTime() == null ? "" : DateUtils.formatDateTime(appUser.getRetTime()));
        jsonObject.put("lastTime", appUser.getLastTime() == null ? "" : DateUtils.formatDateTime(appUser.getLastTime()));
        jsonObject.put("lastIp", StringUtils.isEmpty(appUser.getLastIp()) ? "" : appUser.getLastIp());
        jsonObject.put("userIcon", Global.getFullImagePath(appUser.getUserIcon()));
        jsonObject.put("nickName", StringUtils.isEmpty(appUser.getNickName()) ? "" : appUser.getNickName());
        jsonObject.put("twoCode", Global.getFullImagePath(appUser.getTwoCode()));
        jsonObject.put("integralBalance", appUser.getIntegralBalance() == null ? BigDecimal.ZERO : appUser.getIntegralBalance());
        jsonObject.put("attentionCommunityId", "");
        jsonObject.put("attentionCommunityName", "");

        //当前关注的小区信息
        AttentionCommunityUser attentionCommunityUser = new AttentionCommunityUser();
        attentionCommunityUser.setUserId(appUser.getId());
        attentionCommunityUser.setIsAttention(CommunityHelper.IS_ATTENTION);
        List<AttentionCommunityUser> list = attentionCommunityUserService.findList(attentionCommunityUser);
        if (list != null && list.size() > 0) {
            AttentionCommunityUser thisAttentionCommunityUser = list.get(0);
            jsonObject.put("attentionCommunityId", thisAttentionCommunityUser.getCommunityId());
            jsonObject.put("attentionCommunityName", thisAttentionCommunityUser.getCommunityName());
        }
        return RetAppUtil.success(jsonObject,"查询成功!");
    }

    /**
     * 个人信息 上传头像
     */
    @RequestMapping(value = "/st/appuser/uploadIcon", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp uploadIcon(@ModelAttribute AppUser data, Model model, HttpServletRequest request) {
	    if(StringUtils.isEmpty(data.getUserIcon())){
		    throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"头像");
	    }
	    String token = getParam(request, "token", "");
	    AppUser appUser = appUserService.getByToken(token);
	    appUser.setUserIcon(data.getUserIcon());
	    appUserService.save(appUser);
	    return RetAppUtil.success("头像上传成功!");
    }

    /**
     * app端用户注册
     */
    @RequestMapping(value = "/appuser/register", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp register(HttpServletRequest request)throws Exception {
        String ip = getRemoteid(request);
        String mobile = getParam(request, "mobile", "");
        String password = getParam(request, "password", "");
        String registrationInvitationCode = getParam(request, "registrationInvitationCode", "");
        String smsValidationCode = getParam(request, "smsValidationCode", "");
        //1、用户注册
        AppUser appUser=appUserService.register(ip,mobile,password,registrationInvitationCode,smsValidationCode,null,null,null,null);
        //2、返回登录信息
        JSONObject jsonObject =appUserService.login(appUser,null,null);
        return RetAppUtil.success(jsonObject,"注册成功");
    }


	/**
	 * 第三方登录注册用户
	 */
	@RequestMapping(value = "/appuser/uuid/register", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp uuidRegister(HttpServletRequest request)throws Exception {
		String ip = getRemoteid(request);
		String mobile = getParam(request, "mobile", "");
		String password = getParam(request, "password", "");
		String registrationInvitationCode = getParam(request, "registrationInvitationCode", "");
		String smsValidationCode = getParam(request, "smsValidationCode", "");

		String uuid=getParam(request,"uuid","");
		String identityType=getParam(request,"identityType","");
		String nickName=getParam(request,"nickName","");
		String avatar=getParam(request,"avatar","");


		if(StringUtils.isEmpty(uuid)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"uuid");
		}

		if(StringUtils.isEmpty(identityType)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"identityType");
		}

		if(!CodeUtil.checkDicValueExists("CSP.AUTH_TYPE",identityType)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"identityType");
		}

		//1、用户注册
		AppUser appUser=appUserService.register(ip,mobile,password,registrationInvitationCode,smsValidationCode,uuid,identityType,nickName,avatar);
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
     * app端用户登录-密码登录
     */
    @RequestMapping(value = "/appuser/login", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp login(HttpServletRequest request) throws Exception {
        String ip = getRemoteid(request);
        String mobile = getParam(request, "mobile", "");
        String password = getParam(request, "password", "");
        if (mobile.isEmpty()) {
            throw new DataWarnningException("手机号不能为空");
        }
        if (password.isEmpty()) {
            throw new DataWarnningException("密码不能为空");
        }

        if (password.length() >= 128) {
            // RSA私钥解密
            password = RSAUtilsWithKey.decryptString(password);
        }
        AppUser appUser = appUserService.getByMobile(mobile);
        if(null!=appUser&&SystemService.validatePassword(password,appUser.getPassWord())){
            appUser.setLastIp(ip);
            appUser.setLastTime(new Date());
            appUserService.save(appUser);
            JSONObject jsonObject =appUserService.login(appUser,null,null);
            return RetAppUtil.success(jsonObject,"登录成功");
        }
        return RetAppUtil.error(new ServiceException(RestApiExceptionEnums.LOGIN_FAIL));
    }

    /**
     * app端用户修改密码
     */
    @RequestMapping(value = "/st/appuser/updatePassword", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp updatePassword(HttpServletRequest request) {
        String ip = getRemoteid(request);
        String password = getParam(request, "password", "");
        String oldPassword = getParam(request, "oldPassword", "");
        String token = getParam(request, "token", "");
        if (password.isEmpty()) {
            throw new DataWarnningException("密码不能为空");
        }
        if (oldPassword.isEmpty()) {
            throw new DataWarnningException("旧密码不能为空");
        }

        if (password.length() >= 128) {
            // RSA私钥解密
            password = RSAUtilsWithKey.decryptString(password);
        }
        if (oldPassword.length() >= 128) {
            // RSA私钥解密
            oldPassword = RSAUtilsWithKey.decryptString(oldPassword);
        }

        AppUser appUser = appUserService.getByToken(token);
        if(!SystemService.validatePassword(oldPassword,appUser.getPassWord())){
            throw new DataWarnningException("原密码不正确");
        }
        //updatePasswordWithCore(ip, password, oldPassword, appUser);
        appUser.setPassWord(SystemService.entryptPassword(password));
        appUser.setUpdateCorePasswordFlag(Constants.YES);
        appUser.setLastIp(ip);
        appUserService.save(appUser);
        return RetAppUtil.success("密码修改成功");
    }

    private void updatePasswordWithCore(String ip, String password, String oldPassword, AppUser appUser) throws DataWarnningException {
        String uid = appUser.getSourceSystemId();
        if (!CoreUserUtils.validationPassword(uid, oldPassword)) {
            throw new DataWarnningException("原密码不正确");
        }
        if (!RegExpValidatorUtils.IsPassword(password)) {
            throw new DataWarnningException("密码必须是数字和字母组合，长度为6-20位");
        }
        CoreUserUtils.updatePassword(uid, password);
        appUser.setPassWord(SystemService.entryptPassword(password));
        appUser.setUpdateCorePasswordFlag(Constants.YES);
        appUser.setLastIp(ip);
        appUserService.save(appUser);
    }

    /**
     * app端用户忘记密码-修改密码
     */
    @RequestMapping(value = "/appuser/forgetPassword", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp forgetPassword(HttpServletRequest request) {
        String ip = getRemoteid(request);
        String mobile = getParam(request, "mobile", "");
        String password = getParam(request, "password", "");
        String updatePasswordToken = getParam(request, "updatePasswordToken", "");// 来源与短信验证码授权
        if (mobile.isEmpty()) {
            throw new DataWarnningException("手机号不能为空");
        }

        if (!(CoreUserUtils.validationMobile(mobile))) {
            throw new DataWarnningException("该手机号未注册");
        }

        if (password.isEmpty()) {
            throw new DataWarnningException("密码不能为空");
        }

        if (updatePasswordToken.isEmpty()) {
            throw new DataWarnningException("授权码不能为空");
        }

	    String redis_updatePasswordToken = JedisUtils.get(update_password_PRIFIX + mobile);
	    if (StringUtils.isEmpty(redis_updatePasswordToken)) {
		    throw new DataWarnningException("验证码已失效");
	    } else if (!updatePasswordToken.equals(redis_updatePasswordToken)) {
		    throw new DataWarnningException("验证码错误");
	    }

	    if (password.length() >= 128) {
		    // RSA私钥解密
		    password = RSAUtilsWithKey.decryptString(password);
	    }

	    if (!RegExpValidatorUtils.IsPassword(password)) {
		    throw new DataWarnningException("密码必须是数字和字母组合，长度为6-20位");
	    }

	    boolean updateFlag = CoreUserUtils.updatePasswordByMobile(mobile, password);
	    AppUser appUser = appUserService.getByMobile(mobile);
	    if (updateFlag && appUser != null) {
		    appUser.setUpdateCorePasswordFlag(Constants.YES);
		    appUser.setPassWord(SystemService.entryptPassword(password));
		    appUser.setLastIp(ip);
		    appUserService.save(appUser);
	    }
        return RetAppUtil.success("密码设置成功");
    }

    /**
     * app 验证手机号是否已注册
     */
    @RequestMapping(value = "/appuser/checkMobileIsExists", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp checkMobileIsExists(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String mobile = getParam(request, "mobile", "");
        logger.debug(mobile);

        if (mobile.isEmpty()) {
            throw new DataWarnningException("手机号不能为空");
        }

        if (CoreUserUtils.validationMobile(mobile)) {
            logger.debug("CoreUserUtils.validationMobile(mobile) {}", mobile);
            jsonObject.put("isMobileExists", Constants.YES);
        } else {
            logger.debug("appUserService.getByMobile(mobile) {}", mobile);
            jsonObject.put("isMobileExists", Constants.NO);
        }

	    return RetAppUtil.success(jsonObject,"");
    }

    /**
     * 获取找回密码授权码
     */
    @RequestMapping(value = "/appuser/checkSmsValidationCode", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp checkSmsValidationCode(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String smsValidationCode = getParam(request, "smsValidationCode", "");
        String mobile = getParam(request, "mobile", "");
        String messageWarning = "请在2分钟内完成操作";
        logger.debug(smsValidationCode);

        if (mobile.isEmpty()) {
            throw new DataWarnningException("手机号不能为空");
        }

        if (smsValidationCode.isEmpty()) {
            throw new DataWarnningException("短信验证码不能为空");
        }
        // 会话
        // Session session = UserUtils.getSession();
        String telnoKey = SmsUtil.SMS_VALIDATE_CODE +SmsUtil.SMS_CODE_FORGET_PASSWORD+"." + mobile;
        // 校验短信验证码
        SmsUtil.checkSmsCode(mobile, telnoKey, smsValidationCode);

        String updatePasswordToken = IdGen.uuid();
        JedisUtils.set(update_password_PRIFIX + mobile, updatePasswordToken, 120);// 密码修改授权码,2分钟有效
        // 密码修改授权码
        jsonObject.put("updatePasswordToken", updatePasswordToken);
        jsonObject.put("messageWarning", messageWarning);

        return RetAppUtil.success(jsonObject,"");
    }

    /**
     * 验证图片验证码
     */
    @RequestMapping(value = "/appuser/imgValidationCode", produces = {"application/json"}, method = {RequestMethod.POST,RequestMethod.GET})
    public RetApp imgValidationCode(HttpServletRequest request) {
        String validateCode = getParam(request, "validateCode", "");
        String mobile = getParam(request, "mobile", "");
        if (mobile.isEmpty()) {
            throw new DataWarnningException("手机号不能为空");
        }

        if (validateCode.isEmpty()) {
            throw new DataWarnningException("图片验证码不能为空");
        }
        // 验证手机号是否注册
        if ((!StringUtils.isEmpty(mobile))) {
            if (!CoreUserUtils.validationMobile(mobile)) {
                throw new DataWarnningException("该手机号未注册");
            }
        }

        // 校验图片验证码
        boolean res = ValidateCodeServlet.validateAppImageCode(request, validateCode);
        if (!res) {
            throw new ServiceException(RestApiExceptionEnums.IMAGE_CODE_VALIDATION_ERROR);
        }

        return RetAppUtil.success("成功");
    }

    /**
     * get图片验证码
     */
    @RequestMapping(value = "/appuser/getImgValidationCode", method = {RequestMethod.POST,RequestMethod.GET})
    public void getImgValidationCode(HttpServletRequest request, HttpServletResponse response) {
        try {
            ValidateCodeServlet validateCodeServlet = new ValidateCodeServlet();
            validateCodeServlet.createAppImage(request, response);
        } catch (Exception e) {
            String errMsg = e.getMessage();
            logger.debug(errMsg);
        }
    }

    /**
     * 验证用户token是否过期
     */
    @RequestMapping(value = "/appuser/checkUserToken", method = RequestMethod.POST)
    public RetApp checkUserToken(HttpServletRequest request) {
        String token = getParam(request, "token", "");
	    if (StringUtils.isEmpty(token)) {
            throw new DataWarnningException("token不能为空");
        }
        if (StringUtils.isBlank(UserTokenUtils.getUserIdByToken(token))) {
            return RetAppUtil.tokenDisabled();
        }
        return RetAppUtil.success("ok");
    }


    /**
     * 验证tgt是否有效
     * @param request
     * @return
     */
    @RequestMapping(value = "/appuser/checkTgt", method = RequestMethod.POST)
    public RetApp checkTgt(HttpServletRequest request) throws Exception {
        String tgt = getParam(request, "tgt", "");
        if (StringUtils.isEmpty(tgt)) {
            throw new DataWarnningException("tgt不能为空");
        }
        String st=CoreSSOClientUtils.getServiceTicket(tgt);
        if(StringUtils.isEmpty(st)){
	        return RetAppUtil.tokenDisabled();
        }
        return RetAppUtil.success("tgt有效");
    }

	/**
	 * 获取中原银行二类账户余额
	 */
	@RequestMapping(value = "/st/appuser/zyyh/balance", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp zyyhBalance(HttpServletRequest request) {
		String token=getParam(request,"token","");
		AppUser appUser=appUserService.getByToken(token);
		StringBuffer url=new StringBuffer();
		url.append(Global.getConfig("ZYYH.serverUrl"));
		url.append(ZyyhConstants.BALANCE_URL);
		url.append("?");
		url.append("channelNo="+Global.getConfig("ZYYH.channelNo"));
		url.append("&channelKey="+Global.getConfig("ZYYH.channelKey"));
		url.append("&mobileNo="+appUser.getMobile());
		url.append("&occType="+Global.getConfig("ZYYH.occType"));
		url.append("&openId="+appUser.getSourceSystemId());
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("balance",0);
		jsonObject.put("url",url.toString());
		return RetAppUtil.success(jsonObject,"查询成功");
	}

	/**
	 * 登出
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/appuser/loginOut", method = RequestMethod.POST)
	public RetApp loginOut(HttpServletRequest request) {
		String token = getParam(request, "token", "");
		appUserService.loginOut(token);
		return RetAppUtil.success("登出成功");
	}

	@RequestMapping(value = "/appuser/singleLogin", method = RequestMethod.POST)
	public RetApp getTokenByTgt(HttpServletRequest request) throws Exception {
		String tgt=getParam(request,"tgt","");
		if(StringUtils.isEmpty(tgt)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}
		// RSA私钥解密
		tgt = RSAUtilsWithKey.decryptString(tgt);
		JSONObject jsonObject=appUserService.singleLoginByTgt(tgt);
		return RetAppUtil.success(jsonObject,"登录成功");
	}


    /**
     * 实名认证
     */
    @RequestMapping(value = "/st/appuser/oauth", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp oauth(HttpServletRequest request) {
        String idcard=getParam(request,"idcard","");
        String userName=getParam(request,"userName","");
        if(StringUtils.isEmpty(idcard)||StringUtils.isEmpty(userName)){
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
        }
        String token = getParam(request, "token", "");
        AppUser appUser=appUserService.getByToken(token);
        appUserService.oauth(appUser,idcard,userName);
        return RetAppUtil.success("认证成功!");
    }
}
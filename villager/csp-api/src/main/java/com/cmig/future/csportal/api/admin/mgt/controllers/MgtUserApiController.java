/**
 * .
 */
package com.cmig.future.csportal.api.admin.mgt.controllers;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.exception.IncorrectCredentialsException;
import com.cmig.future.csportal.common.exception.UnknownAccountException;
import com.cmig.future.csportal.common.servlet.ValidateCodeServlet;
import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.common.utils.RegExpValidatorUtils;
import com.cmig.future.csportal.common.utils.SmsUtil;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.verify.RSAUtilsWithKey;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.properties.community.dto.BaseCommunity;
import com.cmig.future.csportal.module.properties.community.service.IBaseCommunityService;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUser;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUserCommunity;
import com.cmig.future.csportal.module.properties.mgtuser.service.IMgtUserService;
import com.cmig.future.csportal.module.sys.service.SystemService;
import com.cmig.future.csportal.module.sys.utils.AdminTokenUtils;
import com.cmig.future.csportal.module.weixin.entry.User;
import com.cmig.future.csportal.module.weixin.helper.WorkWxHelper;
import com.cmig.future.csportal.module.weixin.service.OauthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * app用户Controller
 *
 * @author zhengshujun
 * @version 2015-12-01
 */
@Controller//commonPath
@RequestMapping(value = "${managementPath}/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MgtUserApiController extends BaseExtendController {

    private static Logger logger = LoggerFactory.getLogger(MgtUserApiController.class);

    @Autowired
    private IMgtUserService mgtUserService;
    @Autowired
    private IBaseCommunityService baseCommunityService;

	@Autowired
	private OauthService oauthService;

    private static String UPDATE_PASSWORD_PRIFIX = "updatePasswordToken_";

    /**
     * 物业端用户登录
     */
    @RequestMapping(value = "/adminLogin", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp adminLogin(@ModelAttribute MgtUser data, HttpServletRequest request, HttpServletResponse response) {
        if (data == null) {
            throw new DataWarnningException("参数不能为空");
        }
        if (data.getMobile() == null || "".equals(data.getMobile())) {
            throw new DataWarnningException("手机号不能为空");
        }
        if (data.getPassword() == null || "".equals(data.getPassword())) {
            throw new DataWarnningException("账号或密码错误");
        }
        try {
            String password = data.getPassword();
            if (password.length() >= 128) {
                //RSA私钥解密
                password = RSAUtilsWithKey.decryptString(password);
            }
	        JSONObject jsonObject =mgtUserService.login(data.getMobile(),password);
            return RetAppUtil.success(jsonObject,"登陆成功!");
        } catch (UnknownAccountException ex) {// 用户名没有找到。
            return RetAppUtil.error(new ServiceException(RestApiExceptionEnums.INCORRECT_CREDENTIALS));
        } catch (IncorrectCredentialsException ex) {// 用户名密码不匹配。
	        return RetAppUtil.error(new ServiceException(RestApiExceptionEnums.INCORRECT_CREDENTIALS));
        } catch (Exception e) {// 其他的登录错误
            return RetAppUtil.unKonwError();
        }
    }

    /**
     * 物业端用户修改密码
     */
    @RequestMapping(value = "/modifyPwd", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp modifyPwd(@ModelAttribute MgtUser data, HttpServletRequest request, HttpServletResponse response) {
	    String pwdNew = getParam(request, "pwdNew", "");
	    if (data == null) {
		    throw new DataWarnningException("参数不能为空");
	    }

	    if ((data.getPassword() == null || "".equals(data.getPassword())) && StringUtils.isEmpty(pwdNew)) {
		    throw new DataWarnningException("新密码不能为空");
	    }

	    if (!StringUtils.isEmpty(pwdNew)) {
		    data.setNewPwd(pwdNew);
	    }

	    if (data.getOldPwd() == null || "".equals(data.getOldPwd())) {
		    throw new DataWarnningException("原密码不能为空");
	    }
	    MgtUser mgtUser = AdminTokenUtils.getUserByToken(data.getToken());
	    if (mgtUser == null) {
		    logger.warn("user is null");
		    throw new DataWarnningException("用户不存在");
	    } else {
		    String oldPassword = data.getOldPwd();
		    if (oldPassword.length() >= 128) {
			    //RSA私钥解密
			    oldPassword = RSAUtilsWithKey.decryptString(oldPassword);
		    }
		    if (SystemService.validateMgtPassword(oldPassword, mgtUser.getPassword())) {
			    String password = data.getNewPwd();
			    if (password.length() >= 128) {
				    //RSA私钥解密
				    password = RSAUtilsWithKey.decryptString(password);
			    }
			    mgtUserService.updatePassword(mgtUser,password);
			    return RetAppUtil.success("密码修改成功!");
		    } else {
			    throw new DataWarnningException("原密码错误");
		    }
	    }
    }

    /**
     * 获取用户信息
     */
    @RequestMapping(value = "/getAdminUser", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp getAdminUser(@ModelAttribute MgtUser data, HttpServletRequest request, HttpServletResponse response) {
	    if (data.getToken() == null || "".equals(data.getToken())) {
		    throw new DataWarnningException("token为空");
	    }
	    MgtUser user = AdminTokenUtils.getUserByToken(data.getToken());

	    if (user == null) {
		    throw new DataWarnningException("用户不存在");
	    } else {
		    return RetAppUtil.success(user,"查询成功!");
	    }
    }

    /**
     * 查询用户关联的小区
     */
    @RequestMapping(value = "/findUserCommunityList", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp findUserCommunityList(@ModelAttribute MgtUserCommunity data, HttpServletRequest request, HttpServletResponse response) {
	    if (data.getToken() == null || "".equals(data.getToken())) {
		    throw new DataWarnningException("token不能为空");
	    }
	    MgtUser user = AdminTokenUtils.getUserByToken(data.getToken());
	    if (user == null) {
		    throw new DataWarnningException("用户不存在");
	    } else {
		    BaseCommunity communityQuery = new BaseCommunity();
		    communityQuery.setUserId(user.getId());
		    List<BaseCommunity> list = baseCommunityService.findUserCommunityList(communityQuery);
		    return RetAppUtil.success(list,"查询成功!");
	    }
    }

    /**
     * 查询用户关联的小区
     */
    @RequestMapping(value = "/getSourceToken", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp getSourceToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    String communityId = getParam(request, "communityId", "");
	    String token = getParam(request, "token", "");
	    if (StringUtils.isEmpty(token)) {
		    throw new DataWarnningException("token不能为空");
	    }
	    if (StringUtils.isEmpty(communityId)) {
		    throw new DataWarnningException("communityId不能为空");
	    }
	    MgtUser user = AdminTokenUtils.getUserByToken(token);
	    if (user == null) {
		    throw new DataWarnningException("参数token错误");
	    } else {
		    BaseCommunity community = baseCommunityService.get(communityId);
		    if (null == community) {
			    throw new DataWarnningException("参数communityId错误");
		    }
		    if (StringUtils.isEmpty(community.getServerUrl())) {
			    throw new DataWarnningException("小区未配置服务器域名");
		    }

		    String sourceToken = mgtUserService.getSourceToken(community, user.getId());
		    JSONObject jsonObject = new JSONObject();
		    jsonObject.put("sourceToken", sourceToken);
		    return RetAppUtil.success(jsonObject,"获取成功");
	    }
    }


    /**
     * 找回密码
     */
    @RequestMapping(value = "/forgetPassword", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp forgetPassword(HttpServletRequest request, HttpServletResponse response) {
        String mobile = getParam(request, "mobile", "");
        String password = getParam(request, "password", "");
        String updatePasswordToken = getParam(request, "updatePasswordToken", "");// 来源与短信验证码授权
	    if (mobile.isEmpty()) {
		    throw new DataWarnningException("手机号不能为空");
	    }

	    MgtUser mgtUser = mgtUserService.getUserByMobile(mobile);
	    if (null==mgtUser) {
		    throw new DataWarnningException("该手机号未注册");
	    }

	    if (password.isEmpty()) {
		    throw new DataWarnningException("密码不能为空");
	    }

	    if (updatePasswordToken.isEmpty()) {
		    throw new DataWarnningException("授权码不能为空");
	    }

	    String redis_updatePasswordToken = JedisUtils.get(UPDATE_PASSWORD_PRIFIX + mobile);
	    if (StringUtils.isEmpty(redis_updatePasswordToken)) {
		    throw new DataWarnningException("验证码已失效");
	    } else if (!updatePasswordToken.equals(redis_updatePasswordToken)) {
		    throw new DataWarnningException("验证码错误");
	    }

	    if (password.length() >= 128) {
		    // RSA私钥解密
		    password = RSAUtilsWithKey.decryptString(password);
	    }

	    if (!RegExpValidatorUtils.IsPasswordMgt(password)) {
		    throw new DataWarnningException("密码必须是数字或字母，长度为6-20位");
	    }

	    mgtUserService.updatePassword(mgtUser,password);
        return RetAppUtil.success("密码设置成功");
    }

    /**
     * 验证图片验证码
     */
    @RequestMapping(value = "/imgValidationCode", produces = {"application/json"}, method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public RetApp imgValidationCode(HttpServletRequest request, HttpServletResponse response) {
        String validateCode = getParam(request, "validateCode", "");
        String mobile = getParam(request, "mobile", "");
        logger.debug(validateCode);
        String devid = request.getHeader("devid");
        logger.debug(devid);
	    if (mobile.isEmpty()) {
		    throw new DataWarnningException("手机号不能为空");
	    }

	    if (validateCode.isEmpty()) {
		    throw new DataWarnningException("图片验证码不能为空");
	    }
	    // 验证手机号是否注册
	    if (!StringUtils.isEmpty(mobile)) {
		    MgtUser mgtUser = mgtUserService.getUserByMobile(mobile);
		    if (null == mgtUser) {
			    throw new DataWarnningException("该手机号未注册");
		    }
	    }


	    if (StringUtils.isEmpty(devid)) {
		    throw new DataWarnningException("IEMI不能为空");
	    }
	    // 校验图片验证码
	    boolean res = ValidateCodeServlet.validateAppImageCode(request, validateCode);
	    if (!res) {
		    throw new DataWarnningException("图片验证码错误");
	    }
        return RetAppUtil.success("成功");
    }

    /**
     * 获取找回密码授权码
     */
    @RequestMapping(value = "/checkSmsValidationCode", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp checkSmsValidationCode(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        String smsValidationCode = getParam(request, "smsValidationCode", "");
        String mobile = getParam(request, "mobile", "");
        String messageWarning = "请在2分钟内完成操作";
	    if (mobile.isEmpty()) {
		    throw new DataWarnningException("手机号不能为空");
	    }

	    if (smsValidationCode.isEmpty()) {
		    throw new DataWarnningException("短信验证码不能为空");
	    }
	    // 会话
	    // Session session = UserUtils.getSession();
	    String telnoKey = SmsUtil.SMS_VALIDATE_CODE + SmsUtil.SMS_CODE_FORGET_PASSWORD_MGT + "." + mobile;
	    // 校验短信验证码
	    SmsUtil.checkSmsCode(mobile, telnoKey, smsValidationCode);

	    String updatePasswordToken = IdGen.uuid();
	    JedisUtils.set(UPDATE_PASSWORD_PRIFIX + mobile, updatePasswordToken, 120);// 密码修改授权码,2分钟有效
	    // 密码修改授权码
	    jsonObject.put("updatePasswordToken", updatePasswordToken);
	    jsonObject.put("messageWarning", messageWarning);
        return RetAppUtil.success(jsonObject,"");
    }


	/**
	 * 登出
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/loginOut", method = RequestMethod.POST)
	@ResponseBody
	public RetApp loginOut(HttpServletRequest request, HttpServletResponse response) {
		String token = getParam(request, "token", "");
		mgtUserService.loginOut(token);
		return RetAppUtil.success("登出成功");
	}

	/**
	 * 企业微信授权码单点登录
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/wx/code/login", produces = {"application/json"}, method = {RequestMethod.POST, RequestMethod.GET})
	public RetApp getUserByCode(HttpServletRequest request) throws Exception {
		String code = getParam(request, "code", "");
		String agentNo = getParam(request, "agentNo", "");
		if (StringUtils.isEmpty(agentNo)
				|| StringUtils.isEmpty(code)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}
		User user = oauthService.getUserInfo(agentNo, code);
		String cropId= WorkWxHelper.getCorpIdByAgentNo(agentNo);
		String serverUrl=WorkWxHelper.getCorp(cropId).getServerUrl();
		JSONObject jsonObject=new JSONObject();
		/*if(!StringUtils.isEmpty(user)&&!StringUtils.isEmpty(user.getMobile())) {
			jsonObject=mgtUserService.sslogin(user.getMobile());
		}*/
		jsonObject.put("wxUserInfo",user);
		jsonObject.put("serverUrl",serverUrl);
		return RetAppUtil.success(jsonObject,"登录成功") ;
	}

}
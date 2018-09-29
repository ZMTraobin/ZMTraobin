package com.cmig.future.csportal.module.user.appuser.service.impl;

import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.zmcore.sso.CoreSSOClientUtils;
import com.cmig.future.csportal.common.zmcore.usercenter.CoreUserUtils;
import com.cmig.future.csportal.module.sys.service.SystemService;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.help.UserQRCodeHelper;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.cmig.future.csportal.module.user.appuser.service.IThirdUuidLoginService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 18:31 2017/10/25.
 * @Modified by zhangtao on 18:31 2017/10/25.
 */
@Service("casThirdUuidLoginService")
public class CasThirdUuidLoginServiceImpl implements IThirdUuidLoginService {

	@Autowired
	private IAppUserService appUserService;


	@Override
	public boolean isBind(String thirdChannelCode, String appId, String code) {
		try {
			String uid=CoreSSOClientUtils.thirdQuery(thirdChannelCode,appId,code);
			if(!StringUtils.isEmpty(uid)){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public JSONObject login(String thirdChannelCode, String appId, String code)throws Exception {
		//1、调用核心登录接口
		Map ssoMap = CoreSSOClientUtils.thirdLogin(thirdChannelCode,appId,code);
		if (null != ssoMap && ssoMap.containsKey("uid") && !StringUtils.isEmpty(ssoMap.get("uid"))) {
			String tgt = ssoMap.get("tgt") == null ? "" : ssoMap.get("tgt").toString();
			String st = ssoMap.get("st") == null ? "" : ssoMap.get("st").toString();
			String uid = ssoMap.get("uid") == null ? "" : ssoMap.get("uid").toString();
			AppUser appUser =appUserService.getBySourceSystemId(uid);
			//2、如果核心返回登录成功，并且乐家慧无此用户，则需要新增该用户
			if (null == appUser) {
				String mobile=CoreUserUtils.getMobile(uid);
				appUser = new AppUser();
				appUser.setRelationFlag("0");
				appUser.setUserType("1");
				appUser.setRetTime(new Date());
				appUser.setMobile(mobile);
				appUser.setLastIp(null);
				appUser.setLastTime(new Date());
				appUser.setPassWord(SystemService.entryptPassword(StringUtils.getRandomCharAndNum(8)));
				appUser.setSourceSystemId(uid);
				appUser.setUpdateCorePasswordFlag(Constants.YES);
				appUserService.save(appUser);

				//异步更新用户二维码
				UserQRCodeHelper.updateUserQRCode(mobile);
				//异步更新用户手机号归属地信息
				//MobileUtil.updateUserMobileInfo(appUser);
			} else {
				appUser.setLastIp(null);
				appUser.setLastTime(new Date());
				appUserService.save(appUser);
			}
			JSONObject jsonObject = appUserService.login(appUser,tgt,st);
			return jsonObject;
		}
		return null;
	}

	@Override
	public void bind(String thirdChannelCode, String appId, String code, String uid) throws Exception {
		CoreSSOClientUtils.thirdBind(thirdChannelCode,appId,code,uid);
	}

	@Override
	public AppUser register(String thirdChannelCode, String appId, String code, String mobile,String password) throws Exception {
		//1、注册
		 String uid=CoreUserUtils.register(mobile,password);
		//2、绑定帐号
		 bind(thirdChannelCode,appId,code,uid);
		//3、乐家慧注册帐号
		AppUser appUser = new AppUser();
		appUser.setRelationFlag("0");
		appUser.setUserType("1");
		appUser.setRetTime(new Date());
		appUser.setMobile(mobile);
		appUser.setLastIp(null);
		appUser.setLastTime(new Date());
		appUser.setPassWord(SystemService.entryptPassword(StringUtils.getRandomCharAndNum(8)));
		appUser.setSourceSystemId(uid);
		appUser.setUpdateCorePasswordFlag(Constants.YES);
		appUserService.save(appUser);
		//4、异步更新用户二维码
		UserQRCodeHelper.updateUserQRCode(mobile);
		return appUser;
	}

	@Override
	public void unbind(String uid, String thirdChannelCode, String appId, String code) throws Exception {
		if("sina".equals(thirdChannelCode)||"sinaSdk".equals(thirdChannelCode)){
			CoreUserUtils.userThirdUnBind(uid,null,null,"sina");
			CoreUserUtils.userThirdUnBind(uid,null,null,"sinaSdk");
		}else {
			CoreUserUtils.userThirdUnBind(uid, null, null, thirdChannelCode);
		}
	}
}

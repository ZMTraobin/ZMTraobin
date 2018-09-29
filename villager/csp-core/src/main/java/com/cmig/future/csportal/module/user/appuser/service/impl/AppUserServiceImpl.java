package com.cmig.future.csportal.module.user.appuser.service.impl;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.ChineseToEnglish;
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
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.properties.community.dto.BaseCommunity;
import com.cmig.future.csportal.module.properties.community.service.IBaseCommunityService;
import com.cmig.future.csportal.module.sys.service.SystemService;
import com.cmig.future.csportal.module.sys.utils.UserTokenUtils;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.dto.AppUserAuth;
import com.cmig.future.csportal.module.user.appuser.help.UserQRCodeHelper;
import com.cmig.future.csportal.module.user.appuser.mapper.AppUserAuthMapper;
import com.cmig.future.csportal.module.user.appuser.mapper.AppUserMapper;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.cmig.future.csportal.module.user.appuser.service.IThirdUuidLoginService;
import com.cmig.future.csportal.module.user.attentionCommunity.dto.AttentionCommunityUser;
import com.cmig.future.csportal.module.user.attentionCommunity.mapper.AttentionCommunityUserMapper;
import com.cmig.future.csportal.module.user.attentionCommunity.util.CommunityHelper;
import com.cmig.future.csportal.module.villager.family.dto.FamilyInfo;
import com.cmig.future.csportal.module.villager.family.mapper.FamilyInfoMapper;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AppUserServiceImpl extends BaseServiceImpl<AppUser> implements IAppUserService{

	private static final Logger logger= LoggerFactory.getLogger(AppUserServiceImpl.class);

	public static final String APP_USER_CACHE_ID = "app.user.cache.id:";
	public static final String APP_USER_CACHE_MOBILE = "app.user.cache.mobile:";
	public static final String APP_USER_CACHE_OPEN_ID = "app.user.cache.openid:";

    @Autowired
    private AppUserMapper appUserMapper;

	@Autowired
	private AttentionCommunityUserMapper attentionCommunityUserMapper;

	@Autowired
	private AppUserAuthMapper appUserAuthMapper;

	@Autowired
    private FamilyInfoMapper familyInfoMapper;

	@Autowired
    private IBaseCommunityService communityService;

	@Autowired
	@Qualifier("casThirdUuidLoginService")
	private IThirdUuidLoginService thirdUuidLoginService;

    @Override
    public AppUser getByMobile(String mobile) {
	    AppUser appUser;
	    String appUserId=JedisUtils.get(APP_USER_CACHE_MOBILE+mobile);
	    if(!StringUtils.isEmpty(appUserId)){
		    appUser=get(appUserId);
	    }else{
		    appUser=appUserMapper.getByMobile(mobile);
		    cacheUser(appUser);
	    }
	    return appUser;
    }

    /**
     * 根据token获取用户
     * @param token
     * @return
     */
    public AppUser getByToken(String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        String appUserId = UserTokenUtils.getUserIdByToken(token);
        if(!StringUtils.isEmpty(appUserId)){
            return get(appUserId);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public void save(AppUser appUser) {
        if(!StringUtils.isEmpty(appUser.getId())){
            appUserMapper.updateByPrimaryKeySelective(appUser);
	        clearUserCache(appUser.getId());
        }else{
            appUser.setId(IdGen.uuid());
            super.mapper.insertSelective(appUser);
        }
    }

    @Override
    public void updateUserMobileInfo(AppUser appUser) {
        appUserMapper.updateUserMobileInfo(appUser);
	    clearUserCache(appUser.getId());
    }

    @Override
    public AppUser get(String appUserId) {
        if(!StringUtils.isEmpty(appUserId)){
	        AppUser appUser= (AppUser) JedisUtils.getObject(APP_USER_CACHE_ID+appUserId);
	        if(null==appUser){
		        appUser= appUserMapper.selectByPrimaryKey(appUserId);
		        cacheUser(appUser);
	        }
	        return appUser;
        }
        return null;
    }

    @Override
    public AppUser getBySourceSystemId(String openid) {
    	AppUser appUser;
    	String appUserId=JedisUtils.get(APP_USER_CACHE_OPEN_ID+openid);
    	if(!StringUtils.isEmpty(appUserId)){
		    appUser=get(appUserId);
	    }else{
		    appUser=appUserMapper.getBySourceSystemId(openid);
		    cacheUser(appUser);
	    }
        return appUser;
    }

	/**
	 * 缓存用户信息
	 * @param appUser
	 */
	private void cacheUser(AppUser appUser){
	    if(null!=appUser){
		    JedisUtils.setObject(APP_USER_CACHE_ID+appUser.getId(),appUser,0);
		    JedisUtils.set(APP_USER_CACHE_MOBILE+appUser.getMobile(),appUser.getId(),0);
		    JedisUtils.set(APP_USER_CACHE_OPEN_ID+appUser.getSourceSystemId(),appUser.getId(),0);
	    }
    }

	/**
	 * 清除缓存
	 * @param appUserId
	 */
	private void clearUserCache(String appUserId){
		if(!StringUtils.isEmpty(appUserId)){
			JedisUtils.del(APP_USER_CACHE_ID+appUserId);
		}
	}

    @Override
    public List<AppUser> findList(IRequest request, AppUser appUser, int page, int pagesize){
        PageHelper.startPage(page, pagesize);
        return appUserMapper.findList(appUser);
    }

    @Override
    public List<AppUser> appUserList(AppUser appUser){
        return appUserMapper.findList(appUser);
    }


	public JSONObject login(AppUser appUser, String tgt, String st) {
		JSONObject jsonObject=new JSONObject();
		String token = UserTokenUtils.createToken(appUser.getId(), tgt, st);
		jsonObject.put("token", token);
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
        List<AttentionCommunityUser> list = attentionCommunityUserMapper.findList(attentionCommunityUser);
        if (list != null && list.size() > 0) {
            AttentionCommunityUser thisAttentionCommunityUser = list.get(0);
            jsonObject.put("attentionCommunityId", thisAttentionCommunityUser.getCommunityId());
            jsonObject.put("attentionCommunityName", thisAttentionCommunityUser.getCommunityName());
        }

        //异步更新用户二维码
        UserQRCodeHelper.updateUserQRCode(appUser.getMobile());
		return jsonObject;
	}

    /**
     * 导入用户
     * @param mobile
     * @param userName
     * @return
     */
    public AppUser importUser(String mobile,String userName,String communityCode) {
        try {
            AppUser appUser= register(null,mobile,ChineseToEnglish.getPingYin(userName),null,null,null,null,userName,null);
            BaseCommunity baseCommunity=communityService.findListByCommunityCode(communityCode);
            if(null!=baseCommunity) {
                AttentionCommunityUser attentionCommunityUser = new AttentionCommunityUser();
                attentionCommunityUser.setUserId(appUser.getId());
                attentionCommunityUser.setIsAttention(CommunityHelper.IS_ATTENTION);
                attentionCommunityUser.setCommunityId(baseCommunity.getId());
                attentionCommunityUserMapper.insertSelective(attentionCommunityUser);
            }
            return appUser;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


	/**
	 * 用户注册
	 * @param ip
	 * @param mobile
	 * @param password
	 * @param registrationInvitationCode
	 * @param smsValidationCode
	 * @param uuid
	 * @param identityType
	 * @param nickName
	 * @param avatar
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false)
	public AppUser register(String ip,String mobile, String password, String registrationInvitationCode, String smsValidationCode, String uuid, String identityType, String nickName, String avatar) throws Exception {
		if (StringUtils.isEmpty(mobile)) {
			throw new DataWarnningException("手机号不能为空");
		}
        AppUser appUser = getByMobile(mobile);
		//核心校验手机号唯一性
		if (null!=appUser) {
			throw new DataWarnningException("该手机号已注册，请登录");
		}

		if (StringUtils.isEmpty(password)) {
			throw new DataWarnningException("密码不能为空");
		}
		/*if (smsValidationCode.isEmpty()) {
			throw new DataWarnningException("短信验证码不能为空");
		}
		if (!StringUtils.isEmpty(registrationInvitationCode)
				&& !RegExpValidatorUtils.isRegistrationInvitationCode(registrationInvitationCode)) {
			throw new DataWarnningException("邀请码必须是4位数字");
		}*/

		if (password.length() >= 128) {
			// RSA私钥解密
			password = RSAUtilsWithKey.decryptString(password);
		}
		/*if (!RegExpValidatorUtils.IsPassword(password)) {
			throw new DataWarnningException("密码必须是数字和字母组合，长度为6-20位");
		}*/

		// redis code key
		/*String telnoKey = SmsUtil.SMS_VALIDATE_CODE +SmsUtil.SMS_CODE_REGISTER+"." + mobile;
		SmsUtil.checkSmsCode(mobile, telnoKey, smsValidationCode);*/
		//调用核心用户注册
		//String uid = CoreUserUtils.register(mobile, password);
		//乐家慧本地注册
		if (appUser == null) {
			appUser = newAppUser(ip, mobile, password, registrationInvitationCode, nickName, avatar, null);
		}

		if(null!=appUser&&!StringUtils.isEmpty(uuid)&&!StringUtils.isEmpty(identityType)){
			//绑定第三方uuid和该手机号用户
			AppUserAuth appUserAuth=new AppUserAuth();
			appUserAuth.setUuid(uuid);
			appUserAuth.setIdentityType(identityType);
			appUserAuth.setAppUserId(appUser.getId());
			if(!StringUtils.isEmpty(avatar)) {
				appUserAuth.setAvatar(avatar);
			}
			if(!StringUtils.isEmpty(nickName)) {
				appUserAuth.setNickName(nickName);
			}
			appUserAuth.setAuthId(IdGen.uuid());
			appUserAuthMapper.insertSelective(appUserAuth);
		}
		return appUser;
	}

	@Override
	@Transactional(readOnly = false)
	public AppUser newAppUser(String mobile,String uid){
		return newAppUser(null,mobile,null,null,null,null,uid);
	}

    /**
     * 实名认证
     * @param appUser
     * @param idcard
     * @param userName
     */
    @Override
    @Transactional
    public void oauth(AppUser appUser, String idcard, String userName) {
        FamilyInfo familyInfo=new FamilyInfo();
        familyInfo.setIdcard(idcard);
        familyInfo.setUserName(userName);
        List<FamilyInfo> list= familyInfoMapper.findByIdcardAndName(familyInfo);
        if(StringUtils.isEmpty(list)){
            throw new ServiceException(RestApiExceptionEnums.OAUTH_FAIL);
        }else{
            appUserMapper.updateRelationFlag(appUser.getId(),Constants.YES);
        }
    }

    @Override
    @Transactional
    public AppUser newAppUser(String ip, String mobile, String password, String registrationInvitationCode, String nickName, String avatar, String uid) {
        return newAppUser(ip,mobile,password,registrationInvitationCode,nickName,avatar,uid,null);
    }

    @Override
	@Transactional
	public AppUser newAppUser(String ip, String mobile, String password, String registrationInvitationCode, String nickName, String avatar, String uid,String userName) {
		AppUser appUser= new AppUser();
		appUser.setRelationFlag(Constants.RELATIONS_ID_N);
		appUser.setUserType(Constants.APP_USER_TYPE_ONE);
		appUser.setRetTime(new Date());
		appUser.setMobile(mobile);
		appUser.setLastIp(ip);
		appUser.setLastTime(new Date());
		if(StringUtils.isEmpty(password)){
			password=StringUtils.getRandomCharAndNum(8);
		}
		appUser.setPassWord(SystemService.entryptPassword(password));
		appUser.setRegistrationInvitationCode(registrationInvitationCode);
		appUser.setSourceSystemId(uid);
		appUser.setUpdateCorePasswordFlag(Constants.YES);
		appUser.setSignCollectionAgreementFlag(Constants.NO);
		if(!StringUtils.isEmpty(nickName)){
			appUser.setNickName(nickName);
            if(StringUtils.isEmpty(userName)){
                appUser.setUserName(nickName);
            }
		}
        if(!StringUtils.isEmpty(userName)){
            appUser.setUserName(userName);
            if(StringUtils.isEmpty(nickName)){
                appUser.setNickName(userName);
            }
        }
		if(!StringUtils.isEmpty(avatar)){
			appUser.setUserIcon(avatar);
		}
		save(appUser);

		//异步更新用户二维码
		UserQRCodeHelper.updateUserQRCode(mobile);

		//MobileUtil.updateUserMobileInfo(appUser);//异步更新用户手机号归属地信息
		return appUser;
	}

	@Override
	@Transactional
	public AppUser register(String ip, String mobile, String password, String registrationInvitationCode, String smsValidationCode, String thirdChannelCode, String appId, String code) throws Exception {
		if (mobile.isEmpty()) {
			throw new DataWarnningException("手机号不能为空");
		}
		//核心校验手机号唯一性
		if (CoreUserUtils.validationMobile(mobile)) {
			throw new DataWarnningException("该手机号已注册，请直接登录");
		}

		if (password.isEmpty()) {
			throw new DataWarnningException("密码不能为空");
		}
		if (smsValidationCode.isEmpty()) {
			throw new DataWarnningException("短信验证码不能为空");
		}
		if (!StringUtils.isEmpty(registrationInvitationCode)
				&& !RegExpValidatorUtils.isRegistrationInvitationCode(registrationInvitationCode)) {
			throw new DataWarnningException("邀请码必须是4位数字");
		}

		if (password.length() >= 128) {
			// RSA私钥解密
			password = RSAUtilsWithKey.decryptString(password);
		}
		if (!RegExpValidatorUtils.IsPassword(password)) {
			throw new DataWarnningException("密码必须是数字和字母组合，长度为6-20位");
		}

		// redis code key
		String telnoKey = SmsUtil.SMS_VALIDATE_CODE +SmsUtil.SMS_CODE_REGISTER+"." + mobile;
		SmsUtil.checkSmsCode(mobile, telnoKey, smsValidationCode);
		//调用核心用户注册
		String uid = CoreUserUtils.register(mobile, password);
		//乐家慧本地注册
		AppUser appUser = getByMobile(mobile);
		if (appUser == null) {
			appUser=newAppUser(ip,mobile,password,registrationInvitationCode,null,null,uid);
		}

		if(null!=appUser&&!StringUtils.isEmpty(thirdChannelCode)&&!StringUtils.isEmpty(appId)&&!StringUtils.isEmpty(code)){
			//绑定第三方uuid和该手机号用户
			thirdUuidLoginService.bind(thirdChannelCode,appId,code,uid);
		}
		return appUser;
	}


	@Override
	public void loginOut(String token) {
		UserTokenUtils.cleanToken(token);
	}


	@Override
	public JSONObject singleLoginByTgt(String tgt) throws Exception {
		String st= CoreSSOClientUtils.getServiceTicket(tgt);
		if(StringUtils.isEmpty(st)){
			throw  new ServiceException(RestApiExceptionEnums.CAS_TGT_IS_EXPIRED);
		}
		String uid=CoreSSOClientUtils.proxyValidate(st);
		AppUser appUser=getBySourceSystemId(uid);
		if(null==appUser){
			String mobile=CoreUserUtils.getMobile(uid);
			appUser=newAppUser(mobile,uid);
		}
		return login(appUser,tgt,st);
	}
}
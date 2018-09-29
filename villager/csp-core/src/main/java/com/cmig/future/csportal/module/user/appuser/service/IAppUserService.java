package com.cmig.future.csportal.module.user.appuser.service;

import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import net.sf.json.JSONObject;

import java.util.List;

public interface IAppUserService extends IBaseService<AppUser>, ProxySelf<IAppUserService>{

    AppUser getByMobile(String mobile);

    AppUser getByToken(String token);

    void save(AppUser appUser);

    void updateUserMobileInfo(AppUser appUser);

    AppUser get(String appUserId);

    AppUser getBySourceSystemId(String openid);

    List<AppUser> findList(IRequest requestCtx, AppUser appUser, int page, int pagesize);

    List<AppUser> appUserList(AppUser appUser);

	JSONObject login(AppUser appUser, String tgt, String st);

    /**
     * 导入用户
     * @param mobile
     * @param userName
     * @param communityCode
     * @return
     * @throws Exception
     */
    AppUser importUser(String mobile,String userName,String communityCode);
	AppUser register(String ip,String mobile,String password,String registrationInvitationCode,String smsValidationCode,String uuid,String identityType,String nickName,String avatar)throws Exception;
	AppUser register(String ip,String mobile,String password,String registrationInvitationCode,String smsValidationCode,String thirdChannelCode,String appId,String code)throws Exception;

	void loginOut(String token);

	JSONObject singleLoginByTgt(String tgt) throws Exception;

    AppUser newAppUser(String ip, String mobile, String password, String registrationInvitationCode, String nickName, String avatar, String uid) throws Exception;

	AppUser newAppUser(String ip, String mobile, String password, String registrationInvitationCode, String nickName, String avatar, String uid,String userName) throws Exception;

	AppUser newAppUser(String mobile, String uid)throws Exception;

    /**
     * 实名认证
     * @param appUser
     * @param idcard
     * @param userName
     */
    void oauth(AppUser appUser, String idcard, String userName);
}
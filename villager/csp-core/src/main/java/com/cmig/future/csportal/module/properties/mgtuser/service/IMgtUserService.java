package com.cmig.future.csportal.module.properties.mgtuser.service;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.module.properties.community.dto.BaseCommunity;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUser;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author buzl
 */
public interface IMgtUserService extends IBaseService<MgtUser>, ProxySelf<IMgtUserService> {


	/**
	 * 根据userid获取用户
	 *
	 * @param userId
	 * @return
	 */
	MgtUser getUser(String userId);

	/**
	 * 根据手机号获取手机的使用用户
	 *
	 * @param mobile
	 * @return
	 */
	MgtUser getUserByMobile(String mobile);

	/**
	 * 根据邮件查出用户
	 *
	 * @param email
	 * @return
	 */
	MgtUser getUserByEmail(String email);

	/**
	 * 保存用户
	 *
	 * @param mgtUser
	 */
	void inertMgtUser(MgtUser mgtUser);

	/**
	 * 更新用户
	 *
	 * @param mgtUser
	 */
	void update(MgtUser mgtUser);


	void updateUserLoginInfo(MgtUser mgtUser);

	/**
	 * 查询所有的物业员用户
	 *
	 * @return
	 */
	List<MgtUser> selectMgtUser(IRequest requestContext, MgtUser dto, int page, int pageSize);

	/**
	 * 根据id查询出详细的用户信息
	 */
	MgtUser getMgtUserById(String id);

	/**
	 * 批量更新物业数据
	 *
	 * @param map
	 */
	void updateBathMgtUser(Map<String, Object> map);

	/**
	 * 修改物管云系统密码
	 *
	 * @param mgtUser
	 * @param newPassword
	 */
	void synMgtUserPassword(MgtUser mgtUser, String newPassword);

	/**
	 * 失败重试定时任务
	 */
	void synMgtUserPasswordRetryJob();

	/**
	 * 物管云用户新增同步
	 * @param mgtUser
	 * @param request
	 * @throws Exception
	 */
	void synMgtUserAdd(MgtUser mgtUser, HttpServletRequest request) throws Exception;

	/**
	 * 物管云用户修改同步
	 * @param mgtUser
	 * @param request
	 * @throws Exception
	 */
	void synMgtUserUpdate(MgtUser mgtUser, HttpServletRequest request) throws Exception;

	/**
	 * 修改密码
	 * @param mgtUser
	 * @param password
	 */
	void updatePassword(MgtUser mgtUser, String password);

	void loginOut(String token);

	/**
	 * 获取物管云token
	 * @param community
	 * @param mgtUserId
	 * @return
	 * @throws Exception
	 */
	String getSourceToken(BaseCommunity community, String mgtUserId) throws Exception;

	JSONObject login(String mobile, String password)throws Exception;

	JSONObject sslogin(String mobile) throws Exception;

    /**
     * 新增站长
     * @param dto
     * @return
     */
    void add(MgtUser dto);
}
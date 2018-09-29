/**
 *
 */
package com.cmig.future.csportal.module.properties.mgtuser.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.exception.IncorrectCredentialsException;
import com.cmig.future.csportal.common.exception.UnknownAccountException;
import com.cmig.future.csportal.common.oauth2.OAuthConstants;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.common.utils.MD5;
import com.cmig.future.csportal.common.utils.RegExpValidatorUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.pay.conf.FailRetryHelper;
import com.cmig.future.csportal.module.properties.community.dto.BaseCommunity;
import com.cmig.future.csportal.module.properties.community.mapper.BaseCommunityMapper;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUser;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUserCommunity;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUserSyn;
import com.cmig.future.csportal.module.properties.mgtuser.help.LoginFlag;
import com.cmig.future.csportal.module.properties.mgtuser.mapper.MgtUserCommunityMapper;
import com.cmig.future.csportal.module.properties.mgtuser.mapper.MgtUserMapper;
import com.cmig.future.csportal.module.properties.mgtuser.mapper.MgtUserSynMapper;
import com.cmig.future.csportal.module.properties.mgtuser.service.IMgtUserService;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import com.cmig.future.csportal.module.sys.service.SystemService;
import com.cmig.future.csportal.module.sys.utils.AdminTokenUtils;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class MgtUserServiceImpl extends BaseServiceImpl<MgtUser> implements IMgtUserService {

	private static final Logger logger = LoggerFactory.getLogger(MgtUserServiceImpl.class);

	@Autowired
	private MgtUserMapper mgtUserMapper;
	@Autowired
	private MgtUserSynMapper mgtUserSynMapper;
	@Autowired
	private MgtUserCommunityMapper mgtUserCommunityMapper;
	@Autowired
	private BaseCommunityMapper baseCommunityMapper;

	@Override
	public MgtUser getUser(String id) {
		MgtUser mgtUser = new MgtUser();
		mgtUser.setId(id);
		mgtUser.setDelFlag("0");
		return mgtUserMapper.getMgtUserById(mgtUser);
	}

	/**
	 * 根据手机号查询出用户
	 */
	@Override
	public MgtUser getUserByMobile(String mobile) {
		MgtUser mgtUser = new MgtUser();
		mgtUser.setMobile(mobile);
		return mgtUserMapper.getUserByMobile(mgtUser);
	}

	@Override
	public MgtUser getUserByEmail(String email) {
		MgtUser mgtUser = new MgtUser();
		mgtUser.setEmail(email);
		return mgtUserMapper.getUserByEmail(mgtUser);
	}

	@Override
	@Transactional
	public void inertMgtUser(MgtUser mgtUser) {
		insertMgtUserSyn(mgtUser);
	}

	public void update(MgtUser mgtUser) {
		super.mapper.updateByPrimaryKeySelective(mgtUser);
	}

	@Override
	public void updateUserLoginInfo(MgtUser mgtUser) {
		mgtUserMapper.updateUserLoginInfo(mgtUser);
	}

	/**
	 * 查询所有的用户
	 *
	 * @return
	 */
	@Override
	public List<MgtUser> selectMgtUser(IRequest requestContext, MgtUser dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return mgtUserMapper.selectMgtUser(dto);
	}

	/**
	 * 根据用户id查询出物业用户的详细信息
	 *
	 * @param id
	 * @return
	 */
	@Override
	public MgtUser getMgtUserById(String id) {
		MgtUser mgtUser = new MgtUser();
		mgtUser.setId(id);
		return mgtUserMapper.getMgtUserById(mgtUser);
	}

	/**
	 * 批量删除物业用户，也就是进行软删除
	 *
	 * @param map
	 */
	@Override
	public void updateBathMgtUser(Map<String, Object> map) {
		mgtUserMapper.updateBathMgtUser(map);
	}

	/**
	 * 事物控制用户插入
	 */
	public void insertMgtUserSyn(MgtUser mgtUser) {
		mgtUser.setId(IdGen.uuid());
		mgtUser.setLoginFlag(LoginFlag.enable.getCode());//默认启用,帐号状态 “启用”代表此账号允许登录，“停用”则表示此账号不允许登录
		super.mapper.insertSelective(mgtUser);
		MgtUserSyn mgtUserSyn = new MgtUserSyn();
		mgtUserSyn.setId(IdGen.uuid());
		mgtUserSyn.setSourceSystem(mgtUser.getSourceSystem());
		mgtUserSyn.setSourceSystemId(mgtUser.getSourceSystemId());
		mgtUserSyn.setUserId(mgtUser.getId());
		mgtUserSynMapper.insertMgtUserSyn(mgtUserSyn);
	}


	//检测email
	private boolean checkEmail(String email) {
		if (StringUtils.isEmpty(email)) {
			return true;
		} else {
			MgtUser user = getUserByEmail(email);
			if (user == null) {
				return true;
			} else if ("1".equals(user.getDelFlag())) {
				return true;
			}
		}
		return false;
	}

	//检测手机号
	public boolean checkMobile(String mobile) {
		if (StringUtils.isEmpty(mobile)) {
			return true;
		} else {
			MgtUser user = getUserByMobile(mobile);
			if (user == null) {
				return true;
			} else if ("1".equals(user.getDelFlag())) {
				return true;
			}
		}
		return false;
	}


	//检测系统id,查看是否有相同的记录
	public boolean checkSourceSystemId(OpenAppInfo openAppInfo, String sourceSystemId) {
		MgtUserSyn mgtUserSyn = new MgtUserSyn();
		mgtUserSyn.setSourceSystem(openAppInfo.getAppName());
		mgtUserSyn.setSourceSystemId(sourceSystemId);
		List<MgtUserSyn> list = mgtUserSynMapper.checkSourceAndSystemId(mgtUserSyn);
		if (null == list || list.size() == 0) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * 物管员工新增同步
	 * @param mgtUser
	 * @param request
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public void synMgtUserAdd(MgtUser mgtUser, HttpServletRequest request) throws Exception {
		final String appId = request.getParameter(OAuthConstants.OAUTH_APP_ID);
		String birthday = request.getParameter("birthday");
		//获取源系统ID
		String sourceSystemId = request.getParameter("sourceSystemId");
		String password = request.getParameter("password");
		// 校验appid
		OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appId);
		//源系统id不能为空,并且源系统ID不能重复
		if (StringUtils.isEmpty(sourceSystemId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"源系统id");
		} else if (!checkSourceSystemId(openAppInfo, sourceSystemId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"该用户已经存在，不需要重复新增");
		}

		//从随机密码改为同步密码
		if (StringUtils.isEmpty(password)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"密码");
		}

		//手机号不能为空并且不能重复
		if (StringUtils.isEmpty(mgtUser.getMobile())) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"手机号");
		} else if (!RegExpValidatorUtils.checkMobile(mgtUser.getMobile())) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"手机号格式不正确，请输入11位数字");
		} else if (!checkMobile(mgtUser.getMobile())) {
			//throw new DataWarnningException("该用户已经存在，不需要重复新增");
			//不抛出异常，记录源系统id与系统id映射关系
			mgtUser = getUserByMobile(mgtUser.getMobile());
			if (!password.equals(mgtUser.getPassword())) {
				mgtUser.setPassword(password);
				mgtUserMapper.updateByPrimaryKeySelective(mgtUser);
				synMgtUserPassword(mgtUser,mgtUser.getPassword());
			}
			MgtUserSyn mgtUserSyn = new MgtUserSyn();
			mgtUserSyn.setId(IdGen.uuid());
			mgtUserSyn.setSourceSystem(openAppInfo.getAppName());
			mgtUserSyn.setSourceSystemId(sourceSystemId);
			mgtUserSyn.setUserId(mgtUser.getId());
			mgtUserSynMapper.insertMgtUserSyn(mgtUserSyn);
		}else {
			if (StringUtils.isEmpty(mgtUser.getName())) {
				throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"员工姓名");
			}
			// modify by zhangtao 证件类型和证件号不做校验
		    /*if (StringUtils.isEmpty(mgtUser.getIdcard())) {
	            throw new DataWarnningException("身份证号不能为空");
	        } else if (!RegExpValidatorUtils.checkIdCard(mgtUser.getIdcard())) {
	            throw new DataWarnningException("身份证号格式不正确");
	        }*/
			if (!StringUtils.isEmpty(mgtUser.getSex()) && !(("M").equals(mgtUser.getSex()) || ("F").equals(mgtUser.getSex()))) {
				throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"性别参数不正确，M：男；F：女； ");
			}
			if (!StringUtils.isEmpty(mgtUser.getEmail()) && !RegExpValidatorUtils.checkEmail(mgtUser.getEmail())) {
				throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"邮箱格式不正确");
			} else if (!StringUtils.isEmpty(mgtUser.getEmail()) && !checkEmail(mgtUser.getEmail())) {
				throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"该邮箱已存在");
			}
			if (!StringUtils.isEmpty(birthday)) {
				try {
					mgtUser.setBirthTime(DateUtils.parseDate(birthday, "yyyy-MM-dd"));
					mgtUser.setAge(new Integer(DateUtils.getAge(mgtUser.getBirthTime())).toString());
				} catch (ParseException e) {
					throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"生日格式不正确，数据格式：YYYY-MM-dd");
				} catch (IllegalArgumentException e) {
					throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"生日格式不正能大于系统当前时间");
				}
			}

			mgtUser.setSourceSystem(openAppInfo.getAppName());
			if (!StringUtils.isEmpty(sourceSystemId)) {
				mgtUser.setSourceSystemId(sourceSystemId);
			}
			/**
			 * 保存用户和用户同步之间的关系
			 */
			//String password = StringUtils.getRandomNum(6);// 生产6位随机数作为密码
			//mgtUser.setPassword(SystemService.entryptPassword(password));
			inertMgtUser(mgtUser);
	        /*logger.debug("您好，欢迎使用中民物管APP，您的初始账号为:" + mgtUser.getMobile() + "，初始密码 ******,请尽快登录中民物管APP修改初始密码，确保您的信息安全。祝您工作顺利。");
	        String content = "您好，欢迎使用中民物管APP，您的初始账号为:" + mgtUser.getMobile() + "，初始密码" + password + ",请尽快登录中民物管APP修改初始密码，确保您的信息安全。祝您工作顺利。";
	        SmsUtil.send(content,mgtUser.getMobile());*/
		}
	}

	/**
	 * 物管云员工修改同步
	 * @param mgtUser
	 * @param request
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public void synMgtUserUpdate(MgtUser mgtUser, HttpServletRequest request) throws Exception {
		final String appId = request.getParameter(OAuthConstants.OAUTH_APP_ID);
		String birthday = request.getParameter("birthday");
		//获取源系统ID
		String sourceSystemId = request.getParameter("sourceSystemId");
		// 校验appid
		OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appId);
		if (StringUtils.isEmpty(sourceSystemId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"源系统id");
		}

		//从随机密码改为同步密码
		if (StringUtils.isEmpty(mgtUser.getPassword())) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"密码");
		}

		MgtUserSyn mgtUserSyn = new MgtUserSyn();
		mgtUserSyn.setSourceSystem(openAppInfo.getAppName());
		mgtUserSyn.setSourceSystemId(sourceSystemId);
		List<MgtUserSyn> list = mgtUserSynMapper.checkSourceAndSystemId(mgtUserSyn);
		if (StringUtils.isEmpty(list)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"该用户不存在，请先同步用户信息");
		}
		mgtUserSyn = list.get(0);
		MgtUser userQuery = getUser(mgtUserSyn.getUserId());
		if (null == userQuery) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"该用户不存在，请先同步用户信息");
		}
		logger.debug("参数 原手机号:{} 新手机号:{} 系统来源：{} 系统来源ID:{} ", userQuery.getMobile(), mgtUser.getMobile(), openAppInfo.getAppName(), sourceSystemId);
		if (StringUtils.isEmpty(mgtUser.getMobile())) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"手机号");
		} else if (!RegExpValidatorUtils.checkMobile(mgtUser.getMobile())) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"手机号格式不正确，请输入11为数字");
		} else if (!checkMobile(mgtUser.getMobile()) && !mgtUser.getMobile().equals(userQuery.getMobile())) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"该手机号已存在");
		}
		if (StringUtils.isEmpty(mgtUser.getName())) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"员工姓名");
		}
		// modify by zhangtao 证件类型和证件号不做校验
        /*if (StringUtils.isEmpty(mgtUser.getIdcard())) {
            throw new DataWarnningException("身份证号不能为空");
        } else if (!RegExpValidatorUtils.checkIdCard(mgtUser.getIdcard())) {
            throw new DataWarnningException("身份证号格式不正确");
        }*/
		if (!StringUtils.isEmpty(mgtUser.getSex()) && !(("M").equals(mgtUser.getSex()) || ("F").equals(mgtUser.getSex()))) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"性别参数不正确，M：男；F：女； ");
		}
		if (!StringUtils.isEmpty(mgtUser.getEmail()) && !RegExpValidatorUtils.checkEmail(mgtUser.getEmail())) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"邮箱格式不正确");
		} else if (!StringUtils.isEmpty(mgtUser.getEmail()) && !checkEmail(mgtUser.getEmail()) && !mgtUser.getEmail().equals(userQuery.getEmail())) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"该邮箱已存在");
		}
		if (!StringUtils.isEmpty(birthday)) {
			try {
				mgtUser.setBirthTime(DateUtils.parseDate(birthday, "yyyy-MM-dd"));
				mgtUser.setAge(new Integer(DateUtils.getAge(mgtUser.getBirthTime())).toString());
			} catch (ParseException e) {
				throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"生日格式不正确，数据格式：YYYY-MM-dd");
			} catch (IllegalArgumentException e) {
				throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"生日格式不正确，不能大于系统当前时间");
			}
		}

		userQuery.setIdentityType(mgtUser.getIdentityType());
		userQuery.setIdcard(mgtUser.getIdcard());
		userQuery.setCompanyName(mgtUser.getCompanyName());
		userQuery.setName(mgtUser.getName());
		userQuery.setBirthTime(mgtUser.getBirthTime());
		userQuery.setAge(mgtUser.getAge());
		userQuery.setSex(mgtUser.getSex());
		userQuery.setLoginName(mgtUser.getMobile()); // 默认手机号
		userQuery.setMobile(mgtUser.getMobile());
		if (StringUtils.isEmpty(mgtUser.getLoginFlag())) {
			mgtUser.setLoginFlag(Constants.YES);
		}
		if (!userQuery.getPassword().equals(mgtUser.getPassword())) {
			synMgtUserPassword(userQuery, mgtUser.getPassword());
		}
		userQuery.setPassword(mgtUser.getPassword());
		userQuery.setLoginFlag(Constants.YES.equals(mgtUser.getLoginFlag()) ? "1" : "0");// 帐号状态 “启用”代表此账号允许登录，“停用”则表示此账号不允许登录
		//源系统信息更新为最后更新的来源系统
		userQuery.setSourceSystem(openAppInfo.getAppName());
		userQuery.setSourceSystemId(sourceSystemId);
		update(userQuery);
	}

	@Override
	public void synMgtUserPassword(MgtUser mgtUser, String newPassword) {
		MgtUserSyn mgtUserSyn = new MgtUserSyn();
		mgtUserSyn.setUserId(mgtUser.getId());
		List<MgtUserSyn> mgtUserSynList = mgtUserSynMapper.findList(mgtUserSyn);
		String sourceSystemIds = "[";
		if (null != mgtUserSynList && mgtUserSynList.size() > 0) {
			for (MgtUserSyn entry : mgtUserSynList) {
				sourceSystemIds += "\"" + entry.getSourceSystemId() + "\",";
			}
			if (!StringUtils.isEmpty(sourceSystemIds)) {
				sourceSystemIds = sourceSystemIds.substring(0, sourceSystemIds.length() - 1);
			}
		}
		sourceSystemIds += "]";

		if (!StringUtils.isEmpty(sourceSystemIds)) {
			MgtUserCommunity mgtUserCommunity = new MgtUserCommunity();
			mgtUserCommunity.setMgtUserId(mgtUser.getId());
			List<MgtUserCommunity> list = mgtUserCommunityMapper.findCommunityListByMgtUserId(mgtUserCommunity);
			if (null != list && list.size() > 0) {
				Map<String,Object> serverMap=new HashMap();
				for (MgtUserCommunity entry : list) {
					if(!StringUtils.isEmpty(entry.getServerUrl())) {
						serverMap.put(entry.getServerUrl(), entry.getServerUrl());
					}
				}

				for(Map.Entry<String,Object> entry:serverMap.entrySet()){
					updateMgtUserPassword(entry.getKey(), sourceSystemIds, newPassword);
				}
			}
		}
	}

	/**
	 * 调用物管云修改密码接口失败记录redis key
	 */
	private static final String CSP_MGT_USER_PASSWORD_SYN_ERROR_MAP = "CSP.MGT.USER.PASSWORD.SYN.ERROR.MAP";

	/**
	 * 调用物管云修改密码接口失败重传job状态key
	 */
	private static final String CSP_MGT_USER_PASSWORD_SYN_ERROR_LIST_RETRY_JOB_STATUS = "CSP.MGT.USER.PASSWORD.SYN.ERROR.LIST.RETRY.JOB.STATUS";

	public void updateMgtUserPassword(String serverUrl, String sourceSystemIds, String password) {
		Map map = new HashMap();
		try {
			logger.debug("serverUrl {} sourceSystemIds {} password {} ", serverUrl, sourceSystemIds, password);
			map.put("serverUrl", serverUrl);
			map.put("sourceSystemIds", sourceSystemIds);
			map.put("password", password);
			map.put("createTime", new Date());
			map.put("times", 0);

			Map paramMap = new HashMap();
			paramMap.put("bp_ids", sourceSystemIds);
			paramMap.put("password", password);
			String response = HttpUtil.post(serverUrl + "/api/v1/user_info_datas/update_user_password", paramMap);
			JSONObject jsonObject = JSONObject.parseObject(response);
			if (null != jsonObject && "200".equals(jsonObject.get("result") == null ? "" : jsonObject.get("result").toString())) {
				logger.debug("密码修改成功 serverUrl {} sourceSystemIds {} password {} ", serverUrl, map.get("sourceSystemIds").toString(), map.get("password").toString());
			} else {
				logger.debug("密码修改失败 serverUrl {} sourceSystemIds {} password {} ", serverUrl, map.get("sourceSystemIds").toString(), map.get("password").toString());
				cacheTaskInRedis(serverUrl, sourceSystemIds, password, map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("密码修改失败 serverUrl {} sourceSystemIds {} password {} ", serverUrl, map.get("sourceSystemIds").toString(), map.get("password").toString());
			cacheTaskInRedis(serverUrl, sourceSystemIds, password, map);
		}
	}

	private void cacheTaskInRedis(String serverUrl, String sourceSystemIds, String password, Map map) {
		if (JedisUtils.tryLock(CSP_MGT_USER_PASSWORD_SYN_ERROR_MAP, 35, TimeUnit.SECONDS)) {
			Map<String, Object> failMap = JedisUtils.getObjectMap(CSP_MGT_USER_PASSWORD_SYN_ERROR_MAP);
			String key = MD5.MD5Encode(serverUrl + "_" + sourceSystemIds + "_" + password);
			if (null != failMap) {
				map.put("times", new Integer(map.get("times").toString()) + 1);
				failMap.put(key, map);
			} else {
				failMap = new LinkedHashMap<>();
				map.put("times", new Integer(map.get("times").toString()) + 1);
				failMap.put(key, map);
			}
			JedisUtils.setObjectMap(CSP_MGT_USER_PASSWORD_SYN_ERROR_MAP, failMap, 0);
			JedisUtils.unLock(CSP_MGT_USER_PASSWORD_SYN_ERROR_MAP);
		}
	}

	@Override
	public void synMgtUserPasswordRetryJob() {
		Map<String, Object> failMap = JedisUtils.getObjectMap(CSP_MGT_USER_PASSWORD_SYN_ERROR_MAP);
		Map<String, Object> thisFailMap = new LinkedHashMap<>();
		if (null != failMap) {
			for (Map.Entry<String, Object> entry : failMap.entrySet()) {
				Map map = (Map) entry.getValue();
				int times = new Integer(map.get("times").toString());
				Date createTime = (Date) map.get("createTime");
				String sourceSystemIds = map.get("sourceSystemIds").toString();
				String password = map.get("password").toString();

				// 下次通知时间
				Date timeNextNotify = DateUtils.addSeconds(createTime, FailRetryHelper.getNextTimeSeconds(times));
				if (timeNextNotify.compareTo(createTime) >= 0) {
					String serverUrl = map.get("serverUrl").toString();
					Map paramMap = new HashMap();
					paramMap.put("bp_ids", sourceSystemIds);
					paramMap.put("password", password);
					try {
						String response = HttpUtil.post(serverUrl + "/api/v1/user_info_datas/update_user_password", paramMap);
						JSONObject jsonObject = JSONObject.parseObject(response);
						if (null != jsonObject && "200".equals(jsonObject.get("result") == null ? "" : jsonObject.get("result").toString())) {
							logger.debug("密码修改成功 serverUrl {} sourceSystemIds {} password {} ", sourceSystemIds, password);
						} else {
							logger.debug("密码修改失败 serverUrl {} sourceSystemIds {} password {} ", sourceSystemIds.toString(), password);
							String key = MD5.MD5Encode(serverUrl + "_" + sourceSystemIds + "_" + password);
							map.put("times", new Integer(map.get("times").toString()) + 1);
							thisFailMap.put(key, map);
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.debug("密码修改失败 serverUrl {} sourceSystemIds {} password {} ", sourceSystemIds.toString(), password);
						String key = MD5.MD5Encode(serverUrl + "_" + sourceSystemIds + "_" + password);
						map.put("times", new Integer(map.get("times").toString()) + 1);
						thisFailMap.put(key, map);
					}
				}
			}
			//拿到锁之后再重新覆盖值
			if (JedisUtils.tryLock(CSP_MGT_USER_PASSWORD_SYN_ERROR_MAP, 35, TimeUnit.SECONDS)) {
				Map<String, Object> currentFailMap = JedisUtils.getObjectMap(CSP_MGT_USER_PASSWORD_SYN_ERROR_MAP);
				currentFailMap.putAll(thisFailMap);
				JedisUtils.setObjectMap(CSP_MGT_USER_PASSWORD_SYN_ERROR_MAP, currentFailMap, 0);
				//使用完之后解锁
				JedisUtils.unLock(CSP_MGT_USER_PASSWORD_SYN_ERROR_MAP);
			}
		}
	}

	@Override
	public void updatePassword(MgtUser mgtUser, String password) {
		mgtUser.setPassword(SystemService.entryptMgtPassword(password));
		mgtUserMapper.updateByPrimaryKeySelective(mgtUser);
		synMgtUserPassword(mgtUser,mgtUser.getPassword());
	}

	@Override
	public void loginOut(String token) {
		AdminTokenUtils.cleanToken(token);
	}

	/**
	 * 获取物管云token
	 *
	 * @param community
	 * @param mgtUserId
	 * @return
	 */
	public String getSourceToken(BaseCommunity community, String mgtUserId) throws Exception {
		if (null != community && !StringUtils.isEmpty(community.getServerUrl())) {
			MgtUserCommunity mgtUserCommunityQuery=new MgtUserCommunity();
			mgtUserCommunityQuery.setSourceSystem(community.getSourceSystem());
			mgtUserCommunityQuery.setSourceSystemCommunityId(community.getSourceSystemId());
			mgtUserCommunityQuery.setMgtUserId(mgtUserId);
			List<MgtUserCommunity> list= mgtUserCommunityMapper.getAllUserCommunity(mgtUserCommunityQuery);
			if(!StringUtils.isEmpty(list)){
				mgtUserCommunityQuery=list.get(0);
			}else{
				return null;
			}
			String url = community.getServerUrl() + "/api/v1/app_versions/get_token?bp_id=" + mgtUserCommunityQuery.getSourceMgtUserId() + "&t=" + System.currentTimeMillis();

			Map headerMap = new HashMap();
			headerMap.put("Accept", "*/*");
			String result = HttpUtil.get(url, headerMap);
			if (!StringUtils.isEmpty(result)) {
				net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(result);
				if (null != jsonObject && "200".equals(jsonObject.get("status_code").toString())) {
					String token = jsonObject.get("token").toString();
					return token;
				}
			}
		}
		return null;
	}

	@Override
	public JSONObject login(String mobile, String password)throws Exception {
		MgtUser mgtUser = getUserByMobile(mobile);
		if (null == mgtUser) {
			throw new UnknownAccountException();
		} else if (!SystemService.validateMgtPassword(password, mgtUser.getPassword())) {
			throw new IncorrectCredentialsException();
		}
		return sslogin(mobile);
	}

	@Override
	public JSONObject sslogin(String mobile) throws Exception {
		MgtUser mgtUser = getUserByMobile(mobile);
		return sslogin(mgtUser);
	}

    @Override
    public void add(MgtUser mgtUser) {
        //手机号不能为空并且不能重复
        if (StringUtils.isEmpty(mgtUser.getMobile())) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"手机号");
        } else if (!RegExpValidatorUtils.checkMobile(mgtUser.getMobile())) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"手机号格式不正确，请输入11位数字");
        }else if (!checkMobile(mgtUser.getMobile())) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"该手机号已存在");
        }else {
            if (StringUtils.isEmpty(mgtUser.getName())) {
                throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"员工姓名");
            }
            // modify by zhangtao 证件类型和证件号不做校验
		    /*if (StringUtils.isEmpty(mgtUser.getIdcard())) {
	            throw new DataWarnningException("身份证号不能为空");
	        } else if (!RegExpValidatorUtils.checkIdCard(mgtUser.getIdcard())) {
	            throw new DataWarnningException("身份证号格式不正确");
	        }*/
            if (!StringUtils.isEmpty(mgtUser.getSex()) && !(("M").equals(mgtUser.getSex()) || ("F").equals(mgtUser.getSex()))) {
                throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"性别参数不正确，M：男；F：女； ");
            }
            if (!StringUtils.isEmpty(mgtUser.getEmail()) && !RegExpValidatorUtils.checkEmail(mgtUser.getEmail())) {
                throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"邮箱格式不正确");
            } else if (!StringUtils.isEmpty(mgtUser.getEmail()) && !checkEmail(mgtUser.getEmail())) {
                throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"该邮箱已存在");
            }
            mgtUser.setPassword(SystemService.entryptPassword(mgtUser.getPassword()));
            mgtUser.setId(IdGen.uuid());
            mgtUser.setLoginFlag(LoginFlag.enable.getCode());//默认启用,帐号状态 “启用”代表此账号允许登录，“停用”则表示此账号不允许登录
            super.mapper.insertSelective(mgtUser);
        }
    }

    private JSONObject sslogin(MgtUser mgtUser) throws Exception {
		JSONObject jsonObject = new JSONObject();
		if (null!= mgtUser) {
			// 更新登录IP和时间
			updateUserLoginInfo(mgtUser);
			String userId = mgtUser.getId();
			String adminToken = AdminTokenUtils.createToken(userId);
			jsonObject.put("id", mgtUser.getId());
			jsonObject.put("token", adminToken);
			jsonObject.put("name", mgtUser.getName() == null ? "" : mgtUser.getName());
			jsonObject.put("email", mgtUser.getEmail() == null ? "" : mgtUser.getEmail());
			jsonObject.put("phone", mgtUser.getPhone() == null ? "" : mgtUser.getPhone());
			jsonObject.put("mobile", mgtUser.getMobile() == null ? "" : mgtUser.getMobile());
			jsonObject.put("photo", Global.getFullImagePath(mgtUser.getPhoto()));
			//jsonObject.put("sourceSystem", mgtUser.getSourceSystem());
			//jsonObject.put("sourceSystemId", mgtUser.getSourceSystemId());
			JSONArray array = new JSONArray();
			//物管员工关联的小区集合
			BaseCommunity communityQuery = new BaseCommunity();
			communityQuery.setUserId(mgtUser.getId());
			List<BaseCommunity> communityList = baseCommunityMapper.findUserCommunityList(communityQuery);
			for (BaseCommunity dto : communityList) {
				net.sf.json.JSONObject obj = new net.sf.json.JSONObject();
				obj.put("id", dto.getId());
				obj.put("communityName", dto.getCommunityName());
				obj.put("companyId", dto.getCompanyId());
				obj.put("companyName", dto.getCompanyName());
				//obj.put("sourceSystem", dto.getSourceSystem());
				//obj.put("sourceSystemId", dto.getSourceSystemId());
				//obj.put("serverUrl", dto.getServerUrl());
				//obj.put("residentManager", dto.getResidentManager());
				array.add(obj);
			}
			jsonObject.put("communityList", array);
		}
		return jsonObject;
	}
}
package com.cmig.future.csportal.module.sys.utils;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.common.utils.MD5;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUser;
import com.cmig.future.csportal.module.properties.mgtuser.service.IMgtUserService;
import com.cmig.future.csportal.module.sys.service.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * 员工用户token管理
 * @author Administrator
 *
 */
public class AdminTokenUtils {
	
	/**
	 * 日志对象
	 */
	private static final Logger logger = LoggerFactory.getLogger(AdminTokenUtils.class);
	
	public static final String ADMIN_USER = "admin.user:";
	
	public static final String ADMIN_TOKEN = "admin.token:";

    private static IMgtUserService mgtUserService= SpringContextHolder.getBean("mgtUserServiceImpl");
	
	/**
	 * 生成员工token
	 * @param userId
	 * @return
	 */
	public static String createToken(String userId) {
		String cacheToken = hasToken(userId);
		if(!StringUtils.isEmpty(cacheToken)){
			return cacheToken;
		}
		String uuid = UUID.randomUUID().toString();
		long time = System.currentTimeMillis();
		String token = MD5.MD5Encode(userId+uuid+time);
		int expires = getTokenExpires();
		logger.debug("admin user expires time {} "+expires);
		JedisUtils.set(ADMIN_USER+userId, token, expires);
		String result = JedisUtils.set(ADMIN_TOKEN+token, userId, expires);
		logger.debug("save admin user token result {} "+result);
		return token;
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	private static String hasToken(String userId){
		return JedisUtils.get(ADMIN_USER+userId);
	}
	
	/**
	 * 检查员工token是否有效
	 * @param token
	 * @return
	 */
	public static boolean checkToken(String token){
		if(StringUtils.isEmpty(token)){
			return false;
		}
		String uid = getUserIdByToken(token);
		logger.debug("检查 admin token:"+uid);
		if(StringUtils.isEmpty(uid)){
			return false;
		}
		int expires = getTokenExpires();
		JedisUtils.set(ADMIN_TOKEN+token, uid, expires);
		JedisUtils.set(ADMIN_USER+uid, token, expires);
		logger.debug("检查 admin token======ok");
		return true;
	}
	
	/**
	 * token有效期
	 * @return
	 */
	private static int getTokenExpires(){
		String timeout= Global.getConfig("adminuser.token.timeout");
		int expires = 0;
		if(StringUtils.isEmpty(timeout)){
			expires = 7*24*3600;
		}else{
			expires = Integer.parseInt(timeout);
		}
		return expires;
	}
	
	/**
	 * 获取员工id
	 * @param token
	 * @return
	 */
	public static String getUserIdByToken(String token) {
		return JedisUtils.get(ADMIN_TOKEN+token);
	}
	
	/**
	 * 获取员工
	 * @param token
	 * @return
	 */
	public static MgtUser getUserByToken(String token) {
		String userId= JedisUtils.get(ADMIN_TOKEN+token);
		return mgtUserService.getUser(userId);
	}

	public static void cleanToken(String token){
		String userId=getUserIdByToken(token);
		JedisUtils.del(ADMIN_USER+userId);
		JedisUtils.del(ADMIN_TOKEN+token);
	}
}
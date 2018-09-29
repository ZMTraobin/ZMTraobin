package com.cmig.future.csportal.module.sys.utils;


import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.common.utils.MD5;
import com.cmig.future.csportal.common.zmcore.sso.CoreSSOClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 用户token管理
 * @author Administrator
 *
 */
public class UserTokenUtils {
	
	/**
	 * 日志对象
	 */
	private static final Logger logger = LoggerFactory.getLogger(UserTokenUtils.class);
	
	public static final String APP_USER = "app.user:";
	
	public static final String APP_TOKEN = "app.token:";

    public static final String APP_TOKEN_TGT_ST = "app.token.tgt.st:";

    public static final String TGT="TGT";
    public static final String ST="ST";
	public static final String TGT_KEEP_ALIVE_TIME="TGT_KEEP_ALIVE_TIME";

	public static int expires=0;
	static {
		expires=getTokenExpires();
	}
	/**
	 * 生成token
	 * @param userId
	 * @return
	 */
	public static String createToken(String userId,String tgt,String st) {
		String cacheToken = hasToken(userId);
		if(!StringUtils.isEmpty(cacheToken)){
			cacheTgt(tgt, st,cacheToken);
			return cacheToken;
		}
		String uuid = UUID.randomUUID().toString();
		long time = System.currentTimeMillis();
		String token = MD5.MD5Encode(userId+uuid+time);
		logger.debug("expires time {} "+expires);
		JedisUtils.set(APP_USER+userId, token, expires);
		String result = JedisUtils.set(APP_TOKEN+token, userId, expires);
		cacheTgt(tgt, st,token);
		logger.debug("save user token result {} "+result);
		return token;
	}

	private static void cacheTgt(String tgt, String st,String token) {
		if(!StringUtils.isEmpty(tgt)&&!StringUtils.isEmpty(st)) {
		    CoreSSOClientUtils.ticketsKeepAlive(st,tgt,expires);
		    Map tgtStMap = new HashMap<String, String>();
		    tgtStMap.put(TGT, tgt);
		    tgtStMap.put(ST, st);
			tgtStMap.put(TGT_KEEP_ALIVE_TIME,new Date());
		    JedisUtils.setObjectMap(APP_TOKEN_TGT_ST + token, tgtStMap, expires);
		}
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	private static String hasToken(String userId){
		return JedisUtils.get(APP_USER+userId);
	}
	
	/**
	 * 检查token是否有效
	 * @param token
	 * @return
	 */
	public static boolean checkToken(String token){
		if(StringUtils.isEmpty(token)){
			return false;
		}
		String uid = getUserIdByToken(token);
		logger.debug("检查token:"+uid);
		if(StringUtils.isEmpty(uid)){
			return false;
		}
		JedisUtils.set(APP_TOKEN+token, uid, expires);
		JedisUtils.set(APP_USER+uid, token, expires);

        JedisUtils.expire(APP_TOKEN_TGT_ST+token,expires);
        Map tgtStMap=JedisUtils.getObjectMap(APP_TOKEN_TGT_ST+token);
        if(null!=tgtStMap) {
	        String tgt = tgtStMap.get(TGT) == null ? "" : tgtStMap.get(TGT).toString();
	        String st = tgtStMap.get(ST) == null ? "" : tgtStMap.get(ST).toString();

	        //降低频率，一个小时内，只对cas调用一次延时
	        if (tgtStMap.containsKey(TGT_KEEP_ALIVE_TIME)) {
		        Date tgtKeepAliveTime = (Date) tgtStMap.get(TGT_KEEP_ALIVE_TIME);
		        if (DateUtils.addHours(tgtKeepAliveTime, 1).compareTo(new Date()) <= 0) {
			        CoreSSOClientUtils.ticketsKeepAlive(st, tgt, expires);
			        tgtStMap.put(TGT_KEEP_ALIVE_TIME, new Date());
			        JedisUtils.setObjectMap(APP_TOKEN_TGT_ST + token, tgtStMap, expires);
		        }
	        } else {
		        CoreSSOClientUtils.ticketsKeepAlive(st, tgt, expires);
		        tgtStMap.put(TGT_KEEP_ALIVE_TIME, new Date());
		        JedisUtils.setObjectMap(APP_TOKEN_TGT_ST + token, tgtStMap, expires);
	        }
        }
		logger.debug("检查token======ok");
		return true;
	}
	
	/**
	 * token有效期
	 * @return
	 */
	private static int getTokenExpires(){
		String timeout= Global.getConfig("appuser.token.timeout");
		int expires = 0;
		if(StringUtils.isEmpty(timeout)){
			expires = 7*24*3600;
		}else{
			expires = Integer.parseInt(timeout);
		}
		return expires;
	}
	
	/**
	 * 获取用户id
	 * @param token
	 * @return
	 */
	public static String getUserIdByToken(String token) {
		return JedisUtils.get(APP_TOKEN+token);
	}

	public static void cleanToken(String token){
		String userId=getUserIdByToken(token);
		JedisUtils.del(APP_USER+userId);
		JedisUtils.del(APP_TOKEN+token);
		JedisUtils.del(APP_TOKEN_TGT_ST + token);
	}
}
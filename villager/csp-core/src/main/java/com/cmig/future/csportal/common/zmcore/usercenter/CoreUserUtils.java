package com.cmig.future.csportal.common.zmcore.usercenter;


import com.alibaba.fastjson.JSONArray;
import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.common.utils.StringUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangtao107@126.com on 2016/8/25.
 */
@SuppressWarnings("unchecked")
public class CoreUserUtils {

	private static Logger log = LoggerFactory.getLogger(CoreUserUtils.class);

    private static String USER_CENTER_PRIFIX="ljh_";
    //注册地址
    private static String USER_CENTER_REGISTER;
    //修改密码uid地址
    private static String USER_CENTER_RESET;
    //修改密码手机地址
    private static String USER_CENTER_MOBILE;
    //验证手机唯一性地址
    private static String USER_CHECK_MOBILE;
    //校验密码是否正确接口地址
    private static String USER_CHECKOUT_PASSWORD;
	//获取用户资料接口地址
	private static String GET_USER_INFO;

	//第三方帐号查询
	private static String user_third_query;
	//第三方帐号解绑
	private static String user_third_unBind;



    
    private static String SUCCESS_CODE="0000";
    private static Map headMap=new HashMap();
    public static Map errorCoeMap=new HashMap();
    static{

	    String serverUrl=Global.getHspServerUrl("CORE_USER_CENTER_URL");
        USER_CENTER_REGISTER= serverUrl+"/user/pw/regist";
        USER_CENTER_RESET= serverUrl+"/user/pw/reset";
        USER_CENTER_MOBILE= serverUrl+"/user/pw/reset/mobile";
        USER_CHECK_MOBILE= serverUrl+"/check/mobile";
        USER_CHECKOUT_PASSWORD = serverUrl+"/user/pw/authenticate";
	    GET_USER_INFO=serverUrl+"/user/info/getUser/iTou";
	    user_third_query=serverUrl+"/user/third/query";
	    user_third_unBind=serverUrl+"/user/third/unBind";
        headMap.put("Authorization",Global.getConfig("CORE_USER_CENTER_AUTHORIZATION"));
	    if(Global.getBoolean("HSP.ENABLE_FLAG",false)){
		    headMap.put("Authorization",Global.getHspAuthorization());
	    }

   /*     headMap.put("Authorization","Basic MDAwMjpvMm8xITJAMyM=");
        //注册地址
        USER_CENTER_REGISTER="http://10.17.5.62:8880/user/pw/regist";
        //修改密码uid地址
        USER_CENTER_RESET="http://10.17.5.62:8880/user/pw/reset";
        //修改密码手机地址
        USER_CENTER_MOBILE="http://10.17.5.62:8880/user/pw/reset/mobile";
        //验证手机唯一性地址
        USER_CHECK_MOBILE="http://10.17.5.62:8880/check/mobile";
        //校验密码是否正确接口地址
        USER_CHECKOUT_PASSWORD ="http://10.17.5.62:8880/user/pw/authenticate";*/


        errorCoeMap.put("0001","请求不合法");
        errorCoeMap.put("0002","参数错误");
        errorCoeMap.put("0012","手机号码不正确");
        errorCoeMap.put("0013","该手机号已注册，请直接登录");
        errorCoeMap.put("0014","登录名重复");
        errorCoeMap.put("0015","证件号重复");
        errorCoeMap.put("0016","邮箱重复");
        errorCoeMap.put("0017","该手机号未注册，请先注册");
        errorCoeMap.put("0018","用户密码不正确");
        errorCoeMap.put("0019","登录名错误");
        errorCoeMap.put("0020","证件号错误");
        errorCoeMap.put("0021","邮箱错误");
        errorCoeMap.put("0022","TGT 无效错误");
        errorCoeMap.put("0023","短信发送失败");
        errorCoeMap.put("0024","用户被锁定");
        errorCoeMap.put("9999","系统内部错误");
        errorCoeMap.put("1000","其它错误");
	    errorCoeMap.put("0026","绑定关系没找到");

    }

    /**
     * 用户注册
     * @param mobile
     * @param password
     * @return
     * @throws DataWarnningException
     */
    public static String register(String mobile,String password) throws DataWarnningException {
        JSONObject keyParam=new JSONObject();
        keyParam.put("loginName", USER_CENTER_PRIFIX+mobile);
        keyParam.put("password", password);
        keyParam.put("mobile", mobile);

        JSONObject jsonParam=new JSONObject();
        jsonParam.put("UserRegistPwReq", keyParam);

        String result= HttpUtil.post(USER_CENTER_REGISTER,jsonParam,headMap);
        if(!StringUtils.isEmpty(result)){
            JSONObject jsonObject= JSONObject.fromObject(result);
            if(null!=jsonObject){
                JSONObject resInfo= JSONObject.fromObject(jsonObject.get("Resp"));
                String code=resInfo.get("code").toString();
                if(SUCCESS_CODE.equals(code)){
                    String uid=resInfo.get("uid").toString();
                    return uid;
                }else{
                    throw new DataWarnningException(errorCoeMap.get(code).toString());
                }
            }
        }
        return  null ;
    }

    /**
     * 根据uid修改用户密码
     * @param uid
     * @param password
     * @return
     * @throws DataWarnningException
     */
    public static boolean updatePassword(String uid,String password) throws DataWarnningException{
    	JSONObject keyParam=new JSONObject();
    	keyParam.put("uid", uid);
    	keyParam.put("password", password);
    	JSONObject jsonParam=new JSONObject();
    	jsonParam.put("UserResetPwReq", keyParam);
        String result=HttpUtil.post(USER_CENTER_RESET,jsonParam,headMap);
        if(!StringUtils.isEmpty(result)){
            JSONObject jsonObject= JSONObject.fromObject(result);
            if(null!=jsonObject){
                JSONObject resInfo= JSONObject.fromObject(jsonObject.get("Resp"));
                String code=resInfo.get("code").toString();
                if(SUCCESS_CODE.equals(code)){
                    return false;
                }else{
                    throw new DataWarnningException(errorCoeMap.get(code).toString());
                }
            }
        }

        return true;
    }

    /**
     * 根据手机号修改密码
     * @param mobile
     * @param password
     * @return
     * @throws DataWarnningException
     */
    public static boolean updatePasswordByMobile(String mobile,String password) throws DataWarnningException{
    	 JSONObject keyParam=new JSONObject();
    	 keyParam.put("mobileNo", mobile);
    	 keyParam.put("password", password);
    	 JSONObject jsonParam=new JSONObject();
    	 jsonParam.put("MobileResetPwReq", keyParam);
    	 String result=HttpUtil.post(USER_CENTER_MOBILE,jsonParam,headMap);
         if(!StringUtils.isEmpty(result)){
             JSONObject jsonObject= JSONObject.fromObject(result);
             if(null!=jsonObject){
                 JSONObject resInfo= JSONObject.fromObject(jsonObject.get("Resp"));
                 String code=resInfo.get("code").toString();
                 if(SUCCESS_CODE.equals(code)){
                     return true;
                 }else{
                     throw new DataWarnningException(errorCoeMap.get(code).toString());
                 }
             }
         }
    	
        return false;
    }

    /**
     * 校验手机号是否已存在，true表示已存在，false表示不存在
     * @param mobile
     * @return
     * @throws DataWarnningException
     */
    public static boolean validationMobile(String mobile) throws DataWarnningException{
    	JSONObject keyParam=new JSONObject();
    	keyParam.put("key", mobile);
    	JSONObject jsonParam=new JSONObject();
    	jsonParam.put("CheckReq", keyParam);
    	log.info(jsonParam.toString());
    	String result=HttpUtil.post(USER_CHECK_MOBILE,jsonParam,headMap);
        if(!StringUtils.isEmpty(result)){
            JSONObject jsonObject= JSONObject.fromObject(result);
            if(null!=jsonObject){
                JSONObject resInfo= JSONObject.fromObject(jsonObject.get("Resp"));
                String code=resInfo.get("code").toString();
                if(SUCCESS_CODE.equals(code)){
                    return false;
                }else if("0013".equals(code)){
                    return true;
                }else{
                    throw new DataWarnningException(errorCoeMap.get(code).toString());
                }
            }
        }
       return true;
    }

    /**
     * 校验密码是否正确，true表示正确，false表示不正确
     * @param uid
     * @param password
     * @return
     * @throws DataWarnningException
     */
    public static boolean validationPassword(String uid,String password) throws DataWarnningException{
        JSONObject keyParam=new JSONObject();
        keyParam.put("uid", uid);
        keyParam.put("password", password);
        JSONObject jsonParam=new JSONObject();
        jsonParam.put("UserPwReq", keyParam);
        log.info(jsonParam.toString());
        String result=HttpUtil.post(USER_CHECKOUT_PASSWORD,jsonParam,headMap);
        if(!StringUtils.isEmpty(result)){
            JSONObject jsonObject= JSONObject.fromObject(result);
            if(null!=jsonObject){
                JSONObject resInfo= JSONObject.fromObject(jsonObject.get("Resp"));
                String code=resInfo.get("code").toString();
                if(SUCCESS_CODE.equals(code)){
                    return true;
                }else{
                	 throw new DataWarnningException(errorCoeMap.get(code).toString());
                }
            }
    }
        return false;
    }


	/**
	 * 根据手机号获取用户基本信息
	 * @param mobile
	 * @return
	 * @throws DataWarnningException
	 */
	public static String getUid(String mobile) throws DataWarnningException {
		JSONObject keyParam=new JSONObject();
		keyParam.put("mobileNo", mobile);

		JSONObject jsonParam=new JSONObject();
		jsonParam.put("UserInfoReq", keyParam);

		String result= HttpUtil.post(GET_USER_INFO,jsonParam,headMap);
		if(!StringUtils.isEmpty(result)){
			JSONObject jsonObject= JSONObject.fromObject(result);
			if(null!=jsonObject){
				JSONObject resInfo= JSONObject.fromObject(jsonObject.get("Resp"));
				String code=resInfo.get("code").toString();
				if(SUCCESS_CODE.equals(code)){
					JSONObject userBasic= JSONObject.fromObject(resInfo.get("userBasic"));
					String uid=userBasic.get("userId").toString();
					return uid;
				}else{
					return  null;
					//throw new DataWarnningException(errorCoeMap.get(code).toString());
				}
			}
		}
		return  null ;
	}

	/**
	 * 根据uid获取用户基本信息
	 * @param uid
	 * @return
	 * @throws DataWarnningException
	 */
	public static String getMobile(String uid) throws DataWarnningException {
		JSONObject keyParam=new JSONObject();
		keyParam.put("uid", uid);

		JSONObject jsonParam=new JSONObject();
		jsonParam.put("UserInfoReq", keyParam);

		String result= HttpUtil.post(GET_USER_INFO,jsonParam,headMap);
		if(!StringUtils.isEmpty(result)){
			JSONObject jsonObject= JSONObject.fromObject(result);
			if(null!=jsonObject){
				JSONObject resInfo= JSONObject.fromObject(jsonObject.get("Resp"));
				String code=resInfo.get("code").toString();
				if(SUCCESS_CODE.equals(code)){
					JSONObject userBasic= JSONObject.fromObject(resInfo.get("userSecurityBind"));
					String mobileNo=userBasic.get("mobileNo").toString();
					return mobileNo;
				}else{
					return  null;
					//throw new DataWarnningException(errorCoeMap.get(code).toString());
				}
			}
		}
		return  null ;
	}

	/**
	 * 查询第三方帐号
	 * @param userId
	 * @param openId
	 * @param unionId
	 * @param thirdChannelCode
	 * @return
	 * @throws DataWarnningException
	 */
	public static JSONArray userThirdQuery(String userId,String openId,String unionId ,String thirdChannelCode) throws DataWarnningException {
		JSONObject keyParam=new JSONObject();
		keyParam.put("userId", userId==null?"":userId);
		keyParam.put("openId", openId==null?"":openId);
		keyParam.put("unionId", unionId==null?"":unionId);
		keyParam.put("thirdChannelCode", thirdChannelCode==null?"":thirdChannelCode);
		keyParam.put("bindStatus", "1");//1：绑定 0:未绑定

		JSONObject jsonParam=new JSONObject();
		jsonParam.put("UserThirdBindReq", keyParam);

		String result= HttpUtil.post(user_third_query,jsonParam,headMap);
		if(!StringUtils.isEmpty(result)){
			JSONObject jsonObject= JSONObject.fromObject(result);
			if(null!=jsonObject){
				JSONObject resInfo= JSONObject.fromObject(jsonObject.get("Resp").toString());
				String code=resInfo.get("code")==null?"":resInfo.get("code").toString();
				String message=resInfo.get("message")==null?"":resInfo.get("message").toString();
				if(SUCCESS_CODE.equals(code)){
					JSONArray userThirdBinds= JSONArray.parseArray(resInfo.get("userThirdBinds").toString());
					return userThirdBinds;
				}else{
					throw new DataWarnningException(errorCoeMap.get(code)==null?message:errorCoeMap.get(code).toString());
				}
			}
		}
		return null;
	}

	/**
	 * 解绑第三方帐号
	 * @param userId
	 * @param openId
	 * @param unionId
	 * @param thirdChannelCode
	 * @return
	 * @throws DataWarnningException
	 */
	public static void userThirdUnBind(String userId,String openId,String unionId ,String thirdChannelCode) throws DataWarnningException {
		JSONObject keyParam=new JSONObject();
		keyParam.put("userId", userId==null?"":userId);
		keyParam.put("openId", openId==null?"":openId);
		keyParam.put("unionId", unionId==null?"":unionId);
		keyParam.put("thirdChannelCode", thirdChannelCode==null?"":thirdChannelCode);
		keyParam.put("bindStatus", "");

		JSONObject jsonParam=new JSONObject();
		jsonParam.put("UserThirdBindReq", keyParam);

		String result= HttpUtil.post(user_third_unBind,jsonParam,headMap);
		if(!org.apache.commons.lang3.StringUtils.isEmpty(result)){
			JSONObject jsonObject= JSONObject.fromObject(result);
			if(null!=jsonObject){
				JSONObject resInfo= JSONObject.fromObject(jsonObject.get("Resp").toString());
				String code=resInfo.get("code")==null?"":resInfo.get("code").toString();
				String message=resInfo.get("message")==null?"":resInfo.get("message").toString();
				if(SUCCESS_CODE.equals(code)){

				} else if("0026".equals(code)){

				}else{
					throw new DataWarnningException(errorCoeMap.get(code)==null?message:errorCoeMap.get(code).toString());
				}
			}
		}
	}
    public static void main(String args[]) {
    	String mobile="18210225836";
    	String password="qwe123456";
    	String uid="34ca285e7e68473b96a235ec6228a9ec";
    	try {
			//CoreUserUtils.validationMobile(mobile);
			//CoreUserUtils.updatePasswordByMobile(mobile, password);
    		//CoreUserUtils.updatePassword(uid, password);
			//CoreUserUtils.register(mobile, password);
    		//CoreUserUtils.validationPassword(uid, password);

		    CoreUserUtils.getUid("28210225832");
		} catch (DataWarnningException e) {
			e.printStackTrace();
		}
        
    }
    
}

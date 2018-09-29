/**
 * .
 */
package com.cmig.future.csportal.common.config;


import com.cmig.future.csportal.common.utils.PropertiesLoader;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 全局配置类
 * @author ThinkGem
 * @version 2014-06-25
 */
public class Global {

    private static Logger logger= LoggerFactory.getLogger(Global.class);
	/**
	 * 当前对象实例
	 */
	private static Global global = new Global();
	
	/**
	 * 保存全局属性值
	 */
	private static Map<String, String> map = Maps.newHashMap();
	
	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader loader = new PropertiesLoader("config.properties");

	/**
	 * 显示/隐藏
	 */
	public static final String SHOW = "1";
	public static final String HIDE = "0";

	/**
	 * 是/否
	 */
	public static final String YES = "1";
	public static final String NO = "0";
	
	/**
	 * 对/错
	 */
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	/**
	 * 成员关系
	 */
	public static final String DICT_VILLAGER_FAMILY_RELATION = "villager.family.relation";
	/**
	 * 自有土地面积
	 */
	public static final String DICT_VILLAGER_FAMILY_LANDAREA = "villager.family.landarea";
	/**
	 * 土地流转价格
	 */
	public static final String DICT_VILLAGER_FAMILY_LANDPRICE = "villager.family.landprice";
	/**
	 * 主要作物
	 */
	public static final String DICT_VILLAGER_FAMILY_MAINCROP = "villager.family.maincrop";
	/**
	 * 住房面积
	 */
	public static final String DICT_VILLAGER_FAMILY_HOUSEAREA = "villager.family.housearea";
	/**
	 * 宅基地面积
	 */
	public static final String DICT_VILLAGER_FAMILY_HOMESTEAD = "villager.family.homestead";
	/**
	 * 建造成本
	 */
	public static final String DICT_VILLAGER_FAMILY_BUILDPRICE = "villager.family.buildprice";
	/**
	 * 存款银行
	 */
	public static final String DICT_VILLAGER_FAMILY_BANK = "villager.family.bank";
	/**
	 * 存款金额
	 */
	public static final String DICT_VILLAGER_FAMILY_AMOUNT = "villager.family.amount";
	/**
	 * 是/否
	 */
	public static final String DICT_VILLAGER_FAMILY_YESNO = "villager.family.yesno";



	/**
	 * 获取当前对象实例
	 */
	public static Global getInstance() {
		return global;
	}

    /**
     * 获取配置
     * @param key
     * @return
     */
	public static String getConfig(String key) {
		String value = map.get(key);
		if (value == null){
			value = loader.getProperty(key);

			map.put(key, value != null ? value : StringUtils.EMPTY);
		}
		return value;
	}

    /**
     * 获取配置
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getConfig(String key,String defaultValue) {
        String value = map.get(key);
        if (value == null){
            value = loader.getProperty(key,defaultValue);
            map.put(key, value != null ? value : StringUtils.EMPTY);
        }
        return value;
    }

    /**
     * 获取配置
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBooleanConfig(String key,String defaultValue) {
        return Boolean.parseBoolean(getConfig(key, defaultValue));
    }

    public static Boolean getBoolean(String key, boolean defaultValue) {
        return loader.getBoolean(key,defaultValue);
    }

    /**
     * 页面获取常量
     * @param field
     * @return
     */
	public static Object getConst(String field) {
		try {
			return Global.class.getField(field).get(null);
		} catch (Exception e) {
			// 异常代表无配置，这里什么也不做
		}
		return null;
	}

    /**
     * 获取app名
     */
    public static String getProductName(){
        return  getConfig("csp.productName");
    }

    /**
	 * 获取图片访问URL
	 */
	public static String getImageServerPath() {
        return  getConfig("fastdfs.imageServer");
	}

    /**
     * 获取生成或下载文件的临时根目录
     * @return
     */
    public static String getUserfilesTempDir() {
        String dir = getConfig("userfiles.tempdir");
        if(!dir.endsWith("/")) {
            dir += "/";
        }
        return dir;
    }

	public static String getShieldMobile(String mobile){
		String newStr="";
		if (!StringUtils.isEmpty(mobile)) {
			int frontLen=3;
			int endLen=3;
			String star="*****";
			newStr= mobile.substring(0,frontLen)+star+mobile.substring(mobile.length()-endLen);
		} 
		return newStr;
		
	}
	
	public static String getShieldIdCard(String idCard){
		if(idCard.length()<15){
			String newStr=idCard;
			return newStr;
		}
		String newStr="";
		if (!StringUtils.isEmpty(idCard)) {
			int frontLen=3;
			int endLen=4;
			String star = "***********";
			newStr= idCard.substring(0,frontLen)+star+idCard.substring(idCard.length()-endLen);
		}
		return newStr;
		
	}
	
	/**
	* 此方法描述的是：输入图片半路径返回图片的全路径
	* @author:zhangtao107@126.com
	* @param halfImagePath
	* @return String
	*/
	public static String getFullImagePath(String halfImagePath){
		if (!StringUtils.isEmpty(halfImagePath)&&!(halfImagePath.startsWith("http://")||halfImagePath.startsWith("https://"))) {
			return getImageServerPath()+halfImagePath;
		} 
		return halfImagePath;
		
	}
	
	/**
	* 此方法描述的是：输入图片半路径返回图片略缩图的全路径
	* @author:zhangtao107@126.com
	* @param halfImagePath
	* @return String
	*/
	public static String getFullImagePathForReduce(String halfImagePath){
		if (!StringUtils.isEmpty(halfImagePath)&&!(halfImagePath.startsWith("http://")||halfImagePath.startsWith("https://"))) {
			//return getImageServerPath()+"reduce/"+halfImagePath;
		    return getImageServerPath()+halfImagePath;
		} 
		return halfImagePath;
		
	}

    public static String getProjectPath(HttpServletRequest request){
        String thisServerUrl = request.getScheme() //当前链接使用的协议
                +"://" + request.getServerName();//服务器地址
        if(!StringUtils.isEmpty(request.getServerPort())&&!"80".equals(request.getServerPort())) {
            thisServerUrl+=":" + request.getServerPort(); //端口号
        }
        thisServerUrl+=request.getContextPath(); //应用名称，如果应用名称为
        logger.debug(thisServerUrl);
        return thisServerUrl;
    }

	/**
	 * 获取hsp封装后的接口地址
	 * @param key
	 * @return
	 */
	public static String getHspServerUrl(String key) {
		if(Global.getBoolean("HSP.ENABLE_FLAG",false)){
			return Global.getConfig("HSP."+key);
		}else{
			return Global.getConfig(key);
		}
	}

	/**
	 * 获取hsp接口认证信息
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getHspAuthorization() {
		String account=Global.getConfig("HSP.ACCOUNT","");
		String password=Global.getConfig("HSP.PASSWORD","");
		if(Global.getBoolean("HSP.ENABLE_FLAG",false)){
			String authorization = null;
			try {
				authorization = Base64.encodeBase64String(new String(account + ":" + password).getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return "Basic "+authorization;
		}

		return null;
	}
}

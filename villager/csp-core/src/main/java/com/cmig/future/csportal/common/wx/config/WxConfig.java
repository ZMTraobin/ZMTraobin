package com.cmig.future.csportal.common.wx.config;

import java.util.Properties;

/**
 * 类名：配置类
 * 功能：配置类
 * 类属性：公共类
 * 版本：1.0
 * 日期：2016-1-27
 * 作者：chejh
 * 版权：zymobi
 * 说明：参考银联例子代码编写
 * */
public class WxConfig {
	/**
	 * 配置文件名称
	 */
	private static final String CONF_FILE_NAME = "wx_sdk.properties";//正式环境
	/**
	 * 微信开放平台分配的APPID（开通公众号之后可以获取到）
	 */
    public static String APP_ID;
	/**
	 * 微信开放平台分配的公众号appSecret（开通公众号之后可以获取到）
	 */
	public static String APP_SECRET;

	public static String AUTHORIZE_API;

	public static String OPEN_ID_API;

	public static String ACCESS_TOKEN_API;

	public static String USER_INFO_API;

	/**
	 * 初始化
	 */
	private static WxConfig pInstance = new WxConfig();
	/**
	 * 配置文件解析集变量
	 */
	private Properties m_props = null;

	private WxConfig() {
		m_props = new Properties();
		try {
			m_props.load(WxConfig.class.getClassLoader().getResourceAsStream(CONF_FILE_NAME));
			APP_ID=m_props.getProperty("appID");
			APP_SECRET=m_props.getProperty("appSecret");
			AUTHORIZE_API=m_props.getProperty("authorizeApi");
			OPEN_ID_API=m_props.getProperty("openIdApi");
			ACCESS_TOKEN_API=m_props.getProperty("accessTokenApi");
			USER_INFO_API=m_props.getProperty("userInfoApi");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	synchronized public static WxConfig getInstance() {
		return pInstance;
	}
}

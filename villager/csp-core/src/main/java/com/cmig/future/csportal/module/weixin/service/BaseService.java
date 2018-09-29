package com.cmig.future.csportal.module.weixin.service;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 16:24 2017/12/5.
 * @Modified by zhangtao on 16:24 2017/12/5.
 */
public class BaseService {

	private static final Logger logger= LoggerFactory.getLogger(BaseService.class);

	protected String WORK_WX_SERVER_URL="https://qyapi.weixin.qq.com";

	protected String SUCCESS_CODE="0";

	/**
	 * 获取access_token
	 * @param corpid
	 * @param corpsecret
	 * @return
	 * @throws Exception
	 */
	public String getAccessToken(String corpid,String corpsecret) throws Exception {
		String key="work.wx.access_token:corpid:"+corpid+" corpsecret:"+corpsecret;
		String access_token= JedisUtils.get(key);
		if(!StringUtils.isEmpty(access_token)){
			return access_token;
		}
		String url = WORK_WX_SERVER_URL+"/cgi-bin/gettoken?corpid=" + corpid + "&corpsecret=" + corpsecret;
		String result = HttpUtil.get(url);
		JSONObject jsonObject = JSONObject.parseObject(result);
		access_token = jsonObject.get("access_token").toString();
		int expires_in=new Integer(jsonObject.get("expires_in").toString());
		JedisUtils.set(key,access_token,expires_in-120);
		return access_token;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(new BaseService().getAccessToken("ww89baa51457258c02","oenb4Kuas2QMiiy8syaYz4BAD6nNg1WKfF5Tk38K2jk"));
	}

}

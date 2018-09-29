package com.cmig.future.csportal.module.weixin.service;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.module.weixin.WorkWxException;
import com.cmig.future.csportal.module.weixin.entry.Corp;
import com.cmig.future.csportal.module.weixin.entry.User;
import com.cmig.future.csportal.module.weixin.helper.WorkWxHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 16:36 2017/12/5.
 * @Modified by zhangtao on 16:36 2017/12/5.
 */
@Service
public class OauthService extends BaseService {

	public String getAuthorizeUrl(String agentNo, String redirectUri) throws UnsupportedEncodingException {
		if(redirectUri.indexOf("?")>0){
			redirectUri+="&agentNo="+ agentNo;
		}else{
			redirectUri+="?agentNo="+ agentNo;
		}
		Corp.Application agent=WorkWxHelper.getAgentByNo(agentNo);
		StringBuffer authorizerUrl = new StringBuffer();
		authorizerUrl.append("https://open.weixin.qq.com/connect/oauth2/authorize?");
		authorizerUrl.append("appid=" + agent.getCorpid());
		authorizerUrl.append("&redirect_uri=" + URLEncoder.encode(redirectUri, "utf-8"));
		authorizerUrl.append("&response_type=code");
		authorizerUrl.append("&scope=snsapi_privateinfo");
		authorizerUrl.append("&agentid=" + agent.getAgentId());
		authorizerUrl.append("&state=STATE");
		authorizerUrl.append("#wechat_redirect");
		return authorizerUrl.toString();
	}


	public JSONObject getUserTickitInfo(String agentNo,String code) throws Exception {
		Corp.Application agent=WorkWxHelper.getAgentByNo(agentNo);
		String access_token = getAccessToken(agent.getCorpid(),agent.getAgentSecret());
		String url = WORK_WX_SERVER_URL+"/cgi-bin/user/getuserinfo?access_token=" + access_token + "&code=" + code;
		String result = HttpUtil.get(url);
		JSONObject userTickitInfo = JSONObject.parseObject(result);
		return userTickitInfo;
	}

	public User getUserInfo(String agentNo,String code) throws Exception {
		Corp.Application agent=WorkWxHelper.getAgentByNo(agentNo);
		String access_token = getAccessToken(agent.getCorpid(),agent.getAgentSecret());
		String url = WORK_WX_SERVER_URL+"/cgi-bin/user/getuserinfo?access_token=" + access_token + "&code=" + code;
		String result = HttpUtil.get(url);
		JSONObject userTickitInfo = JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(userTickitInfo.get("errcode").toString())){
			throw new WorkWxException(userTickitInfo.get("errcode").toString());
		}

		String user_ticket = userTickitInfo.get("user_ticket").toString();
		url = WORK_WX_SERVER_URL+"/cgi-bin/user/getuserdetail?access_token=" + access_token;
		net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
		jsonObject.put("user_ticket", user_ticket);
		result = HttpUtil.post(url, jsonObject);
		User user=JSONObject.parseObject(result,User.class);
		return user;
	}
}

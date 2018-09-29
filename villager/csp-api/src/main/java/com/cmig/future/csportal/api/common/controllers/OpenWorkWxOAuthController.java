package com.cmig.future.csportal.api.common.controllers;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.weixin.entry.Event;
import com.cmig.future.csportal.module.weixin.entry.User;
import com.cmig.future.csportal.module.weixin.helper.WorkWxHelper;
import com.cmig.future.csportal.module.weixin.service.EventService;
import com.cmig.future.csportal.module.weixin.service.OauthService;
import com.cmig.future.csportal.module.weixin.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;

@RestController
@RequestMapping(value = "${commonPath}")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OpenWorkWxOAuthController extends BaseExtendController {

	private static final Logger logger = LoggerFactory.getLogger(OpenWorkWxOAuthController.class);

	@Autowired
	private OauthService oauthService;

	@Autowired
	private UserService userService;

	@Autowired
	private EventService eventService;

	@RequestMapping(value = "/open/work/wx/getAuthorizeUrl", produces = {"application/json"}, method = {RequestMethod.POST, RequestMethod.GET})
	public RetApp getAuthorizeUrl(HttpServletRequest request) throws Exception {
		String agentNo=getParam(request,"agentNo","");
		String redirectUri=getParam(request,"redirectUri","http://hshop-uat.zmsq.net/wap/mgt/zhangtao_demo/get_work_wx_code.html");

		if(StringUtils.isEmpty(agentNo)
				||StringUtils.isEmpty(redirectUri)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		String authorizerUrl=oauthService.getAuthorizeUrl(agentNo,redirectUri);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("authorizerUrl", authorizerUrl);
		return RetAppUtil.success(jsonObject, "获取成功");
	}

	@RequestMapping(value = "/open/work/wx/useridToOpenid", produces = {"application/json"}, method = {RequestMethod.POST, RequestMethod.GET})
	public RetApp useridToOpenid(@ModelAttribute User user, HttpServletRequest request) throws Exception {
		String agentNo=getParam(request,"agentNo","");
		if(StringUtils.isEmpty(agentNo)
				||StringUtils.isEmpty(user.getUserid())){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}
		String openid=userService.useridToOpenid(user.getUserid(),agentNo);
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("openid",openid);
		return RetAppUtil.success(jsonObject, "获取成功");
	}

	@RequestMapping(value = "/open/work/wx/openidToUserId", produces = {"application/json"}, method = {RequestMethod.POST, RequestMethod.GET})
	public RetApp openidToUserId(@ModelAttribute User user, HttpServletRequest request) throws Exception {
		String agentNo=getParam(request,"agentNo","");
		String openid=getParam(request,"openid","");
		if(StringUtils.isEmpty(agentNo)
				||StringUtils.isEmpty(openid)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}
		String userid=userService.openidToUserId(WorkWxHelper.getCorpIdByAgentNo(agentNo),openid);
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("userid",userid);
		return RetAppUtil.success(jsonObject, "获取成功");
	}

	@RequestMapping(value = "/open/work/wx/event/callback", produces = {"application/json"}, method = {RequestMethod.POST, RequestMethod.GET})
	public void eventCallBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String agentNo=getParam(request,"agentNo","");
		String sVerifyMsgSig = getParam(request, "msg_signature", "");
		String sVerifyTimeStamp = getParam(request, "timestamp", "");
		String sVerifyNonce = getParam(request, "nonce", "");
		String sVerifyEchoStr = getParam(request, "echostr", "");

		//body部分
		BufferedReader br = request.getReader();
		StringBuffer sreqdata = new StringBuffer();
		String string;
		while ((string = br.readLine()) != null) {
			sreqdata.append(string);
		}

		Event event=new Event();
		event.setBody(sreqdata.toString());
		event.setAgentNo(agentNo);
		event.setVerifyMsgSig(sVerifyMsgSig);
		event.setVerifyTimeStamp(sVerifyTimeStamp);
		event.setVerifyNonce(sVerifyNonce);
		event.setVerifyEchoStr(sVerifyEchoStr);
		String result= eventService.callBack(event);

		// 验证URL成功，将sEchoStr返回
		response.getWriter().print(result);
	}


}

package com.cmig.future.csportal.api.open.syn.workwx.controllers;

import com.cmig.future.csportal.common.oauth2.OAuthConstants;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.weixin.entry.Corp;
import com.cmig.future.csportal.module.weixin.entry.Message;
import com.cmig.future.csportal.module.weixin.helper.WorkWxHelper;
import com.cmig.future.csportal.module.weixin.service.MessageService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * BaseCompanySynController
 * 公司同步
 * <p>
 * Created by bubu on 2017/3/21.
 */
@RestController
@RequestMapping(value = "${commonPath}/")
public class MessageSynController extends BaseExtendController {

	private static Logger logger = LoggerFactory.getLogger(MessageSynController.class);
	@Autowired
	private MessageService messageService;


	/**
	 * 发消息
	 * @param message
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/syn/message/sendText", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp sendText(@ModelAttribute Message message, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId = getParam(request, "companyId", "");
		String content=getParam(request,"content","");
		//xml反转义
		content=StringEscapeUtils.unescapeXml(content);
		//java反转义
		content=StringEscapeUtils.unescapeJava(content);


		if (StringUtils.isEmpty(companyId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		if (StringUtils.isEmpty(content)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp = WorkWxHelper.getCorp(appId, companyId);
		if (null != corp) {
			Corp.Application application=WorkWxHelper.getDefaultAgent(corp);
			if(null!=application&&!StringUtils.isEmpty(application.getAgentNo())){
				messageService.sendText(application.getAgentNo(),message.getTouser(),message.getToparty(),message.getTotag(),content,message.getSafe());
				return RetAppUtil.success("发送成功");
			}else{
				throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
			}
		} else {
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}

	/**
	 * 发消息
	 * @param message
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/syn/message/sendTextToAll", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp sendTextToAll(@ModelAttribute Message message, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId = getParam(request, "companyId", "");
		String content=getParam(request,"content","");
		//xml反转义
		content=StringEscapeUtils.unescapeXml(content);
		//java反转义
		content=StringEscapeUtils.unescapeJava(content);

		if (StringUtils.isEmpty(companyId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		if (StringUtils.isEmpty(content)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp = WorkWxHelper.getCorp(appId, companyId);
		if (null != corp) {
			Corp.Application application=WorkWxHelper.getDefaultAgent(corp);
			if(null!=application&&!StringUtils.isEmpty(application.getAgentNo())){
				messageService.sendTextToAll(application.getAgentNo(),content,message.getSafe());
				return RetAppUtil.success("发送成功");
			}else{
				throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
			}
		} else {
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}

	@RequestMapping(value = "/syn/message/sendTextCard", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp sendTextCard(@ModelAttribute Message message, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId = getParam(request, "companyId", "");
		String title=getParam(request,"title","");
		String description=getParam(request,"description","");
		String detailUrl=getParam(request,"detailUrl","");
		String btntxt=getParam(request,"btntxt","");

		//xml反转义
		description=StringEscapeUtils.unescapeXml(description);
		//java反转义
		description=StringEscapeUtils.unescapeJava(description);

		if (StringUtils.isEmpty(companyId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		if (StringUtils.isEmpty(title)||StringUtils.isEmpty(description)||StringUtils.isEmpty(detailUrl)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp = WorkWxHelper.getCorp(appId, companyId);
		if (null != corp) {
			Corp.Application application=WorkWxHelper.getDefaultAgent(corp);
			if(null!=application&&!StringUtils.isEmpty(application.getAgentNo())){
				messageService.sendTextCard(application.getAgentNo(),message.getTouser(),message.getToparty(),message.getTotag(),title,description,detailUrl,btntxt);
				return RetAppUtil.success("发送成功");
			}else{
				throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
			}
		} else {
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}

	@RequestMapping(value = "/syn/message/sendNews", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp sendNews(@RequestBody Message message, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId = getParam(request, "companyId", "");

		if (StringUtils.isEmpty(companyId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp = WorkWxHelper.getCorp(appId, companyId);
		if (null != corp) {
			Corp.Application application=WorkWxHelper.getDefaultAgent(corp);
			if(null!=application&&!StringUtils.isEmpty(application.getAgentNo())){
				messageService.sendNews(application.getAgentNo(),message);
				return RetAppUtil.success("发送成功");
			}else{
				throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
			}
		} else {
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}
}

package com.cmig.future.csportal.api.open.syn.workwx.controllers;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.oauth2.OAuthConstants;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.weixin.entry.Corp;
import com.cmig.future.csportal.module.weixin.entry.Tag;
import com.cmig.future.csportal.module.weixin.entry.User;
import com.cmig.future.csportal.module.weixin.helper.WorkWxHelper;
import com.cmig.future.csportal.module.weixin.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * BaseCompanySynController
 * 公司同步
 * <p>
 * Created by bubu on 2017/3/21.
 */
@RestController
@RequestMapping(value = "${commonPath}/")
public class TagSynController extends BaseExtendController {

	private static Logger logger = LoggerFactory.getLogger(TagSynController.class);
	@Autowired
	private TagService tagService;


	/**
	 * 创建标签
	 * @param tag
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/syn/tag/create", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp create(@ModelAttribute Tag tag, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId = getParam(request, "companyId", "");

		if (StringUtils.isEmpty(companyId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp = WorkWxHelper.getCorp(appId, companyId);
		if (null != corp) {
			int tagId=tagService.create(corp.getCorpid(), tag);
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("tagId",tagId);
			return RetAppUtil.success(jsonObject,"新增成功");
		} else {
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}

	/**
	 * 修改标签
	 * @param tag
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/syn/tag/update", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp update(@ModelAttribute Tag tag, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId = getParam(request, "companyId", "");

		if (StringUtils.isEmpty(companyId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp = WorkWxHelper.getCorp(appId, companyId);
		if (null != corp) {
			tagService.update(corp.getCorpid(), tag);
			return RetAppUtil.success("修改成功");
		} else {
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}

	/**
	 * 删除标签
	 * @param tag
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/syn/tag/delete", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp delete(@ModelAttribute Tag tag, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId = getParam(request, "companyId", "");

		if(StringUtils.isEmpty(companyId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp = WorkWxHelper.getCorp(appId, companyId);
		if (null != corp) {
			tagService.delete(corp.getCorpid(), tag.getTagid());
			return RetAppUtil.success("删除成功");
		} else {
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}


	/**
	 * 获取标签下用户
	 * @param tag
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/syn/tag/getUserList", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp getUserList(@ModelAttribute Tag tag, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId = getParam(request, "companyId", "");

		if (StringUtils.isEmpty(companyId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		if (StringUtils.isEmpty(tag.getTagid())) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp = WorkWxHelper.getCorp(appId, companyId);
		if (null != corp) {
			List<User> userList = tagService.getUserList(corp.getCorpid(), tag.getTagid());
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("userList", userList);
			return RetAppUtil.success(jsonObject, "查询成功");
		} else {
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}

	/**
	 * 新增标签成员
	 * @param tag
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/syn/tag/addtagusers", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp addtagusers(@ModelAttribute Tag tag, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId = getParam(request, "companyId", "");
		String userIds = getParam(request, "userIds", "");
		String partyIds = getParam(request, "partyIds", "");

		if (StringUtils.isEmpty(companyId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		if (StringUtils.isEmpty(userIds)&&StringUtils.isEmpty(partyIds)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp = WorkWxHelper.getCorp(appId, companyId);
		if (null != corp) {
			 List<String> userlist=null;
			 if(!StringUtils.isEmpty(userIds)) {
				 userlist=Arrays.asList(userIds.split(","));
			 }

			List<Integer> partyList=null;
			if(!StringUtils.isEmpty(partyIds)) {
				partyList=new ArrayList<>();
				for(String party:partyIds.split(",")){
					partyList.add(new Integer(party));
				}
			}

			tagService.addtagusers(corp.getCorpid(), tag.getTagid(),userlist,partyList);
			return RetAppUtil.success("新增成功");
		} else {
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}

	/**
	 * 删除标签成员
	 *
	 * @param tag
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/syn/tag/deltagusers", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp deltagusers(@ModelAttribute Tag tag, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId = getParam(request, "companyId", "");
		String userIds = getParam(request, "userIds", "");
		String partyIds = getParam(request, "partyIds", "");

		if (StringUtils.isEmpty(companyId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		if (StringUtils.isEmpty(userIds)&&StringUtils.isEmpty(partyIds)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp = WorkWxHelper.getCorp(appId, companyId);
		if (null != corp) {
			List<String> userlist=null;
			if(!StringUtils.isEmpty(userIds)) {
				userlist=Arrays.asList(userIds.split(","));
			}

			List<Integer> partyList=null;
			if(!StringUtils.isEmpty(partyIds)) {
				partyList=new ArrayList<>();
				for(String party:partyIds.split(",")){
					partyList.add(new Integer(party));
				}
			}

			tagService.deltagusers(corp.getCorpid(), tag.getTagid(),userlist,partyList);
			return RetAppUtil.success("删除成功");
		} else {
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}

	/**
	 * 查询标签列表
	 * @param tag
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/syn/tag/getList", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp getList(@ModelAttribute Tag tag, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId = getParam(request, "companyId", "");

		if (StringUtils.isEmpty(companyId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp = WorkWxHelper.getCorp(appId, companyId);
		if (null != corp) {
			List<Tag> tagList=tagService.getList(corp.getCorpid());
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("tagList",tagList);
			return RetAppUtil.success(jsonObject,"查询成功");
		} else {
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}
}

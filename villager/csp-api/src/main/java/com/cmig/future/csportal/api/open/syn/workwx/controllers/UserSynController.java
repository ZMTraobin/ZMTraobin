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
import com.cmig.future.csportal.module.weixin.entry.User;
import com.cmig.future.csportal.module.weixin.helper.WorkWxHelper;
import com.cmig.future.csportal.module.weixin.service.UserService;
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
public class UserSynController extends BaseExtendController {

	private static Logger logger = LoggerFactory.getLogger(UserSynController.class);
	@Autowired
	private UserService userService;


	@RequestMapping(value = "/syn/user/create", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp create(@ModelAttribute User user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId = getParam(request, "companyId", "");
		String partyIds = getParam(request, "partyIds", "");

		if (StringUtils.isEmpty(companyId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}
		if (StringUtils.isEmpty(partyIds)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp = WorkWxHelper.getCorp(appId, companyId);
		if (null != corp) {
			List<Integer> departmentList=null;
			if(!StringUtils.isEmpty(partyIds)) {
				departmentList=new ArrayList<>();
				for(String party:partyIds.split(",")){
					departmentList.add(new Integer(party));
				}
			}
			user.setDepartment(departmentList);
			userService.createUser(corp.getCorpid(), user);
			return RetAppUtil.success("新增成功");
		} else {
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}

	@RequestMapping(value = "/syn/user/update", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp update(@ModelAttribute User user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId = getParam(request, "companyId", "");
		String partyIds = getParam(request, "partyIds", "");

		if (StringUtils.isEmpty(companyId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		if (StringUtils.isEmpty(partyIds)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp = WorkWxHelper.getCorp(appId, companyId);
		if (null != corp) {
			List<Integer> departmentList=null;
			if(!StringUtils.isEmpty(partyIds)) {
				departmentList=new ArrayList<>();
				for(String party:partyIds.split(",")){
					departmentList.add(new Integer(party));
				}
			}
			user.setDepartment(departmentList);
			userService.updateUser(corp.getCorpid(), user);
			return RetAppUtil.success("修改成功");
		} else {
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}

	@RequestMapping(value = "/syn/user/delete", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp delete(@ModelAttribute User user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId = getParam(request, "companyId", "");

		if (StringUtils.isEmpty(companyId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp = WorkWxHelper.getCorp(appId, companyId);
		if (null != corp) {
			userService.deleteUserByUserId(corp.getCorpid(), user.getUserid());
			return RetAppUtil.success("删除成功");
		} else {
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}

	@RequestMapping(value = "/syn/user/batch/delete", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp batchDelete(@ModelAttribute User user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId = getParam(request, "companyId", "");
		String userIds = getParam(request, "userIds", "");

		if (StringUtils.isEmpty(companyId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		if (StringUtils.isEmpty(userIds)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp = WorkWxHelper.getCorp(appId, companyId);
		if (null != corp) {
			List<String> userList = Arrays.asList(userIds.split(","));
			userService.batchDeleteUserByUserId(corp.getCorpid(), userList);
			return RetAppUtil.success("删除成功");
		} else {
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}

	@RequestMapping(value = "/syn/user/findByUserId", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp findByUserId(@ModelAttribute User user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId = getParam(request, "companyId", "");

		if (StringUtils.isEmpty(companyId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp = WorkWxHelper.getCorp(appId, companyId);
		if (null != corp) {
			user = userService.findByUserId(corp.getCorpid(), user.getUserid());
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("user", user);
			return RetAppUtil.success(jsonObject, "查询成功");
		} else {
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}

	@RequestMapping(value = "/syn/user/getUserSimplelistByDepartmentId", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp getUserSimplelistByDepartmentId(@ModelAttribute User user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId = getParam(request, "companyId", "");
		String departmentId = getParam(request, "departmentId", "");
		String fetchChild = getParam(request, "fetchChild", "1");

		if (StringUtils.isEmpty(companyId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		if (StringUtils.isEmpty(departmentId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp = WorkWxHelper.getCorp(appId, companyId);
		if (null != corp) {
			List<User> userList = userService.getUserSimplelistByDepartmentId(corp.getCorpid(), departmentId,fetchChild);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("userList", userList);
			return RetAppUtil.success(jsonObject, "查询成功");
		} else {
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}

	@RequestMapping(value = "/syn/user/getUserListByDepartmentId", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp getUserListByDepartmentId(@ModelAttribute User user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId = getParam(request, "companyId", "");
		String departmentId = getParam(request, "departmentId", "");
		String fetchChild = getParam(request, "fetchChild", "1");

		if (StringUtils.isEmpty(companyId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		if (StringUtils.isEmpty(departmentId)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp = WorkWxHelper.getCorp(appId, companyId);
		if (null != corp) {
			List<User> userList= userService.getUserListByDepartmentId(corp.getCorpid(), departmentId,fetchChild);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("userList", userList);
			return RetAppUtil.success(jsonObject, "查询成功");
		} else {
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}


}

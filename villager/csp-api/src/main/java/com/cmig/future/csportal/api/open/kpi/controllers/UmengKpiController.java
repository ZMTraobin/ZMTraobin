package com.cmig.future.csportal.api.open.kpi.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.oauth2.OAuthConstants;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.common.utils.sign.CspSignUtil;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.kpi.dto.LjhKpiEventGroup;
import com.cmig.future.csportal.module.kpi.dto.LjhKpiUser;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiEventGroupService;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiUserService;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@ResponseBody
@RequestMapping(value = "${commonPath}")
public class UmengKpiController extends BaseExtendController {

	@Autowired
	private ILjhKpiUserService ljhKpiUserService;

	@Autowired
	private ILjhKpiEventGroupService ljhKpiEventGroupService;

	@RequestMapping(value = "/umeng/kpi/user", produces = { "application/json" }, method = RequestMethod.POST)
	public RetApp user(HttpServletRequest request, HttpServletResponse response) {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appId);
		String startDate=request.getParameter("startDate");
		String endDate=request.getParameter("endDate");
		String sign=getParam(request,"sign","");

		Map paramMap=new HashMap();
		paramMap.put(OAuthConstants.OAUTH_APP_ID,appId);
		paramMap.put("startDate",startDate);
		paramMap.put("endDate",endDate);
		if(CspSignUtil.checkSign(paramMap,openAppInfo.getAppSecret(),sign)) {
			List<LjhKpiUser> list = ljhKpiUserService.selectReportForDate(DateUtils.parseDate(startDate), DateUtils.parseDate(endDate));
			JSONArray jsonArray = new JSONArray();
			if (list != null && list.size() > 0) {
				for (LjhKpiUser entry : list) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("groupName", entry.getGroupName());
					jsonObject.put("kpiDate", entry.getKpiDateStr());
					jsonObject.put("installations", entry.getInstallations());
					jsonObject.put("newUsers", entry.getNewUsers());
					jsonObject.put("activeUsers", entry.getActiveUsers());
					jsonObject.put("launches", entry.getLaunches());
					jsonArray.add(jsonObject);
				}
			}
			return RetAppUtil.success(jsonArray, "查询成功");
		}else{
			throw new ServiceException(RestApiExceptionEnums.SIGN_ERROR);
		}
	}

	@RequestMapping(value = "/umeng/kpi/event/group", produces = { "application/json" }, method = RequestMethod.POST)
	public RetApp eventGroup(HttpServletRequest request, HttpServletResponse response) {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appId);
		String startDate=request.getParameter("startDate");
		String endDate=request.getParameter("endDate");
		String sign=getParam(request,"sign","");

		Map paramMap=new HashMap();
		paramMap.put(OAuthConstants.OAUTH_APP_ID,appId);
		paramMap.put("startDate",startDate);
		paramMap.put("endDate",endDate);
		if(CspSignUtil.checkSign(paramMap,openAppInfo.getAppSecret(),sign)) {
			List<LjhKpiEventGroup> list = ljhKpiEventGroupService.selectReportForDate(DateUtils.parseDate(startDate), DateUtils.parseDate(endDate));
			JSONArray jsonArray = new JSONArray();
			if (list != null && list.size() > 0) {
				for (LjhKpiEventGroup entry : list) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("groupName", entry.getGroupName());
					jsonObject.put("kpiDate", entry.getKpiDateStr());
					jsonObject.put("name", entry.getName());
					jsonObject.put("displayName", entry.getDisplayName());
					jsonObject.put("num", entry.getNum());
					jsonArray.add(jsonObject);
				}
			}
			return RetAppUtil.success(jsonArray, "查询成功");
		}else{
			throw new ServiceException(RestApiExceptionEnums.SIGN_ERROR);
		}
	}
}

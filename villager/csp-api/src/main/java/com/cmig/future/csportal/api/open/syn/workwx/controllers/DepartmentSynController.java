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
import com.cmig.future.csportal.module.weixin.entry.Department;
import com.cmig.future.csportal.module.weixin.helper.WorkWxHelper;
import com.cmig.future.csportal.module.weixin.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * BaseCompanySynController
 * 公司同步
 * <p>
 * Created by bubu on 2017/3/21.
 */
@RestController
@RequestMapping(value = "${commonPath}/")
public class DepartmentSynController extends BaseExtendController {

	private static Logger logger = LoggerFactory.getLogger(DepartmentSynController.class);
	@Autowired
	private DepartmentService departmentService;

	/**
	 * 组织新增
	 * @param department
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/syn/department/create", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp create(@ModelAttribute Department department, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId=getParam(request,"companyId","");

		if(StringUtils.isEmpty(companyId)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp= WorkWxHelper.getCorp(appId,companyId);
		if(null!=corp){
			int departmentId=departmentService.create(corp.getCorpid(),department);
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("departmentId",departmentId);
			return RetAppUtil.success(jsonObject,"新增成功");
		}else{
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}

	@RequestMapping(value = "/syn/department/update", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp update(@ModelAttribute Department department, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId=getParam(request,"companyId","");

		if(StringUtils.isEmpty(companyId)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp= WorkWxHelper.getCorp(appId,companyId);
		if(null!=corp){
			departmentService.update(corp.getCorpid(),department);
			return RetAppUtil.success("修改成功");
		}else{
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}

	@RequestMapping(value = "/syn/department/delete", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp delete(@ModelAttribute Department department, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId=getParam(request,"companyId","");

		if(StringUtils.isEmpty(companyId)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp= WorkWxHelper.getCorp(appId,companyId);
		if(null!=corp){
			departmentService.delete(corp.getCorpid(),department.getId());
			return RetAppUtil.success("删除成功");
		}else{
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}

	@RequestMapping(value = "/syn/department/getList", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp getList(@ModelAttribute Department department, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		// 校验appid
		OAuthUtils.checkOAuth2AppID(appId);
		String companyId=getParam(request,"companyId","");

		if(StringUtils.isEmpty(companyId)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		Corp corp= WorkWxHelper.getCorp(appId,companyId);
		if(null!=corp){
			List<Department> departmentList= departmentService.getList(corp.getCorpid(),department.getParentid());
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("departmentList",departmentList);
			return RetAppUtil.success(jsonObject,"查询成功");
		}else{
			logger.error("该公司暂未开启微信企业号");
			throw new ServiceException(RestApiExceptionEnums.ENABLED_WORK_WX);
		}
	}
}

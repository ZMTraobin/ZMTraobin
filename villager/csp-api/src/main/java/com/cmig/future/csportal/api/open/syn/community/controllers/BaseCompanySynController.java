package com.cmig.future.csportal.api.open.syn.community.controllers;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.oauth2.OAuthConstants;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.properties.company.dto.LjhBaseCompany;
import com.cmig.future.csportal.module.properties.company.service.ILjhBaseCompanyService;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * BaseCompanySynController
 * 公司同步
 * <p>
 * Created by bubu on 2017/3/21.
 */
@Controller
@ResponseBody
@RequestMapping(value = "${commonPath}/")
public class BaseCompanySynController extends BaseExtendController {

	private static Logger logger = LoggerFactory.getLogger(BaseCompanySynController.class);
	@Autowired
	private ILjhBaseCompanyService ljhBaseCompanyService;

	/**
	 * 公司新增
	 *
	 * @param company
	 * @param request
	 * @param response
	 * @return
	 * @author bubu
	 */
	@RequestMapping(value = "/syn/company/create", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp create(@ModelAttribute LjhBaseCompany company, HttpServletRequest request, HttpServletResponse response) {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		String sourceSystemId = getParam(request, "sourceSystemId", "");
		String companyName = getParam(request, "companyName", "");
		String serverUrl = getParam(request, "serverUrl", "");
		// 校验appid
		OpenAppInfo openAppInfo=OAuthUtils.getOpenAppInfo(appId);

		if (StringUtils.isEmpty(sourceSystemId)) {
			throw new DataWarnningException("公司ID不能为空");
		}
		if (StringUtils.isEmpty(companyName)) {
			throw new DataWarnningException("公司名称不能为空");
		}
		if (StringUtils.isEmpty(serverUrl)) {
			throw new DataWarnningException("服务器域名不能为空");
		}

		LjhBaseCompany companyQuery = new LjhBaseCompany();
		companyQuery.setOpenAppId(openAppInfo.getId());
		companyQuery.setSourceSystemId(sourceSystemId);
		companyQuery = ljhBaseCompanyService.getBySourceSystemId(companyQuery);
		if (null != companyQuery) {
			throw new DataWarnningException("该公司已存在，不能重复新增");
		}

		company.setOpenAppId(openAppInfo.getId());
		ljhBaseCompanyService.save(company);
		return RetAppUtil.success(company,"新增成功");

	}


	/**
	 * 公司修改
	 *
	 * @param company
	 * @param request
	 * @param response
	 * @return
	 * @author
	 */
	@RequestMapping(value = "/syn/company/update", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp update(@ModelAttribute LjhBaseCompany company, HttpServletRequest request, HttpServletResponse response) {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		String sourceSystemId = getParam(request, "sourceSystemId", "");
		String companyName = getParam(request, "companyName", "");
		String serverUrl = getParam(request, "serverUrl", "");
		// 校验appid
		OpenAppInfo openAppInfo=OAuthUtils.getOpenAppInfo(appId);

		if (StringUtils.isEmpty(sourceSystemId)) {
			throw new DataWarnningException("公司ID不能为空");
		}
		if (StringUtils.isEmpty(companyName)) {
			throw new DataWarnningException("公司名称不能为空");
		}
		if (StringUtils.isEmpty(serverUrl)) {
			throw new DataWarnningException("服务器域名不能为空");
		}


		LjhBaseCompany companyQuery = new LjhBaseCompany();
		companyQuery.setOpenAppId(openAppInfo.getId());
		companyQuery.setSourceSystemId(sourceSystemId);
		companyQuery = ljhBaseCompanyService.getBySourceSystemId(companyQuery);
		if (null == companyQuery) {
			throw new DataWarnningException("该公司不存在");
		}

		companyQuery.setCompanyCode(company.getCompanyCode());
		companyQuery.setCompanyName(company.getCompanyName());
		companyQuery.setServerUrl(company.getServerUrl());
		ljhBaseCompanyService.save(companyQuery);
		return RetAppUtil.success("修改成功");
	}


	@RequestMapping(value = "/syn/company/delete", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp delete(@ModelAttribute LjhBaseCompany company, HttpServletRequest request, HttpServletResponse response) {
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		String sourceSystemId = getParam(request, "sourceSystemId", "");
		// 校验appid
		OpenAppInfo openAppInfo=OAuthUtils.getOpenAppInfo(appId);

		if (StringUtils.isEmpty(sourceSystemId)) {
			throw new DataWarnningException("公司ID不能为空");
		}

		LjhBaseCompany companyQuery = new LjhBaseCompany();
		companyQuery.setOpenAppId(openAppInfo.getId());
		companyQuery.setSourceSystemId(sourceSystemId);
		companyQuery = ljhBaseCompanyService.getBySourceSystemId(companyQuery);
		if (null == companyQuery) {
			throw new DataWarnningException("该公司不存在");
		}

		ljhBaseCompanyService.deleteByPrimaryKey(companyQuery);
		return RetAppUtil.success("删除成功");
	}
}

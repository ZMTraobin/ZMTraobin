package com.cmig.future.csportal.module.kpi.controllers;

import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.kpi.UmengKpiHelper;
import com.cmig.future.csportal.module.kpi.dto.UmengApp;
import com.cmig.future.csportal.module.kpi.response.UmengBaseInfo;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiDurationService;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiEventGroupService;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiResultService;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiUserService;
import com.cmig.future.csportal.module.kpi.service.IUmengAppService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class UmengAppController extends BaseController {

	private static final Logger logger= LoggerFactory.getLogger(UmengAppController.class);
	@Autowired
	private IUmengAppService service;

	@Autowired
	private IUmengAppService umengAppService;

	@Autowired
	private ILjhKpiUserService ljhKpiUserService;

	@Autowired
	private ILjhKpiDurationService ljhKpiDurationService;

	@Autowired
	private ILjhKpiEventGroupService ljhKpiEventGroupService;

	@Autowired
	private ILjhKpiResultService ljhKpiResultService;


	@RequestMapping(value = "/csp/umeng/app/query")
	@ResponseBody
	public ResponseData query(UmengApp dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
	                          @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/csp/umeng/app/submit")
	@ResponseBody
	public ResponseData update(HttpServletRequest request, @RequestBody List<UmengApp> dto) {
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.batchUpdate(requestCtx, dto));
	}

	@RequestMapping(value = "/csp/umeng/app/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<UmengApp> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	@RequestMapping(value = "/csp/umeng/all/base")
	@ResponseBody
	public ResponseData baseInfo(UmengApp dto, HttpServletRequest request) {
		List<UmengBaseInfo> list=new ArrayList<>();
		UmengBaseInfo umengBaseInfo;
		try {
			umengBaseInfo= UmengKpiHelper.getAllBaseInfo();

			UmengBaseInfo today=new UmengBaseInfo();
			today.setTitle("今日");
			today.setToday_new_users(umengBaseInfo.getToday_new_users());
			today.setToday_active_users(umengBaseInfo.getToday_active_users());
			today.setToday_launches(umengBaseInfo.getToday_launches());
			today.setInstallations(umengBaseInfo.getInstallations());
			list.add(today);

			UmengBaseInfo yestday=new UmengBaseInfo();
			yestday.setTitle("昨日");
			yestday.setToday_new_users(umengBaseInfo.getYesterday_new_users());
			yestday.setToday_active_users(umengBaseInfo.getYesterday_active_users());
			yestday.setToday_launches(umengBaseInfo.getYesterday_launches());
			yestday.setInstallations(umengBaseInfo.getInstallations()-umengBaseInfo.getToday_new_users());

			list.add(yestday);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseData(list);
	}


	@RequestMapping(value = "/csp/umeng/syn/history")
	@ResponseBody
	public ResponseData historySyn(HttpServletRequest request) {
		try {
			String startDate=request.getParameter("startDate");
			Date start=DateUtils.parseDate(startDate,"yyyy-MM-dd");
			Date yestday=DateUtils.addDays(new Date(),-1);
			String endDate=request.getParameter("endDate");
			if(!StringUtils.isEmpty(endDate)){
				yestday=DateUtils.parseDate(endDate,"yyyy-MM-dd");
			}

			umengAppService.synApps();
			String type=request.getParameter("type")==null?"1":request.getParameter("type");

			while (start.compareTo(yestday)<0){
				String date= DateUtils.formatDate(start,"yyyy-MM-dd");
				logger.debug("正在同步的日期 {}",date);
				switch (type){
					case "1":
						ljhKpiUserService.synUserKpi(date);
						break;
					case "2":
						ljhKpiDurationService.synDurationKpi(date, UmengKpiHelper.PERIOD_TYPE_DAILY);
						ljhKpiDurationService.synDurationKpi(date, UmengKpiHelper.PERIOD_TYPE_DAILY_PER_LAUNCH);
						break;
					case "3":
						ljhKpiEventGroupService.synEventGroupKpi(date);
						break;
					case "4":
						ljhKpiResultService.runResult(DateUtils.parseDate(date));
						break;
					case "all":
						ljhKpiUserService.synUserKpi(date);
						ljhKpiDurationService.synDurationKpi(date, UmengKpiHelper.PERIOD_TYPE_DAILY);
						ljhKpiDurationService.synDurationKpi(date, UmengKpiHelper.PERIOD_TYPE_DAILY_PER_LAUNCH);
						ljhKpiEventGroupService.synEventGroupKpi(date);
						ljhKpiResultService.runResult(DateUtils.parseDate(date));
						break;
				}
				start=DateUtils.addDays(start,1);
			}
			return new ResponseData(true);
		}catch (Exception e){
			e.printStackTrace();
		}
		return new ResponseData(false);
	}
}
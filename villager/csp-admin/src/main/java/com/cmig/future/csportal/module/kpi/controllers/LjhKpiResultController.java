package com.cmig.future.csportal.module.kpi.controllers;

import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.kpi.dto.LjhKpiResult;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiResultService;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Controller
public class LjhKpiResultController extends BaseController {

	private static final Logger logger= LoggerFactory.getLogger(LjhKpiResultController.class);

	@Autowired
	private ILjhKpiResultService service;


	@RequestMapping(value = "/csp/ljh/kpi/result/query")
	@ResponseBody
	public ResponseData query(LjhKpiResult dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
	                          @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {

		List<LjhKpiResult> list=service.select(dto, page, pageSize);
		return new ResponseData(list);
	}

	@RequestMapping(value = "/csp/ljh/kpi/result/submit")
	@ResponseBody
	public ResponseData update(HttpServletRequest request, @RequestBody List<LjhKpiResult> dto) {
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.batchUpdate(requestCtx, dto));
	}

	@RequestMapping(value = "/csp/ljh/kpi/result/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<LjhKpiResult> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	@RequestMapping(value = "/csp/ljh/kpi/result/run")
	@ResponseBody
	public ResponseData run(HttpServletRequest request) throws Exception {
		String startDate=request.getParameter("startDate");
		Date start= DateUtils.parseDate(startDate,"yyyy-MM-dd");
		Date yestday=DateUtils.addDays(new Date(),-1);
		String endDate=request.getParameter("endDate");
		if(!StringUtils.isEmpty(endDate)){
			yestday=DateUtils.parseDate(endDate,"yyyy-MM-dd");
		}
		while (start.compareTo(yestday)<0) {
			String date = DateUtils.formatDate(start, "yyyy-MM-dd");
			logger.debug("正在计算的日期 {}", date);
			service.runResult(start);
			start=DateUtils.addDays(start,1);
		}
		return new ResponseData(true);
	}

	@RequestMapping(value = "/csp/ljh/kpi/result/report")
	@ResponseBody
	public ResponseData report(@RequestParam(defaultValue = "30") int days,HttpServletRequest request) {
		Date yestday=DateUtils.addDays(new Date(),-1);
		LjhKpiResult dto =new LjhKpiResult();
		List<LjhKpiResult> list=service.select(dto, 1, 1);
		if(list!=null&&list.size()>0){
			yestday=list.get(0).getKpiDate();
		}
		dto.setStartDate(DateUtils.addDays(yestday,-days));
		dto.setEndDate(yestday);
		list=service.getReportForDate(dto);
		Collections.sort(list, new Comparator<LjhKpiResult>() {
			@Override
			public int compare(LjhKpiResult o1, LjhKpiResult o2) {
				return o1.getKpiDate().compareTo(o2.getKpiDate());
			}
		});
		return new ResponseData(list);
	}
}
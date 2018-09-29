package com.cmig.future.csportal.module.kpi.controllers;

import com.cmig.future.csportal.module.kpi.dto.LjhKpiEventGroup;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiEventGroupService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class LjhKpiEventGroupController extends BaseController {

	@Autowired
	private ILjhKpiEventGroupService service;


	@RequestMapping(value = "/csp/ljh/kpi/event/group/query")
	@ResponseBody
	public ResponseData query(LjhKpiEventGroup dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
	                          @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/csp/ljh/kpi/event/group/submit")
	@ResponseBody
	public ResponseData update(HttpServletRequest request, @RequestBody List<LjhKpiEventGroup> dto) {
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.batchUpdate(requestCtx, dto));
	}

	@RequestMapping(value = "/csp/ljh/kpi/event/group/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<LjhKpiEventGroup> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	@RequestMapping(value = "/csp/ljh/kpi/event/group/report")
	@ResponseBody
	public ResponseData report(@RequestParam(defaultValue = "30") int days,
	                           @RequestParam(defaultValue = "10") int topNum,HttpServletRequest request) {
		List<LjhKpiEventGroup> list= service.selectReport(days,topNum);
		return new ResponseData(list);
	}
}
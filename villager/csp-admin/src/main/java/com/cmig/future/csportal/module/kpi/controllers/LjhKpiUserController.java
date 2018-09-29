package com.cmig.future.csportal.module.kpi.controllers;

import com.cmig.future.csportal.module.kpi.dto.LjhKpiUser;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiUserService;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class LjhKpiUserController extends BaseController {

	@Autowired
	private ILjhKpiUserService service;


	@RequestMapping(value = "/csp/ljh/kpi/user/query")
	@ResponseBody
	public ResponseData query(LjhKpiUser dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
	                          @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
		IRequest requestContext = createRequestContext(request);
		return new ResponseData(service.select(requestContext, dto, page, pageSize));
	}

	@RequestMapping(value = "/csp/ljh/kpi/user/submit")
	@ResponseBody
	public ResponseData update(HttpServletRequest request, @RequestBody List<LjhKpiUser> dto) {
		IRequest requestCtx = createRequestContext(request);
		return new ResponseData(service.batchUpdate(requestCtx, dto));
	}

	@RequestMapping(value = "/csp/ljh/kpi/user/remove")
	@ResponseBody
	public ResponseData delete(HttpServletRequest request, @RequestBody List<LjhKpiUser> dto) {
		service.batchDelete(dto);
		return new ResponseData();
	}

	@RequestMapping(value = "/csp/ljh/kpi/user/report")
	@ResponseBody
	public ResponseData report(LjhKpiUser dto,@RequestParam(defaultValue = "30") int days,HttpServletRequest request) {
		List<LjhKpiUser> list= service.selectReport(dto);
		Collections.sort(list, new Comparator<LjhKpiUser>() {
			@Override
			public int compare(LjhKpiUser o1, LjhKpiUser o2) {
				return o1.getKpiDate().compareTo(o2.getKpiDate());
			}
		});
		return new ResponseData(list);
	}

}
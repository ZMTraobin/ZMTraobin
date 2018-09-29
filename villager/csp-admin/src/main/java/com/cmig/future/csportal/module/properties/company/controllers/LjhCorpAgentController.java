package com.cmig.future.csportal.module.properties.company.controllers;

import com.cmig.future.csportal.module.properties.company.dto.LjhBaseCompany;
import com.cmig.future.csportal.module.properties.company.dto.LjhCorpAgent;
import com.cmig.future.csportal.module.properties.company.service.ILjhCorpAgentService;
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
public class LjhCorpAgentController extends BaseController {

    @Autowired
    private ILjhCorpAgentService service;


    @RequestMapping(value = "/csp/ljh/corp/agent/query")
    @ResponseBody
    public ResponseData query(LjhCorpAgent dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.findAgent(dto));
    }

    @RequestMapping(value = "/csp/ljh/corp/agent/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<LjhCorpAgent> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/ljh/corp/agent/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<LjhCorpAgent> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

//    @RequestMapping(value = "/csp/ljh/base/company/corp/query")
//    @ResponseBody
//    public ResponseData update(HttpServletRequest request, LjhCorpAgent ljhCorpAgent) {
//        IRequest requestCtx = createRequestContext(request);
//        return new ResponseData(service.selectCorpAgents(requestCtx, ljhCorpAgent));
//    }
}
package com.cmig.future.csportal.module.operate.neighbor.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighTopicView;
import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopic;
import com.cmig.future.csportal.module.operate.neighbor.service.ILjhNeighborTopicService;
import com.hand.hap.core.BaseConstants;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;

@Controller
public class LjhNeighborTopicController extends BaseController {

    @Override
    public void initBinder(WebDataBinder binder, HttpServletRequest request) {
        SimpleDateFormat df = new SimpleDateFormat(BaseConstants.DATE_TIME_FORMAT);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df, false));
    }

    @Autowired
    private ILjhNeighborTopicService service;

    @RequestMapping(value = "/csp/ljh/neighbor/topic/query")
    @ResponseBody
    public ResponseData query(LjhNeighTopicView dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryByCondition(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/csp/ljh/neighbor/topic/queryById")
    @ResponseBody
    public ResponseData queryById(LjhNeighTopicView dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryById(requestContext, dto));
    }

    @RequestMapping(value = "/csp/ljh/neighbor/topic/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<LjhNeighborTopic> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/ljh/neighbor/topic/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<LjhNeighborTopic> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}
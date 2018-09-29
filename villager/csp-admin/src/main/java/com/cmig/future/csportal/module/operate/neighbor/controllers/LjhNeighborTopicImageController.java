package com.cmig.future.csportal.module.operate.neighbor.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicImage;
import com.cmig.future.csportal.module.operate.neighbor.service.ILjhNeighborTopicImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

    @Controller
    public class LjhNeighborTopicImageController extends BaseController{

    @Autowired
    private ILjhNeighborTopicImageService service;


    @RequestMapping(value = "/csp/ljh/neighbor/topic/image/query")
    @ResponseBody
    public ResponseData query(LjhNeighborTopicImage dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/csp/ljh/neighbor/topic/image/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<LjhNeighborTopicImage> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/ljh/neighbor/topic/image/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<LjhNeighborTopicImage> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }
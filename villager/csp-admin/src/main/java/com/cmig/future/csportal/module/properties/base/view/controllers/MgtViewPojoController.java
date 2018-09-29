package com.cmig.future.csportal.module.properties.base.view.controllers;

import com.cmig.future.csportal.module.properties.base.view.dto.MgtViewPojo;
import com.cmig.future.csportal.module.properties.base.view.service.IMgtViewPojoService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MgtViewPojoController extends BaseController{

    @Autowired
    private IMgtViewPojoService mgtViewPojoService;


    @RequestMapping(value = "/csp/mgt/viewPojo/query")
    @ResponseBody
    public ResponseData query(MgtViewPojo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        List<MgtViewPojo> list =mgtViewPojoService.findList(requestContext,dto,page,pageSize);
        return new ResponseData(list);
    }

    @RequestMapping(value = "/csp/mgt/viewPojo/showTree")
    @ResponseBody
    public ResponseData showTree(HttpServletRequest request,MgtViewPojo dto){
        dto.setStatus(dto.STATUS_NORMAL);
        List<MgtViewPojo> list = mgtViewPojoService.findList(dto);
        return new ResponseData(list);
    }

}
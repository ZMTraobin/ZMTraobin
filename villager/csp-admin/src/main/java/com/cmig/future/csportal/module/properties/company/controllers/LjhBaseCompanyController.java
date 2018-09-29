package com.cmig.future.csportal.module.properties.company.controllers;

import com.cmig.future.csportal.module.properties.company.dto.LjhBaseCompany;
import com.cmig.future.csportal.module.properties.company.dto.LjhCorpAgent;
import com.cmig.future.csportal.module.properties.company.exception.CompanyException;
import com.cmig.future.csportal.module.properties.company.service.ILjhBaseCompanyService;
import com.cmig.future.csportal.module.weixin.helper.WorkWxHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class LjhBaseCompanyController extends BaseController {

    @Autowired
    private ILjhBaseCompanyService service;


    @RequestMapping(value = "/csp/ljh/base/company/query")
    @ResponseBody
    public ResponseData query(LjhBaseCompany dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectByCondition(requestContext, dto, page, pageSize));
    }

//@RequestMapping(value = "/csp/ljh/base/company/submit")
//@ResponseBody
//public ResponseData update(HttpServletRequest request,@RequestBody List<LjhBaseCompany> dto){
//    IRequest requestCtx = createRequestContext(request);
//    return new ResponseData(service.batchUpdate(requestCtx, dto));
//}

    @RequestMapping(value = "/csp/ljh/base/company/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<LjhBaseCompany> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/csp/ljh/base/company/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<LjhBaseCompany> dto, BindingResult result) throws CompanyException {
        getValidator().validate(dto,result);
        if(result.hasErrors()){
            ResponseData rd = new ResponseData(false);
            rd.setMessage(getErrorMessage(result, request));
            return rd;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.updateCompany(requestCtx, dto));
    }



}
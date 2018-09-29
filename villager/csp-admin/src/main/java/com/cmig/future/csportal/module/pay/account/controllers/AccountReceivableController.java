package com.cmig.future.csportal.module.pay.account.controllers;

import com.cmig.future.csportal.module.base.entity.ResponseExtData;
import com.cmig.future.csportal.module.pay.account.dto.AccountReceivable;
import com.cmig.future.csportal.module.pay.account.service.IAccountReceivableService;
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
public class AccountReceivableController extends BaseController {

    @Autowired
    private IAccountReceivableService accountReceivableService;


    @RequestMapping(value = "/csp/account/receivable/query")
    @ResponseBody
    public ResponseData query(AccountReceivable dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(accountReceivableService.findList(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/csp/account/receivable/get")
    @ResponseBody
    public ResponseData get(AccountReceivable dto,HttpServletRequest request) {
        dto=accountReceivableService.get(dto);
        return new ResponseExtData(dto);
    }


    @RequestMapping(value = "/csp/account/receivable/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody AccountReceivable dto,BindingResult result) {
        IRequest requestCtx = createRequestContext(request);
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        accountReceivableService.updateByPrimaryKeySelective(requestCtx, dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/csp/account/receivable/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<AccountReceivable> dto) {
        accountReceivableService.batchDeleteByUpdate(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/csp/account/receivable/add")
    @ResponseBody
    public ResponseData add(HttpServletRequest request, @RequestBody AccountReceivable dto, BindingResult result) {
        IRequest requestCtx = createRequestContext(request);
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }

        accountReceivableService.insertSelective(requestCtx,dto);
        return new ResponseData();
    }
}
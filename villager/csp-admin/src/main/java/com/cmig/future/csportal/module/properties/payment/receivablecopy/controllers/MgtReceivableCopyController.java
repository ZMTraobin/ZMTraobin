package com.cmig.future.csportal.module.properties.payment.receivablecopy.controllers;

import com.cmig.future.csportal.module.properties.payment.receivablecopy.dto.MgtReceivableCopy;
import com.cmig.future.csportal.module.properties.payment.receivablecopy.service.IMgtReceivableCopyService;
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
import java.util.List;

@Controller
public class MgtReceivableCopyController extends BaseController {

	private final static Logger logger= LoggerFactory.getLogger(MgtReceivableCopyController.class);

    @Autowired
    private IMgtReceivableCopyService service;

    private static String add_url = "/common/receivable/add";
    private static String update_url = "/common/receivable/update";
    private static String unable_url = "/common/receivable/unable";
    private static String queryStatus_url = "/common/receivable/queryStatus";
    private static String list_url = "/common/receivable/list";
    private static String notify_url = "/user/mgt/payment/receivable/notifyDemo";

    @RequestMapping(value = "/csp/mgt/receivable/copy/query")
    @ResponseBody
    public ResponseData query(MgtReceivableCopy dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }


    @RequestMapping(value = "/csp/mgt/receivable/copy/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<MgtReceivableCopy> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/mgt/receivable/copy/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<MgtReceivableCopy> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }


}
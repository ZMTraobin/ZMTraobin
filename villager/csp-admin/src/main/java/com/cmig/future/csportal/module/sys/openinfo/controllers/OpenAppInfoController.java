package com.cmig.future.csportal.module.sys.openinfo.controllers;

import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.module.base.entity.ResponseExtData;
import com.cmig.future.csportal.module.pay.payment.dto.PaymentConfig;
import com.cmig.future.csportal.module.pay.payment.service.IPaymentConfigService;
import com.cmig.future.csportal.module.sys.openinfo.OpenAppUtils;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import com.cmig.future.csportal.module.sys.openinfo.service.IOpenAppInfoService;
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
public class OpenAppInfoController extends BaseController {

    @Autowired
    private IOpenAppInfoService service;

    @Autowired
    private IPaymentConfigService paymentConfigService;


    @RequestMapping(value = "/ljh/open/app/info/query")
    @ResponseBody
    public ResponseData query(OpenAppInfo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectOppInfo(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/ljh/open/app/info/get")
    @ResponseBody
    public ResponseData get(OpenAppInfo dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        dto=service.getOpenInfoById(dto);

      //  PaymentConfig paymentConfig = new PaymentConfig();

     /*   if(dto!=null){
            if(dto.getPaymentConfigId()!=null){
                paymentConfig.setId(dto.getPaymentConfigId());
                paymentConfig = paymentConfigService.getPaymentById(paymentConfig);
                if(paymentConfig.getPingAppName()!=null && !paymentConfig.getPingAppName().equals("")){
                    dto.setPingAppName(paymentConfig.getPingAppName());
                }
            }

        }*/
        if(null==dto){
            dto=new OpenAppInfo();
            dto.setAppKey(IdGen.uuid());
            dto.setAppSecret(IdGen.uuid());
        }
        return new ResponseExtData(dto);
    }

    @RequestMapping(value = "/ljh/open/app/info/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody OpenAppInfo dto,BindingResult result) {
        IRequest requestCtx = createRequestContext(request);
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }

        service.updateByPrimaryKeySelective(requestCtx, dto);
        OpenAppUtils.cleanCacheOpenappMap();
        return new ResponseData();
    }

    @RequestMapping(value = "/ljh/open/app/info/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<OpenAppInfo> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/ljh/open/app/info/add")
    @ResponseBody
    public ResponseData add(HttpServletRequest request, @RequestBody OpenAppInfo dto,BindingResult result) {
        IRequest requestCtx = createRequestContext(request);
       // dto.setPingAppId(Long.parseLong(dto.getPingAppId()));
       // String pingappid = dto.getPingAppId();
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }

        dto.setId(IdGen.uuid());
        service.save(dto);
       // service.insertSelective(requestCtx,dto);
        OpenAppUtils.cleanCacheOpenappMap();
        return new ResponseData();
    }
}
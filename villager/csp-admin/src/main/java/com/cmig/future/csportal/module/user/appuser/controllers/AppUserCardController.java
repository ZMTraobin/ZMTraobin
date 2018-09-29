package com.cmig.future.csportal.module.user.appuser.controllers;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.verify.RSAUtilsWithKey;
import com.cmig.future.csportal.module.user.appuser.dto.AppUserCard;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserCardService;
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
import java.util.ArrayList;
import java.util.List;

@Controller
    public class AppUserCardController extends BaseController{

    @Autowired
    private IAppUserCardService service;

    @RequestMapping(value = "/csp/app/user/card/query")
    @ResponseBody
    public ResponseData query(AppUserCard dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/csp/app/user/card/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<AppUserCard> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/app/user/card/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<AppUserCard> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/csp/app/user/card/list")
    public ResponseData cardList(AppUserCard appUserCard,@RequestParam(defaultValue = DEFAULT_PAGE) int page,
                           @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ResponseData data = new ResponseData();
        try {
            List<AppUserCard> list = service.findList(requestContext,appUserCard,page,pageSize);
            if(null!=list&&list.size()>0){
                for(AppUserCard dto:list){
                    int length=dto.getIdNo().length();
                    String temp=dto.getIdNo().substring(0,dto.getIdNo().length()-2).replaceAll("\\d","*");
                    String idNoTemp=dto.getIdNo().replaceAll("(\\d{1})\\d{"+(length-2)+"}(\\d{1})","$1"+temp+"$2");
                    dto.setIdNo(idNoTemp);
                    String cardNo=dto.getCardNo();
                    cardNo= RSAUtilsWithKey.decrypt(cardNo);
                    length=cardNo.length();
                    temp=cardNo.substring(0,cardNo.length()-4).replaceAll("\\d","*");
                    String cardNoTemp=cardNo.replaceAll("\\d{"+(length-4)+"}(\\d{4})",temp+"$1");
                    dto.setCardNo(cardNoTemp);
                    String bankMobileTemp=dto.getBankMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
                    dto.setBankMobile(bankMobileTemp);
                }
            }
            return new ResponseData(list);
        }catch (DataWarnningException e) {
            e.printStackTrace();
            data.setRows(new ArrayList<AppUserCard>());
            data.setSuccess(false);
            data.setMessage("查询失败："+e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            data.setRows(new ArrayList<AppUserCard>());
            data.setSuccess(false);
            data.setMessage("查询失败："+e.getMessage());
        }
        return data;
    }

    }
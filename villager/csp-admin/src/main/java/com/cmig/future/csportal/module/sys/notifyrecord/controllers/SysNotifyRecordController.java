package com.cmig.future.csportal.module.sys.notifyrecord.controllers;

import cn.jpush.api.common.resp.APIRequestException;
import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.Jpush.JPushPmCloundUtils;
import com.cmig.future.csportal.module.base.entity.ResponseExtData;
import net.sf.json.util.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.cmig.future.csportal.module.sys.notifyrecord.dto.SysNotifyRecord;
import com.cmig.future.csportal.module.sys.notifyrecord.service.ISysNotifyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

    @Controller
    public class SysNotifyRecordController extends BaseController{

     private static Logger logger= LoggerFactory.getLogger(SysNotifyRecordController.class);
    @Autowired
    private ISysNotifyRecordService service;

    @RequestMapping(value = "/ljh/sys/notify/record/query")
    @ResponseBody
    public ResponseData query(SysNotifyRecord dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/ljh/sys/notify/record/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<SysNotifyRecord> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/ljh/sys/notify/record/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<SysNotifyRecord> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/ljh/sys/notify/record/list")
    @ResponseBody
    public ResponseData list(SysNotifyRecord dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.findList(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/ljh/sys/notify/record/repush")
    @ResponseBody
    public ResponseData repush(SysNotifyRecord sysNotifyRecord, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        SysNotifyRecord dto = service.selectByPrimaryKey(requestContext,sysNotifyRecord);
        JPushPmCloundUtils.pushAlertWithMessageSyn(dto);
        String status = dto.getStatus();
        if(status == Constants.NOTIFY_STATUS_SUCCESS){
            dto.setRemark("");
            return new ResponseExtData(service.updateByPrimaryKeySelective(requestContext,dto));
        }else{
            return new ResponseData();
        }
    }

    }
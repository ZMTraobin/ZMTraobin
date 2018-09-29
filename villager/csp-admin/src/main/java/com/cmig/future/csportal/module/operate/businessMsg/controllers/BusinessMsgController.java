package com.cmig.future.csportal.module.operate.businessMsg.controllers;

import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.operate.businessMsg.service.IBusinessMsgService;
import com.cmig.future.csportal.module.sys.notifyrecord.constants.NotifyMessageTypeEnum;
import com.cmig.future.csportal.module.sys.notifyrecord.dto.SysNotifyRecord;
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
    public class BusinessMsgController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(BusinessMsgController.class);

    @Autowired
    private IBusinessMsgService businessMsgService;

    @RequestMapping(value = "/businessMsg/query")
    @ResponseBody
    public ResponseData query(SysNotifyRecord dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(businessMsgService.findListByMsg(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/businessMsg/delete")
    @ResponseBody
    public ResponseData delete(SysNotifyRecord dto,HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        SysNotifyRecord sysNotifyRecord = businessMsgService.selectByPrimaryKey(requestContext,dto);
        dto.setMessageType(sysNotifyRecord.getMessageType());
        dto.setCategory(sysNotifyRecord.getCategory());
        dto.setCreationDate(sysNotifyRecord.getCreationDate());
        int result = businessMsgService.deleteByTimeAndCateoryAndType(dto);
        ResponseData responseData = new ResponseData();
        if(result > 0){
            responseData.setSuccess(true);
        }else{
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @RequestMapping(value = "/businessMsg/groupByTimeAndCateoryAndType")
    @ResponseBody
    public ResponseData groupByTimeAndCateoryAndType(SysNotifyRecord dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        SysNotifyRecord sysNotifyRecord = businessMsgService.selectByPrimaryKey(requestContext,dto);
        dto.setMessageType(sysNotifyRecord.getMessageType());
        dto.setCategory(sysNotifyRecord.getCategory());
        dto.setCreationDate(sysNotifyRecord.getCreationDate());
        return new ResponseData(businessMsgService.groupByTimeAndCateoryAndType(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/businessMsg/publishToAppUser")
    @ResponseBody
    public ResponseData publishToAppUser(HttpServletRequest request,@RequestBody List<SysNotifyRecord> list){
        ResponseData extData = new ResponseData(list);

        IRequest requestCtx = createRequestContext(request);
        for(SysNotifyRecord notifyRecord:list){
            String types = notifyRecord.getTypes();
            String mobiles = notifyRecord.getMobiles();
            if(StringUtils.isEmpty(mobiles)){
                extData.setSuccess(false);
                extData.setMessage("不存在手机号!");
            }
            String content = notifyRecord.getContent();
            String category = notifyRecord.getCategory();
            if(types.indexOf(NotifyMessageTypeEnum.PUSH.getCode()) != -1){//PUSH推送
                businessMsgService.pushToAppUser(requestCtx,category,content,mobiles);
            }
            if(types.indexOf(NotifyMessageTypeEnum.SMS.getCode()) != -1){//SMS短信
                businessMsgService.smsToAppUser(requestCtx,category,content,mobiles);
            }
        }

        return extData;
    }


    }
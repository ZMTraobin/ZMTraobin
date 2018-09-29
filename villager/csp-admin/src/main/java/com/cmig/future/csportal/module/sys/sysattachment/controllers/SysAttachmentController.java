package com.cmig.future.csportal.module.sys.sysattachment.controllers;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.oauth2.exceptions.OAuth2Exception;
import com.cmig.future.csportal.module.base.entity.ResponseExtData;
import com.cmig.future.csportal.module.operate.articleNotify.dto.ArticleNotify;
import com.cmig.future.csportal.module.sys.enumType.NotifyType;
import com.cmig.future.csportal.module.sys.sysattachment.dto.SysAttachment;
import com.cmig.future.csportal.module.sys.sysattachment.service.ISysAttachmentService;
import com.cmig.future.csportal.module.sys.utils.AttachmentUtils;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
    public class SysAttachmentController extends BaseController {

    @Autowired
    private ISysAttachmentService sysAttachmentService;


    @RequestMapping(value = "/csp/ljh/sys/attachment/query")
    @ResponseBody
    public ResponseData query(SysAttachment dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(sysAttachmentService.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/csp/ljh/sys/attachment/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<SysAttachment> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(sysAttachmentService.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/ljh/sys/attachment/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<SysAttachment> dto){
        sysAttachmentService.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/sysAttachment/saveOrUpdate")
    @ResponseBody
    public ResponseData saveOrUpdate(HttpServletRequest request,@RequestBody List<SysAttachment> dto){
        IRequest requestCtx = createRequestContext(request);
        for(SysAttachment sysAttachment:dto){
            String className = ArticleNotify.class.getName();
            String type = NotifyType.NOTIFY.name();
            sysAttachment.setObjectClass(className);
            sysAttachment.setType(type);
        }
        return new ResponseExtData(sysAttachmentService.saveOrUpdate(requestCtx,dto));
    }

    @RequestMapping(value = "/sysAttachment/deleteById")
    @ResponseBody
    public ResponseData deleteById(HttpServletRequest request,SysAttachment dto){

        return new ResponseExtData(sysAttachmentService.deleteByPrimaryKey(dto));
    }

    @ResponseBody
    @RequestMapping(value = "/sysAttachment/download", method = {RequestMethod.GET, RequestMethod.POST})
    public String downloadAttachment(SysAttachment attachment, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IRequest requestCtx = createRequestContext(request);
        try {
            return AttachmentUtils.download(attachment, request, response,requestCtx);
        }catch (DataWarnningException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (OAuth2Exception e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "新增失败";
        }
    }
    }
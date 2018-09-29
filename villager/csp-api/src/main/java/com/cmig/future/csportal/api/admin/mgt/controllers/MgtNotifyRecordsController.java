/**
 * .
 */
package com.cmig.future.csportal.api.admin.mgt.controllers;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.oauth2.exceptions.OAuth2Exception;
import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.operate.articleNotify.dto.ArticleNotify;
import com.cmig.future.csportal.module.operate.articleNotify.service.IArticleNotifyService;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUser;
import com.cmig.future.csportal.module.sys.enumType.NotifyType;
import com.cmig.future.csportal.module.sys.notifyrecord.constants.NotifyMessageTypeEnum;
import com.cmig.future.csportal.module.sys.notifyrecord.dto.SysNotifyRecord;
import com.cmig.future.csportal.module.sys.notifyrecord.service.ISysNotifyRecordService;
import com.cmig.future.csportal.module.sys.sysattachment.dto.SysAttachment;
import com.cmig.future.csportal.module.sys.utils.AdminTokenUtils;
import com.cmig.future.csportal.module.sys.utils.AttachmentUtils;
import com.github.pagehelper.Page;
import com.hand.hap.core.IRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 物业服务Controller
 *
 * @author bubu
 * @version 2017年4月10日 16:00:49
 */
@Controller//managementPath
@RequestMapping(value = "${managementPath}/notifyRecords/")
@ResponseBody
public class MgtNotifyRecordsController extends BaseExtendController {

    private static Logger logger = LoggerFactory.getLogger(MgtNotifyRecordsController.class);

    @Autowired
    private ISysNotifyRecordService sysNotifyRecordService;

    @Autowired
    private IArticleNotifyService articleNotifyService;

    @RequestMapping(value = "list", produces = { "application/json" }, method = RequestMethod.POST)
    public RetApp list(HttpServletRequest request, HttpServletResponse response,@RequestParam(defaultValue = DEFAULT_PAGE) int page,
                       @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        IRequest requestCtx = createRequestContext(request);
        RetApp retApp = new RetApp();
        String token=getParam(request,"token","");
        String type=getParam(request,"type","");
        String communityId=getParam(request,"communityId","");
        try {

            if(StringUtils.isEmpty(communityId)){
                throw new DataWarnningException("小区ID不能为空");
            }

            MgtUser userQuery = AdminTokenUtils.getUserByToken(token);

            if(!StringUtils.isEmpty(type)&&!NotifyType.contains(type)){
                throw new DataWarnningException("消息分类参数错误");
            }

            SysNotifyRecord sysNotifyRecord=new SysNotifyRecord();
            if(type != ""){
                sysNotifyRecord.setCategory(type);
            }
            sysNotifyRecord.setMgtUserId(userQuery.getId());
            sysNotifyRecord.setCommunityId(communityId);
            sysNotifyRecord.setMessageType(NotifyMessageTypeEnum.PUSH.getCode());
            //sysNotifyRecord.setDelFlag(BaseExtDTO.DEL_FLAG_NORMAL);
            List<SysNotifyRecord> list = sysNotifyRecordService.selectSysNotifyRecord(requestCtx,sysNotifyRecord,page,pageSize);

            Map<String,Object> jsonObject = new HashMap<String,Object>();
            List<Map<String,Object>> array = new ArrayList<>();
            for (SysNotifyRecord dto : list){
                Map<String,Object> obj = new HashMap<String,Object>();
                obj.put("id", dto.getId());
                obj.put("messageType", dto.getMessageType());
                obj.put("type", dto.getCategory());
                obj.put("cmsNotifyId", dto.getCmsNotifyId());
                obj.put("content", dto.getContent());
                obj.put("contentExt", dto.getContentExt()==null?"":dto.getContentExt());
                obj.put("mgtUserId", dto.getMgtUserId());
                obj.put("communityId", dto.getCommunityId());
                obj.put("appUserId", dto.getAppUserId());
                obj.put("receiverInfo", dto.getReceiverInfo());
                obj.put("readFlag", dto.getReadFlag());
                obj.put("readDate", dto.getReadDate()==null?"":DateUtils.formatDate(dto.getReadDate(),"yyyy-MM-dd HH:mm:ss"));
                obj.put("status", dto.getStatus());
                obj.put("times", dto.getTimes());
                obj.put("remark", dto.getRemark());
                obj.put("sourceSystem", dto.getSourceSystem());
                obj.put("createTime", DateUtils.formatDate(dto.getCreationDate(),"yyyy-MM-dd HH:mm:ss"));
                obj.put("updateTime", DateUtils.formatDate(dto.getLastUpdateDate(),"yyyy-MM-dd HH:mm:ss"));
                array.add(obj);
            }
            jsonObject.put("notifyList", array);

            sysNotifyRecord=new SysNotifyRecord();
            sysNotifyRecord.setCategory(type);
            sysNotifyRecord.setMgtUserId(userQuery.getId());
            sysNotifyRecord.setCommunityId(communityId);
            sysNotifyRecord.setReadFlag(Constants.NO);
            List<SysNotifyRecord> list_NORead = sysNotifyRecordService.selectSysNotifyRecord(sysNotifyRecord);
            jsonObject.put("noReadCount",pageSize!=0?list_NORead.size():pageSize);

            if (list != null&&list instanceof Page) {
                retApp.setTotall(Long.valueOf(((Page)list).getTotal()));
            }
            retApp.setData(jsonObject);
            retApp.setStatus(OK);
            retApp.setMessage("查询成功");
        } catch (DataWarnningException e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        }catch (OAuth2Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage("查询失败");
        }

        return retApp;
    }

    @RequestMapping(value = "read", produces = { "application/json" }, method = RequestMethod.POST)
    public RetApp read(SysNotifyRecord sysNotifyRecord,HttpServletRequest request, HttpServletResponse response) {
        IRequest requestCtx = createRequestContext(request);
        RetApp retApp = new RetApp();
        String sysNotifyRecordId =getParam(request,"id","");
        if (sysNotifyRecordId != null && sysNotifyRecordId != ""){
            sysNotifyRecord.setId(Long.parseLong(sysNotifyRecordId));
        }
        try {

            if(StringUtils.isEmpty(sysNotifyRecord.getId())){
                throw new DataWarnningException("消息id不能为空");
            }

            sysNotifyRecord= sysNotifyRecordService.selectByPrimaryKey(requestCtx,sysNotifyRecord);
            if(null!=sysNotifyRecord){
                sysNotifyRecord.setReadFlag(Constants.YES);
                sysNotifyRecord.setReadDate(new Date());
                sysNotifyRecord.setTimes(sysNotifyRecord.getTimes()+1);
                sysNotifyRecordService.save(sysNotifyRecord);
            }else{
                throw new DataWarnningException("参数错误");
            }

            ArticleNotify articleNotify=null;
            if(!StringUtils.isEmpty(sysNotifyRecord.getCmsNotifyId())){
                articleNotify= articleNotifyService.queryArticleNotifyById(sysNotifyRecord.getCmsNotifyId());
            }

            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id",sysNotifyRecord.getId());
            jsonObject.put("title",sysNotifyRecord.getContent());
            jsonObject.put("content",null!=articleNotify?articleNotify.getContent():"");
            jsonObject.put("type",sysNotifyRecord.getCategory());
            jsonObject.put("times",sysNotifyRecord.getTimes());
            jsonObject.put("readFlag",sysNotifyRecord.getReadFlag());
            jsonObject.put("readDate", sysNotifyRecord.getReadDate()==null?"":DateUtils.formatDate(sysNotifyRecord.getReadDate(),"yyyy-MM-dd HH:mm:ss"));
            jsonObject.put("createTime", sysNotifyRecord.getCreationDate()==null?"":DateUtils.formatDate(sysNotifyRecord.getCreationDate(),"yyyy-MM-dd HH:mm:ss"));
            jsonObject.put("updateTime", sysNotifyRecord.getLastUpdateDate()==null?"":DateUtils.formatDate(sysNotifyRecord.getLastUpdateDate(),"yyyy-MM-dd HH:mm:ss"));

            List<SysAttachment> attachments = AttachmentUtils.getAttachmentList(sysNotifyRecord.getCmsNotifyId(),ArticleNotify.class.getName(),NotifyType.NOTIFY.name());
            JSONObject json = new JSONObject();
            JSONArray array = new JSONArray();
            for (SysAttachment attachment : attachments){
                JSONObject obj = new JSONObject();
                obj.put("attachmentId", attachment.getId());
                obj.put("size", attachment.getFileSize());
                obj.put("type", attachment.getFileType());
                obj.put("name", attachment.getFileName());
                obj.put("filePath", Global.getImageServerPath()+attachment.getFilePath());
                array.add(obj);
            }
            json.put("rows", array);
            json.put("total", array.size());

            jsonObject.put("attachments",json);

            retApp.setData(jsonObject);
            retApp.setStatus(OK);
            retApp.setMessage("读取成功");
        } catch (DataWarnningException e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        }catch (OAuth2Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage("读取失败");
        }

        return retApp;
    }

    @RequestMapping(value = "delete", produces = { "application/json" }, method = RequestMethod.POST)
    public RetApp delete(SysNotifyRecord sysNotifyRecord,HttpServletRequest request, HttpServletResponse response) {
        RetApp retApp = new RetApp();
        String sysNotifyRecordId =getParam(request,"id","");
        sysNotifyRecord.setId(Long.parseLong(sysNotifyRecordId));
        try {

            String token=getParam(request,"token","");
            if(StringUtils.isEmpty(sysNotifyRecord.getId())){
                throw new DataWarnningException("消息id不能为空");
            }
            String userId= AdminTokenUtils.getUserIdByToken(token);
            sysNotifyRecord.setMgtUserId(userId);
            sysNotifyRecordService.deleteSysNotifyRecord(sysNotifyRecord);

            retApp.setStatus(OK);
            retApp.setMessage("删除成功");
        } catch (DataWarnningException e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        }catch (OAuth2Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage("删除失败");
        }

        return retApp;
    }

    @RequestMapping(value = "clean", produces = { "application/json" }, method = RequestMethod.POST)
    public RetApp clean(SysNotifyRecord sysNotifyRecord,HttpServletRequest request, HttpServletResponse response) {
        RetApp retApp = new RetApp();
        try {
            String type=getParam(request,"type","");
            String token=getParam(request,"token","");
            String communityId=getParam(request,"communityId","");
            sysNotifyRecord.setCommunityId(communityId);
            if(StringUtils.isEmpty(sysNotifyRecord.getCommunityId())){
                throw new DataWarnningException("小区id不能为空");
            }
            String userId= AdminTokenUtils.getUserIdByToken(token);
            sysNotifyRecord.setMgtUserId(userId);
            if(type != ""){
                sysNotifyRecord.setCategory(type);
            }
            sysNotifyRecordService.clean(sysNotifyRecord);

            retApp.setStatus(OK);
            retApp.setMessage("清空成功");
        } catch (DataWarnningException e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        }catch (OAuth2Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage("清空失败");
        }

        return retApp;
    }

}
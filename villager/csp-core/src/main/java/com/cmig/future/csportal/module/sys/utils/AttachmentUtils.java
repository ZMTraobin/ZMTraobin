package com.cmig.future.csportal.module.sys.utils;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.sys.service.SpringContextHolder;
import com.cmig.future.csportal.module.sys.sysattachment.dto.SysAttachment;
import com.cmig.future.csportal.module.sys.sysattachment.service.ISysAttachmentService;
import com.hand.hap.core.IRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangtao107@126.com on 2016/11/17.
 */
public class AttachmentUtils {
    private static Logger logger = LoggerFactory.getLogger(AttachmentUtils.class);
    private static ISysAttachmentService sysAttachmentService= SpringContextHolder.getBean("sysAttachmentServiceImpl");


    /**
     * 附件下载路径
     * @param dbSavePath
     * @return
     */
   public static String getDownloadPath(String dbSavePath){
       String path = Global.getImageServerPath() + dbSavePath;
        return path;
    }


    /**
     * 查询附件列表
     * @param objectId
     * @param objectClass
     * @param type
     * @return
     */
    public static List<SysAttachment> getAttachmentList(String objectId,String objectClass,String type){
        SysAttachment sysAttachmentQuery=new SysAttachment();
        sysAttachmentQuery.setObjectClass(objectClass);
        sysAttachmentQuery.setObjectId(objectId);
        sysAttachmentQuery.setType(type);
        List<SysAttachment> attachments = new ArrayList<SysAttachment>();
        if (objectId != null && !("").equals(objectId)) {
            attachments = sysAttachmentService.findList(sysAttachmentQuery);
        }
        return attachments;
    }



    public static String getFileName(String fileName, String userAgent) {
        String downloadName = fileName;
        try {
            userAgent = (userAgent == null ? "" : userAgent.toLowerCase());
            logger.info("userAgent:" + userAgent);
            if (userAgent.indexOf("msie") > -1) {
                downloadName = java.net.URLEncoder.encode(fileName, "UTF-8");
                downloadName = StringUtils.replace(downloadName, "+", "%20");
                logger.info(downloadName);
            } else {
                downloadName = new String(downloadName.getBytes("UTF-8"),"ISO8859-1");
            }
        } catch (Exception e) {

        }
        return downloadName;
    }

    /**
     * 附件下载
     * @param attachment
     * @param request
     * @param response
     * @return
     */
    public static String download(SysAttachment attachment, HttpServletRequest request, HttpServletResponse response,IRequest requestCtx) {
        try {
            if(StringUtils.isEmpty(attachment.getId())){
                return "附件id不能为空";
            }

            attachment=sysAttachmentService.selectByPrimaryKey(requestCtx,attachment);
            if(null==attachment){
                return "附件id参数错误";
            }
            String fileName = attachment.getFileName();
            response.setHeader("Content-Length", String.valueOf(attachment.getFileSize()));
            response.setHeader("Content-disposition", "attachment; charset=UTF-8;filename=" + getFileName(fileName, request.getHeader("User-Agent")));// 设置文件名称
            if (!StringUtils.isEmpty(attachment.getSaveType()) || attachment.getSaveType().equals("FILE")) {
                BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
                BufferedInputStream bis = null;
                try {
                    String path = AttachmentUtils.getDownloadPath(attachment.getFilePath());
                    URL url = new URL(path);
                    BufferedInputStream is = new BufferedInputStream(url.openStream());
                    //InputStream is = new FileInputStream(path);
                    bis = new BufferedInputStream(is);
                    int r = 0;
                    byte[] ba = new byte[1024];
                    while ((r = bis.read(ba)) != -1) {
                        bos.write(ba, 0, r);
                        bos.flush();
                    }
                    bos.close();
                } catch (IOException e) {
                    logger.error("Attachment file IO error.");
                } finally {
                    try {
                        if (bis != null) {
                            bis.close();
                        }
                    } catch (Exception e) {
                        logger.error("Attachment file IO error.");
                    }
                }
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return null;
    }

}

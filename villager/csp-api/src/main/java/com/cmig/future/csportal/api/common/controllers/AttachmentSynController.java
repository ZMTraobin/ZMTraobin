package com.cmig.future.csportal.api.common.controllers;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.oauth2.OAuthConstants;
import com.cmig.future.csportal.common.oauth2.exceptions.OAuth2Exception;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.sys.sysattachment.dto.SysAttachment;
import com.cmig.future.csportal.module.sys.utils.AttachmentUtils;
import com.hand.hap.core.IRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * AttachmentSynController
 *
 * @author bubu
 * @version 2017-04-13 11:23:21
 */
@Controller
@RequestMapping(value = "${commonPath}/sys/attchment/")
public class AttachmentSynController extends BaseExtendController {

    private static Logger logger = LoggerFactory.getLogger(AttachmentSynController.class);

    @ResponseBody
    @RequestMapping(value = "download", method = {RequestMethod.GET, RequestMethod.POST})
    public String downloadAttachment(SysAttachment attachment, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IRequest requestCtx = createRequestContext(request);
        final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
        try {
            // 校验appid
            OAuthUtils.checkOAuth2AppID(appId);
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

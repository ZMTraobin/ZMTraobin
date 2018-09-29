package com.cmig.future.csportal.api.common.controllers;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.SendMailUtil;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@ResponseBody
@RequestMapping(value = "${commonPath}/AppEmail")
public class EmailApiController extends BaseExtendController{
	@RequestMapping(value = "/sendCommonEmail", produces = { "application/json" }, method = RequestMethod.POST)
	public RetApp sendCommonMail(String toMailAddr, String subject, String message) {
        RetApp retApp = new RetApp();
        try{
            if(StringUtils.isEmpty(toMailAddr)){
                throw new DataWarnningException("收件地址不能为空");
            }

            Pattern p = Pattern.compile("^((([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6}\\;))*(([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})))$");
            Matcher m = p.matcher(toMailAddr);
            boolean b = m.matches();
            if(!b){
                throw new DataWarnningException("收件地址格式不正确");
            }

            if(subject==null||subject==""){
                throw new DataWarnningException("邮件标题不能为空");
            }

            if(message ==""||message==null){
                throw new DataWarnningException("邮件内容不能为空");
            }

            logger.info("邮件地址:"+toMailAddr);
            SendMailUtil.sendCommonMail(subject, message,toMailAddr.split(";"));
            retApp.setStatus(OK);
            retApp.setMessage("发送成功");
        }catch (DataWarnningException e){
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage("发送失败");
        }
		return retApp;
	}
}

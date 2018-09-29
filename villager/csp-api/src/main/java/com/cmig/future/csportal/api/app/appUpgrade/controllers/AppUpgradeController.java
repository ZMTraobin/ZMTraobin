package com.cmig.future.csportal.api.app.appUpgrade.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.operate.appUpgrade.dto.LjhAppDownload;
import com.cmig.future.csportal.module.operate.appUpgrade.service.ILjhAppDownloadService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "${userPath}")
public class AppUpgradeController extends BaseExtendController {

    @Autowired
    private ILjhAppDownloadService appDownloadService;

    /**
     * 用户协议
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/appdownload/appDownload/getLastVersionAppInfo", produces = {
            "application/json" }, method = RequestMethod.POST)
    @ResponseBody
    public RetApp getLastVersionAppInfo(LjhAppDownload appDownload,HttpServletRequest request, HttpServletResponse response) {
        RetApp retApp=new RetApp();
        
        if(StringUtils.isEmpty(appDownload.getAppName())||!(appDownload.getAppName().equals("appNameProperty")||appDownload.getAppName().equals("appNameOwner"))){
            retApp.setMessage("APP名称不能为空,并且只能输入appNameProperty和appNameOwner");
            retApp.setStatus(FAIL);
            return retApp;
        }
        if(StringUtils.isEmpty(appDownload.getAppType())||!(Constants.APP_TYPE_IOS.equals(appDownload.getAppType())||Constants.APP_TYPE_ANDROID.equals(appDownload.getAppType()))){
            retApp.setMessage("平台类型不能为空,并且只能输入IOS和Android");
            retApp.setStatus(FAIL);
            return retApp;
        }
        
        if(StringUtils.isEmpty(appDownload.getVersion())){
            retApp.setMessage("版本号不能为空");
            retApp.setStatus(FAIL);
            return retApp;
        }
        
        logger.info("平台参数:"+appDownload.getAppType());
        JSONObject jsonObject=new JSONObject();
        appDownload.setVersionFlag(Constants.YES);
        LjhAppDownload thisAppDownload= appDownloadService.findLastVersionApp(appDownload);
        
        if(null!=thisAppDownload){
            String oldVersion =appDownload.getVersion();
            String newVersion =thisAppDownload.getVersion();
            String newV = newVersion.replace(".","");
            String oldV = oldVersion.replace(".", "");
            int n = Integer.valueOf(newV).intValue();
            int o = Integer.valueOf(oldV).intValue();
            if (n> o){
                if(Constants.APP_TYPE_ANDROID.equals(appDownload.getAppType())){
                    jsonObject.put("url", Global.getImageServerPath()+thisAppDownload.getUrl());
                }else{
                    jsonObject.put("url", thisAppDownload.getUrl());
                }
                jsonObject.put("appContent", thisAppDownload.getAppContent());
                jsonObject.put("ismupdatel", thisAppDownload.getIsmupdatel());
                jsonObject.put("version", thisAppDownload.getVersion());
                jsonObject.put("versionFlag", thisAppDownload.getVersionFlag());
            } else {
                retApp.setStatus(OK);
                retApp.setMessage("查询成功!");
                retApp.setData(jsonObject);
                return retApp;
            }
        }else{
            jsonObject.put("version", "");
            jsonObject.put("url", "");
            jsonObject.put("appContent", "");
            jsonObject.put("versionFlag", "");
            jsonObject.put("ismupdatel", "");
        }
        retApp.setStatus(OK);
        retApp.setMessage("查询成功!");
        retApp.setData(jsonObject);
        return retApp;
    }
}

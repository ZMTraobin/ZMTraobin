package com.cmig.future.csportal.module.sys.openinfo;

import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import com.cmig.future.csportal.module.sys.openinfo.service.IOpenAppInfoService;
import com.cmig.future.csportal.module.sys.service.SpringContextHolder;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by zhangtao107@126.com on 2016/8/22.
 */
public class OpenAppUtils {

    private static Logger logger= LoggerFactory.getLogger(OpenAppUtils.class);
    private static IOpenAppInfoService openAppInfoService = SpringContextHolder.getBean("openAppInfoServiceImpl");

    public static OpenAppInfo getOpenAppInfo(String appId){
        Map<String, OpenAppInfo> openAppInfoMap = getOpenAppInfoMap();
        if (openAppInfoMap!=null&&openAppInfoMap.containsKey(appId)){
            return openAppInfoMap.get(appId);
        }else{
            return null;
        }
    }

    public static Map<String, OpenAppInfo> getOpenAppInfoMap(){
        Map<String, OpenAppInfo> openAppInfoMap = (Map<String, OpenAppInfo>) JedisUtils.getObject(Constants.CACHE_OPENAPP_MAP);
        if (openAppInfoMap==null){
            openAppInfoMap = Maps.newHashMap();
            OpenAppInfo openAppInfo = new OpenAppInfo();
            openAppInfo.setAppStatus("1");
            for (OpenAppInfo thisOpenAppInfo : openAppInfoService.findList(openAppInfo)){
                openAppInfoMap.put(thisOpenAppInfo.getAppKey(),thisOpenAppInfo);
            }
            JedisUtils.setObject(Constants.CACHE_OPENAPP_MAP, openAppInfoMap);
        }
        return openAppInfoMap;
    }

    public static void cleanCacheOpenappMap(){
        JedisUtils.del(Constants.CACHE_OPENAPP_MAP);
    }
}

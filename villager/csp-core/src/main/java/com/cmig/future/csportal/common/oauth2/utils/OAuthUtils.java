package com.cmig.future.csportal.common.oauth2.utils;

import com.cmig.future.csportal.common.oauth2.OAuthConstants;
import com.cmig.future.csportal.common.oauth2.exceptions.OAuth2Exception;
import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUser;
import com.cmig.future.csportal.module.properties.mgtuser.service.IMgtUserService;
import com.cmig.future.csportal.module.sys.openinfo.OpenAppUtils;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import com.cmig.future.csportal.module.sys.service.SpringContextHolder;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public final class OAuthUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(OAuthUtils.class);
    private static IAppUserService appUserService= SpringContextHolder.getBean("appUserServiceImpl");
    private static IMgtUserService mgtUserService= SpringContextHolder.getBean("mgtUserServiceImpl");
	private OAuthUtils() {}
	
    public static ModelAndView writeText(final HttpServletResponse response, final String text, final int status) {
        PrintWriter printWriter;
        try {
            printWriter = response.getWriter();
            response.setStatus(status);
            printWriter.print(text);
            printWriter.flush();
            printWriter.close();
        } catch (final IOException e) {
        	logger.error("Failed to write to response", e);
        }
        return null;
    }
    
    public static ModelAndView writeTextError(final HttpServletResponse response, final String error, final int status) {
         return OAuthUtils.writeText(response, "error=" + error, status);
    }
    
    public static ModelAndView redirectTo(final String url) {
        return new ModelAndView(new RedirectView(url));
    }

    /**
     * 验证ClientID 是否正确
     * @param appId
     * @return
     */
    public static boolean validateOAuth2AppID(String appId) {
        if(StringUtils.isEmpty(appId)){
            return false;
        }
        // 校验appid
        Map<String, OpenAppInfo> openAppInfoMap= OpenAppUtils.getOpenAppInfoMap();
        if (null==openAppInfoMap||!openAppInfoMap.containsKey(appId)) {
            return false;
        }

        return true;
    }

    /**
     * 验证ClientID 是否正确
     * @param appId
     * @return
     */
    public static void checkOAuth2AppID(String appId) {
        if (StringUtils.isEmpty(appId)) {
            logger.error("Missing {}", OAuthConstants.OAUTH_APP_ID);
	        throw new OAuth2Exception(RestApiExceptionEnums.ARGS_NULL,"appid");
        }
        // 校验appid
        Map<String, OpenAppInfo> openAppInfoMap= OpenAppUtils.getOpenAppInfoMap();
        if (null==openAppInfoMap||!openAppInfoMap.containsKey(appId)) {
	        throw new OAuth2Exception(RestApiExceptionEnums.ARGS_ERROR,"appid");
        }
    }

    /**
     * 验证ClientID 是否正确
     * @param appId
     * @return
     */
    public static OpenAppInfo getOpenAppInfo(String appId) {
        if (StringUtils.isEmpty(appId)) {
            logger.error("Missing {}", OAuthConstants.OAUTH_APP_ID);
	        throw new OAuth2Exception(RestApiExceptionEnums.ARGS_NULL,"appid");
        }
        // 校验appid
        Map<String, OpenAppInfo> openAppInfoMap=OpenAppUtils.getOpenAppInfoMap();
        if (null==openAppInfoMap||!openAppInfoMap.containsKey(appId)) {
	        throw new OAuth2Exception(RestApiExceptionEnums.ARGS_ERROR,"appid");
        }else{
            return openAppInfoMap.get(appId);
        }
    }

    public static void checkAppOauth(String appId, String appSecret) {
        if (StringUtils.isEmpty(appId)) {
            logger.error("Missing {}", OAuthConstants.OAUTH_APP_ID);
	        throw new OAuth2Exception(RestApiExceptionEnums.ARGS_NULL,"appid");
        }
        if (StringUtils.isEmpty(appSecret)) {
            logger.error("Missing {}", OAuthConstants.OAUTH_APP_SECRET);
	        throw new OAuth2Exception(RestApiExceptionEnums.ARGS_NULL,"secret");
        }

        // 校验appid
        Map<String, OpenAppInfo> openAppInfoMap=OpenAppUtils.getOpenAppInfoMap();
        if (null==openAppInfoMap||!openAppInfoMap.containsKey(appId)) {
	        throw new OAuth2Exception(RestApiExceptionEnums.ARGS_ERROR,"appid");
        }

        OpenAppInfo appInfo = openAppInfoMap.get(appId);
        if (!appSecret.equals(appInfo.getAppSecret())) {
            logger.error("appSecret error {}", appSecret);
	        throw new OAuth2Exception(RestApiExceptionEnums.ARGS_ERROR,"secret");
        }
    }

    /**
     * 根据accessToken获取用户ID
     * @param accessToken
     * @return
     */
    public static String  getAppUserIdByAccessToken(String accessToken) {
        if (StringUtils.isEmpty(accessToken)) {
            logger.error("Missing {}", OAuthConstants.OAUTH_ACCESS_TOKEN);
            throw new OAuth2Exception(RestApiExceptionEnums.ARGS_NULL,"accessToken");
        }
        String authKey = OAuthConstants.AUTH + accessToken;
        String uid = JedisUtils.get(authKey);
        if(StringUtils.isEmpty(uid)){
            logger.error("用户token过期");
            throw new OAuth2Exception(RestApiExceptionEnums.ARGS_ERROR,"用户token过期!");
        }
        return uid;
    }

    /**
     * 根据accessToken获取用户对象
     * @param accessToken
     * @return
     */
    public static AppUser getAppUserByAccessToken(String accessToken) {
        String appUserId=getAppUserIdByAccessToken(accessToken);
        AppUser appUser = appUserService.get(appUserId);
        if(null==appUser){
            logger.error("用户token过期");
	        throw new OAuth2Exception(RestApiExceptionEnums.ARGS_ERROR,"用户token过期!");
        }
        return appUser;
    }


    /**
     * 根据accessToken获取员工ID
     * @param accessToken
     * @return
     */
    public static String  getSysUserIdByAccessToken(String accessToken) {
        if (StringUtils.isEmpty(accessToken)) {
            logger.error("Missing {}", OAuthConstants.OAUTH_ACCESS_TOKEN);
	        throw new OAuth2Exception(RestApiExceptionEnums.ARGS_NULL,"accessToken");
        }
        String authKey = OAuthConstants.AUTH + accessToken;
        String uid = JedisUtils.get(authKey);
        if(StringUtils.isEmpty(uid)){
            logger.error("用户token过期");
	        throw new OAuth2Exception(RestApiExceptionEnums.ARGS_ERROR,"用户token过期!");
        }
        return uid;
    }

    /**
     * 根据accessToken获取员工对象
     * @param accessToken
     * @return
     */
    public static MgtUser getSysUserByAccessToken(String accessToken) {
        String userId=getSysUserIdByAccessToken(accessToken);
        MgtUser user = mgtUserService.getUser(userId);
        if(null==user){
            logger.error("用户token过期");
	        throw new OAuth2Exception(RestApiExceptionEnums.ARGS_ERROR,"用户token过期!");
        }
        return user;
    }

}

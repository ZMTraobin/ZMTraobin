package com.cmig.future.csportal.api.open.oauth2.appuser.controllers;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.oauth2.OAuthConstants;
import com.cmig.future.csportal.common.oauth2.entity.ResponseEntity;
import com.cmig.future.csportal.common.oauth2.exceptions.OAuth2Exception;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping(value = "${commonPath}/oauth2")
public class AppUserOauthController extends BaseExtendController{
	
	@Autowired
	private IAppUserService appUserService;
	
    @RequestMapping("/userinfo")
    public String getUserInfo(HttpServletRequest request, HttpServletResponse response){
    	ResponseEntity result = new ResponseEntity();
    	final String access_token = getParam(request, OAuthConstants.OAUTH_ACCESS_TOKEN, "");
    	logger.debug("{} : {}", OAuthConstants.OAUTH_ACCESS_TOKEN, access_token);
        try {
            AppUser appUser = OAuthUtils.getAppUserByAccessToken(access_token);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("openid", StringUtils.isEmpty(appUser.getSourceSystemId()) ? "" : appUser.getSourceSystemId());
            jsonObject.put("nickName", StringUtils.isEmpty(appUser.getNickName())?"":appUser.getNickName());

            String userIcon="";
            if(!StringUtils.isEmpty(appUser.getUserIcon())){
                userIcon= Global.getFullImagePath(appUser.getUserIcon());
            }
            jsonObject.put("userIcon", userIcon);
            jsonObject.put("mobile", appUser.getMobile());
            jsonObject.put("retTime", DateUtils.formatDateTime(appUser.getRetTime()));
            jsonObject.put("sourceSystemId","");

            jsonObject.put("isOwnerFlag", Constants.NO);
            jsonObject.put("assetsName", "");
            jsonObject.put("sourceSystemHouseIds","");
            result.setStatus(OK);
            result.setMessage("查询成功!");
            result.setData(jsonObject);
            return renderString(response, result);
        }catch (OAuth2Exception e){
            result.setStatus(FAIL);
            result.setMessage(e.getMessage());
            return renderString(response, result);
        }catch (Exception e){
            e.printStackTrace();
            result.setStatus(FAIL);
            result.setMessage("获取用户信息失败，请联系系统管理员！");
            return renderString(response, result);
        }

    }

}

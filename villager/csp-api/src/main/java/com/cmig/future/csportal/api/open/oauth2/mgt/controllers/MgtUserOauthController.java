package com.cmig.future.csportal.api.open.oauth2.mgt.controllers;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.oauth2.OAuthConstants;
import com.cmig.future.csportal.common.oauth2.entity.ResponseEntity;
import com.cmig.future.csportal.common.oauth2.exceptions.OAuth2Exception;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUser;
import com.cmig.future.csportal.module.properties.mgtuser.service.IMgtUserService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping(value = "${commonPath}/${managementPath}/oauth2")
public class MgtUserOauthController extends BaseExtendController {
	
	@Autowired
	private IMgtUserService mgtUserService;
	
    @RequestMapping("/userinfo")
    public String getUserInfo(HttpServletRequest request, HttpServletResponse response){
    	ResponseEntity result = new ResponseEntity();
    	final String access_token = getParam(request, OAuthConstants.OAUTH_ACCESS_TOKEN, "");
    	logger.debug("{} : {}", OAuthConstants.OAUTH_ACCESS_TOKEN, access_token);
        try{
            MgtUser user= OAuthUtils.getSysUserByAccessToken(access_token);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("openid", user.getId());
            jsonObject.put("mobile", user.getMobile());
            String userIcon="";
            if(!StringUtils.isEmpty(user.getPhoto())){
                userIcon= Global.getFullImagePath(user.getPhoto());
            }
            jsonObject.put("userIcon", userIcon);
            jsonObject.put("userName", user.getName());
            jsonObject.put("sex", user.getSex());//0：男；1：女
            jsonObject.put("loginFlag", user.getLoginFlag());//1：启用；0：停用

            String officeName="";
            if(!StringUtils.isEmpty(user.getOfficeName())){
                officeName=user.getOfficeName();
            }
            //所属组织
            jsonObject.put("officeName", officeName);
            String companyName="";
            if(!StringUtils.isEmpty(user.getCompanyName())){
                companyName=user.getCompanyName();
            }
            //所属公司
            jsonObject.put("companyName", companyName);
            
            String communityName="";
            if(!StringUtils.isEmpty(user.getCommunityName())){
                communityName=user.getCommunityName();
            }
            //所属小区
            jsonObject.put("communityName", communityName);
            //员工编号
            jsonObject.put("employeeNo", user.getNo());

            String birthday="";
            if(!StringUtils.isEmpty(user.getBirthTime())){
                birthday= DateUtils.formatDateTime(user.getBirthTime());
            }
            jsonObject.put("birthday", birthday);
            jsonObject.put("roleNames", "");
            jsonObject.put("idcard", user.getIdcard());
            jsonObject.put("sourceSystemId", user.getSourceSystemId());

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
            result.setMessage("获取员工信息失败，请联系系统管理员！");
            return renderString(response, result);
        }
		

    }

}

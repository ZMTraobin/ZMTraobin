package com.cmig.future.csportal.api.open.syn.community.controllers;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.oauth2.OAuthConstants;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.properties.community.dto.BaseCommunity;
import com.cmig.future.csportal.module.properties.community.service.IBaseCommunityService;
import com.cmig.future.csportal.module.sys.appconfig.service.IAppConfigCommunityService;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * BaseCommunitySynController
 * 项目信息同步
 *
 * Created by bubu on 2017/3/21.
 */
@Controller
@ResponseBody
@RequestMapping(value = "${commonPath}/")
public class BaseCommunitySynController extends BaseExtendController {

    private static Logger logger= LoggerFactory.getLogger(BaseCommunitySynController.class);
    @Autowired
    private IBaseCommunityService baseCommunityService;
    @Autowired
    private IAppConfigCommunityService appConfigCommunityService;

    /**
     * 项目信息新增
     *
     * @author bubu
     * @param community
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/syn/community/add", produces = { "application/json" }, method = RequestMethod.POST)
    public RetApp query(@ModelAttribute BaseCommunity community, HttpServletRequest request, HttpServletResponse response) {

        RetApp retApp = new RetApp();
        final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
        String companyId = getParam(request, "companyId", "");
        String companyName = getParam(request, "companyName", "");
        String communityName = getParam(request, "communityName", "");
        String sourceSystemId = getParam(request, "sourceSystemId", "");
        String serverUrl = getParam(request, "serverUrl", "");
        String phone = getParam(request, "phone", "");
        try {
            // 校验appid
            OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appId);

            if (StringUtils.isEmpty(communityName)) {
                throw new OAuth2Exception("项目名称不能为空");
            }
            if (StringUtils.isEmpty(companyId)) {
                throw new OAuth2Exception("物业公司ID不能为空");
            }
            if (StringUtils.isEmpty(companyName)) {
                throw new DataWarnningException("物业公司名称不能为空");
            }
            if (StringUtils.isEmpty(serverUrl)) {
                throw new DataWarnningException("服务器域名不能为空");
            }
            if (StringUtils.isEmpty(sourceSystemId)) {
                throw new OAuth2Exception("源系统ID不能为空");
            }
            BaseCommunity communityQuery = new BaseCommunity();
            communityQuery.setSourceSystem(openAppInfo.getAppName());
            communityQuery.setSourceSystemId(sourceSystemId);
            communityQuery = baseCommunityService.getBySourceSystemId(communityQuery);
            if (null != communityQuery && !StringUtils.isEmpty(communityQuery.getId())) {
                throw new DataWarnningException("该项目已存在，不能重复新增");
            }

            // 校验小区是否存在(同一物业公司id下不存在相同名称的小区)
            communityQuery = new BaseCommunity();
            communityQuery.setCompanyId(companyId);
            communityQuery.setCommunityName(communityName);
            communityQuery = baseCommunityService.findByCommunityName(communityQuery);
            if (null != communityQuery &&!StringUtils.isEmpty(communityQuery.getId())) {
                if(StringUtils.isEmpty(communityQuery.getSourceSystemId())){
                    communityQuery.setSourceSystemId(sourceSystemId);
                    baseCommunityService.save(communityQuery);
                    retApp.setStatus(OK);
                    retApp.setMessage("新增项目成功");
                    return retApp;
                }else{
                    throw new DataWarnningException("该项目已存在，不能重复新增");
                }
            }
            //小区表新建
            //community.setOfficeId(companyId);
            //community.setOfficeName(communityName);
            community.setCommunityName(communityName);
            community.setCompanyId(companyId);
            community.setCompanyName(companyName);
            community.setSourceSystem(openAppInfo.getAppName());
            community.setSourceSystemId(sourceSystemId);
            community.setServerUrl(serverUrl);
            community.setIsRemoteAuthen(Constants.YES);//默认开启远程业主认证
            community.setPhone(phone);
            community.setDelFlag(BaseExtDTO.DEL_FLAG_NORMAL);

            baseCommunityService.save(community);

            retApp.setStatus(OK);
            retApp.setMessage("新增项目成功");
        } catch (DataWarnningException e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        } catch (OAuth2Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage("新增项目失败");
        }
        return retApp;

    }


    /**
     * 项目信息修改
     *
     * @author bubu
     * @param dto
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/syn/community/update", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp update(@ModelAttribute BaseCommunity dto, HttpServletRequest request, HttpServletResponse response) {
        RetApp retApp = new RetApp();
        final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
        String companyId = getParam(request, "companyId", "");
        String communityName = getParam(request, "communityName", "");
        String sourceSystemId = getParam(request, "sourceSystemId", "");
        String companyName = getParam(request, "companyName", "");
        try {
            // 校验appid
            OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appId);
            if (StringUtils.isEmpty(companyId)) {
                throw new OAuth2Exception("物业公司ID不能为空");
            }
            if (StringUtils.isEmpty(communityName)) {
                throw new OAuth2Exception("项目名称不能为空");
            }
            if (StringUtils.isEmpty(companyName)) {
                throw new OAuth2Exception("物业公司名称不能为空");
            }
            if (StringUtils.isEmpty(sourceSystemId)) {
                throw new OAuth2Exception("源系统ID不能为空");
            }
            BaseCommunity communityQuery = new BaseCommunity();
            communityQuery.setSourceSystem(openAppInfo.getAppName());
            communityQuery.setSourceSystemId(sourceSystemId);
            communityQuery = baseCommunityService.getBySourceSystemId(communityQuery);
            if (null != communityQuery && !StringUtils.isEmpty(communityQuery.getId())) {
                //修改物业公司id&名称
                communityQuery.setCompanyId(companyId);
                communityQuery.setCompanyName(companyName);
                //修改小区名称
                communityQuery.setCommunityName(communityName);

                if(!StringUtils.isEmpty(dto.getServerUrl())) {
	                communityQuery.setServerUrl(dto.getServerUrl());
                }
                //修改组织机构id&名称
                //communityQuery.setOfficeId(communityQuery.getId());
                //communityQuery.setOfficeName(communityName);
                baseCommunityService.save(communityQuery);
            } else {
                throw new DataWarnningException("项目修改失败,该项目不存在");
            }

        retApp.setStatus(OK);
            retApp.setMessage("项目修改成功");
        } catch (DataWarnningException e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        } catch (OAuth2Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage("项目修改失败");
        }
        return retApp;
    }

}

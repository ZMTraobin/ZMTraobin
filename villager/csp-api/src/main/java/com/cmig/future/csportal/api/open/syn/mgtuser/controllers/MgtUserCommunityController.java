package com.cmig.future.csportal.api.open.syn.mgtuser.controllers;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.oauth2.OAuthConstants;
import com.cmig.future.csportal.common.oauth2.exceptions.OAuth2Exception;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.properties.community.dto.BaseCommunity;
import com.cmig.future.csportal.module.properties.community.service.IBaseCommunityService;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUserCommunity;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUserSyn;
import com.cmig.future.csportal.module.properties.mgtuser.service.IMgtUserCommunityService;
import com.cmig.future.csportal.module.properties.mgtuser.service.IMgtUserSynService;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
    @Controller
    @RequestMapping(value = "${commonPath}/syn/mgtUserCommunity")
    public class MgtUserCommunityController extends BaseExtendController{
    @Autowired
    private IBaseCommunityService BaseCommunityService;
    @Autowired
    private IMgtUserCommunityService mgtUserCommunityService;
    @Autowired
    private IMgtUserSynService mgtUserSynService;
    @RequestMapping(value = "/add", produces = { "application/json" }, method = RequestMethod.POST)
	public RetApp add(@ModelAttribute MgtUserCommunity mgtUserCommunity, HttpServletRequest request, HttpServletResponse response) {
		RetApp retApp = new RetApp();
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		String sourceSystemCommunityId = getParam(request, "sourceSystemCommunityId", "");
		String sourceMgtUserId = getParam(request, "sourceMgtUserId", "");
        // 校验appid
        OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appId);
        if(StringUtils.isEmpty(sourceMgtUserId)){
            throw new DataWarnningException("源系统员工ID不能为空");
        }
        if(StringUtils.isEmpty(sourceSystemCommunityId)){
            throw new DataWarnningException("源系统小区ID不能为空");
        }

		MgtUserSyn mgtUserSyn = new MgtUserSyn();
		mgtUserSyn.setSourceSystem(openAppInfo.getAppName());
		mgtUserSyn.setSourceSystemId(sourceMgtUserId);
		List<MgtUserSyn> list = mgtUserSynService.checkSourceAndSystemId(mgtUserSyn);
		if (StringUtils.isEmpty(list)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"该用户不存在，请先同步用户信息");
		}
		mgtUserSyn = list.get(0);
        BaseCommunity community=new BaseCommunity();
        community.setSourceSystem(openAppInfo.getAppName());//zhongjia
        community.setSourceSystemId(sourceSystemCommunityId);
        community=BaseCommunityService.getBySourceSystemId(community);
        if(null==community){
            throw new DataWarnningException("该小区不存在，请先同步小区数据");
        }
        MgtUserCommunity mgtUserCommunityQuery=new MgtUserCommunity();
        mgtUserCommunityQuery.setSourceSystem(openAppInfo.getAppName());
        mgtUserCommunityQuery.setSourceSystemCommunityId(sourceSystemCommunityId);
        mgtUserCommunityQuery.setMgtUserId(mgtUserSyn.getUserId());
        List mucList= mgtUserCommunityService.findList(mgtUserCommunityQuery);
        if(!StringUtils.isEmpty(mucList)) {
            retApp.setStatus(FAIL);
            retApp.setMessage("新增失败,已存在，不需要重复关联");
        }else{
            mgtUserCommunity.setId(IdGen.uuid());
            mgtUserCommunity.setMgtUserId(mgtUserSyn.getUserId());
            mgtUserCommunity.setSourceMgtUserId(sourceMgtUserId);
            mgtUserCommunity.setCommunityId(community.getId());
            mgtUserCommunity.setSourceSystem(openAppInfo.getAppName());
            mgtUserCommunityService.save(mgtUserCommunity);
            retApp.setStatus(OK);
            retApp.setMessage("新增成功");
        }
		return retApp;
	}
    
    
    
    /**
     * 删除小区和对象之间的关系
     * @param mgtUserCommunity
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/delete", produces = { "application/json" }, method = RequestMethod.POST)
    public RetApp delete(@ModelAttribute MgtUserCommunity mgtUserCommunity, HttpServletRequest request, HttpServletResponse response) {
        RetApp retApp = new RetApp();
        final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
        String sourceSystemCommunityId = getParam(request, "sourceSystemCommunityId", "");
	    String sourceMgtUserId = getParam(request, "sourceMgtUserId", "");
        try {
            // 校验appid
            OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appId);

	        if(StringUtils.isEmpty(sourceMgtUserId)){
		        throw new DataWarnningException("源系统员工ID不能为空");
	        }
            if(StringUtils.isEmpty(sourceSystemCommunityId)){
                throw new DataWarnningException("源系统小区ID不能为空");
            }
            MgtUserCommunity mgtUserCommunityQuery=new MgtUserCommunity();
            mgtUserCommunityQuery.setSourceSystem(openAppInfo.getAppName());
            mgtUserCommunityQuery.setSourceSystemCommunityId(sourceSystemCommunityId);
            mgtUserCommunityQuery.setSourceMgtUserId(sourceMgtUserId);
            mgtUserCommunityService.deleteBySourceSystemId(mgtUserCommunityQuery);
            retApp.setStatus(OK);
            retApp.setMessage("删除成功");

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
            retApp.setMessage("删除失败");
        }
        return retApp;
    }

}
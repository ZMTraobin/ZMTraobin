package com.cmig.future.csportal.api.app.community.controllers;

import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.properties.community.dto.BaseCommunity;
import com.cmig.future.csportal.module.properties.community.service.IBaseCommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zpf on 2017/4/25.
 */
@Controller
@RequestMapping(value = "${commonPath}/community")
public class AppBaseCommunityController extends BaseExtendController {
    @Autowired
    private IBaseCommunityService baseCommunityService;

    @RequestMapping(value = "/getCommunityById", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp getCommunityById(BaseCommunity community,HttpServletRequest request, HttpServletResponse response)throws Exception{
        //小区ID
        String communityId = getParam(request,"communityId", "");
        if (StringUtils.isEmpty(communityId)) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"communityId");
        }
        community = baseCommunityService.get(communityId);
        if(null==community){
	        throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"communityId");
        }
        Map<String,Object> dataMap = new HashMap<String,Object>();
        if(Constants.NO.equals(community.getIsRemoteAuthen())) {
	        dataMap.put("isStanderMgt", "Y");//是否非物管云小区
        }else{
	        dataMap.put("isStanderMgt", "N");//是否非物管云小区
        }
        dataMap.put("phone",StringUtils.isEmpty(community.getPhone()) ? "" : community.getPhone());
        dataMap.put("servicePhone","0371-4566777");
        dataMap.put("communityId", communityId);
        dataMap.put("communityName", community.getCommunityName());
        //dataMap.put("sourceSystem", StringUtils.isEmpty(community.getSourceSystem()) ? "" : community.getSourceSystem());
        //dataMap.put("sourceSystemCommunityId", StringUtils.isEmpty(community.getSourceSystemId()) ? "" : community.getSourceSystemId());
        //dataMap.put("serverUrl", StringUtils.isEmpty(community.getServerUrl()) ? "" : community.getServerUrl());

        //是否支持联系人管理功能 Y 支持 N 不支持
        if(Constants.YES.equals(community.getResidentManager())) {
	        dataMap.put("residentManager", "Y");
        }else{
	        dataMap.put("residentManager", "N");
        }
        return RetAppUtil.success(dataMap,"查询成功");
    }

}

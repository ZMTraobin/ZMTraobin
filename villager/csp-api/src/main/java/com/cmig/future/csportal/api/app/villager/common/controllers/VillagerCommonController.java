package com.cmig.future.csportal.api.app.villager.common.controllers;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.exceptions.ServiceExceptionHelper;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUser;
import com.cmig.future.csportal.module.properties.mgtuser.service.IMgtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description:
 * @author: zhangtao
 * @create: 2018-09-25 14:35
 **/
@RestController
@RequestMapping(value = "${userPath}")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VillagerCommonController extends BaseExtendController {

    @Autowired
    private IMgtUserService mgtUserService;

    /**
     * 查询站长
     */
    @RequestMapping(value = "/villager/find/mgtuser", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp findMgtUser(HttpServletRequest request) {
        String communityId=getParam(request,"communityId","");
        if (StringUtils.isEmpty(communityId)) {
            throw ServiceExceptionHelper.argsNull();
        }
        List<MgtUser> list= mgtUserService.findMgtUserByCommunity(communityId);
        if(null!=list&&list.size()>0){
            MgtUser mgtUser=list.get(0);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("name",mgtUser.getName());
            jsonObject.put("mobile",mgtUser.getMobile());
            return RetAppUtil.success(jsonObject,"查询成功");
        }
        return RetAppUtil.error(new ServiceException(RestApiExceptionEnums.NO_VILLAGE_HEAD));
    }

}

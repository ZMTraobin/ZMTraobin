package com.cmig.future.csportal.module.properties.mgtuser.controllers;

import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUserCommunity;
import com.cmig.future.csportal.module.properties.mgtuser.service.IMgtUserCommunityService;
import com.cmig.future.csportal.module.properties.mgtuser.service.IMgtUserService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MgtUserCommunityController extends BaseController {

    @Autowired
    private IMgtUserService mgtUserService;
    @Autowired
    private IMgtUserCommunityService mgtUserCommunityService;


    @RequestMapping(value = "/csp/ljh/mgt/user/community/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<MgtUserCommunity> dto){
        IRequest requestCtx = createRequestContext(request);
        for (MgtUserCommunity t : dto) {
            switch (t.get__status()) {
                case DTOStatus.ADD:
                    t.setId(IdGen.uuid());
                    break;
                default:
                    break;
            }
        }
        return new ResponseData(mgtUserCommunityService.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/ljh/mgt/user/community/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<MgtUserCommunity> dto){
        mgtUserCommunityService.batchDelete(dto);
        return new ResponseData();
    }


    @RequestMapping(value = "/csp/ljh/mgt/user/community/delete")
    @ResponseBody
    public ResponseData remove(HttpServletRequest request,@RequestBody List<MgtUserCommunity> dto){
        mgtUserCommunityService.batchDelete(dto);
        return new ResponseData();
    }
   
    @RequestMapping(value = "/csp/ljh/mgt/user/community/queryCommunitiesNoAuth")
    @ResponseBody
    public ResponseData queryCommunitiesNoAuth(MgtUserCommunity dto,@RequestParam(defaultValue = DEFAULT_PAGE) int page,@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        return new ResponseData(mgtUserCommunityService.queryCommunitiesNoAuth(dto, page, pageSize));
    }
}
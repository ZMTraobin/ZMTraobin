package com.cmig.future.csportal.module.properties.mgtuser.controllers;

import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.entity.ResponseExtData;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUser;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUserCommunity;
import com.cmig.future.csportal.module.properties.mgtuser.help.LoginFlag;
import com.cmig.future.csportal.module.properties.mgtuser.service.IMgtUserCommunityService;
import com.cmig.future.csportal.module.properties.mgtuser.service.IMgtUserService;
import com.cmig.future.csportal.module.sys.service.SystemService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
    public class MgtUserController extends BaseController {

    @Autowired
    private IMgtUserService mgtUserService;
    @Autowired
    private IMgtUserCommunityService mgtUserCommunityService;

    @RequestMapping(value = "/ljh/mgt/user/query")
    @ResponseBody
    public ResponseData query(MgtUser dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(mgtUserService.selectMgtUser(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/ljh/mgt/user/add")
    @ResponseBody
    public ResponseData add(HttpServletRequest request,@RequestBody MgtUser dto){
        mgtUserService.add(dto);
        return new ResponseExtData(dto);
    }

/*    @RequestMapping(value = "/ljh/mgt/user/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<MgtUser> dto){
    	mgtUserService.batchDelete(dto);
        return new ResponseData();
    }*/

        /**
         * 跳转到详情界面
          * @param request
         * @return
         */
        @RequestMapping(value = "/ljh/mgt/user/detail")
        @ResponseBody
        public ResponseData detail(MgtUser dto, HttpServletRequest request) {
            IRequest requestContext = createRequestContext(request);

            dto=mgtUserService.selectByPrimaryKey(requestContext,dto);
            return new ResponseExtData(dto);
        }

        /**
         * 更新物业用户
         * @param request
         * @return
         */
        @RequestMapping(value = "/ljh/mgt/user/update")
        @ResponseBody
      //  HttpServletRequest request, @RequestBody OpenAppInfo dto,BindingResult result
        public ResponseData update(HttpServletRequest request,@RequestBody MgtUser dto,BindingResult result) {
            IRequest requestContext = createRequestContext(request);
	        String password=dto.getPassword();
            if(!StringUtils.isEmpty(password)){
                dto.setPassword(SystemService.entryptMgtPassword(password));
            }
            mgtUserService.updateByPrimaryKeySelective(requestContext,dto);

	        if(!StringUtils.isEmpty(password)){
		        mgtUserService.synMgtUserPassword(dto,dto.getPassword());
	        }
            return new ResponseData();
        }


        @RequestMapping(value = "/ljh/mgt/user/bathRemove")
        @ResponseBody
        public ResponseData bathDelete(HttpServletRequest request,MgtUser dto) {
            IRequest requestContext = createRequestContext(request);
            String[]mgtUserIds = request.getParameterValues("arr");
            Map<String,Object> paramMap = new HashMap<String,Object>();
            List<String>listIds = new ArrayList<String>();
            for(int i = 0;i<mgtUserIds.length;i++){
                 listIds.add(mgtUserIds[i]);
            }
            paramMap.put("DEL_FLAG_DELETE",dto.DEL_FLAG_DELETE);
            paramMap.put("list",listIds);
            mgtUserService.updateBathMgtUser(paramMap);
            return new ResponseExtData(dto);
        }

    /**
     * 修改用的的登陆状态
     * @param request
     * @return
     */
    @RequestMapping(value = "/ljh/mgt/user/stopLoginStatus")
    @ResponseBody
    public ResponseData stopLoginStatus(HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        String id = request.getParameter("id");
        MgtUser mgtUser = new MgtUser();
        mgtUser.setLoginFlag(LoginFlag.disable.getCode());
        mgtUser.setId(id);
        mgtUserService.updateByPrimaryKeySelective(requestContext,mgtUser);
        return new ResponseData();
    }

    /**
     * 修改用的的登陆状态
     * @param request
     * @return
     */
    @RequestMapping(value = "/ljh/mgt/user/startLoginStatus")
    @ResponseBody
    public ResponseData startLoginStatus(HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        String id = request.getParameter("id");
        MgtUser mgtUser = new MgtUser();
        mgtUser.setLoginFlag(LoginFlag.enable.getCode());
        mgtUser.setId(id);
        mgtUserService.updateByPrimaryKeySelective(requestContext,mgtUser);
        return new ResponseData();
    }

    /**
     * 根据物管员工id查询关联的项目
     * @param request
     * @return
     */
    @RequestMapping(value = "/ljh/mgt/user/findCommunityListByMgtUserId")
    @ResponseBody
    public ResponseData findCommunityListByMgtUserId(MgtUserCommunity dto,
         @RequestParam(defaultValue = DEFAULT_PAGE)int page,
         @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        //删除多拼接的字符串
        String[] mstUserIds = null;
        if(dto != null && !"".equals(dto.getMgtUserId()))
        mstUserIds =  dto.getMgtUserId().split(",");
        if(mstUserIds != null)
        dto.setMgtUserId(mstUserIds[0]);
        List<MgtUserCommunity> list = mgtUserCommunityService.findCommunityListByMgtUserId(requestContext,dto,page,pageSize);
        return new ResponseData(list);
    }
}
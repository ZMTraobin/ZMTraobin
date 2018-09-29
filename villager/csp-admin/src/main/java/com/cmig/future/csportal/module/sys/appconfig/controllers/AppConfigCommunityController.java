package com.cmig.future.csportal.module.sys.appconfig.controllers;

import com.cmig.future.csportal.module.base.entity.ResponseExtData;
import com.cmig.future.csportal.module.sys.appconfig.dto.AppConfigCommunity;
import com.cmig.future.csportal.module.sys.appconfig.dto.AppConfigFunction;
import com.cmig.future.csportal.module.sys.appconfig.service.IAppConfigCommunityService;
import com.cmig.future.csportal.module.sys.appconfig.service.IAppConfigFunctionService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
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
    public class AppConfigCommunityController extends BaseController{
    @Autowired
    private IAppConfigCommunityService appConfigCommunityService;
    @Autowired
    private IAppConfigFunctionService appConfigFunctionService;


        /**
         * 分页查询业主端的数据
          * @param dto
         * @param page
         * @param pageSize
         * @param request
         * @return
         */
    @RequestMapping(value = "/csp/ljh/app/config/community/queryOwner")
    @ResponseBody
    public ResponseData query(AppConfigCommunity dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(appConfigCommunityService.selectOwner(requestContext,dto,page,pageSize));
    }
        /**
         * 分页查询物业端的数据
         * @param dto
         * @param page
         * @param pageSize
         * @param request
         * @return
         */
        @RequestMapping(value = "/csp/ljh/app/config/community/queryPropertyMC")
        @ResponseBody
        public ResponseData queryPropertyMC(AppConfigCommunity dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
            IRequest requestContext = createRequestContext(request);
            return new ResponseData(appConfigCommunityService.selectPropertyMC(requestContext,dto,page,pageSize));
        }
        @RequestMapping(value = "/csp/ljh/app/config/community/submit")
        @ResponseBody
        public ResponseData update(HttpServletRequest request,@RequestBody List<AppConfigCommunity> dto){
            IRequest requestCtx = createRequestContext(request);
            return new ResponseData(appConfigCommunityService.batchUpdate(requestCtx, dto));
        }

    @RequestMapping(value = "/csp/ljh/app/config/community/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<AppConfigCommunity> dto){
        appConfigCommunityService.batchDelete(dto);
        return new ResponseData();
    }

        /**
         * 跳转到业主端详情界面
         * @param request
         * @return
         */
        @RequestMapping(value = "/csp/ljh/app/config/community/detail")
        @ResponseBody
        public ResponseData detail(AppConfigCommunity dto, HttpServletRequest request) {
            IRequest requestContext = createRequestContext(request);
            String id = request.getParameter("id");

            dto.setId(id);
            dto=appConfigCommunityService.getAppConfigCommunityById(dto);
            return new ResponseExtData(dto);
        }

        /**
         * 跳转到业主端详情界面
         * @param request
         * @return
         */
        @RequestMapping(value = "/csp/ljh/app/config/community/detailProperty")
        @ResponseBody
        public ResponseData detailPropertyMC(AppConfigCommunity dto, HttpServletRequest request) {
            IRequest requestContext = createRequestContext(request);
            String id = request.getParameter("id");

            dto.setId(id);
            dto=appConfigCommunityService.getAppConfigCommunityByIdPropertyMC(dto);
            return new ResponseExtData(dto);
        }




        /**
         * 查询业主端该小区配置了哪些app功能，也就是查看的功能
         * @param dto
         * @param page
         * @param pageSize
         * @param request
         * @return
         */
        @RequestMapping(value = "/csp/ljh/app/config/community/queryAppListOwner")
        @ResponseBody
        public ResponseData queryAppConfig(AppConfigFunction dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
            IRequest requestContext = createRequestContext(request);

            String id = request.getParameter("id");
            System.out.println(id);

            List<AppConfigFunction> list = appConfigFunctionService.selectAppConfigById(id,pageSize,page);

//            AppConfigCommunity appConfigCommunity = new AppConfigCommunity();
//            appConfigCommunity.setId(id);
          //  List<AppConfigFunction> list = appConfigFunctionService.selectOwner(requestContext,dto,page,pageSize);
            //return new ResponseData(appConfigCommunityService.selectOwnerById(requestContext,appConfigCommunity,page,pageSize));
            return new ResponseData(list);
        }


        /**
         * 查询物业端该小区配置了哪些app功能，也就是查看的功能
         * @param dto
         * @param page
         * @param pageSize
         * @param request
         * @return
         */
        @RequestMapping(value = "/csp/ljh/app/config/community/queryAppListPropertyMC")
        @ResponseBody
        public ResponseData queryAppConfigPropertyMC(AppConfigFunction dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                           @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
            IRequest requestContext = createRequestContext(request);

            String id = request.getParameter("id");
            System.out.println(id);

            List<AppConfigFunction> list = appConfigFunctionService.selectAppConfigByIdPropertyMC(id,page,pageSize);

            return new ResponseData(list);
        }
    }
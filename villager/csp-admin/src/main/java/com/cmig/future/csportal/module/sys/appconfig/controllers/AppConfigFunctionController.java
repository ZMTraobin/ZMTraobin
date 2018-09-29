package com.cmig.future.csportal.module.sys.appconfig.controllers;

import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.module.base.entity.ResponseExtData;
import com.cmig.future.csportal.module.sys.appconfig.dto.AppConfigFunction;
import com.cmig.future.csportal.module.sys.appconfig.service.IAppCommunityFunctionService;
import com.cmig.future.csportal.module.sys.appconfig.service.IAppConfigCommunityService;
import com.cmig.future.csportal.module.sys.appconfig.service.IAppConfigFunctionService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AppConfigFunctionController extends BaseController {
    @Autowired
    private IAppConfigFunctionService appConfigFunctionService;
    @Autowired
    private IAppConfigCommunityService appConfigCommunityService;
    @Autowired
    private IAppCommunityFunctionService appCommunityFunctionService;

    /**
     * 查询业主端的数据
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/app/config/function/queryOwner")
    @ResponseBody
    public ResponseData queryOwner(AppConfigFunction dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                   @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        dto.setFunctionFlag(Constants.APP_CONFIG_FLAG_OWNER);
        return new ResponseData(appConfigFunctionService.select(requestContext, dto, page, pageSize));
    }

    /**
     * 查询物业端的数据
     *
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/app/config/function/queryMC")
    @ResponseBody
    public ResponseData queryMC(AppConfigFunction dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        dto.setFunctionFlag(Constants.APP_CONFIG_FLAG);
        return new ResponseData(appConfigFunctionService.select(requestContext, dto, page, pageSize));
    }


    @RequestMapping(value = "/csp/ljh/app/config/function/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<AppConfigFunction> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(appConfigFunctionService.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/ljh/app/config/function/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<AppConfigFunction> dto) {
        appConfigFunctionService.batchDelete(dto);
        return new ResponseData();
    }

    /**
     * 跳转到详情界面,返回业主端的数据
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/app/config/function/get")
    @ResponseBody
    public ResponseData detail(AppConfigFunction dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        String id = request.getParameter("id");
        AppConfigFunction appConfigFunction = new AppConfigFunction();
        appConfigFunction.setId(id);
        dto = appConfigFunctionService.selectByPrimaryKey(requestContext, appConfigFunction);
        return new ResponseExtData(dto);
    }

    /**
     * 新增业主应用
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/app/config/function/addOwner")
    @ResponseBody
    public ResponseData addOwner(HttpServletRequest request, @RequestBody AppConfigFunction dto, BindingResult result) {
        IRequest requestContext = createRequestContext(request);

        dto.setId(IdGen.uuid());
        dto.setFunctionFlag(Constants.APP_CONFIG_FLAG_OWNER);
        appConfigFunctionService.save(dto);
        return new ResponseData();
    }

    /**
     * 更新业主应用
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/app/config/function/updateOwner")
    @ResponseBody
    public ResponseData updateOwner(HttpServletRequest request, @RequestBody AppConfigFunction dto, BindingResult result) {
        IRequest requestContext = createRequestContext(request);
        appConfigFunctionService.updateByPrimaryKeySelective(requestContext, dto);
        return new ResponseData();
    }


    /**
     * 新增业主应用
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/app/config/function/addPropertyMC")
    @ResponseBody
    public ResponseData addPropertyMC(HttpServletRequest request, @RequestBody AppConfigFunction dto, BindingResult result) {
        IRequest requestContext = createRequestContext(request);
        dto.setId(IdGen.uuid());
        dto.setFunctionFlag(Constants.APP_CONFIG_FLAG);
        appConfigFunctionService.insertSelective(requestContext, dto);
        return new ResponseData();
    }

    /**
     * 更新物业端的数据
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/app/config/function/updatePropertyMC")
    @ResponseBody
    public ResponseData updatePropertyMC(HttpServletRequest request, @RequestBody AppConfigFunction dto, BindingResult result) {
        IRequest requestContext = createRequestContext(request);
        appConfigFunctionService.updateByPrimaryKeySelective(requestContext, dto);
        return new ResponseData();
    }


    /**
     * 业主端
     * 修改app的状态，是启用还是停用
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/app/config/function/stopStatusOwner")
    @ResponseBody
    public ResponseData stopLoginStatus(HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        String id = request.getParameter("id");
        AppConfigFunction appConfigFunction = new AppConfigFunction();
        appConfigFunction.setId(id);
        appConfigFunction.setFunctionStatus(Constants.APP_CONFIG_STATUS_OFF);
        appConfigFunctionService.updateByPrimaryKeySelective(requestContext, appConfigFunction);
        return new ResponseData();
    }

    /**
     * 业主端
     * 修改app的状态，是启用还是停用
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/app/config/function/startStatusOwner")
    @ResponseBody
    public ResponseData startStatusOwner(HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        String id = request.getParameter("id");
        AppConfigFunction appConfigFunction = new AppConfigFunction();
        appConfigFunction.setFunctionStatus(Constants.APP_CONFIG_STATUS_ON);
        appConfigFunction.setId(id);
        appConfigFunctionService.updateByPrimaryKeySelective(requestContext, appConfigFunction);
        return new ResponseData();
    }

    /**
     * 物业端
     * 修改app的状态，是启用还是停用
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/app/config/function/startStatusPropertyMC")
    @ResponseBody
    public ResponseData startStatusPropertyMC(HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        String id = request.getParameter("id");
        AppConfigFunction appConfigFunction = new AppConfigFunction();
        appConfigFunction.setFunctionStatus(Constants.APP_CONFIG_STATUS_ON);
        appConfigFunction.setId(id);
        appConfigFunctionService.updateByPrimaryKeySelective(requestContext, appConfigFunction);
        return new ResponseData();
    }

    /**
     * 物业端
     * 修改app的状态，是启用还是停用
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/app/config/function/stopStatusPropertyMC")
    @ResponseBody
    public ResponseData stopStatusPropertyMC(HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        String id = request.getParameter("id");
        AppConfigFunction appConfigFunction = new AppConfigFunction();
        appConfigFunction.setFunctionStatus(Constants.APP_CONFIG_STATUS_OFF);
        appConfigFunction.setId(id);
        appConfigFunctionService.updateByPrimaryKeySelective(requestContext, appConfigFunction);
        return new ResponseData();
    }


    /**
     * 查询业主端小区开通了哪些功能
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/app/config/function/communityConfig")
    @ResponseBody
    public ResponseExtData findommunityConfig(HttpServletRequest request, Model model) {
        //获取小区配置ID
        String id = request.getParameter("id");
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<AppConfigFunction> list = appConfigFunctionService.getCommunityConfig(id);
        model.addAttribute("size", list.size());
        dataMap.put("size", list.size());
        dataMap.put("list", list);
        System.out.println();
        return new ResponseExtData(dataMap);
    }

    /**
     * 查询物业端小区开通了哪些功能
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/app/config/function/communityConfigMC")
    @ResponseBody
    public ResponseExtData findCommunityConfigMC(HttpServletRequest request, Model model) {
        //获取小区配置ID
        String id = request.getParameter("id");
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<AppConfigFunction> list = appConfigFunctionService.getCommunityConfigMC(id);
        model.addAttribute("size", list.size());
        dataMap.put("size", list.size());
        dataMap.put("list", list);
        System.out.println();
        return new ResponseExtData(dataMap);
    }

    /**
     * 查询小区开通功能之外的功能
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/app/config/function/findOtherCommuntiyConfig")
    @ResponseBody
    public ResponseData findOtherCommunityConfig(HttpServletRequest request, Model model) {
        //获取小区配置ID
        String id = request.getParameter("id");
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<AppConfigFunction> list = appConfigFunctionService.findOtherCommuntiyConfig(id);
        dataMap.put("list", list);
        dataMap.put("size", list.size());
        return new ResponseExtData(dataMap);
    }

    /**
     * 查询物业端小区开通功能之外的功能
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/app/config/function/findOtherCommuntiyConfigMC")
    @ResponseBody
    public ResponseData findOtherCommunityConfigMC(HttpServletRequest request, Model model) {
        //获取小区配置ID
        String id = request.getParameter("id");
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<AppConfigFunction> list = appConfigFunctionService.findOtherCommuntiyConfigMC(id);
        dataMap.put("list", list);
        dataMap.put("size", list.size());
        return new ResponseExtData(dataMap);
    }

    /**
     * 更新小区都开通了哪些功能
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/app/config/function/updateCommuntiyConfig")
    @ResponseBody
    public ResponseData updateCommuntiyConfig(HttpServletRequest request, @RequestBody AppConfigFunction appConfigFunction) {
        String ids = request.getParameter("ids");
        String nums = request.getParameter("nums");
        String appConfigCommunityId = request.getParameter("appConfigCommunityId");
	    appCommunityFunctionService.updateCommuntiyConfig(ids.split(","),appConfigCommunityId,nums.split(","));
        return new ResponseData();
    }


    /**
     * 根据ID查询app功能的信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/csp/ljh/app/config/function/getAppConfigFunctionById")
    @ResponseBody
    public ResponseExtData getAppConfigFunction(HttpServletRequest request, Model model) {
        IRequest requestContext = createRequestContext(request);
        //获取appID
        String id = request.getParameter("id");
        AppConfigFunction appConfigFunction = new AppConfigFunction();
        appConfigFunction.setId(id);
        AppConfigFunction appConfig = appConfigFunctionService.selectByPrimaryKey(requestContext, appConfigFunction);
     /*   List<AppConfigFunction> list = new ArrayList<AppConfigFunction>();
        list.add(appConfig);*/
        return new ResponseExtData(appConfig);
    }


    /**
     * 更新csp_ljh_app_community_function中的num
     * 目前没有用处待删除此方法
     *
     * @param request
     * @return
     */
/*    @RequestMapping(value = "/csp/ljh/app/config/function/updateAppCommunityFunctionNum")
    @ResponseBody
    public ResponseExtData updateAppCommunityFunctionNum(HttpServletRequest request, Model model) {
        IRequest requestContext = createRequestContext(request);
        String cid = request.getParameter("cid");
        String curnum = request.getParameter("curnum");
        String prevnum = request.getParameter("prevnum");
        String prevfid = request.getParameter("prevfid");
        String curfid = request.getParameter("curfid");
        //查询上一个AppCommunityFunction对象

        Map<String,Object> paramMap = new HashMap<String,Object>();
        AppCommunityFunction prevappCommunityFunction = new AppCommunityFunction();
        prevappCommunityFunction.setNumber(Long.parseLong(prevnum));
        prevappCommunityFunction.setFid(prevfid);
        prevappCommunityFunction.setDelFlag("0");
        prevappCommunityFunction.setCid(cid);

        List<AppCommunityFunction> prevApp = appCommunityFunctionService.getAppCommunityFunction(prevappCommunityFunction);

        //查询当前的AppCommunityFunction对象
        AppCommunityFunction currappCommunityFunction = new AppCommunityFunction();
        currappCommunityFunction.setNumber(Long.parseLong(curnum));
        currappCommunityFunction.setFid(curfid);
        currappCommunityFunction.setDelFlag("0");
        currappCommunityFunction.setCid(cid);
        paramMap.put("currappCommunityFunction",currappCommunityFunction);

        List<AppCommunityFunction> currApp = appCommunityFunctionService.getAppCommunityFunction(currappCommunityFunction);

        AppCommunityFunction prevAppCon = prevApp.get(0);

        AppCommunityFunction currAppCon = currApp.get(0);


        prevAppCon.setNumber(Long.parseLong(curnum));
        prevAppCon.setFid(curfid);

        currAppCon.setNumber(Long.parseLong(prevnum));
        currAppCon.setFid(prevfid);

        *//**
         * 更新对象，两个对象互换位置
         *//*

        appCommunityFunctionService.updateByPrimaryKeySelective(new ServiceRequest(),prevAppCon);

        appCommunityFunctionService.updateByPrimaryKeySelective(new ServiceRequest(),currAppCon);



        //获取appID
      *//*  String id = request.getParameter("id");
        AppConfigFunction appConfigFunction = new AppConfigFunction();
        appConfigFunction.setId(id);
        AppConfigFunction appConfig = appConfigFunctionService.selectByPrimaryKey(requestContext, appConfigFunction);*//*
     *//*   List<AppConfigFunction> list = new ArrayList<AppConfigFunction>();
        list.add(appConfig);*//*
        return new ResponseExtData(null);
    }*/

	/**
	 * 批量配置小区开通功能
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/csp/ljh/app/config/function/batchUpdateCommuntiyConfig")
	@ResponseBody
	public ResponseData batchUpdateCommuntiyConfig(HttpServletRequest request) {
		String ids = request.getParameter("ids");
		String nums = request.getParameter("nums");
		String appConfigCommunityId = request.getParameter("appConfigCommunityId");
		appCommunityFunctionService.batchAdd(ids.split(","),appConfigCommunityId.split(","),nums.split(","));
		return new ResponseData();
	}

	/**
	 * 批量清空小区开通功能
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/csp/ljh/app/config/function/batchDeleteByCid")
	@ResponseBody
	public ResponseData batchDeleteByCid(HttpServletRequest request) {
		String appConfigCommunityId = request.getParameter("appConfigCommunityId");
		appCommunityFunctionService.batchDeleteByCid(appConfigCommunityId.split(","));
		return new ResponseData();
	}
}

package com.cmig.future.csportal.api.common.controllers;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.properties.community.constants.CommunityConstants;
import com.cmig.future.csportal.module.properties.community.dto.BaseCommunity;
import com.cmig.future.csportal.module.properties.community.service.IBaseCommunityService;
import com.cmig.future.csportal.module.sys.appconfig.dto.AppConfigCommunity;
import com.cmig.future.csportal.module.sys.appconfig.dto.AppConfigFunction;
import com.cmig.future.csportal.module.sys.appconfig.service.IAppConfigFunctionService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "${commonPath}/appconfig/appConfig")
public class AppFunctionController extends BaseExtendController {

    @Autowired
    private IAppConfigFunctionService appConfigFunctionService;

    @Autowired
    private IBaseCommunityService baseCommunityService;

    /**
     * 此方法描述的是：查询对客端配置功能列表
     *
     * @param appConfigCommunity
     * @param request
     * @param response
     * @return String
     */
	@CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/getOwnerFunctions", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp getOwnerFunctions(AppConfigCommunity appConfigCommunity,HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.isEmpty(appConfigCommunity.getCommunityId())){
            //throw  new DataWarnningException("小区id不能为空");
	        BaseCommunity baseCommunity=new BaseCommunity();
	        baseCommunity.setCommunityCode(CommunityConstants.DEFAULT_COMMUNITY_CODE);
	        List<BaseCommunity> list=baseCommunityService.findListByCommunityCode(baseCommunity);
	        if(null==list||list.size()==0){
		        throw new ServiceException(RestApiExceptionEnums.NULL_DEFAULT_COMMUNITY);
	        }else{
		        appConfigCommunity.setCommunityId(list.get(0).getId());
	        }
        }

	    //快捷功能模块分类，默认是乐家慧首页快捷服务模块
	    String functionFlag=getParam(request,"functionFlag",Constants.APP_CONFIG_FLAG_OWNER);

        JSONObject json = new JSONObject();
        //公用
        List<AppConfigFunction> publich;
        //配置功能信息查询
        AppConfigFunction function = new AppConfigFunction();
        //配置功能类型
        function.setFunctionFlag(functionFlag);
        //状态为开启
        function.setFunctionStatus(Constants.APP_CONFIG_STATUS_ON);
        function.setCommunityId(appConfigCommunity.getCommunityId());
        publich=appConfigFunctionService.findList(function);
        JSONArray jsonArray=new JSONArray();
        if(!StringUtils.isEmpty(publich)) {
            for (AppConfigFunction entry : publich) {
                JSONObject obj = new JSONObject();
                obj.put("functionCode",entry.getFunctionCode());
                obj.put("functionName",entry.getFunctionName());
                obj.put("functionUrl",entry.getFunctionUrl());
                if(!StringUtils.isEmpty(entry.getFunctionUrl())&&!(entry.getFunctionUrl().startsWith("http://")||entry.getFunctionUrl().startsWith("https://"))){
                    BaseCommunity baseCommunity=baseCommunityService.get(appConfigCommunity.getCommunityId());
	                if(baseCommunity!=null && !StringUtils.isEmpty(baseCommunity.getServerUrl())){
		                if (!baseCommunity.getServerUrl().endsWith("/") && !entry.getFunctionUrl().startsWith("/")) {
			                entry.setFunctionUrl("/" + entry.getFunctionUrl());
		                }
		                obj.put("functionUrl", baseCommunity.getServerUrl() + entry.getFunctionUrl());
	                }
                }
                obj.put("functionImg",Global.getFullImagePath(entry.getFunctionImg()));
                obj.put("functionNumber",entry.getFunctionNumber());
                obj.put("functionSort",entry.getFunctionSort());
                obj.put("loginStatus",entry.getLoginStatus());
                jsonArray.add(obj);
            }
        }

        json.put("exterior", jsonArray);
        //接口返回值整理
        RetApp retApp = new RetApp();
        retApp.setTotall(new Long(jsonArray.size()));
        retApp.setStatus(OK);
        retApp.setMessage("查询成功");
        retApp.setData(json);

        return retApp;
    }

	/**
	 * 我的缴费
	 */
	public final static String MY_FUNCTION_CSP_JF = "/mgt/UnSystemPayment/myPayment.html";
	/**
	 * 我的房屋
	 */
	public final static String MY_FUNCTION_CSP_FW = "/mgt/UnSystemPayment/myhouse.html";
	/**
	 * 我的交易记录
	 */
	public final static String MY_FUNCTION_CSP_JYJL = "/mgt/UnSystemPayment/userTransactionRecord.html";



	/**
	 * 我的缴费
	 */
	public final static String MY_FUNCTION_MGT_JF = "/app/ZMJF/blank_redirect_wdjf.html";
	/**
	 * 我的房屋
	 */
	public final static String MY_FUNCTION_MGT_FW = "/app/ZMJJ/blank_redirect_wdfw.html";
	/**
	 * 我的管家
	 */
	public final static String MY_FUNCTION_MGT_GJ = "/app/ZMJJ/blank_redirect_wdgj.html";
	/**
	 * 我的交易记录
	 */
	public final static String MY_FUNCTION_MGT_JYJL = "/app/ZMJF/blank_redirect_jyjl.html";
	/**
	 * 我的报修最新
	 */
	public final static String MY_FUNCTION_MGT_BX = "/app/ZMJJ/blank_redirect.html";



	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/my/function", produces = {"application/json"}, method = RequestMethod.POST)
	@ResponseBody
	public RetApp getMyFunctions(AppConfigCommunity appConfigCommunity,HttpServletRequest request, HttpServletResponse response)throws Exception {
		JSONObject json = new JSONObject();
		if(!StringUtils.isEmpty(appConfigCommunity.getCommunityId())){
			BaseCommunity baseCommunity= baseCommunityService.get(appConfigCommunity.getCommunityId());
			if(null==baseCommunity){
				throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"小区ID");
			}

			String serverUrl=baseCommunity.getServerUrl()==null?"":baseCommunity.getServerUrl();

			//配置功能信息查询
			AppConfigFunction function = new AppConfigFunction();
			//配置功能类型
			function.setFunctionFlag(Constants.APP_CONFIG_FLAG_OWNER);
			//状态为开启
			function.setFunctionStatus(Constants.APP_CONFIG_STATUS_ON);
			function.setCommunityId(appConfigCommunity.getCommunityId());

			List<AppConfigFunction> list=appConfigFunctionService.findList(function);
			Map map=new HashMap();
			if(null!=list&&list.size()>0){
				for(AppConfigFunction entry:list){
					map.put(entry.getFunctionSort(),entry.getFunctionUrl());
				}
			}

			if(Constants.NO.equals(baseCommunity.getIsRemoteAuthen())){
				String CSP_H5_SERVER_URL=Global.getConfig("CSP.MY.CENTER.MGT.SERVER.URL","");
				json.put("isStanderMgt","Y");//是否csp标准小区
				json.put("myHouse",CSP_H5_SERVER_URL+MY_FUNCTION_CSP_FW);
				if(map.containsKey(Constants.FUNCTION_SORT_PAYFEES_STANDER)){
					json.put("myPayment",CSP_H5_SERVER_URL+MY_FUNCTION_CSP_JF);
					json.put("userTransactionRecord",CSP_H5_SERVER_URL+MY_FUNCTION_CSP_JYJL);
					json.put("payfeesUrl",getFunctionUrl(serverUrl,map.containsKey(Constants.FUNCTION_SORT_PAYFEES_STANDER)?map.get(Constants.FUNCTION_SORT_PAYFEES_STANDER).toString():""));//物业缴费入口url
				}else{
					json.put("myPayment","");
					json.put("userTransactionRecord","");
					json.put("payfeesUrl","");
				}
				json.put("myRepair","");//报修
				json.put("myHousekeeper","");//管家
				json.put("myPasscard","");//门禁
				json.put("myRenovation","");//装修申请

			}else{
				json.put("isStanderMgt","N");//是否csp标准小区
				json.put("myHouse",serverUrl+MY_FUNCTION_MGT_FW);//我的房屋
				json.put("myPayment",map.containsKey(Constants.FUNCTION_SORT_PAYFEES)?serverUrl+ MY_FUNCTION_MGT_JF :"");//我的缴费
				json.put("userTransactionRecord",map.containsKey(Constants.FUNCTION_SORT_PAYFEES)?serverUrl+MY_FUNCTION_MGT_JYJL:"");//交易记录
				json.put("payfeesUrl",getFunctionUrl(serverUrl,map.containsKey(Constants.FUNCTION_SORT_PAYFEES)?map.get(Constants.FUNCTION_SORT_PAYFEES).toString():""));//物业缴费入口url
				json.put("myRepair",map.containsKey(Constants.FUNCTION_SORT_REPAIRS)?serverUrl+MY_FUNCTION_MGT_BX:"");//报修
				json.put("myHousekeeper",map.containsKey(Constants.FUNCTION_SORT_MYHOUSEKEEPER)?serverUrl+MY_FUNCTION_MGT_GJ:"");//管家
				json.put("myPasscard",getFunctionUrl(serverUrl,map.containsKey(Constants.FUNCTION_SORT_PASSCARD)?map.get(Constants.FUNCTION_SORT_PASSCARD).toString():""));//门禁
				json.put("myRenovation",getFunctionUrl(serverUrl,map.containsKey(Constants.FUNCTION_SORT_RENOVATION)?map.get(Constants.FUNCTION_SORT_RENOVATION).toString():""));//装修申请
			}


			json.put("phone",StringUtils.isEmpty(baseCommunity.getPhone()) ? "" : baseCommunity.getPhone());
			json.put("servicePhone","0371-4566777");
			json.put("communityId", appConfigCommunity.getCommunityId());
			json.put("communityName", baseCommunity.getCommunityName());
			json.put("sourceSystem", StringUtils.isEmpty(baseCommunity.getSourceSystem()) ? "" : baseCommunity.getSourceSystem());
			json.put("sourceSystemCommunityId", StringUtils.isEmpty(baseCommunity.getSourceSystemId()) ? "" : baseCommunity.getSourceSystemId());
			json.put("serverUrl", StringUtils.isEmpty(baseCommunity.getServerUrl()) ? "" : baseCommunity.getServerUrl());
			//是否支持联系人管理功能 Y 支持 N 不支持
			if(Constants.YES.equals(baseCommunity.getResidentManager())) {
				json.put("residentManager", "Y");
			}else{
				json.put("residentManager", "N");
			}
		}
		return RetAppUtil.success(json,"查询成功");
	}

	/**
	 * 拼接functionURL
	 * @param serverUrl
	 * @param functionUrl
	 * @return
	 */
	public String getFunctionUrl(String serverUrl,String functionUrl){
		String result=functionUrl;
		if(!StringUtils.isEmpty(functionUrl)){
			if(!(functionUrl.startsWith("http://")||functionUrl.startsWith("https://"))){
				if (!serverUrl.endsWith("/") && !functionUrl.startsWith("/")) {
					functionUrl="/"+functionUrl;
				}
				result=serverUrl+ functionUrl;
			}
		}
		return result;
	}

    /**
     * 此方法描述的是：处理图片路径
     *
     * @param functions
     * @return List<AppConfigFunction>
     * @author:jinghao.che@zymobi.com
     */

    public List<AppConfigFunction> getFunctionPath(List<AppConfigFunction> functions) {
        if (functions != null && functions.size() > 0) {
            for (int i = 0; i < functions.size(); i++) {
                if (functions.get(i) != null && !StringUtils.isEmpty(functions.get(i).getFunctionImg())) {
                    //处理图标路径
                    functions.get(i).setFunctionImg(Global.getFullImagePath(functions.get(i).getFunctionImg()));
                }
            }
        }
        return functions;
    }


    /**
     * 此方法描述的是：获取物业端功能
     *
     * @param appConfigCommunity
     * @param request
     * @param response
     * @return String
     * @author:jinghao.che@zymobi.com
     */
/*
    @RequestMapping(value = "/getFunctions", produces = {"application/json"}, method = RequestMethod.POST)
    public String getFunctions(AppConfigCommunity appConfigCommunity,
                               HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(appConfigCommunity.getCommunityId())) {
            RetApp retApp = new RetApp(FAIL, "小区id为空", "");
            return renderString(response, retApp);
        }

        //获取小区信息
        BaseCommunity community = baseCommunityService.findCommunityByOfficeId(appConfigCommunity.getCommunityId());
        if (community != null) {
            appConfigCommunity.setCommunityId(community.getId());
        } else {
            RetApp retApp = new RetApp(FAIL, "该小区无功能配置信息", "");
            return renderString(response, retApp);
        }
        //配置功能类型处理（物业）
        appConfigCommunity.setConfigFlag(Constants.APP_CONFIG_FLAG);
        List<AppConfigCommunity> communitys = appConfigCommunityService.findListApp(appConfigCommunity);
        if (communitys != null && communitys.size() > 0) {
            appConfigCommunity = communitys.get(0);
        } else {
            RetApp retApp = new RetApp(FAIL, "该小区无功能配置信息", "");
            return renderString(response, retApp);
        }
        //配置功能信息查询
        AppConfigFunction function = new AppConfigFunction();
        function.setFunctionCodes(appConfigCommunity.getFunctionCodes());
        function.setFunctionFlag(appConfigCommunity.getConfigFlag());
        function.setCid(appConfigCommunity.getId());
        List<AppConfigFunction> functions = appConfigFunctionService.findList(function);
        functions = getFunctionPath(functions);
        appConfigCommunity.setFunctions(functions);
        //接口返回值整理
        RetApp retApp = new RetApp();
        retApp.setStatus(OK);
        retApp.setMessage("查询成功");
        retApp.setData(appConfigCommunity);
        return renderString(response, retApp);
    }

    *//**
     * 此方法描述的是：处理图片路径
     *
     * @param functions
     * @return List<AppConfigFunction>
     * @author:jinghao.che@zymobi.com
     *//*

    public List<AppConfigFunction> getFunctionPath(List<AppConfigFunction> functions) {
        if (functions != null && functions.size() > 0) {
            for (int i = 0; i < functions.size(); i++) {
                if (functions.get(i) != null && !StringUtils.isEmpty(functions.get(i).getFunctionImg())) {
                    //处理图标路径
                    functions.get(i).setFunctionImg(Global.getFullImagePath(functions.get(i).getFunctionImg()));
                }
            }
        }
        return functions;
    }

    *//**
     * 此方法描述的是：判断当前类目功能是否配置(业主)
     *
     * @param communityId
     * @param request
     * @param response
     * @return String
     * @author:jinghao.che@zymobi.com
     *//*

    @RequestMapping(value = "/checkFunctionOwner", produces = {"application/json"}, method = RequestMethod.POST)
    public String checkFunctionOwner(String communityId, String functionSort,
                                     HttpServletRequest request, HttpServletResponse response) {
        //校验小区
        if (StringUtils.isBlank(communityId)) {
            RetApp retApp = new RetApp(FAIL, "小区id为空", "");
            return renderString(response, retApp);
        }
        //校验功能类目
        if (StringUtils.isBlank(functionSort)) {
            RetApp retApp = new RetApp(FAIL, "功能类目为空", "");
            return renderString(response, retApp);
        }

        //配置功能
        AppConfigCommunity community = new AppConfigCommunity();
        community.setConfigFlag(Constants.APP_CONFIG_FLAG_OWNER);
        community.setCommunityId(communityId);
        List<AppConfigCommunity> communitys = appConfigCommunityService.findListApp(community);
        if (communitys != null && communitys.size() > 0) {
            community = communitys.get(0);
        } else {
            RetApp retApp = new RetApp(FAIL, "该小区无功能配置信息", "");
            return renderString(response, retApp);
        }
        //配置功能信息查询
        AppConfigFunction function = new AppConfigFunction();
        function.setFunctionFlag(community.getConfigFlag());
        function.setCid(community.getId());
        function.setFunctionSort(functionSort);
        List<AppConfigFunction> functions = appConfigFunctionService.findList(function);
        functions = getFunctionPath(functions);
        if (functions != null && functions.size() > 0) {
            function = functions.get(0);
        } else {
            RetApp retApp = new RetApp(FAIL, "小区暂未开通此业务", "");
            return renderString(response, retApp);
        }
        //接口返回值整理
        RetApp retApp = new RetApp();
        retApp.setStatus(OK);
        retApp.setMessage("查询成功");
        retApp.setData(function);
        return renderString(response, retApp);
    }

    *//**
     * 此方法描述的是：判断当前类目功能是否配置(物业)
     *
     * @param communityId
     * @param functionSort
     * @param request
     * @param response
     * @return String
     * @author:jinghao.che@zymobi.com
     *//*

    @RequestMapping(value = "/checkFunction", produces = {"application/json"}, method = RequestMethod.POST)
    public String checkFunction(String communityId, String functionSort,
                                HttpServletRequest request, HttpServletResponse response) {
        //校验小区
        if (StringUtils.isBlank(communityId)) {
            RetApp retApp = new RetApp(FAIL, "小区id为空", "");
            return renderString(response, retApp);
        }
        //校验功能类目
        if (StringUtils.isBlank(functionSort)) {
            RetApp retApp = new RetApp(FAIL, "功能类目为空", "");
            return renderString(response, retApp);
        }

        //配置功能
        AppConfigCommunity community = new AppConfigCommunity();
        community.setConfigFlag(Constants.APP_CONFIG_FLAG);
        community.setCommunityId(communityId);
        List<AppConfigCommunity> communitys = appConfigCommunityService.findListApp(community);
        if (communitys != null && communitys.size() > 0) {
            community = communitys.get(0);
        } else {
            RetApp retApp = new RetApp(FAIL, "该小区无功能配置信息", "");
            return renderString(response, retApp);
        }
        //配置功能信息查询
        AppConfigFunction function = new AppConfigFunction();
        function.setFunctionFlag(community.getConfigFlag());
        function.setCid(community.getId());
        function.setFunctionSort(functionSort);
        List<AppConfigFunction> functions = appConfigFunctionService.findList(function);
        functions = getFunctionPath(functions);
        if (functions != null && functions.size() > 0) {
            function = functions.get(0);
        } else {
            RetApp retApp = new RetApp(FAIL, "小区暂未开通此业务", "");
            return renderString(response, retApp);
        }
        //接口返回值整理
        RetApp retApp = new RetApp();
        retApp.setStatus(OK);
        retApp.setMessage("查询成功");
        retApp.setData(function);
        return renderString(response, retApp);
    }*/
}

/**
 * .
 */
package com.cmig.future.csportal.api.app.villager.follow.controllers;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.exceptions.ServiceExceptionHelper;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.sys.code.CodeUtil;
import com.cmig.future.csportal.module.sys.utils.UserTokenUtils;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.cmig.future.csportal.module.user.attentionCommunity.dto.AttentionCommunityUser;
import com.cmig.future.csportal.module.user.attentionCommunity.service.IAttentionCommunityUserService;
import com.cmig.future.csportal.module.villager.family.dto.AssetsInfo;
import com.cmig.future.csportal.module.villager.family.dto.FamilyInfo;
import com.cmig.future.csportal.module.villager.family.dto.HousingInfo;
import com.cmig.future.csportal.module.villager.family.dto.LandInfo;
import com.cmig.future.csportal.module.villager.family.service.IAssetsInfoService;
import com.cmig.future.csportal.module.villager.family.service.IFamilyInfoService;
import com.cmig.future.csportal.module.villager.family.service.IHousingInfoService;
import com.cmig.future.csportal.module.villager.family.service.ILandInfoService;
import com.cmig.future.csportal.module.villager.follow.service.IFollowService;
import com.cmig.future.csportal.module.villager.wealth.dto.AnimalCost;
import com.cmig.future.csportal.module.villager.wealth.dto.AnimalType;
import com.cmig.future.csportal.module.villager.wealth.dto.PlantCost;
import com.cmig.future.csportal.module.villager.wealth.dto.PlantType;
import com.cmig.future.csportal.module.villager.wealth.dto.WealthPlan;
import com.cmig.future.csportal.module.villager.wealth.service.IAnimalCostService;
import com.cmig.future.csportal.module.villager.wealth.service.IAnimalTypeService;
import com.cmig.future.csportal.module.villager.wealth.service.IPlantCostService;
import com.cmig.future.csportal.module.villager.wealth.service.IPlantTypeService;
import com.cmig.future.csportal.module.villager.wealth.service.IWealthPlanService;
import com.cmig.future.csportal.module.villager.wealth.service.IWealthService;
import com.google.gson.JsonArray;
import com.hand.hap.core.IRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * app用户Controller
 *
 * @author su
 * @version 2018
 */
@RestController
@RequestMapping(value = "${userPath}")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FollowApiController extends BaseExtendController {

	@Autowired
    private IFollowService iFollowService;
	
	@Autowired
	IAttentionCommunityUserService iAttentionCommunityUserService;

    /**
     * 添加关注
     */
    @RequestMapping(value = "/st/villager/follow/addFollow", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp addFollow(HttpServletRequest request) {
    	RetApp retApp = new RetApp();
    	// token获取用户id
        String token = getParam(request, "token", "");
        if(StringUtils.isEmpty(token)){
            return RetAppUtil.tokenDisabled();
        }
    	String appUserId = UserTokenUtils.getUserIdByToken(token);
    	// 获取参数~被关注的ID
    	String followedId = getParam(request, "followedId", "");
    	if(StringUtils.isEmpty(followedId)){
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
        // 设置返回值
    	Map map = iFollowService.addFollow(followedId,appUserId);
        return RetAppUtil.success(map,"关注成功!");
    }
    
    /**
     * 查询关注列表--我关注的
     */
    @RequestMapping(value = "/st/villager/follow/queryFollowed", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp queryFollowed(HttpServletRequest request,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
    	RetApp retApp = new RetApp();
    	// token获取用户id
        String token = getParam(request, "token", "");
        if(StringUtils.isEmpty(token)){
            return RetAppUtil.tokenDisabled();
        }
    	String uid = getParam(request, "uid", "");
    	if(StringUtils.isEmpty(uid)) {
    		throw ServiceExceptionHelper.argsNull();
    	}
        // 设置返回值
    	Map map = iFollowService.queryFollowed(uid,page,pageSize);
    	List<Map> list = (List<Map>) map.get("followList");
    	if(list != null) {
    		for(Map mapicon : list) {
    			if(mapicon.get("USER_ICON") == null) {
    				mapicon.put("USER_ICON", Global.getDefaultImagePath(request));
    			}else {
    				mapicon.put("USER_ICON", Global.getFullImagePath(String.valueOf(mapicon.get("USER_ICON"))));
    			}
    			AttentionCommunityUser cu = iAttentionCommunityUserService.getDefault(String.valueOf(mapicon.get("id")));
    			mapicon.put("communityName",cu.getCommunityName());
    		}
    	}else {
    		String[] arr = new String[0];
    		map.put("followList",arr);
    	}
    	
        return RetAppUtil.success(map,"查询成功!");
    }
    
    /**
     * 查询粉丝列表--关注我的
     */
    @RequestMapping(value = "/st/villager/follow/queryFollower", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp queryFollower(HttpServletRequest request,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
    	RetApp retApp = new RetApp();
    	// token获取用户id
        String token = getParam(request, "token", "");
        if(StringUtils.isEmpty(token)){
            return RetAppUtil.tokenDisabled();
        }
    	String uid = getParam(request, "uid", "");
    	if(StringUtils.isEmpty(uid)) {
    		throw ServiceExceptionHelper.argsNull();
    	}
        // 设置返回值
    	Map map = iFollowService.queryFollower(uid,page,pageSize);
    	List<Map> list = (List<Map>) map.get("followList");
    	if(list != null) {
    		for(Map mapicon : list) {
    			if(mapicon.get("USER_ICON") == null) {
    				mapicon.put("USER_ICON", Global.getDefaultImagePath(request));
    			}else {
    				mapicon.put("USER_ICON", Global.getFullImagePath(String.valueOf(mapicon.get("USER_ICON"))));
    			}
    			AttentionCommunityUser cu = iAttentionCommunityUserService.getDefault(String.valueOf(mapicon.get("id")));
    			mapicon.put("communityName",cu.getCommunityName());
    		}
    	}else {
    		String[] arr = new String[0];
    		map.put("followList",arr);
    	}
    	
        return RetAppUtil.success(map,"查询成功!");
    }
    
    /**
     * 查询关注状态--service层接口-进入个人首页时调用
     */
    @RequestMapping(value = "/st/villager/follow/queryFollowStatus", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp queryFollowStatus(HttpServletRequest request) {
    	RetApp retApp = new RetApp();
    	// token获取用户id
        String token = getParam(request, "token", "");
        if(StringUtils.isEmpty(token)){
            return RetAppUtil.tokenDisabled();
        }
    	String appUserId = UserTokenUtils.getUserIdByToken(token);
    	// 获取参数~被关注的ID
    	String followedId = getParam(request, "followedId", "");
    	if(StringUtils.isEmpty(followedId)){
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
        // 设置返回值
    	Boolean followStatus = iFollowService.queryFollowStatus(followedId, appUserId);
    	Map map = new HashMap<>();
    	map.put("followStatus", followStatus);
        return RetAppUtil.success(map,"查询成功!");
    }
    
    /**
     * 取消关注
     */
    @RequestMapping(value = "/st/villager/follow/delFollow", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp delFollow(HttpServletRequest request) {
    	RetApp retApp = new RetApp();
    	// token获取用户id
        String token = getParam(request, "token", "");
        if(StringUtils.isEmpty(token)){
            return RetAppUtil.tokenDisabled();
        }
    	String appUserId = UserTokenUtils.getUserIdByToken(token);
    	// 获取参数~被关注的ID
    	String followedId = getParam(request, "followedId", "");
    	if(StringUtils.isEmpty(followedId)){
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
        // 设置返回值
    	Map map = iFollowService.delFollow(followedId,appUserId);
        return RetAppUtil.success(map,"取消关注成功!");
    }
}
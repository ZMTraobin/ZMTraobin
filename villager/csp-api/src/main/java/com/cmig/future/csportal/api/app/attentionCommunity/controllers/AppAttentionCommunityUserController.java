package com.cmig.future.csportal.api.app.attentionCommunity.controllers;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.properties.community.dto.BaseCommunity;
import com.cmig.future.csportal.module.properties.community.service.IBaseCommunityService;
import com.cmig.future.csportal.module.sys.utils.UserTokenUtils;
import com.cmig.future.csportal.module.user.attentionCommunity.dto.AttentionCommunityUser;
import com.cmig.future.csportal.module.user.attentionCommunity.service.IAttentionCommunityUserService;
import com.cmig.future.csportal.module.user.attentionCommunity.util.CommunityHelper;
import com.github.pagehelper.Page;
import com.hand.hap.core.IRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * AttentionCommunityUserController
 * 关注社区
 *
 * Created by bubu on 2017/3/21.
 */
@Controller
@ResponseBody
@RequestMapping(value = "${userPath}/")
public class AppAttentionCommunityUserController extends BaseExtendController {

    private static Logger logger= LoggerFactory.getLogger(AppAttentionCommunityUserController.class);
    @Autowired
    private IBaseCommunityService baseCommunityService;

    @Autowired
    private IAttentionCommunityUserService attentionCommunityUserService;

    /**
     * 扫码根据小区ID查询小区
     *
     * @author:bubu
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = " /attentionCommunityUser/findCommunityByCommunityId", produces = { "application/json" }, method = RequestMethod.POST)
    public String findCommunityByCommunityId(@ModelAttribute AttentionCommunityUser attentionCommunityUser, HttpServletRequest request, HttpServletResponse response) {

        RetApp retApp = new RetApp();
        String communityId = getParam(request, "id", "");
        String token = getParam(request, "token", "");
        try {
            if (StringUtils.isEmpty(communityId)) {
                throw new DataWarnningException("小区id未获取!");
            }

            BaseCommunity entity = baseCommunityService.get(communityId);
            JSONObject object = new JSONObject();
            object.put("communityId", entity.getId());
            object.put("communityName", entity.getCommunityName());
            object.put("communityAddress", entity.getAddress() != null ? entity.getAddress() : "");
            object.put("communityPhoneNumber", entity.getPhone() == null ? "" : entity.getPhone());
            object.put("sourceSystem", StringUtils.isEmpty(entity.getSourceSystem()) ? "" : entity.getSourceSystem());
            object.put("sourceSystemCommunityId", StringUtils.isEmpty(entity.getSourceSystemId()) ? "" : entity.getSourceSystemId());
            object.put("attentionStatus", "");
            object.put("serverUrl", StringUtils.isEmpty(entity.getServerUrl()) ? "" : entity.getServerUrl());
            object.put("residentManager", StringUtils.isEmpty(entity.getResidentManager()) ? "" : entity.getResidentManager());
	        if(Constants.NO.equals(entity.getIsRemoteAuthen())) {
		        object.put("isStanderMgt", "Y");
	        }else{
		        object.put("isStanderMgt", "N");
	        }
            // 如果登录状态,当前小区与已关注小区做比对,当前已关注返回1,未关注返回0
            if (!StringUtils.isEmpty(token)) {
                String userId = UserTokenUtils.getUserIdByToken(token);//
                if (StringUtils.isEmpty(userId)) {
                    renderString(response, RetAppUtil.tokenDisabled());
                }
                AttentionCommunityUser queryAttentionCommunityUser = new AttentionCommunityUser();
                queryAttentionCommunityUser.setUserId(userId);
                queryAttentionCommunityUser.setCommunityId(communityId);
                AttentionCommunityUser thisAttentionCommunityUser = attentionCommunityUserService.getEntity(queryAttentionCommunityUser);
                if (null != thisAttentionCommunityUser) {
                    object.put("attentionStatus", "1");
                } else {
                    object.put("attentionStatus", "0");
                }
            }
            retApp.setData(object);
            retApp.setStatus(OK);
            retApp.setMessage("查询成功!");
            return renderString(response, retApp);
        } catch (Exception e) {
            String errMsg = e.getMessage();
            retApp.setStatus(FAIL);
            retApp.setMessage(errMsg);
            return renderString(response, retApp);
        }
    }

    /**
     * 改变当前关注的小区
     *
     * @author:bubu
     * @param data
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/st/attentionCommunityUser/updateAttention", produces = { "application/json" }, method = RequestMethod.POST)
    public String updateAttention(@ModelAttribute AttentionCommunityUser data, HttpServletRequest request,HttpServletResponse response) {

        RetApp retApp = new RetApp();
        String communityId = getParam(request, "communityId", "");
        String token = getParam(request, "token", "");
        String userId = UserTokenUtils.getUserIdByToken(token);
        try {
            if (StringUtils.isEmpty(communityId)) {
                throw new DataWarnningException("小区ID未获取!");
            }
            if (StringUtils.isEmpty(userId)) {
                throw new DataWarnningException("token不正确!");
            }

            AttentionCommunityUser attentionCommunityUser = new AttentionCommunityUser();
            attentionCommunityUser.setUserId(userId);
            attentionCommunityUser.setCommunityId(communityId);
            int count = attentionCommunityUserService.getAttentionCount(attentionCommunityUser);
            if (count <= 0) {
                retApp.setStatus(FAIL);
                retApp.setMessage("您未关注该社区,请重试!");
                return renderString(response, retApp);
            } else {
                attentionCommunityUser.setIsAttention(CommunityHelper.IS_ATTENTION);
                attentionCommunityUserService.updateAttention(attentionCommunityUser);
                retApp.setStatus(OK);
                retApp.setMessage("切换小区成功!");
                return renderString(response, retApp);
            }

        } catch (Exception e) {
            String errMsg = e.getMessage();
            retApp.setStatus(FAIL);
            retApp.setMessage(errMsg);
            return renderString(response, retApp);
        }
    }

    /**
     * 分页查询所有小区
     *
     * @author:bubu
     * @param data
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/attentionCommunityUser/findCommunityList", produces = { "application/json" }, method = RequestMethod.POST)
    public String findCommunityList(@ModelAttribute BaseCommunity data,  HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(defaultValue = DEFAULT_PAGE) int pageNo,
                                    @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {

        IRequest requestContext = createRequestContext(request);
        RetApp retApp = new RetApp();
        String token = getParam(request, "token", "");

        try {
            JSONArray communityArray = new JSONArray();
            // 登录状态
            if (!StringUtils.isEmpty(token)) {
                String userId = UserTokenUtils.getUserIdByToken(token);
                if (StringUtils.isEmpty(userId)) {
                    renderString(response, RetAppUtil.tokenDisabled());
                }
                data.setUserId(userId);
            }
            //HAP分页过滤软删除的数据
            //data.setDelFlag(BaseExtDTO.DEL_FLAG_NORMAL);
            List<BaseCommunity> pageAppList = baseCommunityService.findList(requestContext,data,pageNo,pageSize);

            if (null != pageAppList && pageAppList.size() > 0) {
                for (BaseCommunity entity : pageAppList) {
                    JSONObject object = new JSONObject();
                    object.put("communityId", entity.getId());
                    object.put("cityName", entity.getCityName() != null ? entity.getCityName() : "");
                    object.put("communityName", entity.getCommunityName());
                    object.put("communityAddress", entity.getAddress() != null ? entity.getAddress() : "");
                    object.put("isCurrentAttention", entity.getIsCurrentAttention() != null ? entity.getIsCurrentAttention() : "0");
                    object.put("isAttention", entity.getIsAttention() != null ? entity.getIsAttention() : "N");
                    object.put("sourceSystem", StringUtils.isEmpty(entity.getSourceSystem()) ? "" : entity.getSourceSystem());
                    object.put("sourceSystemCommunityId", StringUtils.isEmpty(entity.getSourceSystemId()) ? "" : entity.getSourceSystemId());
                    object.put("serverUrl", StringUtils.isEmpty(entity.getServerUrl()) ? "" : entity.getServerUrl());
                    object.put("residentManager", StringUtils.isEmpty(entity.getResidentManager()) ? "" : entity.getResidentManager());
	                if(Constants.NO.equals(entity.getIsRemoteAuthen())) {
		                object.put("isStanderMgt", "Y");
	                }else{
		                object.put("isStanderMgt", "N");
	                }
                    communityArray.add(object);
                }
            }

            if (pageAppList != null&&pageAppList instanceof Page) {
                retApp.setTotall(Long.valueOf(((Page)pageAppList).getTotal()));
            }
            retApp.setData(communityArray);
            retApp.setStatus(OK);
            retApp.setMessage("查询成功!");
            return renderString(response, retApp);
        } catch (Exception e) {
            String errMsg = e.getMessage();
            retApp.setStatus(FAIL);
            retApp.setMessage(errMsg);
            return renderString(response, retApp);
        }
    }


    /**
     * 根据城市id获取小区
     *
     * @author bubu
     */
    @RequestMapping(value = "/community/communityListByCity",produces={"application/json"},method = RequestMethod.POST)
    public String communityListByCity(@ModelAttribute BaseCommunity data, HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                      @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {

        IRequest requestContext = createRequestContext(request);
        RetApp retApp = new RetApp();
        String cityId = getParam(request, "cityId", "");
        data.setCityId(cityId);
        JSONArray communityArray = new JSONArray();
        List<BaseCommunity> pageAppList = baseCommunityService.findList(requestContext,data,page,pageSize);

        if (null != pageAppList && pageAppList.size() > 0) {
            for (BaseCommunity entity : pageAppList) {
                JSONObject object = new JSONObject();
                object.put("communityId", entity.getId());
                object.put("communityName", entity.getCommunityName());
                object.put("communityAddress", entity.getAddress() != null ? entity.getAddress() : "");
                object.put("createTime", entity.getCreationDate() == null ? "":DateUtils.formatDate(entity.getCreationDate(),"yyyy-MM-dd HH:mm:ss"));
                object.put("updateTime", entity.getLastUpdateDate() == null ? "":DateUtils.formatDate(entity.getLastUpdateDate(),"yyyy-MM-dd HH:mm:ss"));
                object.put("cityId", entity.getCityId() != null ? entity.getCityId() : "");
                object.put("cityName", entity.getCityName() != null ? entity.getCityName() : "");
                object.put("officeId", entity.getOfficeId() != null ? entity.getOfficeId() : "");
                object.put("area", entity.getArea() != null ? entity.getArea() : "");
                object.put("usearea", entity.getUseArea() != null ? entity.getUseArea() : "");
                object.put("greenarea", entity.getGreenArea() != null ? entity.getGreenArea() : "");
                object.put("floornum", entity.getFloorNum() != null ? entity.getFloorNum() : "");
                object.put("companyId", entity.getCompanyId() != null ? entity.getCompanyId() : "");
                object.put("latitude", entity.getLatitude() != null ? entity.getLatitude() : "");
                object.put("longitude", entity.getLongitude() != null ? entity.getLongitude() : "");
                object.put("phone", entity.getPhone() != null ? entity.getPhone() : "");
                object.put("officeName", entity.getOfficeName() != null ? entity.getOfficeName() : "");
                object.put("workingTime", entity.getWorkingTime() != null ? entity.getWorkingTime() : "");
                object.put("address", entity.getAddress() != null ? entity.getAddress() : "");
                object.put("twoCode", entity.getTwoCode() != null ? entity.getTwoCode() : "");
                object.put("groupPhone", entity.getPhone() != null ? entity.getPhone() : "");
                object.put("isCurrentAttention", entity.getIsCurrentAttention() != null ? entity.getIsCurrentAttention() : "0");
                object.put("isAttention", entity.getIsAttention() != null ? entity.getIsAttention() : "N");

                object.put("sourceSystem",StringUtils.isEmpty(entity.getSourceSystem())?"":entity.getSourceSystem());
                object.put("sourceSystemCommunityId",StringUtils.isEmpty(entity.getSourceSystemId())?"":entity.getSourceSystemId());
                object.put("serverUrl", StringUtils.isEmpty(entity.getServerUrl()) ? "" : entity.getServerUrl());
                object.put("residentManager", StringUtils.isEmpty(entity.getResidentManager()) ? "" : entity.getResidentManager());
	            if(Constants.NO.equals(entity.getIsRemoteAuthen())) {
		            object.put("isStanderMgt", "Y");
	            }else{
		            object.put("isStanderMgt", "N");
	            }
                communityArray.add(object);
            }
        }

        retApp.setStatus(OK);
        retApp.setMessage("查询成功");
        if (pageAppList != null&&pageAppList instanceof Page) {
            retApp.setTotall(Long.valueOf(((Page)pageAppList).getTotal()));
        }
        retApp.setData(communityArray);
        return renderString(response, retApp);
    }

    /**
     * 根据小区名称模糊查询
     *
     * @author:bubu
     * @param data
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/attentionCommunityUser/findCommunityByLikeCommunityName", produces = { "application/json" }, method = RequestMethod.POST)
    public String findCommunityByLikeCommunityName(@ModelAttribute AttentionCommunityUser data, HttpServletRequest request, HttpServletResponse response,
                                                   @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                   @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {

        IRequest requestContext = createRequestContext(request);
        RetApp retApp = new RetApp();
        String communityName = getParam(request, "communityName", "");
        String token = getParam(request, "token", "");
        JSONArray array = new JSONArray();
        try {
            if (StringUtils.isEmpty(communityName)) {
                throw new DataWarnningException("小区名称未获取!");
            }
            BaseCommunity community = new BaseCommunity();
            community.setCommunityName(communityName);
            // 登录状态
            if (!StringUtils.isEmpty(token)) {
                String userId = UserTokenUtils.getUserIdByToken(token);
                if (StringUtils.isEmpty(userId)) {
                    renderString(response, RetAppUtil.tokenDisabled());
                }
                community.setUserId(userId);
            }

            List<BaseCommunity> communityList = baseCommunityService.findList(requestContext,community,page,pageSize);
            for (BaseCommunity entity : communityList) {
                JSONObject object = new JSONObject();
                object.put("communityId", entity.getId());
                object.put("communityName", entity.getCommunityName());
                object.put("communityAddress", entity.getAddress() != null ? entity.getAddress() : "");
                object.put("isCurrentAttention", entity.getIsCurrentAttention() != null ? entity.getIsCurrentAttention() : "0");
                object.put("isAttention", entity.getIsAttention() != null ? entity.getIsAttention() : "N");
                object.put("sourceSystem",StringUtils.isEmpty(entity.getSourceSystem())?"":entity.getSourceSystem());
                object.put("sourceSystemCommunityId",StringUtils.isEmpty(entity.getSourceSystemId())?"":entity.getSourceSystemId());
                object.put("serverUrl", StringUtils.isEmpty(entity.getServerUrl()) ? "" : entity.getServerUrl());
                object.put("residentManager", StringUtils.isEmpty(entity.getResidentManager()) ? "" : entity.getResidentManager());
	            if(Constants.NO.equals(entity.getIsRemoteAuthen())) {
		            object.put("isStanderMgt", "Y");
	            }else{
		            object.put("isStanderMgt", "N");
	            }
                array.add(object);
            }

            if (communityList != null&&communityList instanceof Page) {
                retApp.setTotall(Long.valueOf(((Page)communityList).getTotal()));
            }
            retApp.setData(array);
            retApp.setStatus(OK);
            retApp.setMessage("查询成功!");
            return renderString(response, retApp);
        } catch (Exception e) {
            String errMsg = e.getMessage();
            retApp.setStatus(FAIL);
            retApp.setMessage(errMsg);
            return renderString(response, retApp);
        }
    }


    /**
     * 根据用户ID和小区ID取消关注
     *
     * @author:bubu
     * @param data
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/st/attentionCommunityUser/cancelAttention", produces = { "application/json" }, method = RequestMethod.POST)
    public String cancelAttention(@ModelAttribute AttentionCommunityUser data, HttpServletRequest request,HttpServletResponse response) {

        RetApp retApp = new RetApp();
        String communityId = getParam(request, "communityId", "");
        String token = getParam(request, "token", "");
        String userId = UserTokenUtils.getUserIdByToken(token);
        try {
            if (StringUtils.isEmpty(communityId)) {
                throw new DataWarnningException("小区ID未获取!");
            }
            if (StringUtils.isEmpty(userId)) {
                throw new DataWarnningException("token不正确!");
            }
            AttentionCommunityUser attentionCommunityUser = new AttentionCommunityUser();
	        attentionCommunityUser.setUserId(userId);
            /*
            int attentionCountByUser = attentionCommunityUserService.getAttentionCount(attentionCommunityUser);
            if (attentionCountByUser == 1) {
                retApp.setStatus(FAIL);
                retApp.setMessage("请至少保留一个关注的小区!");
                return renderString(response, retApp);
            }*/
            attentionCommunityUser.setCommunityId(communityId);
            //int count = attentionCommunityUserService.getAttentionCount(attentionCommunityUser);
            AttentionCommunityUser thisAttentionCommunityUser = attentionCommunityUserService.getEntity(attentionCommunityUser);
            if (null==thisAttentionCommunityUser) {
                retApp.setStatus(FAIL);
                retApp.setMessage("您未关注该社区,请重试!");
                return renderString(response, retApp);
            } else {
                attentionCommunityUser.setIsAttention(CommunityHelper.NO_ATTENTION);
                attentionCommunityUserService.delete(attentionCommunityUser);
                // 如果取关的是当前关注的小区,将最先关注的小区置为当前关注的小区
                if (null != thisAttentionCommunityUser.getIsAttention() && thisAttentionCommunityUser.getIsAttention().equals(CommunityHelper.IS_ATTENTION)) {
                    AttentionCommunityUser attention = attentionCommunityUserService.getNextData(attentionCommunityUser);
	                if(null!=attention) {
		                attention.setIsAttention(CommunityHelper.IS_ATTENTION);
		                attentionCommunityUserService.updateAttention(attention);
	                }
                }

                retApp.setStatus(OK);
                retApp.setMessage("取消关注成功!");
                return renderString(response, retApp);
            }

        } catch (Exception e) {
	        e.printStackTrace();;
            String errMsg = e.getMessage();
            retApp.setStatus(FAIL);
            retApp.setMessage(errMsg);
            return renderString(response, retApp);
        }
    }


    /**
     * 根据用户id(token)查询关注的小区
     *
     * @author:bubu
     * @param data
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/st/attentionCommunityUser/findList", produces = { "application/json" }, method = RequestMethod.POST)
    public String findList(@ModelAttribute AttentionCommunityUser data, HttpServletRequest request,HttpServletResponse response){

        String token = getParam(request, "token", "");
        String userId = UserTokenUtils.getUserIdByToken(token);
        RetApp retApp = new RetApp();
        JSONArray array = new JSONArray();
        try {
            if (StringUtils.isEmpty(userId)) {
                throw new DataWarnningException("用户ID不能为空!");
            }
            AttentionCommunityUser attentionCommunityUser = new AttentionCommunityUser();
            attentionCommunityUser.setUserId(userId);
            List<AttentionCommunityUser> list = attentionCommunityUserService.findList(attentionCommunityUser);

            for (AttentionCommunityUser entity : list) {
                JSONObject object = new JSONObject();
                object.put("communityId", entity.getCommunityId());
                object.put("communityName", StringUtils.isEmpty(entity.getCommunityName())?"":entity.getCommunityName());
                object.put("communityAddress", entity.getCommunityAddress() != null ? entity.getCommunityAddress() : "");
                object.put("isAttention", StringUtils.isEmpty(entity.getIsAttention())?"":entity.getIsAttention());
                object.put("sourceSystem",StringUtils.isEmpty(entity.getSourceSystem())?"":entity.getSourceSystem());
                object.put("sourceSystemCommunityId",StringUtils.isEmpty(entity.getSourceSystemId())?"":entity.getSourceSystemId());
                object.put("serverUrl", StringUtils.isEmpty(entity.getServerUrl()) ? "" : entity.getServerUrl());
                object.put("residentManager", StringUtils.isEmpty(entity.getResidentManager()) ? "" : entity.getResidentManager());
	            if(Constants.NO.equals(entity.getIsRemoteAuthen())) {
		            object.put("isStanderMgt", "Y");
	            }else{
		            object.put("isStanderMgt", "N");
	            }
                array.add(object);
            }

            if (list != null&&list instanceof Page) {
                retApp.setTotall(Long.valueOf(((Page)list).getTotal()));
            }
            retApp.setData(array);
            retApp.setStatus(OK);
            retApp.setMessage("查询成功!");
            return renderString(response, retApp);
        } catch (Exception e) {
            String errMsg = e.getMessage();
            retApp.setStatus(FAIL);
            retApp.setMessage(errMsg);
            return renderString(response, retApp);
        }
    }


    /**
     * 根据经纬度查询附近的小区
     *
     * @author:bubu
     * @param community
     * @param request
     * @param response
     * @return String
     */
    @RequestMapping(value = "/attentionCommunityUser/nearbyCommunity", produces = { "application/json" }, method = RequestMethod.POST)
    public String nearbyCommunity(@ModelAttribute BaseCommunity community, HttpServletRequest request,
                                  HttpServletResponse response) {
        RetApp retApp = new RetApp();
        String token = getParam(request, "token", "");
        try {
            BigDecimal longitude = new BigDecimal(getParam(request, "longitude", ""));//经度
            BigDecimal latitude = new BigDecimal(getParam(request, "latitude", ""));//纬度

            if (longitude.equals(BigDecimal.ZERO)  && latitude.equals(BigDecimal.ZERO)) {
                throw new DataWarnningException("经纬度不能为空!");
            }
            community.setLongitude(longitude);
            community.setLatitude(latitude);
            BaseCommunity communityEntity = baseCommunityService.findCommunityByLngLat(community);

            JSONObject object = new JSONObject();
            if (null != communityEntity && communityEntity.getDistance() <= 5) {
                object.put("communityId", communityEntity.getId());
                object.put("communityName", communityEntity.getCommunityName());
                object.put("communityAddress", communityEntity.getAddress() != null ? communityEntity.getAddress() : "");
                object.put("sourceSystem",StringUtils.isEmpty(communityEntity.getSourceSystem())?"":communityEntity.getSourceSystem());
                object.put("sourceSystemCommunityId",StringUtils.isEmpty(communityEntity.getSourceSystemId())?"":communityEntity.getSourceSystemId());
                object.put("attentionStatus", "0");
                object.put("serverUrl", StringUtils.isEmpty(communityEntity.getServerUrl()) ? "" : communityEntity.getServerUrl());
                object.put("residentManager", StringUtils.isEmpty(communityEntity.getResidentManager()) ? "" : communityEntity.getResidentManager());
	            if(Constants.NO.equals(communityEntity.getIsRemoteAuthen())) {
		            object.put("isStanderMgt", "Y");
	            }else{
		            object.put("isStanderMgt", "N");
	            }
                // 登录状态
                if (!StringUtils.isEmpty(token)) {
                    String userId = UserTokenUtils.getUserIdByToken(token);
                    if (StringUtils.isEmpty(userId)) {
                        renderString(response, RetAppUtil.tokenDisabled());
                    }
                    AttentionCommunityUser queryAttentionCommunityUser=new AttentionCommunityUser();
                    queryAttentionCommunityUser.setUserId(userId);
                    queryAttentionCommunityUser.setCommunityId(communityEntity.getId());
                    AttentionCommunityUser thisAttentionCommunityUser = attentionCommunityUserService.getEntity(queryAttentionCommunityUser);
                    if(null!=thisAttentionCommunityUser){
                        object.put("attentionStatus", "1");
                    }
                }
            }

            retApp.setStatus(OK);
            retApp.setMessage("查询成功!");
            retApp.setData(object);
            return renderString(response, retApp);
        } catch (DataWarnningException e) {
	        retApp.setStatus(FAIL);
	        retApp.setMessage(e.getMessage());
	        return renderString(response, retApp);
        }catch (Exception e) {
	        e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage("系统错误，请联系系统管理员");
            return renderString(response, retApp);
        }
    }


    /**
     * 用户关注小区
     *
     * @author:bubu
     * @param data
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/st/attentionCommunityUser/attentionCommunity", produces = { "application/json" }, method = RequestMethod.POST)
    public String attentionCommunity(@ModelAttribute AttentionCommunityUser data, HttpServletRequest request, HttpServletResponse response) {

        RetApp retApp = new RetApp();
        String token = getParam(request, "token", "");
        String userId = UserTokenUtils.getUserIdByToken(token);
        String communityId = getParam(request, "communityId", "");

        try {
            if (StringUtils.isEmpty(userId)) {
                throw new DataWarnningException("用户ID未获取!");
            }
            if (StringUtils.isEmpty(communityId)) {
                throw new DataWarnningException("小区ID未获取!");
            }

            BaseCommunity community=baseCommunityService.get(communityId);
            if(StringUtils.isEmpty(community)||StringUtils.isEmpty(community.getId())){
                throw new DataWarnningException("小区ID不正确!");
            }
            AttentionCommunityUser attentionCommunityUser = new AttentionCommunityUser();
            attentionCommunityUser.setUserId(userId);
            int attentionCountByUser = attentionCommunityUserService.getAttentionCount(attentionCommunityUser);
            if (attentionCountByUser >= 10) {
                retApp.setStatus(FAIL);
                retApp.setMessage("当前关注社区数量已达上限");
                return renderString(response, retApp);
            }

            attentionCommunityUser.setCommunityId(communityId);
            int attentionCount = attentionCommunityUserService.getAttentionCount(attentionCommunityUser);
            if (attentionCount == 0) {
                attentionCommunityUser.setDelFlag(BaseExtDTO.DEL_FLAG_NORMAL);
                attentionCommunityUserService.save(attentionCommunityUser);
                attentionCommunityUser.setIsAttention(CommunityHelper.IS_ATTENTION);
                attentionCommunityUserService.updateAttention(attentionCommunityUser);
                JSONObject object=new JSONObject();
                object.put("sourceSystem",StringUtils.isEmpty(community.getSourceSystem())?"":community.getSourceSystem());
                object.put("sourceSystemCommunityId",StringUtils.isEmpty(community.getSourceSystemId())?"":community.getSourceSystemId());
                object.put("serverUrl", StringUtils.isEmpty(community.getServerUrl()) ? "" : community.getServerUrl());
                object.put("residentManager", StringUtils.isEmpty(community.getResidentManager()) ? "" : community.getResidentManager());
	            if(Constants.NO.equals(community.getIsRemoteAuthen())) {
		            object.put("isStanderMgt", "Y");
	            }else{
		            object.put("isStanderMgt", "N");
	            }
                retApp.setData(object);
                retApp.setStatus(OK);
                retApp.setMessage("关注社区成功!");
                return renderString(response, retApp);
            } else {
                JSONObject object=new JSONObject();
                object.put("sourceSystem",StringUtils.isEmpty(community.getSourceSystem())?"":community.getSourceSystem());
                object.put("sourceSystemCommunityId",StringUtils.isEmpty(community.getSourceSystemId())?"":community.getSourceSystemId());
                object.put("serverUrl", StringUtils.isEmpty(community.getServerUrl()) ? "" : community.getServerUrl());
                object.put("residentManager", StringUtils.isEmpty(community.getResidentManager()) ? "" : community.getResidentManager());
	            if(Constants.NO.equals(community.getIsRemoteAuthen())) {
		            object.put("isStanderMgt", "Y");
	            }else{
		            object.put("isStanderMgt", "N");
	            }
                retApp.setData(object);
                retApp.setStatus(FAIL);
                retApp.setMessage("已关注该社区,请不要重复关注!");
                return renderString(response, retApp);
            }

        } catch (Exception e) {
            String errMsg = e.getMessage();
            retApp.setStatus(FAIL);
            retApp.setMessage(errMsg);
            return renderString(response, retApp);
        }
    }


    /**
     * 登录后关联用户关注的小区
     *
     * @author:bubu
     * @param community
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/st/attentionCommunityUser/attentionByLogin", produces = { "application/json" }, method = RequestMethod.POST)
    public String attentionByLogin(@ModelAttribute BaseCommunity community, HttpServletRequest request, HttpServletResponse response) {

        RetApp retApp = new RetApp();
        String token = getParam(request, "token", "");
        String userId = UserTokenUtils.getUserIdByToken(token);
        String communityId = getParam(request, "communityIds", "");
        try {
            if (StringUtils.isEmpty(userId)) {
                throw new DataWarnningException("用户ID未获取!");
            }
            if (StringUtils.isEmpty(communityId)) {
                throw new DataWarnningException("小区ID未获取!");
            }
            BaseCommunity dto = baseCommunityService.get(communityId);
            if(StringUtils.isEmpty(dto)||StringUtils.isEmpty(dto.getId())){
                throw new DataWarnningException("小区ID不正确!");
            }

            AttentionCommunityUser attentionCommunityUser = new AttentionCommunityUser();
            attentionCommunityUser.setUserId(userId);
            int attentionCountByUser = attentionCommunityUserService.getAttentionCount(attentionCommunityUser);
            if (attentionCountByUser >= 10) {
                retApp.setStatus(FAIL);
                retApp.setMessage("当前关注社区数量已达上限");
                return renderString(response, retApp);
            }
            attentionCommunityUser.setCommunityId(communityId);
            int attentionCount = attentionCommunityUserService.getAttentionCount(attentionCommunityUser);
            if (attentionCount == 0) {
                // 登录上传小区时，只有第一次关注小区才将标志位设置为“是”
                if(attentionCountByUser==0){
                    attentionCommunityUser.setIsAttention(CommunityHelper.IS_ATTENTION);
                }else {
                    attentionCommunityUser.setIsAttention(CommunityHelper.NO_ATTENTION);
                }
                attentionCommunityUser.setDelFlag(BaseExtDTO.DEL_FLAG_NORMAL);
                attentionCommunityUserService.save(attentionCommunityUser);
                retApp.setStatus(OK);
                retApp.setMessage("关联社区成功!");
            } else {
                retApp.setStatus(FAIL);
                retApp.setMessage("已关注该社区,关联失败!");
            }
            return renderString(response, retApp);
        } catch (Exception e) {
            String errMsg = e.getMessage();
            retApp.setStatus(FAIL);
            retApp.setMessage(errMsg);
            return renderString(response, retApp);
        }
    }

}

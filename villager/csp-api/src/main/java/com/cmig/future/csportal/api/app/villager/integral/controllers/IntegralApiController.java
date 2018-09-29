/**
 * .
 */
package com.cmig.future.csportal.api.app.villager.integral.controllers;

import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.sys.utils.UserTokenUtils;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.cmig.future.csportal.module.villager.integral.dto.IntegralDetail;
import com.cmig.future.csportal.module.villager.integral.service.IIntegralDetailService;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
public class IntegralApiController extends BaseExtendController {

    @Autowired
    private IIntegralDetailService integralDetailService;
    
    @Autowired
    private IAppUserService appUserService;

    /**
     * 获取积分明细
     */
    @RequestMapping(value = "/st/villager/integral/query", produces = {"application/json"}, method = RequestMethod.GET)
    public RetApp query(@RequestParam("month") String month, HttpServletRequest request) {
    	RetApp retApp = new RetApp();
    	// token获取用户id
        String token = getParam(request, "token", "");
        if(StringUtils.isEmpty(token)){
            return RetAppUtil.tokenDisabled();
        }
        String appUserId = UserTokenUtils.getUserIdByToken(token);
        // 日期格式化
        if(!StringUtils.isNumeric(month) || month.length() !=6){
        	return RetAppUtil.success("month参数错误");
        }
        String strMonth = month.substring(0, 4)+"-"+month.substring(4, 6)+"%";
        // 获取当月份的全部数据
        List<IntegralDetail> list =integralDetailService.queryDetail(appUserId,strMonth);
        Long total= list.get(0).getTotalScores();
        List<JSONObject> objList = new ArrayList<JSONObject>();
            if(!list.isEmpty()){
                for(IntegralDetail map : list){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("changeScores", map.getChangeScores());
                    jsonObject.put("changeDescribe", map.getChangeDescribe());
                    jsonObject.put("changeDate", map.getChangeDate());
                    objList.add(jsonObject);
                }
            }
        Map map = new HashMap<>();
        map.put("totalScores", total);
        map.put("detailScores", objList);
        retApp.setData(map);
        retApp.setStatus(OK);
        retApp.setTotall((long)objList.size());
        retApp.setMessage("查询成功!");
        return retApp;
    }

    /**
     * 根据分值表累积积分
     */
    @RequestMapping(value = "/st/villager/integral/add", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp insert(HttpServletRequest request) {
    	// token获取用户id
        String token = getParam(request, "token", "");
        if(StringUtils.isEmpty(token)){
            return RetAppUtil.tokenDisabled();
        }
        // 获取分值编码
        String code = getParam(request, "code", "");
        if(StringUtils.isEmpty(code)){
        	throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
        }
        AppUser appUser = appUserService.getByToken(token);
        // 分值积分服务端接口
        Map map = integralDetailService.insertDetail(code,appUser.getId());
        return RetAppUtil.success(map,"执行成功!");
    }
    
    /**
     * 积分兑换-兑换币转换成积分
     */
    @RequestMapping(value = "/st/villager/integral/convertToScores", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp convertToScores(HttpServletRequest request) {
    	// token获取用户id
        String token = getParam(request, "token", "");
        if(StringUtils.isEmpty(token)){
            return RetAppUtil.tokenDisabled();
        }
        AppUser appUser = appUserService.getByToken(token);
        // 获取分值编码
        String code = getParam(request, "code", "");
        if(StringUtils.isEmpty(code)){
        	throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
        }
        // 获取分值编码
        String coin = getParam(request, "coin", "");
        if(StringUtils.isEmpty(coin) || !StringUtils.isNumeric(coin)){
        	throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
        }
        // 兑换积分服务端接口
        Map map = integralDetailService.convertToScores(code,coin,appUser.getId());
        return RetAppUtil.success(map,"执行成功!");
    }
}
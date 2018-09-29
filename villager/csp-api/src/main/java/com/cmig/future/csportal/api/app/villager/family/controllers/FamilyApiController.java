/**
 * .
 */
package com.cmig.future.csportal.api.app.villager.family.controllers;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.sys.code.CodeUtil;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.cmig.future.csportal.module.villager.family.dto.AssetsInfo;
import com.cmig.future.csportal.module.villager.family.dto.FamilyInfo;
import com.cmig.future.csportal.module.villager.family.dto.HousingInfo;
import com.cmig.future.csportal.module.villager.family.dto.LandInfo;
import com.cmig.future.csportal.module.villager.family.service.IAssetsInfoService;
import com.cmig.future.csportal.module.villager.family.service.IFamilyInfoService;
import com.cmig.future.csportal.module.villager.family.service.IHousingInfoService;
import com.cmig.future.csportal.module.villager.family.service.ILandInfoService;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
public class FamilyApiController extends BaseExtendController {

    @Autowired
    private IFamilyInfoService iFamilyInfoService;
    
    @Autowired
    private ILandInfoService iLandInfoService;
    
    @Autowired
    private IHousingInfoService iHousingInfoService;
    
    @Autowired
    private IAssetsInfoService iAssetsInfoService;
    
    @Autowired
    private IAppUserService appUserService;

    /**
     * 获取住房信息
     */
    @RequestMapping(value = "/villager/family/queryHouse", produces = {"application/json"}, method = RequestMethod.GET)
    public RetApp queryHouse(HttpServletRequest request) {
    	RetApp retApp = new RetApp();
    	// token获取用户id
//        String token = getParam(request, "token", "");
//        if(StringUtils.isEmpty(token)){
//            return RetAppUtil.tokenDisabled();
//        }
//        AppUser appUser = appUserService.getByToken(token);
    	// 获取家庭编码
        FamilyInfo finfo = iFamilyInfoService.queryFamilyCode(Long.parseLong("15063367167"));
        // 根据家庭编码获取页面家庭信息展示
        String familyInfo =iFamilyInfoService.queryFamily(finfo.getFamilyCode());
        // 根据家庭编码获取住房信息
        HousingInfo houseInfo= iHousingInfoService.queryHouse(finfo.getFamilyCode());
        // 住房信息匹配字典，key转换为value
        JSONObject jsonObject = new JSONObject();
        String houseArea = CodeUtil.getDictLabel(houseInfo.getHomesteadArea(), Global.DICT_VILLAGER_FAMILY_HOUSEAREA, null, null);
        String homestead = CodeUtil.getDictLabel(houseInfo.getHomesteadArea(), Global.DICT_VILLAGER_FAMILY_HOMESTEAD, null, null);
        String buildprice = CodeUtil.getDictLabel(houseInfo.getBuildPrice(), Global.DICT_VILLAGER_FAMILY_BUILDPRICE, null, null);
        jsonObject.put("houseArea", houseArea);
        jsonObject.put("homestead", homestead);
        jsonObject.put("buildprice", buildprice);
        jsonObject.put("floors", houseInfo.getHouseFloors());
        jsonObject.put("buildYear", houseInfo.getBuildYear());
        // 获取页面下拉列表
        List<JSONObject> houseAreas = iFamilyInfoService.convertList(Global.DICT_VILLAGER_FAMILY_HOUSEAREA);
        List<JSONObject> homesteads = iFamilyInfoService.convertList(Global.DICT_VILLAGER_FAMILY_HOMESTEAD);
        List<JSONObject> buildprices = iFamilyInfoService.convertList(Global.DICT_VILLAGER_FAMILY_BUILDPRICE);
        // 设置返回值
        Map map = new HashMap<>();
        map.put("familyCode", finfo.getFamilyCode());
        map.put("familyInfo", familyInfo);
        map.put("houseInfo", jsonObject);
        map.put("houseAreas", houseAreas);
        map.put("homesteads", homesteads);
        map.put("buildprices", buildprices);
        retApp.setData(map);
        retApp.setStatus(OK);
        retApp.setTotall((long)1);
        retApp.setMessage("查询成功!");
        return retApp;
    }
    
    /**
     * 获取土地信息
     */
    @RequestMapping(value = "/villager/family/queryLand", produces = {"application/json"}, method = RequestMethod.GET)
    public RetApp queryLand(HttpServletRequest request) {
    	RetApp retApp = new RetApp();
    	// token获取用户id
//        String token = getParam(request, "token", "");
//        if(StringUtils.isEmpty(token)){
//            return RetAppUtil.tokenDisabled();
//        }
//        AppUser appUser = appUserService.getByToken(token);
    	// 获取家庭编码
        FamilyInfo finfo = iFamilyInfoService.queryFamilyCode(Long.parseLong("15063367167"));
        // 根据家庭编码获取页面家庭信息展示
        String familyInfo =iFamilyInfoService.queryFamily(finfo.getFamilyCode());
        // 根据家庭编码获取土地信息
        LandInfo landInfo= iLandInfoService.queryLand(finfo.getFamilyCode());
        // 土地信息匹配字典，key转换为value
        JSONObject jsonObject = new JSONObject();
        String landArea = CodeUtil.getDictLabel(landInfo.getLandArea(), Global.DICT_VILLAGER_FAMILY_LANDAREA, null, null);
        String landPrice = CodeUtil.getDictLabel(landInfo.getLandPrice(), Global.DICT_VILLAGER_FAMILY_LANDPRICE, null, null);
        String mainCrop = CodeUtil.getDictLabel(landInfo.getMainCrop(), Global.DICT_VILLAGER_FAMILY_MAINCROP, null, null);
        jsonObject.put("landArea", landArea);
        jsonObject.put("landPrice", landPrice);
        jsonObject.put("mainCrop", mainCrop);
        // 获取页面下拉列表
        List<JSONObject> landAreas = iFamilyInfoService.convertList(Global.DICT_VILLAGER_FAMILY_LANDAREA);
        List<JSONObject> landPrices = iFamilyInfoService.convertList(Global.DICT_VILLAGER_FAMILY_LANDPRICE);
        List<JSONObject> mainCrops = iFamilyInfoService.convertList(Global.DICT_VILLAGER_FAMILY_MAINCROP);
        // 设置返回值
        Map map = new HashMap<>();
        map.put("familyCode", finfo.getFamilyCode());
        map.put("familyInfo", familyInfo);
        map.put("landInfo", jsonObject);
        map.put("landAreas", landAreas);
        map.put("landPrices", landPrices);
        map.put("mainCrops", mainCrops);
        retApp.setData(map);
        retApp.setStatus(OK);
        retApp.setTotall((long)1);
        retApp.setMessage("查询成功!");
        return retApp;
    }
    
    /**
     * 获取资金信息
     */
    @RequestMapping(value = "/villager/family/queryAsset", produces = {"application/json"}, method = RequestMethod.GET)
    public RetApp queryAsset(HttpServletRequest request) {
    	RetApp retApp = new RetApp();
    	// token获取用户id
//        String token = getParam(request, "token", "");
//        if(StringUtils.isEmpty(token)){
//            return RetAppUtil.tokenDisabled();
//        }
//        AppUser appUser = appUserService.getByToken(token);
    	// 获取家庭编码
        FamilyInfo finfo = iFamilyInfoService.queryFamilyCode(Long.parseLong("15063367167"));
        // 根据家庭编码获取页面家庭信息展示
        String familyInfo =iFamilyInfoService.queryFamily(finfo.getFamilyCode());
        // 根据家庭编码获取资金信息
        AssetsInfo assetsInfo= iAssetsInfoService.queryAssets(finfo.getFamilyCode());
        // 资金信息匹配字典，key转换为value
        JSONObject jsonObject = new JSONObject();
        String depositBank = CodeUtil.getDictLabel(assetsInfo.getDepositBank(), Global.DICT_VILLAGER_FAMILY_BANK, null, null);
        String depositAmount = CodeUtil.getDictLabel(assetsInfo.getDepositAmount(), Global.DICT_VILLAGER_FAMILY_AMOUNT, null, null);
        String isLoan = CodeUtil.getDictLabel(assetsInfo.getIsLoan(), Global.DICT_VILLAGER_FAMILY_YESNO, null, null);
        jsonObject.put("depositBank", depositBank);
        jsonObject.put("depositAmount", depositAmount);
        jsonObject.put("isLoan", isLoan);
        // 获取页面下拉列表
        List<JSONObject> depositBanks = iFamilyInfoService.convertList(Global.DICT_VILLAGER_FAMILY_BANK);
        List<JSONObject> depositAmounts = iFamilyInfoService.convertList(Global.DICT_VILLAGER_FAMILY_AMOUNT);
        List<JSONObject> isLoans = iFamilyInfoService.convertList(Global.DICT_VILLAGER_FAMILY_YESNO);
        // 设置返回值
        Map map = new HashMap<>();
        map.put("familyCode", finfo.getFamilyCode());
        map.put("familyInfo", familyInfo);
        map.put("assetsInfo", jsonObject);
        map.put("depositBanks", depositBanks);
        map.put("depositAmounts", depositAmounts);
        map.put("isLoans", isLoans);
        retApp.setData(map);
        retApp.setStatus(OK);
        retApp.setTotall((long)1);
        retApp.setMessage("查询成功!");
        return retApp;
    }
    
    /**
     * 保存住房信息
     */
    @RequestMapping(value = "/villager/family/addHouse", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp addHouse(HttpServletRequest request) {
    	RetApp retApp = new RetApp();
    	// token获取用户id
//        String token = getParam(request, "token", "");
//        if(StringUtils.isEmpty(token)){
//            return RetAppUtil.tokenDisabled();
//        }
//        AppUser appUser = appUserService.getByToken(token);
    	String familyCode = getParam(request, "familyCode", "");
    	Long buildYear = Long.parseLong(getParam(request, "buildYear", ""));
    	String buildprice = getParam(request, "buildprice", "");
    	Long floors = Long.parseLong(getParam(request, "floors", ""));
    	String homestead = getParam(request, "homestead", "");
    	String houseArea = getParam(request, "houseArea", "");
    	if(StringUtils.isEmpty(familyCode)) {
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
    	if(StringUtils.isEmpty(buildYear)) {
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
    	if(StringUtils.isEmpty(buildprice)) {
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
    	if(StringUtils.isEmpty(floors)) {
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
    	if(StringUtils.isEmpty(homestead)) {
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
    	if(StringUtils.isEmpty(houseArea)) {
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
        // 根据家庭编码更新住房信息
        iHousingInfoService.updateByFamilyCode(familyCode,buildYear,buildprice,floors,homestead,houseArea);
        // 设置返回值
        retApp.setData("");
        retApp.setStatus(OK);
        retApp.setTotall((long)1);
        retApp.setMessage("执行成功!");
        return retApp;
    }
    
    /**
     * 保存土地信息
     */
    @RequestMapping(value = "/villager/family/addLand", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp addLand(HttpServletRequest request) {
    	RetApp retApp = new RetApp();
    	// token获取用户id
//        String token = getParam(request, "token", "");
//        if(StringUtils.isEmpty(token)){
//            return RetAppUtil.tokenDisabled();
//        }
//        AppUser appUser = appUserService.getByToken(token);
    	String familyCode = getParam(request, "familyCode", "");
    	String landArea = getParam(request, "landArea", "");
    	String landPrice = getParam(request, "landPrice", "");
    	String mainCrop = getParam(request, "mainCrop", "");
    	if(StringUtils.isEmpty(familyCode)) {
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
    	if(StringUtils.isEmpty(landArea)) {
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
    	if(StringUtils.isEmpty(landPrice)) {
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
    	if(StringUtils.isEmpty(mainCrop)) {
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
        // 根据家庭编码更新住房信息
    	iLandInfoService.updateByFamilyCode(familyCode,landArea,landPrice,mainCrop);
        // 设置返回值
        retApp.setData("");
        retApp.setStatus(OK);
        retApp.setTotall((long)1);
        retApp.setMessage("执行成功!");
        return retApp;
    }
    
    /**
     * 保存资金信息
     */
    @RequestMapping(value = "/villager/family/addAsset", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp addAsset(HttpServletRequest request) {
    	RetApp retApp = new RetApp();
    	// token获取用户id
//        String token = getParam(request, "token", "");
//        if(StringUtils.isEmpty(token)){
//            return RetAppUtil.tokenDisabled();
//        }
//        AppUser appUser = appUserService.getByToken(token);
    	String familyCode = getParam(request, "familyCode", "");
    	String depositAmount = getParam(request, "depositAmount", "");
    	String depositBank = getParam(request, "depositBank", "");
    	String isLoan = getParam(request, "isLoan", "");
    	if(StringUtils.isEmpty(familyCode)) {
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
    	if(StringUtils.isEmpty(depositAmount)) {
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
    	if(StringUtils.isEmpty(depositBank)) {
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
    	if(StringUtils.isEmpty(isLoan)) {
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
        // 根据家庭编码更新住房信息
    	iAssetsInfoService.updateByFamilyCode(familyCode,depositAmount,depositBank,isLoan);
        // 设置返回值
        retApp.setData("");
        retApp.setStatus(OK);
        retApp.setTotall((long)1);
        retApp.setMessage("执行成功!");
        return retApp;
    }
}
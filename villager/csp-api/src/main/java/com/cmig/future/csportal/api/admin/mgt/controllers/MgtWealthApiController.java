/**
 * .
 */
package com.cmig.future.csportal.api.admin.mgt.controllers;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.exceptions.ServiceExceptionHelper;
import com.cmig.future.csportal.module.sys.code.CodeUtil;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.cmig.future.csportal.module.villager.family.dto.AssetsInfo;
import com.cmig.future.csportal.module.villager.family.dto.FamilyInfo;
import com.cmig.future.csportal.module.villager.family.dto.HousingInfo;
import com.cmig.future.csportal.module.villager.family.dto.LandInfo;
import com.cmig.future.csportal.module.villager.family.service.IAssetsInfoService;
import com.cmig.future.csportal.module.villager.family.service.IFamilyInfoService;
import com.cmig.future.csportal.module.villager.family.service.IHousingInfoService;
import com.cmig.future.csportal.module.villager.family.service.ILandInfoService;
import com.cmig.future.csportal.module.villager.wealth.dto.AnimalCost;
import com.cmig.future.csportal.module.villager.wealth.dto.PlantCost;
import com.cmig.future.csportal.module.villager.wealth.dto.WealthPlan;
import com.cmig.future.csportal.module.villager.wealth.service.IAnimalCostService;
import com.cmig.future.csportal.module.villager.wealth.service.IPlantCostService;
import com.cmig.future.csportal.module.villager.wealth.service.IWealthPlanService;
import com.cmig.future.csportal.module.villager.wealth.service.IWealthService;
import com.hand.hap.core.IRequest;
import jodd.util.StringUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * app用户Controller
 *
 * @author su
 * @version 2018
 */
@RestController
@RequestMapping(value = "${managementPath}/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MgtWealthApiController extends BaseExtendController {

    @Autowired
    private IWealthPlanService iWealthPlanService;
    
    @Autowired
    private IAppUserService appUserService;
    
    @Autowired
    private IFamilyInfoService iFamilyInfoService;
    
    @Autowired
    private ILandInfoService iLandInfoService;
    
    @Autowired
    private IHousingInfoService iHousingInfoService;
    
    @Autowired
    private IAssetsInfoService iAssetsInfoService;
    
    @Autowired
    private IPlantCostService iPlantCostService;
    
    @Autowired
    private IWealthService iWealthService;
    
    @Autowired
    private IAnimalCostService iAnimalCostService;
    
    /**
     * 获取种植财富列表
     */
    @RequestMapping(value = "/villager/wealth/queryWealthPlantList", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp queryWealthPlantList(HttpServletRequest request,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(defaultValue = "") String keywords) {
    	RetApp retApp = new RetApp();
    	
    	// 获取种植计划
        List<WealthPlan> plantList = iWealthPlanService.queryPlantList(Global.FLAG_PLANT,page,pageSize,keywords);
        // 生成返回结构
        List<JSONObject> objList = new ArrayList<JSONObject>();
        if(plantList.size() != 0 || plantList != null){
            for(WealthPlan map : plantList){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", map.getId());
                jsonObject.put("name", map.getUserName());
                jsonObject.put("date", DateUtils.formatDate(map.getCommitDate(), "yyyy-MM-dd"));
                jsonObject.put("classify", map.getTypeName());
                jsonObject.put("item", map.getItemName());
                // 头像
                AppUser appUser = appUserService.get(map.getUserId());
                String icon = appUser.getUserIcon();
                if(StringUtil.isEmpty(icon)) {
                	icon = Global.getDefaultImagePath(request);
                	jsonObject.put("image", icon);
                }else {
                	jsonObject.put("image", Global.getFullImagePath(icon));
                }
                objList.add(jsonObject);
            }
        }
        
        // 设置返回值
        Map map = new HashMap<>();
        map.put("plantList", objList);
        retApp.setData(map);
        retApp.setStatus(OK);
        retApp.setTotall((long)objList.size());
        retApp.setMessage("查询成功!");
        return retApp;
    }
    
    /**
     * 获取养殖财富列表
     */
    @RequestMapping(value = "/villager/wealth/queryWealthAnimalList", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp queryWealthAnimalList(HttpServletRequest request,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(defaultValue = "") String keywords) {
    	RetApp retApp = new RetApp();
    	
    	// 获取种植计划
        List<WealthPlan> plantList = iWealthPlanService.queryPlantList(Global.FLAG_ANIMAL,page,pageSize,keywords);
        // 生成返回结构
        List<JSONObject> objList = new ArrayList<JSONObject>();
        if(plantList.size() != 0 || plantList != null){
            for(WealthPlan map : plantList){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", map.getId());
                jsonObject.put("name", map.getUserName());
                jsonObject.put("date", DateUtils.formatDate(map.getCommitDate(), "yyyy-MM-dd"));
                jsonObject.put("classify", map.getTypeName());
                jsonObject.put("item", map.getItemName());
                // 头像
                AppUser appUser = appUserService.get(map.getUserId());
                String icon = appUser.getUserIcon();
                if(StringUtil.isEmpty(icon)) {
                	icon = Global.getDefaultImagePath(request);
                	jsonObject.put("image", icon);
                }else {
                	jsonObject.put("image", Global.getFullImagePath(icon));
                }
                objList.add(jsonObject);
            }
        }
        
        // 设置返回值
        Map map = new HashMap<>();
        map.put("animalList", objList);
        retApp.setData(map);
        retApp.setStatus(OK);
        retApp.setTotall((long)objList.size());
        retApp.setMessage("查询成功!");
        return retApp;
    }
    
    /**
     * 获取创业计划列表
     */
    @RequestMapping(value = "/villager/wealth/queryWealthWorkList", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp queryWealthWorkList(HttpServletRequest request,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(defaultValue = "") String keywords) {
    	RetApp retApp = new RetApp();
    	
    	// 获取种植计划
        List<WealthPlan> plantList = iWealthPlanService.queryPlantList(Global.FLAG_WORK,page,pageSize,keywords);
        // 生成返回结构
        List<JSONObject> objList = new ArrayList<JSONObject>();
        if(plantList.size() != 0 || plantList != null){
            for(WealthPlan map : plantList){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", map.getId());
                jsonObject.put("name", map.getUserName());
                jsonObject.put("date", DateUtils.formatDate(map.getCommitDate(), "yyyy-MM-dd"));
                jsonObject.put("classify", map.getTypeName());
                // 头像
                AppUser appUser = appUserService.get(map.getUserId());
                String icon = appUser.getUserIcon();
                if(StringUtil.isEmpty(icon)) {
                	icon = Global.getDefaultImagePath(request);
                	jsonObject.put("image", icon);
                }else {
                	jsonObject.put("image", Global.getFullImagePath(icon));
                }
                objList.add(jsonObject);
            }
        }
        
        // 设置返回值
        Map map = new HashMap<>();
        map.put("workList", objList);
        retApp.setData(map);
        retApp.setStatus(OK);
        retApp.setTotall((long)objList.size());
        retApp.setMessage("查询成功!");
        return retApp;
    }
    
    /**
     * 获取财富计划明细--种植/养殖
     */
    @RequestMapping(value = "/villager/wealth/queryWPlanDetail", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp queryWPlanDetail(HttpServletRequest request) {
    	RetApp retApp = new RetApp();
    	
    	// 明细id
    	String id = getParam(request, "id", "");
        if(StringUtils.isEmpty(id) || !StringUtils.isNumeric(id)){
        	throw ServiceExceptionHelper.argsNull();
        }
    	// 获取种植/养殖计划明细
        IRequest requestContext = createRequestContext(request);
        WealthPlan plan = new WealthPlan();
        plan.setId(Long.parseLong(id));
        WealthPlan detail = iWealthPlanService.selectByPrimaryKey(requestContext, plan);
        
        // 编辑返回结构
        // 种植/养殖计划
        Map mplan = new HashMap<>();
        mplan.put("classify", detail.getTypeName());
        mplan.put("item", detail.getItemName());
        mplan.put("muarea", detail.getMuArea());
        // 财富预估
        Map cplan = new HashMap<>();
        cplan.put("totalincome", detail.getTotalIncome());
        cplan.put("cost", detail.getTotalCost());
        cplan.put("netincome", detail.getNetIncome());
        
        // 预计年投入
        Map cost = new HashMap<>();
        if(Global.FLAG_PLANT.equals(detail.getFlag())) {
        	PlantCost plantCost = new PlantCost();
        	plantCost.setTypeCode(detail.getTypeCode());
        	plantCost.setMuCode(detail.getMuCode());
            List<PlantCost> costList = iPlantCostService.queryPlantCost(plantCost);
            PlantCost costi = costList.get(0);
            cost.put("fertilizerCost", costi.getFertilizerCost());
            cost.put("pesticideCost", costi.getPesticideCost());
            cost.put("seedlingsCost", costi.getSeedlingsCost());
            cost.put("bakeCost", costi.getBakeCost());
            cost.put("machineCost", costi.getMachineCost());
            cost.put("landCost", costi.getLandCost());
        }else if(Global.FLAG_ANIMAL.equals(detail.getFlag())) {
        	AnimalCost animalCost = new AnimalCost();
        	animalCost.setTypeCode(detail.getTypeCode());
        	animalCost.setMuCode(detail.getMuCode());
        	List<AnimalCost> costList = iAnimalCostService.queryAnimalCost(animalCost);
        	AnimalCost costi = costList.get(0);
            cost.put("buildingCost", costi.getBuildingCost());
            cost.put("cubCost", costi.getCubCost());
            cost.put("feedCost", costi.getFeedCost());
            cost.put("landCost", costi.getLandCost());
            cost.put("drugsCost", costi.getDrugsCost());
            cost.put("cleanCost", costi.getCleanCost());
        }
        
        // 家庭信息
        String uid = detail.getUserId();
        AppUser appUser = appUserService.get(uid);
        // 获取家庭编码
        FamilyInfo finfo = iFamilyInfoService.queryFamilyCode(Long.parseLong(appUser.getMobile()));
        // 根据家庭编码获取页面家庭信息展示
        String familyInfo =null;
        if(StringUtils.isEmpty(finfo)) {
        	if(StringUtils.isEmpty(appUser.getUserName())) {
        		familyInfo = "用户未填写用户名，";
            }else {
            	familyInfo = appUser.getUserName() + "(户主)，"+"共1口";
            }
        }else {
        	// 根据家庭编码获取页面家庭信息展示
            familyInfo =iFamilyInfoService.queryFamily(finfo.getFamilyCode());
        }
        
        // 财产信息
        Map property = new HashMap<>();
        // 根据家庭编码获取住房信息
        HousingInfo houseInfo= iHousingInfoService.queryHouse(Long.parseLong(appUser.getMobile()));
        // 住房信息匹配字典，key转换为value
        String houseArea = CodeUtil.getDictLabel(houseInfo.getHomesteadArea(), Global.DICT_VILLAGER_FAMILY_HOUSEAREA, null, null);
        String homestead = CodeUtil.getDictLabel(houseInfo.getHomesteadArea(), Global.DICT_VILLAGER_FAMILY_HOMESTEAD, null, null);
        String buildprice = CodeUtil.getDictLabel(houseInfo.getBuildPrice(), Global.DICT_VILLAGER_FAMILY_BUILDPRICE, null, null);
        property.put("houseArea", houseArea);
        property.put("buildYear", houseInfo.getBuildYear());
        
        // 根据家庭编码获取土地信息
        LandInfo landInfo= iLandInfoService.queryLand(Long.parseLong(appUser.getMobile()));
        // 土地信息匹配字典，key转换为value
        String landArea = CodeUtil.getDictLabel(landInfo.getLandArea(), Global.DICT_VILLAGER_FAMILY_LANDAREA, null, null);
        String landPrice = CodeUtil.getDictLabel(landInfo.getLandPrice(), Global.DICT_VILLAGER_FAMILY_LANDPRICE, null, null);
        String mainCrop = CodeUtil.getDictLabel(landInfo.getMainCrop(), Global.DICT_VILLAGER_FAMILY_MAINCROP, null, null);
        property.put("landArea", landArea);
        property.put("mainCrop", mainCrop);
        
        // 根据家庭编码获取资金信息
        AssetsInfo assetsInfo= iAssetsInfoService.queryAssets(Long.parseLong(appUser.getMobile()));
        // 资金信息匹配字典，key转换为value
        String depositBank = CodeUtil.getDictLabel(assetsInfo.getDepositBank(), Global.DICT_VILLAGER_FAMILY_BANK, null, null);
        String depositAmount = CodeUtil.getDictLabel(assetsInfo.getDepositAmount(), Global.DICT_VILLAGER_FAMILY_AMOUNT, null, null);
        String isLoan = CodeUtil.getDictLabel(assetsInfo.getIsLoan(), Global.DICT_VILLAGER_FAMILY_YESNO, null, null);
        property.put("depositBank", depositBank);
        property.put("depositAmount", depositAmount);
        property.put("isLoan", isLoan);
        
        // echarts图
        Map mapcharts = iWealthService.calculate(Long.parseLong(detail.getTotalIncome()),Long.parseLong(detail.getTotalCost()));
        // 头像
        String icon = appUser.getUserIcon();
        
        // 设置返回值
        Map map = new HashMap<>();
        map.put("mplan", mplan);//种植/养殖计划
        map.put("cplan", cplan);// 财富预估
        map.put("familyInfo", familyInfo);// 家庭信息
        map.put("cost", cost);// 预计年投入
        map.put("property", property);// 财产信息
        map.put("mobile", appUser.getMobile());//电话
        map.put("mapcharts", mapcharts);//5年计划图
        if(StringUtil.isEmpty(icon)) {
        	icon = Global.getDefaultImagePath(request);
        	map.put("image", icon);
        }else {
        	map.put("image", Global.getFullImagePath(icon));
        }
        retApp.setData(map);
        retApp.setStatus(OK);
        retApp.setTotall((long)1);
        retApp.setMessage("查询成功!");
        return retApp;
    }
    
    /**
     * 获取财富计划明细--创业
     */
    @RequestMapping(value = "/villager/wealth/queryWorkWPlanDetail", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp queryWorkWPlanDetail(HttpServletRequest request) {
    	RetApp retApp = new RetApp();
    	
    	// 明细id
    	String id = getParam(request, "id", "");
        if(StringUtils.isEmpty(id) || !StringUtils.isNumeric(id)){
        	throw ServiceExceptionHelper.argsNull();
        }
    	// 获取种植/养殖计划明细
        IRequest requestContext = createRequestContext(request);
        WealthPlan plan = new WealthPlan();
        plan.setId(Long.parseLong(id));
        WealthPlan detail = iWealthPlanService.selectByPrimaryKey(requestContext, plan);
        
        // 编辑返回结构
        // 创业计划
        Map mplan = new HashMap<>();
        mplan.put("classify", detail.getTypeName());
        
        // 家庭信息
        String uid = detail.getUserId();
        AppUser appUser = appUserService.get(uid);
        // 获取家庭编码
        FamilyInfo finfo = iFamilyInfoService.queryFamilyCode(Long.parseLong(appUser.getMobile()));
        // 根据家庭编码获取页面家庭信息展示
        String familyInfo =null;
        if(StringUtils.isEmpty(finfo)) {
        	if(StringUtils.isEmpty(appUser.getUserName())) {
        		familyInfo = "用户未填写用户名，";
            }else {
            	familyInfo = appUser.getUserName() + "(户主)，"+"共1口";
            }
        }else {
        	// 根据家庭编码获取页面家庭信息展示
            familyInfo =iFamilyInfoService.queryFamily(finfo.getFamilyCode());
        }
        
        // 财产信息
        Map property = new HashMap<>();
        // 根据家庭编码获取住房信息
        HousingInfo houseInfo= iHousingInfoService.queryHouse(Long.parseLong(appUser.getMobile()));
        // 住房信息匹配字典，key转换为value
        String houseArea = CodeUtil.getDictLabel(houseInfo.getHomesteadArea(), Global.DICT_VILLAGER_FAMILY_HOUSEAREA, null, null);
        String homestead = CodeUtil.getDictLabel(houseInfo.getHomesteadArea(), Global.DICT_VILLAGER_FAMILY_HOMESTEAD, null, null);
        String buildprice = CodeUtil.getDictLabel(houseInfo.getBuildPrice(), Global.DICT_VILLAGER_FAMILY_BUILDPRICE, null, null);
        property.put("houseArea", houseArea);
        property.put("buildYear", houseInfo.getBuildYear());
        
        // 根据家庭编码获取土地信息
        LandInfo landInfo= iLandInfoService.queryLand(Long.parseLong(appUser.getMobile()));
        // 土地信息匹配字典，key转换为value
        String landArea = CodeUtil.getDictLabel(landInfo.getLandArea(), Global.DICT_VILLAGER_FAMILY_LANDAREA, null, null);
        String landPrice = CodeUtil.getDictLabel(landInfo.getLandPrice(), Global.DICT_VILLAGER_FAMILY_LANDPRICE, null, null);
        String mainCrop = CodeUtil.getDictLabel(landInfo.getMainCrop(), Global.DICT_VILLAGER_FAMILY_MAINCROP, null, null);
        property.put("landArea", landArea);
        property.put("mainCrop", mainCrop);
        
        // 根据家庭编码获取资金信息
        AssetsInfo assetsInfo= iAssetsInfoService.queryAssets(Long.parseLong(appUser.getMobile()));
        // 资金信息匹配字典，key转换为value
        String depositBank = CodeUtil.getDictLabel(assetsInfo.getDepositBank(), Global.DICT_VILLAGER_FAMILY_BANK, null, null);
        String depositAmount = CodeUtil.getDictLabel(assetsInfo.getDepositAmount(), Global.DICT_VILLAGER_FAMILY_AMOUNT, null, null);
        String isLoan = CodeUtil.getDictLabel(assetsInfo.getIsLoan(), Global.DICT_VILLAGER_FAMILY_YESNO, null, null);
        property.put("depositBank", depositBank);
        property.put("depositAmount", depositAmount);
        property.put("isLoan", isLoan);
        
        // 头像
        String icon = appUser.getUserIcon();
        
        // 设置返回值
        Map map = new HashMap<>();
        map.put("mplan", mplan);//创业计划
        map.put("familyInfo", familyInfo);// 家庭信息
        map.put("property", property);// 财产信息
        map.put("mobile", appUser.getMobile());//电话
        if(StringUtil.isEmpty(icon)) {
        	icon = Global.getDefaultImagePath(request);
        	map.put("image", icon);
        }else {
        	map.put("image", Global.getFullImagePath(icon));
        }
        retApp.setData(map);
        retApp.setStatus(OK);
        retApp.setTotall((long)1);
        retApp.setMessage("查询成功!");
        return retApp;
    }

}
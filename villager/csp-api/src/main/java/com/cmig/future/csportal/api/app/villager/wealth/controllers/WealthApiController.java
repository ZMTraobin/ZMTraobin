/**
 * .
 */
package com.cmig.future.csportal.api.app.villager.wealth.controllers;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.utils.DateUtils;
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
import com.cmig.future.csportal.module.villager.wealth.dto.AnimalCost;
import com.cmig.future.csportal.module.villager.wealth.dto.AnimalType;
import com.cmig.future.csportal.module.villager.wealth.dto.PlantCost;
import com.cmig.future.csportal.module.villager.wealth.dto.PlantType;
import com.cmig.future.csportal.module.villager.wealth.service.IAnimalCostService;
import com.cmig.future.csportal.module.villager.wealth.service.IAnimalTypeService;
import com.cmig.future.csportal.module.villager.wealth.service.IPlantCostService;
import com.cmig.future.csportal.module.villager.wealth.service.IPlantTypeService;
import com.cmig.future.csportal.module.villager.wealth.service.IWealthService;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
public class WealthApiController extends BaseExtendController {

    @Autowired
    private IPlantTypeService iPlantTypeService;
    
    @Autowired
    private IPlantCostService iPlantCostService;
    
    @Autowired
    private IAnimalTypeService iAnimalTypeService;
    
    @Autowired
    private IAnimalCostService iAnimalCostService;
    
    @Autowired
    private IWealthService iWealthService;
    
    @Autowired
    private IAppUserService appUserService;

    /**
     * 获取种植财富信息
     */
    @RequestMapping(value = "/villager/wealth/queryWealthPlant", produces = {"application/json"}, method = RequestMethod.GET)
    public RetApp queryWealthPlant(HttpServletRequest request) {
    	RetApp retApp = new RetApp();
    	// token获取用户id
//        String token = getParam(request, "token", "");
//        if(StringUtils.isEmpty(token)){
//            return RetAppUtil.tokenDisabled();
//        }
//        AppUser appUser = appUserService.getByToken(token);
    	// 获取种植信息
        List<PlantType> plantList = iPlantTypeService.queryPlantType();
    	// 获取参数植物编码
    	String typeCode = getParam(request, "typeCode", "");
    	if(StringUtils.isEmpty(typeCode)) {
    		typeCode = plantList.get(0).getTypeCode();
    	}
        // 获取种植成本信息
    	PlantCost plantCost = new PlantCost();
    	plantCost.setTypeCode(typeCode);
        List<PlantCost> costList = iPlantCostService.queryPlantCost(plantCost);
        // 设置返回值
        Map map = new HashMap<>();
        map.put("plantList", plantList);
        map.put("costList", costList);
        retApp.setData(map);
        retApp.setStatus(OK);
        retApp.setTotall((long)1);
        retApp.setMessage("查询成功!");
        return retApp;
    }
    
    /**
     * 获取养殖财富信息
     */
    @RequestMapping(value = "/villager/wealth/queryWealthAnimal", produces = {"application/json"}, method = RequestMethod.GET)
    public RetApp queryWealthAnimal(HttpServletRequest request) {
    	RetApp retApp = new RetApp();
    	// token获取用户id
//        String token = getParam(request, "token", "");
//        if(StringUtils.isEmpty(token)){
//            return RetAppUtil.tokenDisabled();
//        }
//        AppUser appUser = appUserService.getByToken(token);
    	// 获取种植信息
        List<AnimalType> animalList = iAnimalTypeService.queryAnimalType();
    	// 获取参数植物编码
    	String typeCode = getParam(request, "typeCode", "");
    	if(StringUtils.isEmpty(typeCode)) {
    		typeCode = animalList.get(0).getTypeCode();
    	}
        // 获取种植成本信息
    	AnimalCost animalCost = new AnimalCost();
    	animalCost.setTypeCode(typeCode);
        List<AnimalCost> costList = iAnimalCostService.queryAnimalCost(animalCost);
        // 设置返回值
        Map map = new HashMap<>();
        map.put("animalList", animalList);
        map.put("costList", costList);
        retApp.setData(map);
        retApp.setStatus(OK);
        retApp.setTotall((long)1);
        retApp.setMessage("查询成功!");
        return retApp;
    }
    
    /**
     * 获取种植财富计划
     */
    @RequestMapping(value = "/villager/wealth/queryWealthPlantPlan", produces = {"application/json"}, method = RequestMethod.GET)
    public RetApp queryWealthPlantPlan(HttpServletRequest request) {
    	RetApp retApp = new RetApp();
    	// token获取用户id
//        String token = getParam(request, "token", "");
//        if(StringUtils.isEmpty(token)){
//            return RetAppUtil.tokenDisabled();
//        }
//        AppUser appUser = appUserService.getByToken(token);
    	
    	// 获取参数植物编码
    	String typeCode = getParam(request, "typeCode", "");
    	// 获取参数种植成本范围编码
    	String muCode = getParam(request, "muCode", "");
    	
    	if(StringUtils.isEmpty(typeCode)) {
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
    	if(StringUtils.isEmpty(muCode)) {
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
    	
        // 获取种植成本信息
    	PlantCost plantCost = new PlantCost();
    	plantCost.setTypeCode(typeCode);
    	plantCost.setMuCode(muCode);
        List<PlantCost> costList = iPlantCostService.queryPlantCost(plantCost);
        // 计算总投入
        PlantCost cost = costList.get(0);
        Long input = cost.getFertilizerCost() + cost.getPesticideCost()+cost.getSeedlingsCost()+cost.getBakeCost()+cost.getMachineCost()+cost.getLandCost();
        Map map = iWealthService.calculate(costList.get(0).getTotalIncome(),input);
        
        // 设置返回值
        retApp.setData(map);
        retApp.setStatus(OK);
        retApp.setTotall((long)1);
        retApp.setMessage("查询成功!");
        return retApp;
    }
    
    /**
     * 获取养殖财富计划
     */
    @RequestMapping(value = "/villager/wealth/queryWealthAnimalPlan", produces = {"application/json"}, method = RequestMethod.GET)
    public RetApp queryWealthAnimalPlan(HttpServletRequest request) {
    	RetApp retApp = new RetApp();
    	// token获取用户id
//        String token = getParam(request, "token", "");
//        if(StringUtils.isEmpty(token)){
//            return RetAppUtil.tokenDisabled();
//        }
//        AppUser appUser = appUserService.getByToken(token);
    	
    	// 获取参数植物编码
    	String typeCode = getParam(request, "typeCode", "");
    	// 获取参数种植成本范围编码
    	String muCode = getParam(request, "muCode", "");
    	
    	if(StringUtils.isEmpty(typeCode)) {
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
    	if(StringUtils.isEmpty(muCode)) {
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
    	
        // 获取种植成本信息
    	AnimalCost animalCost = new AnimalCost();
    	animalCost.setTypeCode(typeCode);
    	animalCost.setMuCode(muCode);
    	List<AnimalCost> costList = iAnimalCostService.queryAnimalCost(animalCost);
        // 计算总投入
    	AnimalCost cost = costList.get(0);
        Long input = cost.getBuildingCost() + cost.getCubCost()+cost.getFeedCost()+cost.getLandCost()+cost.getDrugsCost()+cost.getCleanCost();
        Map map = iWealthService.calculate(costList.get(0).getTotalIncome(),input);
        
        // 设置返回值
        retApp.setData(map);
        retApp.setStatus(OK);
        retApp.setTotall((long)1);
        retApp.setMessage("查询成功!");
        return retApp;
    }
    
    /**
     * 获取种植财富toB推送
     */
    @RequestMapping(value = "/villager/wealth/addWealthPlantPlan", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp addWealthPlantPlan(HttpServletRequest request) {
    	RetApp retApp = new RetApp();
    	// token获取用户id
//        String token = getParam(request, "token", "");
//        if(StringUtils.isEmpty(token)){
//            return RetAppUtil.tokenDisabled();
//        }
//        AppUser appUser = appUserService.getByToken(token);
    	
    	// 获取参数植物编码
    	String typeCode = getParam(request, "typeCode", "");
    	// 获取参数种植成本范围编码
    	String muCode = getParam(request, "muCode", "");
    	
    	if(StringUtils.isEmpty(typeCode)) {
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
    	if(StringUtils.isEmpty(muCode)) {
    		throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    	}
    	
        // 获取种植成本信息
    	PlantCost plantCost = new PlantCost();
    	plantCost.setTypeCode(typeCode);
    	plantCost.setMuCode(muCode);
        List<PlantCost> costList = iPlantCostService.queryPlantCost(plantCost);
        // 计算总投入
        PlantCost cost = costList.get(0);
        Long input = cost.getFertilizerCost() + cost.getPesticideCost()+cost.getSeedlingsCost()+cost.getBakeCost()+cost.getMachineCost()+cost.getLandCost();
        Map map = iWealthService.calculate(costList.get(0).getTotalIncome(),input);
        
        // 设置返回值
        retApp.setData(map);
        retApp.setStatus(OK);
        retApp.setTotall((long)1);
        retApp.setMessage("查询成功!");
        return retApp;
    }

}
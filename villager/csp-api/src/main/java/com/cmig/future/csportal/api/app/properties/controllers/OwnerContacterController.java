package com.cmig.future.csportal.api.app.properties.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.SmsUtil;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.properties.base.contacter.dto.BpOwnerContacter;
import com.cmig.future.csportal.module.properties.base.contacter.service.IBpOwnerContacterService;
import com.cmig.future.csportal.module.properties.base.customer.dto.BpHouseMap;
import com.cmig.future.csportal.module.properties.base.customer.service.IBpHouseMapService;
import com.cmig.future.csportal.module.properties.base.view.dto.MgtViewStructure;
import com.cmig.future.csportal.module.sys.code.CodeUtil;
import com.cmig.future.csportal.module.sys.utils.UserTokenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.hand.hap.cache.Cache;
import com.hand.hap.cache.CacheManager;
import com.hand.hap.cache.impl.SysCodeCache;
import com.hand.hap.core.BaseConstants;
import com.hand.hap.system.dto.Code;
import com.hand.hap.system.dto.CodeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping(value = "${userPath}")
public class OwnerContacterController extends BaseExtendController {

    @Autowired
    private IBpOwnerContacterService bpOwnerContacterService;
    @Autowired
    private IBpHouseMapService bpHouseMapService;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private ObjectMapper objectMapper;

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/st/contacter/list",produces = {"application/json" }, method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public RetApp contacterList(@RequestParam(required=true) String token, @RequestParam(required=false) String communityId,HttpServletRequest request,@RequestParam(defaultValue = DEFAULT_PAGE) Integer page,
                                @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) {
        String userId=UserTokenUtils.getUserIdByToken(token);
        List<BpOwnerContacter> list = bpOwnerContacterService.getContacterList(userId,communityId,page,pageSize);
        JSONArray jsonArray=new JSONArray();
        for(BpOwnerContacter contacter:list){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("ownerContacterId", contacter.getOwnerContacterId());
            jsonObject.put("contacterName", StringUtils.isEmpty(contacter.getContacterName())?"": contacter.getContacterName());
            String typeDesc = CodeUtil.getDictLabel(contacter.getType(),"OWNER.CONTACTER_TYPE","","");
            jsonObject.put("typeDesc", typeDesc);
            jsonObject.put("type", StringUtils.isEmpty(contacter.getType())?"": contacter.getType());
            jsonObject.put("mobile", StringUtils.isEmpty(contacter.getMobile())?"": contacter.getMobile());
            jsonObject.put("idNo", StringUtils.isEmpty(contacter.getIdNo())?"": contacter.getIdNo());
            List<BpHouseMap> ownerHouse = contacter.getOwnerHouse();
            System.out.println("ownerHouse size:"+ownerHouse.size());
            JSONArray houseArray=new JSONArray();
            if(!ownerHouse.isEmpty()){
                for (BpHouseMap map : ownerHouse){
                    JSONObject houseObject=new JSONObject();
                    houseObject.put("houseId",map.getBuildingId());
                    houseObject.put("houseName",map.getHouseFullName());
                    houseObject.put("isBind",map.getIsBind());
                    houseArray.add(houseObject);
                }
            }
            jsonObject.put("ownerHouse", houseArray);
            jsonArray.add(jsonObject);
        }
        RetApp retApp=new RetApp();
        if (list != null&&list instanceof Page) {
            retApp.setTotall(Long.valueOf(((Page)list).getTotal()));
        }
        retApp.setStatus(RestApiExceptionEnums.SUCCESS.getCode().toString());
        retApp.setData(jsonArray);
        retApp.setMessage("查询成功");
        return retApp;
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/st/contacter/detail",produces = {"application/json" }, method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public RetApp contacterDetail(@RequestParam(required=true) String token,
                                  @RequestParam(required=false) String ownerContacterId,
                                  @RequestParam(required=false) String communityId,HttpServletRequest request) {
        RetApp result = new RetApp();
        String userId=UserTokenUtils.getUserIdByToken(token);
        BpOwnerContacter contacter = bpOwnerContacterService.getContacterDetail(userId,Long.parseLong(ownerContacterId),communityId);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("ownerContacterId", contacter.getOwnerContacterId());
            jsonObject.put("contacterName", StringUtils.isEmpty(contacter.getContacterName())?"": contacter.getContacterName());
            String typeDesc = CodeUtil.getDictLabel(contacter.getType(),"OWNER.CONTACTER_TYPE","","");
            jsonObject.put("typeDesc", typeDesc);
            jsonObject.put("type", StringUtils.isEmpty(contacter.getType())?"": contacter.getType());
            jsonObject.put("mobile", StringUtils.isEmpty(contacter.getMobile())?"": contacter.getMobile());
            jsonObject.put("idNo", StringUtils.isEmpty(contacter.getIdNo())?"": contacter.getIdNo());
            List<BpHouseMap> ownerHouse = contacter.getOwnerHouse();
            JSONArray houseArray=new JSONArray();
            if(!ownerHouse.isEmpty()){
               for (BpHouseMap map : ownerHouse){
                    JSONObject houseObject=new JSONObject();
                    houseObject.put("houseId",map.getBuildingId());
                    houseObject.put("houseName",map.getHouseFullName());
                    houseObject.put("isBind",map.getIsBind());
                    houseObject.put("effectiveStartDate",map.getEffectiveStartDate());
                    houseObject.put("effectiveEndDate",map.getEffectiveEndDate());
                    houseArray.add(houseObject);
                }
            }
            jsonObject.put("ownerHouse", houseArray);
        return RetAppUtil.success(jsonObject,"查询成功");
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/st/contacter/getOwnerHouse",produces = {"application/json" }, method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public RetApp getOwnerHouse(@RequestParam(required=true) String token,@RequestParam(required=false) String communityId, HttpServletRequest request) {
        RetApp result = new RetApp();
        //根据token获取userId
        String userId=UserTokenUtils.getUserIdByToken(token);
        List<BpHouseMap> ownerHouse = bpHouseMapService.getOwnerHouse(userId,communityId);
        JSONArray jsonArray=new JSONArray();
        for(BpHouseMap house:ownerHouse){
            JSONObject jsonObject=new JSONObject();
            //jsonObject.put("mapId", house.getMapId());
            jsonObject.put("houseId", house.getBuildingId());
            jsonObject.put("houseFullName", StringUtils.isEmpty(house.getHouseFullName())?"": house.getHouseFullName());
            jsonObject.put("effectiveStartDate", house.getEffectiveStartDate());
            jsonObject.put("effectiveEndDate", house.getEffectiveEndDate());
            jsonArray.add(jsonObject);
        }
        return RetAppUtil.success(jsonArray,"查询成功");
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/st/contacter/save",produces = {"application/json" }, method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public RetApp addContacter(@RequestBody BpOwnerContacter contacter, HttpServletRequest request) {
        try {
            bpOwnerContacterService.saveContacter(contacter);
        } catch (ServiceException e) {
            e.printStackTrace();
            return RetAppUtil.error(e,e.getMessage());
        }
        return RetAppUtil.success("保存成功");
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/st/contacter/delete",produces = {"application/json" }, method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public RetApp deleteContacter(BpOwnerContacter contacter, HttpServletRequest request) {
        bpOwnerContacterService.deleteContacter(contacter);
        return RetAppUtil.success("删除成功");
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/dir/code",produces = {"application/json" }, method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public RetApp getCodeList(@RequestParam(required=true) String code, HttpServletRequest request) throws JsonProcessingException {
        RetApp result = new RetApp();
        Locale locale = RequestContextUtils.getLocale(request);
        SysCodeCache codeCache = (SysCodeCache) (Cache) cacheManager.getCache("code");
        Code code2 = codeCache.getValue(code + "." + locale);
        if (code2 == null) {
            result.setStatus(FAIL);
            result.setMessage("找不到此编码");
            return result;
        }
        List<CodeValue> enabledCodeValues = getEnabledCodeValues(code2);
        if (enabledCodeValues==null){
            result.setStatus(FAIL);
            result.setMessage("找不到此编码");
            return result;
        }
        System.out.println("value::"+enabledCodeValues.get(0).getValue());
        JSONArray jsonArray=new JSONArray();
        for(CodeValue codeValue : enabledCodeValues){
            JSONObject obj = new JSONObject();
            obj.put("value",codeValue.getValue());
            obj.put("meaning",codeValue.getMeaning());
            jsonArray.add(obj);
        }
        result.setData(jsonArray);
        result.setStatus(OK);
        return result;
    }

    private List<CodeValue> getEnabledCodeValues(Code code){
        List<CodeValue> enabledCodeValues = new ArrayList<>();
        List<CodeValue> allCodeValues =  code.getCodeValues();
        if(allCodeValues!=null){
            for(CodeValue codevalue:allCodeValues){
                    enabledCodeValues.add(codevalue);
            }
        }
        return  enabledCodeValues;
    }

    protected void toJson(StringBuilder sb, String var, Object data) throws JsonProcessingException {
        boolean hasVar = var != null && var.length() > 0;
        if (hasVar) {
            sb.append("var ").append(var).append('=');
        }
        sb.append(objectMapper.writeValueAsString(data));
        if (hasVar) {
            sb.append(';');
        }
    }



}

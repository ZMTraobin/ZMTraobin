package com.cmig.future.csportal.api.common.cooperate.controllers;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.cooperate.dto.Cooperate;
import com.cmig.future.csportal.module.cooperate.service.ICooperateService;
import com.cmig.future.csportal.module.sys.city.cityutil.GenPinYin;
import com.cmig.future.csportal.module.sys.city.dto.LjhSysArea;
import com.cmig.future.csportal.module.sys.city.service.ILjhSysAreaService;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zpf on 2017/4/25.
 */
@Controller
@RequestMapping(value = "${commonPath}/cooperate/cooperate")
public class AppCooperateController extends BaseExtendController {
    @Autowired
    private ICooperateService cooperateService;
    @Autowired
    private IAppUserService appUserService;
    @Autowired
    private  ILjhSysAreaService ljhSysAreaService;
    /**
     * 商户新增
     * @param cooperate
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/add", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp addCooperate(Cooperate cooperate,HttpServletRequest request, HttpServletResponse response){
        RetApp retApp = new RetApp();
        //商户名称
        String merchantName = getParam(request,"marchantName", "");
       //地址
        String address = getParam(request,"address", "");
       //经营类型
        String operateType =  getParam(request,"operateType", "");
        //联系电话
        String phone = getParam(request,"phone", "");
        //token
        String token = getParam(request, "token", "");
        try{
        if (StringUtils.isEmpty(merchantName)) {
            throw new OAuth2Exception("商户名称不能为空");
        }
        if (StringUtils.isEmpty(address)) {
            throw new OAuth2Exception("地址不能为空");
        }
        if (StringUtils.isEmpty(operateType)) {
            throw new OAuth2Exception("经营类型不能为空");
        }
        if (StringUtils.isEmpty(phone)) {
            throw new OAuth2Exception("电话不能为空");
        }
        if (StringUtils.isEmpty(token)) {
            throw new OAuth2Exception("token不能为空");
        }
        AppUser appUser = appUserService.getByToken(token);
        if(appUser==null){
            retApp.setStatus(FAIL);
            retApp.setMessage("手机号不对!");
        }else{
            cooperate.setAppUserId(appUser.getId());
        }
        cooperate.setMerchantName(merchantName);
        cooperate.setAddress(address);
        cooperate.setOperateType(operateType);
        cooperate.setPhone(phone);
        cooperate.setAppUserId(appUser.getId());
        cooperateService.save(cooperate);
        retApp.setStatus(OK);
        retApp.setMessage("新增成功!");
        }catch (DataWarnningException e){
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage(e.getMessage());
        }
        return retApp;
    }

    /**
     * 根据token 获取appUser，根据appUser中的ID，获取该用户下的所有商户
     * @param cooperate
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/list", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp selectOperateList(Cooperate cooperate,HttpServletRequest request, HttpServletResponse response){
        RetApp retApp = new RetApp();
        //token
        String token = getParam(request, "token", "");
        try{
            if (StringUtils.isEmpty(token)) {
                throw new OAuth2Exception("token不能为空");
            }
            AppUser appUser = appUserService.getByToken(token);
            if(appUser==null){
                retApp.setStatus(FAIL);
                retApp.setMessage("手机号不对!");
            }else{
                cooperate.setAppUserId(appUser.getId());
            }
            List<Cooperate> listCooperate = cooperateService.selectCooperateByAppUserId(cooperate);
            retApp.setData(listCooperate);
            retApp.setStatus(OK);
            retApp.setMessage("查询成功!");
        }catch (DataWarnningException e){
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setData(null);
            retApp.setMessage(e.getMessage());
        }
        return retApp;
    }

    @RequestMapping(value = "/city/gencity", produces = {"application/json"}, method = RequestMethod.POST)
    public String gencity(HttpServletRequest request, HttpServletResponse response) {
        String result = "";
        //JsonMapper jsonMapper = new JsonMapper();
        try {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        List<LjhSysArea> qlist = ljhSysAreaService.getListByType(new LjhSysArea());
        if(qlist!=null && qlist.size()>0){
            for(LjhSysArea ljh:qlist){
                Map<String,String> map = new LinkedHashMap<String, String>();
                String province = ljh.getName().endsWith("市")?ljh.getName().substring(0,ljh.getName().length()-1):ljh.getName();
                String pinYin = GenPinYin.getPingYin(province);
                map.put("id",ljh.getId());
                map.put("name",ljh.getName());
                map.put("parentId",ljh.getParentId());
                map.put("shortName",province);
                map.put("letter",GenPinYin.getPinYinHeadChar(province).toUpperCase());
                map.put("cityCode",ljh.getCode());
                map.put("pinyin", GenPinYin.getPinYinHeadChar(ljh.getName().substring(0,1)).toUpperCase()+pinYin.substring(1,pinYin.length()));
                list.add(map);
            }
        }
            result = objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(result);
        return result;
    }
}

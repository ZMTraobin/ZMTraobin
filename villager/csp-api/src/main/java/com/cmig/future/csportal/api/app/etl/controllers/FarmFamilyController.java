package com.cmig.future.csportal.api.app.etl.controllers;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "${userPath}")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FarmFamilyController extends BaseExtendController {

    @RequestMapping(value = "/farm/family/info/{familyId}", produces = {"application/json"})
    public BaseResponse findById(@PathVariable("familyId") Long familyId) throws Exception {
        return getInfo(familyId);
    }

    public BaseResponse getInfo(Long familyId) throws Exception {
       BaseResponse baseResponse=null;
       String result= HttpUtil.get("http://localhost:8989/farm/family/info/"+familyId);
       if(!StringUtils.isEmpty(result)){
            baseResponse= JSONObject.parseObject(result,BaseResponse.class);
       }
       return baseResponse;
    }

}

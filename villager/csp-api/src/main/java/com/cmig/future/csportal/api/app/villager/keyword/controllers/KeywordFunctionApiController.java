/**
 * .
 */
package com.cmig.future.csportal.api.app.villager.keyword.controllers;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.villager.keyword.dto.VillagerKeywordFunction;
import com.cmig.future.csportal.module.villager.keyword.service.IVillagerKeywordFunctionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * voice order controller
 */
@Controller
@ResponseBody
@RequestMapping(value = "${userPath}")
public class KeywordFunctionApiController extends BaseExtendController {

    private final static Logger logger = LoggerFactory.getLogger(KeywordFunctionApiController.class);

    @Autowired
    private IVillagerKeywordFunctionService keywordFunctionService;

    @RequestMapping(value = "/keyword/function/list", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp list(VillagerKeywordFunction villagerKeywordFunction, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(villagerKeywordFunction)||StringUtils.isEmpty(villagerKeywordFunction.getKeyword())) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
        }

        List<VillagerKeywordFunction> list=keywordFunctionService.findByKeyWord(villagerKeywordFunction.getKeyword());
        if(!StringUtils.isEmpty(list)){
            VillagerKeywordFunction item=list.get(0);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("keyword",item.getKeyword());
            jsonObject.put("functionCode",item.getFunctionCode());
            jsonObject.put("functionName",item.getFunctionName());
            return RetAppUtil.success(jsonObject,"匹配成功");
        }
        return RetAppUtil.error(new ServiceException(RestApiExceptionEnums.KEYWORD_FAIL));
    }
}

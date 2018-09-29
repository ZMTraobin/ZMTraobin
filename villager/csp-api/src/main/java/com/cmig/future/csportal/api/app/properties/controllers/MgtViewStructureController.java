package com.cmig.future.csportal.api.app.properties.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.properties.base.view.dto.MgtViewStructure;
import com.cmig.future.csportal.module.properties.base.view.service.IMgtViewStructrueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 14:52 2017/7/18.
 * @Modified by zhangtao on 14:52 2017/7/18.
 */
@Controller
@ResponseBody
@RequestMapping(value="${userPath}")
public class MgtViewStructureController extends BaseExtendController {

	@Autowired
	private IMgtViewStructrueService mgtViewStructrueService;

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/structure/list", produces = {"application/json" }, method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public RetApp getList(HttpServletRequest request, MgtViewStructure dto){
		if(StringUtils.isEmpty(dto.getCommunityId())){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"communityId");
		}
		if(StringUtils.isEmpty(dto.getParentViewId())){
			//throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"parentViewId");
			return RetAppUtil.success(new JSONArray(),"查询成功");
		}
		MgtViewStructure param=new MgtViewStructure();
		param.setCommunityId(dto.getCommunityId());
		param.setParentViewId(dto.getParentViewId());

		List<MgtViewStructure> list= mgtViewStructrueService.findList(param);
		JSONArray jsonArray=new JSONArray();
		for(MgtViewStructure entry:list){
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("structureId",StringUtils.isEmpty(entry.getHouseId())?entry.getStructureId(): IdGen.uuid());
			jsonObject.put("structureName",StringUtils.isEmpty(entry.getHouseId())?entry.getStructureName():entry.getHouseName());
			jsonObject.put("houseId",entry.getHouseId());
			jsonObject.put("houseCode",entry.getHouseCode());
			jsonArray.add(jsonObject);
		}
		return RetAppUtil.success(jsonArray,"查询成功");
	}

}

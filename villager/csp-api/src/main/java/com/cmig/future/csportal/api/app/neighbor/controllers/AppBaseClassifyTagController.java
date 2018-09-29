package com.cmig.future.csportal.api.app.neighbor.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.operate.classifyTag.dto.LjhClassifyTag;
import com.cmig.future.csportal.module.operate.classifyTag.service.ILjhClassifyTagService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 分类标签
 * 
 * @author Mrzhang
 * @version 2016-05-12
 */
@Controller
@RequestMapping(value = "${userPath}")
public class AppBaseClassifyTagController extends BaseExtendController {
	@Autowired
	public ILjhClassifyTagService baseClassifyTagService;

	/**
	 * 查询所有的分类标签
	 * 
	 * @param baseClassifyTag
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/classifytag/findAllTagList", produces = { "application/json" }, method = RequestMethod.POST)
	public String findAllTagList(@ModelAttribute LjhClassifyTag data,
			HttpServletRequest request, HttpServletResponse response) {
		RetApp retApp = new RetApp();
		JSONArray array=new JSONArray();
		try {
		    LjhClassifyTag baseClassifyTag=new LjhClassifyTag();
			if(!StringUtils.isEmpty(data.getModel())){
				baseClassifyTag.setModel(data.getModel());
			}
			List<LjhClassifyTag> list = baseClassifyTagService.findList(baseClassifyTag);
			for(LjhClassifyTag entity:list){
				JSONObject object=new JSONObject();
				object.put("id", entity.getId());
				object.put("tagName", entity.getName());
				object.put("belongModel", entity.getModel());
				array.add(object);
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
}

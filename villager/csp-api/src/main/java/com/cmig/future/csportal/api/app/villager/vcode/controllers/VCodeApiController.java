/**
 * .
 */
package com.cmig.future.csportal.api.app.villager.vcode.controllers;

import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.exceptions.ServiceExceptionHelper;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.sys.city.dto.SimplifyArea;
import com.cmig.future.csportal.module.sys.city.mapper.LjhSysAreaMapper;
import com.cmig.future.csportal.module.sys.city.service.ILjhSysAreaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
public class VCodeApiController extends BaseExtendController {

	@Autowired
	private ILjhSysAreaService ljhSysAreaService;
	
	@Autowired
	private LjhSysAreaMapper ljhSysAreaMapper;

    /**
     * 根据国家统计局网站获取省市县乡村信息
     */
    @RequestMapping(value = "/villager/saveVillager", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp saveVillager(HttpServletRequest request) {
    	// 保存
//        ljhSysAreaService.saveVillager();
        return RetAppUtil.success("","执行成功!");
    }
    
    /**
     * 生成省市县乡村json文件
     */
    @RequestMapping(value = "/villager/getVillagerJson", produces = {"application/json"}, method = RequestMethod.GET)
    public RetApp getVillagerJson(HttpServletRequest request) {
    	// 获取json
//        String json = ljhSysAreaService.getVillagerJson();
        return RetAppUtil.success(null,"执行成功!");
    }
    
    /**
     * 根据id查询下一层级行政区划
     */
    @RequestMapping(value = "/villager/getSonElement", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp getSonElement(HttpServletRequest request) {
        String id = getParam(request, "id", "");
        if(StringUtils.isEmpty(id)){
        	id = "1";
        }
    	// 获取子节点
     	List<SimplifyArea> list = ljhSysAreaMapper.getListByPid(id);
        return RetAppUtil.success(list,"执行成功!");
    }
    
}
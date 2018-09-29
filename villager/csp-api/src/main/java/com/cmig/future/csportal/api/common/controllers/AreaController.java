/**
 * .
 */
package com.cmig.future.csportal.api.common.controllers;

import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.sys.city.dto.LjhSysArea;
import com.cmig.future.csportal.module.sys.city.service.ILjhSysAreaService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 区域Controller
 *
 * @author ThinkGem
 * @version 2013-5-15
 */
@RestController
@RequestMapping(value = "${commonPath}/sys/area")
public class AreaController extends BaseExtendController {

	@Autowired
	private ILjhSysAreaService ljhSysAreaService;

	/**
	 * 查询存在小区的城市列表
	 *
	 * @param data
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/cityWithCommunity", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp areaList(@ModelAttribute LjhSysArea data, HttpServletRequest request, HttpServletResponse response) {
		data.setType("3");
		List<LjhSysArea> list = ljhSysAreaService.findCityWithCommunity(data);
		JSONArray array = new JSONArray();
		for (LjhSysArea area : list) {
			JSONObject obj = new JSONObject();
			obj.put("id", area.getId());
			obj.put("code", area.getCode());
			obj.put("name", area.getName());
			obj.put("remarks", area.getRemarks());
			obj.put("sort", area.getSort());
			array.add(obj);
		}
		return RetAppUtil.success(array,"查询成功",new Long(list.size()));
	}

	/**
	 * 此方法描述的是：选择省
	 *
	 * @param data
	 * @param request
	 * @param response
	 * @return String
	 * @author:jiubing.gao@zymobi.com
	 */
	@RequestMapping(value = "/provinceList", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp expressAreaList(@ModelAttribute LjhSysArea data,HttpServletRequest request, HttpServletResponse response) {
		data.setType("2");
		List<LjhSysArea> list = ljhSysAreaService.findAreaList(data);
		JSONArray array = new JSONArray();
		for (LjhSysArea area : list) {
			JSONObject obj = new JSONObject();
			obj.put("provinceId", area.getId());
			obj.put("code", area.getCode());
			obj.put("name", area.getName());
			array.add(obj);
		}
		return RetAppUtil.success(array,"查询成功",new Long(list.size()));
	}

	/**
	 * 此方法描述的是：根据省查询城市
	 *
	 * @param data
	 * @param request
	 * @param response
	 * @return String
	 * @author:jiubing.gao@zymobi.com
	 */
	@RequestMapping(value = "/cityListByProvince", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp cityListByProvince(@ModelAttribute LjhSysArea data,HttpServletRequest request, HttpServletResponse response) {
		String provinceId = getParam(request, "provinceId", "");
		if (provinceId == null || provinceId.isEmpty()) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"provinceId");
		}

		LjhSysArea cityArea = new LjhSysArea();
		cityArea.setParentId(provinceId);
		cityArea.setType("3");
		List<LjhSysArea> findCityList = ljhSysAreaService.findAreaList(cityArea);
		return RetAppUtil.success(findCityList,"查询成功",new Long(findCityList.size()));
	}

	/**
	 * 此方法描述的是：根据城市查询区
	 *
	 * @param data
	 * @param request
	 * @param response
	 * @return String
	 * @author:jiubing.gao@zymobi.com
	 */
	@RequestMapping(value = "/areaListByCity", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp areaListByCity(@ModelAttribute LjhSysArea data,HttpServletRequest request, HttpServletResponse response) {
		String cityId = getParam(request, "cityId", "");
		if (cityId == null || cityId.isEmpty()) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"cityId");
		}

		LjhSysArea cityArea = new LjhSysArea();
		cityArea.setParentId(cityId);
		cityArea.setType("4");
		List<LjhSysArea> findAreaList = ljhSysAreaService.findAreaList(cityArea);
		return RetAppUtil.success(findAreaList,"查询成功",new Long(findAreaList.size()));
	}

}

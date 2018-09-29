package com.cmig.future.csportal.api.app.user.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmig.future.csportal.module.adv.dto.AppAdv;
import com.cmig.future.csportal.module.adv.service.IAppAdvService;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.hand.hap.system.dto.ResponseData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * class name: AppUserController
 * author: fri
 * date: 2017年3月28日
 * function: 用户中心接口、广告返回接口
 */
@Controller
@ResponseBody
@RequestMapping(value = "${userPath}")
public class UserAppUserController extends BaseExtendController {
	
	@Autowired
    private IAppAdvService service;
	
	/*===广告 start===*/
	/**
     * class name: queryAdv
     * author: fri
     * date: 2017年4月20日
     * function: 获取已上线广告信息
     * in:
     *		parameter1: request
     *		parameter2: response
     * out:
     *		parameter: JSONObject
     */
    @RequestMapping(value = "/getadv",method = RequestMethod.GET)
    public ResponseData queryAdv(final HttpServletRequest request, final HttpServletResponse response){
    	/* 2017年8月25日 modify 保持老版本广告信息 by fri start */
    	/*
    	Long status = 4L;//已上线
    	return new ResponseData(service.queryAppAdv(status));
    	*/
    	//JSON字符在线转码 http://www.sojson.com/yasuo.html
    	String arrayStr = "[{\"objectVersionNumber\":null,\"advId\":54,\"title\":\"i投\",\"description\":null,\"urlType\":\"1\",\"url\":\"http://m.cmiinv.com/sso.jsp\",\"pic\":\"http://sh.img.cm-pro.cn/G01/M00/00/00/rBC6BVkpW8eAXFjxAAEozhWj55I849.png\",\"advType\":1,\"status\":4,\"advRank\":1,\"approvalBy\":null,\"approvalComment\":\"\",\"advBegin\":null,\"advEnd\":null,\"isCas\":2,\"groupIdentifying\":null},{\"objectVersionNumber\":null,\"advId\":61,\"title\":\"家享美味\",\"description\":null,\"urlType\":\"2\",\"url\":null,\"pic\":\"http://sh.img.cm-pro.cn/G02/M00/00/0B/rBC6B1lTgN2AMQ5vAAMFUW7BXRY676.jpg\",\"advType\":3,\"status\":4,\"advRank\":1,\"approvalBy\":null,\"approvalComment\":\"\",\"advBegin\":null,\"advEnd\":null,\"isCas\":0,\"groupIdentifying\":null},{\"objectVersionNumber\":null,\"advId\":64,\"title\":\"单行三栏右1\",\"description\":null,\"urlType\":\"1\",\"url\":\"http://mp.weixin.qq.com/s/5Kovcoi9hpgaM6aW40Anog\",\"pic\":\"http://sh.img.cm-pro.cn/G02/M00/00/0B/rBC6B1lTgmeAaMehAABex6JkVD0633.png\",\"advType\":4,\"status\":4,\"advRank\":1,\"approvalBy\":null,\"approvalComment\":\"\",\"advBegin\":null,\"advEnd\":null,\"isCas\":0,\"groupIdentifying\":null},{\"objectVersionNumber\":null,\"advId\":59,\"title\":\"云石\",\"description\":null,\"urlType\":\"1\",\"url\":\"http://www.theyun.com\",\"pic\":\"http://sh.img.cm-pro.cn/G01/M00/00/02/rBC6BVk6dVqAFplCAAEGsex5y14151.jpg\",\"advType\":1,\"status\":4,\"advRank\":2,\"approvalBy\":null,\"approvalComment\":\"\",\"advBegin\":null,\"advEnd\":null,\"isCas\":0,\"groupIdentifying\":null},{\"objectVersionNumber\":null,\"advId\":62,\"title\":\"家享健康\",\"description\":null,\"urlType\":\"2\",\"url\":null,\"pic\":\"http://sh.img.cm-pro.cn/G02/M00/00/0B/rBC6B1lTgQCAYTNhAAB-HNqckxI175.jpg\",\"advType\":3,\"status\":4,\"advRank\":2,\"approvalBy\":null,\"approvalComment\":\"\",\"advBegin\":null,\"advEnd\":null,\"isCas\":0,\"groupIdentifying\":null},{\"objectVersionNumber\":null,\"advId\":63,\"title\":\"单行三栏左\",\"description\":null,\"urlType\":\"1\",\"url\":\"https://hshop.zmsq.net/wap/special.html?specialId=4\",\"pic\":\"http://sh.img.cm-pro.cn/G02/M00/00/0B/rBC6B1lTgkeAIepTAACeYPN1t5g374.jpg\",\"advType\":2,\"status\":4,\"advRank\":2,\"approvalBy\":null,\"approvalComment\":\"\",\"advBegin\":null,\"advEnd\":null,\"isCas\":0,\"groupIdentifying\":null},{\"objectVersionNumber\":null,\"advId\":60,\"title\":\"家享其成\",\"description\":null,\"urlType\":\"2\",\"url\":null,\"pic\":\"http://sh.img.cm-pro.cn/G02/M00/00/0B/rBC6B1lTgLSAcY55AAEohVIA5GI409.jpg\",\"advType\":3,\"status\":4,\"advRank\":3,\"approvalBy\":null,\"approvalComment\":\"\",\"advBegin\":null,\"advEnd\":null,\"isCas\":0,\"groupIdentifying\":null},{\"objectVersionNumber\":null,\"advId\":65,\"title\":\"单行三栏右2\",\"description\":null,\"urlType\":\"1\",\"url\":\"http://m.cmiinv.com/sso.jsp\",\"pic\":\"http://sh.img.cm-pro.cn/G02/M00/00/0B/rBC6B1lTgp-AVEvaAABSjFfklF8962.png\",\"advType\":5,\"status\":4,\"advRank\":3,\"approvalBy\":null,\"approvalComment\":\"\",\"advBegin\":null,\"advEnd\":null,\"isCas\":2,\"groupIdentifying\":null},{\"objectVersionNumber\":null,\"advId\":67,\"title\":\"健康管理\",\"description\":null,\"urlType\":\"1\",\"url\":\"http://wx.discomfort.healthlink.cn/youbao/HealthlinkAsk/Home.jsp\",\"pic\":\"http://sh.img.cm-pro.cn/G02/M00/00/0B/rBC6B1lUkh-AdLB8AAMnnT53j3Q972.jpg\",\"advType\":1,\"status\":4,\"advRank\":3,\"approvalBy\":null,\"approvalComment\":\"\",\"advBegin\":null,\"advEnd\":null,\"isCas\":0,\"groupIdentifying\":null}]";
    	JSONArray arrayJson = JSONArray.fromObject(arrayStr);
    	@SuppressWarnings({ "deprecation", "unchecked" })
		List<AppAdv> data = JSONArray.toList(arrayJson,AppAdv.class);
    	return new ResponseData(data);
    	/* 2017年8月25日 modify 保持老版本广告信息 by fri end */
    }
    
    /**
     * class name: queryNewAdv
     * author: fri
     * date: 2017年8月23日
     * function: 新版获取已上线广告信息
     * in:
     *		parameter1: @param request
     *		parameter1: @param response
     *		parameter1: @return
     * out:
     *		parameter: JSONObject
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/getnewadv",method = RequestMethod.GET)
    public RetApp queryNewAdv(final HttpServletRequest request, final HttpServletResponse response){
    	String token = getParam(request, "token", "");
    	try{
    		JSONObject data = service.getNewAppAdv(token);
    		return RetAppUtil.success(data,"获取成功");
    	}catch(Exception e){
    		throw new ServiceException(RestApiExceptionEnums.UNKNOW_EXCEPTION);
    	}
    }
    /*===广告 end===*/
}


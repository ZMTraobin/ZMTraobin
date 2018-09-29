package com.cmig.future.csportal.module.properties.community.controllers;

import com.cmig.future.csportal.module.base.entity.ResponseExtData;
import com.cmig.future.csportal.module.properties.base.customer.CustomerInfoException;
import com.cmig.future.csportal.module.properties.community.dto.BaseCommunity;
import com.cmig.future.csportal.module.properties.community.service.IBaseCommunityService;
import com.cmig.future.csportal.module.sys.city.dto.LjhSysArea;
import com.cmig.future.csportal.module.sys.city.service.ILjhSysAreaService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * BaseCommunity
 * 项目管理页面控制层
 * @author bubu
 *
 * 2017-3-21
 */
    @Controller
    public class BaseCommunityController extends BaseController {

    private static Logger logger= LoggerFactory.getLogger(BaseCommunityController.class);
    @Autowired
    private IBaseCommunityService baseCommunityService;

    @Autowired
    private ILjhSysAreaService ljhSysAreaService;

    /**
     *  我的项目-分页查询
     *
     *  @author bubu
     * @param dto
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/ljh/base/community/query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData query(BaseCommunity dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(baseCommunityService.selectCommunity(requestContext,dto,page,pageSize));
    }


    /**
     *  我的项目-根据id查询项目信息
     *
     *  @author bubu
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/ljh/base/community/get")
    @ResponseBody
    public ResponseData get(@RequestParam(required = true)String id, HttpServletRequest request) {
        BaseCommunity dto = baseCommunityService.get(id);

        return new ResponseExtData(dto);
    }


    /**
     * 更新项目信息
     *
     * @author bubu
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/ljh/base/community/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData updateCommunity(HttpServletRequest request,@RequestBody BaseCommunity dto){
        String cityId = dto.getCityId();
        LjhSysArea city = ljhSysAreaService.queryCityNameById(cityId);
        dto.setCityName(city.getName());
        baseCommunityService.save(dto);
        return new ResponseExtData(dto);
    }

    /**
     * 项目启用.停用
     * @param request
     * @param id
     * @param unableFlag
     * @return
     */
    @RequestMapping(value = "/ljh/base/community/unableUpdate", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData unableUpdate(HttpServletRequest request,String id,String unableFlag){
        IRequest requestContext = createRequestContext(request);
        BaseCommunity dto = new BaseCommunity();
        if(id != null){
            dto.setId(id);
            dto = baseCommunityService.selectByPrimaryKey(requestContext,dto);
            dto.setUnableFlag(unableFlag);

            baseCommunityService.save(dto);
        }
        return new ResponseExtData(dto);
    }
    
    /**
     * 项目删除
     * @param request
     * @param id
     * @param unableFlag
     * @return
     * @throws CustomerInfoException 
     */
    @RequestMapping(value = "/ljh/base/community/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,String id) throws CustomerInfoException{
        IRequest requestContext = createRequestContext(request);
        if(id != null){
            baseCommunityService.deleteCommunity(requestContext,id);
        }
        return new ResponseData();
    }

    }
package com.cmig.future.csportal.module.properties.base.house.controllers;

import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.entity.ResponseExtData;
import com.cmig.future.csportal.module.properties.base.house.dto.MgtHouse;
import com.cmig.future.csportal.module.properties.base.house.service.IMgtHouseService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

    @Controller
    public class MgtHouseController extends BaseController{

    @Autowired
    private IMgtHouseService mgtHouseService;


    @RequestMapping(value = "/csp/mgt/house/query")
    @ResponseBody
    public ResponseData query(MgtHouse dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(mgtHouseService.selectList(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/csp/mgt/house/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<MgtHouse> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(mgtHouseService.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/mgt/house/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<MgtHouse> dto){
        mgtHouseService.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/csp/mgt/house/queryByHouseId")
    @ResponseBody
    public ResponseData queryByHouseId(HttpServletRequest request,MgtHouse dto){

        return new ResponseExtData(mgtHouseService.queryByHouseId(dto));
    }

    @RequestMapping(value = "/csp/mgt/house/saveOrUpdate")
    @ResponseBody
    public ResponseData saveOrUpdate(HttpServletRequest request,MgtHouse dto){
        IRequest requestCtx = createRequestContext(request);
        if(StringUtils.isEmpty(dto.getHouseId())){
            dto.set__status(DTOStatus.ADD);
        }else{
            dto.set__status(DTOStatus.UPDATE);
        }
        return new ResponseExtData(mgtHouseService.saveOrUpdate(requestCtx,dto));
    }

    @RequestMapping(value = "/csp/mgt/house/unableUpdate")
    @ResponseBody
    public ResponseData unableUpdate(HttpServletRequest request,MgtHouse dto){
        IRequest requestCtx = createRequestContext(request);
        Long houseId = dto.getHouseId();
        if (houseId != null) {
            mgtHouseService.updateByPrimaryKeySelective(requestCtx,dto);
        }
        return new ResponseExtData(dto);
    }

    }
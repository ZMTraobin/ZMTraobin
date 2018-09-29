package com.cmig.future.csportal.module.properties.base.structure.controllers;

import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.entity.ResponseExtData;
import com.cmig.future.csportal.module.properties.base.structure.dto.MgtStructure;
import com.cmig.future.csportal.module.properties.base.structure.service.IMgtStructureService;
import com.cmig.future.csportal.module.properties.base.view.dto.MgtViewPojo;
import com.cmig.future.csportal.module.properties.base.view.service.IMgtViewPojoService;
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
    public class MgtStructureController extends BaseController {

    @Autowired
    private IMgtStructureService mgtStructureService;
    @Autowired
    private IMgtViewPojoService mgtViewPojoService;

    @RequestMapping(value = "/csp/mgt/structure/query")
    @ResponseBody
    public ResponseData query(MgtStructure dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(mgtStructureService.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/csp/mgt/structure/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<MgtStructure> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(mgtStructureService.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/mgt/structure/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<MgtStructure> dto){
        mgtStructureService.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/csp/mgt/structure/queryByStructureId")
    @ResponseBody
    public ResponseData queryByStructureId(HttpServletRequest request,MgtStructure dto){

        return new ResponseExtData(mgtStructureService.queryByStructureId(dto));
    }

    @RequestMapping(value = "/csp/mgt/structure/saveOrUpdate" )
    @ResponseBody
    public ResponseData saveOrUpdate(HttpServletRequest request,MgtStructure dto,String houseId){
        MgtViewPojo mgtViewPojo = new MgtViewPojo();
        mgtViewPojo.setViewId("b"+houseId);
        List<MgtViewPojo> list = mgtViewPojoService.findList(mgtViewPojo);
        if(!StringUtils.isEmpty(list)){
            ResponseData data = new ResponseData();
            data.setSuccess(false);
            data.setMessage("该房屋实体已经被使用!");
            return data;
        }else{
            if(StringUtils.isEmpty(dto.getStructureId())){
                dto.set__status(DTOStatus.ADD);
            }else{
                dto.set__status(DTOStatus.UPDATE);
            }
            IRequest requestCtx = createRequestContext(request);
            return new ResponseExtData(mgtStructureService.saveOrUpdate(requestCtx,dto,houseId));
        }
    }

    @RequestMapping(value = "/csp/mgt/structure/unableUpdate")
    @ResponseBody
    public ResponseData unableUpdate(HttpServletRequest request,MgtStructure dto){
        IRequest requestCtx = createRequestContext(request);
        Long structureId = dto.getStructureId();
        if (structureId != null) {
            mgtStructureService.updateByPrimaryKeySelective(requestCtx,dto);
        }
        return new ResponseExtData(dto);
    }

    }
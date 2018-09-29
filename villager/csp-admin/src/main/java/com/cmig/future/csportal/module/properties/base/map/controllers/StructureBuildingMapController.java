package com.cmig.future.csportal.module.properties.base.map.controllers;

import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.entity.ResponseExtData;
import com.cmig.future.csportal.module.properties.base.map.dto.StructureBuildingMap;
import com.cmig.future.csportal.module.properties.base.map.service.IStructureBuildingMapService;
import com.cmig.future.csportal.module.properties.base.utils.MgtStructureHelper;
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
    public class StructureBuildingMapController extends BaseController {

    @Autowired
    private IStructureBuildingMapService structureBuildingMapService;
    @Autowired
    private IMgtViewPojoService mgtViewPojoService;

    @RequestMapping(value = "/csp/structure/building/map/query")
    @ResponseBody
    public ResponseData query(StructureBuildingMap dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(structureBuildingMapService.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/csp/structure/building/map/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<StructureBuildingMap> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(structureBuildingMapService.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/structure/building/map/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<StructureBuildingMap> dto){
        structureBuildingMapService.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/csp/structure/building/map/saveOrUpdate")
    @ResponseBody
    public ResponseData saveOrUpdate(HttpServletRequest request,StructureBuildingMap dto) {
        MgtViewPojo mgtViewPojo = new MgtViewPojo();
        mgtViewPojo.setViewId("b" + dto.getBuildingId());
        List<MgtViewPojo> list = mgtViewPojoService.findList(mgtViewPojo);
        if (!StringUtils.isEmpty(list)) {
            ResponseData data = new ResponseData();
            data.setSuccess(false);
            data.setMessage("该房屋实体已经被使用!");
            return data;
        } else {
            if (StringUtils.isEmpty(dto.getMapId())) {
                dto.set__status(DTOStatus.ADD);
            } else {
                dto.set__status(DTOStatus.UPDATE);
            }
            IRequest requestCtx = createRequestContext(request);
            return new ResponseExtData(structureBuildingMapService.saveOrUpdate(requestCtx, dto));
        }
    }

    @RequestMapping(value = "/csp/structure/building/map/deleteByStructureIdAndBuildIngId")
    @ResponseBody
    public ResponseData deleteByStructureIdAndBuildIngId(HttpServletRequest request,StructureBuildingMap dto){
        dto.setBuildingType(MgtStructureHelper.HOUSE);
        dto = structureBuildingMapService.findByBuildingIdAndStructureId(dto);
        structureBuildingMapService.deleteByPrimaryKey(dto);
        return new ResponseData();
    }

    }
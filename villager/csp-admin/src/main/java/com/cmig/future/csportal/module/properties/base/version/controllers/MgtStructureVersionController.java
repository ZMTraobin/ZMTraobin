package com.cmig.future.csportal.module.properties.base.version.controllers;

import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.entity.ResponseExtData;
import com.cmig.future.csportal.module.properties.base.map.dto.StructureBuildingMap;
import com.cmig.future.csportal.module.properties.base.map.service.IStructureBuildingMapService;
import com.cmig.future.csportal.module.properties.base.structure.dto.MgtStructure;
import com.cmig.future.csportal.module.properties.base.structure.service.IMgtStructureService;
import com.cmig.future.csportal.module.properties.base.utils.MgtStructureHelper;
import com.cmig.future.csportal.module.properties.base.version.dto.MgtStructureVersion;
import com.cmig.future.csportal.module.properties.base.version.service.IMgtStructureVersionService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
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
    public class MgtStructureVersionController extends BaseController{

    @Autowired
    private IMgtStructureVersionService mgtStructureVersionService;
    @Autowired
    private IMgtStructureService mgtStructureService;
    @Autowired
    private IStructureBuildingMapService structureBuildingMapService;

    @RequestMapping(value = "/csp/mgt/structure/version/query")
    @ResponseBody
    public ResponseData query(MgtStructureVersion dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        List<MgtStructureVersion> list = mgtStructureVersionService.findList(requestContext,dto,page,pageSize);
        return new ResponseData(list);
    }

    @RequestMapping(value = "/csp/mgt/structure/version/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<MgtStructureVersion> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(mgtStructureVersionService.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/mgt/structure/version/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<MgtStructureVersion> dto){
        mgtStructureVersionService.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/csp/mgt/structure/version/saveOrUpdate")
    @ResponseBody
    public ResponseData saveOrUpdate(HttpServletRequest request,@RequestBody List<MgtStructureVersion> dto){
        IRequest requestCtx = createRequestContext(request);
        MgtStructureVersion mgtStructureVersion = dto.get(0);
        List<MgtStructureVersion> mgtList = mgtStructureVersionService.findByCommunityIdAndDefault(mgtStructureVersion);
        //查询该小区是否有默认的的，如果存在默认的就添加不成功
        if(mgtStructureVersion.getVersionId()==null){
            if(mgtList!=null && mgtList.size()>=1 && mgtStructureVersion.getIsDefault().equals("Y")){
                ResponseData data = new ResponseData();
                data.setSuccess(false);
                data.setMessage("该项目已存在默认结构版本");
                return data;
            }
        }else if(mgtStructureVersion.getVersionId()!=null){
            if(mgtList!=null && mgtList.size()>=1
                    && mgtStructureVersion.getIsDefault().equals("Y")
                    && mgtStructureVersion.getVersionId().longValue()!=mgtList.get(0).getVersionId().longValue()){
                ResponseData data = new ResponseData();
                data.setSuccess(false);
                data.setMessage("该项目已存在默认结构版本");
                return data;
            }else{
                mgtStructureVersionService.saveOrUpdate(requestCtx,dto);
            }
        }
        return new ResponseData(mgtStructureVersionService.saveOrUpdate(requestCtx,dto));
    }

    @RequestMapping(value = "/csp/mgt/structure/version/queryByVersionId")
    @ResponseBody
    public ResponseData queryByVersionId(HttpServletRequest request,MgtStructureVersion dto){
        return new ResponseExtData(mgtStructureVersionService.queryByVersionId(dto));
    }

    @RequestMapping(value = "/csp/mgt/structure/version/versionDel")
    @ResponseBody
    public ResponseData versionDel(HttpServletRequest request,MgtStructureVersion dto){
        Long versionId = dto.getVersionId();
        if(!StringUtils.isEmpty(versionId)) {
            StructureBuildingMap structureBuildingMap = new StructureBuildingMap();
            structureBuildingMap.setVersionId(versionId);
            structureBuildingMap.setBuildingType(MgtStructureHelper.HOUSE);
            //删除关联关系
            structureBuildingMapService.deleteByStructureVersionId(structureBuildingMap);
            MgtStructure mgtStructure = new MgtStructure();
            mgtStructure.setVersionId(versionId);
            //删除建筑结构
            mgtStructureService.deleteByVersionId(mgtStructure);
            //删除版本
            mgtStructureVersionService.versionDel(dto);
        }
        return new ResponseData();
    }

    @RequestMapping(value = "/csp/mgt/structure/version/unableUpdate")
    @ResponseBody
    public ResponseData unableUpdate(HttpServletRequest request,MgtStructureVersion dto){
        IRequest requestContext = createRequestContext(request);
        //dto = mgtStructureVersionService.selectByPrimaryKey(requestContext,dto);
        Long versionId = dto.getVersionId();
        if(!StringUtils.isEmpty(versionId)){
            dto = mgtStructureVersionService.updateByPrimaryKeySelective(requestContext,dto);
        }
        return new ResponseExtData(dto);
    }

    }
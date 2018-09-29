package com.cmig.future.csportal.module.properties.base.structure.service;

import com.cmig.future.csportal.common.utils.excel.ImportExcel;
import com.cmig.future.csportal.module.properties.base.structure.dto.MgtStructure;
import com.cmig.future.csportal.module.properties.base.structure.dto.MgtStructureImport;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface IMgtStructureService extends IBaseService<MgtStructure>, ProxySelf<IMgtStructureService> {

    public void validationImportData(ImportExcel ei, List<MgtStructureImport> list, String communityId);

    public void saveImportDate(List<MgtStructureImport> list, String versionId, String communityId);

    MgtStructure findByStructureCode(MgtStructure dto);

    MgtStructure queryByStructureId(MgtStructure dto);

    MgtStructure saveOrUpdate(IRequest requestCtx, MgtStructure dto, String houseId);

    int deleteByVersionId(MgtStructure dto);

}
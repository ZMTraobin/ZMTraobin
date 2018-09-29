package com.cmig.future.csportal.module.properties.base.house.service;

import com.cmig.future.csportal.common.utils.excel.ImportExcel;
import com.cmig.future.csportal.module.properties.base.house.dto.MgtHouse;
import com.cmig.future.csportal.module.properties.base.house.dto.MgtHouseImport;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface IMgtHouseService extends IBaseService<MgtHouse>, ProxySelf<IMgtHouseService>{

    MgtHouse queryByHouseId(MgtHouse dto);

    MgtHouse saveOrUpdate(IRequest requestCtx, MgtHouse dto);

    public void validationImportData(ImportExcel ei, List<MgtHouseImport> list);

    public void saveImportDate(List<MgtHouseImport> list);

    MgtHouse findBySourceHouseCodeAndSourceSystem(MgtHouse dto);

    List<MgtHouse> selectList(IRequest requestContext, MgtHouse dto, int page, int pageSize);

	MgtHouse get(Object o);
}
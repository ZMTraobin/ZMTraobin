package com.cmig.future.csportal.module.properties.base.customer.service;

import com.cmig.future.csportal.common.utils.excel.ImportExcel;
import com.cmig.future.csportal.module.properties.base.customer.CustomerInfoException;
import com.cmig.future.csportal.module.properties.base.customer.dto.BpOwner;
import com.cmig.future.csportal.module.properties.base.customer.dto.BpOwnerImport;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface IBpOwnerService extends IBaseService<BpOwner>, ProxySelf<IBpOwnerService>{

    void validationImportData(ImportExcel ei, List<BpOwnerImport> list);

    void saveImportDate(List<BpOwnerImport> list);

    List<BpOwner> selectCustomer(IRequest requestContext, Long id, int page, int pageSize);

    List<BpOwner> findGeneralByBuildingId(Long buildingId);

    List<BpOwner> saveOrUpdate(IRequest requestCtx, List<BpOwner> dto) throws CustomerInfoException;

    void deleteOwner(List<BpOwner> dto);

    List<BpOwner> queryOwners(IRequest requestContext, BpOwner dto, int page, int pageSize);

}
package com.cmig.future.csportal.module.villager.family.service;

import com.cmig.future.csportal.common.utils.excel.ImportExcel;
import com.cmig.future.csportal.module.pay.order.dto.AppOrderVo;
import com.cmig.future.csportal.module.properties.payment.receivable.dto.MgtReceivableDetail;
import com.cmig.future.csportal.module.properties.payment.receivable.dto.MgtReceivableImport;
import com.cmig.future.csportal.module.villager.family.dto.FamilyImport;
import com.cmig.future.csportal.module.villager.family.dto.FamilyInfo;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;
import java.util.Map;

public interface IFamilyService extends IBaseService<FamilyInfo>, ProxySelf<IFamilyService>{


    void validationImportData(ImportExcel ei, List<FamilyImport> list);

    void saveImportDate(List<FamilyImport> list);
    
}
package com.cmig.future.csportal.module.properties.payment.receivable.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmig.future.csportal.module.properties.payment.receivable.dto.MgtReceivableDetail;
import com.hand.hap.core.IRequest;
import com.hand.hap.mybatis.common.Mapper;

public interface MgtReceivableDetailMapper extends Mapper<MgtReceivableDetail> {

    MgtReceivableDetail findBySourceReceivableIdAndSourceSystem(MgtReceivableDetail dto);

    List<MgtReceivableDetail> queryMgtReceivableDetail(MgtReceivableDetail dto);

    List<MgtReceivableDetail> queryBills(MgtReceivableDetail detail);

    List<MgtReceivableDetail> queryBillsByType(MgtReceivableDetail detail);

}
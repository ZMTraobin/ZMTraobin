package com.cmig.future.csportal.module.properties.payment.receivable.service;

import com.cmig.future.csportal.common.utils.excel.ImportExcel;
import com.cmig.future.csportal.module.pay.order.dto.AppOrderVo;
import com.cmig.future.csportal.module.properties.payment.receivable.dto.MgtReceivableDetail;
import com.cmig.future.csportal.module.properties.payment.receivable.dto.MgtReceivableImport;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;
import java.util.Map;

public interface IMgtReceivableDetailService extends IBaseService<MgtReceivableDetail>, ProxySelf<IMgtReceivableDetailService>{

    void save(MgtReceivableDetail dto);

    MgtReceivableDetail findBySourceReceivableIdAndSourceSystem(MgtReceivableDetail dto);

    void validationImportData(ImportExcel ei, List<MgtReceivableImport> list);

    void saveImportDate(List<MgtReceivableImport> list);
    
    List<MgtReceivableDetail> queryMgtReceivableDetail(IRequest requestContext, MgtReceivableDetail dtoint, int page, int pageSize);

    List<MgtReceivableDetail> queryBills(MgtReceivableDetail detail, int page, int pageSize);

     List<MgtReceivableDetail> queryBillsFirstType(MgtReceivableDetail detail, Integer page, Integer pageSize);

	/**
	 * app提交缴费单
	 * @param appOrderVo
	 * @return
	 */
	AppOrderVo paymentSubmit(AppOrderVo appOrderVo) throws Exception;

	/**
	 * 支付成功通知回调处理
	 * @param map
	 */
	void notifySuccess(Map map) throws Exception;
}
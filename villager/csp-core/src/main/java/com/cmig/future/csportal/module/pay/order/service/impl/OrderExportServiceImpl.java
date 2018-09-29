package com.cmig.future.csportal.module.pay.order.service.impl;

import com.cmig.future.csportal.common.utils.excel.ExcelDataImportEnum;
import com.cmig.future.csportal.common.utils.excel.ExportExcel;
import com.cmig.future.csportal.common.utils.excel.IExcelDataImportService;
import com.cmig.future.csportal.common.utils.excel.ImportExcel;
import com.cmig.future.csportal.module.pay.order.dto.OrderForm;
import com.cmig.future.csportal.module.pay.order.dto.OrderFormExcel;
import com.cmig.future.csportal.module.pay.order.service.IOrderFormService;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.extensible.components.ServiceListenerChainFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 19:03 2018/1/2.
 * @Modified by zhangtao on 19:03 2018/1/2.
 */
@Service
@Transactional
public class OrderExportServiceImpl implements IExcelDataImportService {

	@Autowired
	private IOrderFormService orderFormService;

	@Autowired
	private ServiceListenerChainFactory chainFactory;

	@Override
	public ExcelDataImportEnum getCode() {
		return ExcelDataImportEnum.ORDER;
	}

	@Override
	public void download(ExcelDataImportEnum excelDataImportEnum, HttpServletResponse response, HttpServletRequest request) throws IOException {
		IRequest requestContext = RequestHelper.createServiceRequest(request);
		OrderForm dto=new OrderForm();
		dto = (OrderForm) this.chainFactory.getChain(this).beforeInsert(requestContext, dto);
		List<OrderForm> list = orderFormService.selectByCondition(requestContext, dto, 1, 100);
		String fileName = excelDataImportEnum.getDescription()+".xlsx";
		new ExportExcel(excelDataImportEnum.getDescription(), OrderFormExcel.class, 2).setDataList(list).write(response, fileName,request).dispose();
	}

	@Override
	public ImportExcel validation(MultipartFile file, Map attach) throws Exception {
		return null;
	}

	@Override
	public void persistence(ImportExcel ei, Map attach) {

	}
}

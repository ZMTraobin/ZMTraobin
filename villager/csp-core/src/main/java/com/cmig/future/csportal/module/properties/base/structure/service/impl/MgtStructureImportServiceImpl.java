package com.cmig.future.csportal.module.properties.base.structure.service.impl;

import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.excel.ExcelDataImportEnum;
import com.cmig.future.csportal.common.utils.excel.ExportExcel;
import com.cmig.future.csportal.common.utils.excel.IExcelDataImportService;
import com.cmig.future.csportal.common.utils.excel.ImportExcel;
import com.cmig.future.csportal.module.properties.base.structure.dto.MgtStructureImport;
import com.cmig.future.csportal.module.properties.base.structure.service.IMgtStructureService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @Date Created in 15:35 2017/7/14.
 * @Modified by zhangtao on 15:35 2017/7/14.
 */
@Service
@Transactional
public class MgtStructureImportServiceImpl implements IExcelDataImportService {
	private static final Logger logger= LoggerFactory.getLogger(MgtStructureImportServiceImpl.class);

	@Autowired
	private IMgtStructureService mgtStructureService;

	@Override
	public ExcelDataImportEnum getCode() {
		return ExcelDataImportEnum.STRUCTURE;
	}

	@Override
	public void download(ExcelDataImportEnum excelDataImportEnum, HttpServletResponse response, HttpServletRequest request) throws IOException {
		String fileName = excelDataImportEnum.getDescription()+"导入模版.xlsx";
		List<MgtStructureImport> list = Lists.newArrayList();
		list.add(new MgtStructureImport());
		new ExportExcel(excelDataImportEnum.getDescription(), MgtStructureImport.class, 2).setDataList(list).write(response, fileName,request).dispose();
	}

	@Override
	public ImportExcel validation(MultipartFile file,Map attach) throws Exception {
		ImportExcel ei =  new ImportExcel(file, 0, 0);
		List<MgtStructureImport> list = ei.getDataList(MgtStructureImport.class);
		logger.debug("upload local file " + file.getName() + " ok" );
		if(StringUtils.isEmpty(ei.getExcelErrorList())){
			//1、校验数据
			String communityId=attach.get("communityId")==null?"":attach.get("communityId").toString();
			mgtStructureService.validationImportData(ei,list,communityId);
		}
		return ei;
	}

	@Override
	public void persistence(ImportExcel ei,Map attach) {
		//2、持久化数据
		String communityId=attach.get("communityId")==null?"":attach.get("communityId").toString();
		String versionId=attach.get("versionId")==null?"":attach.get("versionId").toString();
		mgtStructureService.saveImportDate(ei.getExcelDateList(),versionId,communityId);
	}
}

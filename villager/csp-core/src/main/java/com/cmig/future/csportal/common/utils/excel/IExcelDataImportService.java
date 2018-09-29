package com.cmig.future.csportal.common.utils.excel;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 14:56 2017/7/14.
 * @Modified by zhangtao on 14:56 2017/7/14.
 */
public interface IExcelDataImportService {

	/**
	 * 获取excel导入功能代码
	 * @return
	 */
	ExcelDataImportEnum getCode();

	/**
	 * 下载导入模版
	 * @param excelDataImportEnum
	 * @param response
	 * @param request
	 */
	void download(ExcelDataImportEnum excelDataImportEnum,HttpServletResponse response, HttpServletRequest request) throws IOException;

	/**
	 * 校验excel数据
	 * @param file
	 * @return
	 * @throws Exception
	 */
	ImportExcel validation(MultipartFile file,Map attach) throws Exception;

	/**
	 * 持久化excel数据
	 * @param ei
	 */
	void persistence(ImportExcel ei,Map attach);
}

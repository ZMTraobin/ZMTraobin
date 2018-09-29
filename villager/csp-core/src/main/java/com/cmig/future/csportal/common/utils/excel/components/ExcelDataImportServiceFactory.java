package com.cmig.future.csportal.common.utils.excel.components;

import com.cmig.future.csportal.common.utils.excel.ExcelDataImportEnum;
import com.cmig.future.csportal.common.utils.excel.IExcelDataImportService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 16:38 2017/7/14.
 * @Modified by zhangtao on 16:38 2017/7/14.
 */
@Component
public class ExcelDataImportServiceFactory implements ApplicationContextAware {
	private static Map<ExcelDataImportEnum, IExcelDataImportService> trafficBeanMap;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Map<String, IExcelDataImportService> map = applicationContext.getBeansOfType(IExcelDataImportService.class);
		trafficBeanMap = new HashMap<>();
		map.forEach((key, value) -> trafficBeanMap.put(value.getCode(), value));
	}

	public static <T extends IExcelDataImportService> T getExcelDataImportService(ExcelDataImportEnum code) {
		return (T)trafficBeanMap.get(code);
	}
}

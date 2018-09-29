package com.cmig.future.csportal.module.base.controllers;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.excel.ExcelDataImportEnum;
import com.cmig.future.csportal.common.utils.excel.IExcelDataImportService;
import com.cmig.future.csportal.common.utils.excel.ImportExcel;
import com.cmig.future.csportal.common.utils.excel.components.ExcelDataImportServiceFactory;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.hand.hap.system.controllers.BaseController;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 16:16 2017/7/14.
 * @Modified by zhangtao on 16:16 2017/7/14.
 */
@Controller
@RequestMapping(value = "${commonPath}/excel/")
public class ExcelDataImportController extends BaseController {

	private Logger logger= LoggerFactory.getLogger(ExcelDataImportController.class);

	@Value("${api.maxUploadSize}")
	private String maxUploadSize;

	public static final String OK = "1";
	public static final String FAIL = "0";

	private static Map<String, String> excelTypes = new HashMap<String, String>();
	static {
		excelTypes.put("XLS", "XLS");
		excelTypes.put("XLSX", "XLSX");
	}

	/**
	 * 重写getParameter
	 * @param request
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	protected String getParam(HttpServletRequest request,
	                          String key,String defaultValue) {

		String value = request.getParameter(key);

		if(value==null||value.isEmpty()){
			return defaultValue;
		}
		return StringEscapeUtils.escapeHtml4(value.trim());
	}
	/**
	 * Excel 导入
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "import", produces = { "application/json" }, method = RequestMethod.POST)
	@ResponseBody
	public RetApp upload(HttpServletRequest request, HttpServletResponse response) {
		RetApp retApp = new RetApp();
		JSONArray jsonArray=new JSONArray();
		try{
			String importEnumName = getParam(request,"importEnumName","");
			String mSizeStr = getParam(request,"mSize","");
			String versionId = getParam(request,"versionId","");
			String communityId = getParam(request,"communityId","");
			if(mSizeStr!="" && !("0").equals(mSizeStr)){
				Double mSize = Double.valueOf(mSizeStr)*1024*1024;
				maxUploadSize = String.valueOf(mSize);
			}
			//创建一个通用的多部分解析器
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
			//判断 request 是否有文件上传,即多部分请求
			if(multipartResolver.isMultipart(request)) {
				//转换成多部分request
				MultipartHttpServletRequest multiRequest = multipartResolver.resolveMultipart(request);
				// 取得request中的所有文件名
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					// 记录上传过程起始时的时间，用来计算上传时间
					int pre = (int) System.currentTimeMillis();
					// 取得上传文件
					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null) {
						// 取得当前上传文件的文件名称
						String myFileName = file.getOriginalFilename();
						// 获取上传文件大小
						Long size = file.getSize();
						// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
						if (myFileName.trim() != "") {
							if (new Double(maxUploadSize) < file.getSize()) {
								throw new DataWarnningException("上传文件不能超过"
										+ String.format("%f", Double.valueOf(maxUploadSize) / (1024.0 * 1024.0)) + "M");
							}
							// 文件格式
							String fileType = myFileName.substring(myFileName.lastIndexOf(".") + 1).toUpperCase();
							if (!excelTypes.containsKey(fileType)) {
								throw new DataWarnningException("请上传xls,xlsx格式的文件！");
							}
							Map<String, String> metaList = new HashMap<String, String>();
							metaList.put("uploadDate", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));

							ExcelDataImportEnum excelDataImportEnum=ExcelDataImportEnum.valueOf(importEnumName);
							//从工厂中获取实际执行实现类
							IExcelDataImportService service= ExcelDataImportServiceFactory.getExcelDataImportService(excelDataImportEnum);
							//1、校验数据
							Map attach=new HashMap();
							attach.put("communityId",communityId);
							attach.put("versionId",versionId);
							ImportExcel ei=service.validation(file,attach);
							if(StringUtils.isEmpty(ei.getExcelErrorList())){
								service.persistence(ei,attach);
								retApp.setMessage("导入成功");
							}else if(!StringUtils.isEmpty(ei.getExcelErrorList())){
								retApp.setMessage("导入失败");
							}
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("excelErrorList", ei.getExcelErrorList());
							jsonObject.put("fileName", myFileName);
							jsonObject.put("size", size);
							jsonObject.put("list_length", ei.getExcelDateList().size());
							jsonArray.add(jsonObject);
						}
					}
					// 记录上传该文件后的时间
					int finaltime = (int) System.currentTimeMillis();
					logger.debug(new Integer(finaltime - pre).toString());
				}
			}
			retApp.setData(jsonArray);
			retApp.setStatus(OK);
		} catch (DataWarnningException e) {
			retApp.setStatus(FAIL);
			retApp.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			retApp.setStatus(FAIL);
			retApp.setMessage("上传失败");
		}
		return retApp;
	}

	/**
	 * 下载模板
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "download")
	public String importFileTemplate(HttpServletResponse response, HttpServletRequest request) {
		try {
			String importEnumName = getParam(request,"importEnumName","");
			ExcelDataImportEnum excelDataImportEnum=ExcelDataImportEnum.valueOf(importEnumName);
			//从工厂中获取实际执行实现类
			IExcelDataImportService service= ExcelDataImportServiceFactory.getExcelDataImportService(excelDataImportEnum);
			service.download(excelDataImportEnum,response,request);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 文件删除
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "delete", produces = { "application/json" }, method = RequestMethod.POST)
	@ResponseBody
	public RetApp delete(HttpServletRequest request, HttpServletResponse response) {
		RetApp retApp = new RetApp();
		retApp.setStatus(OK);
		retApp.setMessage("删除成功");

		return retApp;
	}

}

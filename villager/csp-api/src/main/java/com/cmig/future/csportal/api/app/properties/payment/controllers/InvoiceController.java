package com.cmig.future.csportal.api.app.properties.payment.controllers;

import com.alibaba.fastjson.JSONObject;
import com.baiwang.bop.client.BopException;
import com.baiwang.bop.request.impl.invoice.impl.FormatfileBuildRequest;
import com.baiwang.bop.respose.entity.FormatfileBuildResponse;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.invoice.service.InvoiceService;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 17:38 2018/3/9.
 * @Modified by zhangtao on 17:38 2018/3/9.
 */
@Controller
@RequestMapping(value = "${userPath}")
public class InvoiceController extends BaseExtendController {

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private IAppUserService appUserService;

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/st/invoice/format/file/bulid", produces = {"application/json"}, method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS})
	public RetApp formatFileBulid(FormatfileBuildRequest formatfileBuildRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			/*
			String token = getParam(request, "token", "");
			AppUser appUser = appUserService.getByToken(token);
			if (null != appUser && !StringUtils.isEmpty(appUser.getMobile())) {
				//推送标志 0：不推送 1：推送（邮箱电话必填一个）
				formatfileBuildRequest.setPushType("1");
				formatfileBuildRequest.setBuyerPhone(appUser.getMobile());
			} else {
				formatfileBuildRequest.setPushType("0");
			}*/
			formatfileBuildRequest.setPushType("0");
			FormatfileBuildResponse formatfileBuildResponse = invoiceService.formatFileBulid(formatfileBuildRequest);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("pdfurl", formatfileBuildResponse.getData());
			return RetAppUtil.success(jsonObject, formatfileBuildResponse.getMessage());
		} catch (BopException e) {
			throw new DataWarnningException(e.getMessage());
		}
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/st/invoice/read/file", produces = {"application/json"}, method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS})
	public void getPdfStream(FormatfileBuildRequest formatfileBuildRequest,HttpServletRequest request, HttpServletResponse response)throws Exception {
		try {
			String pdfpath=getParam(request,"pdfpath","");
			if(null!=formatfileBuildRequest&&!StringUtils.isEmpty(pdfpath)) {
				InputStream fileInputStream = getYCFile(pdfpath);
				response.setHeader("Content-Disposition", "attachment;fileName=invoice.pdf");
				response.setContentType("multipart/form-data");
				response.setHeader("Access-Control-Allow-Headers","Accept-Ranges");
				OutputStream outputStream = response.getOutputStream();
				IOUtils.write(IOUtils.toByteArray(fileInputStream), outputStream);
			}
		} catch (BopException e) {
			throw new DataWarnningException(e.getMessage());
		}

	}

	public InputStream getYCFile(String urlPath) throws Exception {
		String strUrl = urlPath.trim();
		URL url = new URL(strUrl);
		//打开请求连接
		URLConnection connection = url.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
		httpURLConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		// 取得输入流，并使用Reader读取
		InputStream inputStream = httpURLConnection.getInputStream();
		return inputStream;
	}
}
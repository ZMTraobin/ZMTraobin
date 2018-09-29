package com.cmig.future.csportal.api.common.controllers;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.servlet.ValidateCodeServlet;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.verify.RSAUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.hand.hap.cache.impl.SysCodeCache;
import com.hand.hap.system.dto.Code;
import com.hand.hap.system.dto.CodeValue;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

@Controller
@ResponseBody
@RequestMapping(value = "${commonPath}/")
public class AppCommonController extends BaseExtendController {

	@Autowired
	private SysCodeCache codeCache;

    /**
     * 此方法描述的是：获取公钥
     * @author:zhangtao107@126.com
     * @param toMailAddr
     * @param subject
     * @param message
     * @return RetApp
     */
    @RequestMapping(value = "/getRsaPublicKey", produces = { "application/json" }, method = RequestMethod.POST)
    public RetApp getRsaPublicKey(String toMailAddr, String subject, String message) {
        RetApp retApp = new RetApp();
        JSONObject jsonObject=new JSONObject();
        try {
            RSAPublicKey publicKey = RSAUtils.getDefaultPublicKey();
            String modulus=new String(Hex.encodeHex(publicKey.getModulus().toByteArray()));
            String exponent=new String(Hex.encodeHex(publicKey.getPublicExponent().toByteArray()));
            jsonObject.put("exponent", exponent);//公钥指数
            jsonObject.put("modulus", modulus);//公钥模数
            retApp.setData(jsonObject);
            retApp.setStatus(OK);
            retApp.setMessage("获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            retApp.setStatus(FAIL);
            retApp.setMessage("获取失败");
        }

        return retApp;
    }


	/**
	 * 验证图片验证码
	 */
	@RequestMapping(value = "/validation/imgCode", produces = {"application/json"}, method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public RetApp imgValidationCode(HttpServletRequest request, HttpServletResponse response) {
		RetApp retApp = new RetApp();
		String validateCode = getParam(request, "validateCode", "");
		try {
			boolean res = ValidateCodeServlet.validateAppImageCode(request, validateCode);
			if (!res) {
				throw new DataWarnningException("图片验证码错误");
			}
		} catch (Exception e) {
			String errMsg = e.getMessage();
			retApp.setStatus(FAIL);
			retApp.setMessage(errMsg);
			return retApp;
		}

		retApp.setData("");
		retApp.setMessage("成功");
		retApp.setStatus(OK);
		return retApp;
	}

	/**
	 * get图片验证码
	 */
	@RequestMapping(value = "/get/imgCode", method = {RequestMethod.POST,RequestMethod.GET})
	public void getImgValidationCode(HttpServletRequest request, HttpServletResponse response) {
		try {
			ValidateCodeServlet validateCodeServlet = new ValidateCodeServlet();
			validateCodeServlet.createAppImage(request, response);
		} catch (Exception e) {
			String errMsg = e.getMessage();
			logger.debug(errMsg);
		}
	}

	@RequestMapping(value = "/get/title/domain/list", method = {RequestMethod.POST,RequestMethod.GET})
	public RetApp getTitleDomainList(HttpServletRequest request, HttpServletResponse response) {
		Code code=codeCache.getValue("CSP.APP.TITLE.DOMAIN.zh_CN");
		if(null!=code){
			List<CodeValue> result=code.getCodeValues();
			JSONArray jsonArray=new JSONArray();
			for(CodeValue dto:result){
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("value",dto.getValue());
				jsonObject.put("meaning",dto.getMeaning());
				jsonArray.add(jsonObject);
			}
			return RetAppUtil.success(jsonArray,"");
		}
		 return RetAppUtil.success("");
	}

	@RequestMapping(value = "/get/app/download/qr", method = {RequestMethod.POST,RequestMethod.GET})
	public RetApp getAppDownloadQr(HttpServletRequest request, HttpServletResponse response){
		String appkey=getParam(request,"appkey","");
		Code code=codeCache.getValue("CSP.APP.DOWNLOAD.QRCODE.zh_CN");
		if(null!=code){
			List<CodeValue> result=code.getCodeValues();
			JSONArray jsonArray=new JSONArray();
			for(CodeValue dto:result){
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("appkey",dto.getValue());
				jsonObject.put("url",dto.getMeaning());
				jsonObject.put("html",dto.getDescription());
				if(StringUtils.isEmpty(appkey)||(!StringUtils.isEmpty(appkey)&&appkey.equals(dto.getValue()))){
					jsonArray.add(jsonObject);
				}
			}
			return RetAppUtil.success(jsonArray,"");
		}
		return RetAppUtil.success("");
	}

}

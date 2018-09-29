package com.cmig.future.csportal.api.app.controllers;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.servlet.ValidateCodeServlet;
import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.SmsUtil;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.verify.RSAUtilsWithKey;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.operate.cms.dto.Article;
import com.cmig.future.csportal.module.operate.cms.service.IArticleService;
import com.cmig.future.csportal.module.pay.components.CspLejiaPayServiceImpl;
import com.cmig.future.csportal.module.pay.service.impl.LejiaPayConfig;
import com.cmig.future.csportal.module.sys.utils.UserTokenUtils;
import com.cmig.future.csportal.module.user.appuser.dto.AppUserCard;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserCardService;
import com.hand.hap.cache.impl.SysCodeCache;
import com.hand.hap.system.dto.Code;
import com.hand.hap.system.dto.CodeValue;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 14:25 2017/4/6.
 * @Modified by zhangtao on 14:25 2017/4/6.
 */
@Controller
@RequestMapping(value = "${userPath}")
@ResponseBody
public class AppUserCardApiController extends BaseExtendController {

    private Logger logger = LoggerFactory.getLogger(AppUserCardApiController.class);

	@Autowired
	private CspLejiaPayServiceImpl cspLejiaPayServiceImpl;

	@Autowired
	private IAppUserCardService appUserCardService;

	@Autowired
	private SysCodeCache codeCache;

	@Autowired
	private IArticleService articleService;

	@RequestMapping(value = "/st/id/info", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp idInfo(HttpServletRequest request, HttpServletResponse response) {
		//必填项,需要验证
		String token = getParam(request, "token", "");
		String appUserId= UserTokenUtils.getUserIdByToken(token);
		//查询用户第一次绑卡的信息做为用户基本信息，不排除已解绑
		AppUserCard dto= appUserCardService.getUserIdInfo(appUserId);
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("reauthFlag", Constants.NO);//是否已实名认证
		jsonObject.put("idType", "");
		jsonObject.put("idNo", "");
		jsonObject.put("name", "");
		if(null!=dto) {
			jsonObject.put("reauthFlag", Constants.YES);
			jsonObject.put("idType", dto.getIdType());
			int length = dto.getIdNo().length();
			String temp = dto.getIdNo().substring(0, dto.getIdNo().length() - 2).replaceAll("\\d", "*");
			String idNoTemp = dto.getIdNo().replaceAll("(\\d{1})\\d{" + (length - 2) + "}(\\d{1})", "$1" + temp + "$2");
			jsonObject.put("idNo", idNoTemp);
			jsonObject.put("name", dto.getName());
		}
		return RetAppUtil.success(jsonObject,"查询成功");
	}

	@RequestMapping(value = "/card/info", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp cardInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//必填项,需要验证
		String cardNo = getParam(request, "cardNo", "");
		checkNull(cardNo);
		//RSA解密
		cardNo=RSAUtilsWithKey.decrypt(cardNo);

		com.alibaba.fastjson.JSONObject jsonObject=cspLejiaPayServiceImpl.queryCardInfo(cardNo,LejiaPayConfig.DEFAULT_MERCHANT_ID);

		if(null!=jsonObject) {
			Map bankMap = new HashMap();
			Code code = codeCache.getValue("PAY.BANK_CODE.zh_CN");
			if (null != code) {
				List<CodeValue> result = code.getCodeValues();
				for (CodeValue dto : result) {
					bankMap.put(dto.getValue(), dto.getMeaning());
				}
			}
			if (!bankMap.containsKey(jsonObject.get("bankCode"))) {
				throw new DataWarnningException("暂不支持【" + jsonObject.get("bankName") + "]，请选择其他银行。");
			}
		}
		return RetAppUtil.success(jsonObject,"查询成功");
	}


	/**
	 * 二次以上绑卡
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/st/card/reauth", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp cardReauth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//必填项,需要验证
		String token = getParam(request, "token", "");
		String cardNo = getParam(request, "cardNo", "");
		String cardType = getParam(request, "cardType", "");
		String bankMobile = getParam(request, "bankMobile", "");
		String bankCode = getParam(request, "bankCode", "");
		String smsValidationCode = getParam(request, "smsValidationCode", "");
		bankMobile=RSAUtilsWithKey.decrypt(bankMobile);
		String telnoKey = SmsUtil.SMS_VALIDATE_CODE + SmsUtil.SMS_CODE_CARD_AUTH+ "." + bankMobile;
		// 校验短信验证码
		SmsUtil.checkSmsCode(bankMobile, telnoKey, smsValidationCode);

		//RSA解密
		cardNo=RSAUtilsWithKey.decrypt(cardNo);

		String appUserId= UserTokenUtils.getUserIdByToken(token);
		//查询用户第一次绑卡的信息做为用户基本信息，不排除已解绑
		AppUserCard defaultAppUserCard= appUserCardService.getUserIdInfo(appUserId);
		String idType = defaultAppUserCard.getIdType();
		String idNo = defaultAppUserCard.getIdNo();
		String name = defaultAppUserCard.getName();
		Map paramMap=new HashMap();
		paramMap.put("appUserId",appUserId);
		paramMap.put("idType",idType);
		paramMap.put("idNo",idNo);
		paramMap.put("cardType",cardType);
		paramMap.put("cardNo",cardNo);
		paramMap.put("name",name);
		paramMap.put("bankMobile",bankMobile);
		paramMap.put("bankCode",bankCode);
		AppUserCard appUserCard=cspLejiaPayServiceImpl.userCardAuth(paramMap,LejiaPayConfig.DEFAULT_MERCHANT_ID);

		JSONObject jsonObject=new JSONObject();
		jsonObject.put("cardId",appUserCard.getCardId());
		return RetAppUtil.success(jsonObject,"绑卡成功");
	}

	@RequestMapping(value = "/st/card/auth", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp cardAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//必填项,需要验证
		String token = getParam(request, "token", "");
		String idType = getParam(request, "idType", "");
		String idNo = getParam(request, "idNo", "");
		String name = getParam(request, "name", "");
		String cardNo = getParam(request, "cardNo", "");
		String cardType = getParam(request, "cardType", "");
		String bankMobile = getParam(request, "bankMobile", "");
		String bankCode = getParam(request, "bankCode", "");
		String smsValidationCode = getParam(request, "smsValidationCode", "");
		bankMobile=RSAUtilsWithKey.decrypt(bankMobile);
		String telnoKey = SmsUtil.SMS_VALIDATE_CODE + SmsUtil.SMS_CODE_CARD_AUTH+ "." + bankMobile;
		// 校验短信验证码
		SmsUtil.checkSmsCode(bankMobile, telnoKey, smsValidationCode);

		//RSA解密
		name=RSAUtilsWithKey.decrypt(name);
		cardNo=RSAUtilsWithKey.decrypt(cardNo);
		idNo=RSAUtilsWithKey.decrypt(idNo);

		String appUserId= UserTokenUtils.getUserIdByToken(token);
		//查询用户第一次绑卡的信息做为用户基本信息，不排除已解绑
		AppUserCard defaultAppUserCard= appUserCardService.getUserIdInfo(appUserId);
		if(null!=defaultAppUserCard&&!defaultAppUserCard.getIdNo().equals(idNo)){
			throw new DataWarnningException("只能绑定自己的银行卡");
		}

		Map paramMap=new HashMap();
		paramMap.put("appUserId",appUserId);
		paramMap.put("idType",idType);
		paramMap.put("idNo",idNo);
		paramMap.put("cardType",cardType);
		paramMap.put("cardNo",cardNo);
		paramMap.put("name",name);
		paramMap.put("bankMobile",bankMobile);
		paramMap.put("bankCode",bankCode);
		AppUserCard appUserCard=cspLejiaPayServiceImpl.userCardAuth(paramMap,LejiaPayConfig.DEFAULT_MERCHANT_ID);

		JSONObject jsonObject=new JSONObject();
		jsonObject.put("cardId",appUserCard.getCardId());
		return RetAppUtil.success(jsonObject,"绑卡成功");
	}

	@RequestMapping(value = "/st/card/list", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp cardList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//必填项,需要验证
		String token = getParam(request, "token", "");
		String appUserId= UserTokenUtils.getUserIdByToken(token);
		AppUserCard appUserCard=new AppUserCard();
		appUserCard.setAppUserId(appUserId);
		appUserCard.setStatus(LejiaPayConfig.CARD_STATUS_Y);
		List<AppUserCard> list=appUserCardService.find(appUserCard);
		JSONArray jsonArray=new JSONArray();
		if(null!=list&&list.size()>0){
			Map bankMap=new HashMap();
			Code code=codeCache.getValue("PAY.BANK_CODE.zh_CN");
			if(null!=code){
				List<CodeValue> result=code.getCodeValues();
				for(CodeValue dto:result){
					bankMap.put(dto.getValue(),dto.getMeaning());
				}
			}

			for(AppUserCard dto:list){
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("cardId",dto.getCardId());
				jsonObject.put("idType",dto.getIdType());

				int length=dto.getIdNo().length();
				String temp=dto.getIdNo().substring(0,dto.getIdNo().length()-2).replaceAll("\\d","*");
				String idNoTemp=dto.getIdNo().replaceAll("(\\d{1})\\d{"+(length-2)+"}(\\d{1})","$1"+temp+"$2");
				jsonObject.put("idNo",idNoTemp);
				jsonObject.put("cardType",dto.getCardType());

				String cardNo=dto.getCardNo();
				cardNo=RSAUtilsWithKey.decrypt(cardNo);
				length=cardNo.length();
				temp=cardNo.substring(0,cardNo.length()-4).replaceAll("\\d","*");
				String cardNoTemp=cardNo.replaceAll("\\d{"+(length-4)+"}(\\d{4})",temp+"$1");
				jsonObject.put("cardNo",cardNoTemp);
				jsonObject.put("bankCode",dto.getBankCode()==null?"":dto.getBankCode().toString());
				jsonObject.put("bankName",bankMap.get(jsonObject.get("bankCode").toString()));
				jsonObject.put("name",dto.getName());
				String bankMobileTemp=dto.getBankMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
				jsonObject.put("bankMobile",bankMobileTemp);
				jsonObject.put("defaultFalg",dto.getDefaultFlag());
				jsonObject.put("orderSq",dto.getOrderSq()==null?"":dto.getOrderSq());
				jsonArray.add(jsonObject);
			}
		}
		return RetAppUtil.success(jsonArray,"绑卡成功");
	}

	/**
	 * 切换默认银行卡
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/st/card/default", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp cardDefault(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//必填项,需要验证
		String token = getParam(request, "token", "");
		String cardId = getParam(request, "cardId", "");
		String appUserId= UserTokenUtils.getUserIdByToken(token);
		appUserCardService.setDefaultCard(appUserId,cardId);
		return RetAppUtil.success("切换成功");
	}

	/**
	 * 查询银行卡支持清单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/bank/list", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp bankList(HttpServletRequest request, HttpServletResponse response) {
		Code code=codeCache.getValue("PAY.BANK_CODE.zh_CN");
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

	/**
	 * 绑卡服务协议
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/operate/cms/article/getCardProtocol", produces = {"application/json" }, method = RequestMethod.POST)
	@ResponseBody
	public RetApp getCardProtocol(HttpServletRequest request, HttpServletResponse response) {
		List<Article> list = articleService.getCardProtocol();
		JSONArray jsonArray=new JSONArray();
		if(null!=list&&list.size()>0){
			for(Article article:list){
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("categoryName", article.getCategoryName());
				jsonObject.put("title", article.getTitle());
				jsonObject.put("content", article.getContent());
				jsonArray.add(jsonObject);
			}
		}
		return RetAppUtil.success(jsonArray,"查询成功!");
	}

	@RequestMapping(value = "/st/card/unbundled", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp userCardUnbundled(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//必填项,需要验证
		String cardId = getParam(request, "cardId", "");
		String token = getParam(request, "token", "");
		String smsValidationCode = getParam(request, "smsValidationCode", "");
		if(StringUtils.isEmpty(cardId)){
			throw new DataWarnningException("参数不能为空");
		}

		if(StringUtils.isEmpty(smsValidationCode)){
			throw new DataWarnningException("短信验证码不能为空");
		}

		AppUserCard appUserCard=appUserCardService.selectByPrimaryKey(new Long(cardId));
		if(null==appUserCard||appUserCard.getBankMobile().isEmpty()){
			throw new DataWarnningException("参数错误");
		}

		String telnoKey = SmsUtil.SMS_VALIDATE_CODE + SmsUtil.SMS_CODE_CARD_UNBUNDLED+ "." + appUserCard.getBankMobile();
		// 校验短信验证码
		SmsUtil.checkSmsCode(appUserCard.getBankMobile(), telnoKey, smsValidationCode);

		String appUserId= UserTokenUtils.getUserIdByToken(token);
		cspLejiaPayServiceImpl.userCardUnbundled(cardId,appUserId);
		return RetAppUtil.success("解绑成功");
	}

	/**
	 * 解绑发短信验证码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/card/unbundled/smsCode", produces = {"application/json"}, method = RequestMethod.POST)
	@ResponseBody
	public RetApp cardUnbundledSmsCode(HttpServletRequest request, HttpServletResponse response) {
		String cardId = getParam(request, "cardId", "");
		String validateCode = getParam(request, "validateCode", "");
		String ip=getRemoteid(request);
		if(StringUtils.isEmpty(cardId)||StringUtils.isEmpty(validateCode)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}

		boolean res = ValidateCodeServlet.validateAppImageCode(request, validateCode);
		if(!res){
			throw new ServiceException(RestApiExceptionEnums.IMAGE_CODE_VALIDATION_ERROR);
		}

		AppUserCard appUserCard=appUserCardService.selectByPrimaryKey(new Long(cardId));
		if(null==appUserCard||appUserCard.getBankMobile().isEmpty()){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR);
		}

		//已经解绑的卡不能重复发短信验证码
		if(LejiaPayConfig.CARD_STATUS_N.equals(appUserCard.getStatus())){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR);
		}


		SmsUtil.sendSmsCode(ip,SmsUtil.SMS_CODE_CARD_UNBUNDLED,appUserCard.getBankMobile());
		String bankMobileTemp=appUserCard.getBankMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
		return RetAppUtil.success("短信验证码已发送到"+bankMobileTemp+"上");
	}
}

package com.cmig.future.csportal.module.invoice.service;

import com.baiwang.bop.client.IBopClient;
import com.baiwang.bop.client.ILoginClient;
import com.baiwang.bop.client.impl.BopRestClient;
import com.baiwang.bop.client.impl.PostLogin;
import com.baiwang.bop.request.impl.LoginRequest;
import com.baiwang.bop.request.impl.invoice.common.InvoiceDetails;
import com.baiwang.bop.request.impl.invoice.impl.FormatfileBuildRequest;
import com.baiwang.bop.request.impl.invoice.impl.InvoiceOpenRequest;
import com.baiwang.bop.respose.entity.FormatfileBuildResponse;
import com.baiwang.bop.respose.entity.InvoiceOpenResponse;
import com.baiwang.bop.respose.entity.LoginResponse;
import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.cmig.future.csportal.module.invoice.InvoiceHelper.*;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 17:34 2018/3/9.
 * @Modified by zhangtao on 17:34 2018/3/9.
 */
@Service
public class InvoiceService {

	public String getToken() {
		String token;
		token= JedisUtils.get(INVOICE_BAIWANG_TOKEN_KEY);
		if(!StringUtils.isEmpty(token)){
			return token;
		}
		ILoginClient loginClient = new PostLogin(serverUrl);
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setAppkey(appKey);
		loginRequest.setAppSecret(appSecret);
		loginRequest.setUserName(username);
		loginRequest.setPasswordMd5(password);
		loginRequest.setUserSalt(userSalt);
		LoginResponse loginResponse = loginClient.login(loginRequest);
		int expiresin=loginResponse.getExpires_in().intValue();
		token = loginResponse.getAccess_token();//记录
		JedisUtils.set(INVOICE_BAIWANG_TOKEN_KEY,token,expiresin-120);
		return token;
	}

	/**
	 * 开票
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public InvoiceOpenResponse openRequest(InvoiceOpenRequest request) throws Exception {
		String token = getToken();
		IBopClient client = new BopRestClient(serverUrl, appKey, appSecret);
		InvoiceOpenResponse response = client.execute(request, token, InvoiceOpenResponse.class);
		return response;
	}

	/**
	 * 版式打印
	 * @return
	 * @throws Exception
	 */
	public FormatfileBuildResponse formatFileBulid(FormatfileBuildRequest request)throws Exception{
		/**
		 * 1、登录授权获取Token
		 * 2、组装请求类
		 * 3、发送请求获取返回值
		 */
		String token = getToken();
		//同步请求
		IBopClient client = new BopRestClient(serverUrl, appKey, appSecret);
		FormatfileBuildResponse response =client.execute(request, token, FormatfileBuildResponse.class);
		return response;
	}


	public static void main(String[] args) {
		InvoiceService invoiceService = new InvoiceService();
		InvoiceOpenRequest request = new InvoiceOpenRequest();
		request.setSellerTaxNo("91500000747150532A");
		request.setDeviceType("0");
		request.setOrganizationCode("");
		request.setSerialNo(IdGen.uuid());
		request.setInvoiceSpecialMark("00");
		request.setInvoiceTypeCode("026");
		request.setInvoiceTerminalCode("532dzp");
		request.setBuyerTaxNo("");
		request.setBuyerName("百望股份有限公司");
		request.setBuyerAddressPhone("");
		request.setBuyerBankAccount("");
		request.setDrawer("wwm");
		request.setChecker("");
		request.setPayee("");
		request.setInvoiceType("0");
		request.setInvoiceListMark("0");
		request.setRedInfoNo("");
		request.setOriginalInvoiceCode("");
		request.setOriginalInvoiceNo("");
		request.setTaxationMode("0");
		request.setDeductibleAmount("");
		request.setInvoiceTotalPrice("200.00");
		request.setInvoiceTotalTax("34.00");
		request.setInvoiceTotalPriceTax("234.00");
		request.setSignatureParameter("0000004282000000");
		request.setTaxDiskNo("");
		request.setTaxDiskKey("");
		request.setTaxDiskPassword("");
		request.setGoodsCodeVersion("");
		request.setConsolidatedTaxRate("");
		request.setNotificationNo("");
		request.setRemarks("备注");

		List<InvoiceDetails> invoiceDetailsList = new ArrayList<>();
		InvoiceDetails invoiceDetails = new InvoiceDetails();
		invoiceDetails.setGoodsLineNo("1");
		invoiceDetails.setGoodsLineNature("0");
		invoiceDetails.setGoodsCode("1020101000000000000");
		invoiceDetails.setGoodsExtendCode("");
		invoiceDetails.setGoodsName("商品名称1");
		invoiceDetails.setGoodsTaxItem("");
		invoiceDetails.setGoodsSpecification("");
		invoiceDetails.setGoodsUnit("");
		invoiceDetails.setGoodsQuantity("");
		invoiceDetails.setGoodsPrice("");
		invoiceDetails.setGoodsTotalPrice("200.00");
		invoiceDetails.setGoodsTotalTax("34.00");
		invoiceDetails.setGoodsTaxRate("0.17");
		invoiceDetails.setGoodsDiscountLineNo("");
		invoiceDetails.setPriceTaxMark("0");
		invoiceDetails.setVatSpecialManagement("");
		invoiceDetails.setFreeTaxMark("");
		invoiceDetails.setPreferentialMark("0");
		invoiceDetailsList.add(invoiceDetails);
		request.setInvoiceDetailsList(invoiceDetailsList);

		try {
			InvoiceOpenResponse invoiceOpenResponse=invoiceService.openRequest(request);
			FormatfileBuildRequest formatRequest=new FormatfileBuildRequest();
			//纳税人识别号
			formatRequest.setSellerTaxNo(request.getSellerTaxNo());
			//开票流水号；流水号和发票号码代码二选一必填
			formatRequest.setSerialNo(invoiceOpenResponse.getInvoiceNo());
			//发票代码
			formatRequest.setInvoiceCode(invoiceOpenResponse.getInvoiceCode());
			//发票号码
			formatRequest.setInvoiceNo(invoiceOpenResponse.getInvoiceNo());
			//推送标志 0：不推送 1：推送（邮箱电话必填一个）
			formatRequest.setPushType("0");
			//推送至第三方渠道
			formatRequest.setPushChannel("");
			//	购方客户邮箱
			formatRequest.setBuyerEmail("");
			//购方客户电话
			formatRequest.setBuyerPhone("18210225831");
			invoiceService.formatFileBulid(formatRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

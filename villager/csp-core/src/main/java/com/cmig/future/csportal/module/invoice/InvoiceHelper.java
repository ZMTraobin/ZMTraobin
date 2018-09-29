package com.cmig.future.csportal.module.invoice;

import com.cmig.future.csportal.common.config.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 10:10 2018/3/9.
 * @Modified by zhangtao on 10:10 2018/3/9.
 */
public class InvoiceHelper {

	private static final Logger log = LoggerFactory.getLogger(InvoiceHelper.class);
	public static String appKey = "10000005";
	public static String appSecret = "b65025d0-19d2-4841-88f4-ff4439b8da58";
	public static String username = "admin_1800000021168";
	public static String password = "a123456";
	public static String userSalt = "94db610c5c3049df8da3e9ac91390015";
	public static String serverUrl = "http://60.205.83.27/router/rest";

	public static String INVOICE_BAIWANG_TOKEN_KEY="CSP.INVOICE.BAIWANG.TOKEN";

	static{
		appKey= Global.getConfig("INVOICE.BAIWANG.appKey");
		appSecret= Global.getConfig("INVOICE.BAIWANG.appSecret");
		username= Global.getConfig("INVOICE.BAIWANG.username");
		password= Global.getConfig("INVOICE.BAIWANG.password");
		userSalt= Global.getConfig("INVOICE.BAIWANG.userSalt");
		serverUrl= Global.getConfig("INVOICE.BAIWANG.serverUrl");
	}


}

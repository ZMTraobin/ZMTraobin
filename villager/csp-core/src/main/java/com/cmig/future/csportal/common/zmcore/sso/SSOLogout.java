package com.cmig.future.csportal.common.zmcore.sso;

/**
 * Created by zhangtao107@126.com on 2016/8/24.
 */

public class SSOLogout {

    public static void main(String[] args) {
//		 String logoutRequest = "<samlp:LogoutRequest xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\" ID=\""
//		            + "LR-12345"
//		            + "\" Version=\"2.0\" IssueInstant=\"" + "20150101"
//		            + "\"><saml:NameID xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\">@NOT_USED@</saml:NameID><samlp:SessionIndex>"
//		            + "LT-67890" + "</samlp:SessionIndex></samlp:LogoutRequest>";
//		  Pattern pattern = Pattern.compile("<samlp:SessionIndex>(.*)<\\/samlp:SessionIndex>");
//		  Matcher matcher = pattern.matcher(logoutRequest);
//		  while(matcher.find()){
//			  System.out.println(matcher.group(1));
//		  }

        String userCode = "shix3";
        System.out.println(userCode.matches("[a-zA-Z][a-zA-Z0-9_]{3,19}"));
//
        String passwd = "11111";
        System.out.println(passwd.matches("^(?![0-9]+$)(?![a-zA-Z]+$)(?![\\S&&\\W]+$)[\\S]{6,20}$"));
//
//		System.out.println((new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")).format(new Date()));

    }

}
